
allControllers.controller('BrowseCollectionCtrl', ['$scope','$http','$routeParams',function ($scope,$http,$routeParams) {
	
	$scope.collections=[];
	$scope.subcollections=[];
	
		
	
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


