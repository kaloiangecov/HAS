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
        url: "sample_data/countries.json"
    })
        .then(
            function (response) { //success
                $scope.countries = response.data.countries;
            },
            function (response) { //error
                alert(response.statusText);
            });

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
        $http({
            method: "GET",
            url: "sample_data/sampleUsers.json"
        })
            .then(
                function (response) { //success
                    $scope.loginData = angular.copy(response.data.data[0]);
                },
                function (response) { //error
                    alert(response.statusText);
                });
    });
});