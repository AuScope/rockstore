
allControllers.controller('BrowseSubCollectionCtrl', ['$scope','$http','MapModalService','$routeParams','SearchCollectionService','DropDownValueService','modalService',
                                                      function ($scope,$http,MapModalService,$routeParams,SearchCollectionService,DropDownValueService,modalService) {
	
	$scope.samples=[];
	$scope.subcollections=[];
	$scope.booleans = DropDownValueService.getBoolean();
	$scope.locations = DropDownValueService.getLocations();
	$scope.storageTypes = DropDownValueService.getStorageType();
	
	$scope.form={};	
	
	$scope.totalItem = 0;
	$scope.currentPages = 1;
		
     var getSubCollection = function(collectionId){
    	 $http.get('getSubCollections.do',{
 			params:{	
 				subCollectionId: $routeParams.subCollectionId
 				}
	 	 })          
	      .success(function(data) {
	        $scope.subcollections = data;       
	      })
	      .error(function(data, status) {    	
	     	 modalService.showModal({}, {    	            	           
	 	           headerText: "Error loading data:" + status ,
	 	           bodyText: "Please contact cg-admin@csiro.au if this persist"
	     	 });
	        
	      })
     }
     
     
     $scope.searchSubCollection = function(){
    	 var params ={	
    			collectionId: $scope.form.collectionId,
    			locationInStorage:$scope.form.locationInStorage,
    			storageType: $scope.form.storageType, 			
 				source : $scope.form.source,
 				pageNumber:$scope.currentPages,
 				pageSize:10
 				}
 		
 		//VT: Actual results
 		$http.get('searchSubCollections.do',{
 			params:params
 		 })     
 	     .success(function(data) {
 	       $scope.subcollections = data;       
 	        
 	     })
 	     .error(function(data, status) {    	
 	    	 modalService.showModal({}, {    	            	           
 		           headerText: "Error loading data:" + status ,
 		           bodyText: "Please contact cg-admin@csiro.au if this persist"
 	    	 });
 	       
 	     })
 	     //VT: Get the count of the result
 	     $http.get('searchSubCollectionsCount.do',{
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
 		$scope.searchSubCollection();
 	  };
     
     if($routeParams.subCollectionId){
    	 getSubCollection($routeParams.subCollectionId	);
 	 }else{
 		$scope.searchSubCollection();
 	 }
     
     $scope.getSamples=function(subCollectionId){
    	 if($scope.samples[subCollectionId]){
    		 return;
    	 }    	 
    	 $http.get('getSamplesbySubCollection.do',{
			params:{	
				subCollectionId: subCollectionId
				}
		})     
         .success(function(data) {
           $scope.samples[subCollectionId] = data;       
            
         })
         .error(function(data, status) {    	
        	 modalService.showModal({}, {    	            	           
    	           headerText: "Error loading data:" + status ,
    	           bodyText: "Please contact cg-admin@csiro.au if this persist"
        	 });
           
         })
     }
     
     //VT: GOOGLE MAP MODALS
     $scope.open = function(lat, lon,info){
    	 MapModalService.open(lat, lon,info)
     }
     
     $scope.openSearch = function(){
    	 var promise = SearchCollectionService.open();
    	 promise.then(function(selectedItem) {
    		 $scope.form.collectionId= selectedItem;
    		}, function(reason) {
    		  alert('Failed: ' + reason);
    		});
     }
   
      
}]);


