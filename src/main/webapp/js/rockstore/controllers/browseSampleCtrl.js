

allControllers.controller('BrowseSampleCtrl', ['$scope','$http','MapModalService','$routeParams','SearchSubCollectionService',function ($scope,$http,MapModalService,$routeParams,SearchSubCollectionService) {
	
	
	$scope.samples=[];
	$scope.expansionCSSDefault='out';
	
		
	var getSamples = function(){
		 $http.get('getSample.do',{
				params:{	
					id: $routeParams.id
					}
		 })     
	     .success(function(data) {
	       $scope.samples = data;
	       
	       if($routeParams.id){
	    	   $scope.expansionCSSDefault='in';
	       }
	        
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
     
     $scope.openSearch = function(){
    	 var promise = SearchSubCollectionService.open();
    	 promise.then(function(selectedItem) {
    		 $scope.searchSubCollectionId= selectedItem;
    		}, function(reason) {
    		  alert('Failed: ' + reason);
    		});
     }
     
}]);


