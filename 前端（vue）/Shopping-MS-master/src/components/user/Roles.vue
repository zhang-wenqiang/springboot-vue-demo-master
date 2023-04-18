<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      <el-breadcrumb-item>角色列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图 -->
    <el-card>
      <!-- 添加角色按钮区域 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            placeholder="请输入内容"
            v-model="queryInfo.roleName"
            clearable
            @clear="getRolesList"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="getRolesList"
            ></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="showAddRoleDialogVisible"
            >添加角色</el-button
          >
        </el-col>
      </el-row>

      <!-- 角色列表区域 -->
      <el-table :data="roleList" border stripe>
        <!-- 索引列 -->
        <el-table-column label="#" type="index"></el-table-column>
        <el-table-column label="角色id" prop="id"> </el-table-column>
        <el-table-column label="角色名称" prop="roleName"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="showEditRoleDialog(scope.row)"
              type="primary"
              icon="el-icon-edit"
              >编辑
            </el-button>
            <el-button
              size="mini"
              @click="deleteRole(scope.row.id)"
              type="danger"
              icon="el-icon-delete"
              >删除
            </el-button>
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
    <!-- 添加角色对话框 -->
    <el-dialog
      title="添加角色"
      :visible.sync="addRoleDialogVisible"
      width="50%"
      @close="addRoleDialogClosed"
    >
      <!-- 添加分类表单 -->
      <el-form
        :model="addRoleForm"
        :rules="addRoleFormRules"
        ref="addRoleFormRef"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="typeName">
          <el-input v-model="addRoleForm.roleName" />
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="addRoleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addRole">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 编辑角色对话框 -->
    <el-dialog
      title="修改角色"
      :visible.sync="editRoleDialogVisible"
      width="50%"
      @close="editDialogClosed"
    >
      <!-- 分类表单 -->
      <el-form :model="editRoleForm" ref="editRoleFormRef" label-width="100px">
        <el-form-item label="类型名称" prop="roleName">
          <el-input v-model="editRoleForm.roleName" />
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="editRoleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="editRole">确 定</el-button>
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
      //控制修改角色对话框显示
      editRoleDialogVisible: false,
      //要修改角色信息
      editRoleForm: {
        id: null,
        roleName: "",
      },

      //权限列表
      roleList: [],
      total: 0,
      //查询条件
      queryInfo: {
        roleName: "",
        pagenum: 1,
        pagesize: 5,
      },
      // 控制 添加分类与对话框的显示与隐藏
      addRoleDialogVisible: false,
      // 添加分类的表单数据对象
      addRoleForm: {
        // 添加分类名称
        roleName: "",
      },
      // 添加分类表单的验证规则对象
      addRoleFormRules: {
        roleName: [
          {
            required: true,
            message: "请输入分类名称",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {
    this.getRolesList();
  },
  methods: {
    //确定修改角色
    editRole() {
      

      let params = Qs.stringify({
        id:this.editRoleForm.id,
        roleName:this.editRoleForm.roleName
      })
      axios
        .put("http://localhost:9090/role/updateRole?"+params)
        .then((res) => {
          if (res.status === 200 && res.data.result == 1) {
            this.editRoleDialogVisible = false;
            this.editDialogClosed();
            this.getRolesList();
            return this.$message.success(res.data.msg);
          }
          return this.$message.error(res.data.msg);
        })
        .catch((err) => {
          return this.$message.error("修改失败");
        });
    },
    //监听修改角色对话框关闭事件
    editDialogClosed() {
      this.$refs.editRoleFormRef.resetFields();
      this.editRoleForm.roleName = "";
    },
    //显示编辑角色对话框
    showEditRoleDialog(roleInfo) {
      this.editRoleForm.id = roleInfo.id;
      
      this.editRoleForm.roleName = roleInfo.roleName;
      this.editRoleDialogVisible = true;
    },
    // 监听pagesize
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize;
      this.getRolesList();
    },
    // 监听handleCurrentChange
    handleCurrentChange(newSize) {
      this.queryInfo.pagenum = newSize;
      this.getRolesList();
    },
    // 获取角色列表
    async getRolesList() {
      axios
        .get("http://localhost:9090/role/getRole", {
          params: this.queryInfo,
        })
        .then((res) => {
          
          this.roleList = res.data.roles.records;
          this.total = res.data.total;
          return this.$message.success("获取角色信息成功");
        })
        .catch((err) => {
          return this.$message.error("获取角色信息失败");
        });
    },

    // 点击显示添加分类对话框
    showAddRoleDialogVisible() {
      // 展示对话框
      this.addRoleDialogVisible = true;
    },

    // 点击按钮，添加新的分类
    addRole() {
      this.$refs.addRoleFormRef.validate(async (valid) => {
        if (!valid) return;

        let params = new URLSearchParams();
        params.append("roleName",this.addRoleForm.roleName);
        axios
          .post("http://localhost:9090/role/addRole", params)
          .then((res) => {
            if (res.status === 200 && res.data.result == 1) {
              this.getRolesList();
              this.addRoleDialogVisible = false;
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
    addRoleDialogClosed() {
      this.$refs.addRoleFormRef.resetFields();

      this.addRoleForm.roleName = "";
    },
    //删除角色
    async deleteRole(id){
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
        .delete("http://localhost:9090/role/deleteRole?id=" + id)
        .then((res) => {
          if (res.status === 200 && res.data.result == 1) {
            this.getRolesList();
            this.$message.success(res.data.msg);
          } else {
            this.$message.error(res.data.msg);
          }
        })
        .catch((err) => {
          this.$message.error("删除失败!");
        });
    },
  },
};
</script>

<style lang="less" scoped>
.el-tag {
  margin: 7px;
}

.bdtop {
  border-top: 1px solid #eee;
}
.bdbottom {
  border-bottom: 1px solid #eee;
}
.vcenter {
  display: flex;
  align-items: center;
}
</style>
