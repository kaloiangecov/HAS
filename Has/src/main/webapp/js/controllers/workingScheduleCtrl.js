app.controller("workingScheduleCtrl", function ($scope, $http, $stateParams, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Working Schedule";
    $scope.rolesList = [];
    ctrl.filters = {
        roleID: 1,
        startDate: new Date().toISOString().substr(0, 10),
        endDate: new Date().toISOString().substr(0, 10)
    };
    $scope.master = {};
    ctrl.employees = [];
    $scope.isEdit = false;


    if (window.location.hash.includes("list")) {
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
                data: ctrl.filters,
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
            DTColumnBuilder.newColumn('startDate', 'Start Date')
                .renderWith(function (date) {
                    return new Date(date).toLocaleString();
                }),
            DTColumnBuilder.newColumn('endDate', 'End Date')
                .renderWith(function (date) {
                    return new Date(date).toLocaleString();
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
            ctrl.filters.roleID = $scope.rolesList[0].id;
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

        function saveSchedule(callback) {
            var url = $scope.isEdit ? ("schedule/" + $stateParams.id) : "schedule";
            var method = $scope.isEdit ? "PUT" : "POST";

            $http({
                method: method,
                url: url,
                data: $scope.master,
                responseType: "json",
                headers: {
                    "Authorization": $scope.authentication
                }
            }).then(
                callback,
                function (response) { //error
                    $scope.displayMessage(response.data);
                });
        }

        $scope.getAllEmployees(function (data) {
            ctrl.employees = data;

            if ($stateParams && $stateParams.id) {
                $scope.isEdit = true;

                var url = "schedule/" + $stateParams.id;

                $http({
                    method: "GET",
                    url: url,
                    responseType: "json",
                    headers: {
                        "Authorization": $scope.authentication
                    }
                }).then(
                    function (response) { //success
                        $scope.schedule = response.data;
                    },
                    function (response) { //error
                        $scope.displayMessage(response.data);
                    });
            }
            else {
                $scope.isEdit = false;
                $scope.schedule = {
                    startDate: new Date().toISOString().substr(0, 10),
                    endDate: new Date().toISOString().substr(0, 10),
                    employee: ctrl.employees[0],
                    shift: 0
                };
            }
        });

        $scope.submit = function (schedule) {
            if ($scope.scheduleForm.$valid) {
                $scope.master = angular.copy(schedule);

                saveSchedule(function () {
                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!'
                    };

                    if ($scope.isEdit) {
                        $scope.page.message.text = ('Edited.');
                    } else {
                        $scope.page.message.text = ('Created.');
                    }

                    $('#messageModal').modal('show');
                    window.location.hash = "#!/schedule/list";
                });
            }
        };

    }

    angular.element(document).ready(function () {
        if (window.location.hash.includes("list")) {
            $('#filterStartDate,#filterEndDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true,
                locale: {
                    format: "YYYY-MM-DD"
                }
            });

            $scope.reloadTableData();
        } else {
            $scope.$watch("schedule.startDate", function (newVal, oldVal) {
                if ($scope.schedule.shift == 3) {
                    $scope.schedule.endDate = moment(newVal).add(1, 'days').format('YYYY-MM-DD');
                } else {
                    $scope.schedule.endDate = newVal;
                }
            })

            $('#startDate,#endDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true,
                locale: {
                    format: "YYYY-MM-DD"
                }
            });
        }
    });
});