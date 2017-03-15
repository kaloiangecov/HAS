app.controller("calendarCtrl", function ($scope, $filter, $http) {
    var ctrl = this;
    $scope.page.title = "Calendar";

    $scope.roomTypes = sampleRoomTypes;
    $scope.roomTypes[3] = "All";

    $scope.events = {
        list: [],
        selected: [],
        new: {}
    };

    ctrl.selectedRoomType = 3;
    $scope.guests = {
        list: [],
        selectedGuest: {}
    };
    $scope.isNewGuest = true;
    $scope.isAdditionalGuest = false;
    $scope.isExistingGroup = false;
    $scope.reservationGuest = {
        reservation: {},
        guest: {},
        room: {}
    };
    $scope.newReservationGuest = {
        reservation: {},
        guest: {},
        room: {}
    };
    $scope.groupReservationsList = [];
    $scope.reservationInfo = {};

    $scope.getRooms = function (callback) {
        $http({
            method: "GET",
            url: "rooms",
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
            })
            .then(callback);
    };

    $scope.getFreeGuests = function (reservationId, updateCallback) {
        $http({
            method: "GET",
            url: ("guests/free/" + reservationId),
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

    $scope.getEmployeeByUserId = function (userID, updateCallback) {
        $http({
            method: "GET",
            url: ("employee/by-user/" + userID),
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

    $scope.saveReservation = function () {
        function afterEventCreated(data) {
            $scope.scheduler.message("New event created!");

            console.log("New reservation: ", data);

            $scope.resetReservation();
        }

        if ($scope.isNewGuest) { // create new guest
            $scope.reservationGuest.guest.numberReservations = 0;
            $scope.reservationGuest.guest.status = 0;

            $scope.saveData("guest", $scope.reservationGuest.guest, function (data) {
                $scope.reservationGuest.guest = data;
                $scope.saveData("reservation-guest", $scope.reservationGuest, afterEventCreated, $scope.resetReservation);
            }, $scope.resetReservation);
        }
        else { // use existing guest
            $scope.reservationGuest.guest = $scope.guests.selectedGuest;

            $scope.saveData("reservation-guest", $scope.reservationGuest, afterEventCreated, $scope.resetReservation);
        }
    };

    $scope.closeReservation = function (id, callback) {
        $http({
            method: "PUT",
            url: ("reservation/close/" + id),
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
                $scope.resetReservation();
            })
            .then(callback);
    };

    $scope.getGroupReservations = function () {
        var searchFilters = {
            startDate: $scope.config.startDate,
            endDate: moment($scope.config.startDate).add($scope.config.days, 'days').format("YYYY-MM-DD"),
            group: true
        };

        $http({
            method: "POST",
            url: "reservations/search",
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            },
            data: searchFilters
        })
            .then(
                function (response) {
                    $scope.groupReservationsList = response.data;
                },
                function (response) {
                    $scope.displayMessage(response.data);
                }
            );
    };

    $scope.validateCheckInDate = function (reservation, newRange) {
        var today = new Date().toISOString().substr(0, 10);

        if ((reservation.status == 0 && newRange.start < today)
            || (reservation.status == 1
            && (newRange.start != reservation.startDate
            || newRange.end < today))) {
            $scope.page.message = {
                type: 'danger',
                title: "Check in date error",
                text: ("Check in date " + new Date(newRange.start).toLocaleDateString() + " is not allowed for this reservation!")
            };

            return false;
        }
        return true;
    };

    $scope.calculateRoomGuests = function (event) {
        var n = 0;

        if (event.objReservation.group) {
            angular.forEach(event.objReservation.reservationGuests, function (reservationGuest, key) {
                if (reservationGuest.room.id == event.roomId)
                    n++;
            });
        } else {
            n = event.objReservation.reservationGuests.length;
        }

        return n;
    };

    $scope.startDate = new DayPilot.Date(new Date());
    $scope.endDate = $scope.startDate.addDays(21);
    $scope.startDate = $scope.startDate.addDays(-3);

    $scope.conextMenuItems = {
        showInfo: {
            text: '<i class="fa fa-info"></i> Show info',
            onclick: function () {
                var tmp = this.source.data.objReservation;
                $scope.$apply(function () {
                    $scope.reservationInfo = tmp;
                    $scope.reservationInfo.startDate = new Date($scope.reservationInfo.startDate).toLocaleDateString();
                    $scope.reservationInfo.endDate = new Date($scope.reservationInfo.endDate).toLocaleDateString();
                    $('#infoModal').modal('show');
                });

            }
        },
        addAnotherGuest: {
            text: '<i class="fa fa-user-plus"></i> Add another guest',
            onclick: function () {
                $scope.reservationGuest = this.source.data.objReservation.reservationGuests[0];

                var selectedRoom = $filter('filter')($scope.config.resources, {id: this.source.resource()})[0];
                var nGuests = $scope.calculateRoomGuests(this.source.data);
                var roomCapacity = (selectedRoom.bedsDouble * 2) + selectedRoom.bedsSingle;

                if (nGuests >= roomCapacity) {
                    $scope.page.message = {
                        type: 'danger',
                        title: "Can't add guest!",
                        text: "This room has reached its guest capacity!"
                    };
                    $('#messageModal').modal('show');

                    $scope.resetReservation();
                    return;
                }

                $scope.reservationGuest.guest = {};
                $scope.reservationGuest.reservation = angular.copy(this.source.data.objReservation);
                $scope.reservationGuest.room = selectedRoom;
                $scope.reservationGuest.id = null;
                $scope.reservationGuest.owner = false;

                delete $scope.reservationGuest.reservation.reservationGuests;

                $scope.isAdditionalGuest = true;

                $scope.getFreeGuests($scope.reservationGuest.reservation.id, function (data) {
                    $scope.guests.list = data;

                    if (data.length > 0)
                        $scope.guests.selectedGuest = data[0];
                    else {
                        $scope.isNewGuest = true;
                    }
                });

                $scope.$apply();

                $('#reservationModal').modal('show');
            }
        },
        checkIn: {
            text: '<i class="fa fa-check"></i> Check in',
            onclick: function () {
                if (this.source.data.objReservation.startDate != moment().format("YYYY-MM-DD"))
                    return;

                if (this.source.data.objReservation.status > 0)
                    return;

                if (confirm("Start reservation?\n" + "Start: " + this.source.start() + "\nEnd:" + this.source.end())) {
                    var tmp = this.source.data.objReservation;
                    tmp.status = 1;
                    $scope.saveData('reservation', tmp, function () {
                        $scope.scheduler.message("Reservation started: " + tmp.reservationGuests[0].guest.personalData.fullName);
                        loadEvents();
                    }, $scope.resetReservation, true);
                } else {
                    loadEvents();
                }
            }
        },
        checkOut: {
            text: '<i class="fa fa-times"></i> Check out',
            onclick: function () {
                if (this.source.data.objReservation.status != 1)
                    return;

                if (confirm("Close reservation?\n" + "Start: " + this.source.start() + "\nEnd:" + this.source.end())) {
                    //$scope.scheduler.events.remove(this.source);
                    var selectedRoom = $filter('filter')($scope.config.resources, {id: this.source.resource()})[0];

                    $scope.closeReservation(this.source.data.objReservation.id, function (data) {
                        $scope.scheduler.message("Reservation closed: " + data.reservationGuests[0].guest.personalData.fullName);

                        selectedRoom.status = 2;
                        $scope.saveData("room", selectedRoom, function () {
                        }, function () {
                            $scope.resetReservation();
                        }, true);

                        $scope.resetReservation();
                    });
                } else {
                    loadEvents();
                }
            }
        },
        cancel: {
            text: '<i class="fa fa-ban"></i> Cancel',
            onclick: function () {
                if (confirm("Delete reservation?\n" + "Start: " + this.source.start() + "\nEnd:" + this.source.end())) {
                    $scope.scheduler.events.remove(this.source);

                    $scope.deleteData("reservation", this.source.data.objReservation.id, function (data) {
                        $scope.scheduler.message("Reservation deleted: " + data.reservationGuests[0].guest.personalData.fullName);
                        $scope.resetReservation();
                    });
                } else {
                    loadEvents();
                }
            }
        }
    };

    $scope.config = {
        scale: "Day",
        startDate: $scope.startDate,
        days: 14,
        //scale: "Manual",
        //timeline: getTimeline(),
        //resources: $scope.events.resources,
        timeHeaders: [{groupBy: "Month"}, {groupBy: "Day", format: "d"}],
        eventDeleteHandling: "Update",
        eventClickHandling: "Select",
        allowEventOverlap: false,
        cellWidthSpec: "Auto",
        eventHeight: 50,
        rowHeaderColumns: [
            {title: "Room", width: 46},
            {title: "Capacity", width: 57}
        ],
        onBeforeCellRender: function (args) {
            if (args.cell.start <= DayPilot.Date.today() && DayPilot.Date.today() < args.cell.end) {
                args.cell.backColor = "#fff0b3";
            }
        },
        onBeforeResHeaderRender: function (args) {
            var beds = function (single, double) {
                var html = '<div class="bed">';
                html += single + '<br/>';
                html += '<img src="img/bed-single.png" alt="beds single"/>';
                html += '</div>';

                html += '<div class="bed">';
                html += double + '<br/>';
                html += '<img src="img/bed-double.png" alt="beds double"/>';
                html += '</div>';

                return html;
            };

            args.resource.name = args.resource.number;

            args.resource.columns[0].html = beds(args.resource.bedsSingle, args.resource.bedsDouble);
            //args.resource.columns[1].html = $scope.roomStatuses[args.resource.status];
            switch (args.resource.status) {
                case 1:
                    args.resource.cssClass = "status_dirty";
                    break;
                case 2:
                    args.resource.cssClass = "status_cleanup";
                    break;
            }
        },
        onEventSelected: function (args) {
            $scope.$apply(function () {
                $scope.events.selected = $scope.scheduler.multiselect.events();
            });
        },
        onEventMoved: function (args) {
            $scope.scheduler.clearSelection();

            var tmpReservation = args.e.data.objReservation;

            if (tmpReservation.status > 1) {
                loadEvents();
                return;
            }

            var selectedRoom = $filter('filter')($scope.config.resources, {id: args.newResource})[0];
            var nGuests = $scope.calculateRoomGuests(args.e.data);
            var roomCapacity = (selectedRoom.bedsDouble * 2) + selectedRoom.bedsSingle;

            if (nGuests > roomCapacity) {
                $scope.page.message = {
                    type: 'danger',
                    title: "Can't change the room!",
                    text: "The new room doesn't have enough capacity!"
                };
                $('#messageModal').modal('show');

                $scope.resetReservation();
                return;
            }

            var newRange = {
                start: args.newStart.value.substr(0, 10),
                end: args.newEnd.value.substr(0, 10)
            };
            if (!$scope.validateCheckInDate(tmpReservation, newRange)) {
                $('#messageModal').modal('show');
                loadEvents();
                return;
            }

            if (!confirm("Are you sure you want to move the reservation?")) {
                loadEvents();
                return;
            }

            tmpReservation.startDate = newRange.start;
            tmpReservation.endDate = newRange.end;

            $scope.getSingleData("room", args.newResource, function (room) {
                if (!tmpReservation.group) {
                    angular.forEach(tmpReservation.reservationGuests, function (value, key) {
                        value.room = room;
                    });
                } else {
                    var oldRoomId = args.e.data.roomId;

                    angular.forEach(tmpReservation.reservationGuests, function (resGuest, key) {
                        if (resGuest.room.id == oldRoomId) {
                            resGuest.room = room;
                        }
                    });

                    args.e.data.roomId = room.id;
                }

                $scope.saveData("reservation", tmpReservation, function (data) {
                    $scope.scheduler.message("Reservation moved: " + args.e.text());
                    $scope.resetReservation();
                }, $scope.resetReservation, true);

            }, loadEvents);
        },
        onEventResized: function (args) {
            $scope.scheduler.clearSelection();

            var tmpReservation = args.e.data.objReservation;

            if (tmpReservation.status > 1) {
                loadEvents();
                return;
            }

            var newRange = {
                start: args.newStart.value.substr(0, 10),
                end: args.newEnd.value.substr(0, 10)
            };
            if (!$scope.validateCheckInDate(tmpReservation, newRange)) {
                $('#messageModal').modal('show');
                loadEvents();
                return;
            }

            if (!confirm("Are you sure you want to resize the reservation?")) {
                loadEvents();
                return;
            }

            tmpReservation.startDate = newRange.start;
            tmpReservation.endDate = newRange.end;

            if (tmpReservation)

                $http({
                    method: "PUT",
                    url: ("reservation/move/" + tmpReservation.id),
                    responseType: "json",
                    headers: {
                        "Authorization": $scope.authentication
                    },
                    data: tmpReservation
                }).then(
                    function (response) { //success
                        return response.data;
                    },
                    function (response) { //error
                        $scope.displayMessage(response.data);
                        $scope.resetReservation();
                    })
                    .then(function (data) {
                        $scope.scheduler.message("Reservation duration resized: " + args.e.text());
                        $scope.resetReservation();
                    });
        },
        onEventDeleted: function (args) {
            $scope.scheduler.clearSelection();

            if (args.e.data.objReservation.status > 0) {
                loadEvents();
                return;
            }

            if (confirm("Delete reservation?\n" + "Start: " + args.e.start() + "\nEnd:" + args.e.end())) {
                $scope.scheduler.events.remove(args.e);

                $scope.deleteData("reservation", args.e.data.objReservation.id, function (data) {
                    $scope.scheduler.message("Reservation deleted: " + args.e.text());
                    $scope.resetReservation();
                });
            } else {
                loadEvents();
            }


        },
        onTimeRangeSelected: function (args) {
            $scope.scheduler.clearSelection();
            //clearInterval($scope.timer);
            //loadEvents();

            if (!$scope.events.new.start && (args.start.value.substr(0, 10) >= moment().format('YYYY-MM-DD'))) {
                $scope.$apply(function () {
                    $scope.events.new = {
                        start: args.start,
                        end: args.end,
                        resource: args.resource
                    };
                });

                $scope.getGroupReservations();

                $scope.getFreeGuests(-1, function (data) {
                    $scope.guests.list = data;

                    if (data.length > 0)
                        $scope.guests.selectedGuest = data[0];
                    else {
                        $scope.isNewGuest = true;
                    }
                });

                $('#reservationModal').modal('show');
            }
        },
        onEventClick: function (args) {
            var reservationStatus = args.e.data.objReservation.status;
            var menuItems = [
                $scope.conextMenuItems.showInfo
            ];

            switch (reservationStatus) {
                case 0:
                    menuItems.push($scope.conextMenuItems.addAnotherGuest);
                    menuItems.push($scope.conextMenuItems.checkIn);
                    menuItems.push($scope.conextMenuItems.cancel);
                    break;
                case 1:
                    menuItems.push($scope.conextMenuItems.addAnotherGuest);
                    menuItems.push($scope.conextMenuItems.checkOut);
                    break;
            }

            $scope.config.contextMenu = new DayPilot.Menu({items: menuItems});

        },
        onBeforeEventRender: function (args) {
            var start = new DayPilot.Date(args.data.start);
            var end = new DayPilot.Date(args.data.end);

            args.data.start = start.addHours(12);
            args.data.end = end.addHours(12);

            var now = new DayPilot.Date();
            var today = new DayPilot.Date().getDatePart();
            var status = "";

            // customize the reservation bar color and tooltip depending on status
            switch (args.e.status) {
                case 0:
                    var checkinDeadline = today.addHours(21);
                    if ((start < today) || (start === today && now > checkinDeadline)) { // must checkin before 21:00
                        args.data.barColor = "#e55";  // red
                        status = "Late Arrival";
                    } else {
                        args.data.barColor = "#feda55";  // orange
                        status = "New"
                    }
                    break;
                case 1: // arrived
                    var checkoutDeadline = today.addHours(12);

                    if ((end < today) || (end === today && now > checkoutDeadline)) { // must checkout before 10:00
                        args.data.barColor = "#e55";  // red
                        status = "Late checkout";
                    }
                    else {
                        args.data.barColor = "#1691f4";  // blue
                        status = "Arrived";
                    }
                    break;
                case 2: // checked out
                    args.data.barColor = "red";
                    status = "Checked out";
                    break;
                default:
                    status = "Unexpected state";
                    break;
            }

            // customize the reservation HTML: text, start and end dates
            args.data.html = args.data.text + " (" + start.toString("d/M/yyyy") + " - " + end.toString("d/M/yyyy") + ")" + "<br /><span style='color:gray'>" + status + "</span>";

            // reservation tooltip that appears on hover - displays the status text
            args.e.toolTip = status;
        },
        onEventFilter: function (args) {

        }
    };

    function loadEvents() {
        var range = {
            startDate: $scope.config.startDate,
            endDate: moment($scope.config.startDate).add($scope.config.days, 'days').format("YYYY-MM-DD")
        };

        $http({
            method: "POST",
            url: "reservations/search",
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            },
            data: range
        }).then(
            function (response) { //success
                $scope.events.list = [];

                angular.forEach(response.data, function (reservation, key) {
                    var tmpEvent = {
                        start: reservation.startDate,
                        end: reservation.endDate,
                        //id: reservation.id,
                        //resource: reservation.reservationGuests[0].room.id,
                        status: reservation.status,
                        text: reservation.reservationGuests[0].guest.personalData.fullName,
                        objReservation: reservation
                    };

                    if (reservation.status > 0) {
                        tmpEvent.deleteEnabled = false;

                        if (reservation.status == 2) {
                            tmpEvent.moveEnabled = false;
                            tmpEvent.resizeEnabled = false;
                        }
                    }

                    if (!reservation.group) {
                        tmpEvent.id = reservation.id;
                        tmpEvent.resource = reservation.reservationGuests[0].room.id;
                        $scope.events.list.push(tmpEvent);
                    } else {
                        var resRooms = [];
                        angular.forEach(reservation.reservationGuests, function (resGuest, index) {
                            if (resRooms.indexOf(resGuest.room.id) == -1) {
                                resRooms.push(resGuest.room.id);

                                var tmpEvent2 = angular.extend(
                                    {},
                                    tmpEvent,
                                    {
                                        id: (key + '_' + (index + 1)),
                                        resource: resGuest.room.id,
                                        roomId: resGuest.room.id
                                    }
                                );

                                $scope.events.list.push(tmpEvent2);
                            }
                        });
                    }
                });
            },
            function (response) { //error
                $scope.displayMessage(response.data);
            });
    }

    $scope.changeRoomType = function () {
        $scope.getRooms(function (data) {
            var orderedRooms = $filter('orderBy')(data, "number");

            if (ctrl.selectedRoomType == 3) {
                $scope.config.resources = orderedRooms;
            } else {
                var filtered = $filter('filter')(orderedRooms, {roomClass: ctrl.selectedRoomType});
                $scope.config.resources = filtered;
            }
        });
    };

    $scope.submit = function () {
        if ($scope.isAdditionalGuest)
            $scope.addGuest();
        else
            $scope.addReservation();
    };

    $scope.reset = function () {
        if ($scope.isAdditionalGuest) {
            $scope.resetGuest();
            loadEvents();
        }
        else
            $scope.resetReservation();
    };

    $scope.addReservation = function () {

        $scope.getEmployeeByUserId($scope.loginData.id, function (data) {
            $scope.reservationGuest.room = $filter('filter')($scope.config.resources, {id: $scope.events.new.resource})[0];
            $scope.reservationGuest.room.status = 1;
            $scope.reservationGuest.owner = true;

            var objReservation = {};

            if ($scope.isExistingGroup && $scope.reservationGuest.reservation.id) {
                objReservation = $scope.reservationGuest.reservation;
                $scope.reservationGuest.owner = false;
                $scope.saveReservation();

                $('#reservationModal').modal('hide');
                return;
            } else {
                objReservation = {
                    startDate: $scope.events.new.start.value.substr(0, 10),
                    endDate: $scope.events.new.end.value.substr(0, 10),
                    breakfast: $scope.reservationGuest.reservation.breakfast,
                    dinner: $scope.reservationGuest.reservation.dinner,
                    allInclusive: $scope.reservationGuest.reservation.allInclusive,
                    price: 40.0,
                    discount: 0,
                    numberAdults: 1,
                    numberChildren: 0,
                    status: 0,
                    group: $scope.reservationGuest.reservation.group,
                    receptionist: data
                };
            }

            $scope.saveData("reservation", objReservation, function (newReservation) {
                $scope.reservationGuest.reservation = newReservation;
                $scope.saveReservation();
            }, $scope.resetReservation);

            $('#reservationModal').modal('hide');
        });

    };

    $scope.resetReservation = function () {
        $scope.events.new = {};
        $scope.events.selected = [];
        $scope.guests.selectedGuest = {};
        $scope.reservationGuest = {
            reservation: {},
            guest: {},
            room: {}
        };

        loadEvents();
    };

    $scope.addGuest = function () {
        if ($scope.isNewGuest) {
            $scope.reservationGuest.guest.numberReservations = 0;
            $scope.reservationGuest.guest.status = 0;

            $scope.saveData("guest", $scope.reservationGuest.guest, function (data) {
                $scope.reservationGuest.guest = data;
                $scope.reservationGuest.reservation.lastModifiedBy = $scope.loginData;
                $scope.reservationGuest.reservation.lastModifiedTime = new Date().toISOString();

                $scope.saveData("reservation-guest", $scope.reservationGuest, function (data) {
                    $scope.scheduler.message("Added new guest: " + $scope.reservationGuest.guest.personalData.fullName);

                    $scope.resetGuest();
                    loadEvents();
                }, $scope.resetGuest);
            }, $scope.resetGuest);
        } else {
            $scope.reservationGuest.guest = $scope.guests.selectedGuest;
            $scope.reservationGuest.reservation.lastModifiedBy = $scope.loginData;
            $scope.reservationGuest.reservation.lastModifiedTime = new Date().toISOString();

            $scope.saveData("reservation-guest", $scope.reservationGuest, function (data) {
                $scope.scheduler.message("Added new guest to reservation!");

                $scope.resetGuest();
                loadEvents();
            }, $scope.resetGuest);
        }

        $('#reservationModal').modal('hide');
    };
    $scope.resetGuest = function () {
        $scope.reservationGuest = {
            reservation: {},
            guest: {},
            room: {}
        };
        $scope.isAdditionalGuest = false;
    };

    $scope.scrollTo = function (date) {
        $scope.scheduler.scrollTo(date);
    };

    $scope.setScale = function (val) {
        $scope.config.scale = val;
        if (val == "Day") {
            $scope.config.days = 14;
        }
    };

    function setDateRange(start, end) {
        $scope.$apply(function () {
            var tmp = end._d.getTime() - start._d.getTime();
            //$scope.config.timeline = getTimeline(start._d, tmp / 86400000);
            $scope.config.startDate = start._d.toISOString().substr(0, 10);
            $scope.config.days = tmp / 86400000;

            loadEvents();
        });

    }

    angular.element(document).ready(function () {
        $scope.changeRoomType();

        loadEvents();

        //$scope.timer = setInterval(loadEvents, 10000);

        $('#dateRange').daterangepicker({
            parentEl: "#scheduleContainer",
            startDate: $scope.startDate,
            endDate: $scope.endDate,
            locale: {
                format: "DD/MM/YY"
            }
        }, setDateRange);

        $('.calendar').css({float: 'left'});

        //$('#selectGuest').select2();
    });
});