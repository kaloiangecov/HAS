app.controller("roomCtrl", function ($scope, $http, $location, $state, $stateParams, $resource, $timeout, $interval, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Rooms";
    $scope.roomsTable;
    $scope.master = {};
    ctrl.filters = {
        number: 0,
        type: 0
    };
    $scope.isEdit = false;

    if ($location.path().includes("list")) {
        // rooms table
        ctrl.dtInstance = {};

        ctrl.dtOptions = DTOptionsBuilder.newOptions()
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

        ctrl.dtColumns = [
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
                    var html =
                        '<div class="btn-group btn-group-sm">' +
                        '<a class="btn btn-default action-btn" href="#!/rooms/edit/' +
                        id + '"><i class="fa fa-pencil" aria-hidden="true"></i></a>' +
                        '<button class="btn btn-default action-btn delete-btn" id="del_' +
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
                    $scope.deleteData('room', id, function () {
                        $scope.page.message = {
                            type: 'success',
                            title: 'Deleted!',
                            text: ('Room with id ' + id + ' was successfully deleted!')
                        };
                        $('#messageModal').modal('show');
                    });
                });
            }, 300);
        };

        $scope.$watch("ctrl.filters.number", $scope.addDeleteFunctions);
        $scope.$watch("ctrl.filters.type", $scope.addDeleteFunctions);

        $scope.reloadTableData = function () {
            var resetPaging = false;
            ctrl.dtInstance.reloadData(function (list) {
                //console.log(list);
                $scope.addDeleteFunctions();
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