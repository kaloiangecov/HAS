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
                .renderWith(function (id) {
                    var html =
                        '<div class="btn-group btn-group-sm">' +
                        '<a class="btn btn-default action-btn" href="#!/employees/edit/' +
                        id + '"><i class="fa fa-pencil" aria-hidden="true"></i></a>' +
                        '<button type="button" class="btn btn-default action-btn delete-btn" id="del_' +
                        id + '"><i class="fa fa-trash-o" aria-hidden="true"></i></button>' +
                        '</div>';
                    return html;
                })
        ];

        $scope.addDeleteFunctions = function () {
            $timeout(function () {
                var btns = $('table').find('td').find('button');
                $(btns).off('click');
                $(btns).on('click', function () {
                    var id = this.id.split('_')[1];
                    $scope.deleteData('employee', id, function () {
                        $scope.page.message = {
                            type: 'success',
                            title: 'Deleted!',
                            text: ('Employee with id ' + id + ' was successfully deleted!')
                        };
                        $('#messageModal').modal('show');
                    });
                });
            }, 300);
        };

        $scope.$watch("ctrl.filters.fullName", $scope.addDeleteFunctions);
        $scope.$watch("ctrl.filters.phone", $scope.addDeleteFunctions);
        $scope.$watch("ctrl.filters.dateHired", $scope.addDeleteFunctions);

        $scope.reloadTableData = function () {
            var resetPaging = false;
            $scope.dtInstance.reloadData(function (list) {
                //console.log(list);
                $scope.addDeleteFunctions();
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
                    $scope.getFreeUsers($scope.employee.user.id, function (data) {
                        $scope.usersList = data;
                    });
                },
                function (response) { //error
                    $scope.displayMessage(response.data);
                });
        }
        else {
            $scope.isEdit = false;
            $scope.employee = {
                personalData: {}
            };
            $scope.getFreeUsers(-1, function (data) {
                $scope.usersList = data;
            });
        }

        $scope.submit = function (employee) {
            if ($scope.employeeForm.$valid) {
                $scope.master = angular.copy(employee);

                saveEmployee(function () {
                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!'
                    };

                    if ($scope.isEdit) {
                        $scope.page.message.text = ('Edited: ' + $scope.master.personalData.fullName);
                    } else {
                        $scope.page.message.text = ('Created: ' + $scope.master.personalData.fullName);
                    }

                    $('#messageModal').modal('show');
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