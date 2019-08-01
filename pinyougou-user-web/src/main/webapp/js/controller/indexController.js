app.controller("indexController",function ($scope,$controller,loginService) {

    //继承
    $controller('baseController',{$scope:$scope});

    $scope.showName=function () {
        loginService.showName().success(function (response) {
            $scope.loginName=response.username;
        });
    };

});