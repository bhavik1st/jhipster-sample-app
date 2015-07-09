'use strict';

angular.module('jhipsterApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $location, $timeout, Auth) {
        $scope.user = {};
        $scope.errors = {};
        
        if ($location.search().error) {
        	$scope.errorAccessDenied = $location.search().error == 'access_denied';
        }

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                if ($rootScope.previousStateName === 'register') {
                    $state.go('home');
                } else {
                    $rootScope.back();
                }
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
