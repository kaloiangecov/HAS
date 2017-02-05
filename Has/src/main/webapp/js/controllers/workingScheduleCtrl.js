app.controller("workingScheduleCtrl", function ($scope, $http, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Employees";
    $scope.rolesList = [];
    ctrl.filters = {
        roleID: 1,
        startDate: new Date().toISOString().substr(0, 10),
        endDate: new Date().toISOString().substr(0, 10)
    };

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
        $scope.filters.roleID = $scope.rolesList[0].id;
    });

    angular.element(document).ready(function () {
        $scope.reloadTableData();
    });
});