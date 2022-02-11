import 'bootstrap'
import { error, type } from 'jquery'
import '../../node_modules/layui-src/src/css/layui.css'

window.onload = function() {
    $.ajax({
        url: url_head + "api/article/hot",
        type: "GET",
        dataType: "json",
        success: function(json) {
            $.each(json, function(i, item) {
                var articleInfo = document.querySelector("#articleInfo");
                articleInfo.content.querySelector("h3").innerHTML = item.title + "  (热度: " + item.click + "/click)";
                articleInfo.content.querySelector("p").innerHTML = item.summary;
                articleInfo.content.querySelector('a').href = "/article.html?articleId=" + item.id;
                document.querySelector("ul").appendChild(articleInfo.content.cloneNode(true));
            });
        },
        error: function(xhr, state, errorThrown) {
            console.log(errorThrown);
        }
    })
}