package org.csiro.rockstore.entity.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.csiro.rockstore.entity.postgres.ImportBatch;
import org.csiro.rockstore.entity.postgres.ImportLog;
import org.csiro.rockstore.entity.postgres.RsSample;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
import org.csiro.rockstore.entity.postgres.Staff;
import org.csiro.rockstore.entity.postgres.User;
import org.csiro.rockstore.exceptions.ImportExceptionCode;
import org.csiro.rockstore.exceptions.ImportExceptions;
import org.csiro.rockstore.exceptions.ValidationList;
import org.csiro.rockstore.utilities.ImportUtilities;
import org.csiro.rockstore.utilities.NullUtilities;
import org.csiro.rockstore.utilities.SpatialUtilities;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Point;

@Service
public class ExcelImportService {
	
	SubCollectionEntityService subCollectionEntityService;
	CollectionEntityService collectionEntityService;
	SampleEntityService sampleEntityService;
	ImportEntityService importEntityService;
	List<User> users;
	List<Staff> staffs;
	ValidationList validationList;
	
	@Autowired
	public ExcelImportService(CollectionEntityService collectionEntityService, SubCollectionEntityService subCollectionEntityService,
			SampleEntityService sampleEntityService,ListManagerEntityService listManagerEntityService,ImportEntityService importEntityService){
		this.subCollectionEntityService=subCollectionEntityService;
		this.collectionEntityService =collectionEntityService;
		this.sampleEntityService = sampleEntityService;
		this.importEntityService = importEntityService;
		this.users = listManagerEntityService.getAllUser(null);
		this.staffs = listManagerEntityService.getAllStaff(null);
		this.validationList = new ValidationList();
	}
	
