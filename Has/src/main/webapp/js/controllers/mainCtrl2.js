app2.controller("mainCtrl2", function ($rootScope, $scope, $state, $http, $timeout) {
    var ctrl = this;
    $scope.page = {
        title: "Booking",
        activePage: "home"
    };

    $scope.authentication = "Basic " + btoa("booking:B00king");

    $scope.roomTypes = sampleRoomTypes;
    $scope.roomStatuses = sampleRoomStatuses;

    $scope.filters = {
        numberAdults: 1,
        numberChildren: 0,
        pets: false,
        minibar: false,
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
                $scope.reservation = reservation;

                $scope.filters = {
                    startDate: reservation.startDate,
                    endDate: reservation.endDate,
                    numberAdults: reservation.numberAdults,
                    numberChildren: reservation.numberChildren,
                    pets: reservation.room.pets,
                    minibar: reservation.room.minibar
                };

                $timeout(function () {
                    $scope.dr2 = $('#newDateRange').daterangepicker({
                        startDate: new Date($scope.reservation.startDate),
                        endDate: new Date($scope.reservation.endDate)
                    });
                    $scope.dr2.on('apply.daterangepicker', function (ev, picker) {
                        setDateRange(picker.startDate.format('YYYY-MM-DD'), picker.endDate.format('YYYY-MM-DD'))
                    });
                    $('.calendar').css({float: 'left'});

                    $rootScope.switchNewPets = new Switchery(document.getElementById('newPets'), {color: "#266CEa"});
                    $rootScope.switchNewMinibar = new Switchery(document.getElementById('newMinibar'), {color: "#266CEa"});
                    $rootScope.switchNewBreakfast = new Switchery(document.getElementById('newBreakfast'), {color: "#266CEa"});
                    $rootScope.switchNewDinner = new Switchery(document.getElementById('newDinner'), {color: "#266CEa"});
                    $rootScope.switchNewAllInclusive = new Switchery(document.getElementById('newAllInclusive'), {color: "#EA6C26"});
                }, 200);
            });
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

    $scope.clearGuestData = function () {
        $scope.guest = {};
        $scope.guestFound = false;
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
            data: $scope.filters
        }).then(
            function (response) { //success
                return response.data;
            },
            function (response) { //error
                $scope.displayMessage(response.data);
            })
            .then(function (results) {
                $scope.results = results;
            });
    };

    $scope.selectRooms = function () {
        $scope.reservation = {
            startDate: $scope.filters.startDate,
            endDate: $scope.filters.endDate,
            status: 0,
            numberAdults: $scope.filters.numberAdults,
            numberChildren: $scope.filters.numberChildren,
            dinner: false,
            breakfast: true,
            allInclusive: false,
            price: 30.0,
            discount: 0
        };
        $state.go('app.root.personalData');
    };

    $scope.clearEverything = function () {
        $scope.guest = {};
        $scope.reservation = {};
        $scope.reservationGuest = {};
        $scope.selectedRooms = [];
        $scope.results = [];
    };

    $scope.addReservationsToGroup = function (mainReservations) {
        delete mainReservations.reservationCode;
        delete mainReservations.id;

        for (var i = 1; i < $scope.selectedRooms.length; i++) {
            $scope.saveData("reservation?group=true&groupId=" + (mainReservations.groupId),
                angular.extend({}, mainReservations, {room: $scope.selectedRooms[i]}),
                function (newGroup) {
                    console.log('additional group ' + (i + 1), mainReservations);
                });
        }
    };

    $scope.submitReservation = function () {
        $scope.reservationGuest = {
            owner: true
        };
        $scope.reservation.room = $scope.selectedRooms[0];

        var isGroup = ($scope.selectedRooms.length > 1);

        $scope.saveData("reservation?group=" + isGroup, $scope.reservation, function (newReservation) {
            console.log("reservation", newReservation);

            $scope.reservationGuest.reservation = newReservation;

            if (!$scope.guest.user.username && $scope.guest.user.email) { // create new guest
                $scope.getRole(5, function (role) {
                    $scope.guest.user.username = $scope.guest.user.email;
                    $scope.guest.user.password = base64encode($scope.guest.user.email);
                    $scope.guest.user.userRole = role;
                    $scope.guest.user.regDate = (new Date()).toISOString().substr(0, 10);

                    $scope.guest.numberReservations = 0;
                    $scope.guest.status = 0;

                    $scope.saveData("guest", $scope.guest, function (newGuest) {
                        $scope.reservationGuest.guest = newGuest;
                        $scope.saveData("reservation-guest", $scope.reservationGuest, function (newReservationGuest) {
                            console.log(newReservationGuest);

                            if (isGroup)
                                $scope.addReservationsToGroup(angular.copy(newReservation));

                            $scope.reservationInfo = newReservation;
                            $scope.reservationInfo.reservationGuests = [newReservationGuest];

                            $scope.clearEverything();

                            $state.go('app.root.reservationSuccessful');
                        });
                    }, $scope.resetReservation);
                });
            }
            else { // use existing guest
                $scope.reservationGuest.guest = $scope.guest;

                $scope.saveData("reservation-guest", $scope.reservationGuest, function (newReservationGuest) {
                    console.log("reservation owner", newReservationGuest);

                    if (isGroup)
                        $scope.addReservationsToGroup(angular.copy(newReservation));

                    $scope.reservationInfo = newReservation
                    $scope.reservationInfo.reservationGuests = [newReservationGuest];

                    $scope.clearEverything();

                    $state.go('app.root.reservationSuccessful');
                });
            }
        });
    };

    $scope.editReservation = function () {
        if ($scope.selectedRooms.length > 0) {
            $scope.reservation.room = $scope.selectedRooms[0];
        }

        var isGroup = ($scope.selectedRooms.length > 1);

        $scope.reservation.startDate = $scope.filters.startDate;
        $scope.reservation.endDate = $scope.filters.endDate;


        $scope.saveData("reservation", $scope.reservation,
            function (data) { //success
                $scope.page.message = {
                    type: 'success',
                    title: $scope.reservation.reservationGuests[0].personalData.fullName,
                    text: 'Reservation was successfully changed'
                };
                $('#messageModal').modal('show');
                $scope.clearEverything();
            },
            function () { //error

            }, true);
    };

    function setDateRange(start, end) {
        $scope.$apply(function () {
            $scope.filters.startDate = start;
            $scope.filters.endDate = end;
        });
    }

    $scope.$watch('reservation.allInclusive', function (newVal, oldVal) {
        if (newVal === true) {
            if ($rootScope.switchDinner) {
                $rootScope.switchDinner.disable();
                $rootScope.switchBreakfast.disable();
            }
            if ($rootScope.switchNewDinner) {
                $rootScope.switchNewDinner.disable();
                $rootScope.switchNewBreakfast.disable();
            }
        } else {
            if ($rootScope.switchDinner) {
                $rootScope.switchDinner.enable();
                $rootScope.switchBreakfast.enable();
            }
            if ($rootScope.switchNewDinner) {
                $rootScope.switchNewDinner.enable();
                $rootScope.switchNewBreakfast.enable();
            }
        }
    });

    angular.element(document).ready(function () {
        $timeout(function () {
            if ($rootScope.dr1) {
                $rootScope.dr1.on('apply.daterangepicker', function (ev, picker) {
                    setDateRange(picker.startDate.format('YYYY-MM-DD'), picker.endDate.format('YYYY-MM-DD'))
                });
            }
        }, 500);


    });
});