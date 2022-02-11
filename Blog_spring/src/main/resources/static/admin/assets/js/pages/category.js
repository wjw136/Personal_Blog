url_head="http://121.199.41.175:9090/"
$(document).ready(function() {
	// 填充浏览统计数据
	$.ajax({
		url: url_head+"api/category/list",
		type: "GET",
		dataType: "json",
		success: function(json) {
			$.each(json, function(i, item) {
				$('#tbody-categoris').append(
					'<tr><td>' + +item.id +
					'</td><td>' + item.name +
					'</td><td>' + item.number +
					'</td><td>' + item.createBy +
					'</td><td>' + item.modifiedBy +
					'</td><td><button class="btn btn-danger deleteBtn" onclick="deleteCategory(\'' + item.id + '\')"><i class="fa fa-trash-o"></i>删除</button></td></tr>');
				$('#select-category').append(
					'<option categoryId="' + item.id + '">' + item.name + '</option>'
				);
			});
			$('#dataTables-categoris').dataTable(); //定制化表格
		}
	});
});

// 删除按钮点击事件
function deleteCategory(id) {
	$('#confirmBtn').attr("categoryId", id);
	$('#myModal').modal();
};

// 确认删除按钮点击事件
$('#confirmBtn').click(function() {
	var id = $(this).attr("categoryId");
	$.ajax({
		type: "DELETE",
		url: url_head+"admin/category/" + id,
		success: function() {
			// 刷新页面
			location.reload();
		}
	});
});

// 增加分类按钮点击事件
$('#addCategoryBtn').click(function() {
	var categoryName = $('#addName').val();
	var json = {
		name: categoryName
	};
	$.ajax({
		type: "POST",
		dataType: "json",
		contentType: "application/json;charset=utf-8",
		url: url_head+"admin/category",
		data: JSON.stringify(json),
		success: function() {
			// 刷新页面
			location.reload();
		},
		error: function() {
			location.reload();
		}
	});
});

// 更新分类点击事件
$('#updateCategoryBtn').click(function() {
	var categoryId = $('#select-category option:selected').attr("categoryId");
	var categoryName = $('#updateName').val();
	var categoryJson = {
		id: categoryId,
		name: categoryName
	};
	$.ajax({
		type: "PUT",
		url: url_head+"admin/category/" + categoryId,
		data: JSON.stringify(categoryJson),
		dataType: "json",
		contentType: "application/json;charset=utf-8",
		success: function() {
			// 刷新页面
			location.reload();
		},
		error: function() {
			$('#updateName').val("");
			location.reload();
		}
	})
})