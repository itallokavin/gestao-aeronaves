(function () {
    'use strict';

    angular
        .module('sondaApp', ['ngMaterial', 'ngMessages'])
        .config(function ($mdThemingProvider) {
            $mdThemingProvider.definePalette('aeroBlue', {
                '50': 'e8eaf6',
                '100': 'c5cae9',
                '200': '9fa8da',
                '300': '7986cb',
                '400': '5c6bc0',
                '500': '3f51b5',
                '600': '3949ab',
                '700': '303f9f',
                '800': '283593',
                '900': '1a237e',
                'A100': '8c9eff',
                'A200': '536dfe',
                'A400': '3d5afe',
                'A700': '304ffe',
                'contrastDefaultColor': 'light',
                'contrastDarkColors': ['50', '100', '200', '300', '400', 'A100'],
                'contrastLightColors': undefined
            });

            $mdThemingProvider.theme('default')
                .primaryPalette('aeroBlue')
                .accentPalette('orange');

        });

})();
