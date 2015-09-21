allControllers.controller('SubCollectionCtrl', ['$scope','$rootScope','$http','DropDownValueService','$filter','modalService','SearchCollectionService','$routeParams',
                                                    function ($scope,$rootScope,$http,DropDownValueService,$filter,modalService,SearchCollectionService,$routeParams) {
		
	$scope.paginationOptions = {
		    pageNumber: 1,
		    pageSize: 50,
		    sort: null
	};

	
	$scope.gridOptions = { 
			enableRowSelection: true, 
			enableRowHeaderSelection: false, 
			enableColumnResizing: true,
			paginationPageSizes: [1,50, 100, 200],
		    paginationPageSize: $scope.paginationOptions.pageSize,
		    useExternalPagination: true
	};
	
	$scope.gridOptions.data = [];
	
	$scope.booleans = DropDownValueService.getBoolean();
	$scope.locations = DropDownValueService.getLocations();
	$scope.storageTypes = DropDownValueService.getStorageType();
	
	
	$scope.form ={};
	$scope.search ={};
	$scope.resetForm = function(){
		$scope.form ={						
				locationInStorage : $scope.form.locationInStorage,
				hazardous : $scope.form.hazardous,		
				rsCollection : $scope.form.rsCollection?$scope.form.rsCollection:[]
		    }
		$scope.gridApi.selection.clearSelectedRows();
	}
	
	$scope.submit = function(){
		if(!$scope.form.rsCollection || !$scope.form.rsCollection['collectionId'] ){
			modalService.showModal({}, {    	            	           
		           headerText: "Collection Id not set",
		           bodyText: "Make sure you choose a valid collection id"
	    	 });
			return;
		}
		
		$http.get('subCollectionAddUpdate.do', {
			params:{	
				subcollectionId: $scope.form.subcollectionId,
				locationInStorage: $scope.form.locationInStorage,
				oldId: $scope.form.oldId,				
				storageType: $scope.form.storageType,
				hazardous: $scope.form.hazardous,
				source: $scope.form.source,
				totalPallet: $scope.form.totalPallet,
				collectionId: $scope.form.rsCollection['collectionId'],
				disposedInsufficientInfo : $scope.form.disposedInsufficientInfo,
				previousPalletId : $scope.form.previousPalletId
			}
		})
		.then(function(response) {			
			if($scope.form.subcollectionId){
				$.extend($scope.gridApi.selection.getSelectedRows()[0],response.data)	
				modalService.showModal({}, {    	            	           
			           headerText: "Record Update",
			           bodyText: "Your record has successfully updated"
		    	 });
			}else{				
				$scope.gridOptions.data.push(response.data)
				$scope.resetForm();		
			}
			
			
		  }, function(response) {
			  modalService.showModal({}, {    	            	           
		           headerText: response.data.header,
		           bodyText: response.data.message
	    	 });
		  });
		  
	}

	
	$scope.gridOptions.columnDefs = [	                              
                                 	 { field: 'id',displayName: 'id',width:50 },
	                                 { field: 'subcollectionId',displayName: 'subCollection id',width:150 },
	                                 { field: "rsCollection['collectionId']",displayName: 'collection Id', width:150 },
	                                 { field: 'igsn',displayName: 'igsn',width:150 },
	                                 { field: 'locationInStorage',displayName: 'Store loc',width:150 },	                                 
	                                 { field: 'oldId',displayName: 'old ID',width:130},
	                                 { field: "sampleRangeBySubcollection['minContainer']",displayName: 'Container From',width:180},
	                                 { field: "sampleRangeBySubcollection['maxContainer']",displayName: 'Container To',width:155 },
	                                 { field: "sampleRangeBySubcollection['minSample']",displayName: 'Sample From',width:150 },
	                                 { field: "sampleRangeBySubcollection['maxSample']",displayName: 'Sample To',width:170 },
	                                 { field: 'storageType',displayName: 'Storage Type',width:170 },
	                                 { field: 'hazardous',displayName: 'Hazardous',width:170 },
	                                 { field: 'source',displayName: 'source',width:170 },
	                                 { field: 'totalPallet',displayName: 'Pallet Weight',width:170 },
	                                 { field: 'previousPalletId',displayName: 'Previous Pallet',width:170 },
	                                 { field: 'disposedInsufficientInfo',displayName: 'Insufficient Info',width:170 }
	                               ];
	                              
   $scope.gridOptions.multiSelect = false;
   $scope.gridOptions.modifierKeysToMultiSelect = false;
   $scope.gridOptions.noUnselect = true;
   
   $scope.gridOptions.onRegisterApi = function( gridApi ) {
     $scope.gridApi = gridApi;       
     gridApi.selection.on.rowSelectionChanged($scope, function(){
    	var selectedRow=jQuery.extend(true, {}, $scope.gridApi.selection.getSelectedRows()[0]);    	
    	 $scope.form = selectedRow
     });
     
     gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
         $scope.paginationOptions.pageNumber = newPage;
         $scope.paginationOptions.pageSize = pageSize;
         $scope.searchSubCollection(newPage,pageSize,false);
       });
     $scope.searchSubCollection($scope.paginationOptions.pageNumber,$scope.paginationOptions.pageSize,true);
     
   };
   
   $scope.searchSubCollection = function(page,pageSize, updateCount){
	
  	 var params ={	
  			collectionId: $scope.search.collectionId,
  			oldId : $scope.search.oldId,
  			locationInStorage:$scope.search.locationInStorage,
  			storageType: $scope.search.storageType, 			
			source : $scope.search.source,
			igsn : $scope.search.igsn,
			pageNumber:page,
			pageSize:pageSize
	}
		
	//VT: Actual results
	$http.get('searchSubCollections.do',{
		params:params
	 })     
     .success(function(data) {
       $scope.gridOptions.data = data;       
       $scope.toggleFilter=false;
      
     })
     .error(function(data, status) {    	
    	 modalService.showModal({}, {    	            	           
	           headerText: "Error loading data:" + status ,
	           bodyText: "Please contact cg-admin@csiro.au if this persist"
    	 });
    	       
     })
     
     if(updateCount){
    	//VT: Get the count of the result
         $http.get('searchSubCollectionsCount.do',{
    		params:params
    	 })     
         .success(function(data) {
        	 $scope.gridOptions.totalItems = data;       	        
         })
         .error(function(data, status) {    	
        	 modalService.showModal({}, {    	            	           
    	           headerText: "Error loading data:" + status ,
    	           bodyText: "Please contact cg-admin@csiro.au if this persist"
        	 });	       
         })
     }
     
   }
   
   //VT: Only run once on load.
   if($routeParams.subCollectionId){
	   $http.get('getSubCollections.do',{
		   params:{
			   subCollectionId : $routeParams.subCollectionId
		   }
	   })     
	   .success(function(data) {
	  	 	$scope.form = data[0];
	   })
   }

   $scope.openSearch = function(){
  	 var promise = SearchCollectionService.open();
  	 promise.then(function(selectedItem) { 
		 if(!$scope.form.rsCollection){
			$scope.form.rsCollection=[];
		 }
  			$scope.form.rsCollection['collectionId']=selectedItem;
  		}, function(reason) {
  		  alert('Failed: ' + reason);
  		});
   }
   
   $scope.openSearchLeftPanel = function(){
	   $scope.toggleFilter=false;
	  	 var promise = SearchCollectionService.open();
	  	 promise.then(function(selectedItem) { 			 
	  		$scope.search.collectionId=selectedItem;
	  		$scope.toggleFilter=true;
	  		}, function(reason) {
	  		  alert('Failed: ' + reason);
	  		});
	   }
   
   $scope.resetSearchForm = function(){
  	 $scope.search={};
  	 $scope.searchSubCollection($scope.paginationOptions.pageNumber,$scope.paginationOptions.pageSize,true);
  	 $scope.toggleFilter=false;
   }
   
}]);