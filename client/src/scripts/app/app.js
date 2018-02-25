'use strict';

const SERVER_API = 'http://podtoid.local:8888';

var app = angular.module( 'clientMain', [] )
    .config( function( $interpolateProvider ) {
        $interpolateProvider.startSymbol( '{[{' ).endSymbol( '}]}' );
    } )
    .controller( 'clientMainCtrl',
        async function( $scope, $http, $timeout ) {
            try {
                let res = await $http.get(`${SERVER_API}/v1/index`);
                $scope.clips = res.data;
                $scope.$apply();
            }
            catch( err ) {
                console.log("ERR", err);
            }

            $timeout( () => {
                $scope.audioElement = document.getElementById('myAudio');
                $scope.audioElement.type = "type/ogg";
                $scope.audioElement.addEventListener('ended', function() {
                    if ( $scope.currentlyPlaying ) {
                        $timeout( () =>  {
                            $scope.currentlyPlaying.isPlaying = false;
                        });
                    }
                }, false);
            });

            $scope.playClip = function( cat , file, clip_object) {
                $scope.audioElement.pause();

                if ( clip_object.isPlaying ) {
                    $timeout(()=>{
                        if ( $scope.currentlyPlaying ) {
                            $scope.currentlyPlaying.isPlaying = false;
                        }
                        $scope.currentlyPlaying.isPlaying = null;
                    });
                    return;
                }

                $scope.audioElement.src = `${SERVER_API}/v1/clip/${cat}/${file}`;
                $scope.audioElement.type = "type/ogg";
                $scope.audioElement.play();

                $timeout(()=>{
                    if ( $scope.currentlyPlaying ) {
                        $scope.currentlyPlaying.isPlaying = false;
                    }
                    $scope.currentlyPlaying = clip_object;
                    $scope.currentlyPlaying.isPlaying = true;
                });
            }
        } );