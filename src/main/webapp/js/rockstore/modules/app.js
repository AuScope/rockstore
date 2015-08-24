var app = angular.module('app', ['ngRoute','allControllers','ui.grid','ui.grid.selection','ui.grid.resizeColumns','ui.bootstrap','angularSpinners']);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
        templateUrl: 'views/browse.html',
        controller: 'CollectionCtrl'
      }).
      when('/browse', {
        templateUrl: 'views/browse.html',
        controller: 'CollectionCtrl'
      }).
      when('/collections', {
          templateUrl: 'views/collections.html',
          controller: 'CollectionCtrl'
        }).
      when('/subcollections', {
          templateUrl: 'views/subcollections.html',
          controller: 'SubCollectionCtrl'
        }).
      when('/samples', {
          templateUrl: 'views/samples.html',
          controller: 'SampleCtrl'
        }).
      when('/login', {
          templateUrl: 'views/login.html',
          controller: 'LoginCtrl'
        }).     
      otherwise({
        redirectTo: '/'
      });
  }]);

app.service('DropDownValueService', function() {
    this.getUsers = function() {
           return [{
        	   value:'lala@gmail.com', 
        	   display:'Jeremy Jones'
        	},{
        		value:'hohoho@hotmail.com', 
        		display:'Warren Buffet'
        	}];
        };
 
    this.getBoolean = function() {
            return [{
            	value:true, 
            	YesNo:'Yes'            	
            },{
            	value:false, 
            	YesNo:'No'           	
            }];
        };
        
        
    this.getExternalUsers = function() {
        return [{
        	value:'xxxx@gmail.com', 
        	display:'Andy Lau'
        },{
        	value:'yyyy@hotmail.com', 
        	display:'John Lennon'
        }];
     };  
     
     this.getLocations = function() {
         return ['G1-L2','G2-L3','G4-L5'];
      };  
      
      this.getStorageType = function(){
    	  return ['STANDARD','EXTRA','COLD ROOM'];
      }
      
      this.getSampleType = function(){
    	  return ['ROCK','SAND','LIME'];
      }
      
      this.getDatum = function(){
    	  return ['EPSG:4326','GDA84'];
      }
        
});

app.service('modalService', ['$modal',function ($modal) {

      var modalDefaults = {
          backdrop: true,
          keyboard: true,
          modalFade: true,
          templateUrl: 'widget/modal.html'
      };

      var modalOptions = {
          closeButtonText: 'Close',
          actionButtonText: 'OK',
          headerText: 'Proceed?',
          bodyText: 'Perform this action?'
      };

      this.showModal = function (customModalDefaults, customModalOptions) {
          if (!customModalDefaults) customModalDefaults = {};
          customModalDefaults.backdrop = 'static';
          return this.show(customModalDefaults, customModalOptions);
      };

      this.show = function (customModalDefaults, customModalOptions) {
          //Create temp objects to work with since we're in a singleton service
          var tempModalDefaults = {};
          var tempModalOptions = {};

          //Map angular-ui modal custom defaults to modal defaults defined in service
          angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);

          //Map modal.html $scope custom properties to defaults defined in service
          angular.extend(tempModalOptions, modalOptions, customModalOptions);

          if (!tempModalDefaults.controller) {
              tempModalDefaults.controller = function ($scope, $modalInstance) {
                  $scope.modalOptions = tempModalOptions;
                  $scope.modalOptions.ok = function (result) {
                      $modalInstance.close(result);
                  };
                  $scope.modalOptions.close = function (result) {
                      $modalInstance.dismiss('cancel');
                  };
              }
          }

          return $modal.open(tempModalDefaults).result;
      };

  }]);



app.directive('jqdatepicker', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
         link: function (scope, element, attrs, ngModelCtrl) {
            element.datepicker({
            	dateFormat: 'd/M/yy',
                onSelect: function (date) {
                	ngModelCtrl.$setViewValue(date);
                	ngModelCtrl.$render();
                    scope.$apply();
                }
            });
        }
    };
});

