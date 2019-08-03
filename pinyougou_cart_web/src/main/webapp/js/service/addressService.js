//服务层
app.service('addressService',function($http){

	//增加 
	this.add=function(entity){
		return  $http.post('../address/add.do',entity );
	};
	//修改 
	this.update=function(entity){
		return  $http.post('../address/update.do',entity );
	};

    //查询实体
    this.findOne=function(id){
        return $http.get('../address/findOne.do?id='+id);
    };

	//删除
	this.dele=function(id){
		return $http.get('../address/delete.do?id='+id);
	};

	//获得当前登录账号的地址
	this.findAddressList=function () {
		return $http.get("../address/findListByLoginUser.do");
    }



});
