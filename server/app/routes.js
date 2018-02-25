'use strict';

module.exports = function( app ) {
    const VERSION = '/v1';

    app.route( `${VERSION}/index` )
        .get( require( './controllers/index' ) );

    app.route( `${VERSION}/clip/:category?/:id?` )
        .get( require( './controllers/clip' ) );

};