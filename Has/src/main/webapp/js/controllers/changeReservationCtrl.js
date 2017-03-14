app2.controller("changeReservationCtrl", function ($scope, $state, $http, $timeout) {
    var ctrl = this;

    $scope.getReservationByCode = function (code) {
        $http({
            method: "GET",
            url: ("reservation/code/" + code),
            responseType: "json"
        }).then(
            function (response) { //success
                return response.data;
            },
            function (response) { //error
                $scope.displayMessage(response.data);
            })
            .then(function (reservation) {
                ctrl.reservation = reservation;
                $('#newDateRange').daterangepicker({
                    startDate: new Date(ctrl.reservation.startDate),
                    endDate: new Date(ctrl.reservation.endDate)
                });
            });
    };

    $scope.clearReservation = function () {
        ctrl.reservation = {};
    };

    function setNewDateRange(start, end, label) {
        $scope.$apply(function () {
            //var tmp = end._d.getTime() - start._d.getTime();
            //$scope.config.timeline = getTimeline(start._d, tmp / 86400000);
            ctrl.reservation.startDate = start._d.toISOString().substr(0, 10);
            ctrl.reservation.endDate = end._d.toISOString().substr(0, 10);
        });

    }

    angular.element(document).ready(function () {
        $timeout(function () {
            $('#newDateRange').daterangepicker({
                parentEl: "body",
                startDate: new Date(),
                endDate: new Date(),
                locale: {
                    format: "DD/MM/YY"
                }
            }, setNewDateRange);

            $('.calendar').css({float: 'left'});
        }, 500);

    });
});