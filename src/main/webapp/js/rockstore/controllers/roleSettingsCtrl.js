
allControllers.controller('RoleSettingCtrl', ['$scope','$rootScope','$http','$filter','modalService',
                                                    function ($scope,$rootScope,$http,$filter,modalService) {
	
	$scope.permissions={};
	
	
	
	var listUserPermissions = function(){
		$http.get('listUserPermission.do')     
	     .success(function(data) {
	    	 $scope.permissions = data;
	    	 
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Retrieve Permissions" ,
		           bodyText: "FAILURE: Please contact cg-admin@csiro.au"
	    	 });			       
	     })
	}
	
	$scope.add = function(){
		$http.get('addUserPermission.do',{
			params : $scope.addForm							
		})     
	     .success(function(data) {
	    	 listUserPermissions();	    	 
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Add Permissions" ,
		           bodyText: "FAILURE: Please contact cg-admin@csiro.au"
	    	 });			       
	     })
	}
	
	$scope.deleteEntry = function(staff){
		$http.get('deleteUserPermission.do',{
			params : {
				staff:staff
			}						
		})     
	     .success(function(data) {
	    	 listUserPermissions();	    	 
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Add Permissions" ,
		           bodyText: "FAILURE: Please contact cg-admin@csiro.au"
	    	 });			       
	     })
	}
	
	listUserPermissions();
   
}]);


