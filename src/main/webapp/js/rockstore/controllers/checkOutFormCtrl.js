allControllers.controller('CheckOutFormCtrl', ['$scope','$http','currentAuthService','$routeParams','modalService','SearchSubCollectionService',
                                                    function ($scope,$http,currentAuthService,$routeParams,modalService,SearchSubCollectionService ) {
	
	$scope.form={};
	$scope.returnToBrowseSubcollectionId = false;
	
	 $scope.openSearch = function(){
    	 $scope.toggleFilter=false;
    	 var promise = SearchSubCollectionService.open();
    	 promise.then(function(selectedItem) {
    		 $scope.form.subcollectionId= selectedItem;
    		 $scope.toggleFilter=true;
    		}, function(reason) {
    		  alert('Failed: ' + reason);
    		});
     }
	
	 if($routeParams.subCollectionId){
    	$scope.form.subcollectionId = $routeParams.subCollectionId;
    	$scope.returnToBrowseSubcollectionId = $routeParams.subCollectionId;;
 	 }
	 
	 $scope.checkout = function(){
		 
				
				//VT: Actual results
				$http.get('registerCheckout.do',{
					params:$scope.form
				 })     
			     .success(function(data) {
			    	 $scope.form={}
			    	 modalService.showModal({}, {    	            	           
				           headerText: "Request sub collection checkout" ,
				           bodyText: "Your request has been successfully send!"
			    	 });
			     })
			     .error(function(data, status) {    	
			    	 modalService.showModal({}, {    	            	           
				           headerText: "Request sub collection checkout" ,
				           bodyText: "FAILURE: Ensure you have checkout and due back date entry"
			    	 });			       
			     })
	 }
   
   
}]);