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
            self.fetchBorders(0).then(
                function(d){
                    self.islands= d.length;
                },
                function(errResponse){
                });
        };

        self.updateCountries = function(){
            //Get all the countries for the currently selected range
            QueryService.fetchOnField("border",self.bordersRange)
                .then(
                    /**
                     * After the fetch is complete, go through each of the countries, forming a cumulative list of the
                     * bordering countries, then get the details about THEM for their own tooltips. Then make an
                     * associative array between their alpha3Code and the object.
                     *
                     * @param d The data from the Fetch
                     */
                    function(d){
                        self.countries = d;
                        //Build out the list of "Related Countries," in this case, the countries bordering our results
                        var related = [];
                        angular.forEach(d, function(country){
                            var entries = country["borders"];
                            angular.forEach(entries, function(relCountry){
                                //We only want 1 instance of a country
                                if(related.indexOf(relCountry) == -1){
                                    related.push(relCountry);
                                }
                            });
                        });
                        //Pass our list off to the QueryService
                        QueryService.fetchCountriesByAlpha3(related.toString())
                            .then(
                                function(d){
                                    //Clear the list.
                                    self.relevant = [];
                                    //And rebuild it.
                                    angular.forEach(d, function(relCountry){
                                        if(related.indexOf(relCountry) == -1){
                                            self.relevant[relCountry.alpha3Code] = relCountry;
                                        }
                                    });
                                    //Update the other values, too, to avoid presenting stale data
                                    self.fetchIslands();
                                    self.fetchMaxBorders();
                                },
                                function(errResponse){
                                }
                            );
                    },
                    function(errResponse){
                    }
                );
        };

        self.fetchBorders = function(qty){
            return QueryService.fetchOnField("border",qty)
                .then(
                    function(d){
                        return d;
                    },
                    function(errResponse){
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
                    }
                );
        };

        self.updateCountries();
    }]);
})();