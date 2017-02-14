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
            }
        })

    $urlRouterProvider.otherwise('/home');
});