 //控制层 
app.controller('goodsController' ,function($scope,$controller,goodsService,uploadService,itemCatService,typeTemplateService){
	
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
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	};
	
	//保存 
	$scope.add=function(){
		$scope.entity.goodsDesc.introduction=editor.html();
        goodsService.add( $scope.entity).success(
			function(response){
				if(response.success){
					alert(response.message);
		        	$scope.entity={};
		        	//清空副文本编辑器
                    editor.html("");
				}else{
					alert(response.message);
				}
			}		
		);				
	};
	
	 
	//根据索引删除列表数组中的图片
	$scope.dele=function(index){
		$scope.entity.goodsDesc.itemImages.splice(index,1);
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


	$scope.uploadFile=function () {
		uploadService.uploadFile().success(function (response) {
			if (response.success){
				$scope.image_entity.url=response.message;
			}else {
				alert(response.message);
			}
        })
    };

	$scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
	$scope.add_image_entity=function () {
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    };

	//查询一级商品分类级别
	$scope.selectItemCat1List=function () {
		itemCatService.findByParentId(0).success(function (response) {
			$scope.itemCat1List=response;
        });
    };
	//检测一级分类列表变化,给二级分类列表赋值
	$scope.$watch("entity.goods.category1Id",function (newValue, oldValue) {
		itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat2List=response;
        });
    });
    //检测二级分类列表变化,给三级分类列表赋值
    $scope.$watch("entity.goods.category2Id",function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCat3List=response;
        });
    });

    //检测三分类列表变化,给模板id赋值
    $scope.$watch("entity.goods.category3Id",function (newValue, oldValue) {
       itemCatService.findOne(newValue).success(function (response) {
		   $scope.entity.goods.typeTemplateId=response.typeId;
       });
    });
	//检测模板id,给品牌列表赋值
    $scope.$watch("entity.goods.typeTemplateId",function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(function (response) {
			$scope.typeTeplate=response;
            $scope.typeTeplate.brandIds=JSON.parse($scope.typeTeplate.brandIds);
            $scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.typeTeplate.customAttributeItems);
        });
        typeTemplateService.findSpecList(newValue).success(function (response) {
			$scope.specList=response;
        });
    });

	//$scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};
	//根据选中规格选项往集合中添加数据
    $scope.updateSpecAttribute=function ($event,name, value) {
		var object = $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,"attributeName",name);
		if (object!=null){
			if ($event.target.checked){
                object.attributeValue.push(value);
            }else {
                //已经被勾选,需要取消勾选,从集合中移除
                object.attributeValue.splice(object.attributeValue.indexOf(value),1);
                if (object.attributeValue.length == 0) {
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object));
				}
            }
		} else {
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}
    };

    //创建SKU列表
	$scope.createItemList=function () {
		//列表初始化
		$scope.entity.itemList=[{spec:{},price:0,num:9999,status:"0",isDefault:"0"}];
		var items = $scope.entity.goodsDesc.specificationItems;
		// alert(items);
        for (var i = 0; i < items.length; i++) {
            $scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
        }
    };
	//克隆往集合添加
	addColumn=function (list, columnName, columnValue) {
		var newList = [];
        for (var i = 0; i < list.length; i++) {
        	var oldRow=list[i];
            for (var j = 0; j < columnValue.length; j++) {
            	var newRow=JSON.parse(JSON.stringify(oldRow));
                newRow.spec[columnName]=columnValue[j];
				newList.push(newRow);
            }
        }
        return newList;
    }

});	
