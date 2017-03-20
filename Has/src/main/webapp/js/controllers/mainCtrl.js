app.controller("mainCtrl", function ($scope, $http, $location, $timeout) {
    $scope.page = {
        title: "Home"
    };

    $scope.loginData = {};
    $scope.roomTypes = sampleRoomTypes;
    $scope.roomStatuses = sampleRoomStatuses;
    $scope.shifts = [
        "Morning",
        "Lunch",
        "Evening"
    ];
    $scope.documentElement = document.documentElement;
    $scope.credentials = {
        username: "",
        password: ""
    };
    $scope.authentication = "";
    $scope.isLoginError = false;

    $scope.searchFilters = {
        users: {
            username: "",
            email: "",
            roleID: 1
        },
        employees: {
            fullName: "",
            phone: "",
            dateHired: "",
            showDisabled: false
        },
        guests: {
            fullName: "",
            phone: ""
        },
        meals: {
            name: ''
        },
        rooms: {
            number: 0,
            roomClass: 0
        },
        schedules: {
            roleID: 1,
            startDate: new Date().toISOString().substr(0, 10),
            endDate: new Date().toISOString().substr(0, 10)
        }
    };

    $scope.displayMessage = function (response) {
        if (!response)
            return;

        $scope.page.message = {
            type: 'danger',
            title: response.error,
            text: response.message
        };

        if (response.status === 401) {
            $scope.authentication = "";
            $scope.loginData = {};

            if (!$scope.page.message.text)
                $scope.page.message.text = "You are not logged in!"

            $timeout(function () {
                $location.path('/login');
                $('#messageModal').modal('hide');
            }, 2000);
        } else if (response.status === 403) {
            if (!$scope.page.message.text)
                $scope.page.message.text = "You are not authorized to view this page!"

            $timeout(function () {
                $location.path('/home');
                $('#messageModal').modal('hide');
            }, 2000);
        }

        $('#messageModal').modal('show');
    };

    $scope.getPrincipal = function (callbackSuccess, callbackError) {
        var response = $http({
            method: "GET",
            url: "user/login",
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            }
        }).then(
            function (response) { //success
                return response.data;
            }, callbackError); //error
        response.then(callbackSuccess);
    }

    $scope.login = function () {
        $scope.authentication = "Basic " + btoa($scope.credentials.username + ":" + $scope.credentials.password);
        $timeout(function () {
            $scope.credentials = {};
        }, 1000);

        $scope.getPrincipal(function (data) {
            $scope.loginData = data.principal;
            //delete $scope.loginData.password;

            sessionStorage.setItem("authentication", $scope.authentication);
            sessionStorage.setItem("loginData", angular.toJson($scope.loginData));

            $scope.isLoginError = false;
            $location.path("/home");
        }, function (response) { //error
            if (response.status === 401) {
                $scope.isLoginError = true;
            } else {
                $scope.displayMessage(response.data);
            }
        })
    };

    $scope.logout = function () {
        var response = $http({
            method: "POST",
            url: "logout",
            //headers: {
            //    "Authorization": $scope.authentication
            //}
        }).then(
            function (response) { //success
                return response.data;
            }, function (response) { //error
                return response;
            }).then(
            function (data) {
                //alert("Logged out!");
                $scope.authentication = "";

                $scope.credentials = {
                    username: "",
                    password: ""
                };
                $scope.loginData = {};

                sessionStorage.removeItem("authentication");
                sessionStorage.removeItem("loginData");

                window.location.hash = "#!/login";
            },
            function (response) {
                $scope.displayMessage(response.data);
            });
    };

    $scope.getFreeUsers = function (id, type, updateCallback) {
        $http({
            method: "GET",
            url: ("users/free-" + type + "/" + id),
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            }
        }).then(
            function (response) { //success
                return response.data;
            },
            function (response) { //error
                $scope.displayMessage(response.data);
            }).then(updateCallback);
    };

    $scope.getAllRoles = function (updateCallback) {
        $http({
            method: "GET",
            url: "roles",
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            }
        }).then(
            function (response) { //success
                return response.data;
            },
            function (response) { //error
                $scope.displayMessage(response.data);
            }).then(updateCallback);
    };

    $scope.deleteData = function (dataType, id, callback) {
        $http({
            method: "DELETE",
            url: (dataType + "/" + id),
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            }
        }).then(
            function (response) { //success
                response.data;
            },
            function (response) { //error
                $scope.displayMessage(response.data);
            })
            .then(callback);
    };

    $scope.saveData = function (dataType, data, successCallback, errorCallback, isEdit) {
        var url = isEdit ? (dataType + "/" + data.id) : dataType;
        var method = isEdit ? "PUT" : "POST";

        $http({
            method: method,
            url: url,
            data: data,
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            }
        }).then(
            function (response) { //success
                return response;
            },
            function (response) { //error
                $scope.displayMessage(response.data);
                if (errorCallback)
                    errorCallback;
            })
            .then(function (response) {
                if (response.status == 200)
                    successCallback(response.data);
            });
    };

    $scope.getSingleData = function (dataType, id, successCallback, errorCallback) {
        var url = dataType + "/" + id;
        $http({
            method: "GET",
            url: url,
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            }
        }).then(
            function (response) { //success
                return response.data;
            },
            function (response) { //error
                $scope.displayMessage(response.data);
                if (errorCallback)
                    errorCallback;
            })
            .then(successCallback);
    };

    $scope.fullscreen = function () {
        if (!document.mozFullScreen && !document.webkitFullScreen) {
            if ($scope.documentElement.mozRequestFullScreen) {
                $scope.documentElement.mozRequestFullScreen();
            } else {
                $scope.documentElement.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
            }
        } else {
            if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            } else {
                document.webkitCancelFullScreen();
            }
        }
    };

    angular.element(document).ready(function () {
        if (!$scope.authentication) {
            if (sessionStorage.authentication) {
                $scope.authentication = sessionStorage.authentication;
                $scope.loginData = angular.fromJson(sessionStorage.loginData);
            } else {
                $location.path('/login');
            }
        }
    });
});