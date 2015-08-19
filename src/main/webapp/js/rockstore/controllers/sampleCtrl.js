angular.module('app').controller('SampleCtrl', ['$scope','$rootScope','$http','DropDownValueService','$filter','spinnerService','modalService',
                                                    function ($scope,$rootScope,$http,DropDownValueService,$filter,spinnerService,modalService) {
	
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false };
	$scope.gridOptions.data = [];
	
	
	$scope.form ={};
	
	$scope.resetForm = function(){
		$scope.form ={						
				locationInStorage : $scope.form.locationInStorage,
				hazardous : $scope.form.hazardous,				
		    }
		$scope.gridApi.selection.clearSelectedRows();
	}
	
	$scope.submit = function(){
		if(!($scope.form.rsCollection && $scope.form.rsSubcollection)){
			modalService.showModal({}, {    	            	           
		           headerText: "Collection Id or Sub-collection Id not set",
		           bodyText: "Make sure you choose a valid collection id and sub-collection id"
	    	 });
		}
		$http.get('sampleAddUpdate.do', {
			params:{	
				subcollectionId: $scope.form.rsSubcollection['subcollectionId'],
				igsn: $scope.form.igsn,
				csiroSampleId: $scope.form.csiroSampleId,				
				sampleType: $scope.form.sampleType,
				bhid: $scope.form.bhid,
				depth: $scope.form.depth,
				datum: $scope.form.datum,
				zone : $scope.form.zone,
				containerId : $scope.form.containerId,
				externalRef : $scope.form.externalRef,
				sampleCollector : $scope.form.sampleCollector,
				dateSampled : $scope.form.dateSampled,
				sampleDispose : $scope.form.sampleDispose,
				dateDisposed : $scope.form.dateDisposed,
				staffidDisposed : $scope.form.staffidDisposed,
				location : $scope.form.location,
				collectionId: $scope.form.rsCollection['collectionId']
				}
		})
		.then(function(response) {		
			response.data.dateSampled=$filter('date')(response.data.dateSampled,'d/MMM/yyyy');
			response.data.dateDisposed=$filter('date')(response.data.dateDisposed,'d/MMM/yyyy');						
			if($scope.form.subcollectionId){
				$.extend($scope.gridApi.selection.getSelectedRows()[0],response.data)				
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

 
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false, enableColumnResizing: true };
	
	$scope.gridOptions.columnDefs = [	                              
                                 	 { field: 'id',displayName: 'id',width:50 },
                                 	 { field: "rsCollection['collectionId']",displayName: 'collection Id', width:150 },
	                                 { field: "rsSubcollection['subcollectionId']",displayName: 'subCollection id',width:150 },	                                 
	                                 { field: 'igsn',displayName: 'igsn',width:150 },	                                 
	                                 { field: 'csiroSampleId',displayName: 'CSIRO SampleId',width:130},
	                                 { field: "sampleType",displayName: 'Sample Type',width:180},	                            
	                                 { field: 'bhid',displayName: 'Borehole ID',width:170 },
	                                 { field: 'depth',displayName: 'Depth',width:170 },
	                                 { field: 'datum',displayName: 'Datum',width:170 },
	                                 { field: 'zone',displayName: 'Zone',width:170 },
	                                 { field: 'containerId',displayName: 'ContainerId',width:170 },
	                                 { field: 'externalRef',displayName: 'externalRef',width:170 },
	                                 { field: 'sampleCollector',displayName: 'Sample Collector',width:170 },
	                                 { field: 'dateSampled',displayName: 'Date Sampled', cellFilter :'date:"d/MMM/yyyy"', width:170 },
	                                 { field: 'sampleDispose',displayName: 'Sample Dispose',width:170 },
	                                 { field: 'dateDisposed',displayName: 'Date Disposed', cellFilter :'date:"d/MMM/yyyy"', width:170 },
	                                 { field: 'staffidDisposed',displayName: 'Staff Disposed',width:170 },
	                                 { field: 'location',displayName: 'Location',width:170 },
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
     })
     .error(function(data, status) {
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
        spinnerService.hide('sample.grid')
     })
     
   };	

   
   
}]);