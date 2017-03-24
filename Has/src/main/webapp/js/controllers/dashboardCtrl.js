app.controller("dashboardCtrl", function ($scope, $filter, $http, $location, $state, $stateParams, $interval, DTOptionsBuilder, DTColumnBuilder) {
    $scope.page.title = "Tasks";
    $scope.assigner = {};

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

    if ($location.path().includes("dashboard")) {
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

        var shiftHour = moment().format('YYYY-MM-DD');
        shiftHour += ('T' + $scope.shiftHours[getCurrentShift()].start + ':00');

        $scope.dtInstance = {};

        $scope.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                url: ('tasks/current/' + shiftHour),
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
            DTColumnBuilder.newColumn('targetTime', 'Target Time')
                .renderWith(function (date) {
                    return new Date(date).toLocaleString();
                }),
            DTColumnBuilder.newColumn('dueTime', 'Due TIme')
                .renderWith(function (date) {
                    return new Date(date).toLocaleString();
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
                    $scope.employees = data;
                });
        };

        if ($stateParams && $stateParams.id) {
            $scope.getSingleData("tasks", $stateParams.id, function (data) {
                $scope.task = data;

                $scope.loadEmployees();
            });
        }

        $scope.resetTask = function (task) {
            task = angular.copy($scope.master);
        };

        $scope.submitTask = function (task) {
            $scope.master = angular.copy(task);

            $scope.saveData("tasks", $scope.task, function (task) {
                console.log(task);

                $scope.page.message = {
                    type: 'success',
                    title: 'Success!',
                    text: 'Task editted'
                };
                $('#messageModal').modal('show');

                $location.path('/tasks/dashboard');
            }, $scope.resetTask, true);
        };
    }


    angular.element(document).ready(function () {
        $scope.getEmployeeByUserId($scope.loginData.id, function (employee) {
            $scope.assigner = employee;
        });

        if ($location.path().includes("dashboard")) {
            $interval(function () {
                $scope.reloadTableData(false);
            }, 5000);
        } else {
            $('#targetTime').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true,
                timePicker: true,
                startDate: $scope.task.targetTime,
                minDate: $scope.task.targetTime,
                timePicker24Hour: true,
                locale: {
                    format: 'DD/MM/YYYY HH:mm',
                    firstDay: 1
                }
            }, function (start) {
                $scope.$apply(function () {
                    $scope.task.targetTime = start.format("YYYY-MM-DDTHH:mm:ss");
                    $('#targetTime').val($scope.task.targetTime);
                });
            });

            $('#dueTime').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true,
                timePicker: true,
                startDate: $scope.task.dueTime,
                minDate: $scope.task.dueTime,
                timePicker24Hour: true,
                locale: {
                    format: 'DD/MM/YYYY HH:mm',
                    firstDay: 1
                }
            }, function (start) {
                $scope.$apply(function () {
                    $scope.task.dueTime = start.format("YYYY-MM-DDTHH:mm:ss");
                    $('#targetTime').val($scope.task.dueTime);
                });
            });
        }

    });
});