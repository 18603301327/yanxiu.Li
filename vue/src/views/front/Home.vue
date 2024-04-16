<template>
  <div>
    <div>
      <el-carousel height="250px" :interval="5000">
        <el-carousel-item v-for="item in lun" :key="item.id">
          <a :href="item.link" target="_blank"><img :src="item.img" alt="" style="width:100%"></a>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style=" width: 85%; margin: 10px auto;">
      <div style="display: flex">
        <div style="width: 40%;">
          <img src="../../assets/6.jpg" alt="" style="width: 100%">
        </div>
        <div style="width:60%; margin-left: 20px; margin-top: 10px">
          唐山市，位于河北东部、华北平原东北部，南临渤海，北依燕山，是河北省辖的地级市。它地处华北与东北通道的咽喉要地，
          唐山因唐太宗李世民东征高句丽驻跸而得名，素有“北方瓷都”之称，是省域副中心城市，也是中国(唐山)跨境电子商务综合试验区和中国(河北)自由贸易试验区的组成部分。
          此地还是中国第一座机械化采煤矿井、第一条标准轨距铁路、第一台蒸汽机车、第一桶机制水泥的诞生地。
          同时，唐山也是中国评剧的发源地，素有“冀东三支花”之称的皮影、评剧、乐亭大鼓，为国家级非物质文化遗产。
          唐山市地处渤海湾中心地带，拥有丰富多样的自然风光和历史古迹。
          让我们一起走进唐山，探索其独特之美。
        </div>
      </div>
    </div>






  <div style="text-align: center; color: black; font-weight: bold; font-size: 24px; ">景区票务系统</div>
  <div style="margin: 10px 90px">
    <div style="margin: 20px 0; border-bottom: 1px solid #ddd; padding-bottom: 10px">
      <el-button v-for="item in categories" :key="item.id" :class="{'active': item.name === active}" @click="load(item.name)">{{ item.name }}</el-button>
    </div>
    <el-row :gutter="10" style="min-height: 280px">
      <el-col :span="6" v-for="item in goods" :key="item.id" style=" margin-bottom: 10px">
        <div style="background-color: white; padding: 10px; cursor: pointer" @click="$router.push('/front/detail?id=' + item.id)">
          <img :src="item.img" alt="" style="width: 90%; height: 200px; overflow: hidden; border-radius: 10px">
          <div style="color: #666;margin: 10px 0" class="line1">{{ item.name }}</div>
          <div style="margin: 10px 0; display: flex">
            <div style="color: red; font-size: 14px; flex: 20; font-weight: bold">
              <span>价格：{{ item.price }}</span>
            </div>
            <div style="flex: 5; font-size: 14px; color: #666">
              <img src="@/assets/点赞.png" style="width: 16px" alt=""><span style="position: relative; top: -2px">{{ item.praiseCount }}</span>
              <img src="@/assets/收藏.png" style="width: 16px;" alt=""><span style="position: relative; top: -2px">{{ item.collectCount }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <div style="padding: 10px 0; background-color: white">
      <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[4, 8, 12]"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>
  </div>



  </div>
</template>

<script>
export default {
  name: "FrontHome",
  data() {
    return {
      lun: [],
      files: [],
      goods: [],
      tops: [],
      recommends: [],
      categories: [],
      pageNum: 1,
      pageSize: 8,
      total: 0,
      name: ''
    }
  },
  created() {
    this.load()
    this.request.get("/lun").then(res => {
      this.lun = res.data
    })
    this.request.get("/category").then(res => { // 回调函数
      this.categories = res.data
      this.load(this.categories[0].name)
    })

    this.request.get("/goods/top").then(res => { // 回调函数
      this.tops = res.data
    })

    this.request.get("/goods/recommend").then(res => { // 回调函数
      this.recommends = res.data
    })
  },
  methods: {
    load(category) {
      this.active = category
      this.request.get("/goods/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          category: category
        }
      }).then(res => {
        this.goods = res.data.records
        this.total = res.data.total
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load(this.active)
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load(this.active)
    },
  }
}
</script>

<style>
.line1 {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.active {
  color: white;
  background-color: #1E90FF;
}
</style>
