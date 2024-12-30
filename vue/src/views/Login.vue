<template>
  <div class="container">
    <div style="width: 380px; padding:50px 30px; background-color: rgba(255,255,255,.8); border-radius: 5px;">
      <div style="text-align: center; font-size: 24px; margin-bottom: 30px; color: #333">《论语》博客平台</div>

      <el-tabs type="card" v-model="activeName">
        <el-tab-pane label="账号密码登录" name="first">
          <el-form :model="form" :rules="rules" ref="formRef">
            <el-form-item prop="username">
              <el-input size="medium" prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input size="medium" prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item prop="role">
              <el-radio-group v-model="form.role">
                <el-radio label="ADMIN">管理员</el-radio>
                <el-radio label="USER">用户</el-radio>
              </el-radio-group>
              <el-form-item prop="code">
                <div style="display: flex">
                  <el-input style="flex: 1" size="medium" v-model="code"></el-input>
                  <Identify :identify-code="identifyCode" @click.native="refreshCode"/>
                </div>
              </el-form-item>
            </el-form-item>
            <el-form-item>
              <el-button size="medium" style="width: 100%; background-color: #333; border-color: #333; color: white" @click="login">登 录</el-button>
            </el-form-item>
            <div style="display: flex; align-items: center">
              <div style="flex: 1"></div>
              <div style="flex: 1; text-align: right">
                还没有账号？请 <a href="/register">注册</a>
              </div>
            </div>
            <div style="display: flex; align-items: center;margin-top: 10px">
              <div style="flex: 1"></div>
              <div style="flex: 1; text-align: right">
                <a @click="handlePass()" style="cursor: pointer">重置密码</a>
              </div>
            </div>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="邮箱登录" name="second">
          <el-form :model="form1" :rules="rules" ref="formRef">
            <el-form-item prop="email">
              <el-input size="medium" prefix-icon="el-icon-message" placeholder="请输入邮箱" v-model="form1.email"></el-input>
            </el-form-item>
            <el-form-item prop="code1">
              <el-input size="medium" prefix-icon="el-icon-lock" placeholder="请输入验证码" style="width: 210px"  v-model="form1.code"></el-input>
              <el-button type="primary" size="small" style="margin-left: 10px" @click="getEmailCode(1)">获取验证码</el-button>
            </el-form-item>
<!--            <el-form-item prop="role">-->
<!--              <el-radio-group v-model="form1.role">-->
<!--                <el-radio label="ADMIN">管理员</el-radio>-->
<!--                <el-radio label="USER">用户</el-radio>-->
<!--              </el-radio-group>-->
<!--            </el-form-item>-->
            <el-form-item>
              <el-button size="medium" style="width: 100%; background-color: #333; border-color: #333; color: white" @click="loginEmail">登 录</el-button>
            </el-form-item>
            <div style="display: flex; align-items: center">
              <div style="flex: 1"></div>
              <div style="flex: 1; text-align: right">
                还没有账号？请 <a href="/register">注册</a>
              </div>
            </div>
            <div style="display: flex; align-items: center;margin-top: 10px">
              <div style="flex: 1"></div>
              <div style="flex: 1; text-align: right">
                <a @click="handlePass()" style="cursor: pointer">重置密码</a>
              </div>
            </div>
          </el-form>
        </el-tab-pane>
      </el-tabs>

    </div>
    <el-dialog title="找回密码" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="100px" size="small">
        <el-form-item label="邮箱">
          <el-input v-model="pass.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="验证码">
          <el-input size="small" placeholder="请输入验证码" style="width: 180px"  v-model="pass.code"></el-input>
          <el-button type="primary" size="small" style="margin: 10px 10px" @click="getEmailCode(2)">获取验证码</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible=false">取消</el-button>
        <el-button type="primary" @click="passwordBack">重置密码</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Identify from "@/components/Identify";

export default {
  name: "Login",
  components: {Identify},
  comments:{
    Identify
  },
  data() {
    return {
      form: { role: 'ADMIN' },
      form1:{role: 'ADMIN'},
      pass:{},
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        // email: [
        //   { required: true, message: '请输入邮箱', trigger: 'blur' },
        //   {min:1,max:20,message: '长度在20个字符以内',trigger: 'blur'}
        // ],
        // code1: [
        //   { required: true, message: '请输入验证码', trigger: 'blur' },
        //   {min:1,max:20,message: '长度4位',trigger: 'blur'}
        // ]
      },
      code:'',  //表单绑定的验证码属性
      //图片验证码
      identifyCode:'',
      //验证码规则
      identifyCodes:'0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ',
      activeName:'first',
      dialogFormVisible:false,
    }
  },
  mounted() {
    this.refreshCode()
  },
  methods: {
    handlePass(){
      this.dialogFormVisible=true
      this.pass={}
    },
    getEmailCode(type){
      let email;
      if (type === 1){
        email=this.form1.email
      }else if (type === 2){
        email=this.pass.email
      }
      if (!email){
        this.$message.warning("请输入邮箱账号")
        return
      }
      //发送邮箱验证码
      this.$request.get("/email/" + email + "/" + type).then(res=>{
        if (res.code === '200'){
          this.$message.success("发送成功")
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    // 切换验证码
    refreshCode() {
      this.identifyCode = ''
      this.makeCode(this.identifyCodes, 4)
    },
    // 生成随机验证码
    makeCode(o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode += this.identifyCodes[Math.floor(Math.random() * (this.identifyCodes.length))]
      }
    },
    login() {
      if (!this.code){
        this.$message.warning("请输入验证码")
        this.refreshCode()
        return
      }
      if (this.code.toLocaleLowerCase()!=this.identifyCode.toLocaleLowerCase()){
        this.$message.warning("验证码错误")
        this.refreshCode()
        return
      }
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              this.$message.success('登录成功')
              setTimeout(()=>{
                // 跳转主页
                if (res.data.role === 'ADMIN'){
                  location.href='/home'
                }else {
                  location.href='/front/home'
                }
              },500)
            } else {
              this.refreshCode()
              this.$message.error(res.msg)
            }
          })
        }
      })
    },

    loginEmail() {
          this.$request.post('/loginEmail', this.form1).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              this.$message.success('登录成功')
              setTimeout(()=>{
                // 跳转主页
                if (res.data.role === 'ADMIN'){
                  location.href='/home'
                }else {
                  location.href='/front/home'
                }
              },500)
            } else {
              this.refreshCode()
              this.$message.error(res.msg)
            }
          })
       },
    passwordBack(){
      this.$request.put("/reset" , this.pass).then(res=>{
        if (res.code === '200'){
          this.$message.success("重置密码成功，新密码为123，请尽快修改密码")
          this.dialogFormVisible=false
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/活动1.png");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}
a {
  color: #2a60c9;
}
</style>