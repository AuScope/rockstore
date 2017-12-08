var app = angular.module('app', ['ngRoute','allControllers','ui.grid','ui.grid.pagination','ui.grid.selection','ui.grid.resizeColumns','ui.bootstrap','angularSpinners','uiGmapgoogle-maps','ngAnimate','monospaced.qrcode','ngFileUpload']);

app.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
    	  redirectTo: '/browsecollections'
      
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
      when('/rolesettings', {
          templateUrl: 'restricted/rolesettings.html'
        
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
      when('/import', {
          templateUrl: 'restricted/importExcel.html'
        
      }).
      when('/batchlog', {
          templateUrl: 'restricted/viewBatchLog.html'
        
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
            return ['E10-L1',
                    'E10-L2',
                    'E10-L3',
                    'E10-L4',
                    'E10-L5',
                    'E11-L1',
                    'E11-L2',
                    'E11-L3',
                    'E11-L4',
                    'E11-L5',
                    'E12-L1',
                    'E12-L2',
                    'E12-L3',
                    'E12-L4',
                    'E12-L5',
                    'E13-L1',
                    'E13-L2',
                    'E13-L3',
                    'E13-L4',
                    'E13-L5',
                    'E14-L1',
                    'E14-L2',
                    'E14-L3',
                    'E14-L4',
                    'E14-L5',
                    'E15-L1',
                    'E15-L2',
                    'E15-L3',
                    'E15-L4',
                    'E15-L5',
                    'E16-L1',
                    'E16-L2',
                    'E16-L3',
                    'E16-L4',
                    'E16-L5',
                    'E17-L1',
                    'E17-L2',
                    'E17-L3',
                    'E17-L4',
                    'E17-L5',
                    'E2-L1',
                    'E2-L2',
                    'E2-L3',
                    'E2-L4',
                    'E2-L5',
                    'E3-L1',
                    'E3-L2',
                    'E3-L3',
                    'E3-L4',
                    'E3-L5',
                    'E4-L1',
                    'E4-L2',
                    'E4-L3',
                    'E4-L4',
                    'E4-L5',
                    'E5-L1',
                    'E5-L2',
                    'E5-L3',
                    'E5-L4',
                    'E5-L5',
                    'E6-L1',
                    'E6-L2',
                    'E6-L3',
                    'E6-L4',
                    'E6-L5',
                    'E7-L1',
                    'E7-L2',
                    'E7-L3',
                    'E7-L4',
                    'E7-L5',
                    'E8-L1',
                    'E8-L2',
                    'E8-L3',
                    'E8-L4',
                    'E8-L5',
                    'E9-L1',
                    'E9-L2',
                    'E9-L3',
                    'E9-L4',
                    'E9-L5',
                    'F10-L1',
                    'F10-L2',
                    'F10-L3',
                    'F10-L4',
                    'F10-L5',
                    'F11-L1',
                    'F11-L2',
                    'F11-L3',
                    'F11-L4',
                    'F11-L5',
                    'F12-L1',
                    'F12-L2',
                    'F12-L3',
                    'F12-L4',
                    'F12-L5',
                    'F13-L1',
                    'F13-L2',
                    'F13-L3',
                    'F13-L4',
                    'F13-L5',
                    'F14-L1',
                    'F14-L2',
                    'F14-L3',
                    'F14-L4',
                    'F14-L5',
                    'F15-L1',
                    'F15-L2',
                    'F15-L3',
                    'F15-L4',
                    'F15-L5',
                    'F16-L1',
                    'F16-L2',
                    'F16-L3',
                    'F16-L4',
                    'F16-L5',
                    'F17-L1',
                    'F17-L2',
                    'F17-L3',
                    'F17-L4',
                    'F17-L5',
                    'F2-L1',
                    'F2-L2',
                    'F2-L3',
                    'F2-L4',
                    'F2-L5',
                    'F3-L1',
                    'F3-L2',
                    'F3-L3',
                    'F3-L4',
                    'F3-L5',
                    'F4-L1',
                    'F4-L2',
                    'F4-L3',
                    'F4-L4',
                    'F4-L5',
                    'F5-L1',
                    'F5-L2',
                    'F5-L3',
                    'F5-L4',
                    'F5-L5',
                    'F6-L1',
                    'F6-L2',
                    'F6-L3',
                    'F6-L4',
                    'F6-L5',
                    'F7-L1',
                    'F7-L2',
                    'F7-L3',
                    'F7-L4',
                    'F7-L5',
                    'F8-L1',
                    'F8-L2',
                    'F8-L3',
                    'F8-L4',
                    'F8-L5',
                    'F9-L1',
                    'F9-L2',
                    'F9-L3',
                    'F9-L4',
                    'F9-L5',
                    'G10-L1',
                    'G10-L2',
                    'G10-L3',
                    'G10-L4',
                    'G10-L5',
                    'G11-L1',
                    'G11-L2',
                    'G11-L3',
                    'G11-L4',
                    'G11-L5',
                    'G12-L1',
                    'G12-L2',
                    'G12-L3',
                    'G12-L4',
                    'G12-L5',
                    'G13-L1',
                    'G13-L2',
                    'G13-L3',
                    'G13-L4',
                    'G13-L5',
                    'G14-L1',
                    'G14-L2',
                    'G14-L3',
                    'G14-L4',
                    'G14-L5',
                    'G15-L1',
                    'G15-L2',
                    'G15-L3',
                    'G15-L4',
                    'G15-L5',
                    'G16-L1',
                    'G16-L2',
                    'G16-L3',
                    'G16-L4',
                    'G16-L5',
                    'G17-L1',
                    'G17-L2',
                    'G17-L3',
                    'G17-L4',
                    'G17-L5',
                    'G18-L1',
                    'G18-L2',
                    'G18-L3',
                    'G18-L4',
                    'G18-L5',
                    'G19-L1',
                    'G19-L2',
                    'G19-L3',
                    'G19-L4',
                    'G19-L5',
                    'G1-L1',
                    'G1-L2',
                    'G1-L3',
                    'G1-L4',
                    'G1-L5',
                    'G20-L1',
                    'G20-L2',
                    'G20-L3',
                    'G20-L4',
                    'G20-L5',
                    'G21-L1',
                    'G21-L2',
                    'G21-L3',
                    'G21-L4',
                    'G21-L5',
                    'G22-L1',
                    'G22-L2',
                    'G22-L3',
                    'G22-L4',
                    'G22-L5',
                    'G23-L1',
                    'G23-L2',
                    'G23-L3',
                    'G23-L4',
                    'G23-L5',
                    'G2-L1',
                    'G2-L2',
                    'G2-L3',
                    'G2-L4',
                    'G2-L5',
                    'G3-L1',
                    'G3-L2',
                    'G3-L3',
                    'G3-L4',
                    'G3-L5',
                    'G4-L1',
                    'G4-L2',
                    'G4-L3',
                    'G4-L4',
                    'G4-L5',
                    'G5-L1',
                    'G5-L2',
                    'G5-L3',
                    'G5-L4',
                    'G5-L5',
                    'G6-L1',
                    'G6-L2',
                    'G6-L3',
                    'G6-L4',
                    'G6-L5',
                    'G7-L1',
                    'G7-L2',
                    'G7-L3',
                    'G7-L4',
                    'G7-L5',
                    'G8-L1',
                    'G8-L2',
                    'G8-L3',
                    'G8-L4',
                    'G8-L5',
                    'G9-L1',
                    'G9-L2',
                    'G9-L3',
                    'G9-L4',
                    'G9-L5',
                    'H0-L1',
                    'H0-L2',
                    'H0-L3',
                    'H0-L4',
                    'H0-L5',
                    'H10-L1',
                    'H10-L2',
                    'H10-L3',
                    'H10-L4',
                    'H10-L5',
                    'H11-L1',
                    'H11-L2',
                    'H11-L3',
                    'H11-L4',
                    'H11-L5',
                    'H12-L1',
                    'H12-L2',
                    'H12-L3',
                    'H12-L4',
                    'H12-L5',
                    'H13-L1',
                    'H13-L2',
                    'H13-L3',
                    'H13-L4',
                    'H13-L5',
                    'H14-L1',
                    'H14-L2',
                    'H14-L3',
                    'H14-L4',
                    'H14-L5',
                    'H15-L1',
                    'H15-L2',
                    'H15-L3',
                    'H15-L4',
                    'H15-L5',
                    'H16-L1',
                    'H16-L2',
                    'H16-L3',
                    'H16-L4',
                    'H16-L5',
                    'H17-L1',
                    'H17-L2',
                    'H17-L3',
                    'H17-L4',
                    'H17-L5',
                    'H18-L1',
                    'H18-L2',
                    'H18-L3',
                    'H18-L4',
                    'H18-L5',
                    'H19-L1',
                    'H19-L2',
                    'H19-L3',
                    'H19-L4',
                    'H19-L5',
                    'H1-L1',
                    'H1-L2',
                    'H1-L3',
                    'H1-L4',
                    'H1-L5',
                    'H20-L1',
                    'H20-L2',
                    'H20-L3',
                    'H20-L4',
                    'H20-L5',
                    'H21-L1',
                    'H21-L2',
                    'H21-L3',
                    'H21-L4',
                    'H21-L5',
                    'H22-L1',
                    'H22-L2',
                    'H22-L3',
                    'H22-L4',
                    'H22-L5',
                    'H23-L1',
                    'H23-L2',
                    'H23-L3',
                    'H23-L4',
                    'H23-L5',
                    'H2-L1',
                    'H2-L2',
                    'H2-L3',
                    'H2-L4',
                    'H2-L5',
                    'H3-L1',
                    'H3-L2',
                    'H3-L3',
                    'H3-L4',
                    'H3-L5',
                    'H4-L1',
                    'H4-L2',
                    'H4-L3',
                    'H4-L4',
                    'H4-L5',
                    'H5-L1',
                    'H5-L2',
                    'H5-L3',
                    'H5-L4',
                    'H5-L5',
                    'H6-L1',
                    'H6-L2',
                    'H6-L3',
                    'H6-L4',
                    'H6-L5',
                    'H7-L1',
                    'H7-L2',
                    'H7-L3',
                    'H7-L4',
                    'H7-L5',
                    'H8-L1',
                    'H8-L2',
                    'H8-L3',
                    'H8-L4',
                    'H8-L5',
                    'H9-L1',
                    'H9-L2',
                    'H9-L3',
                    'H9-L4',
                    'H9-L5',
                    'WS-A1-L1',
                    'WS-A1-L2',
                    'WS-A1-L3',
                    'WS-B1-L1',
                    'WS-B1-L2',
                    'WS-B1-L3',
                    'WS-A2-L1',
                    'WS-A2-L2',
                    'WS-A2-L3',
                    'WS-A3-L1',
                    'WS-A3-L2',
                    'WS-A3-L3',
                    'WS-A4-L1',
                    'WS-A4-L2',
                    'WS-A4-L3',
                    'WS-A5-L1',
                    'WS-A5-L2',
                    'WS-A5-L3',
                    'WS-A6-L1',
                    'WS-A6-L2',
                    'WS-A6-L3',
                    'WS-B2-L1',
                    'WS-B2-L2',
                    'WS-B2-L3',
                    'WS-B3-L1',
                    'WS-B3-L2',
                    'WS-B3-L3',
                    'WS-B4-L1',
                    'WS-B4-L2',
                    'WS-B4-L3',
                    'WS-B5-L1',
                    'WS-B5-L2',
                    'WS-B5-L3',
                    'WS-B6-L1',
                    'WS-B6-L2',
                    'WS-B6-L3',
                    'WS-FA-1',
                    'WS-FA-2',
                    'WS-FA-3',
                    'WS-FA-4',
                    'WS-FB-1',
                    'WS-FB-2',
                    'WS-FB-3',
                    'WS-FB-4',
                    'DL-T1-A',
                    'DL-T1-B',
                    'DL-T2-A',
                    'DL-T2-B',
                    'DL-T3-A',
                    'DL-T3-B',
                    'DL-S1-A',
                    'DL-S1-B',
                    'DL-A-L1',
                    'DL-A-L2',
                    'DL-A-L3',
                    'DL-A-L4',
                    'DL-B-L1',
                    'DL-B-L2',
                    'DL-B-L3',
                    'DL-B-L4',
                    'DL-C-L1',
                    'DL-C-L2',
                    'DL-C-L3',
                    'DL-C-L4',
                    'DL-D-L1',
                    'DL-D-L2',
                    'DL-D-L3',
                    'DL-D-L4',
                    'DL-E-L1',
                    'DL-E-L2',
                    'DL-E-L3',
                    'DL-E-L4',
                    'DL-E-L5',
                    'DL-E-L6',
                    'DL-E-L7',
                    'DL-E-L8',
                    'DL-F-L1',
                    'DL-F-L2',
                    'DL-F-L3',
                    'DL-F-L4',
                    'DL-F-L5',
                    'DL-F-L6',
                    'DL-F-L7',
                    'DL-F-L8',
                    'DL-G-L1',
                    'DL-G-L2',
                    'DL-G-L3',
                    'DL-G-L4',
                    'DL-G-L5',
                    'DL-G-L6',
                    'DL-G-L7',
                    'DL-G-L8',
                    'DL-H-L1',
                    'DL-H-L2',
                    'DL-H-L3',
                    'DL-H-L4',
                    'DL-H-L5',
                    'DL-H-L6',
                    'DL-H-L7',
                    'DL-H-L8'];
            
         };  
      
      this.getStorageType = function(){
    	  return ['STANDARD','ARCHIVE','HAZARDOUS/ SPECIAL','FLUIDS','PETROLEUM','FLOOR'];
      }
      
      this.getSampleType = function(){
    	  return ['unknown',
    	          'Chips - in trays',
    	          'Reference',
    	          'Crushed',
  				  'Core',
    	          'Minus 1 mm',
    	          'Laterite',
    	          'Hand',
    	          'Grab',
    	          'Mineralised Separate',
    	          'Bulk',
    	          'Pulp',
    	          'Superpan Light Minerals',
    	          'Mineralised',
    	          'Hand sample-surface',
    	          'Hand sample–underground'];
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
        setName : function(name){
        	status.name=name;
        },
        getName : function(){
        	return status.name;
        },
        setPermissions : function(permissions){
        	status.permissions=permissions;
        },
        getPermissions : function(){
        	return status.permissions;
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

