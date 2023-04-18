// pages/myorders/myorders.js
import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';
import Notify from '../../miniprogram_npm/@vant/weapp/notify/notify';
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    myOrderList:[]
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
    this.getOrderList();
  },

  //请求订单列表
  getOrderList(){
    let _this = this;
    wx.getStorage({
      key:'userInfo',
      success(res){
        wx.request({
          url: app.globalData.appURL+'order/getOrderByUserIdX', 
          data: {
            userid:JSON.parse(res.data).id
          },
          header: {
            'content-type': 'application/x-www-form-urlencoded' // 默认值
          },
          success (res) {
            
            _this.setData({
              myOrderList:res.data
            })
          },
          fail(err){
            console.log(err)
          }
        })
      }
    })
  },
  //删除订单
  deleteOrder(msg) {
    let _this = this;
    //console.log(msg.currentTarget.dataset.orderid)
    Dialog.confirm({
      title: '请确认',
      message: '确定要删除该订单嘛？',
    }).then(() => {
      // on confirm
      _this.delete(msg.currentTarget.dataset.id);
    }).catch(() => {
      // on cancel
    });
  },
  //删除订单业务
  delete(id) {
    //console.log(orderid)
    let _this = this;
    wx.request({
      url: app.globalData.appURL+'order/deleteOrderX?id=' + id,
      method: 'DELETE',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      success(res) {
        if (res.data.result == 1) {
          Notify({ type: 'success', message: res.data.msg, duration: 500 });
          //重新请求订单
          _this.getOrderList();
        } else {
          Notify({ type: 'warning', message: res.data.msg, duration: 500 })
        }
      },
      fail(err) {

        Notify({ type: 'warning', message: '删除失败', duration: 500 })
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