<template>
  <BaseChart :option="chartOption" :height="height" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import BaseChart from './BaseChart.vue'

interface TrendItem {
  month: string
  monthLabel: string
  extractionCount: number
  bidCount: number
}

interface Props {
  data: TrendItem[]
  title?: string
  height?: string
  seriesConfig?: { name: string; field: 'extractionCount' | 'bidCount'; color: string }[]
}

const props = withDefaults(defineProps<Props>(), {
  title: '',
  height: '280px',
  seriesConfig: () => [
    { name: '抽取次数', field: 'extractionCount', color: '#409EFF' },
    { name: '评标次数', field: 'bidCount', color: '#67C23A' }
  ]
})

const chartOption = computed(() => ({
  title: {
    text: props.title,
    left: 'center',
    textStyle: { fontSize: 14, fontWeight: 'normal' }
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'cross' }
  },
  legend: {
    data: props.seriesConfig.map(s => s.name),
    bottom: '5%'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '15%',
    top: '20%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: props.data.map(item => item.monthLabel)
  },
  yAxis: {
    type: 'value',
    minInterval: 1
  },
  series: props.seriesConfig.map(config => ({
    name: config.name,
    type: 'line',
    smooth: true,
    symbol: 'circle',
    symbolSize: 8,
    lineStyle: { color: config.color },
    itemStyle: { color: config.color },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: config.color + '40' },
          { offset: 1, color: config.color + '05' }
        ]
      }
    },
    data: props.data.map(item => item[config.field])
  }))
}))
</script>