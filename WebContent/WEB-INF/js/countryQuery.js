/**
 * Created by Flare576 on 1/20/2016.
 */
(function(){
    var app = angular.module("countryQuery", []);

    app.factory('QueryService', ['$http', '$q', "$sce", "$rootScope", function($http,$q,$sce,$rootScope){
        $rootScope.errors = "";
        var obj = {
            fetchMaxField: function(restName){
                var defer = $q.defer();
                $http.get("v1/" + restName + "/maxQty")
                    .then(
                        function(response){
                            defer.resolve(response.data);
                        },
                        function(errResponse){
                            $rootScope.errors = "The maximum amount for "+restName+" could not be retrieved. Please try again in a few minutes.";
                            defer.reject();
                        }
                    );
                return defer.promise;
            },
            fetchOnField: function(restName,qty){
                var defer = $q.defer();
                $http.get("v1/" + restName + "/" + qty)
                    .then(
                        function(response){
                            var d = response.data;
                            for(var i = 0; i< d.length; i++){
                                d[i].details = $sce.trustAsHtml(obj.generateDetails(d[i]));
                            }
                            defer.resolve(d);
                        },
                        function(errResponse){
                            $rootScope.errors = "The data for could not be retrieved for countries with "+qty+" "+restName+"'s. Please try again in a few minutes.";
                            defer.reject();
                        }
                    );
                return defer.promise;
            },
            fetchCountriesByAlpha3: function(codes){
                var defer = $q.defer();
                $http.get("v1/alpha?codes=" + codes)
                    .then(
                        function(response){
                            var d = response.data;
                            for(var i = 0; i< d.length; i++){
                                d[i].details = $sce.trustAsHtml(obj.generateDetails(d[i]));
                            }
                            defer.resolve(d);
                        },
                        function(errResponse){
                            $rootScope.errors = "The data for Country data for alpha3Codes ['"+codes+"'] could not be retrieved. Please try again in a few minutes.";
                            defer.reject();
                        }
                    );
                return defer.promise;
            },
            generateDetails: function(country){
                var returnable = "";
                for(var propertyName in country){
                    if(country.hasOwnProperty(propertyName)){
                        if("relevance" == propertyName || "translations" == propertyName){
                            continue;
                        }
                        //By default, JavaScript concatenates without a space.
                        var value = country[propertyName];
                        if(Array.isArray(value)){
                            value = value.join(", ");
                        }
                        //altSpellings becomes alt Spellings
                        var displayable = propertyName.replace(/([A-Z0-9])/g, ' $1').trim();
                        //alt Spellings becomes Alt Spellings
                        displayable = displayable.charAt(0).toUpperCase() + displayable.slice(1);
                        returnable += "<div>" + displayable + ": " + value + "</div>";
                    }
                }
                return returnable;
            }
        };
        return obj;
    }]);
})();