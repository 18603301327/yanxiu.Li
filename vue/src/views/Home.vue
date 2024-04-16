<template>
  <div>
    <el-row :gutter="24" style="margin-top: 10px">
      <el-col :span="24">
        <el-card style="width: 100%">
          <p>
            您好，{{user.nickname}}，欢迎使用本系统
          </p>
        </el-card>
      </el-col>
    </el-row>

<!--      <el-row>-->
<!--      <el-col :span="24">-->
<!--        <el-card style="margin: 10px 0">-->
<!--          <div id="main" style="width: 100%; height: 450px"></div>-->
<!--        </el-card>-->
<!--      </el-col>-->

<!--      <el-col :span="24">-->
<!--        <el-card style="margin: 10px 0">-->
<!--          <div id="pie" style="width: 100%; height: 450px"></div>-->
<!--        </el-card>-->
<!--      </el-col>-->
<!--    </el-row>-->

  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  mounted() {  // 页面元素渲染之后再触发
    const option = {
      title: {
        text: '每月销售额统计',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      xAxis: {
        type: 'category',
        data: []
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'bar'
        },
      ]
    };

    // 饼图

    const pieOption = {
      title: {
        text: '各门票类别数量统计',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '55%',
          center: ['50%', '50%'],
          data: [],  // 填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        },
      ],
    };


    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    this.request.get("/echarts/echarts1").then(res => {
      // 填空
      option.xAxis.data = res.data.x
      option.series[0].data = res.data.y
      // 数据准备完毕之后再set
      myChart.setOption(option);
    })

    this.request.get("/echarts/echarts2").then(res => {
      pieOption.series[0].data = res.data
      pieChart.setOption(pieOption)
    })
  }
}
</script>

<style scoped>

</style>
