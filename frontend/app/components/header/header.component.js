(function () {
    'use strict';

    angular
        .module('sondaApp')
        .component('headerComponent', {
            templateUrl: 'app/components/header/header.template.html',
            controller: HeaderController
        });

    function HeaderController() {
        var $ctrl = this;
        $ctrl.searchQuery = '';

        $ctrl.$onInit = function () {
        };
    }

})();
