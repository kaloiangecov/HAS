app.controller("userCtrl", function ($scope, $state, $stateParams, $interval, $resource, $http, DTOptionsBuilder, DTColumnBuilder) {
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

    if (window.location.hash.includes("list")) {
        $scope.getAllRoles(function (data) {
            $scope.rolesList = data;
            $scope.filters.roleID = $scope.rolesList[0].id;
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
            DTColumnBuilder.newColumn('lastLogin', 'Last Login')
                .renderWith(function (date) {
                    return new Date(date).toLocaleString();
                }),
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (id) {
                    var html =
                        '<div class="btn-group btn-group-sm">' +
                        '<a class="btn btn-default action-btn" href="#!/users/edit/' +
                        id + '"><i class="fa fa-pencil" aria-hidden="true"></i></a>' +
                        '<a class="btn btn-default action-btn delete-btn" id="ban_' +
                        id + '" href="javascript:;"><i class="fa fa-trash-o" aria-hidden="true"></i></a>' +
                        '</div>';
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
        $scope.getAllRoles(function (data) {
            $scope.rolesList = data;

            if ($stateParams && $stateParams.id) {
                $scope.isEdit = true;
                $scope.getUser($stateParams.id, function (data) {
                    $scope.user = data;
                    if (!$scope.user.picture)
                        $scope.user.picture = 'img/user.png';
                });
            }
            else {
                $scope.isEdit = false;
                $scope.user = {
                    userRole: $scope.rolesList[0],
                    picture: 'img/user.png'
                };
            }
        });

        $scope.submit = function (user) {
            if ($scope.userForm.$valid) {
                $scope.master = angular.copy(user);
                $scope.master.regDate = (new Date()).toISOString();
                //$scope.master.lastLogin = (new Date()).toISOString();

                var url = $scope.isEdit ? ("user/" + $stateParams.id) : "user";
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
                    function (response) { //success
                        if ($scope.isEdit) {
                            alert('Edited: ' + $scope.master.username);
                        } else {
                            alert('Created: ' + $scope.master.username);
                        }
                        window.location.hash = "#!/users/list";
                    },
                    function (response) { //error
                        $scope.displayMessage(response.data);
                    });
            }
        };
    }

    angular.element(document).ready(function () {

        function banUser(id) {
            $http({
                method: "DELETE",
                url: ("user/" + id),
                responseType: "json",
                headers: {
                    "Authorization": $scope.authentication
                }
            }).then(
                function (response) { //success
                    alert("User deleted");
                    window.location.hash = "#!/users/list";
                },
                function (response) { //error
                    $scope.displayMessage(response.data);
                });
        };

        if (window.location.hash.includes("list")) {
            $scope.reloadTableData();

            $('.delete-btn').click(function () {
                var id = this.id.split('_');
                banUser(id[1]);
            });
            //$interval($scope.reloadTableData, 30000);
        }
        else {
            $('#inputPicture').on('change', function () {
                var files = $(this).prop('files');
                if (files && files[0]) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        $scope.$apply(function () {
                            $scope.user.picture = e.target.result;
                        });
                    };

                    reader.readAsDataURL(files[0]);
                }
            });

            $scope.chooseProfilePicture = function () {
                $('#inputPicture').trigger('click');
            };
        }
    });

});