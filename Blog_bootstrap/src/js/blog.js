// import '../js/jquery/2.0.0/jquery.min.js'
import '../css/bootstrap/3.3.6/bootstrap.min.css'
import '../js/jquery/2.0.0/pagination.min.js'
import '../css/pagination.css'
// import 'pagination'
// import '../js/bootstrap/3.3.6/bootstrap.min.js'
import 'bootstrap'
import '../css/blog.css';
// var url_head = "http://localhost:8080/"

var number_cnt = new Map()
    // 页面初始化：填充数据
window.onload = function() {
    $.ajax({
        url: window.url_head + "api/category/list/",
        type: "GET",
        dataType: "json",
        success: function(json) {

            var cnt = 0
                // 先填充分类信息
            $.each(json, function(i, item) {
                // 填充分类信息
                var categoryInfo = document.querySelector("#categoryInfo");
                categoryInfo.content.querySelector("a").innerHTML = item.name + "(" + item.number + ")";
                number_cnt.set(item.id, item.number)
                cnt += item.number;
                categoryInfo.content.querySelector("a").href = "?categoryId=" + item.id + "&pageIndex=0";
                document.getElementById("category").appendChild(categoryInfo.content.cloneNode(true));
            });

            number_cnt.set(-1, cnt)
                // console.log(number_cnt)

            var categoryId = getQueryVariable("categoryId") == false ? -1 : getQueryVariable("categoryId");
            var pageIndex = getQueryVariable("pageIndex") == false ? 0 : getQueryVariable("pageIndex");

            // console.log(number_cnt.get(parseInt(categoryId)))
            // console.log(categoryId === 1)
            // console.log('aaaa')
            $("#Pagination").pagination(Math.ceil(number_cnt.get(parseInt(categoryId)) / 8) * 8, { //total不能少        
                callback: PageCallback,
                pageSize: 8,
                prev_text: '上一页',
                next_text: '下一页',
                // num_display_entries: 4, //连续分页主体部分显示的分页条目数
                // num_edge_entries: 1, //两侧显示的首尾分页的条目数 
            });

            function PageCallback(index, jq) { //前一个表示您当前点击的那个分页的页数索引值，后一个参数表示装载容器。  
                // console.log(index)

                var e1 = document.getElementsByClassName('articleInfo')

                for (var i = e1.length - 1; i >= 0; i--) {
                    var e = e1[i]
                    e.parentElement.removeChild(e);
                }

                if (categoryId != -1)
                    showArticleByCategoryId(categoryId, index + 1, 8);
                else
                    showAllArticleInfo(index + 1, 8)
            }

        }
    });
};

// 获取网页中的参数
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return (false);
}





// 显示全部文章信息
function showAllArticleInfo(pageNum, pageSize) {

    $.ajax({
        type: "get",
        url: url_head + "api/article/list" + "?pageIndex=" + pageNum + "&pageSize=" + pageSize,
        dataType: "json",
        success: function(json) {
            $.each(json.list, function(i, item) {
                // 填充文章信息
                var articleInfo = document.querySelector("#articleInfo");
                articleInfo.content.querySelector("img").src = item.pictureUrl;
                // console.log(item.top)
                if (item.isTop == true) {
                    articleInfo.content.querySelector("h5").innerHTML = "<font color=\"#FF0000 \">[置顶]</font>" + item.title;
                    //					articleInfo.content.querySelector("h5").style.fontWeight = "bold";
                } else {
                    articleInfo.content.querySelector("h5").innerHTML = item.title;
                }
                articleInfo.content.querySelector("h6").innerHTML = item.id;
                document.getElementById("articleInfos").appendChild(articleInfo.content.cloneNode(true));

                var articleInfos = document.getElementsByClassName('articleInfo')

                Array.from(articleInfos).forEach(element => element.addEventListener('click', function() {
                    // console.log('aaaa')
                    var articleId = $(element).children("h6").text();
                    var url = "article.html?articleId=" + articleId;
                    window.location.href = url;
                }));

            });
        }
    });
}

// 显示指定分类下的文章列表
function showArticleByCategoryId(id, pageNum, pageSize) {
    $.ajax({
        type: "get",
        url: url_head + "api/article/list/sort/" + id + "?pageIndex=" + pageNum + "&pageSize=" + pageSize,
        dataType: "json",
        success: function(json) {
            $.each(json.list, function(i, item) {
                // 填充文章信息
                var articleInfo = document.querySelector("#articleInfo");
                articleInfo.content.querySelector("img").src = item.pictureUrl;
                if (item.isTop == true) {
                    articleInfo.content.querySelector("h5").innerHTML = "<font color=\"#FF0000 \">[置顶]</font>" + item.title;
                    //					articleInfo.content.querySelector("h5").style.fontWeight = "bold";
                } else {
                    articleInfo.content.querySelector("h5").innerHTML = item.title;
                }
                articleInfo.content.querySelector("h6").innerHTML = item.id;
                //不会拷贝那些使用 addEventListener() 方法或者 node.onclick = fn 这种用JavaScript动态绑定的事件.
                document.getElementById("articleInfos").appendChild(articleInfo.content.cloneNode(true));
            });

            var articleInfos = document.getElementsByClassName('articleInfo')

            Array.from(articleInfos).forEach(element => element.addEventListener('click', function() {
                console.log('aaaa')
                var articleId = $(element).children("h6").text();
                var url = "article.html?articleId=" + articleId;
                window.location.href = url;
            }));
        }
    });
}

//html.onclick必须绑定全局方法
//webpack的module打包后不再是全局方法
// 跳转到指定文章
// document.getElementsByClassName('articleInfo')[0].addEventListener('click',
//     function(e) {
//         //html.this自动给js函数绑定当前标签
//         _this = e.target
//         var articleId = $(_this).children("h6").text();
//         var url = "article.html?articleId=" + articleId;
//         window.location.href = url;
//     }
// )

// function showArticle(_this) {
//     //html.this自动给js函数绑定当前标签
//     var articleId = $(_this).children("h6").text();
//     var url = "article.html?articleId=" + articleId;
//     window.location.href = url;
// }