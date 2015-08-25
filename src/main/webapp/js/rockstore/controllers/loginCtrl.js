allControllers.controller('LoginCtrl', ['$scope','$http','currentAuthService','$route','$templateCache','$location',
                                                    function ($scope,$http,currentAuthService,$route,$templateCache,$location ) {
	
	
	var authenticate = function(credentials) {
		if(!(credentials.username && credentials.password)){
			return;
		}
		
		var details = 'j_username=' + encodeURIComponent(credentials.username) +
        '&j_password=' + encodeURIComponent(credentials.password);
		

	    $http.post('views/login.html',details, {
            headers : {
                'Content-Type': 'application/x-www-form-urlencoded'               
            }
        }).success(function(data,status) {
        	
	      if (data.name) {
	    	  currentAuthService.setAuthenticated(true);
	    	  currentAuthService.setUsername(data.name);
	    	  
	    	  if($location.path()=='/login'){
	    		  $location.path("/");
	    	  }else{
	    		  var currentPageTemplate = $route.current.templateUrl;
		    	  $templateCache.remove(currentPageTemplate);
		    	  $route.reload();  
	    	  }
	    	  	    	  
          }else{
        	  currentAuthService.setAuthenticated(false);
        	  $scope.error=true;
          }
	    
	    }).error(function() {
	    	currentAuthService.setAuthenticated(false);	     
	    });	    

	  }

	  
	  $scope.credentials = {};
	  $scope.login = function() {
	      authenticate($scope.credentials);
	  };
	  
	  
	  $scope.logout = function() {
		  $http.get('logout', {}).success(function() {
			  currentAuthService.setAuthenticated(false);
		  }).error(function(data) {
			  currentAuthService.setAuthenticated(false);
		  });
		}
   
   
}]);