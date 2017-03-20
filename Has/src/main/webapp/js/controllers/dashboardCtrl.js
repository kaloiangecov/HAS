app.controller("dashboardCtrl", function ($scope, $filter, $http) {
    $scope.page.title = "Manager Dashboard";

    $scope.events = {
        list: [],
        new: {},
        resources: []
    };

    $scope.taskInfo = {};

    $scope.config = {
        scale: "Hour",
        startDate: moment().format('YYYY-MM-DD HH:mm:ss'),
        days: (1 / 3),
        resources: $scope.events.resources,
        timeHeaders: [{groupBy: "Day", format: "dd MMM yyyy"}, {groupBy: "Hour", format: 'HH'}],
        eventDeleteHandling: "Update",
        eventClickHandling: "Select",
        eventResizeHandling: "Disabled",
        allowEventOverlap: false,
        cellWidthSpec: "Auto",
        eventHeight: 50,
        rowHeaderColumns: [
            {title: "Employee", width: 100}
        ],
        contextMenu: new DayPilot.Menu({
            items: [
                {
                    text: '<i class="fa fa-info"></i> Show info',
                    onclick: function () {
                        var task = this.source.data.objTask;
                        $scope.$apply(function () {
                            $scope.taskInfo = task;

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
            args.resource.name = args.resource.personalData.fullName;
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
            $scope.dashboard.clearSelection();

            $scope.$apply(function () {
                var tmpEvent = {
                    start: args.start,
                    end: args.end,
                    id: new Date().getTime(),
                    text: "New Task",
                    resource: args.resource,
                    status: 0
                };

                $scope.events.list.push(tmpEvent);
            });
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

            //args.data.text = args.data.objTask.assigner;

            // customize the TASK HTML: text, start and end dates
            args.data.html = args.data.text + "<br /><span style='color:gray'>" + status + "</span>";

            // reservation tooltip that appears on hover - displays the status text
            args.e.toolTip = status;
        }
    };

    $scope.loadEmployees = function () {
        var today = moment().format('YYYY-MM-DD');
        var nowHour = moment().format('HH:mm');
        var shift = 0;

        if (nowHour >= '14:00' && nowHour < '22:00')
            shift = 1;
        else if (nowHour < '6:00')
            shift = 2;

        $http({
            method: "GET",
            url: ("employees/shift?date=" + today + "&shift=" + shift),
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
            .then(function (data) {
                $scope.config.resources = data;
            });
    };

    $scope.loadEvents = function () {
        $http({
            method: "GET",
            url: "tasks",
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
            .then(function (data) {
                $scope.events.list = [];
                angular.forEach(data, function (task, key) {

                    var estimatedTime = moment(task.timePlaced);
                    estimatedTime.add({hours: task.duration.substr(0, 2), minutes: task.duration.substr(3, 2)});

                    var tmpEvent = {
                        start: task.timePlaced,
                        end: estimatedTime.format('YYYY-MM-DD HH:mm:ss'),
                        id: task.id,
                        text: task.title,
                        resource: task.assignee.id,
                        status: task.status,
                        objTask: task
                    };

                    $scope.events.list.push(tmpEvent)
                });

            });
    };

    angular.element(document).ready(function () {
        $scope.loadEmployees();
        //$scope.loadEvents();
    });
});