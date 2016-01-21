/**
 * Created by Flare576 on 1/19/2016.
 */
(function(){
    var app = angular.module("countries",["borders","timezones","ui.bootstrap"]);

    app.directive("statsTabs", function() {
        return {
            restrict: "E",
            templateUrl: "html/statsTabs.html",
            controller: function() {
                this.tab = 1;

                this.isSet = function(checkTab) {
                    return this.tab === checkTab;
                };

                this.setTab = function(activeTab) {
                    this.tab = activeTab;
                };
            },
            controllerAs: "tab"
        };
    });

    app.directive("statsBorders", function() {
        return {
            restrict:"E",
            templateUrl: "html/stats-borders.html"
        };
    });

    app.directive("statsTimezones", function() {
        return {
            restrict:"E",
            templateUrl: "html/stats-timezones.html"
        };
    });

    app.directive("statsAbout", function() {
        return {
            restrict:"E",
            templateUrl: "html/stats-about.html"
        };
    });


})();