
allControllers.controller('BrowseSampleCtrl', ['$scope','$http',function ($scope,$http) {
	
	$scope.data=[];
		
	
     $http.get('getCollections.do')     
     .success(function(data) {
       $scope.data = data;       
        
     })
     .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
       
     })
      
}]);


