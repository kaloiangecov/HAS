app.controller("dashboardCtrl", function ($scope, $filter, $http, $location, $state, $stateParams, $interval, DTOptionsBuilder, DTColumnBuilder) {
    $scope.page.title = "Tasks";
    $scope.assigner = {};
    $scope.employees = [];
    $scope.isEdit = false;
    $scope.isTimePickerValid = true;

    var SHIFT_MORNING = 0;
    var SHIFT_LUNCH = 1;
    var SHIFT_NIGHT = 2;

    $scope.taskStatuses = [
        "New",
        "In Progress",
        "Finished"
    ];
    $scope.taskStatusLabels = [
        "danger",
        "warning",
        "success"
    ];


    $scope.shiftHours = [
        { // SHIFT_MORNING
            start: '06:00',
            end: '14:00'
        },
        { // SHIFT_LUNCH
            start: '14:00',
            end: '22:00'
        },
        { // SHIFT_NIGHT
            start: '22:00',
            end: '06:00'
        }
    ];

    $scope.timeSelect = {
        hours: [],
        minutes: []
    };

    $scope.picker = {
        start: {
            hours: 0,
            minutes: 0
        },
        finish: {
            hours: 0,
            minutes: 0
        }
    };

    for (var i = 0; i < 24; i++)
        $scope.timeSelect.hours.push(i);
    for (var i = 0; i < 60; i++)
        $scope.timeSelect.minutes.push(i);

    $scope.refreshInterval = undefined;

    function getCurrentShift() {
        var nowHour = moment().format('HH:mm');
        var shift = SHIFT_NIGHT;

        if (nowHour >= $scope.shiftHours[SHIFT_MORNING].start && nowHour < $scope.shiftHours[SHIFT_MORNING].end)
            shift = SHIFT_MORNING;
        else if (nowHour >= $scope.shiftHours[SHIFT_LUNCH].start && nowHour < $scope.shiftHours[SHIFT_LUNCH].end)
            shift = SHIFT_LUNCH;

        return shift;
    }

    $scope.getEmployeeByUserId = function (userID, callback) {
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
            }).then(callback);
    };

    $scope.loadEmployees = function (callback) {
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
            .then(callback);
    };

    if ($location.path().includes("dashboard")) { // dashboard view
        $scope.loadTasks = function (callback) {
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
                .then(callback);
        };

        var today = moment().format('YYYY-MM-DD');

        $scope.dtInstance = {};

        $scope.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                url: 'tasks/current',
                type: 'GET',
                dataType: "json",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': $scope.authentication
                },
                data: $scope.searchFilters.tasks,
                error: function (jqXHR, textStatus, errorThrown) {
                    $scope.displayMessage({
                        status: jqXHR.status,
                        error: jqXHR.statusText,
                        message: jqXHR.responseText
                    });
                }
            })
            .withDataProp('data')
            .withOption('processing', true)
            .withOption('serverSide', true)
            .withOption('pagingType', 'full_numbers')
            .withOption('dom', 'lrtip');

        $scope.dtColumns = [
            DTColumnBuilder.newColumn('id', 'ID').notVisible(),
            DTColumnBuilder.newColumn('startTime', 'Start Time')
                .renderWith(function (time) {
                    if (time)
                        return today + ' ' + time.substr(0, 5);
                    else
                        return "N\\A";
                }),
            DTColumnBuilder.newColumn('finishTime', 'Finish Time')
                .renderWith(function (time) {
                    if (time)
                        return today + ' ' + time.substr(0, 5);
                    else
                        return "N\\A";
                }),
            DTColumnBuilder.newColumn('title', 'Title'),
            DTColumnBuilder.newColumn('assignee.personalData.fullName', 'Assignee'),
            DTColumnBuilder.newColumn('status', 'Status')
                .renderWith(function (status) {
                    var html = '<span class="label label-' + $scope.taskStatusLabels[status] +
                        '">' + $scope.taskStatuses[status] + '</span>';
                    return html;
                }),
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (id) {
                    var html =
                        '<div class="btn-group btn-group-sm">' +
                        '<a class="btn btn-default action-btn" href="#!/tasks/edit/' +
                        id + '"><i class="fa fa-pencil" aria-hidden="true"></i></a>' +
                        '<button class="btn btn-default action-btn delete-btn" id="del_' +
                        id + '"><i class="fa fa-calendar-times-o" aria-hidden="true"></i></button>' +
                        '</div>';
                    return html;
                })
        ];

        $scope.reloadTableData = function (resetPaging) {
            $scope.dtInstance.reloadData(function (list) {
                //console.log(list);
            }, resetPaging);
        };
    }
    else {
        $scope.task = {};
        $scope.master = {};

        $scope.getEmployeeByUserId($scope.loginData.id, function (employee) {
            $scope.assigner = employee;

            $scope.loadEmployees(function (employees) {
                $scope.employees = employees;

                if ($stateParams && $stateParams.id) { //edit (re-assign)
                    $scope.isEdit = true;

                    $scope.getSingleData("tasks", $stateParams.id, function (data) {
                        $scope.task = data;

                        $scope.picker = {
                            start: {
                                hours: parseInt($scope.task.startTime.substr(0, 2)),
                                minutes: parseInt($scope.task.startTime.substr(3, 2))
                            },
                            finish: {
                                hours: parseInt($scope.task.finishTime.substr(0, 2)),
                                minutes: parseInt($scope.task.finishTime.substr(3, 2))
                            }
                        };
                    });
                } else { //new assignment
                    $scope.isEdit = false;

                    $scope.task = {
                        priority: 1,
                        status: 0,
                        duration: '00:20',
                        assigner: ($scope.assigner.personalData.fullName + ' (' + $scope.assigner.user.username + ')'),
                        type: 1,
                        assignee: $scope.employees[0]
                    };

                    var now = moment();

                    $scope.picker.start.hours = parseInt(now.format('H'));
                    $scope.picker.start.minutes = parseInt(now.format('m'));

                    now.add(20, 'minutes');

                    $scope.picker.finish.hours = parseInt(now.format('H'));
                    $scope.picker.finish.minutes = parseInt(now.format('m'));

                }

            });
        });

        $scope.resetTask = function (task) {
            task = angular.copy($scope.master);
        };

        $scope.submitTask = function (task) {

            if (task.startTime >= task.finishTime) {
                $scope.isTimePickerValid = false;
                return;
            }

            if ($scope.taskForm.$valid) {

                $scope.master = angular.copy(task);

                if (!$scope.isEdit)
                    $scope.master.timePlaced = moment().format('YYYY-MM-DD HH:mm:ss');

                $scope.master.startTime = ("0" + $scope.picker.start.hours).slice(-2) + ':' + ("0" + $scope.picker.start.minutes).slice(-2);
                $scope.master.finishTime = ("0" + $scope.picker.finish.hours).slice(-2) + ':' + ("0" + $scope.picker.finish.minutes).slice(-2);

                if ($scope.master.startTime >= $scope.master.finishTime) {
                    $scope.page.message = {
                        type: 'danger',
                        title: 'Invalid time!',
                        text: "Start date can't be later than finish date!"
                    };
                    angular.element('#messageModal').modal('show');
                    return;
                }

                $scope.saveData("tasks", $scope.master, function (task) {
                    console.log(task);

                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!',
                        text: 'Task editted'
                    };
                    angular.element('#messageModal').modal('show');

                    $location.path('/tasks/dashboard');
                }, $scope.resetTask, $scope.isEdit);
            }
        };
    }


    angular.element(document).ready(function () {
        if ($location.path().includes("dashboard")) {
            if (!$scope.refreshInterval) {
                $scope.refreshInterval = $interval(function () {
                    $scope.reloadTableData(false);
                }, 5000);
            }

            $scope.$on("$destroy", function () {
                if ($scope.refreshInterval) {
                    $interval.cancel($scope.refreshInterval);
                    $scope.refreshInterval = undefined;
                }
            });
        } else {

        }

    });
});