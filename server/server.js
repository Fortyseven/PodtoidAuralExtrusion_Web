'use strict';

/*
    88""Yb  dP"Yb  8888b.  888888  dP"Yb  88 8888b.
    88__dP dP   Yb  8I  Yb   88   dP   Yb 88  8I  Yb
    88"""  Yb   dP  8I  dY   88   Yb   dP 88  8I  dY
    88      YbodP  8888Y"    88    YbodP  88 8888Y"

       db    88   88 88""Yb    db    88
      dPYb   88   88 88__dP   dPYb   88
     dP__Yb  Y8   8P 88"Yb   dP__Yb  88  .o
    dP""""Yb `YbodP' 88  Yb dP""""Yb 88ood8

    888888 Yb  dP 888888 88""Yb 88   88 .dP"Y8 88  dP"Yb  88b 88
    88__    YbdP    88   88__dP 88   88 `Ybo." 88 dP   Yb 88Yb88
    88""    dPYb    88   88"Yb  Y8   8P o.`Y8b 88 Yb   dP 88 Y88
    888888 dP  Yb   88   88  Yb `YbodP' 8bodP' 88  YbodP  88  Y8

    Server
    MIT License, 2018
    Toby Deshane <fortyseven@bytestemplar.com>
*/

const PORT = 8888;
const THIRTYDAYS = 2592000000;

const routes = require( './app/routes.js' );

const express = require( 'express' );
const cors = require( 'cors' );
const app = express();


function loadClipIndex() {
    const yaml = require( 'js-yaml' );
    const fs = require( 'fs' );

    try {
        return yaml.safeLoad( fs.readFileSync( './app/clips.yaml', 'utf8' ) );
    }
    catch ( e ) {
        console.error( e );
    }
}

/***********************************************************/
/***********************************************************/

global.server = {
    clip_index : loadClipIndex()
};

app.use( cors( { origin: "*" } ) );

app.use( function( req, res, next ) {
    res.setHeader( "Cache-Control", "public, max-age=" + THIRTYDAYS );
    res.setHeader( "Expires", new Date( Date.now() + THIRTYDAYS ).toUTCString() );
    next();
} );

routes( app );

app.listen( PORT );

console.log( "### Server started" );