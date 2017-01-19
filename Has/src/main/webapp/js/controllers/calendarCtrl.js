app.controller("calendarCtrl", function ($scope, $timeout, $filter, $http) {
    $scope.page.title = "Calendar";

    $scope.roomTypes = sampleRoomTypes;
    $scope.events = [];
    $scope.resources = sampleResources;
    $scope.selectedEvents = [];
    $scope.newEvent;
    $scope.reservation;
    $scope.timer;

    $scope.startDate = new Date();
    $scope.startDate.setDate($scope.startDate.getDate() - 3)

    $scope.config = {
        //scale: "Day",
        //startDate: $scope.startDate,
        //days: 14,
        scale: "Manual",
        timeline: getTimeline(),
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
                    text: "Show event ID", onclick: function () {
                    alert("Event value: " + this.source.value());
                }
                },
                {
                    text: "Show event text", onclick: function () {
                    alert("Event text: " + this.source.text());
                }
                },
                {
                    text: "Show event range", onclick: function () {
                    alert("Event start: " + this.source.start().toStringSortable().substr(0, 10) + "\nEvent end: " + this.source.end().toStringSortable().substr(0, 10));
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
        resources: $scope.resources,
        onBeforeCellRender: function (args) {
            if (args.cell.start <= DayPilot.Date.today() && DayPilot.Date.today() < args.cell.end) {
                args.cell.backColor = "#fff0b3";
            }
        },
        onBeforeResHeaderRender: function (args) {
            var beds = function (count) {
                return count + " bed" + (count > 1 ? "s" : "");
            };

            args.resource.columns[0].html = beds(args.resource.capacity);
            args.resource.columns[1].html = args.resource.status;
            switch (args.resource.status) {
                case "Dirty":
                    args.resource.cssClass = "status_dirty";
                    break;
                case "Cleanup":
                    args.resource.cssClass = "status_cleanup";
                    break;
            }
        },
        onEventSelected: function (args) {
            $scope.$apply(function () {
                $scope.selectedEvents = $scope.scheduler.multiselect.events();
            });
        },
        onEventMoved: function (args) {
            $scope.scheduler.message("Reservation moved: " + args.e.text());
        },
        onEventResized: function (args) {
            $scope.scheduler.message("Reservation period changed: " + args.e.text());
        },
        onEventDeleted: function (args) {
            delete $scope.newEvent;
        },
        onTimeRangeSelected: function (args) {
            $scope.scheduler.clearSelection();

            //clearInterval($scope.timer);
            //loadEvents();

            if (!$scope.newEvent) {
                $timeout(function () {
                    $scope.newEvent = {
                        start: args.start,
                        end: args.end,
                        resource: args.resource,
                    };
                    $scope.reservation = {
                        country: "BG"
                    };
                    $('#reservationModal').modal('show');
                }, 1);
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
                case "New":
                    var checkinDeadline = today.addHours(21);
                    if ((start < today) || (start === today && now > checkinDeadline)) { // must checkoin before 21:00
                        args.data.barColor = "#e55";  // red
                        status = "Late Arrival";
                    } else {
                        args.data.barColor = "#feda55";  // orange
                        status = "New"
                    }
                    break;
                case 'Arrived': // arrived
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
                case 'Leaving': // checked out
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
            var filteredRes = $filter('filter')($scope.resources, {type: args.filter});
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

    function loadResources() {
        $scope.config.resources = $scope.resources;
    }

    function loadEvents() {
        $http({
            method: "GET",
            url: "sample_data/sampleEvents.json"
        })
            .then(
                function (response) { //success
                    $scope.events = response.data.events;
                    if ($scope.newEvent)
                        $scope.events.push($scope.newEvent);
                },
                function (response) { //error
                    alert(response.statusText);
                });
    }

    $scope.changeRoomType = function () {
        $timeout(function () {
            if ($scope.selectedRoom == "All") {
                $scope.config.resources = $scope.resources;
            } else {
                var filtered = $filter('filter')($scope.resources, {type: $scope.selectedRoom.toLowerCase()});
                $scope.config.resources = filtered;
            }
        }, 1);
    };

    $scope.addReservation = function () {
        $timeout(function () {
            if ($scope.reservationForm.$valid) {
                $scope.newEvent.id = "new_res";
                $scope.newEvent.text = $scope.reservation.name;
                $scope.newEvent.bubbleHtml = "Reservation details";
                $scope.newEvent.status = "New";

                $scope.events.push($scope.newEvent);
                $scope.scheduler.message("New event created!");
                console.log($scope.events);

                //$scope.timer = setInterval(loadEvents, 10000);

                $('#reservationModal').modal('hide');
            }
        }, 1);
    };

    $scope.resetReservation = function () {
        $timeout(function () {
            delete $scope.newEvent;
            $scope.reservation = {
                country: "BG"
            };
        }, 1);
    };

    $scope.scrollTo = function (date) {
        $scope.scheduler.scrollTo(date);
    };

    $scope.scale = function (val) {
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
        $timeout(function () {
            var tmp = end._d.getTime() - start._d.getTime();
            $scope.config.timeline = getTimeline(start._d, tmp / 86400000);
        }, 1);
    }

    angular.element(document).ready(function () {
        loadEvents();
        //$scope.timer = setInterval(loadEvents, 10000);

        $('#dateRange').daterangepicker({
            parentEl: "#scheduleContainer",
            startDate: $scope.startDate,
            endDate: addDays($scope.startDate, 21),
            locale: {
                format: "DD/MM/YY"
            }
        }, setDateRange);

        $('.calendar').css({ float: 'left' });

        //$('#country').select2();
    });
});