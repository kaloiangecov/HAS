app.controller("guestCtrl", function ($scope, $state, $stateParams, $timeout, $interval, $resource, $http, DTOptionsBuilder, DTColumnBuilder) {
    $scope.page.title = "Guests";
    $scope.master = {};
    $scope.filters = {};
    $scope.users = [];
    $scope.isEdit = false;

    if ($stateParams && $stateParams.id) {
        $scope.isEdit = true;
        var url = "sample_data/sampleGuests.json"; //TODO put real service URL here

        $http({
            method: "GET",
            url: url,
            data: {
                id: $stateParams.id
            },
            responseType: "json"
        }).then(
            function (response) { //success
                $scope.guest = response.data;
                console.log($scope.user);
            },
            function (response) { //error
                alert(response.statusText);
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


    $http({
        method: "GET",
        url: "sample_data/sampleUsers.json", //TODO put real service URL here
        responseType: "json"
    }).then(
        function (response) { //success
            $scope.users = response.data.data;
        },
        function (response) { //error
            alert(response.statusText);
        });

    $scope.search = function () {
        alert(JSON.stringify($scope.filters));
        $scope.reloadTableData();
    };

    $scope.submit = function (guest) {
        if ($scope.guestForm.$valid) {
            $scope.master = angular.copy(guest);
            var url = $scope.isEdit ?
                "sample_data/sampleGuests.json" : // TODO put real service URL here
                "sample_data/sampleGuests.json"; // TODO put real service URL here

            $http({
                method: "POST",
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
                    alert(response.statusText);
                });
            $state.go('loggedin.root.guests.list')
        }
    };

    // guests table
    $scope.dtInstance = {};

    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: 'sample_data/sampleGuests.json', //TODO put real service URL here
            type: 'GET',
            data: {
                fullName: $scope.filters.fullName
            }
        })
        .withDataProp('data')
        .withOption('processing', true)
        .withOption('serverSide', true)
        .withOption('pagingType', 'full_numbers')
        .withOption('dom', 'lrtip');

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('id', 'ID'),
        DTColumnBuilder.newColumn('numberReservations', 'Number of Reservations'),
        DTColumnBuilder.newColumn('personalData.phone', 'Phone Number'),
        DTColumnBuilder.newColumn('personalData.fullName', 'Full Name'),
        DTColumnBuilder.newColumn('userID', 'User')
            .renderWith(function (userID) {
                return $scope.users[userID].username;
            }),
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
        $('#identityIssueDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true
            },
            function (start) {
                $scope.guest.personalData.identityIssueDate = start.toISOString();
            });
        $('#identityExpireDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true
            },
            function (start) {
                $scope.guest.personalData.identityExpireDate = start.toISOString();
            });

        $interval($scope.reloadTableData, 5000);
    });

});