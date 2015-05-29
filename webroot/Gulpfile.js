var gulp = require('gulp');
var jshint = require('gulp-jshint');
var stylish = require('jshint-stylish');
var sass = require('gulp-sass');
var cssimport = require("gulp-cssimport");
var browserify = require('browserify');
var babelify = require('babelify');
var source = require('vinyl-source-stream');
var del = require('del');
var fs = require('fs');
var util = require('gulp-util');


gulp.task('create-config', function(cb) {
  fs.writeFile('config.json', JSON.stringify({
    env: util.env.env || 'dev',
    hosts: {
    	vertx: {
    		dev: 'localhost:8888',
    		prod: 'charts.hbar.io:8888'
    	},
    	www: {
    		dev: 'localhost:8080',
    		prod: 'charts.hbar.io'
    	}
    }
  }), cb);
});

gulp.task('clean', function() {
	return del.sync(['./public/resources/*']);
});

gulp.task('browserify', ['create-config'], function() {
	return browserify('./resources/js/app.js')
		.transform(babelify, { stage : 0 })
		.bundle()
		.pipe(source('bundle.js'))
		.pipe(gulp.dest('./public/resources/js'));
});

gulp.task('sass', function() {
	return gulp.src('./resources/scss/*.scss')
		.pipe(sass())
		.pipe(cssimport({extensions: ["css"]}))
		.pipe(gulp.dest('./public/resources/css'));
});

gulp.task('fonts', function() {
	return gulp.src('./node_modules/bootstrap-sass/assets/fonts/bootstrap/*.{ttf,woff,eof,svg,eot}')
		.pipe(gulp.dest('./public/resources/fonts'));
});

gulp.task('images', function() {
	return gulp.src('./resources/img/**/*.{jpg,png}')
		.pipe(gulp.dest('./public/resources/img'));
});

gulp.task('proto', function() {
	return gulp.src('../protobuf/*.proto')
		.pipe(gulp.dest('./public/resources/protobuf'));
});

gulp.task('lint', function() {
    return gulp.src(['./resources/js/**/*.js', '!./resources/js/lib/**/*.js'])
        .pipe(jshint({ browserify : true }))
        .pipe(jshint.reporter(stylish));
});

gulp.task('build', [
	'lint',
	'clean',
	'sass',
	'fonts',
	'images',
	'proto',
	'browserify'
]);