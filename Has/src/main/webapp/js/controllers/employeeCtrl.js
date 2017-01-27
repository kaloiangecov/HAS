app.controller("employeeCtrl", function ($scope, $state, $stateParams, $timeout, $interval, $resource, $http, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Employees";
    $scope.master = {};
    ctrl.filters = {
        fullName: "",
        phone: "",
        dateHired: ""
    };
    $scope.usersList = [];
    $scope.isEdit = false;

    if (window.location.hash.includes("list")) {
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
                data: ctrl.filters,
                error: function (jqXHR, textStatus, errorThrown) {
                    if (jqXHR.status == 401) {
                        $scope.resetAuthorization("Unauthorized access!");
                    } else {
                        $scope.resetAuthorization(errorThrown + '\n' + textStatus);
                    }
                }
            })
            .withDataProp('data')
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
                    var dateItems = date.split("/");
                    return new Date(dateItems[2], (dateItems[1] - 1), dateItems[0]).toLocaleDateString();
                }),
            DTColumnBuilder.newColumn('user.username', 'User'),
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (data) {
                    var html = '<a class="action-btn" href="#/employees/edit/' +
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
    }
    else {
        function saveEmployee(callback) {
            var url = $scope.isEdit ? ("employee/" + $stateParams.id) : "employee";
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
                    $scope.resetAuthorization(response.data.message);
                });
        }

        $scope.getAllUsers(function (data) {
            $scope.usersList = data;

            if ($stateParams && $stateParams.id) {
                $scope.isEdit = true;

                var url = "employee/" + $stateParams.id;

                $http({
                    method: "GET",
                    url: url,
                    responseType: "json",
                    headers: {
                        "Authorization": $scope.authentication
                    }
                }).then(
                    function (response) { //success
                        $scope.employee = response.data;
                        $scope.employee.userID = $scope.employee.user.id;
                    },
                    function (response) { //error
                        $scope.resetAuthorization(response.data.message);
                    });
            }
            else {
                $scope.isEdit = false;
                $scope.employee = {
                    personalData: {},
                    userID: $scope.usersList[0].id
                };
            }
        });

        $scope.submit = function (employee) {
            if ($scope.employeeForm.$valid) {
                $scope.master = angular.copy(employee);

                delete $scope.master.userID;

                $scope.getUser(employee.userID, function (data) {
                    $scope.master.user = data;

                    saveEmployee(function () {
                        if ($scope.isEdit) {
                            alert('Edited: ' + $scope.master.personalData.fullName);
                        } else {
                            alert('Created: ' + $scope.master.personalData.fullName);
                        }
                        window.location.hash = "#/employees/list";
                    });
                });
            }
        };
    }

    angular.element(document).ready(function () {

        if (window.location.hash.includes("list")) {
            $('#filterDateHired').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true,
                    autoUpdateInput: false
                },
                function (start) {
                    ctrl.filters.dateHired = start.format("DD/MM/YYYY");
                    $('#filterDateHired').val(ctrl.filters.dateHired);
                });

            $scope.reloadTableData();
            //$interval($scope.reloadTableData, 30000);
        }
        else {
            $('#dateHired').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true,
                    autoUpdateInput: false
                },
                function (start) {
                    $scope.employee.dateHired = start.format("DD/MM/YYYY");
                    $('#dateHired').val($scope.employee.dateHired);
                });
            $('#identityIssueDate').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true,
                    autoUpdateInput: false
                },
                function (start) {
                    $scope.employee.personalData.identityIssueDate = start.format("DD/MM/YYYY");
                    $('#identityIssueDate').val($scope.employee.personalData.identityIssueDate);
                });
            $('#identityExpireDate').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true,
                    autoUpdateInput: false
                },
                function (start) {
                    $scope.employee.personalData.identityExpireDate = start.format("DD/MM/YYYY");
                    $('#identityExpireDate').val($scope.employee.personalData.identityExpireDate);
                });
        }
    });

});