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

allControllers.controller('ViewUserInfoCtrl', function ($scope, $modalInstance, params, modalService,$http) {
	
	$scope.form={};
	$scope.title = "User Information"

	$http.get('getUserByName.do',{
		params : {
			contactName : params.name
		}
	})     
    .success(function(data) {
    	$scope.form = data;      
       
    })
    .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error retrieving user list",
	           bodyText: data
		 });	       
    }) 
    $scope.ok = function () {	
		 $modalInstance.close();		 
	 };
	  
});

allControllers.controller('ViewStaffInfoCtrl', function ($scope, $modalInstance, params, modalService,$http) {
	
	$scope.form={};
	$scope.title = "Staff Information"

	$http.get('getStaffByName.do',{
		params : {
			contactName : params.name
		}
	})     
    .success(function(data) {
    	$scope.form = data;      
       
    })
    .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error retrieving user list",
	           bodyText: data
		 });	       
    }) 
    
    
    $scope.ok = function () {	
		 $modalInstance.close();		 
	 };
	
	  
});

allControllers.controller('ViewCollectionHistoryCtrl', function ($scope, $modalInstance, params, modalService,$http) {
	
	$scope.collections={};
	$scope.title = "Collection History"

	$http.get('getCollectionAudit.do',{
		params : {
			collectionId : params.collectionId
		}
	})     
    .success(function(data) {
    	$scope.collections = data;      
       
    })
    .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error retrieving collection history",
	           bodyText: data
		 });	       
    }) 
    
    
    $scope.ok = function () {	
		 $modalInstance.close();		 
	 };
	
	  
});

allControllers.controller('ViewSubcollectionHistoryCtrl', function ($scope, $modalInstance, params, modalService,$http) {
	
	$scope.subCollections={};
	$scope.title = "Collection History"

	$http.get('getSubCollectionAudit.do',{
		params : {
			subCollectionId : params.subCollectionId
		}
	})     
    .success(function(data) {
    	$scope.subCollections = data;      
       
    })
    .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error retrieving sub-collection history",
	           bodyText: data
		 });	       
    }) 
    
    
    $scope.ok = function () {	
		 $modalInstance.close();		 
	 };
	
	  
});

allControllers.controller('ViewSampleHistoryCtrl', function ($scope, $modalInstance, params, modalService,$http) {
	
	$scope.samples={};
	$scope.title = "Collection History"

	$http.get('getSampleAudit.do',{
		params : {
			id : params.sampleId
		}
	})     
    .success(function(data) {
    	$scope.samples = data;      
       
    })
    .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error retrieving sample history",
	           bodyText: data
		 });	       
    }) 
    
    
    $scope.ok = function () {	
		 $modalInstance.close();		 
	 };
	
	  
});


allControllers.controller('SearchCollectionCtrl', function ($scope,DropDownValueService, $modalInstance,$filter,spinnerService,$http,modalService) {
	
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false, enableColumnResizing: true };
	
	DropDownValueService.getUsers()
	.then(function(data) {
		 $scope.users= data;
	}, function(data, status) {
		 modalService.showModal({}, {    	            	           
	           headerText: "Error retrieving user list",
	           bodyText: data
		 });
	});
	
	
	DropDownValueService.getStaffs()
	.then(function(data) {
		 $scope.staffs= data;
	}, function(data, status) {
		modalService.showModal({}, {    	            	           
	           headerText: "Error retrieving staff list",
	           bodyText: data
		 });
	});
	
	
	$scope.booleans = DropDownValueService.getBoolean();
	$scope.gridOptions.data = [];
	$scope.form ={};
	
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
     
   };
   
   $scope.searchCollection = function(){
		 spinnerService.show('searchCollection.grid');
		 var params ={	
					project: $scope.form.project,
					staffIdFieldManager:$scope.form.staffIdFieldManager,
					staffidResponsible: $scope.form.staffidResponsible,
					projectPublication : $scope.form.projectPublication
		 }
			
			//VT: Actual results
		$http.get('searchCollections.do',{
			params:params
		 })   
 	     .success(function(data) {
 	    	 $scope.gridOptions.data = data;
 	         spinnerService.hide('searchCollection.grid') 	              	        
 	     })
 	     .error(function(data, status) {    	
 	    	 modalService.showModal({}, {    	            	           
 		           headerText: "Error loading data:" + status ,
 		           bodyText: "Please contact cg-admin@csiro.au if this persist"
 		        	   
 	    	 });
 	    	spinnerService.hide('searchCollection.grid') 	      
 	     })
	 }
	
	

	 $scope.ok = function () {
		 spinnerService._unregister('searchCollection.grid')
		 $modalInstance.close($scope.selectedCollection);
	 };
	 
	 $scope.cancel = function () {
		 spinnerService._unregister('searchCollection.grid')
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
	$scope.form ={};
	
	$scope.gridOptions.columnDefs = [	                              
                                 	 { field: 'id',displayName: 'id',width:50 },
	                                 { field: 'subcollectionId',displayName: 'subCollection id',width:150 },
	                                 { field: "rsCollection['collectionId']",displayName: 'collection Id', width:150 },
	                                 { field: "rsCollection['project']",displayName: 'Project', width:150 },
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
        
   };
   
   $scope.searchSubCollection = function(){
	   spinnerService.show('searchSubCollection.grid');
	   var params ={	
   			collectionId: $scope.form.collectionId,
   			project : $scope.form.project,
   			oldId : $scope.form.oldId,
   			locationInStorage:$scope.form.locationInStorage,
   			storageType: $scope.form.storageType, 			
			source : $scope.form.source,
			pageNumber:$scope.currentPages,
			pageSize:10
		}
		
		//VT: Actual results
		$http.get('searchSubCollections.do',{
			params:params
		 })     
	     .success(function(data) {
	    	 $scope.gridOptions.data = data;
		     spinnerService.hide('searchSubCollection.grid')
	        
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Error loading data:" + status ,
		           bodyText: "Please contact cg-admin@csiro.au if this persist"
	    	 });
	    	 spinnerService.hide('searchSubCollection.grid')
	     })
   }
	
	

	 $scope.ok = function () {
		 spinnerService._unregister('searchSubCollection.grid')
		 $modalInstance.close($scope.selectedSubCollection);
		 
	 };
	 
	 $scope.cancel = function () {
		 spinnerService._unregister('searchSubCollection.grid')
		 $modalInstance.dismiss('cancel');
	 };
	  
});