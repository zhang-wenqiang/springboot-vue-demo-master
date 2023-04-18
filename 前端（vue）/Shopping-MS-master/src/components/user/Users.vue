<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>用户列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图区域 -->
    <el-card>
      <!-- 搜索添加区域 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            placeholder="请输入内容"
            v-model="queryInfo.query"
            clearable
            @clear="getUserList"
          >
            <el-button
              slot="append"
              icon="el-icon-search"
              @click="getUserList"
            ></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addDialogVisible = true"
            >添加用户</el-button
          >
        </el-col>
      </el-row>
      <!-- 用户列表区域 -->
      <el-table :data="userlist" border stripe>
        <el-table-column type="index"> </el-table-column>
        <el-table-column label="姓名" prop="name"> </el-table-column>
        <el-table-column label="性别" prop="sex"> </el-table-column>
        <el-table-column label="年龄" prop="age"> </el-table-column>
        <el-table-column label="地址" prop="address"> </el-table-column>
        <el-table-column
          label="角色"
          prop="role"
          :formatter="formatterRole"
        ></el-table-column>
        <!-- 用户操作 -->
        <el-table-column label="操作">
          <template v-slot="scope">
            <!-- 修改按钮 -->
            <el-tooltip
              effect="dark"
              content="修改"
              placement="top"
              :enterable="false"
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
            >
              <el-button
                type="danger"
                icon="el-icon-delete"
                size="mini"
                @click="removeUserById(scope.row.id)"
              />
            </el-tooltip>
            <!-- 分配角色按钮 -->
            <el-tooltip
              effect="dark"
              content="分配角色"
              placement="top"
              :enterable="false"
            >
              <el-button
                type="warning"
                icon="el-icon-setting"
                size="mini"
                @click="setRole(scope.row)"
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

      <!-- 添加用户的对话框 -->
      <el-dialog
        title="添加用户"
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
          <el-button type="primary" @click="addUser">确 定</el-button>
        </span>
      </el-dialog>

      <!-- 修改用户的对话框 -->
      <el-dialog
        title="修改用户"
        :visible.sync="editDialogVisible"
        width="50%"
        @close="editDialogClosed"
      >
        <el-form
          :model="editForm"
          :rules="editFormRules"
          ref="editFormRef"
          label-width="70px"
        >
          <el-form-item label="姓名" prop="name">
            <el-input v-model="editForm.name" disabled></el-input>
          </el-form-item>
          <el-form-item label="账号" prop="account">
            <el-input v-model="editForm.account" disabled></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="editForm.password"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sex">
            <el-input v-model="editForm.sex"></el-input>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-input v-model="editForm.age"></el-input>
          </el-form-item>
          <el-form-item label="地址" prop="address">
            <el-input v-model="editForm.address"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="editUserInfo">确 定</el-button>
        </span>
      </el-dialog>

      <!-- 分配角色对话框 -->
      <el-dialog
        title="分配角色"
        :visible.sync="setRoleDialogVisible"
        width="50%"
        @close="setRoleDialogColsed"
      >
        <div>
          <p>当前的用户：{{ userInfo.name }}</p>
          <p>当前的角色：{{ tableFormatterRole(userInfo.role) }}</p>
          <p>
            分配新角色：
            <el-select v-model="selectedRoleId" placeholder="请选择">
              <el-option
                v-for="(item, index) in roleList"
                :key="index"
                :label="item.roleName"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </p>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="setRoleDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveRoleInfo">确 定</el-button>
        </span>
      </el-dialog>
    </el-card>
  </div>
</template>

