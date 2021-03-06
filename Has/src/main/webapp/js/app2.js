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
                angular.element(document).ready(function () {
                    FastClick.attach(document.body);
                    angular.element('.carousel').carousel({
                        interval: 5000
                    });
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
            onEnter: function ($rootScope) {
                setTimeout(function () {
                    angular.element('.navbar-nav').find('li').removeClass('active');
                    angular.element('#home').addClass('active');

                    $rootScope.dr1 = $('#dateRange').daterangepicker({
                        parentEl: "body",
                        startDate: new Date(),
                        endDate: new Date(),
                        minDate: new Date(),
                        locale: {
                            format: "DD/MM/YY",
                            firstDay: 1
                        }
                    });

                    angular.element('.calendar').css({float: 'left'});

                    $rootScope.switchPets = new Switchery(document.getElementById('pets'), {color: "#266CEa"});
                    $rootScope.switchMinibar = new Switchery(document.getElementById('minibar'), {color: "#266CEa"});
                }, 1);
            }
        })
        .state('app.root.personalData', {
            url: '/step2',
            views: {
                'content@app': {
                    templateUrl: 'templates/booking/personalData.html'
                    //controller: "mainCtrl2"
                }
            },
            onEnter: function ($rootScope) {
                setTimeout(function () {
                    $rootScope.bookCapcha = grecaptcha.render(
                        "bookCapcha",
                        {
                            sitekey: '6LedaBoUAAAAAFVLXtjPNI-6Jh5eMRHzqwGr9y1a',
                            theme: 'light'
                        }
                    );

                    $rootScope.switchBreakfast = new Switchery(document.getElementById('breakfast'), {color: "#266CEa"});
                    $rootScope.switchDinner = new Switchery(document.getElementById('dinner'), {color: "#266CEa"});
                    $rootScope.switchAllInclusive = new Switchery(document.getElementById('allInclusive'), {color: "#EA6C26"});
                }, 1);
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
                    templateUrl: 'templates/booking/edit.html'
                    //controller: 'changeReservationCtrl'
                }
            },
            onEnter: function () {
                angular.element(document).ready(function () {
                    angular.element('.navbar-nav').find('li').removeClass('active');
                    angular.element('#change').addClass('active');
                });
            }
        });

    $urlRouterProvider.otherwise('/home');
});