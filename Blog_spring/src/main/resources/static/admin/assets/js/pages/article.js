
url_head="http://121.199.41.175:9090/"
$(document).ready(function () {
    // 填充博文分类信息
    $.ajax({
        url: url_head+"api/category/list",
        type: "GET",
        dataType: "json",
        success: function (json) {
            $.each(json, function (i, item) {
                $('#articleCategories').append('<option categoryId="' + item.id + '">' + item.name + '</option>');
                $('#addCategories').append('<option categoryId="' + item.id + '">' + item.name + '</option>');
            });
        }
    });

    // 填充博文列表信息
    $.ajax({
        type: "get",
        url: url_head+"api/article/list?pageSize=1000&pageIndex=1",
            //url_head+"api/article/list?pageSize=1000&pageIndex=1",
        dataType: "json",
        success: function (json) {
            $.each(json.list, function (i, item) {
                $('#tbody-articles').append(
                    '<tr><td>' + +item.id +
                    '</td><td>' + item.title +
                    '</td><td>' + item.isTop +
                    '</td><td>' + item.traffic +
                    '</td><td><a href="' + item.pictureUrl + '">点击这里</a></td>' +
                    '<td><button class="btn btn-success" onclick="updateArticle(' + item.id + ')"><i class="fa fa-edit"></i> 编辑</button> ' +
                    '<button class="btn btn-danger" onclick="deleteArticle(' + item.id + ')"><i class="fa fa-trash-o"> 删除</i></button></td></tr>');
            });
            $('#dataTables-articles').dataTable();

        }
    });

});

// 监听博文分类Select改变，改变注入相应的博文
document.getElementById("articleCategories").onchange = function () {
    var categoryId = $('#articleCategories option:selected').attr("categoryId");
    //	alert(categoryId);
    if (categoryId === "") {
        var url = url_head+"api/article/list?pageIndex=1&pageSize=1000";
    } else {
        var url = url_head+"api/article/list/sort/" + categoryId+"?pageIndex=1&pageSize=1000";
    }
    // 填充博文分类信息
    $.ajax({
        url: url,
        type: "GET",
        dataType: "json",
        success: function (json) {
            // 先清空博文数据
            $('#tbody-articles').html("");
            $.each(json.list, function (i, item) {
                $('#tbody-articles').append(
                    '<tr><td>' + +item.id +
                    '</td><td>' + item.title +
                    '</td><td>' + item.top +
                    '</td><td>' + item.traffic +
                    '</td><td><a href="' + item.pictureUrl + '">点击这里</a></td>' +
                    '<td><button class="btn btn-success" onclick="updateArticle(' + item.id + ')"><i class="fa fa-edit"></i> 编辑</button> ' +
                    '<button class="btn btn-danger" onclick="deleteArticle(' + item.id + ')"><i class="fa fa-trash-o"> 删除</i></button></td></tr>');
            });
            $('#dataTables-articles').dataTable();

        }
    });
};

// 删除按钮点击
function deleteArticle(id) {
    $('#confirmBtn').attr("articleId", id);
    $('#myModal').modal();
};

// 确认删除按钮点击
$('#confirmBtn').click(function () {
    var id = $(this).attr("articleId");
    $.ajax({
        type: "DELETE",
        url: url_head+"admin/article/" + id,
        success: function () {
            // 刷新页面
            location.reload();
        }
    });
});

// 编辑文章按钮点击
function updateArticle(id) {
    // 往模态框填充数据
    $('#updateBtn').attr("articleId", id);
    $.ajax({
        type: "get",
        url: url_head+"admin/article/" + id,
        dataType: "json",
        async: false,
        success: function (json) {
            $('#articleTitle').val(json.title);
            $('#articleSummary').val(json.summary);
            if (json.top == true) {
                document.getElementById("articleTop").checked = true;
            }
            // 填充分类数据
            $.ajax({
                url: url_head+"api/category/list",
                type: "GET",
                dataType: "json",
                async: false,
                success: function (json) {
                    $('#updateCategories').html("");
                    $.each(json, function (i, item) {
                        $('#updateCategories').append('<option categoryId="' + item.id + '">' + item.name + '</option>');
                    });
                }
            });
            var select = document.getElementById("updateCategories");
            for (var i = 0; i < select.options.length; i++) {
                if (select.options[i].innerHTML == json.categoryName) {
                    //					alert(i);
                    //					alert(select.options[i].innerHTML);
                    //					alert(json.categoryName);
                    select.options[i].selected = true;
                    break;
                }
            }
            $('#articlePicture').val(json.pictureUrl);
            $('#articleContent').val(json.content);
        }
    });

    // 显示模态框
    $('#updateModal').modal();
};

