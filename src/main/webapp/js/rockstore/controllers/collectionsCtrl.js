
angular.module('app').controller('CollectionCtrl', ['$scope','$rootScope','$http','DropDownValueService','$filter',
                                                    function ($scope,$rootScope,$http,DropDownValueService,$filter) {
	
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false };
	$scope.gridOptions.data = [];

	$scope.users = DropDownValueService.getUsers();
	
	$scope.booleans = DropDownValueService.getBoolean();
	
	
	$scope.form ={};
	
	$scope.resetForm = function(){
		$scope.form ={			
				collectionId : '' ,
				project : '',	
				staffIdFieldManager:$scope.form.staffIdFieldManager,
				staffidResponsible : $scope.form.staffidResponsible,
				projectResult: '',
				projectPublication : '',
				projectCloseDate :'',
				availableToPublic : true,
				archiveDue :''
		    }
	}
	
	$scope.submit = function(){
		$http.get('addUpdate.do', {
			params:{	
				collectionId: $scope.form.collectionId,
				projectId: $scope.form.project,
				staffFieldManager: $scope.form.staffIdFieldManager,
				staffResponsible: $scope.form.staffidResponsible,
				projectResult: $scope.form.projectResult,
				projectPublication: $scope.form.projectPublication,
				projectCloseDate: $scope.form.projectCloseDate,
				availableToPublic: $scope.form.availableToPublic,
				archiveDue: $scope.form.archiveDue
				}
		})
		.then(function(response) {
			response.data.projectCloseDate=$filter('date')(response.data.projectCloseDate,'d/MMM/yyyy');
			response.data.archiveDue=$filter('date')(response.data.archiveDue,'d/MMM/yyyy');			
			if($scope.form.collectionId){
				$scope.gridApi.selection.getSelectedRows()[0]=response.data
			}else{				
				$scope.gridOptions.data.push(response.data)
			}
			$scope.resetForm();		
			
		  }, function(response) {
		    console.log(response)
		  });
	}

 
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false, enableColumnResizing: true };
	
	$scope.gridOptions.columnDefs = [
	                                 //{field: 'id', displayName: 'Id'},
                                 	 { field: 'id',displayName: 'id',width:50 },
	                                 { field: 'collectionId',displayName: 'collection id',width:150 },
	                                 { field: 'project',displayName: 'project',width:130},
	                                 { field: 'staffIdFieldManager',displayName: 'Field Manager',width:180},
	                                 { field: 'staffidResponsible',displayName: 'Staff Responsible',width:155 },
	                                 { field: 'projectResult',displayName: 'Project Result',width:150 },
	                                 { field: 'projectPublication',displayName: 'publication',width:170 },
	                                 { field: 'projectCloseDate',displayName: 'Close Date',cellFilter :'date:"d/MMM/yyyy"',width:170 },
	                                 { field: 'availableToPublic',displayName: 'public',width:170 },
	                                 { field: 'archiveDue',displayName: 'Archive Due', cellFilter :'date:"d/MMM/yyyy"', width:150 }
	                               ];
	                              
   $scope.gridOptions.multiSelect = false;
   $scope.gridOptions.modifierKeysToMultiSelect = false;
   $scope.gridOptions.noUnselect = true;
   $scope.gridOptions.onRegisterApi = function( gridApi ) {
     $scope.gridApi = gridApi;       
     gridApi.selection.on.rowSelectionChanged($scope, function(){
    	var selectedRow=jQuery.extend(true, {}, $scope.gridApi.selection.getSelectedRows()[0]);
    	selectedRow.projectCloseDate=$filter('date')(selectedRow.projectCloseDate,'d/MMM/yyyy');
    	selectedRow.archiveDue=$filter('date')(selectedRow.archiveDue,'d/MMM/yyyy');
    	 $scope.form = selectedRow
     });
     $http.get('getCollections.do')
     .success(function(data) {
       $scope.gridOptions.data = data;
     });
   };
   
   
  
   
}]);

angular.module('app').controller('SubCollectionCtrl', ['$scope', function ($scope) {
	 
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false };
	
	$scope.gridOptions.columnDefs = [
                                 	 { name: 'ID',width:50 },
	                                 { name: 'Old ID',width:100 },
	                                 { name: 'SubCollectionId',width:160},
	                                 { name: 'Location in Store',width:160},
	                                 { name: 'Container From',width:150 },
	                                 { name: 'Container To',width:145 },
	                                 { name: 'Sample From',width:140 },
	                                 { name: 'Sample To',width:140 },
	                                 { name: 'Storage Type',width:145 },
	                                 { name: 'Hazardous',width:130 },
	                                 { name: 'Collection Id',width:145 },	                                
	                                 { name: 'ARRC Store',width:140 },
	                                 { name: 'Source',width:120 },
	                                 { name: 'Total Pallet',width:150 }
	                               ];
	                              
   $scope.gridOptions.multiSelect = false;
   $scope.gridOptions.modifierKeysToMultiSelect = false;
   $scope.gridOptions.noUnselect = true;
   $scope.gridOptions.onRegisterApi = function( gridApi ) {
     $scope.gridApi = gridApi;
   };
  
  
   
	
   $scope.gridOptions.data = [
	    {
	    	 'ID': 14,
             'Old ID':'',
             'SubCollectionId': 'SC00001',
             'Location in Store':'G2-L3',
             'Container From':'',
             'Container To':'',
             'Sample From':'HM-1A',
             'Sample To':'HM-110C',
             'Storage Type':'STANDARD',
             'Hazardous':'',
             'Collection Id':'C00001',             
             'ARRC Store':'',
             'Source':'',
             'Total Pallet':'1',
	    },
	    {
	    	 'ID': 14,
            'Old ID':'',
            'SubCollectionId': 'SC00001',
            'Location in Store':'G2-L3',
            'Container From':'',
            'Container To':'',
            'Sample From':'HM-1A',
            'Sample To':'HM-110C',
            'Storage Type':'STANDARD',
            'Hazardous':'',
            'Collection Id':'C00001',             
            'ARRC Store':'',
            'Source':'',
            'Total Pallet':'1',
	    },
	    {
	    	 'ID': 14,
            'Old ID':'',
            'SubCollectionId': 'SC00001',
            'Location in Store':'G2-L3',
            'Container From':'',
            'Container To':'',
            'Sample From':'HM-1A',
            'Sample To':'HM-110C',
            'Storage Type':'STANDARD',
            'Hazardous':'',
            'Collection Id':'C00001',             
            'ARRC Store':'',
            'Source':'',
            'Total Pallet':'1',
	    }
    ];
}]);

