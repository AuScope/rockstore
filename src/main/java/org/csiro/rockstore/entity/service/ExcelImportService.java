package org.csiro.rockstore.entity.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.csiro.rockstore.entity.postgres.RsSample;
import org.csiro.rockstore.entity.postgres.RsSubcollection;
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
	
	@Autowired
	public ExcelImportService(CollectionEntityService collectionEntityService, SubCollectionEntityService subCollectionEntityService,SampleEntityService sampleEntityService){
		this.subCollectionEntityService=subCollectionEntityService;
		this.collectionEntityService =collectionEntityService;
		this.sampleEntityService = sampleEntityService;
	}
	
	public void readExcel(FileInputStream inputFile, String user){
		try
        {
            FileInputStream file = inputFile;
 
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            
            int processRow=0;
            HashMap<String,Integer> headers = null;
            
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                processRow++;
                
                //VT: We assume the first row to be the header.
                
                if(processRow==1){
                	 headers = this.processHeader(row);
                }else{
                	createNewSample(row,headers, user);
                }
                
                
                
               
            }
            file.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	public void createNewSample(Row row , HashMap<String,Integer> headers, String user) throws ParseException, NoSuchAuthorityCodeException, FactoryException{
		
		RsSubcollection rsc = subCollectionEntityService.search(getCellValue(row,headers,"Subcollection_ID"));
		
		String datumEPSGFormat = ImportUtilities.DATUM_TABLE.get(getCellValue(row,headers,"Datum").toUpperCase());
		
		Point p = (Point)(SpatialUtilities.wktToGeometry(getCellValue(row,headers,"Northing_MGA"), getCellValue(row,headers,"Easting_MGA"),datumEPSGFormat));
		
		RsSample rs= new RsSample(rsc,  getCellValue(row,headers,"CSIRO_sample_ID"),
				getCellValue(row,headers,"Sample_type"),  getCellValue(row,headers,"BHID"),  getNumericCellValue(row,headers,"Depth_m"),  datumEPSGFormat,
				getCellValue(row,headers,"Container_ID"),  getCellValue(row,headers,"External_reference"),
				getCellValue(row,headers,"Sample_Collector"),NullUtilities.parseDateAllowNullddMMyyyy(getCellValue(row,headers,"Date_sampled")) , Boolean.valueOf(getCellValue(row,headers,"Sample_disposed")),
				NullUtilities.parseDateAllowNullddMMyyyy(getCellValue(row,headers,"Date_disposed")),  getCellValue(row,headers,"Staff_ID_disposed"), p, getCellValue(row,headers,"Northing_MGA"),  getCellValue(row,headers,"Easting_MGA"), user);

       	this.sampleEntityService.persist(rs);
	}
	
	public  String getCellValue(Row row,HashMap<String,Integer> headers,String columnName){
		return row.getCell(headers.get(columnName.toUpperCase())).getStringCellValue();
	}
	
	public  Double getNumericCellValue(Row row,HashMap<String,Integer> headers,String columnName){
		return row.getCell(headers.get(columnName.toUpperCase())).getNumericCellValue();
	}
	
	
	public HashMap<String,Integer> processHeader(Row row){
		
		HashMap<String,Integer> headers = new HashMap<String,Integer>();
		
		Iterator<Cell> cellIterator = row.cellIterator();
		
		int column =0;
        
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
	
	public static void main(String [] args) throws FileNotFoundException{
		
		 FileInputStream file = new FileInputStream(new File("C:\\Users\\tey006\\Documents\\request_form.xlsx"));
		
		
		
	}

}
