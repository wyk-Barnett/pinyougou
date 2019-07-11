//品牌控制
app.controller('brandController1',function($scope,$http,brandService){
    //查询品牌列表
    $scope.findAll=function(){
        brandService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    };
    //分页控件配置currentPage:当前页   totalItems :总记录数  itemsPerPage:每页记录数  perPageOptions :分页选项  onChange:当页码变更后自动触发的方法
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();
        }
    };
    //刷新列表
    $scope.reloadList=function(){
        $scope.search( $scope.paginationConf.currentPage ,  $scope.paginationConf.itemsPerPage );
    };

    //分页
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
    //定义一个集合,储存复选框被选中的品牌id
    $scope.selectIds=[];
    //改变复选框集合中的值
    $scope.updateSelection=function ($event,id) {
        if ($event.target.checked){
            //表示input标签的复选框被选中,再向集合中添加id值
            $scope.selectIds.push(id);
        }else {
            //再次点击,从集合中删除  根据元素获取在集合中的索引
            var index = $scope.selectIds.indexOf(id);
            //删除指定索引位置的元素,删除一位
            $scope.selectIds.splice(index,1);
        }
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