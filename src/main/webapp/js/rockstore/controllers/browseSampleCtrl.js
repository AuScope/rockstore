

allControllers.controller('BrowseSampleCtrl', ['$scope','$http','MapModalService','$routeParams','SearchSubCollectionService','modalService','currentAuthService','ViewUserInfoService',
                                               function ($scope,$http,MapModalService,$routeParams,SearchSubCollectionService,modalService,currentAuthService,ViewUserInfoService) {
	
	$scope.status = currentAuthService.getStatus();
	
	$scope.samples=[];
	$scope.expansionCSSDefault='out';
	
	$scope.form={};		
	$scope.totalItem = 0;
	$scope.currentPages = 1;
	
		
	var getSamples = function(id){
		 $http.get('getSample.do',{
				params:{	
					id: id
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
	
	//VT:page determines the page to search and set the current page
	$scope.searchSample = function(page){
		$scope.currentPages = page;
	   	 var params ={	
   			subcollectionId: $scope.form.subcollectionId,
   			igsn:$scope.form.igsn,
   			csiroSampleId: $scope.form.csiroSampleId, 			
   			bhid : $scope.form.bhid,
   			externalRef : $scope.form.externalRef,
			pageNumber:page,
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
 		$scope.searchSample($scope.currentPages);
 	  };
     
     if($routeParams.id){
    	 getSamples($routeParams.id);
 	 }else{
 		$scope.searchSample(1);
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
     
     $scope.resetForm = function(){
    	 $scope.form={};
    	 getSamples();
     }
     
     $scope.viewStaff = function(name){
    	 ViewUserInfoService.viewStaff(name);
     }
     
     $scope.viewUser = function(name){
    	 ViewUserInfoService.viewUser(name);
     }
     
}]);


