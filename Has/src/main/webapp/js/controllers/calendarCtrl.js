app.controller("calendarCtrl", function ($scope, $filter, $http) {
    var ctrl = this;
    $scope.page.title = "Calendar";

    $scope.roomTypes = sampleRoomTypes;
    $scope.roomTypes[3] = "All";

    $scope.events = {
        list: [],
        selected: [],
        new: {},
        resources: []
    };

    ctrl.selectedRoomType = 3;
    $scope.guests = {
        list: [],
        selectedGuest: {}
    };
    $scope.isNewGuest = true;
    $scope.isAdditionalGuest = false;
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
    $scope.timer;

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

    $scope.getAllGuests = function (updateCallback) {
        $http({
            method: "GET",
            url: "guests",
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

    $scope.saveReservationGuest = function (callback) {
        $http({
            method: "POST",
            url: "reservation-guest",
            data: $scope.reservationGuest,
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

    $scope.saveReservation = function (reservation, callback) {
        $http({
            method: "POST",
            url: "reservation",
            data: reservation,
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

    $scope.startDate = new Date();
    $scope.startDate.setDate($scope.startDate.getDate() - 3);

    $scope.config = {
        //scale: "Day",
        //startDate: $scope.startDate,
        //days: 14,
        scale: "Manual",
        timeline: getTimeline(),
        resources: $scope.events.resources,
        timeHeaders: [{groupBy: "Month"}, {groupBy: "Day", format: "d"}],
        eventDeleteHandling: "Update",
        eventClickHandling: "Select",
        allowEventOverlap: false,
        cellWidthSpec: "Auto",
        eventHeight: 50,
        rowHeaderColumns: [
            {title: "Room"},
            {title: "Capacity", width: 80},
            {title: "Status", width: 80}
        ],
        contextMenu: new DayPilot.Menu({
            items: [
                {
                    text: '<i class="fa fa-info"></i> Show event info',
                    onclick: function () {
                        var dateRange = "\nEvent start: " +
                            this.source.start().toStringSortable().substr(0, 10) +
                            "\nEvent end: " +
                            this.source.end().toStringSortable().substr(0, 10);
                        alert("ID: " + this.source.value() + "\nGuest: " + this.source.text() + dateRange);
                    }
                },
                {
                    text: '<i class="fa fa-plus"></i> Add another guest',
                    onclick: function () {
                        $scope.reservationGuest = this.source.data.objReservation.reservationGuests[0];
                        var tmpReservation = this.source.data.objReservation;
                        delete tmpReservation.reservationGuests;

                        $scope.reservationGuest.guest = {};
                        $scope.reservationGuest.reservation = tmpReservation;
                        $scope.reservationGuest.id = null;
                        $scope.reservationGuest.owner = false;

                        $scope.isAdditionalGuest = true;

                        $('#reservationModal').modal('show');
                    }
                },
                {
                    text: "Delete", onclick: function () {
                    if (confirm("Delete reservation?\n" + "Start: " + this.source.start() + "\nEnd:" + this.source.end())) {
                        if (this.source.id() == "new_res")
                            $scope.resetReservation();
                        $scope.scheduler.events.remove(this.source);
                        $scope.scheduler.message("Reservation removed: " + this.source.text());
                    }
                }
                },
                {
                    text: "Disabled menu item", onclick: function () {
                    alert("disabled")
                }, disabled: true
                }
            ]
        }),
        onBeforeCellRender: function (args) {
            if (args.cell.start <= DayPilot.Date.today() && DayPilot.Date.today() < args.cell.end) {
                args.cell.backColor = "#fff0b3";
            }
        },
        onBeforeResHeaderRender: function (args) {
            var beds = function (single, double) {
                return (single + " single, " + double + " double");
            };

            args.resource.name = args.resource.number;

            args.resource.columns[0].html = beds(args.resource.bedsSingle, args.resource.bedsDouble);
            args.resource.columns[1].html = $scope.roomStatuses[args.resource.status];
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
            $scope.scheduler.message("Reservation moved: " + args.e.text());
        },
        onEventResized: function (args) {
            $scope.scheduler.message("Reservation period changed: " + args.e.text());
        },
        onEventDeleted: function (args) {
            $scope.events.new = {};
        },
        onTimeRangeSelected: function (args) {
            $scope.scheduler.clearSelection();
            //clearInterval($scope.timer);
            //loadEvents();

            if (!$scope.events.new.start) {
                $scope.$apply(function () {
                    $scope.events.new = {
                        start: args.start,
                        end: args.end,
                        resource: args.resource
                    };
                });
                $('#reservationModal').modal('show');
            }
        },
        onEventClick: function (args) {

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
                    if ((start < today) || (start === today && now > checkinDeadline)) { // must checkoin before 21:00
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

            // add a bar highlighting how much has been paid already (using an "active area")
            /*
             var paid = args.e.paid;
             var paidColor = "#aaa";
             args.data.areas = [
             {
             bottom: 10,
             right: 4,
             html: "<div style='color:" + paidColor + "; font-size: 8pt;'>Paid: " + paid + "%</div>",
             v: "Visible"
             },
             {
             left: 4,
             bottom: 8,
             right: 4,
             height: 2,
             html: "<div style='background-color:" + paidColor + "; height: 100%; width:" + paid + "%'></div>"
             }
             ];
             */
        },
        onEventFilter: function (args) {
            var filteredRes = $filter('filter')($scope.events.resources, {type: args.filter});
            var found = false;

            angular.forEach(filteredRes, function (value, key) {
                var tmp = args.e.resource();
                if (value.id == tmp) {
                    found = true;
                    return;
                }
            });
            args.visible = found;
        }
    };

    function getTimeline(date, days) {
        var initDate = date || DayPilot.Date.today();
        var start = new DayPilot.Date(initDate);
        var period = days || 14;

        var timeline = [];

        var checkin = 12;
        var checkout = 12;

        for (var i = 0; i < period; i++) {
            var day = start.addDays(i);
            timeline.push({start: day.addHours(checkin), end: day.addDays(1).addHours(checkout)});
        }

        return timeline;
    }

    function loadEvents() {
        $http({
            method: "GET",
            url: "reservations",
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            }
        }).then(
            function (response) { //success
                $scope.events.list = [];

                angular.forEach(response.data, function (reservation, key) {
                    var arrStartDate = reservation.startDate.split('/');
                    var arrEndDate = reservation.endDate.split('/');

                    var tmpEvent = {
                        start: new Date(arrStartDate[2], parseInt(arrStartDate[1]) - 1, parseInt(arrStartDate[0]) + 1),
                        end: new Date(arrEndDate[2], parseInt(arrEndDate[1]) - 1, arrEndDate[0]),
                        id: reservation.id,
                        resource: (reservation.reservationGuests[0].room.id - 1),
                        status: reservation.status,
                        text: reservation.reservationGuests[0].guest.personalData.fullName,
                        objReservation: reservation
                    };

                    $scope.events.list.push(tmpEvent);
                });

                if ($scope.events.new.start)
                    $scope.events.list.push($scope.events.new);
            },
            function (response) { //error
                alert(response.statusText);
            });
    }

    $scope.changeRoomType = function () {
        $scope.getRooms(function (data) {
            $scope.events.resources = data;

            if (ctrl.selectedRoomType == 3) {
                $scope.config.resources = $scope.events.resources;
            } else {
                var filtered = $filter('filter')($scope.events.resources, {roomClass: ctrl.selectedRoomType});
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
        if ($scope.isAdditionalGuest)
            $scope.resetGuest();
        else
            $scope.resetReservation();
    };

    $scope.addReservation = function () {

        $scope.getEmployeeByUserId($scope.loginData.id, function (data) {
            $scope.reservationGuest.room = $scope.events.resources[$scope.events.new.resource];
            $scope.reservationGuest.owner = true;

            var arrStartDate = $scope.events.new.start.value.substr(0, 10).split('-');
            var arrEndDate = $scope.events.new.end.value.substr(0, 10).split('-');

            var objReservation = {
                startDate: (arrStartDate[2] + '/' + arrStartDate[1] + '/' + arrStartDate[0]),
                endDate: (arrEndDate[2] + '/' + arrEndDate[1] + '/' + arrEndDate[0]),
                price: 40.0,
                discount: 0,
                numberAdults: 1,
                numberChildren: 0,
                status: 0,
                receptionist: data
            };

            $scope.events.new.bubbleHtml = "Reservation details";
            $scope.events.new.status = 0;

            $scope.saveReservation(objReservation, function (newReservation) {
                $scope.reservationGuest.reservation = newReservation;

                if ($scope.isNewGuest) { // create new guest
                    $scope.reservationGuest.guest.numberReservations = 0;
                    $scope.reservationGuest.guest.status = 0;

                    $http({
                        method: "POST",
                        url: "guest",
                        responseType: "json",
                        headers: {
                            "Authorization": $scope.authentication
                        },
                        data: $scope.reservationGuest.guest
                    }).then(
                        function (response) { //success
                            return response.data;
                        },
                        function (response) { //error
                            $scope.displayMessage(response.data);
                        })
                        .then(function (data) {
                            $scope.reservationGuest.guest = data;

                            $scope.events.new.text = $scope.reservationGuest.guest.personalData.fullName;

                            $scope.saveReservationGuest(function (data) {
                                $scope.events.new.id = data.reservation.id;
                                $scope.events.list.push($scope.events.new);
                                $scope.scheduler.message("New event created!");
                                $scope.resetReservation();
                                loadEvents();
                            });
                        });

                } else { // use existing guest
                    $scope.reservationGuest.guest = $scope.guests.selectedGuest;
                    $scope.reservationGuest.guest.numberReservations += 1;

                    $scope.events.new.text = $scope.reservationGuest.guest.personalData.fullName;

                    $scope.saveReservationGuest(function (data) {
                        $scope.events.new.id = data.reservation.id;
                        $scope.events.list.push($scope.events.new);
                        $scope.scheduler.message("New event created!");
                        $scope.resetReservation();
                        loadEvents();
                    });
                }
            });

            //$scope.timer = setInterval(loadEvents, 10000);
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
    };

    $scope.addGuest = function () {
        if ($scope.isNewGuest) {
            $scope.reservationGuest.guest.numberReservations = 0;
            $scope.reservationGuest.guest.status = 0;

            $http({
                method: "POST",
                url: "guest",
                responseType: "json",
                headers: {
                    "Authorization": $scope.authentication
                },
                data: $scope.reservationGuest.guest
            }).then(function (response) { //success
                return response.data;
            }, function (response) { //error
                $scope.displayMessage(response.data)
            })
                .then(function (data) {
                    $scope.reservationGuest.guest = data;
                    $scope.reservationGuest.reservation.lastModifiedBy = $scope.loginData;
                    $scope.reservationGuest.reservation.lastModifiedTime = new Date().toISOString();

                    $scope.saveReservationGuest(function (data) {
                        $scope.scheduler.message("Added new guest to reservation!");
                        $scope.resetGuest();
                        loadEvents();
                    });
                });
        } else {
            $scope.reservationGuest.guest = $scope.guests.selectedGuest;
            $scope.reservationGuest.reservation.lastModifiedBy = $scope.loginData;
            $scope.reservationGuest.reservation.lastModifiedTime = new Date().toISOString();

            $scope.saveReservationGuest(function (data) {
                $scope.scheduler.message("Added new guest to reservation!");
                $scope.resetGuest();
                loadEvents();
            });
        }

        $('#reservationModal').modal('hide');
        console.log($scope.reservationGuest);
    };
    $scope.resetGuest = function () {
        $scope.reservationGuest = {
            reservation: {},
            guest: {},
            room: {}
        };
        $scope.isAdditionalGuest = false;
    }

    $scope.scrollTo = function (date) {
        $scope.scheduler.scrollTo(date);
    };

    $scope.setScale = function (val) {
        $scope.config.scale = val;
        if (val == "Day") {
            $scope.config.days = 14;
        }
    };

    function addDays(date, days) {
        var result = new Date(date);
        result.setDate(result.getDate() + days);
        return result;
    }

    function setDateRange(start, end, label) {
        $scope.$apply(function () {
            var tmp = end._d.getTime() - start._d.getTime();
            $scope.config.timeline = getTimeline(start._d, tmp / 86400000);
        });
    }

    angular.element(document).ready(function () {
        $scope.changeRoomType();

        loadEvents();

        //$scope.timer = setInterval(loadEvents, 10000);

        $scope.getAllGuests(function (data) {
            $scope.guests.list = data;
        });

        $('#dateRange').daterangepicker({
            parentEl: "#scheduleContainer",
            startDate: $scope.startDate,
            endDate: addDays($scope.startDate, 21),
            locale: {
                format: "DD/MM/YY"
            }
        }, setDateRange);

        $('.calendar').css({float: 'left'});

        //$('#country').select2();
    });
});