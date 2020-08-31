var main = {
    init: function () {

        var _this = this;

        $('#btn-save').on('click', function (e) {
            _this.save();
        });

        $('#btn-update').on('click', function () {
            _this.update();
        });

        $('#btn-delete').on('click', function () {
            _this.delete();
        });

        $("input[name=uploadFile]").on('change', function () {
             _this.upload();
        });

        $(".uploadResult").on("click","button", function (e){
                       var targetFile = $(this).data("file");
                       var type = $(this).data("type");
                       var targetLi = $(this).closest("li");

                       console.log(targetFile);
                       console.log(type);
                       console.log(targetLi);

                         $.ajax({
                         url : '/api/v1/posts/deleteFile/', //+ id,
                         data : {fileName: targetFile, type: type},
                         dataType : 'text',
                         type : 'DELETE',
                         }).done(function (result) {
                         alert('fileDelete');
                         targetLi.remove();
                         console.log(result);
                         }).fail(function (result) {
                         console.log(result);
                         alert(JSON.stringify(result));
                         });
                   });

          $(".uploadResult").on("click","li", function (e){
                var point = $(this);
                _this.download(point);
           });

          $(".postLike").on("click","li", function(){
                console.log("like");
                var id = $('#id').val();
                var data = { likes : 1 , dislike : 0};

                $.ajax({
                        type: 'POST',
                        url: '/api/v1/posts/like/' + id,
                        dataType: 'json',
                        contentType: 'application/json; charset = utf-8',
                        data: JSON.stringify(data)
                        }).done(function (result) {
                        showLikeResult(result);
                        console.log("complete like");
                        }).fail(function (error) {
                        alert(JSON.stringify(error));
                        });

           });

          $(".postDislike").on("click","li",function(){
            var id = $('#id').val();
            var data = { likes : 0 , dislike : -1};

            $.ajax({
                 type: 'POST',
                 url: '/api/v1/posts/like/' + id,
                 dataType: 'json',
                 contentType: 'application/json; charset = utf-8',
                 data: JSON.stringify(data)
             }).done(function (result) {
                 console.log("complete dislike");
                 showLikeResult(result);
             }).fail(function (error) {
                 alert(JSON.stringify(error));
             });
          });

          function showLikeResult(result) {

                var totalLikes = result.totalLikes;
                var totalResult = result.totalResult;

                console.log(totalLikes);

                if(totalLikes == 0){
                    alert("두번 좋아요는 불가능 합니다");
                 }


                 else if(totalLikes == -1){
                      alert("두번 싫어요는 불가능 합니다");
                 }

                 var str = "";
                 var totalLikeUL = $(".totalLike ul a");
                 console.log(result);
                 str += "<li>"+totalResult+"</li>";
                 totalLikeUL.html(str);
           }

    },

    save: function () {

        var uploadList;
        var attachArray=[];

        $(".uploadResult ul li").each(function(i, obj){

          var jobj = $(obj);

            console.dir(jobj);
            console.log("-------------------------");
            console.log(jobj.data("filename"));

            uploadList = {
                'fileName' : jobj.data("filename"),
                'uuid' : jobj.data("uuid"),
                'uploadPath' : jobj.data("path"),
                'image' : jobj.data("type")

            };

         attachArray[i] = uploadList;

        });

        console.log(attachArray);

        var data = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val(),
            attachList: attachArray
        };

        console.log(JSON.stringify(data));

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset = utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('登録しました。');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    update: function () {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'put',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset = utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('修正しました。');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    delete: function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/' + id,
            dataType: 'json',
            contentType: 'application/json; charset = utf-8'
        }).done(function () {
            alert('削除しました。');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },


    upload: function () {
        var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
        var maxSize = 5242880; //5MB
        console.log("try upload");
        function checkExtension(fileName, fileSize) {

            if (fileSize >= maxSize) {
                alert("파일 사이즈 초과");
                return false;
            }

            if (regex.test(fileName)) {
                alert("해당 종류의 파일은 업로드할 수 없습니다.");
                return false;
            }
            return true;
        }
        console.log("try");
        changedUpload();

        function changedUpload(){

                var formData = new FormData();
                var inputFile = $("input[name='uploadFile']");
                var files = inputFile[0].files;
                console.log(files);

                //add filedate to formdata
                for (var i = 0; i < files.length; i++) {
                    if (!checkExtension(files[i].name, files[i].size)) {
                        return false;
                    }
                    formData.append("uploadFile", files[i]);
                }

                $.ajax({
                    url: '/api/v1/postsUploadForm',
                    processData: false,
                    contentType: false,
                    dataType: 'json',
                    data: formData,
                    type: 'POST'
                }).done(function (result) {
                    alert('Uploaded');
                    console.log(result);
                    showUploadedFile(result);
                }).fail(function (result) {
                    console.log(result);
                    alert(JSON.stringify(result));
                });

        }

        function showUploadedFile(uploadResultArr) {

          if(!uploadResultArr || uploadResultArr.length == 0){return;}

          var uploadUL = $(".uploadResult ul");
          var str = "";

           $(uploadResultArr).each(function (i, obj) {

                if(obj.image){

                 var fileCallPath =  encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid +"_"+obj.fileName);
                 var originalFileName = encodeURIComponent( obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
                console.log(fileCallPath);

                 str += "<li data-path='"+obj.uploadPath+"'";
                 			str +=" data-uuid='"+obj.uuid+"' data-fileName='"+obj.fileName+"' data-type='"+obj.image+"'"
                 			str +" ><div>";
                 			str += "<span>"+obj.fileName+"</span>";
                 			str += "<button type='button' data-file=\'"+fileCallPath+"\'"
                 			str += "data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
                 			str += "<img src= '/posts/display?fileName="+fileCallPath+"'></a>";
                 			str += "</div>";
                 			str += "</li>";

                 }else{

                 var fileCallPath =  encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
                 var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");

                 str += "<li "
                			str += "data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-fileName='"+obj.fileName+"' data-type='"+obj.image+"' ><div>";
                			str += "<span> "+ obj.fileName+"</span>";
                			str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' "
                			str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
                			str += "<img src='/img/attach.png'></a>";
                            str += "</div>";
                			str += "</li>";
                  }
             });
                    uploadUL.append(str);
        }
    },

     download: function(Obj) {
        var liObj = Obj;
        var path = encodeURIComponent(liObj.data("path")+"/" + liObj.data("uuid") + "_" + liObj.data("filename"));
        self.location ="/posts/download?fileName="+path
     }


};

main.init();