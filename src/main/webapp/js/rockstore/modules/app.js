var app = angular.module('app', ['ngRoute','allControllers','ui.grid','ui.grid.selection','ui.grid.resizeColumns','ui.bootstrap','angularSpinners','uiGmapgoogle-maps','ngAnimate','monospaced.qrcode']);

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
      when('/browsesubcollections/:igsn', {
        templateUrl: 'views/browse_subcollection.html'
      
      }). 
      when('/browsesamples', {
        templateUrl: 'views/browse_sample.html'
       
      }).    
      when('/browsesamples/:igsn', {
          templateUrl: 'views/browse_sample.html'
         
      }).  
      when('/collections', {
          templateUrl: 'restricted/collections.html'
        
      }).
      when('/collections/:collectionId', {
        templateUrl: 'restricted/collections.html'
      
      }).  
      when('/subcollections', {
          templateUrl: 'restricted/subcollections.html'
        
      }).
      when('/subcollections/:subCollectionId', {
          templateUrl: 'restricted/subcollections.html'
        
      }).
      when('/samples', {
          templateUrl: 'restricted/samples.html'
        
      }).
      when('/samples/:id', {
          templateUrl: 'restricted/samples.html'
        
      }).
      when('/login', {
          templateUrl: 'views/login.html'        
      }).
      when('/usersettings', {
          templateUrl: 'restricted/usersettings.html'
        
      }).
      when('/staffsettings', {
          templateUrl: 'restricted/staffsettings.html'
        
      }).
      when('/registercheckout', {
          templateUrl: 'restricted/checkoutform.html'        
      }).
      when('/registercheckout/:subCollectionId', {
          templateUrl: 'restricted/checkoutform.html'
        
      }).
      when('/managecheckouts', {
          templateUrl: 'restricted/checkInCheckOut.html'
        
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

app.service('DropDownValueService', ['$q','$http',function($q,$http) {
	
    this.getUsers = function() {
    	return $q(function(resolve, reject) {
    		$http.get('getUsers.do')     
    	     .success(function(data) {
    	       resolve(data);       
    	        
    	     })
    	     .error(function(data, status) {    	
    	    	 reject(data,status);    	       
    	     }) 
   	 	},1000);
    };
    	    	    
        
    this.getStaffs = function() {    	
    	return $q(function(resolve, reject) {
    		$http.get('getStaffs.do')     
    	     .success(function(data) {
    	       resolve(data);       
    	        
    	     })
    	     .error(function(data, status) {    	
    	    	 reject(data,status);    	       
    	     })
   		 
   	      
   	 	},1000);       
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
        
        
   
     
     this.getLocations = function() {
         return ['G1-L2','G2-L3','G4-L5'];
      };  
      
      this.getStorageType = function(){
    	  return ['STANDARD','ARCHIVE','HAZARDOUS/ SPECIAL','FLUIDS','PETROLEUM','FLOOR'];
      }
      
      this.getSampleType = function(){
    	  return ['ROCK','SAND','LIME'];
      }
      
      this.getDatum = function(){
    	  return [{
    		  display : 'WGC84/EPSG:4326',
    		  value : 'EPSG:4326'
    	  },{
    		  display : 'GDA94/MGA Zone 48',
    		  value : 'EPSG:28348'
    	  },{
    		  display : 'GDA94/MGA Zone 49',
    		  value : 'EPSG:28349'
    	  },{
    		  display : 'GDA94/MGA Zone 50',
    		  value : 'EPSG:28350'
    	  },{
    		  display : 'GDA94/MGA Zone 51',
    		  value : 'EPSG:28351'
    	  },{
    		  display : 'GDA94/MGA Zone 52',
    		  value : 'EPSG:28352'
    	  },{
    		  display : 'GDA94/MGA Zone 53',
    		  value : 'EPSG:28353'
    	  },{
    		  display : 'GDA94/MGA Zone 54',
    		  value : 'EPSG:28354'
    	  },{
    		  display : 'GDA94/MGA Zone 55',
    		  value : 'EPSG:28355'
    	  },{
    		  display : 'GDA94/MGA Zone 56',
    		  value : 'EPSG:28356'
    	  },{
    		  display : 'GDA94/MGA Zone 57',
    		  value : 'EPSG:28357'
    	  },{
    		  display : 'GDA94/MGA Zone 58',
    		  value : 'EPSG:28358'
    	  }];
    	  
      }
      
      this.getZone = function(){
    	  return ['Zone 48','Zone 51','Zone 52','Zone 53','Zone 54','Zone 55','Zone 56','Zone 57',,'Zone 58'];
      }
        
}]);

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


app.service('ViewUserInfoService',['$modal','$q','modalService','currentAuthService',function ($modal,$q,modalService,currentAuthService) {
	//VT: GOOGLE MAP MODALS
     this.viewUser = function(userName){
    	 
    	 if(currentAuthService.getStatus().authenticated){
    		 var modalInstance = $modal.open({
    	         animation: true,
    	         templateUrl: 'widget/ViewUserInfoModal.html',
    	         controller: 'ViewUserInfoCtrl',
    	         size: 'lg',
    	         resolve: {
    	             params: function () {
    	               return {
    	              	 name :userName	              	
    	               }
    	             }
    	           }
    	       });
    	 }else{
    		 modalService.showModal({}, {    	            	           
  	           headerText: "Unauthorized Access",
  	           bodyText: "You have to be logged in to view the user details"
    		 });	   
    	 }
    	 
    	
     }
     
     this.viewStaff = function(userName){
    	 if(currentAuthService.getStatus().authenticated){
    		 var modalInstance = $modal.open({
    	         animation: true,
    	         templateUrl: 'widget/ViewUserInfoModal.html',
    	         controller: 'ViewStaffInfoCtrl',
    	         size: 'lg',
    	         resolve: {
    	        	 params: function () {
    	               return {
    	              	 name :userName	              	
    	               }
    	             }
    	           }
    	       });
    	 }else{
    		 modalService.showModal({}, {    	            	           
    	           headerText: "Unauthorized Access",
    	           bodyText: "You have to be logged in to view the user details"
      		 });	   
    	 }    	 
     }
}])

app.service('ViewHistoryService',['$modal','$q','modalService','currentAuthService',function ($modal,$q,modalService,currentAuthService) {
	//VT: GOOGLE MAP MODALS
     this.viewCollectionHistory = function(collectionId){
    	 
    	 if(currentAuthService.getStatus().authenticated){
    		 var modalInstance = $modal.open({
    	         animation: true,
    	         templateUrl: 'widget/ViewCollectionHistoryModal.html',
    	         controller: 'ViewCollectionHistoryCtrl',
    	         size: 'lg',
    	         resolve: {
    	             params: function () {
    	               return {
    	              	 collectionId :collectionId	              	
    	               }
    	             }
    	           }
    	       });
    	 }else{
    		 modalService.showModal({}, {    	            	           
  	           headerText: "Unauthorized Access",
  	           bodyText: "You have to be logged in to view the history details"
    		 });	   
    	 }
     }
     
     this.viewSubCollectionHistory = function(subCollectionId){
    	 
    	 if(currentAuthService.getStatus().authenticated){
    		 var modalInstance = $modal.open({
    	         animation: true,
    	         templateUrl: 'widget/ViewSubcollectionHistoryModal.html',
    	         controller: 'ViewSubcollectionHistoryCtrl',
    	         size: 'lg',
    	         resolve: {
    	             params: function () {
    	               return {
    	            	  subCollectionId :subCollectionId	              	
    	               }
    	             }
    	           }
    	       });
    	 }else{
    		 modalService.showModal({}, {    	            	           
  	           headerText: "Unauthorized Access",
  	           bodyText: "You have to be logged in to view the history details"
    		 });	   
    	 }
     }
     
     this.viewSampleHistory = function(sampleId){
    	 
    	 if(currentAuthService.getStatus().authenticated){
    		 var modalInstance = $modal.open({
    	         animation: true,
    	         templateUrl: 'widget/ViewSampleHistoryModal.html',
    	         controller: 'ViewSampleHistoryCtrl',
    	         size: 'lg',
    	         resolve: {
    	             params: function () {
    	               return {
    	              	 sampleId :sampleId	              	
    	               }
    	             }
    	           }
    	       });
    	 }else{
    		 modalService.showModal({}, {    	            	           
  	           headerText: "Unauthorized Access",
  	           bodyText: "You have to be logged in to view the history details"
    		 });	   
    	 }
     }
     
     this.viewCheckoutHistory = function(subcollectionId){
    	 
    	 if(currentAuthService.getStatus().authenticated){
    		 var modalInstance = $modal.open({
    	         animation: true,
    	         templateUrl: 'widget/ViewCheckoutHistoryModal.html',
    	         controller: 'ViewCheckoutHistoryCtrl',
    	         size: 'lg',
    	         resolve: {
    	             params: function () {
    	               return {
    	            	   subcollectionId :subcollectionId	              	
    	               }
    	             }
    	           }
    	       });
    	 }else{
    		 modalService.showModal({}, {    	            	           
  	           headerText: "Unauthorized Access",
  	           bodyText: "You have to be logged in to view the history details"
    		 });	   
    	 }
     }
     

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

