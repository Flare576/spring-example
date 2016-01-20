/**
 * Created by Flare576 on 1/20/2016.
 */
(function(){
    var app = angular.module("borders",["genericQuery"]);



    app.factory('BorderService', ['$http', '$sce', "CountryService", function($http,$sce,CountryService){
        return {
            fetchMaxBorders: function(){
                return $http.get("/v1/border/maxQty")
                    .then(
                        function(response){
                            return response.data;
                        },
                        function(errResponse){
                            //todo error stuff
                        }
                    );
            },
            fetchBorders: function(qty){
                return $http.get("/v1/border/" + qty)
                    .then(
                        function(response){
                            return response.data;
                        },
                        function(errResponse){
                            //todo error stuff
                        }
                    );
            },
            fetchRelevant: function(countries){
                var allAplphas = [];
                angular.forEach(countries, function(country){
                    var borders = country.borders;
                    angular.forEach(borders, function(borderCountry){
                        if(allAplphas.indexOf(borderCountry) == -1){
                            allAplphas.push(borderCountry);
                        }
                    });
                });
                return CountryService.fetchCountriesByAlpha3(allAplphas.toString())
                    .then(
                        function(d){
                            for(var i = 0; i< d.length; i++){
                                d[i].details = $sce.trustAsHtml(CountryService.generateDetails(d[i]));
                            }
                            return d;
                        },
                        function(errResponse){
                            //todo error stuff
                        }
                    );
            }
        };
    }]);

    app.controller('BorderController', ['$scope', 'BorderService', function($scope, BorderService){
        var self = this;
        self.islands = 0;
        self.maxBorders = 0;
        self.countries = [];
        self.relevant = [];

        self.fetchIslands = function(){
            self.fetchBorders(0).then(function(d){ self.islands= d.length;});
            //self.islands = self.fetchBorders(0).length;
        };

        self.updateCountries = function(){
            BorderService.fetchBorders(self.bordersRange)
                .then(
                    function(d){
                        self.countries = d;
                        BorderService.fetchRelevant(d)
                            .then(
                                function(d){
                                    angular.forEach(d, function(country){
                                        self.relevant[country.alpha3Code] = country;
                                    });
                                }
                            );
                    }
                );
            //Update the other values, too, to avoid presenting stale data
            self.fetchIslands();
            self.fetchMaxBorders();
        };

        self.fetchBorders = function(qty){
            return BorderService.fetchBorders(qty)
                .then(
                    function(d){
                        return d;
                    },
                    function(errResponse){
                        //todo error stuff
                    }
                );
        };

        self.fetchMaxBorders = function(){
            BorderService.fetchMaxBorders()
                .then(
                    function(d){
                        self.maxBorders = d;
                    },
                    function(errResponse){
                        //todo error stuff
                    }
                );
        };

        self.fetchIslands();
        self.fetchMaxBorders();
    }]);
})();