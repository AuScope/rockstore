
allControllers.controller('BrowseCollectionCtrl', ['$scope','$http','$routeParams','DropDownValueService','modalService','currentAuthService','ViewUserInfoService','$location','ViewHistoryService',
                                                   function ($scope,$http,$routeParams,DropDownValueService,modalService,currentAuthService,ViewUserInfoService,$location,ViewHistoryService) {
	
	$scope.status = currentAuthService.getStatus();
	
	
	$scope.getCurrentUrlPath =$location.absUrl().replace($routeParams.collectionId,'') + "/";

	
	$scope.collections=[];
	$scope.subcollections=[];
	
	DropDownValueService.getUsers()
	.then(function(data) {
		 $scope.users= data;
	}, function(data, status) {
		 modalService.showModal({}, {    	            	           
	           headerText: "Error retrieve user list",
	           bodyText: data
		 });
	});
	
	
	DropDownValueService.getStaffs()
	.then(function(data) {
		 $scope.staffs= data;
	}, function(data, status) {
		modalService.showModal({}, {    	            	           
	           headerText: "Error retrieve staff list",
	           bodyText: data
		 });
	});
	
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
	
	$scope.searchCollection = function(page){
		$scope.currentPages = page;//VT page is reset to 1 on new search
		var params ={	
				project: $scope.form.project,
				staffIdFieldManager:$scope.form.staffIdFieldManager,
				staffidResponsible: $scope.form.staffidResponsible,
				projectPublication : $scope.form.projectPublication,
				pageNumber:page,
				pageSize:10
				}
		
		//VT: Actual results
		$http.get('searchCollections.do',{
			params:params
		 })     
	     .success(function(data) {
	       $scope.collections = data;       
	       $scope.toggleFilter=false;
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
		$scope.searchCollection($scope.currentPages);
	  };
	
    
	if($routeParams.collectionId){
		getCollection($routeParams.collectionId	);
	}else{
		$scope.searchCollection(1);
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
   
     $scope.resetForm = function(){
    	 $scope.form={};
    	 getCollection();
    	 $scope.toggleFilter=false;
     }
     
     $scope.viewStaff = function(name){
    	 ViewUserInfoService.viewStaff(name);
     }
     
     $scope.viewUser = function(name){
    	 ViewUserInfoService.viewUser(name);
     }
     
     $scope.viewHistory = function(collectionId){
    	 ViewHistoryService.viewCollectionHistory(collectionId);
     }
      
}]);


