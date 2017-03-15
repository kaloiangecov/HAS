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

    $scope.getPersonal = function (callback) {
        $http({
            method: "GET",
            url: "employees",
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
        startTIme: 4,
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
            {title: "Room", width: 52}
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
            args.resource.name = args.resource.id;
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
        onTimeRangeSelected: function (args) {
            $scope.scheduler.clearSelection();
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
            },
            function (response) { //error
                $scope.displayMessage(response.data);
            });
    }

    $scope.changeRoomType = function () {
        $scope.getPersonal(function (data) {
            $scope.events.resources = data;
            $scope.config.resourves = $scope.events.resources;
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