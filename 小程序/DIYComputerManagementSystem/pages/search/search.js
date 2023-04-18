// pages/search/search.js
import Notify from '../../miniprogram_npm/@vant/weapp/notify/notify';
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    navigationBarHeight:0,
    recommends:['显卡','CPU','机箱','电脑主板','内存条','GPU','硬盘','电脑风扇','笔记本转轴','主板电源接口转向头','显卡/网卡挡板','主板支架','路由器','信号线'],
    searchvalue:"",
    //判断有没有点击搜索
    okSearch:false,
    //查询出来的商品
    searchGoods:[]
  },
  //选择商品进入详情页
  selectGoods(msg) {
    //console.log(msg.currentTarget.dataset.goodsid);
    wx.navigateTo({
      url: '../goodsDetial/goodsDetial?id='+msg.currentTarget.dataset.goodsid,
    })
  },
  //输入内容变化事件
  onChange(e){
    this.setData({
      searchvalue:e.detail
    })
  },
  //点击搜索
  onClick(){
    let _this = this;
    wx.request({
      url: app.globalData.appURL+'goods/getGoodsListX', 
      method:'GET',
      data: {
        goodsName:this.data.searchvalue
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded' // 默认值
      },
      success (res) {
        //console.log(res.data);
        _this.setData({
          okSearch:true,
          searchGoods:res.data
        })
      },
      fail(err){
        Notify({type:'warning',message:'搜索失败',duration:500})
      }
    })
  },
  //键盘搜索
  onSearch(){
    this.onClick()
  },
  //标签选中事件
  select_recommend(e){
    this.setData({
      searchvalue:this.data.recommends[e.currentTarget.dataset.id]
    })
  },

  

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let data = wx.getMenuButtonBoundingClientRect()
    this.setData({
      navigationBarHeight:data.bottom+5
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