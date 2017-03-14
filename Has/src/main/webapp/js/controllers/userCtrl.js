app.controller("userCtrl", function ($scope, $http, $location, $state, $stateParams, $timeout, $interval, $resource, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Users";
    $scope.master = {};
    $scope.rolesList = [];
    ctrl.filters = {
        username: "",
        email: "",
        roleID: 1
    };
    $scope.isEdit = false;

    $scope.changeUserStatus = function (userId, callback) {
        $http({
            method: "PUT",
            url: ('user/enabled/' + userId),
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
        $scope.getAllRoles(function (data) {
            var emptyArray = [
                {
                    id: -1,
                    roleName: "-- ALL --"
                }
            ];

            $scope.rolesList = emptyArray.concat(data);
            $scope.rolesList.pop();

            ctrl.filters.roleID = $scope.rolesList[0].id;
        });

        // users table
        $scope.dtInstance = {};
        $scope.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                url: 'users/search',
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
            DTColumnBuilder.newColumn('username', 'Username'),
            DTColumnBuilder.newColumn('email', 'E-Mail'),
            DTColumnBuilder.newColumn('userRole.roleName', 'Role'),
            DTColumnBuilder.newColumn('regDate', 'Registration Date')
                .renderWith(function (date) {
                    return new Date(date).toLocaleDateString();
                }),
            DTColumnBuilder.newColumn('lastLogin', 'Last Login')
                .renderWith(function (date) {
                    return new Date(date).toLocaleString();
                }),
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (id, type, full) {
                    var html =
                        '<div class="btn-group btn-group-sm">' +
                        '<a class="btn btn-default action-btn" href="#!/users/edit/' +
                        id + '"><i class="fa fa-pencil" aria-hidden="true"></i></a>';

                    if (full.id != $scope.loginData.id) {
                        html += '<button type="button" class="btn btn-default action-btn delete-btn" id="del_' +
                            id + '">';

                        if (full.enabled)
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
                    var userId = this.id.split('_')[1];
                    $scope.changeUserStatus(userId, function () {
                        $scope.reloadTableData(false);
                        $scope.addDeleteFunctions();
                    });
                });
            }, 300);
        };

        $scope.$watch("ctrl.filters.username", $scope.addDeleteFunctions);
        $scope.$watch("ctrl.filters.email", $scope.addDeleteFunctions);
        $scope.$watch("ctrl.filters.roleID", $scope.addDeleteFunctions);

        $scope.reloadTableData = function (resetPaging) {
            $scope.dtInstance.reloadData(function (list) {
                //console.log(list);
            }, resetPaging);
        };
    }
    else {
        $scope.getAllRoles(function (data) {
            $scope.rolesList = data;

            if ($stateParams && $stateParams.id) {
                $scope.isEdit = true;
                $scope.getSingleData("user", $stateParams.id, function (data) {
                    $scope.user = data;
                });
            }
            else {
                $scope.isEdit = false;
                $scope.user = {
                    userRole: $scope.rolesList[0]
                };
            }
        });

        $scope.submit = function (user) {
            if ($scope.userForm.$valid) {
                $scope.master = angular.copy(user);
                $scope.master.regDate = (new Date()).toISOString();
                //$scope.master.lastLogin = (new Date()).toISOString();

                $scope.saveData("user", $scope.master, function () {
                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!'
                    };

                    if ($scope.isEdit) {
                        $scope.page.message.text = ('Edited: ' + $scope.master.username);
                    } else {
                        $scope.page.message.text = ('Created: ' + $scope.master.username);
                    }

                    $('#messageModal').modal('show');
                    $location.path("/users/list");
                }, undefined, $scope.isEdit);
            }
        };
    }

    angular.element(document).ready(function () {
        if ($location.path().includes("list")) {
            $scope.addDeleteFunctions();
            $scope.reloadTableData(false);
            //$interval($scope.reloadTableData, 30000);
        }
        else {

        }
    });

});