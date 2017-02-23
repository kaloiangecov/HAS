app2.controller("mainCtrl2", function ($scope, $http, $timeout) {
    var ctrl = this;
    $scope.page = {
        title: "Booking"
    };

    $scope.roomTypes = sampleRoomTypes;
    $scope.roomStatuses = sampleRoomStatuses;
    $scope.credentials = {
        username: "",
        password: ""
    };
    $scope.authentication = "";

    ctrl.filters = {};
    $scope.results = [];

    $scope.myInterval = 5000;
    $scope.noWrapSlides = false;
    $scope.active = 0;
    $scope.slides = [
        {
            id: 1,
            image: 'img/carousel1.jpg',
            alt: 'Carousel Image'
        },
        {
            id: 2,
            image: 'img/carousel2.jpg',
            alt: 'Carousel Image'
        }
    ];

    function setDateRange(start, end, label) {
        $scope.$apply(function () {
            //var tmp = end._d.getTime() - start._d.getTime();
            //$scope.config.timeline = getTimeline(start._d, tmp / 86400000);
            ctrl.filters.startDate = start._d.toISOString().substr(0, 10);
            ctrl.filters.endDate = end._d.toISOString().substr(0, 10);
        });

    };

    $scope.submit = function () {
        $http({
            method: "GET",
            url: "reservations/booking",
            responseType: "json",
            headers: {
                //'Accept': 'application/json',
                'Content-Type': 'application/json',
                "Authorization": $scope.authentication
            },
            data: ctrl.filters
        }).then(
            function (response) { //success
                return response.data;
            },
            function (response) { //error
                console.log(response);
            })
            .then(function (results) {
                $scope.results = results;
            });
    };

    angular.element(document).ready(function () {
        $timeout(function () {
            $('#dateRange').daterangepicker({
                parentEl: "#filters",
                startDate: new Date(),
                endDate: new Date(),
                locale: {
                    format: "DD/MM/YY"
                }
            }, setDateRange);

            $('.calendar').css({float: 'left'});
        }, 500);


    });
});