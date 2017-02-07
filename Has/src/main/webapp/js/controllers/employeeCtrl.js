app.controller("employeeCtrl", function ($scope, $state, $stateParams, $interval, $resource, $http, DTOptionsBuilder, DTColumnBuilder) {
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
                    var html = '<a class="action-btn" href="#!/employees/edit/' +
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
                    $scope.displayMessage(response.data);
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
                    },
                    function (response) { //error
                        $scope.displayMessage(response.data);
                    });
            }
            else {
                $scope.isEdit = false;
                $scope.employee = {
                    personalData: {},
                    user: $scope.usersList[0]
                };
            }
        });

        $scope.submit = function (employee) {
            if ($scope.employeeForm.$valid) {
                $scope.master = angular.copy(employee);

                saveEmployee(function () {
                    if ($scope.isEdit) {
                        alert('Edited: ' + $scope.master.personalData.fullName);
                    } else {
                        alert('Created: ' + $scope.master.personalData.fullName);
                    }
                    window.location.hash = "#!/employees/list";
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
                    ctrl.filters.dateHired = start.format("YYYY-MM-DD");
                    $('#filterDateHired').val(ctrl.filters.dateHired);
                });

            $scope.reloadTableData();
            //$interval($scope.reloadTableData, 30000);
        }
        else {
            $('#dateHired,#identityIssueDate,#identityExpireDate').daterangepicker({
                    singleDatePicker: true,
                    showDropdowns: true,
                locale: {
                    format: "YYYY-MM-DD"
                }
                });
        }
    });

});