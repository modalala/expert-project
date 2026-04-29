<template>
  <div class="role-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="showCreateDialog">新建角色</el-button>
        </div>
      </template>

      <!-- Role List Table -->
      <el-table :data="roleList" v-loading="loading" border stripe>
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="showEditDialog(row)">编辑</el-button>
            <el-button type="warning" size="small" @click="showPermissionDialog(row)">权限</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Create/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑角色' : '新建角色'" width="500px">
      <el-form :model="roleForm" :rules="roleRules" ref="roleRef" label-width="100px">
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="roleForm.description" type="textarea" :rows="3" placeholder="请输入角色描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="roleForm.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRole">确定</el-button>
      </template>
    </el-dialog>

    <!-- Permission Dialog -->
    <el-dialog v-model="permissionVisible" title="权限配置" width="600px">
      <el-tree
        ref="permissionTree"
        :data="permissionTreeData"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedPermissions"
        :props="{ label: 'permName', children: 'children' }"
      />
      <template #footer>
        <el-button @click="permissionVisible = false">取消</el-button>
        <el-button type="primary" @click="savePermissions">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import api from '@/api'

interface Role {
  id: number
  roleCode: string
  roleName: string
  description: string
  status: number
  permissionIds: number[]
}

interface Permission {
  id: number
  permCode: string
  permName: string
  permType: number
  parentId: number
  path: string
  children?: Permission[]
}

const loading = ref(false)
const roleList = ref<Role[]>([])
const permissionTreeData = ref<Permission[]>([])

const dialogVisible = ref(false)
const isEdit = ref(false)
const roleRef = ref<FormInstance>()
const currentRoleId = ref<number | null>(null)

const roleForm = reactive({
  roleCode: '',
  roleName: '',
  description: '',
  status: 1
})

const roleRules: FormRules = {
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }]
}

const permissionVisible = ref(false)
const permissionTree = ref<any>()
const checkedPermissions = ref<number[]>([])

const loadRoleList = async () => {
  loading.value = true
  try {
    const res = await api.get('/role/list')
    if (res.code === 200) {
      roleList.value = res.data
    }
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const loadPermissions = async () => {
  permissionTreeData.value = [
    { id: 1, permName: '系统管理', children: [
      { id: 11, permName: '用户管理' },
      { id: 12, permName: '角色管理' },
      { id: 13, permName: '权限管理' }
    ]},
    { id: 2, permName: '专家管理', children: [
      { id: 21, permName: '专家注册' },
      { id: 22, permName: '专家初审' },
      { id: 23, permName: '专家复审' },
      { id: 24, permName: '专家主数据' }
    ]},
    { id: 3, permName: '抽取管理', children: [
      { id: 31, permName: '采购方案单' },
      { id: 32, permName: '抽取方案配置' },
      { id: 33, permName: '专家确认' }
    ]},
    { id: 4, permName: '评标管理', children: [
      { id: 41, permName: '评标委员会' },
      { id: 42, permName: '专家评分' }
    ]}
  ]
}

const showCreateDialog = () => {
  isEdit.value = false
  currentRoleId.value = null
  Object.assign(roleForm, { roleCode: '', roleName: '', description: '', status: 1 })
  dialogVisible.value = true
}

const showEditDialog = (row: Role) => {
  isEdit.value = true
  currentRoleId.value = row.id
  Object.assign(roleForm, row)
  dialogVisible.value = true
}

const submitRole = async () => {
  const valid = await roleRef.value?.validate()
  if (!valid) return

  try {
    if (isEdit.value && currentRoleId.value) {
      const res = await api.put(`/role/${currentRoleId.value}`, roleForm)
      if (res.code === 200) {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        loadRoleList()
      }
    } else {
      const res = await api.post('/role', roleForm)
      if (res.code === 200) {
        ElMessage.success('创建成功')
        dialogVisible.value = false
        loadRoleList()
      }
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const showPermissionDialog = async (row: Role) => {
  currentRoleId.value = row.id
  checkedPermissions.value = row.permissionIds || []
  await loadPermissions()
  permissionVisible.value = true
}

const savePermissions = async () => {
  if (!currentRoleId.value) return
  const checkedIds = permissionTree.value?.getCheckedKeys() || []
  try {
    await api.put(`/role/${currentRoleId.value}/permissions`, { permissionIds: checkedIds })
    ElMessage.success('权限保存成功')
    permissionVisible.value = false
    loadRoleList()
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

const handleDelete = async (row: Role) => {
  await ElMessageBox.confirm('确定删除该角色？', '提示', { type: 'warning' })
  try {
    await api.delete(`/role/${row.id}`)
    ElMessage.success('删除成功')
    loadRoleList()
  } catch (e) {
    ElMessage.error('删除失败')
  }
}

onMounted(() => {
  loadRoleList()
})
</script>

<style scoped>
.role-container { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>