allControllers.controller('LoginCtrl', ['$scope','$rootScope','$http',
                                                    function ($scope,$rootScope,$http) {
	
	$scope.authenticated=$rootScope.authenticated
	
	var authenticate = function(credentials) {
		if(!(credentials.username && credentials.password)){
			return;
		}
		
		var details = 'j_username=' + encodeURIComponent(credentials.username) +
        '&j_password=' + encodeURIComponent(credentials.password);
		

	    $http.post('login.html',details, {
            headers : {
                'Content-Type': 'application/x-www-form-urlencoded'               
            }
        }).success(function(data,status) {
        	
	      if (data.redirect) {
	    	  $rootScope.authenticated = true;
              window.location.href = data.redirect;
          }else{
        	  $rootScope.authenticated = false;
          }
	    
	    }).error(function() {
	      $rootScope.authenticated = false;	     
	    });	    

	  }

	  
	  $scope.credentials = {};
	  $scope.login = function() {
	      authenticate($scope.credentials);
	  };
	  
	  
	  $scope.logout = function() {
		  $http.get('logout', {}).success(function() {
		    $rootScope.authenticated = false;		   
		  }).error(function(data) {
		    $rootScope.authenticated = false;
		  });
		}
   
   
}]);