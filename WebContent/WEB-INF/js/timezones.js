/**
 * Created by Flare576 on 1/20/2016.
 */
(function(){
    var app = angular.module("timezones",["countryQuery"]);


    app.controller('TimezoneController', ['$scope', 'QueryService', function($scope, QueryService){
        var self = this;
        self.noTimezone = "...";
        self.maxTimezones = "...";
        self.countries = [];
        self.relevant = [];
        self.timezoneRange = 0;

        self.fetchSingles = function(){
            self.fetchTimezones(0).then(function(d){ self.noTimezone= d.length;});
        };

        self.updateCountries = function(){
            QueryService.fetchOnField("timezone",self.timezoneRange)
                .then(
                    function(d){
                        self.countries = d;
                    }
                );
            //Update the other values, too, to avoid presenting stale data
            self.fetchSingles();
            self.fetchMaxTimezones();
        };

        self.fetchTimezones = function(qty){
            return QueryService.fetchOnField("timezone",qty)
                .then(
                    function(d){
                        return d;
                    },
                    function(errResponse){
                        //todo error stuff
                    }
                );
        };

        self.fetchMaxTimezones = function(){
            QueryService.fetchMaxField("timezone")
                .then(
                    function(d){
                        self.maxTimezones = d;
                    },
                    function(errResponse){
                        //todo error stuff
                    }
                );
        };

        self.updateCountries();
    }]);
})();