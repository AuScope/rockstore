
allControllers.controller('BrowseSubCollectionCtrl', ['$scope','$http','MapModalService','$routeParams','SearchCollectionService','DropDownValueService','modalService','currentAuthService',
                                                      function ($scope,$http,MapModalService,$routeParams,SearchCollectionService,DropDownValueService,modalService,currentAuthService) {
	
	$scope.status = currentAuthService.getStatus();
	
	$scope.samples=[];
	$scope.subcollections=[];
	$scope.booleans = DropDownValueService.getBoolean();
	$scope.locations = DropDownValueService.getLocations();
	$scope.storageTypes = DropDownValueService.getStorageType();
	
	$scope.form={};	
	
	$scope.totalItem = 0;
	$scope.currentPages = 1;
		
     var getSubCollection = function(igsn){
    	 $http.get('getSubCollectionsByIGSN.do',{
 			params:{	
 				igsn: igsn
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
     
     
     $scope.searchSubCollection = function(page){
    	 $scope.currentPages = page;//VT page is reset to 1 on new search
    	 var params ={	
    			collectionId: $scope.form.collectionId,
    			oldId : $scope.form.oldId,
    			locationInStorage:$scope.form.locationInStorage,
    			storageType: $scope.form.storageType, 			
 				source : $scope.form.source,
 				pageNumber:page,
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
 		$scope.searchSubCollection($scope.currentPages);
 	  };
     
     if($routeParams.igsn){
    	 getSubCollection($routeParams.igsn);
 	 }else{
 		$scope.searchSubCollection(1);
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
   
     $scope.resetForm = function(){
    	 $scope.form={};
    	 getSubCollection();
     }
      
}]);


