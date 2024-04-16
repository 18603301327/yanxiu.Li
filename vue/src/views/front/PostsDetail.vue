<template>
  <div style="margin: 10px 0">
    <el-card>
      <div style="padding: 0 20px">
        <div style="margin-bottom: 20px; font-size: 40px">{{ detail.name }}</div>

        <div style="margin-top: 20px; color: #666">
          <span><i class="el-icon-user"></i> {{ detail.user }}</span>
          <span style="margin-left: 10px"><i class="el-icon-time"></i> {{ detail.time }}</span>
        </div>

        <div style="margin: 40px 0" v-html="detail.content">
        </div>
      </div>
    </el-card>

    <el-card style="margin: 10px 0">
      <div style="padding: 10px 0; border-bottom: 1px solid #ccc">评论</div>

      <div style="margin: 10px 0">
        <el-input type="textarea" :rows="3" v-model="form.content"></el-input>
        <div style="text-align: right; margin: 10px 0">
          <el-button type="primary" @click="save">评论</el-button>
        </div>

        <div style="padding: 10px; border-top: 1px solid #ccc">
          <div v-for="item in comment" :key="item.id">
            <div style="margin: 10px 0; font-size: 18px">{{ item.content }} <span
                style="margin-left: 20px; font-size: 12px; color: #666">{{ item.user }} <span
                style="margin-left: 10px">{{ item.createTime }}</span></span></div>

            <div style="margin: 10px 0">
              <el-button type="text" @click="replys(item.id, item.user)">回复</el-button>
              <el-button style="color: red" type="text" @click="del(item.id)" v-if="user.id === item.userid">删除</el-button>
            </div>
            <div style="margin: 10px 30px; background-color: aliceblue; padding: 10px " v-if="item.children.length">
              <div v-for="sub in item.children" :key="sub.id">
                <div style="margin: 10px 0; font-size: 18px"> <span style="color: #666;">回复 <b style="color: orange; margin-right: 20px">{{ sub.replyUser }}:</b></span>{{ sub.content }} <span
                    style="margin-left: 20px; font-size: 12px; color: #666">
              {{ sub.user }} <span style="margin-left: 10px">{{ sub.createTime }}</span></span></div>
                <div style="margin: 10px 0">
                  <el-button type="text" @click="replys(item.id,  sub.user)">回复</el-button>
                  <el-button style="color: red" type="text" @click="del(sub.id)" v-if="user.id === sub.userid">删除</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <el-dialog title="信息" :visible.sync="dialogFormVisible" width="40%" :close-on-click-modal="false">
          <el-form label-width="140px" size="small" style="width: 85%;" :model="reply" ref="ruleForm">
            <el-form-item prop="reply" label="内容">
              <el-input type="textarea" v-model="reply.content" autocomplete="off"></el-input>
            </el-form-item>

          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </el-card>
  </div>
</template>

<script>

export default {
  name: 'detail',
  data() {
    return {
      id: this.$route.query.id,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      detail: {},
      comment: [],
      form: {},
      dialogFormVisible: false,
      reply: {}
    }
  },
  methods: {
    collect() {
      this.request.post('/collect2', {postsid: this.id, name: this.detail.name, img: this.detail.img }).then(res => {
        this.$message.success('操作成功')
        this.load()
      })
    },
    replys(pid, username) {
      this.reply = {}
      this.reply.pid = pid
      this.reply.replyUser = username
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/comment/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    load() {
      this.request.get("/posts/" + this.id).then(res => {
        this.detail = res.data
      })

      this.request.get("/comment/tree/" + this.id).then(res => {
        this.comment = res.data
      })
    },
    save() {
      this.form.fid = this.id
      if (!this.form.content && !this.reply.content) {
        this.$message.error("请填写内容")
        return
      }
      if (!this.form.content) {
        this.form.content = this.reply.content
        this.form.pid = this.reply.pid
        this.form.replyUser = this.reply.replyUser
      }
      this.request.post('/comment', this.form).then(res => {
        if (res.code === '200') {
          this.$message({
            type: "success",
            message: "操作成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.form.content = ""
        this.load()
        this.dialogFormVisible = false
      })
    },
  },
  created() {
    this.load()
  }
}
</script>
