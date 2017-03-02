app2.controller("mainCtrl2", function ($scope, $state, $http, $timeout) {
    var ctrl = this;
    $scope.page = {
        title: "Booking"
    };

    $scope.roomTypes = sampleRoomTypes;
    $scope.roomStatuses = sampleRoomStatuses;

    ctrl.filters = {
        numberAdults: 1,
        numberChildren: 0,
        startDate: moment().format('YYYY-MM-DD'),
        endDate: moment().format('YYYY-MM-DD')
    };
    $scope.results = [];

    $scope.reservation = {};
    $scope.guest = {};
    $scope.guestFound = false;
    $scope.selectedRooms = [];

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

    $scope.toggleSelection = function toggleSelection(room) {
        var idx = $scope.selectedRooms.indexOf(room);

        // Is currently selected
        if (idx > -1) {
            $scope.selectedRooms.splice(idx, 1);
        }

        // Is newly selected
        else {
            $scope.selectedRooms.push(room);
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
        $('#messageModal').modal('show');

        if (response.status === 401) {
            $scope.authentication = "";
            $scope.loginData = {};
            window.location.hash = "#!/login";
        } else if (response.status === 403) {
            window.location.hash = "#!/home";
        }
    };

    $scope.getRole = function (roleID, callback) {
        $http({
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
            }).then(callback);
    };

    $scope.getGuestUserByEmail = function (email) {
        $http({
            method: "POST",
            url: "guest/by-email",
            responseType: "json",
            data: {email: email}
        }).then(
            function (response) { //success
                return response.data;
            }, $scope.clearGuestData)
            .then(function (data) {
                if (data) {
                    $scope.guest = data;
                    $scope.guestFound = true;
                } else {
                    $scope.guestFound = false;
                }
            });
    };

    $scope.clearGuestData = function () {
        $scope.guest = {};
        $scope.guestFound = false;
    };

    function setDateRange(start, end, label) {
        $scope.$apply(function () {
            //var tmp = end._d.getTime() - start._d.getTime();
            //$scope.config.timeline = getTimeline(start._d, tmp / 86400000);
            ctrl.filters.startDate = start._d.toISOString().substr(0, 10);
            ctrl.filters.endDate = end._d.toISOString().substr(0, 10);
        });

    };

    $scope.search = function () {
        $http({
            method: "POST",
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

    $scope.selectRooms = function () {
        $scope.reservation = {
            startDate: ctrl.filters.startDate,
            endDate: ctrl.filters.endDate,
            group: ($scope.selectedRooms.length > 0),
            status: 0,
            numberAdults: ctrl.filters.numberAdults,
            numberChildren: ctrl.filters.numberChildren,
            dinner: false,
            breakfast: true,
            allInclusive: false
        };
        $state.go('app.root.personalData');
    };

    $scope.submitReservation = function () {
        if (!$scope.guest.user) {
            $scope.getRole(5, function (role) {
                $scope.guest.user.username = $scope.guest.user.email;
                $scope.guest.user.userRole = role;
            });
        }
        $scope.reservationGuest = {
            reservation: $scope.reservation,
            guest: $scope.guest,
            room: $scope.selectedRooms[0],
            isOwner: true
        };

        console.log($scope.reservationGuest);

        //$state.go('app.root.personalData');
    };

    angular.element(document).ready(function () {
        $timeout(function () {
            $('#dateRange').daterangepicker({
                parentEl: "#filters",
                startDate: new Date(ctrl.filters.startDate),
                endDate: new Date(ctrl.filters.endDate),
                locale: {
                    format: "DD/MM/YY"
                }
            }, setDateRange);

            $('#identityIssueDate,#identityExpireDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true,
                locale: {
                    format: "YYYY-MM-DD"
                }
            });

            $('.calendar').css({float: 'left'});
        }, 500);

    });
});