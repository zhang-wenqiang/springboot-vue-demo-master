<template>
  <div class="login_container">
    <div class="systemName" ><h1>DIY 电 脑 管 理 系 统</h1></div>
    <div class="login_box">
      <!-- 头像区域 -->
      <div class="avatar_box">
        <img src="../assets/logo.png" alt="" />
      </div>
      <!-- 登录表单区域 -->
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginFormRules"
        label-width="0px"
        class="login_form"
      >
        <!-- 账号 -->
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            prefix-icon="iconfont icon-user"
          ></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            prefix-icon="iconfont icon-3702mima"
            type="password"
          ></el-input>
        </el-form-item>
        <!-- 按钮区域 -->
        <el-form-item class="btns">
          <el-button type="primary" @click="login">登录</el-button>
          <el-button type="info" @click="addDialogVisible = true"
            >注册</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <!-- 添加用户的对话框 -->
    <el-dialog
      title="注册用户"
      :visible.sync="addDialogVisible"
      width="50%"
      @close="addDialogClosed"
    >
      <!-- 内容主体区域 -->
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="addFormRef"
        label-width="70px"
      >
        <el-form-item label="账号" prop="account">
          <el-input v-model="addForm.account"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password"></el-input>
        </el-form-item>
        <el-form-item label="名字" prop="name">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="addForm.age"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-input v-model="addForm.sex"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="addForm.address"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addUser">注 册</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    //验证年龄的规则
    let checkAge = (rule, value, cb) => {
      if (value >= 18) {
        return cb();
      }

      cb(new Error("年龄必须大于十八岁"));
    };
    //验证性别
    let checkSex = (rule, value, cb) => {
      if (value == "男" || value == "女") {
        return cb();
      }
      cb(new Error("性别必须为男或女"));
    };
    return {
      //登录表单的数据绑定对象
      loginForm: {
        username: "",
        password: "",
      },

      //登陆表单的验证对象
      loginFormRules: {
        //验证用户名
        username: [
          { required: true, message: "请输入登录账号", trigger: "blur" },
          {
            min: 3,
            max: 10,
            message: "长度在 3 到 10 个字符",
            trigger: "blur",
          },
        ],
        //验证密码
        password: [
          { required: true, message: "请输入登录密码", trigger: "blur" },
          {
            min: 6,
            max: 15,
            message: "长度在 6 到 15 个字符",
            trigger: "blur",
          },
        ],
      },

      //控制注册弹窗显示
      addDialogVisible: false,
      //注册用户验证
      //添加用户的表单数据
      addForm: {
        account: "",
        password: "",
        name: "",
        address: "",
        age: 18,
        sex: "",
      },
      //添加表单的验证规则对象
      addFormRules: {
        account: [
          { required: true, message: "请输入账号", trigger: "blur" },
          {
            min: 3,
            max: 10,
            message: "账号长度在3~10个字符之间",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 6,
            max: 15,
            message: "密码长度在6~15个字符之间",
            trigger: "blur",
          },
        ],
        name: [{ required: true, message: "请输入名字", trigger: "blur" }],
        address: [{ required: true, message: "请输入地址", trigger: "blur" }],
        age: [
          { required: true, message: "请输入年龄", trigger: "blur" },
          { validator: checkAge, trigger: "blur" },
        ],
        sex: [
          { required: true, message: "请输入年龄", trigger: "blur" },
          { validator: checkSex, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    //注册用户
    addUser() {
      var params = new URLSearchParams();
      params.append("account", this.addForm.account);
      params.append("password", this.addForm.password);
      params.append("name", this.addForm.name);
      params.append("age", this.addForm.age);
      params.append("address", this.addForm.address);
      params.append("sex", this.addForm.sex);

      axios
        .post("http://localhost:9090/user/register", params)
        .then((res) => {
          if (res.status == 200 && res.data.result == 1) {
            //console.log(res)
            this.$message.success("注册成功！已自动登录");
            this.addDialogVisible = false;
            this.addDialogClosed();
            window.sessionStorage.setItem(
              "userInfo",
              JSON.stringify(res.data.data)
            );
            this.$router.push("/home");
          } else {
            this.$message.error("注册失败！");
          }
        })
        .catch((error) => {
          alert("网络异常" + error);
        });
    },
    //监听添加用户对话框的关闭
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
    },
    login() {
      this.$refs.loginFormRef.validate(async (valid) => {
        if (!valid) return;
        let { username, password } = this.loginForm;
        var params = new URLSearchParams();
        params.append("account", username);
        params.append("password", password);
        axios
          .post("http://localhost:9090/user/login", params)
          .then((res) => {
            if (res.status == 200 && res.data.result == 1) {
              window.sessionStorage.setItem(
                "userInfo",
                JSON.stringify(res.data.data)
              );
              this.$message.success("登录成功");
              this.$router.push("/home");
            } else {
              alert("账号或密码错误");
            }
          })
          .catch((error) => {
            alert("网络异常" + error);
          });
      });
    },
  },
};
</script>

<style lang="less" scoped>
.systemName {
  // color: #216dba;
  // margin-left: 36%;
  // margin-top: 0;
  // padding-top: 60px;
  // font-size: 60px;
  width: 100%;
  display: flex;
  flex-direction: column;
  
}
.systemName h1{ 
  color: #216dba;
  font-size: 60px;
  align-self: center;  
}

.login_container {
  background-color: #2b4b6b;
  height: 100%;
}

.login_box {
  width: 450px;
  height: 300px;
  background-color: #fff;
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);

  .avatar_box {
    height: 130px;
    width: 130px;
    border: 1px solid #eee;
    border-radius: 50%;
    padding: 10px;
    box-shadow: 0 0 10px #ddd;
    position: absolute;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #fff;

    img {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      background-color: #eee;
    }
  }
}

.login_form {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}

.btns {
  display: flex;
  justify-content: flex-end;
}
</style>
