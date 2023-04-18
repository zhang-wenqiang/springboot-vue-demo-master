<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            placeholder="请输入内容"
            v-model="queryInfo.query"
            clearable
            @clear="getGoodList"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="getGoodList"
            ></el-button>
          </el-input>
        </el-col>
        <el-col :span="4" v-if="!isUser">
          <el-button type="primary" @click="goAddpage">添加商品</el-button>
        </el-col>
      </el-row>

      <!-- table表格区域 -->
      <el-table :data="goodsList" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="商品名称" prop="goodsName"></el-table-column>
        <el-table-column
          label="商品价格（元）"
          prop="price"
          width="200px"
        ></el-table-column>
        <el-table-column
          label="商品数量"
          prop="amount"
          width="200px"
        ></el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="200px">
          <template slot-scope="scope">
            {{ scope.row.createTime | dateFormat }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200px">
          <template slot-scope="scope">
            <!-- 购买按钮 -->
            <el-tooltip
              effect="dark"
              content="购买商品"
              placement="top"
              :enterable="false"
              v-if="isUser"
            >
              <el-button
                type="primary"
                icon="el-icon-goods"
                size="mini"
                @click="addOrder(scope.row)"
              />
            </el-tooltip>
            <!-- 修改按钮 -->
            <el-tooltip
              effect="dark"
              content="修改"
              placement="top"
              :enterable="false"
              v-if="!isUser"
            >
              <el-button
                type="primary"
                icon="el-icon-edit"
                size="mini"
                @click="showEditDialog(scope.row.id)"
              />
            </el-tooltip>
            <!-- 删除按钮 -->
            <el-tooltip
              effect="dark"
              content="删除"
              placement="top"
              :enterable="false"
              v-if="!isUser"
            >
              <el-button
                type="danger"
                icon="el-icon-delete"
                size="mini"
                @click="removeById(scope.row.id)"
              />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页区域 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="queryInfo.pagenum"
      :page-sizes="[5, 10, 20]"
      :page-size="queryInfo.pagesize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

    <!-- 修改商品的对话框 -->
    <el-dialog
      title="修改用户"
      :visible.sync="editDialogVisible"
      width="50%"
      @close="editDialogClosed"
    >
      <el-form :model="editForm" ref="editFormRef" label-width="70px">
        <el-form-item label="名称" prop="goodsName">
          <el-input v-model="editForm.goodsName"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="editForm.price"></el-input>
        </el-form-item>
        <el-form-item label="数量" prop="amount">
          <el-input v-model="editForm.amount"></el-input>
        </el-form-item>
        <el-form-item label="类别" prop="goodsType">
          <el-select v-model="editForm.goodsType" placeholder="请选择">
            <el-option
              v-for="cate in cateList"
              :key="cate.id"
              :label="cate.typeName"
              :value="cate.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editUserInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import Qs from 'qs';
export default {
  data() {
    return {
      queryInfo: {
        //查询参数对象
        query: "",
        pagenum: 1,
        pagesize: 10,
      },
      //   商品列表
      goodsList: [],
      //   总数据条数
      total: 0,
      //修改商品弹窗控制
      editDialogVisible: false,
      // 查询到的商品信息对象
      editForm: {},
      // 商品分类列表
      cateList: [],
      //判断是不是用户
      isUser: false,
      //当前用户信息
      currentUserInfo: null,
    };
  },
  created() {
    this.getGoodList();
    this.getCateList();
    this.checkUser();
  },
  methods: {
    //判断当前用户是不是用户，是的话就显示购买按钮
    async checkUser() {
      let userInfo = window.sessionStorage.getItem("userInfo");
      if (JSON.parse(userInfo).role != 1) {
        this.currentUserInfo = JSON.parse(userInfo);
        this.isUser = true;
      }
    },
    // 获取商品分类列表
    async getCateList() {
      axios
        .get("http://localhost:9090/goodsType/getGoodsType")
        .then((res) => {
          this.cateList = res.data;
          console.log("获取商品类型成功");
        })
        .catch((err) => {
          return this.$message.error("获取商品类型错误");
        });
    },
    //   根据分页获取商品列表数据
    async getGoodList() {
      axios
        .get("http://localhost:9090/goods/getGoodsList", {
          params: this.queryInfo,
        })
        .then((res) => {
          this.goodsList = res.data.goods.records;
          this.total = res.data.total;
          return this.$message.success("查询成功");
        })
        .catch((err) => {
          console.log(err);
          return this.$message.error("商品信息查询失败");
        });
    },
    // 监听页码改变
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize;
      this.getGoodList();
    },
    handleCurrentChange(newSize) {
      this.queryInfo.pagenum = newSize;
      this.getGoodList();
    },
    // 删除商品信息
    async removeById(id) {
      const confirmResult = await this.$confirm(
        "此操作将永久删除该商品, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).catch((err) => err);

      if (confirmResult !== "confirm") {
        return this.$message.info("已经取消删除！");
      }

      axios
        .delete("http://localhost:9090/goods/deleteGoods?id=" + id)
        .then((res) => {
          this.$message.success("删除成功！");
          this.getGoodList();
        })
        .catch((err) => {
          this.$message.error("删除失败!");
        });
    },
    // 跳转添加商品页面
    goAddpage() {
      this.$router.push("/goods/add");
    },

    //展示修改商品对话框
    async showEditDialog(id) {
      axios
        .get("http://localhost:9090/goods/getGoodsById?id=" + id)
        .then((res) => {
          if (res.status === 200 && res.data.result == 1) {
            this.editForm = res.data.data;
            this.editDialogVisible = true;
          } else {
            return this.$message.error("获取用户信息失败");
          }
        })
        .catch((err) => {
          return this.$message.error("获取用户信息失败");
        });
    },
    //监听修改用户对话框的关闭
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
    },
    //修改用户信息并提交
    editUserInfo() {
      //预验证
      this.$refs.editFormRef.validate(async (valid) => {
        //发起修改用户信息的数据请求
        if (!valid) return;
        
        
        let params = Qs.stringify({
          id:this.editForm.id,
          goodsName:this.editForm.goodsName,
          amount:this.editForm.amount,
          goodsType:this.editForm.goodsType,
          img:this.editForm.img,
          price:this.editForm.price
        })
        

        axios
          .put("http://localhost:9090/goods/updateGoods?"+params)
          .then((res) => {
            if (res.status === 200 && res.data.result == 1) {
              //隐藏对话框
              this.$message.success("修改成功");
              this.editDialogVisible = false;
              //重新请求数据
              this.getGoodList();
              console.log(res);
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
    //添加订单
    async addOrder(row) {
      const confirmResult = await this.$confirm("确定要购买此商品嘛?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).catch((err) => err);

      if (confirmResult !== "confirm") {
        return this.$message.info("已经取消购买！");
      }
      let params = new URLSearchParams();
      params.append("userid", this.currentUserInfo.id);
      params.append("goodsid", row.id);
      params.append("address", this.currentUserInfo.address);
      params.append("paymoney", row.price);
      params.append("ispay", 1);
      axios
        .post("http://localhost:9090/order/insertOrder", params)
        .then((res) => {
          if (res.status === 200 && res.data.result == 1) {
            this.getGoodList();
            return this.$message.success(res.data.msg);
          } else {
            return this.$message.error(res.data.msg);
          }
        })
        .catch((err) => {
          console.log(err);
          return this.$message.error("购买失败");
        });
    },
  },
};
</script>

<style lang="less" scoped></style>