<script >
import axios from "axios";
import Qs from 'qs';
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
      //用户列表信息
      queryInfo: {
        query: "",
        //当前页数
        pagenum: 1,
        // 当前每页显示多少条数据
        pagesize: 5,
      },
      //用户列表和总数
      userlist: [],
      total: 0,
      // 控制对话框的显示隐藏
      addDialogVisible: false,
      editDialogVisible: false,
      setRoleDialogVisible: false,
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
      },
      // 查询到的用户信息对象
      editForm: {},
      //修改表达的验证规则对象
      editFormRules: {
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
      // 需要被分配角色的用户信息
      userInfo: {},
      // 所有角色的数据列表
      roleList: [],
      // 已选中的角色ID值
      selectedRoleId: null,
    };
  },
  created() {
    this.getUserList();
    this.getRoleList();
  },

  methods: {
    //获取角色列表
    async getRoleList() {
      axios
        .get("http://localhost:9090/role/getAllRole")
        .then((res) => {
          this.roleList = res.data;
          
        })
        .catch((err) => {
          return this.$message.error("获取角色信息失败");
        });
    },
    //获取用户列表信息
    async getUserList() {
      let { id } = JSON.parse(window.sessionStorage.getItem("userInfo"));
      axios
        .get("http://localhost:9090/user/pageSelect", {
          params: {
            name: this.queryInfo.query,
            id: id,
            pagenum: this.queryInfo.pagenum,
            pagesize: this.queryInfo.pagesize,
          },
        })
        .then((res) => {
          console.log("用户列表获取成功");
          this.userlist = res.data.users.records;
          
          this.total = res.data.total;
        })
        .catch((err) => {
          console.log(err);
          return this.$message.error("获取用户列表失败!");
        });
    },

    //分配角色展示当前角色
    tableFormatterRole(id){
      let array = this.roleList;
      for (let index = 0; index < array.length; index++) {
        if(array[index].id==id){
          return array[index].roleName;
        }
      }
    },

    //用户列表展示角色名称
    formatterRole(row) {
      let array = this.roleList;
      for (let index = 0; index < array.length; index++) {
        if(array[index].id==row.role){
          return array[index].roleName;
        }
      }
      // return row.role == 1 ? "管理员" : "普通用户";
    },
    //监听pagesize改变
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize;
      //改变pagesize后重新请求数据
      this.getUserList();
    },
    //监听页码值改变
    handleCurrentChange(newSize) {
      this.queryInfo.pagenum = newSize;
      this.getUserList();
    },

    //监听添加用户对话框的关闭
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
    },
    //点击按钮，添加新用户
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
            this.$message.success("添加成功");

            this.addDialogClosed();
          } else {
            this.$message.error("添加失败！可能名字重复");
          }
        })
        .catch((error) => {
          alert("网络异常" + error);
        });
    },
    //展示编辑用户对话框
    async showEditDialog(id) {
      axios
        .get("http://localhost:9090/user/getUserById", {
          params: {
            id: id,
          },
        })
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
      // const { data: res } = await this.$http.get("users/" + id);
      // if (res.meta.status !== 200) {
      //   return this.$message.error("查询用户信息失败！");
      // }
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
          name:this.editForm.name,
          account:this.editForm.account,
          password:this.editForm.password,
          sex:this.editForm.sex,
          age:this.editForm.age,
          address:this.editForm.address,
          avatar:this.editForm.avatar,
          role:this.editForm.role
        })

        axios
          .put("http://localhost:9090/user/updateUserInfo?"+params)
          .then((res) => {
            if (res.status === 200 && res.data.result == 1) {
              //隐藏对话框
              this.editDialogVisible = false;
              //重新请求数据
              this.getUserList();
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
    //根据ID删除对应用户信息
    async removeUserById(id) {
      //弹窗询问用户是否删除数据;
      const confirmResult = await this.$confirm(
        "此操作将永久删除该用户, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then((res) => res)
        .catch((err) => err);
      //用户确认输出，return字符串 confirm， 否则return cancel
      if (confirmResult !== "confirm") {
        return this.$message.info("已取消删除");
      }
      axios
        .delete("http://localhost:9090/user/deleteUserById", {
          params: {
            id: id,
          },
        })
        .then((res) => {
          if ((res.status === 200) & (res.data.result == 1)) {
            this.getUserList();
            return this.$message.success("删除用户成功!");
          }
        })
        .catch((err) => {
          return this.$message.error("删除用户失败!");
        });
    },
    // 展示分配角色对话框
    async setRole(userInfo) {
      this.userInfo = userInfo;

      this.setRoleDialogVisible = true;
    },
    // 点击按钮，分配角色
    async saveRoleInfo() {
      if (this.selectedRoleId == null) {
        return this.$message.error("请选择要分配的角色！");
      }
      

      let params = Qs.stringify({
        id:this.userInfo.id,
        role:this.selectedRoleId
      })

      axios
        .put("http://localhost:9090/user/updateRoleById?"+params)
        .then((res) => {
          if (res.status === 200 && res.data.result == 1) {
            this.getUserList();
            this.setRoleDialogVisible = false;
            return this.$message.success("修改用户角色成功");
          }
          return this.$message.error("修改用户角色失败");
        })
        .catch((err) => {
          return this.$message.error("修改用户角色失败");
        });
    },

    // 监听分配角色对话框的关闭
    setRoleDialogColsed() {
      this.selectedRoleId = "";
      this.userInfo = {};
    },
  },
};
</script>

<style lang="less" scoped></style>
