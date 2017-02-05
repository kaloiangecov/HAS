app.controller("mainCtrl", function ($scope, $http) {
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

    $scope.displayMessage = function (response) {
        if (!response)
            return;

        alert(response.error + '\n' + response.message);

        if (response.status === 401) {
            $scope.authentication = "";
            $scope.loginData = {};
            window.location.hash = "#!/login";
        } else if (response.status === 403) {
            window.location.hash = "#!/home";
        }
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
        $scope.getPrincipal(function (data) {
            $scope.loginData = data.principal;
            //delete $scope.loginData.password;

            window.location.hash = "#!/home";
        }, function (response) { //error
            $scope.displayMessage(response.data);
        })
    };

    $scope.logout = function () {
        var response = $http({
            method: "POST",
            url: "logout",
            headers: {
                "Authorization": $scope.authentication
            }
        }).then(
            function (response) { //success
                return response.data;
            }, function (response) { //error
                $scope.displayMessage(response.data);
            }).then(
            function (data) {
                alert("Logged out!");
                window.location.hash = "#!/login";
            });
    };

    $scope.getUser = function (userID, updateCallback) {
        var response = $http({
            method: "GET",
            url: ("user/" + userID),
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

    $scope.getAllUsers = function (updateCallback) {
        $http({
            method: "GET",
            url: "users",
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

    $scope.getRole = function (roleID, updateCallback) {
        var response = $http({
            method: "GET",
            url: ("role/" + roleID),
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

    $scope.exportForm = function (data) {
        var text = JSON.stringify(data);

        var a = window.document.createElement('a');
        a.href = window.URL.createObjectURL(new Blob([text], {type: 'text/csv'}));
        a.download = ('form.txt');

        // Append anchor to body.
        document.body.appendChild(a)
        a.click();

        // Remove anchor from body
        document.body.removeChild(a)
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

    });
});