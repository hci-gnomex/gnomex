/*
 * Copyright (c) 2016 Huntsman Cancer Institute at the University of Utah, Confidential and Proprietary
 */
var gulp = require("gulp");
var del = require("del");
var tslint = require("gulp-tslint");
var webpack = require("webpack-stream");
var parseArgs = require("minimist");
var Server = require('karma').Server;

/**
 * Cleans the current build
 */
gulp.task("clean", function () {
  return del.sync([
    'dist/**',
    'code-coverage/**'
  ]);
});

/**
 * Run the linter for Typescript static analysis.
 */
gulp.task("tslint", function () {
  return gulp.src("./src/**/*.ts").pipe(tslint({
    formatter: "verbose"
  })).pipe(tslint.report());
});

/**
 * Run unit tests through Karma test runner.
 */
gulp.task("test", function () {
  new Server({
    configFile: __dirname + '/webpack/karma.conf.js'
  }).start();
});

/**
 * Bundle the application using webpack. Webpack takes care of all Typescript transpiling and asset compilation
 * (e.g. less or sass) which is defined by the webpack configuration.
 */
gulp.task("bundle", ["clean"], function () {
  var options = parseArgs(process.argv.slice(2), {
    boolean: "prod"
  });
  var config = options.prod ? require("./webpack/webpack.prod.js") : require("./webpack/webpack.dev.js");

  return webpack(config).pipe(gulp.dest("dist/"));
});

/**
 * Copy all static resources
 */
gulp.task("copy-statics", function() {
  gulp.src('./src/assets/**').pipe(gulp.dest('dist/assets'));
  gulp.src('./src/data/**').pipe(gulp.dest('dist/data'));
  gulp.src('./src/favicon.ico').pipe(gulp.dest('dist'));
})

gulp.task("build", [
  "tslint",
  "test",
  "bundle",
  "copy-statics"
], function () {
  // TODO: BHY (07/14/16) - Determine any post processing or cleanup that may be necessary.
});


