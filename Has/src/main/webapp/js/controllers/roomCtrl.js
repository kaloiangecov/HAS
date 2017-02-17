app.controller("roomCtrl", function ($scope, $http, $state, $stateParams, $resource, $interval, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Rooms";
    $scope.roomsTable;
    $scope.master = {};
    ctrl.filters = {
        number: 0,
        type: 0
    };
    $scope.isEdit = false;

    ctrl.deleteRoom = function (id) {
        $http({
            method: "DELETE",
            url: ("room/" + id),
            responseType: "json",
            headers: {
                "Authorization": $scope.authentication
            }
        }).then(
            function (response) { //success
                alert("Room deleted");
                window.location.hash = "#!/rooms/list";
            },
            function (response) { //error
                $scope.displayMessage(response.data);
            });
    };

    if (window.location.hash.includes("list")) {
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
                        '<a class="btn btn-default action-btn delete-btn" id="ban_' +
                        id + '" href="javascript:;"><i class="fa fa-trash-o" aria-hidden="true"></i></a>' +
                        '</div>';
                    return html;
                })
        ];
        $scope.reloadTableData = function () {
            var resetPaging = false;
            ctrl.dtInstance.reloadData(function (list) {
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
                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!'
                    };

                    if ($scope.isEdit) {
                        $scope.page.message.text = ('Edited: ' + $scope.master.number);
                    } else {
                        $scope.page.message.text = ('Created: ' + $scope.master.number);
                    }

                    $('#messageModal').modal('show');
                    window.location.hash = "#!/rooms/list";
                },
                function (response) { //error
                    $scope.displayMessage(response.data);
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
                    $scope.displayMessage(response.data);
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