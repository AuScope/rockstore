allControllers.controller('LoginStatusCtrl', ['$scope','$http','currentAuthService',
                                                    function ($scope,$http,currentAuthService) {
	
	
	$scope.status = currentAuthService.getStatus();
	
	if(!$scope.status.authenticated){
		$http.get('getUser.do', {}).success(function(response) {
			 if(response.name){
				 currentAuthService.setAuthenticated(true);
				 currentAuthService.setUsername(response.name); 
			 }else{
				 currentAuthService.setAuthenticated(false);
			 }		  
		  }).error(function(data) {
			  currentAuthService.setAuthenticated(false);
		  });
	}

   
}]);