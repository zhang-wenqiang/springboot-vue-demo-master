import Notify from "../../miniprogram_npm/@vant/weapp/notify/notify";
import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';
var app = getApp();
// pages/register/register.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

    //注册用户信息
    account: "",
    password: "",
    name: "",
    sex: "男",
    age: 18,
    address: "",
  },

  //性别改变事件
  onChange(event) {
    this.setData({
      sex: event.detail,
    });

  },

  //注册事件
  register() {
    let _this = this;
    if (this.data.account == "") {
      Notify({ type: "warning", message: "账号不能为空", duration: 500 });
      return "";
    }
    if (this.data.password == "") {
      Notify({ type: "warning", message: "密码不能为空", duration: 500 });
      return "";
    }
    if (this.data.name == "") {
      Notify({ type: "warning", message: "昵称不能为空", duration: 500 });
      return "";
    }
    if (this.data.age < 18) {
      Notify({ type: "warning", message: "年龄必须大于18岁", duration: 500 });
      return "";
    }
    if (this.data.address == "") {
      Notify({ type: "warning", message: "地址不能为空", duration: 500 });
      return "";
    }
    //如果条件都满足，那就进行注册
    let params = {
      account: this.data.account,
      password: this.data.password,
      name: this.data.name,
      sex: this.data.sex,
      age: this.data.age,
      address: this.data.address,
    }

    wx.request({
      url: app.globalData.appURL+'user/registerX', 
      data: params,
      method: "POST",
      header: {
        'content-type': 'application/x-www-form-urlencoded' // 默认值
      },
      success(res) {
        //console.log(res.data)
        if (res.data.result == 1) {
          //console.log(res.data.data);

          Dialog.confirm({
            title: '注册成功',
            message: '是否直接登录',
          })
            .then(() => {
              // on confirm
              wx.setStorage({
                key: "userInfo",
                data: JSON.stringify(res.data.data)
              });
              wx.switchTab({
                url: '/pages/mine/mine',
              })
            })
            .catch(() => {
              // on cancel
              _this.setData({
                account: "",
                password: "",
                name: "",
                sex: "男",
                age: 18,
                address: "",
              })
            });
        } else {
          Notify({ type: "warning", message: "该昵称已存在", duration: 500 })
        }
      },
      fail(err) {
        Notify({ type: "warning", message: "注册失败", duration: 500 })
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})