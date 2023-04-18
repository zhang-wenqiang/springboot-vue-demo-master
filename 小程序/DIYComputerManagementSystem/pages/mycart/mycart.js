// pages/mycart/mycart.js
import Dialog from '../../miniprogram_npm/@vant/weapp/dialog/dialog';
import Notify from '../../miniprogram_npm/@vant/weapp/notify/notify';
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {

    isLogin: false,
    myOrderList: [],
    totalPrice: 0
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
    //设置当前页面被选中
    let _this = this;
    this.getTabBar().setData({
      selected: 1
    });
    //判断有没有登录
    wx.getStorage({
      key: 'userInfo',
      //登录了就请求购物车内容
      success(res) {
        _this.setData({
          isLogin: true
        })
        //查询购物车
        _this.selectCart(JSON.parse(res.data).id);
      },
      //没有登录就显示未登录
      fail(err) {
        _this.setData({

          isLogin: false,
          myOrderList: [],
          totalPrice: 0
        })
      }
    })
  },

  //查询购物车
  selectCart(userid) {
    let _this = this;
    wx.request({
      url: app.globalData.appURL+'order/getOrderNoPay',
      data: {
        id: userid
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded' // 默认值
      },
      success(res) {
        //设置未付款的订单（即是购物车）
        _this.setData({
          myOrderList: res.data
        })
        //统计总价
        _this.computeTotalPrice();
      },
      fail(err) {
        console.log(err)
      }
    })
  },

  //统计总价
  computeTotalPrice() {

    let array = this.data.myOrderList;
    let total = 0;
    array.forEach(element => {
      total = total + element.price;
    });

    this.setData({
      totalPrice: total
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
      _this.delete(msg.currentTarget.dataset.orderid);
    }).catch(() => {
      // on cancel
    });
  },

  //删除订单业务
  delete(orderid) {
    //console.log(orderid)
    let _this = this;
    wx.request({
      url: app.globalData.appURL+'order/deleteOrderX?id=' + orderid,
      method: 'DELETE',
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      success(res) {
        if (res.data.result == 1) {
          Notify({ type: 'success', message: res.data.msg, duration: 500 });
          //重新请求购物车
          wx.getStorage({
            key: 'userInfo',
            success(res) {
              _this.selectCart(JSON.parse(res.data).id);
            }
          })
        } else {
          Notify({ type: 'warning', message: res.data.msg, duration: 500 })
        }
      },
      fail(err) {

        Notify({ type: 'warning', message: '删除失败', duration: 500 })
      }
    })
  },

  //提交订单业务
  onSubmit() {
    let array = this.data.myOrderList;
    let _this = this;
    Dialog.confirm({
      title: '提交订单',
      message: '确认购买嘛？',
    })
      .then(() => {
        // on confirm
        for (let index = 0; index < array.length; index++) {
          _this.updateIsPay(array[index].orderid, array[index].price);
        }
        //重新请求购物车
        wx.getStorage({
          key: 'userInfo',
          success(res) {
            _this.selectCart(JSON.parse(res.data).id);
          }
        })
      })
      .catch(() => {
        // on cancel
      });
  },

  //修改商品为已付款
  updateIsPay(orderid, paymoney) {
    wx.request({
      url: app.globalData.appURL+'order/updateIsPay?orderid=' + orderid + '&paymoney=' + paymoney,
      method: 'PUT',
      header: {
        'content-type': 'application/x-www-form-urlencoded' // 默认值
      },
      success(res) {
        console.log(res.data)
      },
      fail(err) {
        Notify({ type: 'warning', message: '提交失败', duration: 500 })
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