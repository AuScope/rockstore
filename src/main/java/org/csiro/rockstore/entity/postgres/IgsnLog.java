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
@Table(name = "igsn_log")
public class IgsnLog implements java.io.Serializable{
	
	private int id;
	private String igsn;
	private String handle;
	
	public IgsnLog(){}
	
	public IgsnLog(String igsn,String handle){
		this.igsn = igsn;
		this.handle = handle;
		
	}
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@SequenceGenerator(name="igsn_log_id_seq",sequenceName="igsn_log_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="igsn_log_id_seq")
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	@Column(name = "igsn")
	public String getIgsn() {
		return this.igsn;
	}

	public void setIgsn(String igsn) {
		this.igsn = igsn;
	}
	
	@Column(name = "handle")
	public String getHandle() {
		return this.handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}
	
	

}
