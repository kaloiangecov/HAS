app.controller("roomCtrl", function ($scope, $http, $state, $stateParams, $resource, $timeout, $interval, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Rooms";
    $scope.roomsTable;
    $scope.master = {};
    ctrl.filters = {
        number: 0,
        type: 0
    };
    $scope.isEdit = false;

    if (window.location.hash.includes("list")) {
        // rooms table
        $scope.dtInstance = {};

        $scope.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                url: 'rooms/search',
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
                    } else if (jqXHR.status == 403) {
                        $scope.resetAuthorization("You don't have permissions to view this page!");
                    } else {
                        $scope.resetAuthorization(textStatus);
                    }
                }
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
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (id) {
                    var html = '<a class="action-btn" href="#/rooms/edit/' +
                        id +
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
        function saveRoom() {
            var url = $scope.isEdit ? ("room/" + $stateParams.id) : "room";
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
                        alert('Edited: ' + $scope.master.number);
                    } else {
                        alert('Created: ' + $scope.master.number);
                    }
                    window.location.hash = "#/rooms/list";
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
                responseType: "json",
                headers: {
                    "Authorization": $scope.authentication
                }
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
            }
        };

        $scope.reset = function () {
            $scope.room = angular.copy($scope.master);
        };
    }

    angular.element(document).ready(function () {
        if (window.location.hash.includes("list")) {
            $scope.reloadTableData();
            //$interval($scope.reloadTableData, 30000);
        }
        else {

        }
    });

});