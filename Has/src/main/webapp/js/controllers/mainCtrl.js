app.controller("mainCtrl", function ($scope, $http) {
    $scope.page = {
        title: "Home"
    };

    $scope.loginData = {};
    $scope.roles = sampleRoles;
    $scope.roomTypes = sampleRoomTypes;
    $scope.roomStatuses = sampleRoomStatuses;
    $scope.countries = [];
    $scope.documentElement = document.documentElement;

    $http({
        method: "GET",
        url: "js/countries.json"
    })
        .then(
            function (response) { //success
                $scope.countries = response.data.countries;
            },
            function (response) { //error
                alert(response.statusText);
            });

    $scope.getUser = function (userID, updateCallback) {
        var response = $http({
            method: "GET",
            url: ("user/" + userID),
            responseType: "json"
        }).then(
            function (response) { //success
                return response.data;
            },
            function (response) { //error
                alert(response.data.message);
            }).then(updateCallback);
    };

    $scope.getAllUsers = function (updateCallback) {
        $http({
            method: "GET",
            url: "users",
            responseType: "json"
        }).then(
            function (response) { //success
                return response.data;
            },
            function (response) { //error
                alert(response.data.message);
                return undefined;
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
        $scope.getUser(1, function (data) {
            $scope.loginData = data;
        });
    });
});