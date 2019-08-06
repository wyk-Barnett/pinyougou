app.controller("cartController",function ($scope, cartService,addressService) {
   
    $scope.findCartList=function () {
        cartService.findCartList().success(function (response) {
            $scope.cartList=response;
            $scope.total = cartService.sum($scope.cartList);
        })
    };
    
    $scope.addGoodsToCartList=function (itemId, num) {
        cartService.addGoodsToCartList(itemId,num).success(function (response) {
            if (response.success){
                $scope.findCartList();//刷新列表
            } else {
                alert(response.message);
            }
        })
    };

    //读取列表数据绑定到表单中
    $scope.findAddressList=function(){
        addressService.findAddressList().success(
            function(response){
                $scope.addressList=response;
                for (var i = 0; i < $scope.addressList.length; i++) {
                    if ( $scope.addressList[i].isDefault=='1'){
                        $scope.address=$scope.addressList[i];
                        break;
                    }
                }
            }
        );
    };

    //订单对象
    $scope.order={paymentType:'1'};
    //选择支付类型
    $scope.selectPayType=function(type){
        $scope.order.paymentType=type;
    };

    $scope.submitOrder=function(){
        $scope.order.receiverAreaName=$scope.address.address;
        $scope.order.receiverMobile=$scope.address.mobile;
        $scope.order.receiver=$scope.address.contact;
        cartService.submitOrder( $scope.order).success(function (response) {
            alert(response.message);
            if (response.success){
                $scope.cartList=[];
                if ( $scope.order.paymentType=="1"){
                    //微信支付跳转到支付页面
                    location.href="pay.html";
                } else {
                    //货到付款跳转到提示页面
                    location.href="paysuccess.html";
                }
            } else {
                alert(response.message);
                location.href="payfail.html";
            }
        })
    };





    //地址处理***************
    //定义变量 记录选中地址
    $scope.address={};
    //将选中的地址添加为当前地址
    $scope.selectAddress=function(add){
        $scope.address=add;
    };
    //判断地址是否为当前选中地址
    $scope.isSelectAddress=function(add){
        return $scope.address==add;
    };
    //新增或修改地址
    $scope.save=function(){
        var serviceObject;//服务层对象
        if($scope.entity.id!=null){//如果有ID
            serviceObject=addressService.update( $scope.entity ); //修改
        }else{
            serviceObject=addressService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    //重新查询
                    // $scope.reloadList();//重新加载
                    alert(response.message);
                    $scope.entity={};
                }else{
                    alert(response.message);
                }
            }
        );
    };

    //查询单个地址对象回显
    $scope.findOne=function (id) {
        addressService.findOne(id).success(function (response) {
            $scope.entity=response;
        })
    };
    //删除地址对象
    $scope.dele=function (id) {
        addressService.dele(id).success(function (response) {
            if (response.success){
                $scope.findCartList();
            } else {
                alert(response.message);
            }
        })
    }



});