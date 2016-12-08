var webpack = require("webpack");
var HtmlWebpackPlugin = require("html-webpack-plugin");
var CopyWebpackPlugin = require("copy-webpack-plugin");
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var helpers = require("./helpers");

module.exports = {
    entry: {
        "polyfills": "./src/polyfills.ts",
        "vendor": "./src/vendor.ts",
        "app": "./src/main.ts"
    },

    // output defined in environment specific files

    resolve: {
        extensions: ["", ".js", ".ts"]
    },

    module: {
        loaders: [
            /**
             * A loader to transpile our Typescript code to ES5, guided by the tsconfig.json file. Excludes transpiling unit
             * and integration test files.
             */
            {
                test: /\.ts$/,
                loader: "ts",
                exclude: [/\.(spec|e2e)\.ts$/]
            },
            {
                // for component templates
                test: /\.html$/,
                loader: "html"
            },
            {
                // Images and fonts are bundled as well.
                test: /\.(png|jpe?g|gif|ico)$/,
                loader: "file?name=assets/[name].[hash].[ext]"
            },
            {
                test: /\.less$/,
                loader: "raw!less"
            },
            {
                test: /\.css$/,
                loader: ExtractTextPlugin.extract("style", "css?sourceMap")
            },
            {
                test: /\.woff(\?v=\d+\.\d+\.\d+)?$/,
                loader: "url?limit=10000&mimetype=application/font-woff"
            },
            {
                test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/,
                loader: "url?limit=10000&mimetype=application/font-woff"
            },
            {
                test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
                loader: "url?limit=10000&mimetype=application/octet-stream"
            },
            {
                test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
                loader: "file"
            },
            {
                test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
                loader: "url?limit=10000&mimetype=image/svg+xml"
            }
        ]
    },
    bail: true,
    progress: true,
    profile: true,

    plugins: [
        new webpack.optimize.CommonsChunkPlugin({
            name: ["app", "vendor", "polyfills"]
        }),
        // generating html
        new HtmlWebpackPlugin({
            template: "src/index.html"
        }),
        new CopyWebpackPlugin([
            {
                from: "src/favicon.ico",
                to: "favicon.ico"
            },
            {
                from: "src/assets",
                to: "assets"
            },
            {
                from: "src/data",
                to: "data"
            }
        ])
    ]
};
