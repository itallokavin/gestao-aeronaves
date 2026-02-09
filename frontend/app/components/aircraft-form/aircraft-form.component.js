(function () {
    'use strict';

    angular
        .module('sondaApp')
        .component('aircraftForm', {
            templateUrl: 'app/components/aircraft-form/aircraft-form.template.html',
            controller: AircraftFormController,
            bindings: {
                onAircraftAdded: '&'
            }
        });

    AircraftFormController.$inject = ['AircraftService', '$mdToast', '$rootScope'];

    function AircraftFormController(AircraftService, $mdToast, $rootScope) {
        var $ctrl = this;

        $ctrl.brands = ['EMBRAER', 'BOEING', 'AIRBUS'];

        $ctrl.$onInit = function () {
            resetForm();
            $rootScope.$on('aircraft:edit', function (event, aircraft) {
                $ctrl.aircraft = angular.copy(aircraft);
            });
        };

        $ctrl.submit = function () {
            if ($ctrl.aircraftForm.$valid) {
                var promise;
                if ($ctrl.aircraft.id) {
                    promise = AircraftService.update($ctrl.aircraft.id, $ctrl.aircraft);
                } else {
                    promise = AircraftService.create($ctrl.aircraft);
                }

                promise.then(function (res) {
                    var action = $ctrl.aircraft.id ? 'atualizada' : 'cadastrada';
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('Aeronave ' + action + ' com sucesso!')
                            .position('top right')
                            .hideDelay(3000)
                    );
                    resetForm();
                    $rootScope.$broadcast('aircraft:updated');
                }).catch(function (error) {
                    var msg = 'Erro ao salvar aeronave.';
                    if (error.data && error.data.message) {
                        msg = error.data.message;
                    }
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent(msg)
                            .position('top right')
                            .hideDelay(5000)
                            .theme('error-toast') // We might need to define this theme or just accept default style with different text
                    );
                });
            }
        };

        $ctrl.cancel = function () {
            resetForm();
        };

        function resetForm() {
            $ctrl.aircraft = {
                name: '',
                brand: '',
                year: null,
                description: '',
                sold: false
            };
            if ($ctrl.aircraftForm) {
                $ctrl.aircraftForm.$setUntouched();
                $ctrl.aircraftForm.$setPristine();
            }
        }
    }

})();
