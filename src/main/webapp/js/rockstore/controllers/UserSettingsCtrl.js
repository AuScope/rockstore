
allControllers.controller('UserSettingsCtrl', ['$scope','$rootScope','$http','DropDownValueService','$filter','spinnerService','modalService',
                                                    function ($scope,$rootScope,$http,DropDownValueService,$filter,spinnerService,modalService) {
	
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false, enableColumnResizing: true };
	
	$scope.gridOptions.data = [];
	
	
	
	$scope.form ={};
	
	$scope.resetForm = function(){
		$scope.form ={}
		$scope.gridApi.selection.clearSelectedRows();
	}
	
	$scope.submit = function(){
		$http.get('addUser.do', {
			params:{	
				organization: $scope.form.organization,
				contactName: $scope.form.contactName,
				phone: $scope.form.phone,
				email: $scope.form.email,
				address: $scope.form.address,
				city: $scope.form.city,
				state: $scope.form.state,				
				zipCode: $scope.form.zipCode
				}
		})
		.then(function(response) {								
			$scope.gridOptions.data.push(response.data)
			$scope.resetForm();					
		  }, function(response,status) {
			  modalService.showModal({}, {    	            	           
		           headerText: response.data.header,
		           bodyText: response.data.message
	    	 });
		  });
		  
	}

	$scope.gridOptions.columnDefs = [	                                
                                 	 { field: 'organization',displayName: 'Organization',width:130 },
	                                 { field: 'contactName',displayName: 'Contact Name',width:150 },
	                                 { field: 'phone',displayName: 'phone',width:100},
	                                 { field: 'email',displayName: 'Email',width:80},
	                                 { field: 'address',displayName: 'Address',width:150 },
	                                 { field: 'city',displayName: 'City',width:80 },
	                                 { field: 'state',displayName: 'State',width:80 },
	                                 { field: 'zipCode',displayName: 'Postcode',width:130 }
	                                
	                               ];
	
	                              
   $scope.gridOptions.multiSelect = false;
   $scope.gridOptions.modifierKeysToMultiSelect = false;
   $scope.gridOptions.noUnselect = true;
   $scope.gridOptions.onRegisterApi = function( gridApi ) {
     $scope.gridApi = gridApi;       
     gridApi.selection.on.rowSelectionChanged($scope, function(){
    	var selectedRow=jQuery.extend(true, {}, $scope.gridApi.selection.getSelectedRows()[0]);    	
    	 $scope.form = selectedRow
     });
     
     spinnerService.show('usersettings.grid');
     $http.get('getUsers.do')     
     .success(function(data) {
       $scope.gridOptions.data = data;
       spinnerService.hide('usersettings.grid')
       spinnerService._unregister('usersettings.grid')
        
     })
     .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
       
        spinnerService.hide('usersettings.grid')
        spinnerService._unregister('usersettings.grid')
     })
     
   };
   
   
  

   
   
}]);


