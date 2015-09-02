

allControllers.controller('BrowseSampleCtrl', ['$scope','$http','MapModalService','$routeParams','SearchSubCollectionService','modalService',
                                               function ($scope,$http,MapModalService,$routeParams,SearchSubCollectionService,modalService) {
	
	
	$scope.samples=[];
	$scope.expansionCSSDefault='out';
	
	$scope.form={};		
	$scope.totalItem = 0;
	$scope.currentPages = 1;
	
		
	var getSamples = function(){
		 $http.get('getSample.do',{
				params:{	
					id: $routeParams.id
					}
		 })     
	     .success(function(data) {
	       $scope.samples = data;
	       
	       if($routeParams.id){
	    	   $scope.expansionCSSDefault='in';
	       }
	        
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Error loading data:" + status ,
		           bodyText: "Please contact cg-admin@csiro.au if this persist"
	    	 });
	       
	     })
	}
	
	
	$scope.searchSample = function(){
	   	 var params ={	
   			subcollectionId: $scope.form.subcollectionId,
   			igsn:$scope.form.igsn,
   			csiroSampleId: $scope.form.csiroSampleId, 			
   			bhid : $scope.form.bhid,
   			externalRef : $scope.form.externalRef,
			pageNumber:$scope.currentPages,
			pageSize:10
		 }
		
		//VT: Actual results
		$http.get('searchSample.do',{
			params:params
		 })     
	     .success(function(data) {
	       $scope.samples = data;       
	        
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Error loading data:" + status ,
		           bodyText: "Please contact cg-admin@csiro.au if this persist"
	    	 });
	       
	     })
	     //VT: Get the count of the result
	     $http.get('searchSampleCount.do',{
			params:params
		 })     
	     .success(function(data) {
	       $scope.totalItem = data;       	        
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Error loading data:" + status ,
		           bodyText: "Please contact cg-admin@csiro.au if this persist"
	    	 });
	       
	     })
    }
	
	
	
	$scope.pageChanged = function() {
 		$scope.searchSample();
 	  };
     
     if($routeParams.id){
    	 getSamples($routeParams.id);
 	 }else{
 		$scope.searchSample();
 	 }
     
     
     //VT: GOOGLE MAP MODALS
     $scope.open = function(lat, lon,info){
    	 MapModalService.open(lat, lon,info)
     }
     
     $scope.openSearch = function(){
    	 var promise = SearchSubCollectionService.open();
    	 promise.then(function(selectedItem) {
    		 $scope.form.subcollectionId= selectedItem;
    		}, function(reason) {
    		  alert('Failed: ' + reason);
    		});
     }
     
}]);


