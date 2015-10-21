package org.csiro.rockstore.exceptions;

public class ImportExceptions extends Throwable{

	private static final long serialVersionUID = -1695345647328255048L;
	
	ImportExceptionCode code;
	String importExceptionMessage;
	String column;
	int row;
	
	
	public ImportExceptions(ImportExceptionCode code,String column,int row){
		this.code=code;
		this.importExceptionMessage=code.getMessage();
		this.column = column;
		this.row=row + 1;//Starts from 0
		
	}
	
	public ImportExceptionCode getCode(){
		return code;
	}
	
	public String getColumn(){
		return column;		
	}
	
	public int getRow(){
		
		return row;
	}
	
	public String getImportError(){
		return importExceptionMessage;
	}

}
