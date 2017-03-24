app.controller("guestCtrl", function ($scope, $state, $location, $stateParams, $timeout, $interval, $resource, $http, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Guests";
    $scope.master = {};
    $scope.usersList = [];
    $scope.isEdit = false;

    if ($location.path().includes("list")) {
        // guests table
        $scope.dtInstance = {};

        $scope.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                url: 'guests/search',
                type: 'GET',
                dataType: "json",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': $scope.authentication
                },
                data: $scope.searchFilters.guests,
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
            DTColumnBuilder.newColumn('personalData.fullName', 'Full Name'),
            DTColumnBuilder.newColumn('personalData.phone', 'Phone Number'),
            DTColumnBuilder.newColumn('personalData.address', 'Address'),
            DTColumnBuilder.newColumn('numberReservations', 'Guest rating'),
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (id) {
                    var html =
                        '<div class="btn-group btn-group-sm">' +
                        '<a class="btn btn-default action-btn" href="#!/guests/history/' +
                        id + '"><i class="fa fa-clock-o" aria-hidden="true"></i></a>' +
                        '<a class="btn btn-default action-btn" href="#!/guests/edit/' +
                        id + '"><i class="fa fa-pencil" aria-hidden="true"></i></a>' +
                        '</div>';
                    return html;
                })
        ];

        $scope.reloadTableData = function (resetPaging) {
            $scope.dtInstance.reloadData(function (list) {
                //console.log(list);
            }, resetPaging);
        };
    } else if ($location.path().includes("history")) {
        // guests table
        $scope.dtHistoryInstance = {};

        $scope.dtHistoryOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                url: ('reservations/guest/' + $stateParams.id),
                type: 'GET',
                dataType: "json",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': $scope.authentication
                },
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

        $scope.dtHistoryColumns = [
            DTColumnBuilder.newColumn('id', 'ID').notVisible(),
            DTColumnBuilder.newColumn('startDate', 'Check in'),
            DTColumnBuilder.newColumn('endDate', 'Check out'),
            DTColumnBuilder.newColumn('room.number', 'Room'),
            DTColumnBuilder.newColumn('price', 'Price')
        ];

        $scope.reloadHistoryTableData = function (resetPaging) {
            $scope.dtHistoryInstance.reloadData(function (list) {
                //console.log(list);
                $scope.addDeleteFunctions();
            }, resetPaging);
        };
    }
    else {
        if ($stateParams && $stateParams.id) {
            $scope.isEdit = true;

            $scope.getSingleData("guest", $stateParams.id, function (data) {
                $scope.guest = data;

                var userID = -1;
                if ($scope.guest.user)
                    userID = $scope.guest.user.id;

                $scope.getFreeUsers(userID, "guests", function (data) {
                    var emptyArray = [
                        {
                            id: 0,
                            username: "-- None --",
                            email: "-- None --"
                        }
                    ];

                    $scope.usersList = emptyArray.concat(data);

                    if (!$scope.guest.user)
                        $scope.guest.user = $scope.usersList[0];
                });
            });
        }
        else {
            $scope.isEdit = false;
            $scope.guest = {
                numberReservations: 0,
                status: 0,
                personalData: {}
            };
            $scope.getFreeUsers(-1, "guests", function (data) {
                var emptyArray = [
                    {
                        id: 0,
                        username: "-- None --",
                        email: "-- None --"
                    }
                ];

                $scope.usersList = emptyArray.concat(data);

                $scope.guest.user = $scope.usersList[0];
            });
        }


        $scope.submit = function (guest) {
            if ($scope.guestForm.$valid) {
                $scope.master = angular.copy(guest);

                if ($scope.master.user.id === 0)
                    delete $scope.master.user;

                $scope.saveData("guest", $scope.master, function () {
                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!'
                    };

                    if ($scope.isEdit)
                        $scope.page.message.text = ('Edited: ' + $scope.master.personalData.fullName);
                    else
                        $scope.page.message.text = ('Created: ' + $scope.master.personalData.fullName);

                    $('#messageModal').modal('show');
                    $location.path("/guests/list");
                }, undefined, $scope.isEdit);
            }
        };
    }

    angular.element(document).ready(function () {

        if ($location.path().includes("list")) {
            $scope.reloadTableData();
            //$interval($scope.reloadTableData, 30000);
        }
        else {
            $('#dateHired,#identityIssueDate,#identityExpireDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true,
                locale: {
                    format: "YYYY-MM-DD",
                    firstDay: 1
                }
            });
        }
    });

});