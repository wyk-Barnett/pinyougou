//品牌服务层
app.service("brandService",function ($http) {
    //查询所有
    this.findAll=function () {
        return $http.get('../brand/findAll.do');
    };
    //分页查询
    this.findPage=function (currentPage,pageSize) {
        return $http.get('../brand/findByPage.do?currentPage='+currentPage +'&pageSize='+pageSize);
    };
    //添加品牌
    this.add=function (entity) {
        return $http.post("../brand/add.do",entity);
    };
    //修改品牌
    this.update=function (entity) {
        return $http.post("../brand/update.do",entity);
    };
    //按照id查询
    this.findOne=function (id) {
        return $http.get("../brand/findOne.do?id="+id);
    };
    //批量删除
    this.dele=function (ids) {
        return $http.get("../brand/delete.do?ids="+ids);
    };
    //按条件查询
    this.search=function (currentPage,pageSize,searchEntity) {
        return $http.post("../brand/search.do?currentPage="+currentPage+"&pageSize="+pageSize ,searchEntity);
    };
    //查询产品
    this.findBrandList=function () {
       return $http.get("../brand/findBrandList.do");
    }

});