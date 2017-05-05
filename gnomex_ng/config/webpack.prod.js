/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
var commonConfig = require("./webpack.common.js");
const DefinePlugin = require("webpack/lib/DefinePlugin");
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var helpers = require("./helpers");
const LoaderOptionsPlugin = require("webpack/lib/LoaderOptionsPlugin");
const OptimizeJsPlugin = require("optimize-js-plugin");
const UglifyJsPlugin = require("webpack/lib/optimize/UglifyJsPlugin");
var webpack = require("webpack");
var webpackMerge = require("webpack-merge");

const ENV = process.env.NODE_ENV = process.env.ENV = "production";

/**
 * Extra configuration for production (in addition to webpack.common.js).  This primarily includes additional
 * minification and uglifying code.
 *
 * @param options
 * @returns {*}
 */
module.exports = function (options) {
    return webpackMerge(commonConfig({env: ENV}), {

        devtool: "source-map",

        output: {
            path: helpers.root("dist"),
            publicPath: "/gnomex/",
            filename: "[name].[chunkhash].bundle.js",
            sourceMapFilename: "[name].[chunkhash].bundle.map",
            chunkFilename: "[id].[chunkhash].chunk.js"
        },

        plugins: [
            new OptimizeJsPlugin({
                sourceMap: false
            }),

            new ExtractTextPlugin("[name].[contenthash].css"),

            new DefinePlugin({
                "ENV": JSON.stringify(ENV),
                "process.env": {
                    "ENV": JSON.stringify(ENV),
                    "NODE_ENV": JSON.stringify(ENV),
                }
            }),

          /*new UglifyJsPlugin({
           beautify: false,
           output: {
           comments: false
           },
           mangle: {
           screw_ie8: true
           },
           compress: {
           screw_ie8: true,
           warnings: false,
           conditionals: true,
           unused: true,
           comparisons: true,
           sequences: true,
           dead_code: true,
           evaluate: true,
           if_return: true,
           join_vars: true,
           negate_iife: false // we need this for lazy v8
           },
           }),*/

            new LoaderOptionsPlugin({
                minimize: true,
                debug: false,
                options: {

                    /**
                     * Html loader advanced options
                     *
                     * See: https://github.com/webpack/html-loader#advanced-options
                     */
                    htmlLoader: {
                        minimize: true,
                        removeAttributeQuotes: false,
                        caseSensitive: true,
                        customAttrSurround: [
                            [/#/, /(?:)/],
                            [/\*/, /(?:)/],
                            [/\[?\(?/, /(?:)/]
                        ],
                        customAttrAssign: [/\)?\]?=/]
                    },

                }
            }),
        ]
    });
}
