
allControllers.controller('BrowseCollectionCtrl', ['$scope','$http',function ($scope,$http) {
	
	$scope.mycollections=[];
	$scope.subcollections=[];
	var selectedCollection=0;
		
	
     $http.get('getCollections.do')     
     .success(function(data) {
       $scope.mycollections = data;       
        
     })
     .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
       
     })
     
     $scope.getSubCollection=function(collectionId){
    	 if(collectionId==selectedCollection){
    		 return;
    	 }else{
    		 selectedCollection=collectionId;
    	 }    	 
    	 $http.get('getSubCollectionsByCollection.do',{
			params:{	
				collectionId: collectionId
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
   
      
}]);


