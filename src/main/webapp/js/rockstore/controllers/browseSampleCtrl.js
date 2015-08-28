

allControllers.controller('BrowseSampleCtrl', ['$scope','$http',function ($scope,$http) {
	
	
	$scope.samples=[];
	
		
	
     $http.get('getSample.do')     
     .success(function(data) {
       $scope.samples = data;       
        
     })
     .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
       
     })
 
}]);


