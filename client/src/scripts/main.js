(function(){function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s}return e})()({1:[function(require,module,exports){
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
},{}]},{},[1]);
