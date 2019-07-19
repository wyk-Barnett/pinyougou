 //控制层 
app.controller('goodsController' ,function($scope,$controller ,$location ,goodsService,itemCatService,brandService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	};
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};

    //查询实体
    $scope.findOne=function(){
        var id = $location.search()["id"];
        if (id!=null){
            goodsService.findOne(id).success(
                function(response){
                    $scope.entity= response;
                    editor.html($scope.entity.goodsDesc.introduction);

                    $scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);
                    $scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                    $scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems);

                    for (var i = 0; i < $scope.entity.itemList.length; i++) {
                        $scope.entity.itemList[i].spec  = JSON.parse($scope.entity.itemList[i].spec);
                    }
                }
            );
        }
    };

    $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
    $scope.add_image_entity=function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    };
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	};
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};
	//状态定义
    $scope.status=["未审核","已审核","审核未通过","已关闭"];
	//读取商品分类
    $scope.itemCatList=[];
    $scope.findItemCatList=function () {
        itemCatService.findAll().success(function (response) {
            for (var i = 0; i < response.length; i++) {
                $scope.itemCatList[response[i].id]=response[i].name;
            }
        });
    };
    //读取品牌列表
    $scope.brandList=[];
    $scope.findBrandList=function () {
        brandService.findAll().success(function (response) {
            for (var i = 0; i < response.length; i++) {
                $scope.brandList[response[i].id]=response[i].name;
            }
        });
    };

    $scope.updateStatus=function (status) {
		goodsService.updateStatus($scope.selectIds,status).success(function (response) {
			if (response.success){
				//刷新页面并清空复选数组
				$scope.selectIds=[];
				$scope.reloadList();
			}else {
				alert(response.message);
			}
        });
    }

    
});	
