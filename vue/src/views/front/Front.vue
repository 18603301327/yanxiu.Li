<template>
  <div style="background-color: GhostWhite; min-height: 100vh">
    <div style="padding: 5px 20px; color: #666; font-size: 12px" ><i class="el-icon-bell" style="margin-right: 2px"></i>活动通知：{{ top }}</div>
<!--    头部-->
    <div style="display: flex; height: 60px; line-height: 60px; border-bottom: 1px solid #eee; background: white">
      <div style="width: 280px; display: flex; padding-left: 10px">
        <div style="width: 50px; margin-left: 10px">
          <img src="../../assets/logo2.png" alt="" style="width: 40px; position: relative; top: 10px; right: 0; border-radius: 50%">
        </div>
        <div style="flex: 1; color: #13227a; cursor: pointer" @click="$router.push('/front/home')" ><b>景区票务系统</b></div>
      </div>
      <div style="flex: 1; display: flex">
       <div style="width: 60%">
         <el-menu :default-active="$route.path" class="el-menu-demo" mode="horizontal" router style="border: none; height: 59px">
           <el-menu-item index="/front/home">首页</el-menu-item>
           <el-menu-item index="/front/orders">我的订单</el-menu-item>
           <el-menu-item index="/front/posts">公告</el-menu-item>
         </el-menu>
       </div>

        <div style="width: 40%">
          <el-input size="medium" style="width: 200px" v-model="name" clearable placeholder="请输入门票名称"></el-input>
          <el-button size="medium" @click="search" style="margin-left: 5px">搜索</el-button>
        </div>
      </div>
      <div style="width: 200px">
        <div v-if="!user.username" style="text-align: right; padding-right: 30px">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
            <div style="display: inline-block">
              <img :src="user.avatarUrl" alt=""
                   style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
              <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/collect">我的收藏</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/password">修改密码</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/person">个人信息</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <div style="text-decoration: none" @click="logout">退出</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <div style="width:90%; margin: 5px auto; padding-bottom: 100px">
      <router-view @refreshUser="getUser"  ref="child" />
    </div>
  </div>
</template>

<script>
export default {
  name: "Front",
  data() {
    return {
      name: '',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      notice: [],
      top: ''
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get('/notice').then(res => {
        this.notice = res.data

        let i = 0
        if (this.notice && this.notice.length) {
          this.top = this.notice[0].content
          setInterval(() => {
            this.top = this.notice[i].content
            i++
            if (i === this.notice.length) {
              i = 0
            }
          }, 5000)
        }
      })
    },
    search() {
      this.$router.replace('/front/search?name=' + this.name + '&random' + Math.random())
      this.$refs.child.searchGoods()
    },
    logout() {
      this.$store.commit("logout")
      this.$message.success("退出成功")
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // 从后台获取User数据
        this.request.get("/user/username/" + username).then(res => {
          // 重新赋值后台的最新User数据
          this.user = res.data
        })
      }
    }
  }
}
</script>

<style>
.item{
  display: inline-block;
  width: 100px;

  text-align: center;
  cursor: pointer
}
.item a {
  color: 	#1E90FF;
}
.item:hover{
  background-color: 	LightPink;
}
</style>
