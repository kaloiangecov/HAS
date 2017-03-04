app.controller("mealCtrl", function ($scope, $http, $state, $stateParams, $resource, $timeout, $interval, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Meals";
    $scope.mealCategories = [];
    $scope.master = {};
    ctrl.filters = {
        name: ''
    };
    $scope.isEdit = false;

    $scope.getMealCategories = function (callback) {
        $http({
            method: "GET",
            url: "mealTypes/all",
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
            }).then(callback);
    };

    if (window.location.hash.includes("list")) {
        // meals table
        ctrl.dtInstance = {};

        ctrl.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                url: 'meal/search',
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
            DTColumnBuilder.newColumn('id', 'ID').notVisible(),
            DTColumnBuilder.newColumn('name', 'Meal')
                .renderWith(function (data, type, full) {
                    var html = data;
                    html += '<br/><img alt="meal picture" src="' + full.img + '" />';
                    return html;
                }),
            DTColumnBuilder.newColumn('description', 'Description')
                .renderWith(function (data) {
                    if (data.length >= 40)
                        return data.substr(0, 40) + '...';
                    else
                        return data;
                }),
            DTColumnBuilder.newColumn('price', 'Price'),
            DTColumnBuilder.newColumn('mealCategory', 'Category')
                .renderWith(function (mealCategory) {
                    return mealCategory.title;
                }),
            DTColumnBuilder.newColumn('id').notSortable().withClass('actions-column')
                .renderWith(function (id) {
                    var html =
                        '<div class="btn-group btn-group-sm">' +
                        '<a class="btn btn-default action-btn" href="#!/meals/edit/' +
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
                    $scope.deleteData('meal', id, function () {
                        $scope.page.message = {
                            type: 'success',
                            title: 'Deleted!',
                            text: ('Meal with id ' + id + ' was successfully deleted!')
                        };
                        $('#messageModal').modal('show');
                    });
                });
            }, 300);
        };

        $scope.$watch("ctrl.filters.name", $scope.addDeleteFunctions);

        $scope.reloadTableData = function () {
            var resetPaging = false;
            ctrl.dtInstance.reloadData(function (list) {
                //console.log(list);
                $scope.addDeleteFunctions();
            }, resetPaging);
        };
    }
    else {
        function saveMeal() {
            var url = $scope.isEdit ? ("meal/" + $stateParams.id) : "meal";
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
                    window.location.hash = "#!/meals/list";
                },
                function (response) { //error
                    $scope.displayMessage(response.data);
                });
        }

        $scope.getMealCategories(function (categoriesList) {
            $scope.mealCategories = categoriesList;

            if ($stateParams && $stateParams.id) {
                $scope.isEdit = true;
                var url = "meal/" + $stateParams.id;

                $http({
                    method: "GET",
                    url: url,
                    responseType: "json",
                    headers: {
                        "Authorization": $scope.authentication
                    }
                }).then(
                    function (response) { //success
                        $scope.meal = response.data;
                        if (!$scope.meal.img)
                            $scope.meal.img = 'img/meal.png';
                    },
                    function (response) { //error
                        $scope.displayMessage(response.data);
                    });
            }
            else {
                $scope.isEdit = false;
                $scope.meal = {
                    name: '',
                    description: '',
                    price: 0.0,
                    date: (moment().format('YYYY-MM-DD')),
                    img: 'img/meal.png',
                    mealCategory: $scope.mealCategories[0]
                };
            }
        });

        $scope.submit = function (meal) {
            if ($scope.mealForm.$valid) {
                $scope.master = angular.copy(meal);
                saveMeal();
            }
        };

        $scope.reset = function () {
            $scope.meal = angular.copy($scope.master);
        };
    }

    angular.element(document).ready(function () {
        if (window.location.hash.includes("list")) {
            $scope.reloadTableData();
            //$interval($scope.reloadTableData, 30000);
        }
        else {
            $('#inputPicture').on('change', function () {
                var files = $(this).prop('files');
                if (files && files[0]) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        if (e.total <= 131072) {
                            $scope.$apply(function () {
                                $scope.meal.img = e.target.result;
                            });
                        } else {
                            $scope.$apply(function () {
                                $scope.page.message = {
                                    type: 'danger',
                                    title: 'Error!',
                                    text: "File size can't be more than 128 KB!"
                                };
                                $('#messageModal').modal('show');
                            });
                        }
                    };

                    reader.readAsDataURL(files[0]);
                }
            });

            $scope.chooseMealPicture = function () {
                $('#inputPicture').trigger('click');
            };
        }
    });

});