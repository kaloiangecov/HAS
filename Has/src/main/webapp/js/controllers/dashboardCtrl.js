app.controller("dashboardCtrl", function ($scope, $filter, $http) {
    var ctrl = this;
    $scope.page.title = "Manager Dashboard";

    $scope.events = {
        list: [],
        selected: [],
        new: {},
        resources: []
    };

    $scope.requestInfo = {};

    ctrl.selectedRoomType == 3

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

    $scope.startDate = new DayPilot.Date(new Date());

    $scope.config = {
        scale: "Hour",
        startDate: $scope.startDate,
        days: 1,
        resources: $scope.events.resources,
        timeHeaders: [{groupBy: "Day", format: "d MMM yyyy"}, {groupBy: "Hour", format: 'HH'}],
        eventDeleteHandling: "Update",
        eventClickHandling: "Select",
        eventResizeHandling: "Disabled",
        allowEventOverlap: true,
        cellWidthSpec: "Auto",
        eventHeight: 50,
        rowHeaderColumns: [
            {title: "Room", width: 46}
        ],
        contextMenu: new DayPilot.Menu({
            items: [
                {
                    text: '<i class="fa fa-info"></i> Show info',
                    onclick: function () {
                        var task = this.source.data.objRequest;
                        $scope.$apply(function () {
                            $scope.requestInfo = task;

                            $('#infoModal').modal('show');
                        });
                    }
                }
            ]
        }),
        onBeforeCellRender: function (args) {
            var today = new DayPilot.Date(new Date());
            if (args.cell.start <= today && today < args.cell.end) {
                args.cell.backColor = "#fff0b3";
            }
        },
        onBeforeResHeaderRender: function (args) {
            args.resource.name = args.resource.number;
        },
        onEventSelected: function (args) {
            $scope.$apply(function () {
                $scope.events.selected = $scope.dashboard.multiselect.events();
            });
        },
        onEventDeleted: function (args) {
            $scope.dashboard.clearSelection();

            if (confirm("Delete task " + args.e.text() + " ?")) {
                $scope.dashboard.events.remove(args.e);

            } else {
                loadEvents();
            }


        },
        onBeforeEventRender: function (args) {
            var status = "Placed";

            switch (args.e.status) {
                case 0:
                    args.data.barColor = "#e55";
                    status = "Placed";
                    break;
                case 1:
                    args.data.barColor = "#feda55";
                    status = "In Progress";
                    break;
                case 2:
                    args.data.barColor = "#1ABB9C";
                    status = "Finished";
                    break;
                default:
                    status = "Unexpected state";
                    break;
            }

            // customize the TASK HTML: text, start and end dates
            args.data.html = args.data.text + "<br /><span style='color:gray'>" + status + "</span>";

            // reservation tooltip that appears on hover - displays the status text
            args.e.toolTip = status;
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

    function loadEvents() {
        var range = {
            timePlaced: $scope.config.startDate,
            timeFinished: moment($scope.config.startDate).add($scope.config.days, 'days').format("YYYY-MM-DD HH-mm-ss")
        };

        $http({
            method: "GET",
            url: "requests",
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            },
            //data: range
        }).then(
            function (response) { //success
                $scope.events.list = [];

                angular.forEach(response.data, function (request, key) {
                    var tmpEvent = {
                        start: request.timePlaced,
                        end: request.timeFinished,
                        id: request.id,
                        resource: request.reservationGuest.room.id,
                        status: request.status,
                        text: request.type,
                        tooltip: request.reservationGuest.guest.personalData.fullName,
                        objRequest: request
                    };

                    $scope.events.list.push(tmpEvent);
                });
            },
            function (response) { //error
                $scope.displayMessage(response.data);
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

    function addDays(date, days) {
        var result = new Date(date);
        result.setDate(result.getDate() + days);
        return result;
    }

    angular.element(document).ready(function () {
        $scope.changeRoomType();

        loadEvents();

        //$scope.timer = setInterval(loadEvents, 10000);
        //$('#selectGuest').select2();
    });
});