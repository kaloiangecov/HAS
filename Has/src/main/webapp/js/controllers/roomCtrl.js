app.controller("roomCtrl", function ($scope, $http, $location, $state, $stateParams, $resource, $timeout, $interval, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Rooms";
    $scope.master = {};
    $scope.isEdit = false;

    if ($location.path().includes("list")) {
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
                data: $scope.searchFilters.rooms,
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
            DTColumnBuilder.newColumn('number', 'Number'),
            DTColumnBuilder.newColumn('roomClass', 'Type')
                .renderWith(function (typeIndex) {
                    return $scope.roomTypes[typeIndex];
                }),
            //DTColumnBuilder.newColumn('status', 'Status'),
            DTColumnBuilder.newColumn('bedsSingle', 'Single Beds'),
            DTColumnBuilder.newColumn('bedsDouble', 'Double Beds'),
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (id, type, full) {
                    var html =
                        '<div class="btn-group btn-group-sm">' +
                        '<a class="btn btn-default action-btn" href="#!/rooms/edit/' +
                        id + '"><i class="fa fa-pencil" aria-hidden="true"></i></a>';

                    html += '<button type="button" class="btn btn-default action-btn delete-btn" id="del_' +
                        id + '">';

                    if (full.status < 3)
                        html += '<i class="fa fa-ban" aria-hidden="true"></i></button>';
                    else
                        html += '<i class="fa fa-refresh" aria-hidden="true"></i></button>';

                    html += '</div>';
                    return html;
                })
        ];

        $scope.addDeleteFunctions = function () {
            $timeout(function () {
                var btns = $('table').find('td').find('button');
                $(btns).off('click');
                $(btns).on('click', function () {

                    var roomId = this.id.split('_')[1];

                    $scope.getSingleData("room", roomId, function (room) {
                        room.status = (room.status == 3) ? 0 : 3;

                        $scope.saveData('room', room, function () {
                            $scope.reloadTableData(false);
                            $scope.addDeleteFunctions();

                        }, $scope.reloadTableData, true);
                    }, $scope.reloadTableData);
                });
            }, 300);
        };

        $scope.$watch("searchFilters.rooms.number", $scope.addDeleteFunctions);
        $scope.$watch("searchFilters.rooms.roomClass", $scope.addDeleteFunctions);

        $scope.reloadTableData = function (resetPaging) {
            $scope.dtInstance.reloadData(function (list) {
                //console.log(list);
            }, resetPaging);
        };

    }
    else {
        if ($stateParams && $stateParams.id) {
            $scope.isEdit = true;
            $scope.getSingleData("room", $stateParams.id, function (data) {
                $scope.room = data;
            });
        }
        else {
            $scope.isEdit = false;
            $scope.room = {
                number: 101,
                roomClass: 0,
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

                $scope.saveData("room", $scope.master, function () {
                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!'
                    };

                    if ($scope.isEdit)
                        $scope.page.message.text = ('Edited: ' + $scope.master.number);
                    else
                        $scope.page.message.text = ('Created: ' + $scope.master.number);

                    $('#messageModal').modal('show');
                    $location.path("/rooms/list");
                }, undefined, $scope.isEdit);
            }
        };

        $scope.reset = function () {
            $scope.room = angular.copy($scope.master);
        };
    }

    angular.element(document).ready(function () {
        if ($location.path().includes("list")) {
            $scope.reloadTableData();
            //$interval($scope.reloadTableData, 30000);
        }
        else {

        }
    });

});