// components/navigationbar/navigationbar.js
const app = getApp()
Component({
    properties: {
      navigationBarHeight:0,
      navbarData: ''
    },
    data: {
        
    },
    attached: function () {
        // 定义导航栏的高度   方便对齐
        this.setData({
        })
    },
    methods: {
        // 返回上一页面
        _navback() {
           
        },
        // 搜索功能
        search: function (e) {
          wx.navigateTo({
            url: '/pages/search/search',
          })
        },
        // 清空搜索框
        clearInput:function(){
            
        },
        
    }
 
})