<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>订单管理</el-breadcrumb-item>
      <el-breadcrumb-item>订单列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 搜索区域 -->
      <el-row :gutter="20">
        <el-col :span="8" v-if="!isUser">
          <el-input
            placeholder="请输入买家姓名"
            v-model="queryInfo.query"
            clearable
            @clear="getOrderList"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="getOrderList"
            ></el-button>
          </el-input>
        </el-col>
      </el-row>
      <!-- 数据显示区域 -->
      <el-table :data="orderList" border stripe>
        <el-table-column label="#" type="index"></el-table-column>
        <el-table-column label="订单编号" prop="id"></el-table-column>

        <el-table-column label="商品名称" prop="goodsName"></el-table-column>
        <el-table-column label="买家" prop="name"></el-table-column>
        <el-table-column label="下单时间" prop="createTime">
          <template slot-scope="scope">
            {{ scope.row.createTime | dateFormat }}
          </template>
        </el-table-column>
        <el-table-column
          label="是否付款"
          prop="ispay"
          :formatter="formatterIspay"
        ></el-table-column>
        <el-table-column
          label="已支付金额(元)"
          prop="paymoney"
        ></el-table-column>
        <el-table-column label="收货地址" prop="address"> </el-table-column>
        <el-table-column label="操作" width="130px">
          <template slot-scope="scope">
            <!-- 修改按钮 -->
            <el-tooltip
              effect="dark"
              content="修改地址"
              placement="top"
              :enterable="false"
            >
              <el-button
                type="primary"
                icon="el-icon-edit"
                size="mini"
                @click="showEditDialog(scope.row)"
              />
            </el-tooltip>
            <!-- 删除按钮 -->
            <el-tooltip
              effect="dark"
              content="删除"
              placement="top"
              :enterable="false"
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
      <!-- 分页区域 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[5, 10]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <!-- 修改订单地址的对话框 -->
    <el-dialog
      title="修改订单"
      :visible.sync="editOrderDialogVisible"
      width="50%"
      @close="editOrderDialogClosed"
    >
      <el-form
        :model="editOrderForm"
        :rules="editOrderFormRules"
        ref="editOrderFormRef"
        label-width="70px"
      >
        <el-form-item label="商品名称" prop="goodsName">
          <el-input v-model="editOrderForm.goodsName" disabled></el-input>
        </el-form-item>
        <el-form-item label="买家" prop="name">
          <el-input v-model="editOrderForm.name" disabled></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="editOrderForm.address"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editOrderDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editOrderInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import Qs from "qs";
export default {
  data() {
    return {
      //权限列表
      orderList: [],

      //搜索参数
      queryInfo: {
        //搜索内容
        query: "",
        //当前页
        pagenum: 1,
        //页大小
        pagesize: 5,
      },
      //订单总数
      total: 0,

      //控制修改订单对话框显示
      editOrderDialogVisible: false,
      //要修改订单的信息
      editOrderForm: {
        id: null,
        goodsName: "",
        name: "",
        address: "",
      },
      //
      editOrderFormRules: {
        address: { required: true, message: "请输入地址", trigger: "blur" },
      },

      //是不是普通用户
      isUser: false,
    };
  },
  created() {
    let userInfo = window.sessionStorage.getItem("userInfo");
    if (JSON.parse(userInfo).role != 1) {
      this.currentUserInfo = JSON.parse(userInfo);
      this.isUser = true;
      this.queryInfo.query = JSON.parse(userInfo).name;
    }
    // 获取所有订单
    this.getOrderList();
  },
  methods: {
    // 获取订单列表
    async getOrderList() {
      axios
        .get("http://localhost:9090/order/getPageOrder", {
          params: this.queryInfo,
        })
        .then((res) => {
          //console.log(res.data);
          this.orderList = res.data.orders;
          //console.log(this.orderList)
          this.total = res.data.total;
          return this.$message.success("获取订单列表成功!");
        })
        .catch((err) => {
          console.log(err);
          return this.$message.error("获取订单列表失败!");
        });
    },
    //监听pagesize改变
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize;
      //改变pagesize后重新请求数据
      this.getOrderList();
    },
    //监听页码值改变
    handleCurrentChange(newSize) {
      this.queryInfo.pagenum = newSize;
      this.getOrderList();
    },
    //是否已付款
    formatterIspay(row) {
      return row.ispay == 1 ? "是" : "否";
    },
    //显示修改订单对话框
    showEditDialog(row) {
      this.editOrderForm.id = row.id;
      this.editOrderForm.goodsName = row.goodsName;
      this.editOrderForm.name = row.name;
      this.editOrderForm.address = row.address;
      this.editOrderDialogVisible = true;
    },
    //监听对话框关闭
    editOrderDialogClosed() {
      this.$refs.editOrderFormRef.resetFields();
    },
    //提交订单修改
    editOrderInfo() {
      this.$refs.editOrderFormRef.validate(async (valid) => {
        //发起修改订单地址的数据请求
        if (!valid) return;

        let params = Qs.stringify({
          id: this.editOrderForm.id,
          address: this.editOrderForm.address,
        });
        axios
          .put("http://localhost:9090/order/updateAddress?" + params)
          .then((res) => {
            if (res.status === 200 && res.data.result == 1) {
              this.getOrderList();
              this.editOrderDialogVisible = false;
              return this.$message.success(res.data.msg);
            } else {
              return this.$message.error(res.data.msg);
            }
          })
          .catch((err) => {
            console.log(err);
            return this.$message.error("修改地址失败");
          });
      });
    },
    //删除订单
    async removeById(id) {
      //弹窗询问用户是否删除数据;
      const confirmResult = await this.$confirm("确定删除订单嘛?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then((res) => res)
        .catch((err) => err);
      //用户确认输出，return字符串 confirm， 否则return cancel
      if (confirmResult !== "confirm") {
        return this.$message.info("已取消删除");
      }
      axios
        .delete("http://localhost:9090/order/deleteOrder", {
          params: {
            id: id,
          },
        })
        .then((res) => {
          if (res.status === 200 && res.data.result == 1) {
            this.getOrderList();
            return this.$message.success(res.data.msg);
          } else {
            return this.$message.error(res.data.msg);
          }
        })
        .catch((err) => {
          console.log(err);
          return this.$message.error("删除订单失败");
        });
    },
  },
};
</script>

<style></style>
