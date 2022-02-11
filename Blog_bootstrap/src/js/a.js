import "jquery";
import 'bootstrap'; //  引入 Bootstrap
import "bootstrap/dist/css/bootstrap.min.css";

// import 'bootstrap/dist/css/bootstrap.min.css'; //  引入 Bootstrap 的 css


import b from "./b";
console.log(b);

//css文件的覆盖
import '../css/a.less';

$('#modal').modal();



export default '我是A页面'