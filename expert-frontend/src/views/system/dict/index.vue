<template>
  <div class="dict-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据字典管理</span>
          <el-button type="primary" @click="showAddDict">新增字典</el-button>
        </div>
      </template>

      <el-table :data="dictList" v-loading="loading" border>
        <el-table-column prop="dictCode" label="字典编码" width="150" />
        <el-table-column prop="dictName" label="字典名称" width="150" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="showDictItems(row)">
              字典项
            </el-button>
            <el-button type="danger" size="small" @click="deleteDict(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Add Dictionary Dialog -->
    <el-dialog v-model="addDictVisible" title="新增字典" width="400px">
      <el-form :model="dictForm" label-width="100px">
        <el-form-item label="字典编码">
          <el-input v-model="dictForm.dictCode" placeholder="如：EXPERT_TYPE" />
        </el-form-item>
        <el-form-item label="字典名称">
          <el-input v-model="dictForm.dictName" placeholder="如：专家类型" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="dictForm.description" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDictVisible = false">取消</el-button>
        <el-button type="primary" @click="createDict">创建</el-button>
      </template>
    </el-dialog>

    <!-- Dict Items Dialog -->
    <el-dialog v-model="itemsVisible" title="字典项管理" width="600px">
      <div class="items-header">
        <span>{{ currentDict?.dictName }} ({{ currentDict?.dictCode }})</span>
        <el-button type="primary" size="small" @click="showAddItem">新增字典项</el-button>
      </div>
      <el-table :data="itemList" v-loading="itemsLoading" border>
        <el-table-column prop="itemCode" label="项编码" width="120" />
        <el-table-column prop="itemName" label="项名称" width="150" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="danger" size="small" @click="deleteItem(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- Add Item Dialog -->
    <el-dialog v-model="addItemVisible" title="新增字典项" width="400px">
      <el-form :model="itemForm" label-width="100px">
        <el-form-item label="项编码">
          <el-input v-model="itemForm.itemCode" placeholder="如：TECH" />
        </el-form-item>
        <el-form-item label="项名称">
          <el-input v-model="itemForm.itemName" placeholder="如：技术类" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="itemForm.sortOrder" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addItemVisible = false">取消</el-button>
        <el-button type="primary" @click="createItem">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

interface Dict {
  id: number
  dictCode: string
  dictName: string
  description: string
  status: number
}

interface DictItem {
  id: number
  dictCode: string
  itemCode: string
  itemName: string
  sortOrder: number
}

const loading = ref(false)
const dictList = ref<Dict[]>([])
const addDictVisible = ref(false)
const dictForm = ref({ dictCode: '', dictName: '', description: '' })

const itemsVisible = ref(false)
const itemsLoading = ref(false)
const itemList = ref<DictItem[]>([])
const currentDict = ref<Dict | null>(null)

const addItemVisible = ref(false)
const itemForm = ref({ dictCode: '', itemCode: '', itemName: '', sortOrder: 0 })

const loadDictList = async () => {
  loading.value = true
  try {
    const res = await api.get('/dict/list')
    if (res.code === 200) {
      dictList.value = res.data.records
    }
  } finally {
    loading.value = false
  }
}

const showAddDict = () => {
  dictForm.value = { dictCode: '', dictName: '', description: '' }
  addDictVisible.value = true
}

const createDict = async () => {
  try {
    const res = await api.post('/dict', dictForm.value)
    if (res.code === 200) {
      ElMessage.success('字典创建成功')
      addDictVisible.value = false
      loadDictList()
    } else {
      ElMessage.error(res.message)
    }
  } catch (e) {
    ElMessage.error('创建字典失败')
  }
}

const deleteDict = async (row: Dict) => {
  await ElMessageBox.confirm('确定删除该字典及其所有字典项？', '提示')
  try {
    const res = await api.delete(`/dict/${row.id}`)
    if (res.code === 200) {
      ElMessage.success('字典已删除')
      loadDictList()
    }
  } catch (e) {
    ElMessage.error('删除字典失败')
  }
}

const showDictItems = async (row: Dict) => {
  currentDict.value = row
  itemsVisible.value = true
  itemsLoading.value = true
  try {
    const res = await api.get(`/dict/${row.dictCode}/items`)
    if (res.code === 200) {
      itemList.value = res.data
    }
  } finally {
    itemsLoading.value = false
  }
}

const showAddItem = () => {
  itemForm.value = { dictCode: currentDict.value?.dictCode || '', itemCode: '', itemName: '', sortOrder: 0 }
  addItemVisible.value = true
}

const createItem = async () => {
  try {
    const res = await api.post('/dict/item', itemForm.value)
    if (res.code === 200) {
      ElMessage.success('字典项创建成功')
      addItemVisible.value = false
      showDictItems(currentDict.value!)
    } else {
      ElMessage.error(res.message)
    }
  } catch (e) {
    ElMessage.error('创建字典项失败')
  }
}

const deleteItem = async (row: DictItem) => {
  await ElMessageBox.confirm('确定删除该字典项？', '提示')
  try {
    const res = await api.delete(`/dict/item/${row.id}`)
    if (res.code === 200) {
      ElMessage.success('字典项已删除')
      showDictItems(currentDict.value!)
    }
  } catch (e) {
    ElMessage.error('删除字典项失败')
  }
}

onMounted(() => {
  loadDictList()
})
</script>

<style scoped>
.dict-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.items-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}
</style>