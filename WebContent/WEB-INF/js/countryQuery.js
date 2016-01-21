/**
 * Created by Flare576 on 1/20/2016.
 */
(function(){
    var app = angular.module("countryQuery", []);

    app.factory('QueryService', ['$http', "$sce", function($http,$sce){
        var self = this;
        var obj = {
            fetchMaxField: function(restName){
                return $http.get("/v1/" + restName + "/maxQty")
                    .then(
                        function(response){
                            return response.data;
                        },
                        function(errResponse){
                            //todo error stuff
                        }
                    );
            },
            fetchOnField: function(restName,qty){
                return $http.get("/v1/" + restName + "/" + qty)
                    .then(
                        function(response){
                            var d = response.data;
                            for(var i = 0; i< d.length; i++){
                                d[i].details = $sce.trustAsHtml(obj.generateDetails(d[i]));
                            }
                            return d;
                        },
                        function(errResponse){
                            //todo error stuff
                        }
                    );
            },
            fetchCountriesByAlpha3: function(codes){
                return $http.get("/v1/alpha?codes=" + codes)
                    .then(
                        function(response){
                            var d = response.data;
                            for(var i = 0; i< d.length; i++){
                                d[i].details = $sce.trustAsHtml(obj.generateDetails(d[i]));
                            }
                            return d;
                        },
                        function(errResponse){
                            //todo error stuff
                        }
                    );
            },
            generateDetails: function(country){
                var returnable = "";
                for(var propertyName in country){
                    if(country.hasOwnProperty(propertyName)){
                        if("relevance" == propertyName || "translations" == propertyName){
                            continue;
                        }
                        //altSpellings becomes alt Spellings
                        var displayable = propertyName.replace(/([A-Z0-9])/g, ' $1').trim();
                        //alt Spellings becomes Alt Spellings
                        displayable = displayable.charAt(0).toUpperCase() + displayable.slice(1);
                        returnable += "<div>"+displayable+": "+country[propertyName] + "</div>";
                    }
                }
                return returnable;
            }
        };
        return obj;
    }]);
})();