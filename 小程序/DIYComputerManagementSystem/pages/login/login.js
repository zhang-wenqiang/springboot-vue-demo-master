// pages/login/login.js
import Notify from '../../miniprogram_npm/@vant/weapp/notify/notify';
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    account: "",
    password: ""

  },

  // 登录函数
  loginCheck() {
    if (this.data.account.trim() != '' && this.data.password.trim() != '') {
      //进行网络请求
      wx.request({
        url: app.globalData.appURL+'user/loginX', //仅为示例，并非真实的接口地址
        method: "POST",
        data: {
          account: this.data.account,
          password: this.data.password
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded' // 默认值
        },
        success(res) {
          //console.log(res.data)
          if (res.data.result == 1) {
            Notify({ type: "success", message: '登录成功', duration: 1000 });
            wx.setStorage({
              key: "userInfo",
              data: JSON.stringify(res.data.data)
            });
            wx.switchTab({
              url: '/pages/mine/mine',
            })
          } else {
            Notify({ type: "warning", message: '账号或密码错误', duration: 1000 });
          }

        },
        fail(err) {
          Notify({ type: "warning", message: '失败', duration: 1000 });
        }
      })
    } else {
      Notify({ type: 'warning', message: '账号或密码不能为空', duration: 1000 });

    }
  },
  //去注册页面
  toRegister() {
    wx.navigateTo({
      url: '/pages/register/register',
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