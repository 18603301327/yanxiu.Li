<template>

  <div>
    <div style="display: flex; margin: 10px 0">
      <div style="width: 40%; ">
        <img :src="goods.img" style="width: 100%;height: 406px"></img>
      </div>
      <div style="margin-left: 10px; flex: 1">
        <el-card>
          <el-form label-width="100px">
            <el-form-item label="门票名称">{{ goods.name }}</el-form-item>
            <el-form-item label="门票编码"><span>{{ goods.code }}</span></el-form-item>
            <el-form-item label="门票描述">{{ goods.descpription }}</el-form-item>
            <el-form-item label="门票价格"><span style="color: red">￥{{ goods.price }}</span></el-form-item>
            <el-form-item label="活动价"><span style="color: red;font-weight: bold">￥{{ goods.discount }}</span></el-form-item>
            <el-form-item label="门票库存"><span>{{ goods.nums }}</span></el-form-item>
            <el-form-item label="门票日期"><span>{{ goods.date }}</span></el-form-item>

            <div>
              <el-input-number :value="1" size="medium" :min="1" style="width: 150px; margin-left: 30px" v-model="buyNum"></el-input-number>
              <el-button style="background: red; font-size: 16px; color: white; padding: 10px; margin-left: 5px" @click="addCart">立即购买</el-button>
            </div>

            <div style="margin: 10px 30px">
              <span style=" cursor: pointer">
                <img src="@/assets/点赞.png" alt="" style="width: 30px" v-if="!goods.hasPraise" @click="praise">
                <img src="@/assets/点赞-active.png" alt="" style="width: 30px" v-else @click="praise">
                <span style="position: relative; top: -8px; color: #666"  v-if="!goods.hasPraise">{{ goods.praiseCount }}</span>
                <span style="position: relative; top: -8px; color: red" v-else>{{ goods.praiseCount }}</span>
              </span>
              <span style="margin-left: 10px; cursor: pointer">
                <img src="@/assets/收藏.png" alt="" style="width: 30px" v-if="!goods.hasCollect" @click="collect">
                <img src="@/assets/收藏 -active.png" alt="" style="width: 30px" v-else @click="collect">
                <span style="position: relative; top: -8px; color: #666" v-if="!goods.hasCollect">{{ goods.collectCount }}</span>
                 <span style="position: relative; top: -8px; color: red" v-else>{{ goods.collectCount }}</span>
              </span>
            </div>
          </el-form>
        </el-card>
      </div>
    </div>


<!--    <el-card style="margin-top: 10px">-->
<!--      <div style="color: #1E90FF; font-size: 20px;font-weight: bold">-->
<!--        地点-->
<!--      </div>-->
<!--      <div style="margin-top: 10px">-->
<!--        <Modal @on-cancel="cancel" v-model="showMapComponent"  :closable="false" :mask-closable="false">-->
<!--          <baidu-map v-bind:style="mapStyle" class="bm-view" ak="rbjgevsMLkje2IrU3GynESAM1iuSpDDW"-->
<!--                     :center="center"-->
<!--                     :zoom="zoom"-->
<!--                     :scroll-wheel-zoom="true"-->
<!--                     @click="getClickInfo"-->
<!--                     @moving="syncCenterAndZoom"-->
<!--                     @moveend="syncCenterAndZoom"-->
<!--                     @zoomend="syncCenterAndZoom">-->
<!--            <bm-view style="width: 100%; height:870px;"></bm-view>-->
<!--            <bm-marker v-for="(item,index) in mapPoint" :key="index" :position="item" :dragging="false" animation="BMAP_ANIMATION_BOUNCE">-->
<!--            </bm-marker>-->
<!--            <bm-control :offset="{width: '10px', height: '10px'}" >-->
<!--              &lt;!&ndash;              <bm-auto-complete v-model="keyword" :sugStyle="{zIndex: 999999}">&ndash;&gt;-->
<!--              &lt;!&ndash;                <input type="text" placeholder="请输入搜索关键字" class="serachinput">&ndash;&gt;-->
<!--              &lt;!&ndash;              </bm-auto-complete>&ndash;&gt;-->
<!--            </bm-control>-->
<!--          </baidu-map>-->
<!--        </Modal>-->
<!--      </div>-->
<!--    </el-card>-->

    <div style="margin: 10px 0">
      <el-card>
        <div style="margin: 10px 0; font-size: 18px; border-bottom: 1px solid #ccc; padding-bottom: 10px">门票评价</div>
        <div v-for="item in comments" :key="item.id" style="padding: 10px 0; border-bottom: 1px solid #eee">
          <div style="display: flex; align-items: center">
            <img style="width: 60px; height: 60px; border-radius: 50%" :src="item.avatar" />
            <div style="flex: 1; ">
              <div  style="margin-left: 20px;">{{ item.user }}
              </div>
              <div style="margin-left: 17px;">
                <el-rate v-model="item.scores" disabled></el-rate>
              </div>
              <span style="margin-left: 20px; color: #666; font-size: 14px">{{ item.comment }}</span>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <div style="margin-bottom: 10px">
        收货地址
      </div>
      <div  v-for="item in address" :key="item.id" style="margin: 10px 0">
        <el-radio :label="item.id" v-model="addressid">{{ item.content }} <span style="margin-left: 10px">{{ item.phone }}</span></el-radio>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addCart">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {BaiduMap, BmAutoComplete, BmControl, BmDriving, BmLocalSearch, BmMarker, BmView} from "vue-baidu-map";

