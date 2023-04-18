Component({
  data: {
    selected: 0,
    color: "#7A7E83",
    selectedColor: "#3cc51f",
    list: [{
      pagePath: "/pages/index/index",
      iconPath: "/images/bottom_main.png",
      selectedIconPath: "/images/bottom_main_selected.png",
      text: "主页"
    }, 
    {
      pagePath: "/pages/mycart/mycart",
      iconPath: "/images/bottom_mycart.png",
      selectedIconPath: "/images/bottom_mycart_selected.png",
      text: "购物车"
    },
    {
      pagePath: "/pages/mine/mine",
      iconPath: "/images/bottom_mine.png",
      selectedIconPath: "/images/bottom_mine_selected.png",
      text: "我的"
    }]
  },
  attached() {
  },
  methods: {
    switchTab(e) {
      const data = e.currentTarget.dataset
      const url = data.path
      wx.switchTab({url})
      this.setData({
        selected: data.index
      })
      
    }
  }
})