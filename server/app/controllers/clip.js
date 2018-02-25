'use strict';

const fs = require( 'fs' );

module.exports = function clip( req, res ) {
    const id = req.params.id;
    const category = req.params.category;

    console.log( 'clip', category, id );

    if ( !server.clip_index.category.hasOwnProperty( category ) ) {
        return res.send( 404, "Bad category" );
    }

    const path = server.clip_index.category[category].path;
    const filepath = `.${ path }/${ id }.ogg`;

    if ( fs.existsSync( filepath ) ) {
        fs.createReadStream( filepath ).pipe( res );
    }
    else {
        res.send( 404, "Clip not found" );
    }
};