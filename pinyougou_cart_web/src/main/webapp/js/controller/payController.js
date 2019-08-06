app.controller("payController",function ($scope,$location,payService) {

    $scope.createNative=function () {
        payService.createNative().success(function (response) {
            $scope.money=response.total_fee;
            $scope.out_trade_no=response.out_trade_no;
            var qr = new QRious({
                element:document.getElementById("qrious"),
                size:250,
                value:response.code_url,
                level:"H"
            });
            queryPayStatus($scope.out_trade_no);
        })
    };

    //查询支付状态
    queryPayStatus=function (out_trade_no) {
        payService.queryPayStatus(out_trade_no).success(function (response) {
            if (response.success){
                location.href="paysuccess.html#?money="+$scope.money;
            } else {
                if (response.message=="二维码超时") {
                    alert("二维码过期,请重新获取");
                    $scope.createNative();
                }else {
                    location.href="payfail.html";
                }
            }
        })
    };

    //获取支付金额,单位: 分
    $scope.getMoney=function () {
        $scope.payMoney=$location.search()["money"];
    }

});