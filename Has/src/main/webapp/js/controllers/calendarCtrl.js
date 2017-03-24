app.controller("calendarCtrl", function ($scope, $filter, $http, $sce, $interval) {
    var ctrl = this;
    $scope.page.title = "Reception";

    $scope.roomTypes[3] = "All";

    $scope.events = {
        list: [],
        groupColors: {},
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
    $scope.isGroupReservation = false;
    $scope.isExistingGroup = false;
    $scope.reservationGuest = {
        reservation: {},
        guest: {},
        room: {}
    };
    $scope.groupReservationsList = [];
    $scope.selectedGroupReservation = {};
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

    $scope.getFreeGuests = function (range, updateCallback) {
        var url = "guests/free?startDate=" + range.startDate + "&endDate=" + range.endDate;

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

    $scope.saveReservationGuest = function () {
        function afterEventCreated(data) {
            $scope.scheduler.message("New event created!");
            $scope.resetReservation();
        }

        if ($scope.isNewGuest) { // create new guest
            $scope.reservationGuest.guest.numberReservations = 0;
            $scope.reservationGuest.guest.status = 0;

            $scope.saveData("guest", $scope.reservationGuest.guest, function (data) {
                $scope.reservationGuest.guest = data;
                $scope.saveData("reservation-guest", $scope.reservationGuest,
                    afterEventCreated, $scope.resetReservation);
            }, $scope.resetReservation);
        }
        else { // use existing guest
            $scope.reservationGuest.guest = $scope.guests.selectedGuest;
            $scope.saveData("reservation-guest", $scope.reservationGuest, afterEventCreated, $scope.resetReservation);
        }
    };

    $scope.checkReservation = function (action, id, callback) {
        $http({
            method: "PUT",
            url: ("reservation/" + action + "/" + id),
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

    $scope.closeRoom = function (reservationId, roomId, callback) {
        $http({
            method: "PUT",
            url: ("reservation-guest/" + reservationId + "/" + roomId),
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
        var range = {
            startDate: $scope.config.startDate,
            endDate: moment($scope.config.startDate).add($scope.config.days, 'days').format("YYYY-MM-DD")
        };

        $http({
            method: "GET",
            url: ("reservations/search?startDate=" + range.startDate + "&endDate=" + range.endDate + "&group=true"),
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            }
        })
            .then(
                function (response) {
                    $scope.groupReservationsList = [];
                    angular.forEach(response.data, function (reservation, key) {
                        var repeating = $filter('filter')(
                            $scope.groupReservationsList,
                            {groupId: reservation.groupId});

                        if (!repeating || repeating.length == 0)
                            $scope.groupReservationsList.push(reservation);
                    });

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

    $scope.validateGroupCheckInDate = function (reservation) {
        var groups = $filter('filter')(
            $scope.events.list, function (event) {
                return (event.objReservation.groupId == reservation.groupId);
            });
        var differentGroupStart = false;

        angular.forEach(groups, function (event, key) {
            if (event.objReservation.startDate != reservation.startDate) {
                differentGroupStart = true;
                return;
            }
        });

        if (differentGroupStart) {
            var text = "Some rooms of this group reservation are not set to check in at the same date!";
            text += (' Please, look at the other reservations whith the same label: <span class="label label-default" style="background-color:'
            + $scope.events.groupColors[reservation.groupId] + '">Group</span>');

            $scope.page.message = {
                type: 'danger',
                title: "Group check in date error",
                text: $sce.trustAsHtml(text)
            };
            return false;
        }

        return true;
    };

    function getRandomColor() {
        var letters = '0123456789ABCDEF';
        var color = '#';
        for (var i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }

    var beginning = moment().subtract(3, "days");
    var ending = moment().add(21, "days");

    $scope.startDate = beginning.format("YYYY-MM-DD");
    $scope.endDate = ending.format("YYYY-MM-DD");

    $scope.conextMenuItems = {
        showInfo: {
            text: '<i class="fa fa-info"></i> Show info',
            onclick: function () {
                var tmp = angular.copy(this.source.data.objReservation);
                $scope.$apply(function () {
                    $scope.reservationInfo = tmp;

                    if ($scope.reservationInfo.groupId) {
                        var repeating = $filter('filter')(
                            $scope.events.list, function (event) {
                                return (event.objReservation.groupId == $scope.reservationInfo.groupId);
                            });

                        if (repeating && repeating.length > 0) {
                            $scope.reservationInfo.guestsList = [];
                            $scope.reservationInfo.group = true;

                            angular.forEach(repeating, function (event, key) {
                                angular.forEach(event.objReservation.reservationGuests, function (reservationGuest, key) {
                                    $scope.reservationInfo.guestsList.push(
                                        {
                                            guest: reservationGuest.guest,
                                            room: event.objReservation.room,
                                            owner: reservationGuest.owner
                                        }
                                    );
                                });
                            });
                        }
                    } else {
                        $scope.reservationInfo.guestsList = $scope.reservationInfo.reservationGuests;
                    }

                    $scope.reservationInfo.startDate = new Date($scope.reservationInfo.startDate).toLocaleDateString();
                    $scope.reservationInfo.endDate = new Date($scope.reservationInfo.endDate).toLocaleDateString();
                    $('#infoModal').modal('show');
                });
            }
        },
        addAnotherGuest: {
            text: '<i class="fa fa-user-plus"></i> Add another guest',
            onclick: function () {
                $scope.reservationGuest = {};

                var tmpReservation = this.source.data.objReservation;

                var selectedRoom = $filter('filter')($scope.config.resources, {id: this.source.resource()})[0];
                if (tmpReservation.reservationGuests && tmpReservation.reservationGuests.length > 0) {
                    var nGuests = this.source.data.objReservation.reservationGuests.length;
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
                }

                $scope.reservationGuest.guest = {};
                $scope.reservationGuest.reservation = angular.copy(tmpReservation);
                $scope.reservationGuest.room = selectedRoom;
                $scope.reservationGuest.owner = false;

                delete $scope.reservationGuest.reservation.reservationGuests;

                $scope.isAdditionalGuest = true;

                $scope.getFreeGuests(
                    {
                        startDate: tmpReservation.startDate,
                        endDate: tmpReservation.endDate
                    },
                    function (data) {
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
                var tmpReservation = this.source.data.objReservation;

                if (tmpReservation.startDate != moment().format("YYYY-MM-DD"))
                    return;

                if (tmpReservation.status > 0)
                    return;

                if (tmpReservation.groupId && !$scope.validateGroupCheckInDate(tmpReservation)) {
                    $('#messageModal').modal('show');
                    loadEvents();
                    return;
                }

                if (confirm("Start reservation?\n" + "Start: " + this.source.start() + "\nEnd:" + this.source.end())) {
                    $scope.checkReservation('checkin', tmpReservation.id, function (data) {
                        $scope.scheduler.message("Reservation started: " + data.reservationGuests[0].guest.personalData.fullName);
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
                    $scope.checkReservation('close', this.source.data.objReservation.id, function (data) {
                        $scope.scheduler.message("Reservation closed: " + data.reservationGuests[0].guest.personalData.fullName);
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
        eventClickHandling: "Disabled",
        allowEventOverlap: false,
        cellWidthSpec: "Auto",
        eventHeight: 50,
        rowHeaderColumns: [
            {title: "Room", width: 46},
            {title: "Capacity", width: 60}
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
            var nGuests = tmpReservation.reservationGuests.length;
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
            tmpReservation.room = selectedRoom;

            $scope.saveData("reservation", tmpReservation, function (data) {
                $scope.scheduler.message("Reservation moved: " + args.e.text());
                $scope.resetReservation();
            }, $scope.resetReservation, true);
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

            var range = {
                startDate: args.start.value.substr(0, 10),
                endDate: args.end.value.substr(0, 10)
            };

            if (!$scope.events.new.start && (range.startDate >= moment().format('YYYY-MM-DD'))) {
                $scope.$apply(function () {
                    $scope.events.new = {
                        start: args.start,
                        end: args.end,
                        resource: args.resource
                    };
                });

                $scope.reservationGuest.reservation.breakfast = true;

                $scope.getGroupReservations();

                $scope.getFreeGuests(range, function (data) {
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
        onEventMouseOver: function (args) {
            var reservation = args.e.data.objReservation;
            var menuItems = [
                $scope.conextMenuItems.showInfo
            ];

            switch (reservation.status) {
                case 0:
                    menuItems.push($scope.conextMenuItems.addAnotherGuest);

                    if (reservation.startDate == moment().format("YYYY-MM-DD"))
                        menuItems.push($scope.conextMenuItems.checkIn);

                    menuItems.push($scope.conextMenuItems.cancel);
                    break;
                case 1:
                    menuItems.push($scope.conextMenuItems.addAnotherGuest);
                    menuItems.push($scope.conextMenuItems.checkOut);
                    break;
            }

            $scope.$apply(function () {
                $scope.config.contextMenu = new DayPilot.Menu({items: menuItems});
            });
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

            var reservation = args.data.objReservation;

            // customize the reservation HTML: text, start and end dates
            args.data.html = args.data.text + " (" + start.toString("d/M/yyyy") + " - " + end.toString("d/M/yyyy") + ")"
                + "<br /><span style='color:gray'>" + status + "</span>";

            var groupId = reservation.groupId;
            if (groupId) {
                if (!$scope.events.groupColors[groupId])
                    $scope.events.groupColors[groupId] = getRandomColor();

                args.data.html += '<br/><span class="label label-default" style="background-color:'
                    + $scope.events.groupColors[groupId] + '">Group</span>';

                var isRoomEmpty = (!reservation.reservationGuests || reservation.reservationGuests.length == 0);
                if (!isRoomEmpty && reservation.reservationGuests[0].owner)
                    args.data.html += ' <span class="label label-warning">Owner</span>';
            }

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
            method: "GET",
            url: ("reservations/search?startDate=" + range.startDate + "&endDate=" + range.endDate),
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            }
        }).then(
            function (response) { //success
                $scope.events.list = [];

                angular.forEach(response.data, function (reservation, key) {
                    var tmpEvent = {
                        start: reservation.startDate,
                        end: reservation.endDate,
                        id: reservation.id,
                        resource: reservation.room.id,
                        status: reservation.status,
                        objReservation: reservation
                    };

                    var isRoomEmpty = (!reservation.reservationGuests || reservation.reservationGuests.length == 0);
                    // set event text
                    if (!isRoomEmpty)
                        tmpEvent.text = reservation.reservationGuests[0].guest.personalData.fullName;
                    else
                        tmpEvent.text = "Empty";

                    if (reservation.status > 0) {
                        tmpEvent.deleteEnabled = false;

                        if (reservation.status == 2) {
                            tmpEvent.moveEnabled = false;
                            tmpEvent.resizeEnabled = false;
                        }
                    }

                    $scope.events.list.push(tmpEvent);
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
        if ($scope.isNewGuest) {
            $scope.reservationForm.$submitted = true;
            if (!$scope.reservationForm.$valid)
                return;
        }

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
        //var room = $filter('filter')($scope.config.resources, {id: $scope.events.new.resource})[0];
        $scope.reservationGuest.owner = true;

        var objReservation = {
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
            room: {id: $scope.events.new.resource}
        };

        var url = "reservation?recepcionistUserId=" + $scope.loginData.id;
        url += ("&group=" + $scope.isGroupReservation);

        if ($scope.isGroupReservation && $scope.isExistingGroup && $scope.selectedGroupReservation) {
            objReservation.groupId = $scope.selectedGroupReservation.groupId;
            objReservation.startDate = $scope.selectedGroupReservation.startDate;
            objReservation.endDate = $scope.selectedGroupReservation.endDate;
            objReservation.allInclusive = $scope.selectedGroupReservation.allInclusive;
            objReservation.breakfast = $scope.selectedGroupReservation.breakfast;
            objReservation.dinner = $scope.selectedGroupReservation.dinner;
            objReservation.numberAdults = $scope.selectedGroupReservation.numberAdults + 1;

            url += "&groupId=" + objReservation.groupId;
            $scope.reservationGuest.owner = false;
        }

        $scope.saveData(url, objReservation, function (newReservation) {
            $scope.reservationGuest.reservation = newReservation;
            console.log("New reservation: ", $scope.reservationGuest.reservation);
            $scope.saveReservationGuest();
        }, $scope.resetReservation);

        $('#reservationModal').modal('hide');

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
                $scope.reservationGuest.reservation.numberAdults += $scope.reservationGuest.reservation.numberAdults;

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
            $scope.reservationGuest.reservation.numberAdults += $scope.reservationGuest.reservation.numberAdults;

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
            $scope.config.startDate = start.format('YYYY-MM-DD');
            $scope.config.days = tmp / 86400000;

            loadEvents();
        });

    }

    angular.element(document).ready(function () {
        $scope.changeRoomType();

        $('#dateRange').daterangepicker({
            parentEl: "#scheduleContainer",
            startDate: new Date($scope.startDate),
            endDate: new Date($scope.endDate),
            locale: {
                format: "DD/MM/YYYY",
                firstDay: 1
            }
        }, setDateRange);

        $('#identityIssueDate,#identityExpireDate').daterangepicker({
            parentEl: "#reservationModal",
            singleDatePicker: true,
            showDropdowns: true,
            locale: {
                format: "YYYY-MM-DD",
                firstDay: 1
            }
        });

        $('.calendar').css({float: 'left'});

        loadEvents();

        $interval(loadEvents, 5000);
    });
});