allControllers.controller('ManageCheckOutCtrl', ['$scope','$http','modalService',
                                                    function ($scope,$http,modalService ) {
	
	$scope.checkouts={};
	
	var getPendingEntries = function(){
		$http.get('getPendingEntries.do')     
	     .success(function(data) {
	    	 $scope.checkouts = data;
	    	 
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Request sub collection checkout" ,
		           bodyText: "FAILURE: Please contact cg-admin@csiro.au"
	    	 });			       
	     })
	}
	
	getPendingEntries();
	 
	$scope.processCheckout = function(entry){
		$http.get('checkOut.do',{
			params:{
				id : entry.id
			}
		})     
	     .success(function(data) {
	    	 entry.checkoutStatus=true;	    	 
	    	 
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Request sub collection checkout" ,
		           bodyText: "FAILURE: Please contact cg-admin@csiro.au"
	    	 });			       
	     })
	}
	
	$scope.processCheckin = function(entry){
		$http.get('checkIn.do',{
			params:{
				id : entry.id
			}
		})     
	     .success(function(data) {
	    	 entry.checkoutStatus = data.checkoutStatus;
	    	 entry.dateCheckin = data.dateCheckin;
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Request sub collection checkout" ,
		           bodyText: "FAILURE: Please contact cg-admin@csiro.au"
	    	 });			       
	     })
	}
	
   
}]);