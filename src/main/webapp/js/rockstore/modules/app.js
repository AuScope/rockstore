var app = angular.module('app', ['ui.grid','ui.grid.selection','ui.grid.resizeColumns']);

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
            	value:true, 
            	YesNo:'Yes'            	
            },{
            	value:false, 
            	YesNo:'No'           	
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



app.directive('jqdatepicker', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
         link: function (scope, element, attrs, ngModelCtrl) {
            element.datepicker({
            	dateFormat: 'd/M/yy',
                onSelect: function (date) {
                	ngModelCtrl.$setViewValue(date);
                	ngModelCtrl.$render();
                    scope.$apply();
                }
            });
        }
    };
});