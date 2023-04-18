<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>添加商品</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图 -->
    <el-card>
      <!-- 提示区域 -->
      <el-alert
        title="添加商品信息"
        type="info"
        center
        show-icon
        :closable="false"
      >
      </el-alert>
      <!-- 步骤条区域 -->
      <el-steps
        :space="200"
        :active="activeIndex - 0"
        finish-status="success"
        align-center
      >
        <el-step title="基本信息"></el-step>
        <!-- <el-step title="商品参数"></el-step> -->
        <!-- <el-step title="商品属性"></el-step> -->
        <!-- <el-step title="商品图片"></el-step> -->
        <!-- <el-step title="商品内容"></el-step> -->
        <el-step title="完成"></el-step>
      </el-steps>
      <!-- tab标签页 -->
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="addFormRef"
        label-width="100px"
        label-position="'left'"
      >
        <el-tabs v-model="activeIndex" :tab-position="'left'">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="0">
            <el-form-item label="商品名称" prop="goodsName">
              <el-input v-model="addForm.goodsName"></el-input>
            </el-form-item>
            <el-form-item label="商品价格" prop="price">
              <el-input v-model="addForm.price" type="number"></el-input>
            </el-form-item>
            <el-form-item label="商品数量" prop="amount">
              <el-input v-model="addForm.amount" type="number"></el-input>
            </el-form-item>
            <el-form-item label="商品分类" prop="goodsType">
              <el-select v-model="addForm.goodsType" placeholder="请选择">
                <el-option
                  v-for="cate in cateList"
                  :key="cate.id"
                  :label="cate.typeName"
                  :value="cate.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-button type="primary" class="btnAdd" @click="add"
              >添加</el-button
            >
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-card>

    <!-- 预览图片对话框 -->
    <!-- <el-dialog
      title="预览图片"
      :visible.sync="previewDialogVisible"
      width="50%"
    >
      <img :src="previewPath" class="previewImg" />
    </el-dialog> -->
  </div>
</template>

<script>
import _ from "lodash";
import axios from "axios";

export default {
  data() {
    return {
      activeIndex: "0",
      //  添加商品 表单验证规则对象
      addForm: {
        //商品名字
        goodsName: "",
        //商品价格
        price: 100,
        //商品数量
        amount: 100,
        //商品图片
        img: "",
        //商品类型
        goodsType: null,
      },
      addFormRules: {
        goods_name: [
          { required: true, message: "请输入商品名称", trigger: "blur" },
        ],
        price: [{ required: true, message: "请输入商品价格", trigger: "blur" }],
        amount: [
          { required: true, message: "请输入商品数量", trigger: "blur" },
        ],
      },
      // 商品分类列表
      cateList: [],
      cateProps: {
        label: "typeName",
        value: "id",
        //children: "children",
      },
    };
  },

  created() {
    this.getCateList();
  },
  methods: {
    // 获取商品分类列表
    async getCateList() {
      axios
        .get("http://localhost:9090/goodsType/getGoodsType")
        .then((res) => {
          this.cateList = res.data;
          return this.$message.success("商品类型获取成功");
        })
        .catch((err) => {
          return this.$message.error("获取商品类型错误");
        });
    },

    // 添加商品
    add() {
      console.log(this.addForm.goodsName);
      this.$refs.addFormRef.validate(async (valid) => {
        if (!valid) {
          return this.$message.error("请填写必要的表单项!");
        }

        // 发起请求，添加商品
        let params = new URLSearchParams();
        params.append("goodsName", this.addForm.goodsName);
        params.append("price", this.addForm.price);
        params.append("amount", this.addForm.amount);
        params.append("img", this.addForm.img);
        params.append("goodsType", this.addForm.goodsType);
        axios
          .post("http://localhost:9090/goods/addGoods", params)
          .then((res) => {
            this.$message.success("添加商品成功！");
            this.$router.push("/goods");
          })
          .catch((err) => {
            this.$message.error("添加商品失败！" + err);
          });
      });
    },
  },
};
</script>

<style lang="less" scoped>
.el-checkbox {
  margin: 0 10px !important;
}

.previewImg {
  width: 100%;
}

.btnAdd {
  margin-top: 15px;
}
</style>
