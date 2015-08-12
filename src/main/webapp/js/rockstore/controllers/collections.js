var app = angular.module('app', ['ui.grid','ui.grid.selection']);

app.service('DropDownValueService', function() {
    this.getUsers = function() {
           return [{
        	   value:'lala@gmail.com', 
        	   display:'Jeremy Jones'
        	},{
        		value:'hohoho@hotmail.com', 
        		display:'Warren Buffet'
        	}];
        };
 
    this.getBoolean = function() {
            return [{
            	value:0, 
            	YesNo:'Yes',
            	boolean:'true'
            },{
            	value:1, 
            	YesNo:'No',
            	boolean:'false'
            }];
        };
        
        
    this.getExternalUsers = function() {
        return [{
        	value:'xxxx@gmail.com', 
        	display:'Andy Lau'
        },{
        	value:'yyyy@hotmail.com', 
        	display:'John Lennon'
        }];
     };  
     
     this.getLocations = function() {
         return ['G1-L2','G2-L3','G4-L5'];
      };  
      
      this.getStorageType = function(){
    	  return ['STANDARD','EXTRA','COLD ROOM'];
      }
      
      this.getSampleType = function(){
    	  return ['ROCK','SAND','LIME'];
      }
      
      this.getDatum = function(){
    	  return ['EPSG:4326','GDA84'];
      }
        
});


 
app.controller('CollectionCtrl', ['$scope','$rootScope','DropDownValueService', function ($scope,$rootScope,DropDownValueService) {

	$scope.users = DropDownValueService.getUsers();
	
	$scope.booleans = DropDownValueService.getBoolean();
	
	
	$scope.form ={
	    	 ID			: 0,
	    	 CollectionID : 'SC0001' ,
             ProjectID : '',
             StaffFieldManager: $scope.users[0].value,
             StaffResponsible : $scope.users[0].value,
             ProjectResult: '',
             ProjectPublication : '',
             ProjectCloseDate :'',
             AvailableToPublic :0,
             ArchiveDue :''
	    }
	
	$scope.submit = function(){
		$http.get('/addUpdate.do').
		  then(function(response) {
		    // this callback will be called asynchronously
		    // when the response is available
		  }, function(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		  });
	}

 
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false };
	
	$scope.gridOptions.columnDefs = [
                                 	 { name: 'ID',width:50 },
	                                 { name: 'Collection ID',width:150 },
	                                 { name: 'Project ID',width:130},
	                                 { name: 'Staff Field Manager',width:180},
	                                 { name: 'Staff Responsible',width:155 },
	                                 { name: 'Project Result',width:150 },
	                                 { name: 'Project Publication',width:170 },
	                                 { name: 'Project Close date',width:170 },
	                                 { name: 'Available To Public',width:170 },
	                                 { name: 'Archive Due',width:150 }
	                               ];
	                              
   $scope.gridOptions.multiSelect = false;
   $scope.gridOptions.modifierKeysToMultiSelect = false;
   $scope.gridOptions.noUnselect = true;
   $scope.gridOptions.onRegisterApi = function( gridApi ) {
     $scope.gridApi = gridApi;
   };
  
   $scope.gridOptions.data = [
	    {
	    	 'ID'			: 9,
	    	 'Collection ID' : 'C00001' ,
             'Project ID' : 'Hammersley basin distall footprints',
             'Staff Field Manager': 'AJW',
             'Staff Responsible' : 'AJW',
             'Project Result': 'AJW',
             'Project Publication': '',
             'Project Close date':'',
             'Available To Public':'',
             'Archive Due':''
	    },
	    {
	    	 'ID'			: 9,
	    	 'Collection ID' : 'C00001' ,
            'Project ID' : 'Hammersley basin distall footprints',
            'Staff Field Manager': 'AJW',
            'Staff Responsible' : 'AJW',
            'Project Result': 'AJW',
            'Project Publication': '',
            'Project Close date':'',
            'Available To Public':'',
            'Archive Due':''
	    },
	    {
	    	 'ID'			: 9,
	    	 'Collection ID' : 'C00001' ,
            'Project ID' : 'Hammersley basin distall footprints',
            'Staff Field Manager': 'AJW',
            'Staff Responsible' : 'AJW',
            'Project Result': 'AJW',
            'Project Publication': '',
            'Project Close date':'',
            'Available To Public':'',
            'Archive Due':''
	    }
    ];
}]);

app.controller('SubCollectionCtrl', ['$scope', function ($scope) {
	 
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

app.controller('SampleCtrl', ['$scope', function ($scope) {
	 
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