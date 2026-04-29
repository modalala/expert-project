<template>
  <div ref="chartRef" :style="{ width: width, height: height }"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts'

interface Props {
  width?: string
  height?: string
  option: any
}

const props = withDefaults(defineProps<Props>(), {
  width: '100%',
  height: '300px'
})

const chartRef = ref<HTMLDivElement>()
let chartInstance: echarts.ECharts | null = null

const initChart = () => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value)
    chartInstance.setOption(props.option)
  }
}

const resizeChart = () => {
  chartInstance?.resize()
}

watch(() => props.option, (newOption) => {
  nextTick(() => {
    chartInstance?.setOption(newOption, true)
  })
}, { deep: true })

onMounted(() => {
  initChart()
  window.addEventListener('resize', resizeChart)
})

onUnmounted(() => {
  chartInstance?.dispose()
  window.removeEventListener('resize', resizeChart)
})

defineExpose({
  resize: resizeChart,
  clear: () => chartInstance?.clear(),
  setOption: (option: echarts.EChartsOption) => chartInstance?.setOption(option),
  getInstance: () => chartInstance
})
</script>