	public ExcelImportService(){
		
		ListManagerEntityService listManagerEntityService;
		
		this.subCollectionEntityService=new SubCollectionEntityService();
		listManagerEntityService=new ListManagerEntityService();
		this.sampleEntityService = new SampleEntityService();
		this.users = listManagerEntityService.getAllUser(null);
		this.staffs = listManagerEntityService.getAllStaff(null);
		this.validationList = new ValidationList();
		this.importEntityService = new ImportEntityService();
	}

	
	public void processExcel(FileInputStream inputFile, String user) throws IOException {
		
		ImportBatch batch= new ImportBatch(new Date(),null,0,user);
		this.importEntityService.persist(batch);
		
        FileInputStream file = inputFile;           
        XSSFWorkbook workbook = new XSSFWorkbook(file);           
        XSSFSheet sheet = workbook.getSheetAt(0);
             
        Iterator<Row> rowIterator = sheet.iterator();
        
        int processRow=0;
        HashMap<String,Integer> headers = null;
        
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            processRow++;
            if(processRow>5001){  
            	ImportExceptions ie = new ImportExceptions(ImportExceptionCode.EXCEED_5000_ROW_BATCH,"Not Applicable",5002);
            	batch.setError(batch.getError()+1);
            	this.importEntityService.merge(batch);
            	ImportLog log= new ImportLog(ie.getRow(),ie.getColumn(),ie.getImportError(),batch);
        		this.importEntityService.persist(log);
        		
            }
            
            //VT: We assume the first row to be the header.
            
            if(processRow==1){
            	 headers = this.processHeader(row);            	
            }else{
            	RsSample rs=null;
            	try{
	            	rs=createSample(row,headers, user);
	            	this.sampleEntityService.persist(rs);
	            	
	            	//VT: Set where the id starts and end
	            	if(batch.getStartId()==null){
	            		batch.setStartId(rs.getId());
	            	}
	            	
	            	if(!rowIterator.hasNext()){
	            		batch.setEndId(rs.getId());
	            	}
            	}catch(ImportExceptions ie){
            		batch.setError(batch.getError()+1);
            		this.importEntityService.merge(batch);
            		ImportLog log= new ImportLog(ie.getRow(),ie.getColumn(),ie.getImportError(),batch);
            		this.importEntityService.persist(log);
            	}catch(Exception e){
            		batch.setError(batch.getError()+1);
            		this.importEntityService.merge(batch);
            		ImportLog log= new ImportLog(row.getRowNum()+1,"Unknown","UnExpected Exception",batch);
            		this.importEntityService.persist(log);
            	}
            	
            	
            }
                           
        }
        file.close();
        batch.setEndTime(new Date());
        this.importEntityService.merge(batch);
        workbook.close();
	}
	
	public List<RsSample> generatePreview(FileInputStream inputFile, String user) throws IOException, NoSuchAuthorityCodeException, ParseException, FactoryException, ImportExceptions{
		
        FileInputStream file = inputFile;           
        XSSFWorkbook workbook = new XSSFWorkbook(file);           
        XSSFSheet sheet = workbook.getSheetAt(0);
        
        List<RsSample> result = new ArrayList<RsSample>();
             
        Iterator<Row> rowIterator = sheet.iterator();
        
        int processRow=0;
        HashMap<String,Integer> headers = null;
        
        //VT: Generating no more than 30 for the preview;
        while (rowIterator.hasNext() && processRow < 30)
        {
            Row row = rowIterator.next();
            processRow++;            
            //VT: We assume the first row to be the header.            
            if(processRow==1){
            	 headers = this.processHeader(row);            	
            }else{
            	result.add(createSample(row,headers, user));
            }
                           
        }
        file.close();
        workbook.close();
        return result;
	}
	
	public RsSample createSample(Row row , HashMap<String,Integer> headers, String user) throws ParseException, NoSuchAuthorityCodeException, FactoryException, ImportExceptions{
		
		RsSubcollection rsc=null;
		try{
			 rsc = subCollectionEntityService.search(getCellValue(row,headers,"Subcollection_ID"));
		}catch(Exception e){
			throw new ImportExceptions(ImportExceptionCode.INVALID_SUBCOLLECTION_ID,"Subcollection_ID",row.getRowNum());
		}
		
		String datumEPSGFormat = ImportUtilities.DATUM_TABLE.get(getCellValue(row,headers,"Datum").toUpperCase());
		
		if(datumEPSGFormat==null || datumEPSGFormat.isEmpty()){
			throw new ImportExceptions(ImportExceptionCode.DATUM_OUTSIDE_LIST,"Datum",row.getRowNum());
		}
		
		
		Double lat=getNumericCellValue(row,headers,"Northing_MGA");
		Double lon=getNumericCellValue(row,headers,"Easting_MGA");
		if(lat==null || lon==null || lat==0 || lon==0 || lat.isNaN() || lon.isNaN()){
			throw new ImportExceptions(ImportExceptionCode.INVALID_LAT_LON,"Northing_MGA or Easting_MGA",row.getRowNum());
		}

		Point p = (Point)(SpatialUtilities.wktToGeometry(lat, lon,datumEPSGFormat));
		
		String sampleType=validationList.matchSampleType(getCellValue(row,headers,"Sample_type"));
		
		if(sampleType==null){
			throw new ImportExceptions(ImportExceptionCode.SAMPLE_TYPE_OUTSIDE_LIST,"Sample_type",row.getRowNum());
		}
		
		String sampleCollector=matchUser(getCellValue(row,headers,"Sample_Collector"));
		if(sampleCollector==null){
			throw new ImportExceptions(ImportExceptionCode.INVALID_SAMPLE_COLLECTOR,"Sample_Collector",row.getRowNum());
		}
		
		String staffDisposed= matchStaff(getCellValue(row,headers,"Staff_ID_disposed"));
		if(staffDisposed==null){
			throw new ImportExceptions(ImportExceptionCode.INVALID_STAFF_ID,"Staff_ID_disposed",row.getRowNum());
		}
		
		
		RsSample rs= new RsSample(rsc,  getCellValue(row,headers,"CSIRO_sample_ID"),
				sampleType,  getCellValue(row,headers,"BHID"),  getNumericCellValue(row,headers,"Depth_m"),  datumEPSGFormat,
				getCellValue(row,headers,"Container_ID"),  getCellValue(row,headers,"External_reference"),
				sampleCollector,NullUtilities.parseExcelDateAllowNull(getNumericCellValue(row,headers,"Date_sampled")) , getBooleanCellValue(row,headers,"Sample_disposed"),
				NullUtilities.parseExcelDateAllowNull(getNumericCellValue(row,headers,"Date_disposed")), staffDisposed, p, 
				String.valueOf(getNumericCellValue(row,headers,"Northing_MGA")),  String.valueOf(getNumericCellValue(row,headers,"Easting_MGA")), user);
           
		return rs;
       
	}
	
	private String matchStaff(String staff){
		if(staff.isEmpty()){
			return "";
		}
		for(Staff s:staffs){
			if(s.getContactName().toUpperCase().equals(staff.toUpperCase())){
				return s.getContactName();
			}
		}
		return null;
	}
	
	private String matchUser(String user){
		if(user.isEmpty()){
			return "";
		}
		for(User u:users){
			if(u.getContactName().toUpperCase().equals(user.toUpperCase())){
				return u.getContactName();
			}
		}
		return null;
	}
	
	public  String getCellValue(Row row,HashMap<String,Integer> headers,String columnName) throws ImportExceptions{
		try{
			return row.getCell(headers.get(columnName.toUpperCase())).getStringCellValue();
		}catch(NullPointerException e){
			return "";
		}catch(IllegalStateException e){
			try{
				return String.valueOf(row.getCell(headers.get(columnName.toUpperCase())).getNumericCellValue());
			}catch(Exception e1){
				throw e1;
			}			
		}catch(Exception e){
			throw new ImportExceptions(ImportExceptionCode.INVALID_CELL_FORMAT,columnName,row.getRowNum());
		}
	}
	
	public  Double getNumericCellValue(Row row,HashMap<String,Integer> headers,String columnName) throws ImportExceptions{
		try{
			return row.getCell(headers.get(columnName.toUpperCase())).getNumericCellValue();
		}catch(NullPointerException e){
			return null;
		}catch(Exception e){
			throw new ImportExceptions(ImportExceptionCode.INVALID_CELL_FORMAT,columnName,row.getRowNum());
		}
	}
	
	public  Boolean getBooleanCellValue(Row row,HashMap<String,Integer> headers,String columnName) throws ImportExceptions{
		try{
			return row.getCell(headers.get(columnName.toUpperCase())).getBooleanCellValue();
		}catch(NullPointerException e){
			return null;
		}catch(Exception e){
			throw new ImportExceptions(ImportExceptionCode.INVALID_CELL_FORMAT,columnName,row.getRowNum());
		}
	}
	
	
	public HashMap<String,Integer> processHeader(Row row){
		
		HashMap<String,Integer> headers = new HashMap<String,Integer>();
		
		Iterator<Cell> cellIterator = row.cellIterator();
		
		int column = -1;
        
        while (cellIterator.hasNext())
        {
            Cell cell = cellIterator.next();
            column++;
            //Check the cell type and format accordingly
            switch (cell.getCellType())
            {
                case Cell.CELL_TYPE_NUMERIC:                    
                    headers.put(String.valueOf(cell.getNumericCellValue()).toUpperCase(),column);
                    break;
                case Cell.CELL_TYPE_STRING:
                	headers.put(String.valueOf(cell.getStringCellValue()).toUpperCase(),column);
                    break;
            }
        }
        return headers;
		
	}
	
	public static void main(String [] args) throws NoSuchAuthorityCodeException, IOException, ParseException, FactoryException{
		
	
		 FileInputStream file = new FileInputStream(new File("C:\\Users\\tey006\\Documents\\request_form.xlsx"));
		 ExcelImportService s= new ExcelImportService();
		 s.processExcel(file,"tey006");
		 //System.out.println(s.generatePreview(file,"tey006"));
		
		 
		
		
	}

}
