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
            onEnter: function ($rootScope) {
                setTimeout(function () {
                    $('.navbar-nav').find('li').removeClass('active');
                    $('#home').addClass('active');

                    $rootScope.dr1 = $('#dateRange').daterangepicker({
                        parentEl: "body",
                        startDate: new Date(),
                        endDate: new Date(),
                        locale: {
                            format: "DD/MM/YY"
                        }
                    });

                    $('.calendar').css({float: 'left'});

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
                    $('#identityIssueDate,#identityExpireDate').daterangepicker({
                        parentEl: "body",
                        singleDatePicker: true,
                        showDropdowns: true,
                        locale: {
                            format: "YYYY-MM-DD"
                        }
                    });

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
                    templateUrl: 'templates/booking/edit.html',
                    //controller: 'changeReservationCtrl'
                }
            },
            onEnter: function ($rootScope) {
                setTimeout(function () {
                    $('.navbar-nav').find('li').removeClass('active');
                    $('#change').addClass('active');
                }, 1);
            }
        });

    $urlRouterProvider.otherwise('/home');
});