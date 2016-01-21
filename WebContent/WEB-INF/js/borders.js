/**
 * Created by Flare576 on 1/20/2016.
 */
(function(){
    var app = angular.module("borders",["countryQuery"]);


    app.controller('BorderController', ['$scope', 'QueryService', function($scope, QueryService){
        var self = this;
        self.islands = "...";
        self.maxBorders = "...";
        self.countries = [];
        self.relevant = [];
        self.bordersRange = 0;

        self.fetchIslands = function(){
            self.fetchBorders(0).then(function(d){ self.islands= d.length;});
        };

        self.updateCountries = function(){
            QueryService.fetchOnField("border",self.bordersRange)
                .then(
                    function(d){
                        self.countries = d;
                        var related = [];
                        angular.forEach(d, function(country){
                            var entries = country["borders"];
                            angular.forEach(entries, function(relCountry){
                                if(related.indexOf(relCountry) == -1){
                                    related.push(relCountry);
                                }
                            });
                        });
                        QueryService.fetchCountriesByAlpha3(related.toString())
                            .then(
                                function(d){
                                    self.relevant = [];
                                    angular.forEach(d, function(relCountry){
                                        if(related.indexOf(relCountry) == -1){
                                            self.relevant[relCountry.alpha3Code] = relCountry;
                                        }
                                    });
                                },
                                function(errResponse){
                                    //todo error stuff
                                }
                            );
                    }
                );
            //Update the other values, too, to avoid presenting stale data
            self.fetchIslands();
            self.fetchMaxBorders();
        };

        self.fetchBorders = function(qty){
            return QueryService.fetchOnField("border",qty)
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
            QueryService.fetchMaxField("border")
                .then(
                    function(d){
                        self.maxBorders = d;
                    },
                    function(errResponse){
                        //todo error stuff
                    }
                );
        };

        self.updateCountries();
    }]);
})();