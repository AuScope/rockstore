
allControllers.controller('BrowseCollectionCtrl', ['$scope','$http','$routeParams','DropDownValueService','modalService',function ($scope,$http,$routeParams,DropDownValueService,modalService) {
	
	$scope.collections=[];
	$scope.subcollections=[];
	$scope.users = DropDownValueService.getUsers();	
	$scope.booleans = DropDownValueService.getBoolean();
	$scope.form={};	
	
	$scope.totalItem = 0;
	$scope.currentPages = 1;
	
	
	var getCollection = function(collectionId){
		$http.get('getCollections.do',{
			params:{	
				collectionId: collectionId		
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
	}
	
	$scope.searchCollection = function(){
		
		var params ={	
				project: $scope.form.project,
				staffIdFieldManager:$scope.form.staffIdFieldManager,
				staffidResponsible: $scope.form.staffidResponsible,
				projectPublication : $scope.form.projectPublication,
				pageNumber:$scope.currentPages,
				pageSize:10
				}
		
		//VT: Actual results
		$http.get('searchCollections.do',{
			params:params
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
	     //VT: Get the count of the result
	     $http.get('searchCollectionsCount.do',{
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
		$scope.searchCollection();
	  };
	
    
	if($routeParams.collectionId){
		getCollection($routeParams.collectionId	);
	}else{
		$scope.searchCollection();
	}
	
	
	
     
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


