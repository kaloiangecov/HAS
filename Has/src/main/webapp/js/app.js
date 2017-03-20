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
        .state('loggedin.root.users', {abstract: true, url: '/users'})
        .state('loggedin.root.users.usersList', stateUserList)
        .state('loggedin.root.users.usersAdd', stateUserAdd)
        .state('loggedin.root.users.usersEdit', stateUserEdit);

    $stateProvider // Employees module
        .state('loggedin.root.employees', {abstract: true, url: '/employees'})
        .state('loggedin.root.employees.employeesList', stateEmployeeList)
        .state('loggedin.root.employees.employeesAdd', stateEmployeeAdd)
        .state('loggedin.root.employees.employeesEdit', stateEmployeeEdit);

    $stateProvider // Working Schedule module
        .state('loggedin.root.schedule', {abstract: true, url: '/schedule'})
        .state('loggedin.root.schedule.scheduleList', stateScheduleList)
        .state('loggedin.root.schedule.scheduleAdd', stateScheduleAdd)
        .state('loggedin.root.schedule.scheduleEdit', stateScheduleEdit);

    $stateProvider // Guests module
        .state('loggedin.root.guests', {abstract: true, url: '/guests'})
        .state('loggedin.root.guests.guestsList', stateGuestList)
        .state('loggedin.root.guests.guestsAdd', stateGuestAdd)
        .state('loggedin.root.guests.guestsEdit', stateGuestEdit)
        .state('loggedin.root.guests.guestsHistory', stateGuestHistory);

    $stateProvider // Rooms module
        .state('loggedin.root.rooms', {abstract: true, url: '/rooms'})
        .state('loggedin.root.rooms.roomsList', stateRoomList)
        .state('loggedin.root.rooms.roomsAdd', stateRoomAdd)
        .state('loggedin.root.rooms.roomsEdit', stateRoomEdit);

    $stateProvider // Meals module
        .state('loggedin.root.meals', {abstract: true, url: '/meals'})
        .state('loggedin.root.meals.mealsList', stateMealList)
        .state('loggedin.root.meals.mealsAdd', stateMealAdd)
        .state('loggedin.root.meals.mealsEdit', stateMealEdit);

    $urlRouterProvider.otherwise('/login');
});