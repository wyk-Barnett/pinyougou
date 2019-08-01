 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	

	//注册用户
	$scope.reg=function () {
		if ($scope.password != $scope.entity.password){
			alert("两次输入密码不一致,请重新输入");
			$scope.password="";
			return;
		}

		userService.add($scope.entity,$scope.smsCode).success(function (response) {
			alert(response.message);
        });
    };

	$scope.sendCode=function () {
        if ($scope.entity.phone==null || $scope.entity.phone==""){
            alert("手机号不能为空");
            return;
        }
		userService.sendCode($scope.entity.phone).success(function (response) {
			alert(response.message);
        })
    };



    
});	
