app.controller("guestCtrl", function ($scope, $state, $stateParams, $timeout, $interval, $resource, $http, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Guests";
    $scope.master = {};
    ctrl.filters = {
        fullName: "",
        phone: ""
    };
    $scope.usersList = [];
    $scope.isEdit = false;

    if (window.location.hash.includes("list")) {
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
            DTColumnBuilder.newColumn('personalData.fullName', 'Full Name'),
            DTColumnBuilder.newColumn('personalData.phone', 'Phone Number'),
            DTColumnBuilder.newColumn('personalData.address', 'Address'),
            DTColumnBuilder.newColumn('numberReservations', 'Number of Reservations'),
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (id) {
                    var html =
                        '<div class="btn-group btn-group-sm">' +
                        '<a class="btn btn-default action-btn" href="#!/guests/edit/' +
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
                    $scope.deleteData('guest', id, function () {
                        $scope.page.message = {
                            type: 'success',
                            title: 'Deleted!',
                            text: ('Guest with id ' + id + ' was successfully deleted!')
                        };
                        $('#messageModal').modal('show');
                    });
                });
            }, 300);
        };

        $scope.$watch("ctrl.filters.fullName", $scope.addDeleteFunctions);
        $scope.$watch("ctrl.filters.phone", $scope.addDeleteFunctions);

        $scope.reloadTableData = function () {
            var resetPaging = false;
            $scope.dtInstance.reloadData(function (list) {
                //console.log(list);
                $scope.addDeleteFunctions();
            }, resetPaging);
        };
    }
    else {
        function saveGuest(callback) {
            var url = $scope.isEdit ? ("guest/" + $stateParams.id) : "guest";
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
                callback,
                function (response) { //error
                    $scope.displayMessage(response.data);
                });
        }

        if ($stateParams && $stateParams.id) {
            $scope.isEdit = true;

            var url = "guest/" + $stateParams.id;

            $http({
                method: "GET",
                url: url,
                responseType: "json",
                headers: {
                    "Authorization": $scope.authentication
                }
            }).then(
                function (response) { //success
                    return response.data;
                },
                function (response) { //error
                    $scope.displayMessage(response.data);
                }).then(
                function (data) {
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

                saveGuest(function () {
                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!'
                    };

                    if ($scope.isEdit) {
                        $scope.page.message.text = ('Edited: ' + $scope.master.personalData.fullName);
                    } else {
                        $scope.page.message.text = ('Created: ' + $scope.master.personalData.fullName);
                    }

                    $('#messageModal').modal('show');
                    window.location.hash = "#!/guests/list";
                });
            }
        };
    }

    angular.element(document).ready(function () {

        if (window.location.hash.includes("list")) {
            $scope.reloadTableData();
            //$interval($scope.reloadTableData, 30000);
        }
        else {
            $('#dateHired,#identityIssueDate,#identityExpireDate').daterangepicker({
                singleDatePicker: true,
                showDropdowns: true,
                locale: {
                    format: "YYYY-MM-DD"
                }
            });
        }
    });

});