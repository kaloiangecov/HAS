app.controller("roomCtrl", function ($scope, $state, $stateParams, $resource, $timeout, $interval, DTOptionsBuilder, DTColumnBuilder) {
    $scope.page.title = "Rooms";
    $scope.roomsTable;
    $scope.master = {};
    $scope.isEdit = false;

    if ($stateParams.id) {
        $scope.isEdit = true;
        $scope.room = angular.copy($scope.rooms[$stateParams.id]);
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

    $scope.submit = function (user) {
        if ($scope.roomForm.$valid) {
            $scope.master = angular.copy(user);

            if ($scope.isEdit) {
                $scope.rooms[$stateParams.id] = $scope.master;
            } else {
                $scope.rooms.push($scope.master);
            }

            delete $scope.master.picture;
            $scope.exportForm($scope.master);

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
            url: 'sample_data/sampleRooms.json', //TODO put real service URL here
            type: 'GET'
        })
        .withDataProp('data')
        .withOption('processing', true)
        .withOption('serverSide', true)
        .withOption('pagingType', 'full_numbers')
        .withOption('dom', 'lrtip');

    $scope.dtColumns = [
        DTColumnBuilder.newColumn('number', 'Number'),
        DTColumnBuilder.newColumn('type', 'Type')
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
        $interval($scope.reloadTableData, 5000);
    });

});