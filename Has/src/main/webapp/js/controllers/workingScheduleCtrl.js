app.controller("workingScheduleCtrl", function ($scope, $http, $location, $stateParams, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Working Schedule";
    $scope.rolesList = [];
    $scope.master = {};
    ctrl.employees = [];
    $scope.isEdit = false;


    if ($location.path().includes("list")) {
        $scope.isEdit = false;

        // schedule table
        $scope.dtInstance = {};
        $scope.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                url: 'schedules/search',
                type: 'GET',
                dataType: "json",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': $scope.authentication
                },
                data: $scope.searchFilters.schedules,
                error: function (jqXHR, textStatus, errorThrown) {
                    $scope.displayMessage({
                        status: jqXHR.status,
                        error: jqXHR.statusText,
                        message: jqXHR.responseText
                    });
                }
            })
            .withDataProp('data')
            .withOption('responsive', true)
            .withOption('processing', true)
            .withOption('serverSide', true)
            .withOption('pagingType', 'full_numbers')
            .withOption('dom', 'lrtip');

        $scope.dtColumns = [
            DTColumnBuilder.newColumn('employee.personalData.fullName', 'Employee'),
            DTColumnBuilder.newColumn('employee.user.userRole.roleName', 'Post'),
            DTColumnBuilder.newColumn('date', 'Date')
                .renderWith(function (date) {
                    return new Date(date).toLocaleDateString();
                }),
            DTColumnBuilder.newColumn('shift', 'Shift')
                .renderWith(function (data) {
                    return $scope.shifts[data]
                }),
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (data) {
                    var html = '<a class="action-btn" href="#!/schedule/edit/' +
                        data +
                        '"><i class="fa fa-pencil" aria-hidden="true"></i></a>';
                    return html;
                })
        ];
        $scope.reloadTableData = function () {
            var resetPaging = false;
            $scope.dtInstance.reloadData(function (list) {
                console.log(list);
            }, resetPaging);
        };

        $scope.getAllRoles(function (data) {
            $scope.rolesList = data;
            $scope.searchFilters.schedules.roleID = $scope.rolesList[0].id;
        });
    } else {
        $scope.getAllEmployees = function (updateCallback) {
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
                }).then(updateCallback);
        };

        $scope.getAllEmployees(function (data) {
            ctrl.employees = data;

            if ($stateParams && $stateParams.id) {
                $scope.isEdit = true;
                $scope.getSingleData("schedule", $stateParams.id, function (data) {
                    $scope.schedule = data;
                });
            }
            else {
                $scope.isEdit = false;
                $scope.schedule = {
                    date: new Date().toISOString().substr(0, 10),
                    employee: ctrl.employees[0],
                    shift: 0
                };
            }
        });

        $scope.submit = function (schedule) {
            if ($scope.scheduleForm.$valid) {
                $scope.master = angular.copy(schedule);

                $scope.saveData("schedule", $scope.master, function () {
                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!'
                    };

                    if ($scope.isEdit)
                        $scope.page.message.text = ('Edited.');
                    else
                        $scope.page.message.text = ('Created.');

                    angular.element('#messageModal').modal('show');
                    $location.path("/schedule/list");
                }, undefined, $scope.isEdit);
            }
        };

    }

    angular.element(document).ready(function () {
        if ($location.path().includes("list")) {
            angular.element('#filterDateRange').daterangepicker({
                parentEl: "#scheduleContainer",
                startDate: new Date($scope.searchFilters.schedules.startDate),
                endDate: new Date($scope.searchFilters.schedules.endDate),
                locale: {
                    format: "DD/MM/YYYY"
                }
            }, function (start, end) {
                $scope.$apply(function () {
                    $scope.searchFilters.schedules.startDate = start.format("YYYY-MM-DD");
                    $scope.searchFilters.schedules.endDate = end.format("YYYY-MM-DD");
                });
            });

            angular.element('.calendar').css({float: 'left'});

            $scope.reloadTableData();
        } else {
            /*
             $scope.$watch("schedule.date", function (newVal, oldVal) {
             if ($scope.schedule.shift == 3) {
             $scope.schedule.date = moment(newVal).add(1, 'days').format('YYYY-MM-DD');
             } else {
             $scope.schedule.endDate = newVal;
             }
             })
             */

            angular.element('#date').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true,
                locale: {
                    format: "YYYY-MM-DD",
                    firstDay: 1
                }
            });
        }
    });
});