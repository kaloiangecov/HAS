var app = angular.module("mse", ['ui.router', 'daypilot', 'datatables', 'ngResource']);

app.run(function () {

});

app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider // Login module
        .state("login", stateLogin);

    $stateProvider // Main module
        .state("loggedin", stateMain)
        .state("loggedin.root", stateRoot);

    $stateProvider // Home module
        .state('loggedin.root.home', stateHome)
        .state('loggedin.root.profile', stateProfileView);

    $stateProvider // Calendar module
        .state('loggedin.root.calendar', stateCalendar);

    $stateProvider // Users module
        .state('loggedin.root.users', {abstract: true})
        .state('loggedin.root.users.list', stateUserList)
        .state('loggedin.root.users.add', stateUserAdd)
        .state('loggedin.root.users.edit', stateUserEdit);

    $stateProvider // Employees module
        .state('loggedin.root.employees', {abstract: true})
        .state('loggedin.root.employees.list', stateEmployeeList)
        .state('loggedin.root.employees.add', stateEmployeeAdd)
        .state('loggedin.root.employees.edit', stateEmployeeEdit);

    $stateProvider // Working Schedule module
        .state('loggedin.root.schedule', {abstract: true})
        .state('loggedin.root.schedule.list', stateScheduleList)
        .state('loggedin.root.schedule.add', stateScheduleAdd)
        .state('loggedin.root.schedule.edit', stateScheduleEdit);

    $stateProvider // Guests module
        .state('loggedin.root.guests', {abstract: true})
        .state('loggedin.root.guests.list', stateGuestList)
        .state('loggedin.root.guests.add', stateGuestAdd)
        .state('loggedin.root.guests.edit', stateGuestEdit);

    $stateProvider // Rooms module
        .state('loggedin.root.rooms', {abstract: true})
        .state('loggedin.root.rooms.list', stateRoomList)
        .state('loggedin.root.rooms.add', stateRoomAdd)
        .state('loggedin.root.rooms.edit', stateRoomEdit);

    $urlRouterProvider.otherwise('/login');
});