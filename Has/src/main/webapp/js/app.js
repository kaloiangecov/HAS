var app = angular.module("mse", ['ui.router', 'ui.select', 'daypilot', 'datatables', 'ngResource', 'ngSanitize']);

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

    $stateProvider // Dashboard module
        .state('loggedin.root.dashboard', stateDashboard);

    $stateProvider // Users module
    //.state('loggedin.root.users', {abstract: true})
        .state('loggedin.root.usersList', stateUserList)
        .state('loggedin.root.usersAdd', stateUserAdd)
        .state('loggedin.root.usersEdit', stateUserEdit);

    $stateProvider // Employees module
    //.state('loggedin.root.employees', {abstract: true})
        .state('loggedin.root.employeesList', stateEmployeeList)
        .state('loggedin.root.employeesAdd', stateEmployeeAdd)
        .state('loggedin.root.employeesEdit', stateEmployeeEdit);

    $stateProvider // Working Schedule module
    //.state('loggedin.root.schedule', {abstract: true})
        .state('loggedin.root.scheduleList', stateScheduleList)
        .state('loggedin.root.scheduleAdd', stateScheduleAdd)
        .state('loggedin.root.scheduleEdit', stateScheduleEdit);

    $stateProvider // Guests module
    //.state('loggedin.root.guests', {abstract: true})
        .state('loggedin.root.guestsList', stateGuestList)
        .state('loggedin.root.guestsAdd', stateGuestAdd)
        .state('loggedin.root.guestsEdit', stateGuestEdit);

    $stateProvider // Rooms module
    //.state('loggedin.root.rooms', {abstract: true})
        .state('loggedin.root.roomsList', stateRoomList)
        .state('loggedin.root.roomsAdd', stateRoomAdd)
        .state('loggedin.root.roomsEdit', stateRoomEdit);

    $stateProvider // Meals module
    //.state('loggedin.root.meals', {abstract: true})
        .state('loggedin.root.mealsList', stateMealList)
        .state('loggedin.root.mealsAdd', stateMealAdd)
        .state('loggedin.root.mealsEdit', stateMealEdit);

    $urlRouterProvider.otherwise('/login');
});