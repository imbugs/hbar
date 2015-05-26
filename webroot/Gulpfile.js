var gulp = require('gulp');
var jshint = require('gulp-jshint');
var stylish = require('jshint-stylish');
var sass = require('gulp-sass');
var cssimport = require("gulp-cssimport");
var browserify = require('browserify');
var babelify = require('babelify');
var source = require('vinyl-source-stream');
var del = require('del');

gulp.task('clean', function() {
	return del.sync(['./public/resources/*']);
});

gulp.task('browserify', function() {
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

gulp.task('proto', function() {
	return gulp.src('../protobuf/*.proto')
		.pipe(gulp.dest('./public/resources/protobuf'));
});

gulp.task('lint', function() {
    return gulp.src('./resources/js/**/*.js')
        .pipe(jshint({ browserify : true }))
        .pipe(jshint.reporter(stylish));
});

gulp.task('build', [
	'clean',
	'sass',
	'fonts',
	'proto',
	'browserify'
]);