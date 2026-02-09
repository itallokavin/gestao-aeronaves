(function () {
    'use strict';

    angular
        .module('sondaApp')
        .component('dashboardStats', {
            templateUrl: 'app/components/dashboard-stats/dashboard-stats.template.html',
            controller: DashboardStatsController
        });

    DashboardStatsController.$inject = ['AircraftService', '$q', '$rootScope'];

    function DashboardStatsController(AircraftService, $q, $rootScope) {
        var $ctrl = this;
        $ctrl.stats = [];

        $ctrl.$onInit = function () {
            loadStats();
            $rootScope.$on('aircraft:updated', function () {
                loadStats();
            });
        };

        function loadStats() {
            var promises = {
                unsold: AircraftService.getUnsoldCount(),
                decades: AircraftService.getDecadeStats(),
                lastWeek: AircraftService.getLastWeek()
            };

            $q.all(promises).then(function (results) {
                var unsold = results.unsold;
                var decades = results.decades;
                var lastWeek = results.lastWeek;


                $ctrl.stats = [
                    {
                        label: 'DÉCADA 90',
                        value: decades[1990] || 0,
                        icon: 'history',
                        color: '#E0E0E0'
                    },
                    {
                        label: 'DÉCADA 00',
                        value: decades[2000] || 0,
                        icon: 'calendar_today',
                        color: '#E0E0E0'
                    },
                    {
                        label: 'ESSA SEMANA',
                        value: lastWeek ? lastWeek.length : 0,
                        icon: 'trending_up',
                        color: '#4CAF50'
                    },
                    {
                        label: 'NÃO VENDIDAS',
                        value: unsold,
                        icon: 'local_offer',
                        color: '#FF9800'
                    }
                ];

            });
        }
    }

})();
