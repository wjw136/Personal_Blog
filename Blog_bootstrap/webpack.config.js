const webpack = require("webpack");
const path = require("path");
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require("clean-webpack-plugin")
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
// const CommonsChunkPlugin = webpack.optimize.CommonsChunkPlugin;
const copyWebpackPlugin = require('copy-webpack-plugin')

module.exports = {
    mode: "development",
    entry: {
        "a": path.resolve(__dirname, 'src/js/a.js'),
        "b": path.resolve(__dirname, 'src/js/b.js'),
        "index": path.resolve(__dirname, "src/js/index.js"),
        "article": path.resolve(__dirname, "src/js/article.js"),
        "about": path.resolve(__dirname, "src/js/about.js"),
        'blog': path.resolve(__dirname, "src/js/blog.js"),
        'hot': path.resolve(__dirname, 'src/js/hot.js')
    },
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'js/[name].js'
    },
    module: {
        rules: [{
                test: /\.css$/,
                use: [MiniCssExtractPlugin.loader, 'css-loader'],
            },
            {
                test: /\.less$/,
                use: [
                    MiniCssExtractPlugin.loader, {
                        loader: "css-loader",
                    }, {
                        loader: "less-loader"
                    }
                ]
            },
            // {
            //     // 问题：默认处理不了html中img图片
            //     // 处理图片资源
            //     test: /\.(jpg|png|gif)$/,
            //     // 使用一个loader
            //     // 下载 url-loader file-loader
            //     loader: 'url-loader',
            //     options: {
            //         // 图片大小小于8kb，就会被base64处理
            //         // 优点: 减少请求数量（减轻服务器压力）
            //         // 缺点：图片体积会更大（文件请求速度更慢）
            //         limit: 8 * 1024,
            //         // 问题：因为url-loader默认使用es6模块化解析，而html-loader引入图片是commonjs
            //         // 解析时会出问题：[object Module]
            //         // 解决：关闭url-loader的es6模块化，使用commonjs解析
            //         esModule: false,
            //         // 给图片进行重命名
            //         // [hash:10]取图片的hash的前10位
            //         // [ext]取文件原来扩展名
            //         name: '[hash:10].[ext]'
            //     }
            // },
            // {
            //     test: /\.(png|svg|jpg|gif)$/,
            //     use: {
            //         loader: 'url-loader'
            //     }
            // }

        ]
    },
    plugins: [
        // new CommonsChunkPlugin({ //打包共用库
        //     names: ['vendor'],
        //     minChunks: Infinity
        // }),

        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery',
            'window.jQuery': 'jquery',
        }),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, 'src/html/a.html'),
            filename: 'a.html',
            chunks: ['a', 'vendor'] //所以这里我们得引入两个文件vendor和jQuery
        }),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, 'src/html/b.html'),
            filename: 'b.html',
            // chunks: ['vendor', 'jquery', 'b']
        }),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, 'src/html/index.html'),
            filename: 'index.html',
            chunks: ['index']
        }),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, 'src/html/article.html'),
            filename: 'article.html',
            chunks: ['article']
        }),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, 'src/html/about.html'),
            filename: 'about.html',
            chunks: ['about']
        }),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, 'src/html/blog.html'),
            filename: 'blog.html',
            chunks: ['blog']
        }),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, 'src/html/hot.html'),
            filename: 'hot.html',
            chunks: ['hot']
        }),
        new CleanWebpackPlugin(),
        // new BundleAnalyzerPlugin(),
        new MiniCssExtractPlugin({
            // 类似 webpackOptions.output里面的配置 可以忽略
            filename: 'css/[name].css',
            chunkFilename: 'css/[id].css',
        }),
        new copyWebpackPlugin({
            patterns: [{
                from: __dirname + '/static/', //打包的静态资源目录地址
                to: './static/' //打包到dist下面的public
            }]
        }),
    ],
    devServer: {
        static: { // static: ['assets']
            directory: path.resolve(__dirname, 'static/')
        },
        // contentBase: path.join(__dirname, "dist"), //告诉服务器从哪里提供内容。只有在你想要提供静态文件时才需要
        port: "3000", //端口号
        open: true, //自动用浏览器打开网页，默认是true
        hot: true, //热更新
        // open: true, //指定打开的页面
        allowedHosts: [ //配置一个白名单列表，只有HTTP请求的HOST在列表里才正常返回
            "localhost:80"
        ]
    },
    optimization: {
        splitChunks: {
            //chunks: 表示显示块的范围，有三个可选值：initial(初始块)、async(按需加载块)、all(全部块)，默认为all;
            chunks: 'async',
            minSize: 30000, // 表示在压缩前的最小模块大小，默认是30kb；
            minRemainingSize: 0, // 
            maxSize: 0,
            minChunks: 1, // 表示被引用次数，默认为1；
            maxAsyncRequests: 6, //所有异步请求不得超过6个
            maxInitialRequests: 4, //初始话并行请求不得超过4个
            automaticNameDelimiter: '~', //名称分隔符，默认是~
            // automaticNameMaxLength: 30,
            // cacheGroups: 缓存组
            cacheGroups: {
                common: {
                    name: 'common', //抽取的chunk的名字
                    chunks(chunk) { //同外层的参数配置，覆盖外层的chunks，以chunk为维度进行抽取
                    },
                    test(module, chunks) { //可以为字符串，正则表达式，函数，以module为维度进行抽取，只要是满足条件的module都会被抽取到该common的chunk中，为函数时第一个参数是遍历到的每一个模块，第二个参数是每一个引用到该模块的chunks数组。自己尝试过程中发现不能提取出css，待进一步验证。
                    },
                    priority: 10, //优先级，一个chunk很可能满足多个缓存组，会被抽取到优先级高的缓存组中
                    minChunks: 2, //最少被几个chunk引用
                    reuseExistingChunk: true, //  如果该chunk中引用了已经被抽取的chunk，直接引用该chunk，不会重复打包代码
                    enforce: true // 如果cacheGroup中没有设置minSize，则据此判断是否使用上层的minSize，true：则使用0，false：使用上层minSize
                },
                vendors: {
                    test: /[\\/]node_modules[\\/]/,
                    name: 'vendor',
                    chunks: 'all',
                    maxSize: Infinity,
                },
            }
        }
    }

}