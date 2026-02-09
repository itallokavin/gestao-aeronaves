(function () {
    'use strict';

    angular
        .module('sondaApp')
        .factory('AircraftService', AircraftService);

    AircraftService.$inject = ['$http', '$q'];

    function AircraftService($http, $q) {
        var API_URL = 'http://localhost:8080/aeronaves';

        var service = {
            getAll: getAll,
            findByTerm: findByTerm,
            getById: getById,
            create: create,
            update: update,
            deleteAircraft: deleteAircraft,
            getUnsoldCount: getUnsoldCount,
            getDecadeStats: getDecadeStats,
            getLastWeek: getLastWeek
        };

        return service;

        function getAll() {
            return $http.get(API_URL).then(handleSuccess, handleError);
        }

        function findByTerm(term) {
            return $http.get(API_URL + '/find', { params: { term: term } }).then(handleSuccess, handleError);
        }

        function getById(id) {
            return $http.get(API_URL + '/' + id).then(handleSuccess, handleError);
        }

        function create(aircraft) {
            return $http.post(API_URL, aircraft).then(handleSuccess, handleError);
        }

        function update(id, aircraft) {
            return $http.put(API_URL + '/' + id, aircraft).then(handleSuccess, handleError);
        }

        function deleteAircraft(id) {
            return $http.delete(API_URL + '/' + id).then(handleSuccess, handleError);
        }

        // Statistics
        function getUnsoldCount() {
            return $http.get(API_URL + '/statistics/unsold').then(handleSuccess, handleError);
        }

        function getDecadeStats() {
            return $http.get(API_URL + '/statistics/by-decade').then(handleSuccess, handleError);
        }

        function getLastWeek() {
            return $http.get(API_URL + '/statistics/last-week').then(handleSuccess, handleError);
        }

        function handleSuccess(res) {
            return res.data;
        }

        function handleError(error) {
            console.error('API Error:', error);
            return $q.reject(error);
        }
    }

})();
