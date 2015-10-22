
allControllers.controller('BatchLogCtrl', ['$scope','$rootScope','$http','$filter','modalService',
                                                    function ($scope,$rootScope,$http,$filter,modalService) {
	
	
	
	
	
	var listBatch = function(){
		$http.get('listBatch.do')     
	     .success(function(data) {
	    	 $scope.batchs = data;
	    	 
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Retrieve Batch" ,
		           bodyText: "FAILURE: Please contact cg-admin@csiro.au"
	    	 });			       
	     })
	}
	
	listBatch();
	
	$scope.listBatchError = function(batchId){
		$http.get('listBatchError.do',{
			params:{	
				batchId: batchId		
				}
		 })     
	     .success(function(data) {
	    	 $scope.batchlogs = data;
	    	 
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Retrieve Batch" ,
		           bodyText: "FAILURE: Please contact cg-admin@csiro.au"
	    	 });			       
	     })
	}
	
	
	
	
   
}]);


