// 页面初始化：填充数据

import '../js/jquery/2.0.0/jquery.min.js'
import '../css/bootstrap/3.3.6/bootstrap.min.css'
// import '../js/bootstrap/3.3.6/bootstrap.min.js'
import "../css/index.css"



// var url_head = "http://localhost:8080/"
window.onload = function() {
    $.ajax({
        url: url_head + "api/article/list/latest",
        type: "GET",
        dataType: "json",
        success: function(json) {
            console.log(json);
            $.each(json, function(i, item) {
                // 设置右下角题图的内容
                $(".smallPictures img[location=" + i + "]").attr("src", item.pictureUrl);
                $(".smallPictures img[location=" + i + "]").attr("pictureUrl", item.pictureUrl);
                $(".smallPictures img[location=" + i + "]").attr("articleId", item.id);
                $(".smallPictures img[location=" + i + "]").attr("title1", item.title);
                // console.log($(".smallPictures img[location=" + i + "]").attr("title"));
                $(".smallPictures img[location=" + i + "]").attr("summary", item.summary);

                // 默认显示第一篇文章的信息
                if (i == "0") {
                    $("#articleTitle").html(item.title);
                    $("#articleSummary").html(item.summary);
                    $("#articlePicture img").attr("src", item.pictureUrl);
                    $("#showArticle").attr("articleId", item.id);
                }
            });
        },
        error: function(data, type, err) {
            console.log(err);
            console.log(type);
        }

    });
};

// 按钮点击进行文章详情页
$("#showArticle").on('click', function() {
    var articleId = $(this).attr("articleId");
    var url = "article.html?articleId=" + articleId; //全局var 自动全局变量
    console.log(url);
    window.location.href = url;
});

// 测试使用的函数
// $("#showArticle").click(function() {
// 	$.ajax({
// 		url: "http://10.2.3.235:80/api/article/1",
// 		type: "GET",
// 		dataType: "json",
// 		success: function(json) {
// 			$("#articleTitle").html(json.title);
// 			$("#articleSummary").html(json.summary);
// 		}
// 	})
// });

// 缩略图鼠标进入事件：更换大图和按钮的articleId
$(".smallPictures img").mouseenter(function() {
    var title = $(this).attr("title1");
    var pictureUrl = $(this).attr("pictureUrl");
    var articleId = $(this).attr("articleId");

    var summary = $(this).attr("summary");
    $("#articlePicture img").attr("src", pictureUrl);
    $("#showArticle").attr("articleId", articleId);

    $("#articleTitle").html(title);
    $("#articleSummary").html(summary);
});

// function checkPicurl(url) {
// 	var img = new Image();
// 	img.src = url;
// 	img.onerror = function() {
// 		alert(name + " 图片加载失败，请检查url是否正确");
// 		return false;
// 	};
//
// 	if(img.complete) {
// 		console.log(img.width + " " + img.height);
// 	} else {
// 		img.onload = function() {
// 			console.log(img.width + " " + img.height);
// 			img.onload = null;
// 			//避免重复加载
// 		}
// 	}
// }