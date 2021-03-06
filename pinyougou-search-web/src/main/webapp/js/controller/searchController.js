app.controller("searchController",function ($scope,$location, searchService) {

    //定义搜索对象的结构  category:商品分类
    $scope.searchMap={"keywords":"","category":"","brand":"",spec:{},price:"",pageNum:1,pageSize:40,sort:"",sortField:""};

    //搜索方法
    $scope.search=function () {
        $scope.searchMap.pageNum = parseInt( $scope.searchMap.pageNum);
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap=response;
            buildPageLabel();
        })
    };

    //构建分页栏
    buildPageLabel=function(){
        $scope.pageLabel=[];
        var firstPage = 1;
        var lastPage = $scope.resultMap.totalPages;

        $scope.firstDot=true;//前面由点....
        $scope.lastDot=true;//后面由点....

        if (lastPage > 5) {
            if ($scope.searchMap.pageNum <= 3){
                lastPage = 5;
                $scope.firstDot=false;
            }else if ($scope.searchMap.pageNum >= lastPage-2){
                firstPage = lastPage - 4;
                $scope.lastDot=false;
            } else {
                firstPage = $scope.searchMap.pageNum - 2;
                lastPage = $scope.searchMap.pageNum + 2;
            }
        }else {
            $scope.firstDot=false;
            $scope.lastDot=false;
        }
        //构建页码
        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLabel.push(i);
        }
    };

    //点击页码跳转
    $scope.queryForPage=function(pageNum){
        if (pageNum < 1 || pageNum > $scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNum=pageNum;
        $scope.search();
    };

    //添加搜索项,改变searchMap的值
    $scope.addSearchItem=function (key, value) {
        if (key == "category" || key == "brand" || key == "price"){
            //是分类或品牌
            $scope.searchMap[key]=value;
        }else {
            //是规格
            $scope.searchMap.spec[key]=value;
        }
        $scope.search();
    };

    //移除搜索项
    $scope.removeSearchItem=function (key) {
        if (key == "category" || key == "brand" || key == "price"){
            //是分类或品牌
            $scope.searchMap[key]="";
        }else {
            //是规格
            delete $scope.searchMap.spec[key];
        }
        $scope.search();
    };
    //判断当前页是否为第一页
    $scope.isTopPage=function () {
        if ($scope.searchMap.pageNum == 1){
            return true;
        } else {
            return false;
        }
    };
    //判断当前页是否为最后一页
    $scope.isEndPage=function () {
        if ($scope.searchMap.pageNum==$scope.resultMap.totalPages){
            return true;
        } else {
            return false;
        }
    };

    //排序搜索
    $scope.sortSearch=function (sortField,sort) {
        $scope.searchMap.sortField=sortField;
        $scope.searchMap.sort=sort;
        $scope.search();
    };

    $scope.keywordsIsBrand=function () {
        var keywords = $scope.searchMap.keywords;
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            var brand = $scope.resultMap.brandList[i].text;
            if (keywords.indexOf(brand)>=0) {
                return true;
            }
        }
        return false;
    };

    //加载关键字
    $scope.loadKeywords=function () {
        $scope.searchMap.keywords=$location.search()["keywords"];
        $scope.search();
    }

});