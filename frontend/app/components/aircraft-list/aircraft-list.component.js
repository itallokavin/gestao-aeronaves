(function () {
    'use strict';

    angular
        .module('sondaApp')
        .component('aircraftList', {
            templateUrl: 'app/components/aircraft-list/aircraft-list.template.html',
            controller: AircraftListController
        });

    AircraftListController.$inject = ['AircraftService', '$mdDialog', '$mdToast', '$rootScope'];

    function AircraftListController(AircraftService, $mdDialog, $mdToast, $rootScope) {
        var $ctrl = this;
        $ctrl.aircrafts = [];
        $ctrl.searchTerm = '';

        $ctrl.$onInit = function () {
            loadAircrafts();
            $rootScope.$on('aircraft:updated', function () {
                loadAircrafts();
            });
        };

        $ctrl.search = function () {
            if ($ctrl.searchTerm) {
                AircraftService.findByTerm($ctrl.searchTerm).then(function (data) {
                    $ctrl.aircrafts = data;
                });
            } else {
                loadAircrafts();
            }
        };

        $ctrl.edit = function (aircraft) {
            $rootScope.$broadcast('aircraft:edit', aircraft);
        };

        $ctrl.delete = function (ev, aircraft) {
            var confirm = $mdDialog.confirm()
                .title('Deseja excluir esta aeronave?')
                .textContent('A aeronave ' + aircraft.name + ' será removida permanentemente.')
                .ariaLabel('Confirmar exclusão')
                .targetEvent(ev)
                .ok('Sim, excluir')
                .cancel('Cancelar');

            $mdDialog.show(confirm).then(function () {
                AircraftService.deleteAircraft(aircraft.id).then(function () {
                    loadAircrafts();
                    $rootScope.$broadcast('aircraft:updated');
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('Aeronave excluída com sucesso!')
                            .position('top right')
                            .hideDelay(3000)
                    );
                });
            }, function () {
            });
        };

        function loadAircrafts() {
            AircraftService.getAll().then(function (data) {
                $ctrl.aircrafts = data;
                calculateBrandStats(data);
            });
        }

        function calculateBrandStats(data) {
            var stats = {
                EMBRAER: 0,
                BOEING: 0,
                AIRBUS: 0
            };

            data.forEach(function (aircraft) {
                if (stats[aircraft.brand] !== undefined) {
                    stats[aircraft.brand]++;
                } else {
                    stats[aircraft.brand] = 1;
                }
            });

            $ctrl.brandStats = stats;
        }
    }

})();
