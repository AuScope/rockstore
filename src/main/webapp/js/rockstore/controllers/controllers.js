var allControllers = angular.module('allControllers', []);


allControllers.controller('MapModalInstanceCtrl', function ($scope, $modalInstance, marker, uiGmapIsReady,uiGmapGoogleMapApi ) {

	$scope.initialize=function() {
		 $scope.map = { center: { latitude: marker.latitude, longitude: marker.longitude }, zoom: 8 };
		  
		  $scope.marker = {	
				  id : 0,
			      coords: {
			        latitude: marker.latitude,
			        longitude: marker.longitude
			      }	      
		  };
		
		  $scope.windowOptions = {
		       visible: false
		  };
		  $scope.onClick = function() {
	        $scope.windowOptions.visible = !$scope.windowOptions.visible;
		  };
		  $scope.closeClick = function() {
	        $scope.windowOptions.visible = false;
	      };
	      $scope.ok = function () {
		    $modalInstance.close();
		  };
		  $scope.cancel = function () {
		    $modalInstance.dismiss('cancel');
		  };
		  
		  $scope.info=marker.info

		  uiGmapIsReady.promise()
		    .then(function (map_instances) {
		    	var map = map_instances[0].map
		    	google.maps.event.trigger(map, 'resize');
		    	map.setCenter(new google.maps.LatLng($scope.map.center.latitude,$scope.map.center.longitude));
		    });

		  
		 
    }

    $scope.initialize();
	  
	 
	 
	  
	  
});