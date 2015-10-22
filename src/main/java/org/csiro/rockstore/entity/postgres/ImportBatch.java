package org.csiro.rockstore.entity.postgres;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "import_batch")
@NamedQueries({
	@NamedQuery(
			name="ImportBatch.listBatch",
		    query="SELECT ib FROM ImportBatch ib  WHERE ib.batchUser = :user order by ib.id"
	)		
})
public class ImportBatch {
	
	private int id;
	private Date startTime;
	private Date endTime;
	private Integer error;
	private String batchUser;
	private Integer startId;
	private Integer endId;
	
	public ImportBatch(){}
	
	public ImportBatch(Date startTime, Date endTime, int error, String batchUser){
		this.startTime=startTime;
		this.endTime= endTime;
		this.error = error;
		this.batchUser = batchUser;		
		this.error=0;
	}
	

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@SequenceGenerator(name="import_batch_id_seq",sequenceName="import_batch_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="import_batch_id_seq")
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", length = 29)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", length = 29)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "error")
	public Integer getError() {
		return this.error;
	}

	public void setError(Integer error) {
		this.error = error;
	}
	

	@Column(name = "batch_user", length = 100)
	public String getBatchUser() {
		return this.batchUser;
	}

	public void setBatchUser(String batchUser) {
		this.batchUser = batchUser;
	}
	
	
	@Column(name = "start_id")
	public Integer getStartId() {
		return this.startId;
	}

	public void setStartId(Integer startId) {
		this.startId = startId;
	}
	
	@Column(name = "end_id")
	public Integer getEndId() {
		return this.endId;
	}

	public void setEndId(Integer endId) {
		this.endId = endId;
	}

}
