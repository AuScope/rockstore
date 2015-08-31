

allControllers.controller('BrowseSampleCtrl', ['$scope','$http','MapModalService','$routeParams',function ($scope,$http,MapModalService,$routeParams) {
	
	
	$scope.samples=[];
	
		
	var getSamples = function(){
		 $http.get('getSample.do',{
				params:{	
					id: $routeParams.id
					}
		 })     
	     .success(function(data) {
	       $scope.samples = data;       
	        
	     })
	     .error(function(data, status) {    	
	    	 modalService.showModal({}, {    	            	           
		           headerText: "Error loading data:" + status ,
		           bodyText: "Please contact cg-admin@csiro.au if this persist"
	    	 });
	       
	     })
	}
	getSamples();
     
     
     //VT: GOOGLE MAP MODALS
     $scope.open = function(lat, lon,info){
    	 MapModalService.open(lat, lon,info)
     }
     
}]);


