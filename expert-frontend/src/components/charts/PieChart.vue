<template>
  <BaseChart :option="chartOption" :height="height" ref="baseChartRef" />
</template>

<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import BaseChart from './BaseChart.vue'

interface DistributionItem {
  code: string
  name: string
  count: number
  percentage: number
}

interface Props {
  data: DistributionItem[]
  title?: string
  height?: string
  colors?: string[]
}

const props = withDefaults(defineProps<Props>(), {
  title: '',
  height: '280px',
  colors: () => ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
})

const emit = defineEmits<{ (e: 'click', code: string): void }>()

const baseChartRef = ref()

const chartOption = computed(() => ({
  title: {
    text: props.title,
    left: 'center',
    textStyle: { fontSize: 14, fontWeight: 'normal' }
  },
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'horizontal',
    bottom: '5%',
    left: 'center'
  },
  series: [{
    type: 'pie',
    radius: ['35%', '60%'],
    center: ['50%', '45%'],
    avoidLabelOverlap: true,
    itemStyle: {
      borderRadius: 6,
      borderColor: '#fff',
      borderWidth: 2
    },
    label: {
      show: false
    },
    emphasis: {
      label: {
        show: true,
        fontSize: 14,
        fontWeight: 'bold'
      }
    },
    data: props.data.map((item, index) => ({
      name: item.name,
      value: item.count,
      code: item.code,
      itemStyle: { color: props.colors[index % props.colors.length] }
    }))
  }]
}))

onMounted(() => {
  const instance = baseChartRef.value?.getInstance()
  if (instance) {
    instance.on('click', (params: any) => {
      if (params.data?.code) {
        emit('click', params.data.code)
      }
    })
  }
})
</script>