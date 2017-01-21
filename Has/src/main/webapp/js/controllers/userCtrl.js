app.controller("userCtrl", function ($scope, $state, $stateParams, $timeout, $interval, $resource, $http, DTOptionsBuilder, DTColumnBuilder) {
    $scope.page.title = "Users";
    $scope.master = {};
    $scope.filters = {
        role: 1
    };
    $scope.isEdit = false;

    if ($stateParams && $stateParams.id) {
        $scope.isEdit = true;
        $scope.getUser($stateParams.id, function(data) {
            $scope.user = data;
        });
    }
    else {
        $scope.isEdit = false;
        $scope.user = {
            role: 1,
            picture: 'img/user.png'
        };
    }

    $scope.submit = function (user) {
        if ($scope.userForm.$valid) {
            $scope.master = angular.copy(user);
            $scope.master.regDate = (new Date()).toISOString();
            $scope.master.lastLogin = (new Date()).toISOString();
            $scope.master.userRole = {
                id: user.role,
                roleName: sampleRoles[user.role]
            }

            var url = $scope.isEdit ? ("user/" + $stateParams.id) : "user";
            var method = $scope.isEdit ? "PUT" : "POST";

            $http({
                method: method,
                url: url,
                data: $scope.master,
                responseType: "json"
            }).then(
                function (response) { //success
                    if ($scope.isEdit) {
                        alert('Edited: ' + $scope.master.username);
                    } else {
                        alert('Created: ' + $scope.master.username);
                    }
                },
                function (response) { //error
                    alert(response.data.message);
                });
            $state.go('loggedin.root.users.list')
        }
    };

    $scope.banUser = function (index) {
        var url = "user" + $stateParams.id;

        $http({
            method: "DELETE",
            url: url,
            data: {
                id: $stateParams.id
            },
            responseType: "json"
        }).then(
            function (response) { //success
                alert("User banned");
            },
            function (response) { //error
                alert(response.data.message);
            });

        reloadData();
    };

    // users table
    $scope.dtInstance = {};
    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: 'searchusers',
            type: 'GET',
            dataType: "json",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': ('Basic ' + window.base64encode($scope.loginData.username + ':' + $scope.loginData.password))
            },
            data: {
                username: $scope.filters.username,
                email: $scope.filters.email,
                role: $scope.filters.role
            }
        })
        .withDataProp('data')
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
            .renderWith(function (data) {
                var html = '<a class="action-btn" href="#/users/edit/' +
                    data +
                    '"><i class="fa fa-pencil" aria-hidden="true"></i></a><a class="action-btn" id="ban_' +
                    data +
                    '" href="javascript:;" ng-click="banUser(' +
                    data +
                    ', this)"><i class="fa fa-ban" aria-hidden="true"></i></a>';
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
        //$('.roles').select2();

        //$interval($scope.reloadTableData, 5000);

        $('#inputPicture').on('change', function () {
            var files = $(this).prop('files');
            if (files && files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $timeout(function () {
                        $scope.user.picture = e.target.result;
                    }, 1);
                };

                reader.readAsDataURL(files[0]);
            }
        });

        $scope.chooseProfilePicture = function () {
            $('#inputPicture').trigger('click');
        };

    });

});