// index.js
// 获取应用实例
var app = getApp()

Page({

  data: {
    interval: 2000,
    background: ['/images/swiper01.png', '/images/swiper02.png', '/images/swiper03.png', '/images/swiper04.png', '/images/swiper05.png'],
    navigationBarHeight: 0,
    goods10:[],
  },

  //选择商品进入详情页
  selectGoods(msg) {
    //console.log(msg.currentTarget.dataset.goodsid);
    wx.navigateTo({
      url: '../goodsDetial/goodsDetial?id='+msg.currentTarget.dataset.goodsid,
    })
  },

  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认，开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    wx.getUserProfile({
      desc: '展示用户信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        console.log(res)
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    })
  },

  //页面周期函数：页面显示时
  onShow: function () {
    this.getTabBar().setData({
      selected: 0
    })
    
  
  },


  //页面加载执行函数
  onLoad() {
    let data = wx.getMenuButtonBoundingClientRect()
    this.setData({
      navigationBarHeight: data.bottom + 5
    });
    let _this = this;
    wx.request({
      url: app.globalData.appURL+'goods/get10Goods', 
      method: 'GET',
      data: {},
      header: {
        'content-type': 'application/x-www-form-urlencoded' // 默认值
      },
      success(res) {
        _this.setData({
          goods10:res.data
        })
      }
    })
  },

  
})
