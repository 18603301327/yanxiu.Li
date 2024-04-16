<template>
  <div>
    <el-table :data="tableData" stripe size="medium"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="goodName" label="门票名称"></el-table-column>
      <el-table-column prop="price" label="门票单价"></el-table-column>
      <el-table-column prop="num" label="门票数量"></el-table-column>
      <el-table-column prop="img" label="图片">
        <template v-slot="scope">
          <el-image :src="scope.row.img" style="width: 100px; height: 100px"></el-image>
        </template>
      </el-table-column>
<!--      <el-table-column prop="userid" label="用户id"></el-table-column>-->

      <el-table-column label="操作"  width="180" align="center">
        <template slot-scope="scope">
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 20px; background-color: white; margin: 10px 0; border-radius: 10px">
      <div style="margin-bottom: 10px">
        收货地址
      </div>
      <div  v-for="item in address" :key="item.id" style="margin: 10px 0">
        <el-radio :label="item.id" v-model="addressid">{{ item.content }} <span style="margin-left: 10px">{{ item.phone }}</span></el-radio>
      </div>
    </div>

    <div style="padding: 10px 20px; background-color: white; margin: 10px 0; text-align: right; border-radius: 10px">
     <div style="color: red;">共{{ total }}件门票 总价 ￥{{ totalPrice }}</div>
      <div style="margin: 10px 0">
        <el-button style="background-color: red; color: white; font-size: 18px; padding: 10px 20px" @click="adaOrder">下单</el-button>
      </div>
    </div>

    <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
      <el-form label-width="140px" size="small" style="width: 85%;" :model="form" :rules="rules" ref="ruleForm">
        <el-form-item prop="goodsId" label="门票id">
          <el-input v-model="form.goodsId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="num" label="门票数量">
          <el-input v-model="form.num" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item prop="userid" label="用户id">
          <el-input v-model="form.userid" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Cart",
  data() {
    return {
      tableData: [],
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        name: [
          { required: true, message: '请输入名称', trigger: 'blur'}
        ],
      },
      total: 0,
      totalPrice: 0,
      address: [],
      addressid: 0
    }
  },
  created() {
    this.load()
  },
  methods: {
    adaOrder() {
      if (!this.multipleSelection.length) {
        this.$message.warning("下单失败，未选择门票")
        return
      }
      if (!this.addressid) {
        this.$message.warning("下单失败，请选择地址")
        return
      }
      this.request.post("/orders/addOrder?addressid=" + this.addressid, this.multipleSelection).then(res => {
        if (res.code === '200') {
          this.$message.success("下单成功")
          this.load()
          this.addressid = 0
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load() {
      this.request.get("/cart/personCart").then(res => {
        this.tableData = res.data.list
      })

      this.request.get('/address').then(res => {
        this.address = res.data
      })
    },
    save() {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            this.request.post("/cart", this.form).then(res => {
              if (res.code === '200') {
                this.$message.success("保存成功")
                this.dialogFormVisible = false
                this.load()
              } else {
                this.$message.error(res.msg)
              }
            })
          }
        })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
      this.$nextTick(() => {
        if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
       this.$nextTick(() => {
         if(this.$refs.img) {
           this.$refs.img.clearFiles();
         }
         if(this.$refs.file) {
          this.$refs.file.clearFiles();
         }
       })
    },
    del(id) {
      this.request.delete("/cart/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
      this.request.post("/cart/cal", val).then(res => {
        this.totalPrice = res.data
      })
      this.total = 0
      this.multipleSelection.forEach(v =>  this.total += v.num)
    },
    delBatch() {
      if (!this.multipleSelection.length) {
        this.$message.error("请选择需要删除的数据")
        return
      }
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/cart/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess(res) {
      this.form.file = res
    },
    handleImgUploadSuccess(res) {
      this.form.img = res
    },
    download(url) {
      window.open(url)
    },
    exp() {
      window.open("http://localhost:9090/cart/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
