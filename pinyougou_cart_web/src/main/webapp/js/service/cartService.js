app.service("cartService",function ($http) {

    this.findCartList=function () {
        return $http.get("../cart/findCartList.do");
    };

    this.addGoodsToCartList=function (itemId, num) {
        return $http.get("../cart/addGoodsToCartList.do?itemId="+itemId+"&num="+num)
    };

    this.sum=function (cartList) {
        var totalValue={totalNum:0,totalMoney:0};

        for (var i = 0; i < cartList.length; i++) {
            var cart = cartList[i];

            for (var j = 0; j < cart.orderItemList.length; j++) {
                var orderItem=cart.orderItemList[j];
                //累加明细数量 到totalNum
                totalValue.totalNum+=orderItem.num;
                //累加明细小计,totalMoney
                totalValue.totalMoney+=orderItem.totalFee;
            }
        }
        return totalValue;
    }
});