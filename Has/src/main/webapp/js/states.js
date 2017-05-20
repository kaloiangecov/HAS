var stateLogin = {
    url: "/login",
    views: {
        "login": {
            templateUrl: "templates/login.html"
            //controller: "mainCtrl"
        }
    },
    onEnter: function () {
        angular.element(document).ready(function () {
            angular.element('body').removeClass('nav-md');
            angular.element('body').addClass('login');
            angular.element('#logged_content').hide();
            angular.element('.login_wrapper').show();
        });
    },
    onExit: function () {
        angular.element(document).ready(function () {
            angular.element('body').removeClass('login');
            angular.element('body').addClass('nav-md');
            angular.element('.login_wrapper').hide();
            angular.element('#logged_content').show();
        });
    }
};

var stateMain = {
    url: "",
    abstract: true,
    views: {
        "loggedin": {
            templateUrl: "templates/main.html"
            //controller: "mainCtrl"
        }
    },
    onEnter: function () {
        angular.element(document).ready(function () {
            FastClick.attach(document.body);
        });
    }
};

var stateRoot = {
    views: {
        "navigation": {
            templateUrl: 'templates/navigation.html'
        },
        "menu": {
            templateUrl: 'templates/menu.html'
        },
        'content': {
            template: 'DEFAULT VIEW'
        }
    },
    onEnter: function () {
        angular.element(document).ready(function () {
            CURRENT_URL = window.location.href.split('?')[0];
            $BODY = $('body');
            $MENU_TOGGLE = $('#menu_toggle');
            $SIDEBAR_MENU = $('#sidebar-menu');
            $SIDEBAR_FOOTER = $('.sidebar-footer');
            $LEFT_COL = $('.left_col');
            $RIGHT_COL = $('.right_col');
            $NAV_MENU = $('.nav_menu');
            $FOOTER = $('footer');

            initializeSidebar();
        });
    }
};

var stateHome = {
    url: '/home',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/home.html'
            //controller: "mainCtrl"
        }
    }
};

var stateCalendar = {
    url: '/calendar',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/reception/calendar.html'
        }
    }
};

var stateDashboard = {
    url: '/dashboard',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/tasks/dashboard.html',
            controller: 'dashboardCtrl'
        }
    }
};

var stateAddTask = {
    url: '/add',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/tasks/add.html',
            controller: 'dashboardCtrl'
        }
    }
};

var stateEditTask = {
    url: '/edit/:id',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/tasks/add.html',
            controller: 'dashboardCtrl'
        }
    }
};


//---------------------USERS----------------------//

var stateProfileView = {
    url: '/profile',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/profile.html',
            controller: "userCtrl"
        }
    }
};

var stateUserList = {
    url: '/list',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/users/list.html',
            controller: "userCtrl"
        }
    }
};

var stateUserAdd = {
    url: '/add',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/users/add.html',
            controller: "userCtrl"
        }
    }
};

var stateUserEdit = {
    url: '/edit/:id',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/users/add.html',
            controller: "userCtrl"
        }
    }
};

//------------------------------------------------//


//-------------------EMPLOYEES--------------------//
var stateEmployeeList = {
    url: '/list',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/employees/list.html',
            controller: 'employeeCtrl'
        }
    }
};

var stateEmployeeAdd = {
    url: '/add',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/employees/add.html',
            controller: 'employeeCtrl'
        }
    }
};

var stateEmployeeEdit = {
    url: '/edit/:id',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/employees/add.html',
            controller: 'employeeCtrl'
        }
    }
};

//------------------------------------------------//


//--------------------SCHEDULE--------------------//

var stateScheduleList = {
    url: '/list',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/schedule/list.html'
        }
    }
};

var stateScheduleAdd = {
    url: '/add',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/schedule/add.html'
        }
    }
};

var stateScheduleEdit = {
    url: '/edit/:id',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/schedule/add.html'
        }
    }
};

//------------------------------------------------//


//---------------------GUESTS---------------------//
var stateGuestList = {
    url: '/list',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/guests/list.html',
            controller: 'guestCtrl'
        }
    }
};

var stateGuestAdd = {
    url: '/add',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/guests/add.html',
            controller: 'guestCtrl'
        }
    }
};

var stateGuestEdit = {
    url: '/edit/:id',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/guests/add.html',
            controller: 'guestCtrl'
        }
    }
};

var stateGuestHistory = {
    url: '/history/:id',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/guests/history.html',
            controller: 'guestCtrl'
        }
    }
};

//------------------------------------------------//


//---------------------ROOMS----------------------//

var stateRoomList = {
    url: '/list',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/rooms/list.html',
            controller: 'roomCtrl'
        }
    }
};

var stateRoomAdd = {
    url: '/add',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/rooms/add.html',
            controller: "roomCtrl"
        }
    }
};

var stateRoomEdit = {
    url: '/edit/:id',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/rooms/add.html',
            controller: "roomCtrl"
        }
    }
};

//------------------------------------------------//


//---------------------MEALS----------------------//

var stateMealList = {
    url: '/list',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/meals/list.html',
            controller: "mealCtrl"
        }
    }
};

var stateMealAdd = {
    url: '/add',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/meals/add.html',
            controller: "mealCtrl"
        }
    }
};

var stateMealEdit = {
    url: '/edit/:id',
    views: {
        'content@loggedin': {
            templateUrl: 'templates/meals/add.html',
            controller: "mealCtrl"
        }
    }
};

//------------------------------------------------//