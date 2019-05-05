var gulp = require('gulp');
const fs   = require('fs');
var concat = require('gulp-concat');

gulp.task('default', (done) => {

    const folders = [
    	'./target/',
    	'./target/classes/',
    	'./target/classes/public/',
        './target/classes/public/script',
        './target/classes/public/style'
    ];

    folders.forEach(dir => {
        if(!fs.existsSync(dir)) {
            fs.mkdirSync(dir);
        }   
    });

    // regular JS
    gulp.src([
		'node_modules/jquery/dist/jquery.min.js',
		'node_modules/vue/dist/vue.js',
		'node_modules/vuetify/dist/vuetify.js',
	])
    .pipe(concat('deps.js'))
    .pipe(gulp.dest('./target/classes/public/script/'));
    
    // minified JS
    gulp.src([
    		'node_modules/jquery/dist/jquery.min.js',
    		'node_modules/vue/dist/vue.min.js',
    		'node_modules/vuetify/dist/vuetify.min.js',
		])
	    .pipe(concat('deps.min.js'))
	    .pipe(gulp.dest('./target/classes/public/script/'));

    // minified CSS
	gulp.src([
    		'node_modules/vuetify/dist/vuetify.min.css',
    		'node_modules/material-design-icons-iconfont/dist/material-design-icons.css',
		])
	    .pipe(concat('deps.css'))
	    .pipe(gulp.dest('./target/classes/public/style/'));

    done();
});