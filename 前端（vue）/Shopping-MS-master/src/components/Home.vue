<template>
  <el-container class="home-container">
    <!-- 头部区域 -->
    <el-header>
      <div>
        <img src="../assets/heima.png" alt="" />
        <span>DIY电脑管理系统</span>
      </div>
      <p>欢迎“{{ currentName }}{{isUser?'':'(管理员)'}}”登录</p>
      <el-button type="info" @click="logOut"> 退出</el-button>
    </el-header>
    <!-- 页面主体区域 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '59px' : '200px'">
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <!-- 侧边栏菜单区域 -->
        <el-menu
          background-color="#313743"
          text-color="#fff"
          active-text-color="#359afc"
          unique-opened
          :collapse="isCollapse"
          :collapse-transition="false"
          router
          default-active="activePath"
        >
          <!-- 用户管理 -->
          <el-submenu index="/user" v-if="!isUser">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/users">
              <i class="el-icon-menu"></i>
              <span slot="title">用户列表</span>
            </el-menu-item>
            <el-menu-item index="/roles">
              <i class="el-icon-menu"></i>
              <span slot="title">角色列表</span>
            </el-menu-item>
          </el-submenu>

          <!-- 订单管理 -->
          <el-submenu index="/order">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>订单管理</span>
            </template>
            <el-menu-item index="/orderlist">
              <i class="el-icon-menu"></i>
              <span slot="title">订单列表</span>
            </el-menu-item>
          </el-submenu>

          <!-- 商品管理 -->
          <el-submenu index="/goodss">
            <template slot="title">
              <i class="el-icon-location"></i>
              <span>商品管理</span>
            </template>
            <el-menu-item index="/categories">
              <i class="el-icon-menu"></i>
              <span slot="title">商品分类</span>
            </el-menu-item>
            <!-- <el-menu-item index="/params">
              <i class="el-icon-menu"></i>
              <span slot="title">参数列表</span>
            </el-menu-item> -->
            <el-menu-item index="/goods" >
              <i class="el-icon-menu"></i>
              <span slot="title">商品列表</span>
            </el-menu-item>
            <el-menu-item index="/goods/add" v-if="!isUser">
              <i class="el-icon-menu"></i>
              <span slot="title" >添加商品</span>
            </el-menu-item>
          </el-submenu>
          <el-menu-item index="/personalInfo">
            <i class="el-icon-menu"></i>
            <span slot="title">个人信息</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <!-- 右侧主体内容 -->
      <el-main>
        <router-view> </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      //左侧菜单数据

      isCollapse: false,
      activePath: "",

      //当前用户名字
      currentName: "",
      //是不是普通用户
      isUser: false,
    };
  },
  created() {
    this.activePath = window.sessionStorage.getItem("activePath");
    this.currentName = JSON.parse(
      window.sessionStorage.getItem("userInfo")
    ).name;
    if (JSON.parse(window.sessionStorage.getItem("userInfo")).role != 1) {
      this.isUser = true;
    }
  },
  methods: {
    logOut() {
      window.sessionStorage.clear();
      this.$router.push("/login");
    },

    //菜单折叠与展开
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
    },
  },
};
</script>

<style lang="less" scoped>
.home-container {
  height: 100%;
}

.el-header {
  background-color: #373d41;
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  align-items: center;
  color: #fff;
  font-size: 20px;
  > div {
    display: flex;
    align-items: center;
    span {
      margin-left: 15px;
    }
  }
}

.el-aside {
  background-color: #333744;
  .el-menu {
    border-right: none;
  }
}

.el-main {
  background-color: #eaedf1;
}

.iconfont {
  margin-right: 10px;
}

.toggle-button {
  background-color: #475162;
  font-size: 10px;
  line-height: 24px;
  color: #fff;
  text-align: center;
  letter-spacing: 0.2rem;
  cursor: pointer;
}
</style>
