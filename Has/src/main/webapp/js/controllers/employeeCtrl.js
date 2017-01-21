app.controller("employeeCtrl", function ($scope, $state, $stateParams, $timeout, $interval, $resource, $http, DTOptionsBuilder, DTColumnBuilder) {
    $scope.page.title = "Employees";
    $scope.master = {};
    $scope.filters = {};
    $scope.users = [];
    $scope.isEdit = false;

    function saveEmployee() {
        var url = $scope.isEdit ? ("employee/" + $stateParams.id) : "employee";
        var method = $scope.isEdit ? "PUT" : "POST";

        $http({
            method: method,
            url: url,
            data: $scope.master,
            responseType: "json"
        }).then(
            function (response) { //success
                if ($scope.isEdit) {
                    alert('Edited: ' + $scope.master.personalData.fullName);
                } else {
                    alert('Created: ' + $scope.master.personalData.fullName);
                }
            },
            function (response) { //error
                alert(response.data.message);
            });
    }

    if ($stateParams && $stateParams.id) {
        $scope.isEdit = true;
        var url = "employee/" + $stateParams.id;

        $http({
            method: "GET",
            url: url,
            responseType: "json"
        }).then(
            function (response) { //success
                $scope.employee = response.data;
            },
            function (response) { //error
                alert(response.data.message);
            });
    }
    else {
        $scope.isEdit = false;
        $scope.employee = {
            personalData: {}
        };
    }

    $scope.submit = function (employee) {
        if ($scope.employeeForm.$valid) {
            $scope.master = angular.copy(employee);

            delete $scope.master.userID;

            if (!$scope.isEdit)
                $scope.getUser(employee.userID, function(data) {
                    $scope.master.user = data;
                });
            if ($scope.master.user)
                saveEmployee();

            $state.go('loggedin.root.employees.list')
        }
    };

    // employees table
    $scope.dtInstance = {};

    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: 'searchemployees',
            type: 'GET',
            dataType: "json",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': ('Basic ' + window.base64encode($scope.loginData.username + ':' + $scope.loginData.password))
            },
            data: {
                fullName: $scope.filters.fullName,
                dateHired: $scope.filters.dateHired
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
                return new Date(date).toLocaleDateString();
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

    angular.element(document).ready(function () {
        $scope.getAllUsers(function(data) {
            $scope.uesrs = data;
        });

        $('#dateHired').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true
            },
            function (start) {
                $scope.employee.dateHired = start.toISOString();
            });
        $('#identityIssueDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true
            },
            function (start) {
                $scope.employee.personalData.identityIssueDate = start.toISOString();
            });
        $('#identityExpireDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true
            },
            function (start) {
                $scope.employee.personalData.identityExpireDate = start.toISOString();
            });

        //$interval($scope.reloadTableData, 5000);
    });

});