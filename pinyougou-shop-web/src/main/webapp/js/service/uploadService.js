app.service("uploadService",function ($http) {

    //上传文件
    this.uploadFile=function () {
        var formdata = new FormData();
        //file 文件上传框的name
        formdata.append("file",file.files[0]);
        return $http({
            url:"../upload.do",
            method:"post",
            data:formdata,
            headers:{"Content-Type":undefined},
            transformRequest:angular.identity
        });
    }
});

