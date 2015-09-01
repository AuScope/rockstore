var allControllers = angular.module('allControllers', []);

//VT: these are service level controllers 

allControllers.controller('MapModalInstanceCtrl', function ($scope, $modalInstance, marker, uiGmapIsReady,uiGmapGoogleMapApi ) {

	$scope.initialize=function() {
		 $scope.map = { center: { latitude: marker.latitude, longitude: marker.longitude }, zoom: 8 };
		  
		  $scope.marker = {	
				  id : 0,
			      coords: {
			        latitude: marker.latitude,
			        longitude: marker.longitude
			      }	      
		  };
		
		  $scope.windowOptions = {
		       visible: false
		  };
		  $scope.onClick = function() {
	        $scope.windowOptions.visible = !$scope.windowOptions.visible;
		  };
		  $scope.closeClick = function() {
	        $scope.windowOptions.visible = false;
	      };
	      $scope.ok = function () {
		    $modalInstance.close();
		  };
		  $scope.cancel = function () {
		    $modalInstance.dismiss('cancel');
		  };
		  
		  $scope.info=marker.info

		  uiGmapIsReady.promise()
		    .then(function (map_instances) {
		    	var map = map_instances[0].map
		    	google.maps.event.trigger(map, 'resize');
		    	map.setCenter(new google.maps.LatLng($scope.map.center.latitude,$scope.map.center.longitude));
		    });

		  
		 
    }

    $scope.initialize();
	  
});


allControllers.controller('SearchCollectionCtrl', function ($scope,DropDownValueService, $modalInstance,$filter,spinnerService,$http) {
	
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false, enableColumnResizing: true };
	
	$scope.users = DropDownValueService.getUsers();	
	$scope.booleans = DropDownValueService.getBoolean();
	$scope.gridOptions.data = [];
	
	$scope.gridOptions.columnDefs = [	                                                                 	
	                                 { field: 'collectionId',displayName: 'collection id',width:150 },
	                                 { field: 'project',displayName: 'project',width:130},
	                                 { field: 'staffIdFieldManager',displayName: 'Field Manager',width:180},
	                                 { field: 'staffidResponsible',displayName: 'Staff Responsible',width:155 },
	                                 { field: 'projectResult',displayName: 'Project Result',width:150 },
	                                 { field: 'projectPublication',displayName: 'publication',width:170 },
	                                 { field: 'projectCloseDate',displayName: 'Close Date',cellFilter :'date:"d/MMM/yyyy"',width:170 },
	                                 { field: 'availableToPublic',displayName: 'public',width:170 },
	                                 { field: 'archiveDue',displayName: 'Archive Due', cellFilter :'date:"d/MMM/yyyy"', width:150 }
	                               ];
	                              
   $scope.gridOptions.multiSelect = false;
   $scope.gridOptions.modifierKeysToMultiSelect = false;
   $scope.gridOptions.noUnselect = true;
   $scope.gridOptions.onRegisterApi = function( gridApi ) {
     $scope.gridApi = gridApi;       
     gridApi.selection.on.rowSelectionChanged($scope, function(){
    	var selectedRow=jQuery.extend(true, {}, $scope.gridApi.selection.getSelectedRows()[0]);    	
    	 $scope.selectedCollection = selectedRow.collectionId
     });
     
     spinnerService.show('searchCollection.grid');
     $http.get('getCollections.do')     
     .success(function(data) {
       $scope.gridOptions.data = data;
       spinnerService.hide('searchCollection.grid')
       spinnerService._unregister('searchCollection.grid')
        
     })
     .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
       
        spinnerService.hide('searchCollection.grid')
        spinnerService._unregister('searchCollection.grid')
     })
     
   };
	
	

	 $scope.ok = function () {
		 $modalInstance.close($scope.selectedCollection);
	 };
	 
	 $scope.cancel = function () {
		    $modalInstance.dismiss('cancel');
	 };
	  
});


allControllers.controller('SearchSubCollectionCtrl', function ($scope,DropDownValueService, $modalInstance,$filter,spinnerService,$http) {
	
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false, enableColumnResizing: true };
	
	$scope.booleans = DropDownValueService.getBoolean();
	$scope.locations = DropDownValueService.getLocations();
	$scope.storageTypes = DropDownValueService.getStorageType();
	$scope.users = DropDownValueService.getUsers();	
	$scope.booleans = DropDownValueService.getBoolean();
	

	$scope.gridOptions.data = [];	
	
	$scope.gridOptions.columnDefs = [	                              
                                 	 { field: 'id',displayName: 'id',width:50 },
	                                 { field: 'subcollectionId',displayName: 'subCollection id',width:150 },
	                                 { field: "rsCollection['collectionId']",displayName: 'collection Id', width:150 },
	                                 { field: 'locationInStorage',displayName: 'Store loc',width:150 },	                                 
	                                 { field: 'oldId',displayName: 'old ID',width:130},
	                                 { field: "sampleRangeBySubcollection['minContainer']",displayName: 'Container From',width:180},
	                                 { field: "sampleRangeBySubcollection['maxContainer']",displayName: 'Container To',width:155 },
	                                 { field: "sampleRangeBySubcollection['minSample']",displayName: 'Sample From',width:150 },
	                                 { field: "sampleRangeBySubcollection['maxSample']",displayName: 'Sample To',width:170 },
	                                 { field: 'storageType',displayName: 'Storage Type',width:170 },
	                                 { field: 'hazardous',displayName: 'Hazardous',width:170 },
	                                 { field: 'source',displayName: 'source',width:170 },
	                                 { field: 'totalPallet',displayName: 'Pallet Count',width:170 }	                                 
	                               ];
	                              
   $scope.gridOptions.multiSelect = false;
   $scope.gridOptions.modifierKeysToMultiSelect = false;
   $scope.gridOptions.noUnselect = true;
   $scope.gridOptions.onRegisterApi = function( gridApi ) {
     $scope.gridApi = gridApi;       
     gridApi.selection.on.rowSelectionChanged($scope, function(){
    	var selectedRow=jQuery.extend(true, {}, $scope.gridApi.selection.getSelectedRows()[0]);    	
    	 $scope.selectedSubCollection = selectedRow.subcollectionId;
     });
     
     spinnerService.show('searchSubCollection.grid');
     $http.get('getSubCollections.do') 
     .success(function(data) {
       $scope.gridOptions.data = data;
       spinnerService.hide('searchSubCollection.grid')
       spinnerService._unregister('searchSubCollection.grid')
     })
     .error(function(data, status) {
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
        spinnerService.hide('searchSubCollection.grid')
        spinnerService._unregister('searchSubCollection.grid')
     })
     
   };
	
	

	 $scope.ok = function () {
		 $modalInstance.close($scope.selectedSubCollection);
	 };
	 
	 $scope.cancel = function () {
		    $modalInstance.dismiss('cancel');
	 };
	  
});