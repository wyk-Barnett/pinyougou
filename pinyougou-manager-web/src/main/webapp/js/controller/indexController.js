app.controller("indexController",function($scope,loginService){
    //显示登录信息
    $scope.showLoginName=function () {
        loginService.loginName().success(function (response) {
            $scope.loginName=response.loginName;
        })
    }
});