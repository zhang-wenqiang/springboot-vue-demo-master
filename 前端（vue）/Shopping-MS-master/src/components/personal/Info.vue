<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>个人信息</el-breadcrumb-item>
    </el-breadcrumb>
    <h2>用户信息：</h2>
    <el-form
      label-position="right"
      label-width="100px"
      :model="userInfo"
      :rules="userInfoRules"
      ref="userInfoRef"
    >
      <el-form-item label="用户编号">
        <el-input v-model="userInfo.id" style="width:500px" disabled></el-input
        ><label style="font-size:12px;color:#F56C6C">不可修改</label>
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input v-model="userInfo.name" style="width:500px"></el-input>
      </el-form-item>
      <el-form-item label="账号">
        <el-input
          v-model="userInfo.account"
          style="width:500px"
          disabled
        ></el-input
        ><label style="font-size:12px;color:#F56C6C">不可修改</label>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="userInfo.password" style="width:500px"></el-input>
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="userInfo.age" style="width:500px"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-input v-model="userInfo.sex" style="width:500px"></el-input>
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="userInfo.address" style="width:500px"></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-input
          :value="userRole"
          style="width:500px"
          disabled
        ></el-input
        ><label style="font-size:12px;color:#F56C6C">不可修改</label>
      </el-form-item>
    </el-form>
    <el-row
      ><el-button
        style="margin-left: 300px;"
        type="primary"
        round
        @click="saveInfo"
        >保存</el-button
      ></el-row
    >
  </div>
</template>

<script>
import axios from "axios";
import Qs from 'qs';
export default {
  name: "PersonalInfo",

  data() {
    //验证年龄的规则
    let checkAge = (rule, value, cb) => {
      if (value >= 18) {
        return cb();
      }

      cb(new Error("年龄必须大于十八岁"));
    };
    let checkSex = (rule, value, cb) => {
      if (value == "男" || value == "女") {
        return cb();
      }
      cb(new Error("性别必须为男或女"));
    };
    return {
      //当前角色信息
      userInfo: {},
      //检验规则
      userInfoRules: {
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
          { required: true, message: "请输入性别", trigger: "blur" },
          { validator: checkSex, trigger: "blur" },
        ],
      },
      //角色列表
      roleList: [],
      //当前角色
      userRole:"",
    };
  },
  created() {
    //获取登录用户的信息
    let user = window.sessionStorage.getItem("userInfo");
    this.userInfo = JSON.parse(user);
    
    //获取角色列表
    this.getRoleList();  
  },

  methods: {
    
    //获取角色列表
    async getRoleList() {
      axios
        .get("http://localhost:9090/role/getAllRole")
        .then((res) => {
          this.roleList = res.data;
          this.formatterRole(this.userInfo.role);
          return this.$message.success("用户信息获取成功");
        })
        .catch((err) => {
          return this.$message.error("获取角色信息失败");
        });
    },

    //角色转换格式
    formatterRole(id) {
      
      let array = this.roleList;
      
      for (let index = 0; index < array.length; index++) {
        if (array[index].id == id) {
          this.userRole = array[index].roleName ;
        }
      }
    },

    //提交用户信息更改
    saveInfo() {
      //预验证
      this.$refs.userInfoRef.validate(async (valid) => {
        //发起修改用户信息的数据请求
        if (!valid) return;
        

        let params = Qs.stringify({
          id:this.userInfo.id,
          name:this.userInfo.name,
          account:this.userInfo.account,
          password:this.userInfo.password,
          sex:this.userInfo.sex,
          age:this.userInfo.age,
          address:this.userInfo.address,
          avatar:this.userInfo.avatar,
          role:this.userInfo.role
        })

        axios
          .put("http://localhost:9090/user/updateUserInfo?"+params)
          .then((res) => {
            if (res.status === 200 && res.data.result == 1) {
              window.sessionStorage.setItem("userInfo",JSON.stringify(this.userInfo));
              
              return this.$message.success("修改用户信息成功");
            } else {
              return this.$message.error("更新用户信息失败!");
            }
          })
          .catch((err) => {
            console.log(err);
            return this.$message.error("更新用户信息失败!");
          });
      });
    },
  },
};
</script>

<style></style>
