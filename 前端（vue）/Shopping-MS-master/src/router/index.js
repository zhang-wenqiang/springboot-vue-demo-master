import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../components/Login.vue";
import Home from "../components/Home.vue";
import Welcome from "../components/Welcome.vue";
import Users from "../components/user/Users.vue";
import OrderList from "../components/order/OrderList.vue";
import Roles from "../components/user/Roles.vue";
import Cate from "../components/goods/Cate.vue";
import Params from "../components/goods/Params.vue";
import GoodsList from "../components/goods/List.vue";
import Add from "../components/goods/Add.vue";
import PersonalInfo from "../components/personal/Info.vue";

Vue.use(VueRouter);

const routes = [
  {
    //重定位
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: "/home",
    name: "Home",
    component: Home,
    redirect: "/welcome",
    children: [
      { path: "/welcome", component: Welcome },
      { path: "/users", component: Users },
      { path: "/orderlist", component: OrderList },
      { path: "/roles", component: Roles },
      { path: "/categories", component: Cate },
      { path: "/params", component: Params },
      { path: "/goods", component: GoodsList },
      { path: "/goods/add", component: Add },
      { path: "/personalInfo", component: PersonalInfo },
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

//挂载路由导航守卫
router.beforeEach((to, from, next) => {
  //to 将要访问的路径
  //from 代表从那个路径跳转而来
  //next()放行   next('/login')强制跳转

  // if (to.path === "/login") return next();
  // //保存token
  // const tokenStr = window.sessionStorage.getItem("token");
  // if (!tokenStr) return next("./login");
  // next();

  if (!sessionStorage.getItem("userInfo")) {
    if (to.path !== "/login") {
      next("/login");
    }
  }
  next();
});

export default router;
