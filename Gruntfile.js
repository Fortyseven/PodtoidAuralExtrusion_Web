'use strict';

/*---------------------------------------------------------------------------
   ___________       __      _________ __          __  .__
   \_   _____/____ _/  |_   /   _____//  |______ _/  |_|__| ____
    |    __)_\__  \\   __\  \_____  \\   __\__  \\   __\  |/ ___\
    |        \/ __ \|  |    /        \|  |  / __ \|  | |  \  \___
   /_______  (____  /__|   /_______  /|__| (____  /__| |__|\___  >
           \/     \/               \/           \/             \/

    A static site building gruntscript.
    Toby Deshane <fortyseven@bytestemplar.com>
    MIT License
    2018

    -------------------------------------------------------------------------
    Defaults:
        Source files exist in `./src`
        Destination files go in `./dist`

    -------------------------------------------------------------------------
    Tasks:
             clean  Clean files and folders.
              less  Compile LESS files to CSS, with experimental features.
        twigRender  Render twig templates
              copy  Copy files.
               zip  Zip files together
             unzip  Unzip files into a folder
           default  clean -> less -> twigRender -> copy

    -------------------------------------------------------------------------
*/

const DEFAULT_CONFIG_PATH = "./config.local.yaml";

module.exports = function( grunt ) {
    require( 'time-grunt' )( grunt );
    grunt.initConfig( {} );

    const CONFIG_PATH = grunt.option( 'config' ) || DEFAULT_CONFIG_PATH;

    const pkg = grunt.file.readJSON( 'package.json' );

    const config      = grunt.file.readYAML( CONFIG_PATH );

    console.log( "------------------------------------------------------" );
    console.log( "Config: " + CONFIG_PATH );
    console.log( "Source path: " + config.path.source.root );
    console.log( "Destination path: " + config.path.dest.root );
    console.log( "------------------------------------------------------" );

    /******************************************************/
    grunt.loadNpmTasks( 'grunt-contrib-clean' );
    grunt.config( 'clean', {
        dist : {
            options : {
                force : true
            },
            files : [
                {
                    dot : true,
                    src : [
                        config.path.dest.root + '/{,*/}*',
                        '!' + config.path.dest.root + '/.git{,*/}*'
                    ]
                }
            ]
        }
    } );

    /******************************************************/
    grunt.loadNpmTasks( 'grunt-assemble-less' );
    var lesscfg = {
        options : {
            plugins : [
                ( new ( require( 'less-plugin-autoprefix' ) )( { browsers: [ "last 2 versions" ] } ) ),
                ( new ( require( 'less-plugin-clean-css' ) )( {
                    advanced      : true,
                    compatibility : 'ie8'
                } ) )
            ],
            compress   : true,
            globalVars : {
                'path_css'    : "\"" + config.path.dest.css + "\"",
                'path_images' : "\"" + config.path.dest.images + "\"",
                'path_fonts'  : "\"" + config.path.dest.fonts + "\"",
                'path_video'  : "\"" + config.path.dest.video + "\""
            }
        },
        build : {
            files : {}
        }
    };

    var path_src_css  = config.path.source.root + config.path.source.css;
    var path_dest_css = config.path.dest.root + config.path.dest.css;

    lesscfg[ 'build' ][ 'files' ][ path_dest_css + '/main.css' ] = path_src_css + '/main.less';

    grunt.config( 'less', lesscfg );

    /******************************************************/
    grunt.loadNpmTasks( 'grunt-contrib-watch' );

    var watch_opts = {
        root : {
            files : [ config.path.source.root + config.path.source.top_level + '/**/*' ],
            tasks : [ 'copy' ],
        },
        images : {
            files : [ config.path.source.root + config.path.source.images + '/**/*' ],
            tasks : [ 'copy:images' ]
        },
        fonts : {
            files : [ config.path.source.root + config.path.source.fonts + '/**/*' ],
            tasks : [ 'copy:fonts' ]
        },
        video : {
            files : [ config.path.source.root + config.path.source.video + '/**/*' ],
            tasks : [ 'copy:video' ]
        },
        js : {
            files : [
                config.path.source.root + config.path.source.scripts + '/**/*.js',
                '!' + config.path.source.root + config.path.source.scripts + '/assets/js/node_modules/**/*'
            ],
            tasks : [ 'copy:scripts' ]
        },
        styles : {
            files : [ config.path.source.root + config.path.source.css + '/**/*.less' ],
            tasks : [
                'less'
            ]
        },
        pages : {
            files : [
                config.path.source.root + '/**/*.twig',
                config.path.source.root + '/**/*.info',
                config.path.source.root + config.path.source.views + "/../data.json"
            ],
            tasks : [ 'twigRender:main' ]
        }
    };

    grunt.config( 'watch', watch_opts );

    /******************************************************/
    //    grunt.loadNpmTasks( 'grunt-string-replace' );
    //    grunt.config( 'string-replace', {
    //        version: {
    //            files:   {
    //                'tmp/theme.info':   [ config.path.source + '/theme.info' ],
    //                'tmp/template.php': [ config.path.source + '/template.php' ]
    //            },
    //            options: {
    //                replacements: [
    //                    {
    //                        pattern:     /{{ THEME_NAME }}/g,
    //                        replacement: pkg.theme.name
    //                    },
    //                    {
    //                        pattern:     /{{ THEME_DESCRIPTION }}/g,
    //                        replacement: pkg.theme.description
    //                    },
    //                    {
    //                        pattern:     /{{ VERSION }}/g,
    //                        replacement: pkg.version
    //                    }
    //                ]
    //            }
    //        }
    //    } );

    /******************************************************/
    grunt.loadNpmTasks( 'grunt-twig-render' );
    grunt.config( 'twigRender', {
        options : {},
        main    : {
            files : [
                {
                    data : [
                        CONFIG_PATH,
                        config.path.source.root + config.path.source.views + "/../data.yaml"
                    ],
                    // dataPath: "potato",
                    expand : true,
                    cwd    : config.path.source.root + config.path.source.views,
                    src    : [ "**/*.twig", "!**/_*.twig" ],
                    dest   : config.path.dest.root + config.path.dest.html,
                    ext    : ".html"
                }
            ]
        }
    } );

    /******************************************************/
    grunt.loadNpmTasks( 'grunt-contrib-copy' );
    grunt.config( 'copy', {
        images : {
            files : [
                {
                    expand  : true,
                    flatten : false,
                    dot     : true,
                    cwd     : config.path.source.root + config.path.source.images,
                    dest    : config.path.dest.root + config.path.dest.images,
                    src     : [ '**/*' ]
                }
            ]
        },
        fonts : {
            files : [
                {
                    expand  : true,
                    flatten : false,
                    dot     : true,
                    cwd     : config.path.source.root + config.path.source.fonts,
                    dest    : config.path.dest.root + config.path.dest.fonts,
                    src     : [ '**/*' ]
                }
            ]
        },
        video : {
            files : [
                {
                    expand  : true,
                    flatten : false,
                    dot     : true,
                    cwd     : config.path.source.root + config.path.source.video,
                    dest    : config.path.dest.root + config.path.dest.video,
                    src     : [ '**/*' ]
                }
            ]
        },
        scripts : {
            files : [
                {
                    expand  : true,
                    flatten : false,
                    dot     : true,
                    cwd     : config.path.source.root + config.path.source.scripts,
                    dest    : config.path.dest.root + config.path.dest.scripts,
                    src     : [
                        '**/*.js',
                        '**/*.css',
                        '**/*.gif',
                        '**/*.woff',
                        '**/*.ttf',
                        '!**/node_modules/**'
                    ]
                }
            ]
        },
        // Everything in the root path gets copied 1:1 to the theme root
        root : {
            files : [
                {
                    expand  : true,
                    flatten : false,
                    dot     : true,
                    cwd     : config.path.source.root + config.path.source.top_level,
                    dest    : config.path.dest.root + config.path.dest.top_level + '/',
                    src     : [ '**/*.*' ]
                }
            ]
        }
    } );

    /******************************************************/
    grunt.loadNpmTasks( 'grunt-zip' );
    grunt.config( 'zip', {
        chrome : {
            cwd  : config.path.dest + '/',
            src  : [ config.path.dest + '/**/*' ],
            dest : '_releases/' + pkg.name + '-' + pkg.version + '.zip'
        }
    } );

    /******************************************************/
    grunt.loadNpmTasks( 'grunt-http-server' );
    grunt.config( 'http-server', {
        default : {
            root            : "./dist",
            port            : 8000,
            host            : "192.168.1.17",
            showDir         : true,
            autoIndex       : true,
            ext             : "html",
            runInBackground : true,
        }
    } );

    /******** Register Tasks *************/
    // By default we'll do a complete clean and rebuild
    grunt.registerTask( 'default', [ 'clean', 'less', 'twigRender', 'copy' ] );
    grunt.registerTask( 'server', ['default', 'http-server', 'watch'] );
};