/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
var webpackMerge = require("webpack-merge");
var devConfig = require("./webpack.dev.js");

/**
 * A webpack configuration for a "live" development deployment.
 *
 * @author brandony <brandon.youkstetter@hci.utah.edu>
 * @since 7/18/16
 */
module.exports = webpackMerge(devConfig, {
  module: {
    preLoaders: [
      {
        test: /\.ts$/,
        loader: "tslint"
      }
    ]
  },

  devServer: {
    historyApiFallback: true,
    stats: "minimal"
  },

  tslint: {
    emitErrors: true,
    failOnHint: false
  }
});
