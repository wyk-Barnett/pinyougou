app.controller("baseController",function($scope){
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
    //根据key获取json数据中的value,并进行拼接,返回拼接后的字符串
    $scope.formatJSON=function (jsonStr, key) {
        var json = JSON.parse(jsonStr);
        var value = "";
        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                value+=",";
            }
            value+=json[i][key];
        }
        return value;
    };
    //根据key在集合中查找value
    $scope.searchObjectByKey=function (list, key, value) {
        for (var i = 0; i < list.length; i++) {
            if (list[i][key]==value){
                return list[i];
            }
        }
        return null;
    }
});