app.controller("guestCtrl", function ($scope, $state, $stateParams, $timeout, $interval, $resource, $http, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Guests";
    $scope.master = {};
    ctrl.filters = {
        fullName: ""
    };
    ctrl.users = [];
    $scope.isEdit = false;

    function saveGuest(callback) {
        var url = $scope.isEdit ? ("guest/" + $stateParams.id) : "guest";
        var method = $scope.isEdit ? "PUT" : "POST";

        $http({
            method: method,
            url: url,
            data: $scope.master,
            responseType: "json"
        }).then(
            callback,
            function (response) { //error
                alert(response.data.message);
            });
    }

    if ($stateParams && $stateParams.id) {
        $scope.isEdit = true;
        var url = "guest/" + $stateParams.id;

        $http({
            method: "GET",
            url: url,
            responseType: "json"
        }).then(
            function (response) { //success
                $scope.guest = response.data;
            },
            function (response) { //error
                alert(response.data.message);
            });
    }
    else {
        $scope.isEdit = false;
        $scope.guest = {
            numberReservations: 0,
            status: 0,
            personalData: {}
        };
    }

    $scope.submit = function (guest) {
        if ($scope.guestForm.$valid) {
            $scope.master = angular.copy(guest);

            delete $scope.master.userID;

            if (!$scope.isEdit)
                $scope.getUser(guest.userID, function (data) {
                    $scope.master.user = data;
                });
            if ($scope.master.user)
                saveGuest(function () {
                    if ($scope.isEdit) {
                        alert('Edited: ' + $scope.master.personalData.fullName);
                    } else {
                        alert('Created: ' + $scope.master.personalData.fullName);
                    }
                    $state.go('loggedin.root.guests.list');
                    $scope.reloadTableData();
                });


        }
    };

    // guests table
    $scope.dtInstance = {};

    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: 'searchguests',
            type: 'GET',
            dataType: "json",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': ('Basic ' + window.base64encode($scope.loginData.username + ':' + $scope.loginData.password))
            },
            data: ctrl.filters
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
        DTColumnBuilder.newColumn('numberReservations', 'Number of Reservations'),
        DTColumnBuilder.newColumn('user.username', 'User'),
        DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
            .renderWith(function (data) {
                var html = '<a class="action-btn" href="#/guests/edit/' +
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
        $scope.getAllUsers(function (data) {
            ctrl.uesrs = data;
        });


        $('#identityIssueDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true
            },
            function (start) {
                $scope.guest.personalData.identityIssueDate = start.format("DD/MM/YYYY");
            });
        $('#identityExpireDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true
            },
            function (start) {
                $scope.guest.personalData.identityExpireDate = start.format("DD/MM/YYYY");
            });

        //$interval($scope.reloadTableData, 5000);
    });

});