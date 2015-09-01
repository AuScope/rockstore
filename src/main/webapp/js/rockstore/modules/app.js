var app = angular.module('app', ['ngRoute','allControllers','ui.grid','ui.grid.selection','ui.grid.resizeColumns','ui.bootstrap','angularSpinners','uiGmapgoogle-maps']);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
        templateUrl: 'views/browse_collection.html'
      
      }).
      when('/browsecollections', {
        templateUrl: 'views/browse_collection.html'
     
      }).
      when('/browsecollections/:collectionId', {
          templateUrl: 'views/browse_collection.html'
       
        }).
      when('/browsesubcollections', {
          templateUrl: 'views/browse_subcollection.html'
        
        }).
      when('/browsesubcollections/:subCollectionId', {
        templateUrl: 'views/browse_subcollection.html'
      
        }). 
      when('/browsesamples', {
        templateUrl: 'views/browse_sample.html'
       
      }).    
      when('/browsesamples/:id', {
          templateUrl: 'views/browse_sample.html'
         
      }).  
      when('/collections', {
          templateUrl: 'restricted/collections.html'
        
        }).
      when('/subcollections', {
          templateUrl: 'restricted/subcollections.html'
        
        }).
      when('/samples', {
          templateUrl: 'restricted/samples.html'
        
        }).      
      when('/login', {
          templateUrl: 'views/login.html'
         
        }).     
      otherwise({
        redirectTo: '/'
      });
    
    
    
  }]);

app.config(function(uiGmapGoogleMapApiProvider) {
    uiGmapGoogleMapApiProvider.configure({
        //    key: 'your api key',
        //v: '3.20', //defaults to latest 3.X anyhow
        libraries: 'weather,geometry,visualization'
    });
})

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

app.service('currentAuthService', function() {
	
	 var status = {};	

    return {
    	getAuthenticated: function () {
            return status.authenticated;
        },
        setAuthenticated: function (auth) {
        	status.authenticated = auth;
        },
        setUsername : function(name){
        	status.username=name;
        },
        getUsername : function(){
        	return status.username;
        },
        getStatus : function(){
        	return status;
        }
    };	    
    
});

app.service('MapModalService',['$modal',function ($modal) {
	//VT: GOOGLE MAP MODALS
     this.open = function (lat, lon,info) {

       var modalInstance = $modal.open({
         animation: true,
         templateUrl: 'widget/GoogleMapModal.html',
         controller: 'MapModalInstanceCtrl',
         size: 'lg',
         resolve: {
           marker: function () {
             return {
            	 latitude :lat,
            	 longitude : lon,
            	 info : info 
             }
           }
         }
       });
     };
}])

app.service('SearchCollectionService',['$modal','$q',function ($modal,$q) {
	//VT: GOOGLE MAP MODALS
     this.open = function () {
    	 return $q(function(resolve, reject) {
    		 var modalInstance = $modal.open({
    	         animation: true,
    	         templateUrl: 'widget/SearchCollectionModal.html',
    	         controller: 'SearchCollectionCtrl',
    	         size: 'lg'         
    	       });
    		 
    	       modalInstance.result.then(function (selectedItem) {
    	 	      resolve(selectedItem);
    	 	    });
    	 },1000);

     };
}])

app.service('SearchSubCollectionService',['$modal','$q',function ($modal,$q) {
	//VT: GOOGLE MAP MODALS
     this.open = function () {
    	 return $q(function(resolve, reject) {
    		 var modalInstance = $modal.open({
    	         animation: true,
    	         templateUrl: 'widget/SearchSubCollectionModal.html',
    	         controller: 'SearchSubCollectionCtrl',
    	         size: 'lg'         
    	       });
    		 
    	       modalInstance.result.then(function (selectedItem) {
    	 	      resolve(selectedItem);
    	 	    });
    	 },1000);

     };
}])

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

