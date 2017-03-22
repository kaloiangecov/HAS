app.controller("dashboardCtrl", function ($scope, $filter, $http) {
    $scope.page.title = "Manager Dashboard";

    $scope.events = {
        list: [],
        new: {},
        resources: []
    };

    $scope.shiftHours = {
        morning: {
            start: '06:00',
            end: '14:00'
        },
        lunch: {
            start: '14:00',
            end: '22:00'
        },
        night: {
            start: '22:00',
            end: '06:00'
        }
    };

    var SHIFT_MORNING = 0;
    var SHIFT_LUNCH = 1;
    var SHIFT_NIGHT = 2;

    $scope.task = {};
    $scope.taskInfo = {};

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

    $scope.config = {
        scale: "Hour",
        startDate: moment().format('YYYY-MM-DD HH:mm:ss'),
        days: 1,
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
            var today = new Date().toISOString();
            if (args.cell.start.value <= today && today < args.cell.end.value) {
                args.cell.backColor = "#fff0b3";
            }

            var nowHour = moment().format('HH:mm');
            var cellStartHour = moment(args.cell.start.value).format('HH:mm');
            var cellEndHour = moment(args.cell.end.value).format('HH:mm');


            if (nowHour >= '06:00' && nowHour < '14:00') { //morning
                if (cellEndHour < '06:00' || cellStartHour >= '14:00')
                    args.cell.backColor = "#cc6655";
            } else if (nowHour >= '14:00' && nowHour < '22:00') { //lunch
                if (cellEndHour < '14:00' || cellStartHour >= '22:00')
                    args.cell.backColor = "#cc6655";
            } else if (nowHour < '06:00') { //night
                if ((cellEndHour < '22:00' && cellEndHour >= '14') || (cellStartHour >= '06:00' && cellStartHour < '22:00'))
                    args.cell.backColor = "#cc6655";
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

            if (!$scope.events.new.start && (args.start.value.substr(0, 10) >= moment().format('YYYY-MM-DD'))) {
                $scope.$apply(function () {
                    $scope.events.new = {
                        start: args.start,
                        end: args.end,
                        id: new Date().getTime(),
                        text: "New Task",
                        resource: args.resource,
                        status: 0
                    };

                    $('#taskModal').modal('show');

                    //$scope.events.list.push(tmpEvent);
                });
            }
        },
        onBeforeEventRender: function (args) {
            var status = "Placed";

            switch (args.e.status) {
                case 0:
                    args.data.barColor = "#60f";
                    status = "Placed";
                    break;
                case 1:
                    args.data.barColor = "#a6f";
                    status = "In Progress";
                    break;
                case 2:
                    args.data.barColor = "#caf";
                    status = "Finished";
                    break;
                default:
                    status = "Unexpected state";
                    break;
            }

            args.data.text = args.data.objTask.assignee.personalData.fullName;

            // customize the TASK HTML: text, start and end dates
            args.data.html = args.data.text + "<br /><span style='color:gray'>" + args.data.objTask.title + "</span>";

            // reservation tooltip that appears on hover - displays the status text
            args.e.toolTip = status;
        }
    };

    function getCurrentShift() {
        var nowHour = moment().format('HH:mm');
        var shift = SHIFT_MORNING;

        if (nowHour >= $scope.shiftHours.lunch.start && nowHour < $scope.shiftHours.lunch.end)
            shift = SHIFT_LUNCH;
        else if (nowHour >= $scope.shiftHours.night.start || nowHour < $scope.shiftHours.night.end)
            shift = SHIFT_NIGHT;

        return shift;
    }

    $scope.loadEmployees = function () {
        var today = moment().format('YYYY-MM-DD');

        $http({
            method: "GET",
            url: ("employees/service/shift?date=" + today + "&shift=" + getCurrentShift()),
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
            url: ("tasks/current/" + moment().format('YYYY-MM-DDTHH:mm:ss')),
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

                    var estimatedTime = moment(task.targetTime);
                    estimatedTime.add({hours: task.duration.substr(0, 2), minutes: task.duration.substr(3, 2)});

                    var tmpEvent = {
                        start: task.targetTime,
                        end: estimatedTime.format('YYYY-MM-DDTHH:mm:ss'),
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

    $scope.resetTask = function () {
        $scope.events.new = {};
        $scope.task = {};

        $scope.loadEvents();
    };

    $scope.submitTask = function () {
        $scope.getEmployeeByUserId($scope.loginData.id, function (employee) {
            var assignee = $filter('filter')($scope.config.resources, {id: $scope.events.new.resource})[0];

            var duration = moment($scope.events.new.end.ticks - $scope.events.new.start.ticks)
                .format('HH:mm');

            $scope.task.status = 0;
            $scope.task.timePlaced = moment().format('YYYY-MM-DDTHH:mm:ss');
            $scope.task.targetTime = $scope.events.new.start.value;
            $scope.task.duration = duration;
            $scope.task.assignee = assignee;
            $scope.task.assigner = employee.personalData.fullName;

            $scope.saveData("tasks", $scope.task, function (task) {
                console.log(task);
                $scope.resetTask();
                $('#taskModal').modal('hide');
            }, $scope.resetTask);
        });
    };

    angular.element(document).ready(function () {
        $scope.loadEmployees();
        $scope.loadEvents();
    });
});