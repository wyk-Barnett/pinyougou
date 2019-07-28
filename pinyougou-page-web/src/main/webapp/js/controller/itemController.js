app.controller("itemController",function ($scope) {

	//数量增减
    $scope.addNum=function(x){
        $scope.num=$scope.num+x;
        if($scope.num<1){
            $scope.num=1;
        }
	};
	
	//存储用户选择的规格
	$scope.specificationItems={};
	
	//添加 用户选择规格选项
	$scope.selectSpecification=function(key,value){
		$scope.specificationItems[key]=value;
        $scope.searchSku();
	};
	
	//判断规格选项是否被选中
	$scope.isSelected=function(key,value){
		return $scope.specificationItems[key]==value;
	};

	//当前选择的sju
	$scope.sku={};
	
	//加载默认的sku
	$scope.loadSku=function () {
		$scope.sku=skuList[0];
		$scope.specificationItems=JSON.parse(JSON.stringify($scope.sku.spec));
    };

	//判断两个对象是否相等
	matchObject=function (map1, map2) {
		//判断对象的key/属性 个数是否相等
		if ( Object.getOwnPropertyNames(map1).length != Object.getOwnPropertyNames(map2).length ){
			return false;
		}
		//判断key对应 的value是否相等
		for (var key in map1){
			if ( map1[key] != map2[key] ){
				return false;
			}
		}
		return true;
    };

	//根据规格查询sku
	$scope.searchSku=function () {
        for (var i = 0; i < skuList.length; i++) {
			if (matchObject(skuList[i].spec,$scope.specificationItems)) {
                $scope.sku=skuList[i];
                return;
			}
        }
        $scope.sku={id:0,title:'暂时没货',price:0};
    };

	$scope.addToCart=function () {
		alert("数量:"+$scope.num +"----" + "skuid"+$scope.sku.id);
    }


});