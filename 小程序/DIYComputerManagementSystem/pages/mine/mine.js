import Notify from "../../miniprogram_npm/@vant/weapp/notify/notify";

var app = getApp();
// pages/mine/mine.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isLogin: false,
    username:""
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
    this.getTabBar().setData({
      selected: 2
    });
    let _this = this;
    wx.getStorage({
      key: "userInfo",
      success(res) {
        //console.log(JSON.parse(res.data));
        _this.setData({
          isLogin: true,
          username:JSON.parse(res.data).name,
        })
      }
    })
  },
  //点击登录去登陆界面函数
  toLoginPage() {
    wx.navigateTo({
      url: '/pages/login/login'
    })

  },
  //退出
  exit() {
    wx.clearStorage()
    //console.log("123");
    this.setData({
      isLogin: false
    })
  },

  //进入订单页面
  toMyOrders(){
    wx.getStorage({
      key:'userInfo',
      success(res){
        wx.navigateTo({
          url: '../myorders/myorders',
        })
      },
      fail(err){
        Notify({type:'warning',message:'请先登录',duration:500})
      }
    })
    
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