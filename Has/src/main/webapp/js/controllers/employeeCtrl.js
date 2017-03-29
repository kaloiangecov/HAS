app.controller("employeeCtrl", function ($scope, $location, $state, $stateParams, $timeout, $interval, $resource, $http, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Employees";
    $scope.master = {};
    $scope.usersList = [];
    $scope.isEdit = false;

    $scope.changeEmployed = function (id, callback) {
        $http({
            method: "PUT",
            url: ('employee/employed/' + id),
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

    if ($location.path().includes("list")) {
        // employees table
        $scope.dtInstance = {};

        $scope.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                url: 'employees/search',
                type: 'GET',
                dataType: "json",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': $scope.authentication
                },
                data: $scope.searchFilters.employees,
                success: $scope.addDeleteFunctions,
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
            DTColumnBuilder.newColumn('id', 'ID'),
            DTColumnBuilder.newColumn('personalData.fullName', 'Full Name'),
            DTColumnBuilder.newColumn('personalData.phone', 'Phone Number'),
            DTColumnBuilder.newColumn('dateHired', 'Date Hired')
                .renderWith(function (date) {
                    return new Date(date).toLocaleDateString();
                }),
            DTColumnBuilder.newColumn('user.username', 'User'),
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (id, type, full) {
                    var html =
                        '<div class="btn-group btn-group-sm">' +
                        '<a class="btn btn-default action-btn" href="#!/employees/edit/' +
                        id + '"><i class="fa fa-pencil" aria-hidden="true"></i></a>';

                    if (full.user.id != $scope.loginData.id) {
                        html += '<button type="button" class="btn btn-default action-btn delete-btn" id="del_' +
                            id + '">';

                        if (full.employed)
                            html += '<i class="fa fa-ban" aria-hidden="true"></i></button>';
                        else
                            html += '<i class="fa fa-refresh" aria-hidden="true"></i></button>';
                    }

                    html += '</div>';

                    return html;
                })
        ];

        $scope.addDeleteFunctions = function () {
            $timeout(function () {
                var btns = $('table').find('td').find('button');
                $(btns).off('click');
                $(btns).on('click', function () {
                    var id = this.id.split('_')[1];
                    $scope.changeEmployed(id, function () {
                        $scope.reloadTableData(false);
                        $scope.addDeleteFunctions();
                    });
                });
            }, 300);
        };

        $scope.$watch("searchFilters.employees", $scope.addDeleteFunctions);
        $scope.$watch("searchFilters.employees.fullName", $scope.addDeleteFunctions);
        $scope.$watch("searchFilters.employees.phone", $scope.addDeleteFunctions);
        $scope.$watch("searchFilters.employees.dateHired", $scope.addDeleteFunctions);
        $scope.$watch("searchFilters.employees.showDisabled", $scope.addDeleteFunctions);

        $scope.reloadTableData = function (resetPaging) {
            $scope.dtInstance.reloadData(function (list) {
                //console.log(list);
            }, resetPaging);
        };

        $scope.search = function () {
            $scope.reloadTableData(false);
            $scope.addDeleteFunctions();
        };
    }
    else {
        if ($stateParams && $stateParams.id) {
            $scope.isEdit = true;

            $scope.getSingleData("employee", $stateParams.id, function (data) {
                $scope.employee = data;

                var userID = -1;
                if ($scope.employee.user)
                    userID = $scope.employee.user.id;

                $scope.getFreeUsers(userID, "employees", function (data) {
                    $scope.usersList = data;
                });
            });
        }
        else {
            $scope.isEdit = false;
            $scope.employee = {
                personalData: {}
            };
            $scope.getFreeUsers(-1, "employees", function (data) {
                $scope.usersList = data;

                if (data.length > 0)
                    $scope.employee.user = data[0];
            });
        }

        $scope.submit = function (employee) {
            if ($scope.employeeForm.$valid) {
                $scope.master = angular.copy(employee);

                $scope.saveData("employee", $scope.master, function () {
                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!'
                    };

                    if ($scope.isEdit)
                        $scope.page.message.text = ('Edited: ' + $scope.master.personalData.fullName);
                    else
                        $scope.page.message.text = ('Created: ' + $scope.master.personalData.fullName);

                    $('#messageModal').modal('show');
                    $location.path('/employees/list');
                }, undefined, $scope.isEdit);
            }
        };
    }

    angular.element(document).ready(function () {

        if ($location.path().includes("list")) {
            var showDisabledSwitch = new Switchery(document.getElementById('showDisabled'), {color: "#26B99A"});

            $scope.addDeleteFunctions();

            $('#filterDateHired').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true,
                    autoUpdateInput: false
                },
                function (start) {
                    $scope.searchFilters.employees.dateHired = start.format("YYYY-MM-DD");
                    $scope.$apply();
                    $('#filterDateHired').val($scope.searchFilters.employees.dateHired);
                });

            $scope.reloadTableData(false);
            //$interval($scope.reloadTableData, 30000);
        }
        else {
            $('#dateHired,#identityIssueDate,#identityExpireDate').daterangepicker({
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