allControllers.controller('ImportCtrl', ['$scope','$rootScope','$http','$filter','modalService','$routeParams','spinnerService','Upload', '$timeout','$location',
                                                    function ($scope,$rootScope,$http,$filter,modalService,$routeParams,spinnerService,Upload, $timeout,$location) {
	
	
	$scope.gridOptions = { 
			enableRowSelection: true, 
			enableRowHeaderSelection: false, 
			enableColumnResizing: true  
	};
	
	$scope.gridOptions.data = [];
	
	$scope.gridOptions.columnDefs = [	                                                               	                              	
	                                 { field: "rsSubcollection['subcollectionId']",displayName: 'subCollection id',width:150 },	                                 	                                                    
	                                 { field: 'csiroSampleId',displayName: 'CSIRO SampleId',width:150},
	                                 { field: "sampleType",displayName: 'Sample Type',width:140},	                            
	                                 { field: 'bhid',displayName: 'Borehole ID',width:170 },
	                                 { field: 'depth',displayName: 'Depth',width:110 },
	                                 { field: 'datum',displayName: 'Datum',width:120 },	                                
	                                 { field: 'containerId',displayName: 'ContainerId',width:170 },
	                                 { field: 'externalRef',displayName: 'externalRef',width:170 },
	                                 { field: 'sampleCollector',displayName: 'Sample Collector',width:170 },
	                                 { field: 'dateSampled',displayName: 'Date Sampled', cellFilter :'date:"d/MMM/yyyy"', width:170 },
	                                 { field: 'sampleDispose',displayName: 'Sample Dispose',width:170 },
	                                 { field: 'dateDisposed',displayName: 'Date Disposed', cellFilter :'date:"d/MMM/yyyy"', width:170 },
	                                 { field: 'staffidDisposed',displayName: 'Staff Disposed',width:170 },
	                                 { field: 'origLat',displayName: 'Latitude',width:120 },
	                                 { field: 'origLon',displayName: 'Longtitude',width:120 }
	                                
	                               ];
	
	  $scope.gridOptions.multiSelect = false;
	   $scope.gridOptions.modifierKeysToMultiSelect = false;
	   $scope.gridOptions.noUnselect = true;
	   $scope.gridOptions.onRegisterApi = function( gridApi ) {
	     $scope.gridApi = gridApi;       	    
	   }
	     
	
	
	 $scope.uploadFiles = function(file) {
		 spinnerService.show('import-preview-grid');
	        $scope.f = file;
	       
	        if (file) {
	            file.upload = Upload.upload({
	                url: 'preview.do',
	                data: {file: file}
	            });

	            file.upload.then(function (response) {
	                $timeout(function () {
	                	$scope.gridOptions.data = response.data;
	                	spinnerService.hide('import-preview-grid');
	                });
	            }, function (response) {
	            	spinnerService.hide('import-preview-grid');
	                if (response.status > 0)
	                	modalService.showModal({}, {    	            	           
	 			           headerText: response.data.header ,
	 			           bodyText: response.data.message
	 		    	 });
	            }, function (evt) {
	                file.progress = Math.min(100, parseInt(100.0 * 
	                                         evt.loaded / evt.total));
	            });
	        }   
	    }
	 
	 $scope.processFile = function() {
		
	       var file = $scope.f;
	       
	        if (file) {
	            file.upload = Upload.upload({
	                url: 'import.do',
	                data: {file: file}
	            });

	            file.upload.then(function (response) {
	                $timeout(function () {
	                	$location.path("/batchlog");
	                	
	                });
	            });
	        }   
	    }
	 
   
   
}]);