
allControllers.controller('CollectionCtrl', ['$scope','$rootScope','$http','DropDownValueService','$filter','spinnerService','modalService',
                                                    function ($scope,$rootScope,$http,DropDownValueService,$filter,spinnerService,modalService) {
	
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false };
	$scope.gridOptions.data = [];

	$scope.users = DropDownValueService.getUsers();	
	$scope.booleans = DropDownValueService.getBoolean();
	
	
	$scope.form ={};
	
	$scope.resetForm = function(){
		$scope.form ={			
				collectionId : '' ,
				project : '',	
				staffIdFieldManager:$scope.form.staffIdFieldManager,
				staffidResponsible : $scope.form.staffidResponsible,
				projectResult: '',
				projectPublication : '',
				projectCloseDate :'',
				availableToPublic : true,
				archiveDue :''
		    }
		$scope.gridApi.selection.clearSelectedRows();
	}
	
	$scope.submit = function(){
		$http.get('collectionAddUpdate.do', {
			params:{	
				collectionId: $scope.form.collectionId,
				projectId: $scope.form.project,
				staffFieldManager: $scope.form.staffIdFieldManager,
				staffResponsible: $scope.form.staffidResponsible,
				projectResult: $scope.form.projectResult,
				projectPublication: $scope.form.projectPublication,
				projectCloseDate: $scope.form.projectCloseDate,
				availableToPublic: $scope.form.availableToPublic,
				archiveDue: $scope.form.archiveDue
				}
		})
		.then(function(response) {
			response.data.projectCloseDate=$filter('date')(response.data.projectCloseDate,'d/MMM/yyyy');
			response.data.archiveDue=$filter('date')(response.data.archiveDue,'d/MMM/yyyy');			
			if($scope.form.collectionId){
				$.extend($scope.gridApi.selection.getSelectedRows()[0],response.data)				
			}else{				
				$scope.gridOptions.data.push(response.data)
				$scope.resetForm();		
			}
			
			
		  }, function(response,status) {
			  modalService.showModal({}, {    	            	           
		           headerText: response.data.header,
		           bodyText: response.data.message
	    	 });
		  });
		  
	}

 
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false, enableColumnResizing: true };
	
	$scope.gridOptions.columnDefs = [	                                
                                 	 { field: 'id',displayName: 'id',width:50 },
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
    	selectedRow.projectCloseDate=$filter('date')(selectedRow.projectCloseDate,'d/MMM/yyyy');
    	selectedRow.archiveDue=$filter('date')(selectedRow.archiveDue,'d/MMM/yyyy');
    	 $scope.form = selectedRow
     });
     
     spinnerService.show('collection.grid');
     $http.get('getCollections.do')     
     .success(function(data) {
       $scope.gridOptions.data = data;
       spinnerService.hide('collection.grid')
       spinnerService._unregister('collection.grid')
        
     })
     .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
       
        spinnerService.hide('collection.grid')
        spinnerService._unregister('collection.grid')
     })
     
   };

   
   
}]);