angular.module('app').controller('SampleCtrl', ['$scope', function ($scope) {
	 
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false};
	
	$scope.gridOptions.columnDefs = [
                                 	 { name: 'Sample Id',width:100 },
	                                 { name: 'IGSN',width:100 },
	                                 { name: 'CSIRO Sample',width:155},
	                                 { name: 'Sample Type',width:140},
	                                 { name: 'BHID',width:80},
	                                 { name: 'Easting',width:110 },
	                                 { name: 'Northing',width:110 },
	                                 { name: 'Depth(m)',width:130 },
	                                 { name: 'Datum',width:120 },
	                                 { name: 'Zone',width:100 },
	                                 { name: 'Container ID',width:150 },
	                                 { name: 'Storage Location',width:165 },
	                                 { name: 'SubCollection Id',width:165 },
	                                 { name: 'External Ref',width:150 },
	                                 { name: 'Sample Collector',width:165 },	                                 
	                                 { name: 'Date Sampled',width:150 },	                               
	                                 { name: 'Sample Disposed',width:165 },
	                                 { name: 'Staff Disposed',width:165 }
	                               ];
	                              
   $scope.gridOptions.multiSelect = false;
   $scope.gridOptions.modifierKeysToMultiSelect = false;
   $scope.gridOptions.noUnselect = true;
   $scope.gridOptions.onRegisterApi = function( gridApi ) {
     $scope.gridApi = gridApi;
   };
  
   
   
	
   $scope.gridOptions.data = [
                      	    {
                      	    	 'Sample Id':'970',
                                 'IGSN':'',
                                 'CSIRO Sample':'HM-1A',
                                 'Sample Type': 'Rock',
                                 'BHID':'',
                                 'Easting':'584076',
                                 'Northing':'7487628',
                                 'Depth(m)':'50',
                                 'Datum':'',
                                 'Zone':'',
                                 'Container ID':'',
                                 'Storage Location':'',	 
                                 'SubCollection Id' :'SC0001',
                                 'External Ref':'White, Fortescue Group regional-scale metasomatism study, CSIRO Data Collection, 2013; White et al., Journal of Petrology, 55, 977-1009, 2014',
                                 'Sample Collector':'Alistair White',	                                 
                                 'Date Sampled':'Alistair White',	                               
                                 'Sample Disposed':'08/08/2015',
                                 'Staff Disposed':''
                      	    },
                      	   {
                     	    	 'Sample Id':'970',
                                'IGSN':'',
                                'CSIRO Sample':'HM-1A',
                                'Sample Type': 'Rock',
                                'BHID':'',
                                'Easting':'584076',
                                'Northing':'7487628',
                                'Depth(m)':'50',
                                'Datum':'',
                                'Zone':'',
                                'Container ID':'',
                                'Storage Location':'',	 
                                'SubCollection Id' :'SC0001',
                                'External Ref':'White, Fortescue Group regional-scale metasomatism study, CSIRO Data Collection, 2013; White et al., Journal of Petrology, 55, 977-1009, 2014',
                                'Sample Collector':'Alistair White',	                                 
                                'Date Sampled':'Alistair White',	                               
                                'Sample Disposed':'08/08/2015',
                                'Staff Disposed':''
                     	    },
                     	   {
                     	    	 'Sample Id':'970',
                                'IGSN':'',
                                'CSIRO Sample':'HM-1A',
                                'Sample Type': 'Rock',
                                'BHID':'',
                                'Easting':'584076',
                                'Northing':'7487628',
                                'Depth(m)':'50',
                                'Datum':'',
                                'Zone':'',
                                'Container ID':'',
                                'Storage Location':'',	 
                                'SubCollection Id' :'SC0001',
                                'External Ref':'White, Fortescue Group regional-scale metasomatism study, CSIRO Data Collection, 2013; White et al., Journal of Petrology, 55, 977-1009, 2014',
                                'Sample Collector':'Alistair White',	                                 
                                'Date Sampled':'Alistair White',	                               
                                'Sample Disposed':'08/08/2015',
                                'Staff Disposed':''
                     	    }
                          ];
}]);