export default {
  components: {
    BaiduMap,
    BmControl,
    BmView,
    BmAutoComplete,
    BmLocalSearch,
    BmMarker,
    BmDriving
  },
  name: "Detail",
  data() {
    let goodsId = this.$route.query.id
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      goods: {},
      goodsId: goodsId,
      buyNum: 1,
      comments: [],
      dialogFormVisible: false,
      address: [],
      addressid: 0,
      showMapComponent: this.value,
      keyword: '',
      mapStyle: {
        // width: '100%',
        // height: this.mapHeight + 'px'
      },
      center: {lng: 116.404269, lat: 39.916927},
      zoom: 10,
      mapPoint:[
        {
          lng: 102.99876,
          lat: 29.983259
        }
      ],
      end: ''
    }
  },
  created() {
    this.load()
  },
  watch: {
    value: function (currentValue) {
      this.showMapComponent = currentValue
      if (currentValue) {
        this.keyword = ''
      }

    }
  },
  props: {
    value: Boolean,
    mapHeight: {
      type: Number,
      default: 500
    }
  },
  methods: {
    /***
     * 地图点击事件。
     */
    getClickInfo (e) {
      this.center.lng = e.point.lng
      this.center.lat = e.point.lat
    },
    syncCenterAndZoom (e) {
      const {lng, lat} = e.target.getCenter()
      this.center.lng = lng
      this.center.lat = lat
      this.zoom = e.target.getZoom()
    },
    /***
     * 确认
     */
    confirm () {
      this.showMapComponent = false
      this.$emit('map-confirm', this.center)
      console.log(this.center);

    },
    /***
     * 取消
     */
    cancel () {
      this.showMapComponent = false
      this.$emit('cancel', this.showMapComponent)
    },
    load() {
      this.request.get('/goods/' + this.goodsId).then(res => {
        this.goods = res.data
        let data = {lng: this.goods.lng,lat:this.goods.lat};
        this.mapPoint.push(data)
      })

      this.request.get('/orderItem/comment/' + this.goodsId).then(res => this.comments = res.data)


      this.request.get('/address').then(res => {
        this.address = res.data

      })
    },
    praise() {
      this.request.post('/praise', {goodsid: this.goodsId }).then(res => {
        this.$message.success('操作成功')
        this.load()
      })
    },
    collect() {
      this.request.post('/collect', {goodsid: this.goodsId, name: this.goods.name, img: this.goods.img }).then(res => {
        this.$message.success('操作成功')
        this.load()
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
    },
    addCart() {
      this.request.post('/orders/addOrder?addressid=' + this.addressid, [{ goodsId: this.goodsId, goodName: this.goods.name, price: this.goods.discount, num: this.buyNum, userid:this.user.id, img: this.goods.img }]).then(res => {
        if (res.code === '200') {
          this.$message.success('下单成功')
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>