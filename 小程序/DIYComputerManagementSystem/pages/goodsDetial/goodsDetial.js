// pages/goodsDetial/goodsDetial.js
import Notify from '../../miniprogram_npm/@vant/weapp/notify/notify';
import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: 0,
    goodsInfo: {}
  },

  //添加购物车
  addCart() {
    let _this = this;
    //判断有没有登录
    wx.getStorage({
      key: 'userInfo',
      success(res) {
        
        let params = {
          userid: JSON.parse(res.data).id,
          goodsid:_this.data.goodsInfo.id,
          address:JSON.parse(res.data).address,
          paymoney:0,
          ispay:0
        }
        wx.request({
          url: app.globalData.appURL+'order/insertOrderX',
          method: 'POST',
          data: params,
          header: {
            'content-type': 'application/x-www-form-urlencoded' // 默认值
          },
          success(res) {
            wx.showToast({
              title: '添加成功',
              icon: 'success',
              duration: 500
            })
          },
          fail(err) {
            Notify({ type: "warning", message: "添加失败！", duration: 1000 })
          }
        })
      },
      fail(err) {
        Notify({ type: "warning", message: "抱歉，您还没有登录！", duration: 1000 })
      }
    })
  },

  //购买
  btnBuy(){
    let _this = this;
    //判断有没有登录
    wx.getStorage({
      key: 'userInfo',
      success(res) {
        //确定购买嘛
        Dialog.confirm({
          title: '提示',
          message: '确定购买嘛？',
        })
          .then(() => {
            // 确定
            let params = {
              userid: JSON.parse(res.data).id,
              goodsid:_this.data.goodsInfo.id,
              address:JSON.parse(res.data).address,
              paymoney:_this.data.goodsInfo.price,
              ispay:1
            }
            wx.request({
              url: app.globalData.appURL+'order/insertOrderX',
              method: 'POST',
              data: params,
              header: {
                'content-type': 'application/x-www-form-urlencoded' // 默认值
              },
              success(res) {
                wx.showToast({
                  title: '购买成功',
                  icon: 'success',
                  duration: 500
                })
              },
              fail(err) {
                Notify({ type: "warning", message: "购买失败！", duration: 1000 })
              }
            })
          })
          .catch(() => {
            // on cancel
            
          });       
      },
      fail(err) {
        Notify({ type: "warning", message: "抱歉，您还没有登录！", duration: 1000 })
      }
    })
    
  },

  //去购物车
  toMyCart(){
    wx.switchTab({
      url: '../mycart/mycart',
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    //console.log(options)
    this.setData({
      id: options.id
    });
    let _this = this;
    wx.request({
      url: app.globalData.appURL+'goods/getGoodsByIdX',
      data: {
        id: _this.data.id
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded' // 默认值
      },
      success(res) {

        _this.setData({
          goodsInfo: res.data.data
        })
      },
      fail(err) {
        Notify({ type: "warning", message: "商品信息获取失败", duration: 1000 })
      }
    })
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