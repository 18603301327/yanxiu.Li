<template>
  <el-card>
<!--    <div style="margin-bottom: 20px">-->
<!--      <el-input size="medium" style="width: 300px"  placeholder="请输入门票名称搜索" v-model="name" clearable></el-input>-->
<!--      <el-button size="medium" style="margin-left: 5px" @click="load">搜索</el-button>-->
<!--    </div>-->
    <el-row :gutter="10" style="min-height: 280px">
      <el-col :span="6" v-for="item in goods" :key="item.id" style="margin-bottom: 10px">
        <div style="background-color: white; padding: 10px; cursor: pointer" @click="$router.push('/front/detail?id=' + item.id)">
          <img :src="item.img" alt="" style="width: 100%; height: 200px; overflow: hidden; border-radius: 10px">
          <div style="color: #666; margin: 10px 0" class="line1">{{ item.name }}</div>
          <div style="margin: 10px 0; color: red; font-weight: bold; font-size: 14px">价格：￥{{ item.price }} </div>
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
  </el-card>
</template>

<script>
export default {
  name: "FrontHome",
  data() {
    return {
      goods: [],
      pageNum: 1,
      pageSize: 8,
      total: 0,
      name: ''
    }
  },
  created() {
    this.searchGoods()
  },
  methods: {
    searchGoods() {
      this.request.get("/goods/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name || this.$route.query.name
        }
      }).then(res => {
        this.goods = res.data.records
        this.total = res.data.total
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.searchGoods()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.searchGoods()
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
</style>