package org.csiro.rockstore.exceptions;

public class ValidationList {
	
	public ValidationList(){
		
	}
	

	
	public String matchSampleType(String sampleType){
		if(sampleType==null || sampleType.isEmpty()){
			return "";
		}
		String [] list ={				
				"unknown",
				"Chips - in trays",
				"Reference",
				"Crushed",
				"Core",
				"Minus 1 mm",
				"Laterite",
				"Hand",
				"Grab",
				"Mineralised Separate",
				"Bulk",
				"Pulp",
				"Superpan Light Minerals",
				"Mineralised",
				"Hand sample-surface",
				"Hand sample–underground",
				"Soil",
  	          	"Regolith",
  	          	"Vegetation",
  	          	"Water",
  	          	"Termite mound"};
		
		for(String item:list){
			if(item.toUpperCase().equals(sampleType.toUpperCase())){
				return item;
			}
		}
		return null;
		
	}

}
