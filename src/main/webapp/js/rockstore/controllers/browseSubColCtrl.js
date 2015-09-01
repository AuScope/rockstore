
allControllers.controller('BrowseSubCollectionCtrl', ['$scope','$http','MapModalService','$routeParams','SearchCollectionService','DropDownValueService',
                                                      function ($scope,$http,MapModalService,$routeParams,SearchCollectionService,DropDownValueService) {
	
	$scope.samples=[];
	$scope.subcollections=[];
	$scope.booleans = DropDownValueService.getBoolean();
	$scope.locations = DropDownValueService.getLocations();
	$scope.storageTypes = DropDownValueService.getStorageType();
		
	
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
     
     $scope.getSubCollection=function(subCollectionId){
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
    		 $scope.searchCollectionId= selectedItem;
    		}, function(reason) {
    		  alert('Failed: ' + reason);
    		});
     }
   
      
}]);


