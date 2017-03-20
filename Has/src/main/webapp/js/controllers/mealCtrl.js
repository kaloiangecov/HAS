app.controller("mealCtrl", function ($scope, $http, $location, $state, $stateParams, $resource, $timeout, $interval, DTOptionsBuilder, DTColumnBuilder) {
    var ctrl = this;
    $scope.page.title = "Meals";
    $scope.mealCategories = [];
    $scope.master = {};
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

    if ($location.path().includes("list")) {
        // meals table
        $scope.dtInstance = {};

        $scope.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                url: 'meal/search',
                type: 'GET',
                dataType: "json",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': $scope.authentication
                },
                data: $scope.searchFilters.meals,
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
            DTColumnBuilder.newColumn('id', 'ID').notVisible(),
            DTColumnBuilder.newColumn('name', 'Meal').withClass('meal-cell')
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
                        $scope.reloadTableData(false);
                        $scope.addDeleteFunctions();
                    });
                });
            }, 300);
        };

        $scope.$watch("searchFilters.meals.name", $scope.addDeleteFunctions);

        $scope.reloadTableData = function (resetPaging) {
            $scope.dtInstance.reloadData(function (list) {
                //console.log(list);
            }, resetPaging);
        };
    }
    else {
        $scope.getMealCategories(function (categoriesList) {
            $scope.mealCategories = categoriesList;

            if ($stateParams && $stateParams.id) {
                $scope.isEdit = true;

                $scope.getSingleData("meal", $stateParams.id, function (data) {
                    $scope.meal = data;
                    if (!$scope.meal.img)
                        $scope.meal.img = 'img/meal.png';
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

                $scope.saveData("meal", $scope.master, function () {
                    $scope.page.message = {
                        type: 'success',
                        title: 'Success!'
                    };

                    if ($scope.isEdit)
                        $scope.page.message.text = ('Edited: ' + $scope.master.name);
                    else
                        $scope.page.message.text = ('Created: ' + $scope.master.name);

                    $('#messageModal').modal('show');
                    $location.path("/meals/list");
                }, undefined, $scope.isEdit);
            }
        };

        $scope.reset = function () {
            $scope.meal = angular.copy($scope.master);
        };
    }

    angular.element(document).ready(function () {
        if ($location.path().includes("list")) {
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