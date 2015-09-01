
allControllers.controller('BrowseCollectionCtrl', ['$scope','$http','$routeParams','DropDownValueService',function ($scope,$http,$routeParams,DropDownValueService) {
	
	$scope.collections=[];
	$scope.subcollections=[];
	$scope.users = DropDownValueService.getUsers();	
	$scope.booleans = DropDownValueService.getBoolean();
		
	
     $http.get('getCollections.do',{
		params:{	
			collectionId: $routeParams.collectionId
			}
	 })     
     .success(function(data) {
       $scope.collections = data;       
        
     })
     .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
       
     })
     
     $scope.getSubCollection=function(collectionId){
    	 if($scope.subcollections[collectionId]){
    		 return;
    	 }   	 
    	 $http.get('getSubCollectionsByCollection.do',{
			params:{	
				collectionId: collectionId
				}
		})     
         .success(function(data) {
           $scope.subcollections[collectionId] = data;       
            
         })
         .error(function(data, status) {    	
        	 modalService.showModal({}, {    	            	           
    	           headerText: "Error loading data:" + status ,
    	           bodyText: "Please contact cg-admin@csiro.au if this persist"
        	 });
           
         })
     }
   
      
}]);


