package org.csiro.rockstore.entity.postgres;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "import_log")
@NamedQueries({
	@NamedQuery(
			name="ImportLog.listBatchLog",
		    query="SELECT il FROM ImportLog il WHERE il.importBatch.id = :batchId order by il.id"
	)	
})	
public class ImportLog {
	
	private int id;
	private Integer excelRow;
	private String excelColumn;
	private String errorMessage;
	private ImportBatch importBatch;
	
	public ImportLog(){}
	
	public ImportLog(Integer excelRow,String column,String errorMessage, ImportBatch importBatch){
		this.excelRow = excelRow;
		this.excelColumn = column;
		this.errorMessage = errorMessage;
		this.importBatch = importBatch;
	}
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@SequenceGenerator(name="import_log_id_seq",sequenceName="import_log_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="import_log_id_seq")
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	@Column(name = "excel_row")
	public Integer getExcelRow() {
		return this.excelRow;
	}

	public void setExcelRow(Integer excelRow) {
		this.excelRow = excelRow;
	}
	
	@Column(name = "excel_column")
	public String getExcelColumn() {
		return this.excelColumn;
	}

	public void setExcelColumn(String excelColumn) {
		this.excelColumn = excelColumn;
	}
	
	@Column(name = "error_message", length = 100)
	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch",referencedColumnName="id")
	@Fetch(FetchMode.JOIN) 	
	public ImportBatch getImportBatch() {
		return this.importBatch;
	}

	public void setImportBatch(ImportBatch importBatch) {
		this.importBatch = importBatch;
	}

}
