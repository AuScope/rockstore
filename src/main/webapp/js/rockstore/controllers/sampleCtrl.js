allControllers.controller('SampleCtrl', ['$scope','$rootScope','$http','DropDownValueService','$filter','spinnerService','modalService','SearchSubCollectionService','$routeParams',
                                                    function ($scope,$rootScope,$http,DropDownValueService,$filter,spinnerService,modalService,SearchSubCollectionService,$routeParams) {
	
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false, enableColumnResizing: true };	
	$scope.gridOptions.data = [];
	
	DropDownValueService.getUsers()
	.then(function(data) {
		 $scope.users= data;
	}, function(data, status) {
		 modalService.showModal({}, {    	            	           
	           headerText: "Error retrieve user list",
	           bodyText: data
		 });
	});
	
	
	DropDownValueService.getStaffs()
	.then(function(data) {
		 $scope.staffs= data;
	}, function(data, status) {
		modalService.showModal({}, {    	            	           
	           headerText: "Error retrieve staff list",
	           bodyText: data
		 });
	});
	
	$scope.sampleTypes = DropDownValueService.getSampleType();
	$scope.booleans = DropDownValueService.getBoolean();
	$scope.datums = DropDownValueService.getDatum();
	
	$scope.form ={};
	
	$scope.resetForm = function(){
		$scope.form ={										
				rsSubcollection:  $scope.form.rsSubcollection?$scope.form.rsSubcollection:[]
		    }
		$scope.gridApi.selection.clearSelectedRows();
	}
	
	$scope.submit = function(){
		if(!$scope.form.rsSubcollection || !$scope.form.rsSubcollection['subcollectionId'] ){
			modalService.showModal({}, {    	            	           
		           headerText: "Sub-collection Id not set",
		           bodyText: "Make sure you choose a valid sub-collection id"
	    	 });
			return;
		}
		
		$http.get('sampleAddUpdate.do', {
			params:{	
				id : $scope.form.id,
				subcollectionId: $scope.form.rsSubcollection['subcollectionId'],				
				csiroSampleId: $scope.form.csiroSampleId,				
				sampleType: $scope.form.sampleType,
				bhid: $scope.form.bhid,
				depth: $scope.form.depth,
				datum: $scope.form.datum,				
				containerId : $scope.form.containerId,
				externalRef : $scope.form.externalRef,
				sampleCollector : $scope.form.sampleCollector,
				dateSampled : $scope.form.dateSampled,
				sampleDispose : $scope.form.sampleDispose,
				dateDisposed : $scope.form.dateDisposed,
				staffidDisposed : $scope.form.staffidDisposed,
				origLat : $scope.form.origLat,
				origLon : $scope.form.origLon								
				}
		})
		.then(function(response) {		
			response.data.dateSampled=$filter('date')(response.data.dateSampled,'d/MMM/yyyy');
			response.data.dateDisposed=$filter('date')(response.data.dateDisposed,'d/MMM/yyyy');						
			if($scope.form.id){//VT if ID exist, we assume it is a update else it is a insert new
				$.extend($scope.gridApi.selection.getSelectedRows()[0],response.data)		
				 modalService.showModal({}, {    	            	           
			           headerText: "Record Update",
			           bodyText: "Your record has successfully updated"
		    	 });
			}else{				
				$scope.gridOptions.data.push(response.data)
				$scope.resetForm();		
			}
			
			
		  }, function(response) {
			  modalService.showModal({}, {    	            	           
		           headerText: response.data.header,
		           bodyText: response.data.message
	    	 });
		  });
		
	}
	
	$scope.gridOptions.columnDefs = [	                              
                                 	 { field: 'id',displayName: 'id',width:50 },                                 	
	                                 { field: "rsSubcollection['subcollectionId']",displayName: 'subCollection id',width:150 },	                                 
	                                 { field: 'igsn',displayName: 'igsn',width:150 },	                                 
	                                 { field: 'csiroSampleId',displayName: 'CSIRO SampleId',width:150},
	                                 { field: "sampleType",displayName: 'Sample Type',width:140},	                            
	                                 { field: 'bhid',displayName: 'Borehole ID',width:170 },
	                                 { field: 'depth',displayName: 'Depth',width:110 },
	                                 { field: 'datum',displayName: 'Datum',width:120 },	                                
	                                 { field: 'containerId',displayName: 'ContainerId',width:170 },
	                                 { field: 'externalRef',displayName: 'externalRef',width:170 },
	                                 { field: 'sampleCollector',displayName: 'Sample Collector',width:170 },
	                                 { field: 'dateSampled',displayName: 'Date Sampled', cellFilter :'date:"d/MMM/yyyy"', width:170 },
	                                 { field: 'sampleDispose',displayName: 'Sample Dispose',width:170 },
	                                 { field: 'dateDisposed',displayName: 'Date Disposed', cellFilter :'date:"d/MMM/yyyy"', width:170 },
	                                 { field: 'staffidDisposed',displayName: 'Staff Disposed',width:170 },
	                                 { field: 'origLat',displayName: 'Latitude',width:120 },
	                                 { field: 'origLon',displayName: 'Longtitude',width:120 }
	                                
	                               ];
	                              
   $scope.gridOptions.multiSelect = false;
   $scope.gridOptions.modifierKeysToMultiSelect = false;
   $scope.gridOptions.noUnselect = true;
   $scope.gridOptions.onRegisterApi = function( gridApi ) {
     $scope.gridApi = gridApi;       
     gridApi.selection.on.rowSelectionChanged($scope, function(){
    	var selectedRow=jQuery.extend(true, {}, $scope.gridApi.selection.getSelectedRows()[0]);  
    	selectedRow.dateSampled=$filter('date')(selectedRow.dateSampled,'d/MMM/yyyy');
    	selectedRow.dateDisposed=$filter('date')(selectedRow.dateDisposed,'d/MMM/yyyy');
    	 $scope.form = selectedRow
     });
     
     spinnerService.show('sample.grid');
     $http.get('getSample.do') 
     .success(function(data) {
       $scope.gridOptions.data = data;
       spinnerService.hide('sample.grid')
       spinnerService._unregister('sample.grid')
       
     })
     .error(function(data, status) {
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
        spinnerService.hide('sample.grid')
        spinnerService._unregister('sample.grid')
     })
     
   };	
   
   if($routeParams.id){
	   $http.get('getSample.do',{
		   params:{
			   id : $routeParams.id
		   }
	   })     
	   .success(function(data) {
		   $scope.form = data[0];
		   $scope.form.dateSampled=$filter('date')($scope.form.dateSampled,'d/MMM/yyyy');
		   $scope.form.dateDisposed=$filter('date')($scope.form.dateDisposed,'d/MMM/yyyy');
	   })
   }
   
   
   $scope.openSearch = function(){
	  	 var promise = SearchSubCollectionService.open();
	  	 promise.then(function(selectedItem) { 
			 if(!$scope.form.rsSubcollection){
				$scope.form.rsSubcollection=[];
			 }
	  			$scope.form.rsSubcollection['subcollectionId']=selectedItem;
	  		}, function(reason) {
	  		  alert('Failed: ' + reason);
	  		});
	   }

   
   
}]);