// 更新文章按钮点击事件
$('#updateBtn').click(function () {
    var articleId = $('#updateBtn').attr("articleId");
    var articleTitle = $('#articleTitle').val();
    var articleSummary = $("#articleSummary").val();
    //	if(document.getElementById("articleTop").checked == true) {
    //		var articleTop = 1;
    //		alert("")
    //	} else {
    //		var articleTop = 0;
    //	}
    var articleTop = document.getElementById("articleTop").checked;
    //	alert(articleTop);
    var articleCategoryId = $("#updateCategories option:selected").attr("categoryId");
    var articlePicture = $('#articlePicture').val();
    var articleContent = $('#articleContent').val();
    var article = {
        id: articleId,
        title: articleTitle,
        summary: articleSummary,
        isTop: articleTop,
        categoryId: articleCategoryId,
        pictureUrl: articlePicture,
        content: articleContent
    }
    $.ajax({
        type: "PUT",
        url: url_head+"admin/article/" + articleId,
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(article),
        success: function () {
            location.reload();
        },
        error: function () {
            location.reload();
        }
    });
});
var default_picurl=["https://pic.imgdb.cn/item/61f961d52ab3f51d914294d1.jpg",
    "https://cnblog-img-william.oss-cn-beijing.aliyuncs.com/img/3-1G010092K7.jpg",
    "https://cnblog-img-william.oss-cn-beijing.aliyuncs.com/img/3-1G010092P1.jpg",
    "https://cnblog-img-william.oss-cn-beijing.aliyuncs.com/img/3-1G010092P8.jpg",
    "https://cnblog-img-william.oss-cn-beijing.aliyuncs.com/img/3-1G010092S1.jpg",
    "https://cnblog-img-william.oss-cn-beijing.aliyuncs.com/img/3-1G010092R5.jpg"
]
// 增加文章按钮点击事件
$('#addArticleBtn').click(function () {
    var articleTitle = $('#addArticleTitle').val();
    var articleSummary = $("#addArticleSummary").val();
    var articleTop = document.getElementById("addArticleTop").checked;
    // alert(articleTop);
    var articleCategoryId = $("#addCategories option:selected").attr("categoryId");
    var articlePicture = $('#addArticlePicture').val();
    // console.log(articlePicture);
    if(articlePicture==="")
        articlePicture=default_picurl[Math.floor(Math.random() * 6 )]
    var articleContent = $('#addArticleContent').val();
    var article = {
        title: articleTitle,
        summary: articleSummary,
        isTop: articleTop,
        categoryId: articleCategoryId,
        pictureUrl: articlePicture,
        content: articleContent
    }
    $.ajax({
        type: "POST",
        url: url_head+"admin/article/",
        dataType: "text",//预期返回类型
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(article),
        success: function () {
            // toastr.success('上传成功','success');
            location.reload();
            // toastr.success('上传成功','success');
        },
        error: function (data,type, err) {
            //可能是服务器的错误 也可能是解析服务器返回信息的时候的报错
            // console.log(JSON.stringify(article))
            // console.log(JSON.parse(JSON.stringify(article)))
            // toastr.error('上传失败','error');
            // console.log(data);
            // console.log("ajax错误类型："+type);
            // console.log(err);
            location.reload();
            // toastr.error('上传失败','error');
        }
    });
});

//文件选择

// $('#file').fileinput({
//     language: 'zh',     //设置语言
//     dropZoneEnabled: false,      //是否显示拖拽区域
//     dropZoneTitle: "可以将图片拖放到这里",    //拖拽区域显示文字
//     // uploadUrl: 'file/imgSave',  //上传路径
//     showUpload: false, //是否显示上传按钮
//     showRemove: false, //显示移除按钮
//     showPreview: false, //是否显示预览
//     showCancel:false,   //是否显示文件上传取消按钮。默认为true。只有在AJAX上传过程中，才会启用和显示
//     showCaption: false,//是否显示文件标
//     allowedFileExtensions: ['md'],   //指定上传文件类型
//     maxFileSize: 0,
//     maxFileSize: 2048,   //上传文件最大值，单位kb
//     // uploadAsync: true,  //异步上传
//     // maxFileCount: 2    //上传文件最大个数。
//     }).on("fileuploaded", function(event,data) { //异步上传成功后回
//
//         $("#addArticleContent").val=data;
//         console.log(data);		//data为返回的数据
// });

function handleFiles(files)
{

    if(files.length){

        let file = files[0];
        let reader = new FileReader();
        reader.onload = function(){
            document.getElementById('addArticleContent').innerHTML = this.result;
        };
        reader.readAsText(file);
    }
}

toastr.options = {'closeButton': true}
