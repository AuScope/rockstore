
allControllers.controller('BrowseSubCollectionCtrl', ['$scope','$http',function ($scope,$http) {
	
	$scope.samples=[];
	$scope.subcollections=[];
	
		
	
     $http.get('getSubCollections.do')     
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
   
      
}]);


