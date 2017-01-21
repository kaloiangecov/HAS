app.controller("roomCtrl", function ($scope, $http, $state, $stateParams, $resource, $timeout, $interval, DTOptionsBuilder, DTColumnBuilder) {
    $scope.page.title = "Rooms";
    $scope.roomsTable;
    $scope.master = {};
    $scope.isEdit = false;

    function saveRoom() {
        var url = $scope.isEdit ? ("room/" + $stateParams.id) : "room";
        var method = $scope.isEdit ? "PUT" : "POST";

        $http({
            method: method,
            url: url,
            data: $scope.master,
            responseType: "json"
        }).then(
            function (response) { //success
                if ($scope.isEdit) {
                    alert('Edited: ' + $scope.master.number);
                } else {
                    alert('Created: ' + $scope.master.number);
                }
            },
            function (response) { //error
                alert(response.data.message);
            });
    }

    if ($stateParams && $stateParams.id) {
        $scope.isEdit = true;
        var url = "room/" + $stateParams.id;

        $http({
            method: "GET",
            url: url,
            responseType: "json"
        }).then(
            function (response) { //success
                $scope.room = response.data;
            },
            function (response) { //error
                alert(response.data.message);
            });
    }
    else {
        $scope.isEdit = false;
        $scope.room = {
            number: 101,
            type: 0,
            bedsSingle: 1,
            bedsDouble: 1,
            status: 0,
            children: true,
            pets: false,
            minibar: false
        };
    }

    $scope.submit = function (room) {
        if ($scope.roomForm.$valid) {
            $scope.master = angular.copy(room);
            saveRoom();
            $state.go('loggedin.root.users.list')
        }
    };

    $scope.reset = function () {
        $scope.room = angular.copy($scope.master);
    };


    // rooms table
    $scope.dtInstance = {};

    $scope.dtOptions = DTOptionsBuilder.newOptions()
        .withOption('ajax', {
            url: 'searchrooms',
            type: 'GET',
            dataType: "json",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': ('Basic ' + window.base64encode($scope.loginData.username + ':' + $scope.loginData.password))
            },
        })
        .withDataProp('data')
        .withOption('processing', true)
        .withOption('serverSide', true)
        .withOption('pagingType', 'full_numbers')
        .withOption('dom', 'lrtip');

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('number', 'Number'),
        DTColumnBuilder.newColumn('roomClass', 'Type')
            .renderWith(function (typeIndex) {
                return $scope.roomTypes[typeIndex];
            }),
        //DTColumnBuilder.newColumn('status', 'Status'),
        DTColumnBuilder.newColumn('bedsSingle', 'Single Beds'),
        DTColumnBuilder.newColumn('bedsDouble', 'Double Beds'),
        DTColumnBuilder.newColumn('number').notSortable().withClass('actions-column')
            .renderWith(function (roomNumber) {
                var html = '<a class="action-btn" href="#/rooms/edit/' +
                    roomNumber +
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
        //$interval($scope.reloadTableData, 5000);
    });

});