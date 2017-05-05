var webpack = require("webpack");
var helpers = require("./helpers");
var path = require("path");

const autoprefixer = require("autoprefixer");
var CopyWebpackPlugin = require("copy-webpack-plugin");
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var HtmlWebpackPlugin = require("html-webpack-plugin");
const LoaderOptionsPlugin = require("webpack/lib/LoaderOptionsPlugin");
const ProvidePlugin = require("webpack/lib/ProvidePlugin");

module.exports = function (options) {

    return {
        entry: {
            polyfills: "./src/polyfills.ts",
            twbs: "bootstrap-loader",
            vendor: "./src/vendor.ts",
            app: "./src/main.ts"
        },

        resolve: {
            extensions: [".ts", ".js"],
            modules: [helpers.root("src"), helpers.root("node_modules")]
        },

        module: {
            rules: [
                {
                    test: /\.ts$/,
                    use: [
                        {
                            loader: "@angularclass/hmr-loader"
                        },
                        {
                            loader: "awesome-typescript-loader",
                            options: {
                                configFileName: "tsconfig.json"
                            }
                        },
                        {
                            loader: "angular2-template-loader"
                        }
                    ],
                    exclude: [/\.(spec|e2e)\.ts$/, /node_modules/]
                },
                {
                    // Images and fonts are bundled as well.
                    test: /\.(png|jpe?g|gif|ico)$/,
                    loader: "file-loader?name=assets/[name].[hash].[ext]"
                },
                {
                    test: /\.css$/,
                    loader: ExtractTextPlugin.extract({ fallbackLoader: "style-loader", loader: "css-loader?sourceMap" }),
                    exclude: [helpers.root("src", "assets")]
                },
                {
                    test: /\.less$/,
                    loader: ExtractTextPlugin.extract({loader:[ "css-loader", "less-loader" ], fallbackLoader: "style-loader"})
                },
                {
                    test: /\.scss$/,
                    use: ["raw-loader", "sass-loader"]
                },
                {
                    test: /bootstrap\/dist\/js\/umd\//,
                    use: "imports-loader?jQuery=jquery"
                },
                {
                    test: /\.html$/,
                    loader: "html-loader"
                },
                {
                    test: /\.woff(\?v=\d+\.\d+\.\d+)?$/,
                    loader: "url-loader?limit=10000&mimetype=application/font-woff"
                },
                {
                    test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/,
                    loader: "url-loader?limit=10000&mimetype=application/font-woff"
                },
                {
                    test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
                    loader: "url-loader?limit=10000&mimetype=application/octet-stream"
                },
                {
                    test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
                    loader: "file-loader"
                },
                {
                    test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
                    loader: "url-loader?limit=10000&mimetype=image/svg+xml"
                }
            ]
        },

        plugins: [
            new webpack.optimize.CommonsChunkPlugin({
                name: ["app", "vendor", "twbs", "polyfills"]
            }),

            new HtmlWebpackPlugin({
                template: "src/index.html",
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
            ]),

            new webpack.ProvidePlugin({
                $: "jquery",
                jQuery: "jquery",
                "window.jQuery": "jquery",
                Tether: "tether",
                "window.Tether": "tether",
                Tooltip: "exports-loader?Tooltip!bootstrap/js/dist/tooltip",
                Alert: "exports-loader?Alert!bootstrap/js/dist/alert",
                Button: "exports-loader?Button!bootstrap/js/dist/button",
                Collapse: "exports-loader?Collapse!bootstrap/js/dist/collapse",
                Dropdown: "exports-loader?Dropdown!bootstrap/js/dist/dropdown",
                Modal: "exports-loader?Modal!bootstrap/js/dist/modal",
                Tab: "exports-loader?Tab!bootstrap/js/dist/tab",
                Util: "exports-loader?Util!bootstrap/js/dist/util"
            }),
        ]
    };
}