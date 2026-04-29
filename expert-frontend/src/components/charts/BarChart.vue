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
  horizontal?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  title: '',
  height: '280px',
  colors: () => ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399'],
  horizontal: false
})

const emit = defineEmits<{ (e: 'click', code: string): void }>()

const baseChartRef = ref()

const chartOption = computed(() => {
  const isHorizontal = props.horizontal

  return {
    title: {
      text: props.title,
      left: 'center',
      textStyle: { fontSize: 14, fontWeight: 'normal' }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '20%',
      containLabel: true
    },
    xAxis: {
      type: isHorizontal ? 'value' : 'category',
      data: isHorizontal ? undefined : props.data.map(item => item.name),
      axisLabel: { interval: 0, rotate: isHorizontal ? 0 : 15 }
    },
    yAxis: {
      type: isHorizontal ? 'category' : 'value',
      data: isHorizontal ? props.data.map(item => item.name) : undefined
    },
    series: [{
      type: 'bar',
      barWidth: '50%',
      data: props.data.map((item, index) => ({
        value: item.count,
        code: item.code,
        itemStyle: { color: props.colors[index % props.colors.length] }
      })),
      label: {
        show: true,
        position: isHorizontal ? 'right' : 'top',
        formatter: '{c}'
      }
    }]
  }
})

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