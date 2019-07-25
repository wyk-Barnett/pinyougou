app.controller("contentController",function ($scope, contentService) {

    $scope.contentList=[];
    $scope.findByCategoryId=function (categoryId) {
        contentService.findByCategoryId(categoryId).success(function (response) {
            $scope.contentList[categoryId]=response;
        })
    };

    //页面跳转,参数传递
    $scope.search=function () {
        location.href="http://localhost:9104/search.html#?keywords="+$scope.keywords;
    }

});