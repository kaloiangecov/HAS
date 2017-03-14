var app2 = angular.module("booking", ['ui.router', 'ui.bootstrap', 'ngResource', 'ngAnimate']);

app2.run(function () {

});

app2.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider // Main module
        .state("app", {
            url: "",
            abstract: true,
            templateUrl: "templates/booking/main.html"
        })
        .state("app.root", {
            views: {
                "navbar": {
                    templateUrl: 'templates/booking/navigation.html'
                },
                'content': {
                    template: 'DEFAULT VIEW'
                }
            },
            onEnter: function () {
                $('.carousel').carousel({
                    interval: 5000
                });
            }
        });

    $stateProvider // Home module
        .state('app.root.home', {
            url: '/home',
            views: {
                'content@app': {
                    templateUrl: 'templates/booking/home.html'
                    //controller: "mainCtrl2"
                }
            },
            onEnter: function () {
                $('.navbar-nav').find('li').removeClass('active');
                $('#home').addClass('active');
            }
        })
        .state('app.root.personalData', {
            url: '/step2',
            views: {
                'content@app': {
                    templateUrl: 'templates/booking/personalData.html'
                    //controller: "mainCtrl2"
                }
            }
        })
        .state('app.root.reservationSuccessful', {
            url: '/step3',
            views: {
                'content@app': {
                    templateUrl: 'templates/booking/reservationSuccessful.html'
                }
            }
        })
        .state('app.root.changeReservation', {
            url: '/change-reservation',
            views: {
                'content@app': {
                    templateUrl: 'templates/booking/edit.html',
                    //controller: 'changeReservationCtrl'
                }
            },
            onEnter: function () {
                $('.navbar-nav').find('li').removeClass('active');
                $('#change').addClass('active');
            }
        });

    $urlRouterProvider.otherwise('/home');
});