//品牌控制
app.controller('brandController',function($scope,$http,$controller,brandService){
    //引用父类基础控制器
    $controller("baseController",{$scope:$scope});

    //查询品牌列表
    $scope.findAll=function(){
        brandService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    };
    //分页查询
    $scope.findPage=function(currentPage,pageSize){
        brandService.findPage(currentPage,pageSize).success(
            function(response){
                $scope.list=response.rows;//显示当前页数据
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    };
    //新增修改
    $scope.save=function () {
        var serviceObject=null;
        if ($scope.entity.id!=null){
            serviceObject=brandService.update($scope.entity);
        }else {
            serviceObject=brandService.add($scope.entity);
        }
        serviceObject.success(function (response) {
            if (response.success) {
                //新增成功,刷新
                $scope.reloadList();
            }else {
                //新增失败,弹出提示消息
                alert(response.message);
            }
        })
    };
    //查询单个
    $scope.findOne=function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity=response;
        })
    };

    //删除功能
    $scope.dele=function () {
        brandService.dele($scope.selectIds).success(function (response) {
            if (response.success) {
                //删除成功,刷新
                $scope.reloadList();
            }else {
                //删除失败,弹出提示消息
                alert(response.message);
            }
        })
    };
    //条件查询
    $scope.searchEntity = {};
    $scope.search=function (currentPage,pageSize) {
        brandService.search(currentPage,pageSize,$scope.searchEntity).success(
            function (response) {
                $scope.list=response.rows;//显示当前页数据
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            })
    };
})