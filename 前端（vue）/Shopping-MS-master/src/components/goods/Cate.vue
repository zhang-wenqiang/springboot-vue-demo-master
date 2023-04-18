<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>商品管理</el-breadcrumb-item>
      <el-breadcrumb-item>商品分类</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 搜索区域 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            placeholder="请输入内容"
            v-model="queryInfo.type"
            clearable
            @clear="getCateList"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="getCateList"
            ></el-button>
          </el-input>
        </el-col>
        <!-- 添加分类按钮区域 -->
        <el-col :span="4" v-if="!isUser">
          <el-button type="primary" @click="showAddCateDialogVisible"
            >添加分类</el-button
          >
        </el-col>
      </el-row>

      <!-- 表格 -->
      <el-table :data="cateList" style="width: 100%">
        <el-table-column label="ID" width="400">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px">{{ scope.row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="类别名称" width="400">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>id为: {{ scope.row.id }}</p>

              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.typeName }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="操作" v-if="!isUser" width="400">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)"
              >编辑</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[5, 10, 15]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <!-- 添加分类对话框 -->
    <el-dialog
      title="添加分类"
      :visible.sync="addCateDialogVisible"
      width="50%"
      @close="addCateDialogClosed"
    >
      <!-- 添加分类表单 -->
      <el-form
        :model="addCateForm"
        :rules="addCateFormRules"
        ref="addCateFormRef"
        label-width="100px"
      >
        <el-form-item label="分类名称" prop="typeName">
          <el-input v-model="addCateForm.typeName" />
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="addCateDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addCate">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑分类对话框 -->
    <el-dialog
      title="修改类型"
      :visible.sync="editCateDialogVisible"
      width="50%"
      @close="editCateDialogClosed"
    >
      <!-- 分类表单 -->
      <el-form
        :model="editCateForm"
        :rules="editCateFormRules"
        ref="editCateFormRef"
        label-width="100px"
      >
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="editCateForm.typeName" />
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="editCateDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editCate">确 定</el-button>
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
      // 查询条件
      queryInfo: {
        type: "",
        pagenum: 1,
        pagesize: 5,
      },
      // 商品分类的数据列表
      cateList: [],
      // 总数据条数
      total: 0,

      // 控制 添加分类与对话框的显示与隐藏
      addCateDialogVisible: false,
      // 添加分类的表单数据对象
      addCateForm: {
        // 添加分类名称
        typeName: "",
      },
      // 添加分类表单的验证规则对象
      addCateFormRules: {
        typeName: [
          {
            required: true,
            message: "请输入分类名称",
            trigger: "blur",
          },
        ],
      },
      //编辑分类对话框显示
      editCateDialogVisible: false,
      // 编辑分类的表单数据对象
      editCateForm: {
        //
        id: null,
        // 编辑分类名称
        typeName: "",
      },
      // 编辑分类表单的验证规则对象
      editCateFormRules: {
        typeName: [
          {
            required: true,
            message: "请输入分类名称",
            trigger: "blur",
          },
        ],
      },
      //是不是普通用户
      isUser: false,
    };
  },
  created() {
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
    // 获取商品分类数据
    async getCateList() {
      axios
        .get("http://localhost:9090/goodsType/getPageType", {
          params: this.queryInfo,
        })
        .then((res) => {
          // console.log(res);

          this.$message.success("获取商品类型成功");
          this.cateList = res.data.types.records;
          this.total = res.data.total;
        })
        .catch((err) => {
          return this.$message.error("获取商品类型错误");
        });
    },

    // 监听pagesize
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize;
      this.getCateList();
    },
    // 监听handleCurrentChange
    handleCurrentChange(newSize) {
      this.queryInfo.pagenum = newSize;
      this.getCateList();
    },

    // 点击显示添加分类对话框
    showAddCateDialogVisible() {
      // 展示对话框
      this.addCateDialogVisible = true;
    },

    // 点击按钮，添加新的分类
    addCate() {
      this.$refs.addCateFormRef.validate(async (valid) => {
        if (!valid) return;
        let params = new URLSearchParams();
        params.append("typeName", this.addCateForm.typeName);
        axios
          .post("http://localhost:9090/goodsType/addGoodsType", params)
          .then((res) => {
            if (res.status === 200 && res.data.result == 1) {
              this.getCateList();
              this.addCateDialogVisible = false;
              return this.$message.success(res.data.msg);
            }
            return this.$message.error(res.data.msg);
          })
          .catch((err) => {
            return this.$message.error("添加失败");
          });
      });
    },
    // 监听对话框关闭事件，重置表单数据
    addCateDialogClosed() {
      this.$refs.addCateFormRef.resetFields();
      this.addCateForm.typeName = "";
    },
    //删除指定id类别
    async handleDelete(id) {
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
        return this.$message.info("已取消删除！");
      }

      axios
        .delete("http://localhost:9090/goodsType/deleteType?id=" + id)
        .then((res) => {
          if (res.status === 200 && res.data.result == 1) {
            this.getCateList();
            this.$message.success(res.data.msg);
          } else {
            this.$message.error(res.data.msg);
          }
        })
        .catch((err) => {
          this.$message.error("删除失败!");
        });
    },

    //显示编辑分类对话框
    handleEdit(type) {
      this.editCateForm.id = type.id;
      this.editCateForm.typeName = type.typeName;
      this.editCateDialogVisible = true;
    },
    //提交编辑
    async editCate(id) {
      

      let params = Qs.stringify({
        id:this.editCateForm.id,
        typeName:this.editCateForm.typeName
      })
      axios
        .put("http://localhost:9090/goodsType/updateType?"+params)
        .then((res) => {
          if (res.status === 200 && res.data.result == 1) {
            this.getCateList();
            this.editCateDialogVisible = false;
            return this.$message.success("修改成功");
          }
          return this.$message.error("无需修改");
        })
        .catch((err) => {
          return this.$message.error("修改失败");
        });
    },
    // 监听对话框关闭事件，重置表单数据
    editCateDialogClosed() {
      this.$refs.editCateFormRef.resetFields();
      this.editCateForm.typeName = "";
    },
  },
};
</script>

<style lang="less" scoped>
.tree-table {
  margin-top: 15px;
}

.el-cascader {
  width: 100%;
}
</style>
