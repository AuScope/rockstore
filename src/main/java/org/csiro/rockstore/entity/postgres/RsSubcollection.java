package org.csiro.rockstore.entity.postgres;

// Generated 12/08/2015 3:14:54 PM by Hibernate Tools 4.3.1



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * RsSubcollection generated by hbm2java
 */
@Entity
@Table(name = "rs_subcollection")
@NamedQueries({
	@NamedQuery(
			name="RsSubcollection.findSubCollectionById",
		    query="SELECT c FROM RsSubcollection c INNER JOIN FETCH c.rsCollection LEFT JOIN FETCH c.sampleRangeBySubcollection WHERE c.subcollectionId = :subcollectionId"
	),
	@NamedQuery(
			name="RsSubcollection.getAllSubCollection",
		    query="SELECT c FROM RsSubcollection c INNER JOIN FETCH c.rsCollection LEFT JOIN FETCH c.sampleRangeBySubcollection"
	),
	@NamedQuery(
			name="RsSubcollection.findSubCollectionByCollection",
		    query="SELECT c FROM RsSubcollection c INNER JOIN FETCH c.rsCollection LEFT JOIN FETCH c.sampleRangeBySubcollection WHERE c.rsCollection.collectionId = :collectionId"
	),
	@NamedQuery(
			name="RsSubcollection.findSubCollectionByIGSN",
		    query="SELECT c FROM RsSubcollection c INNER JOIN FETCH c.rsCollection LEFT JOIN FETCH c.sampleRangeBySubcollection WHERE c.igsn = :igsn"
	),
	@NamedQuery(
			name="RsSubcollection.getUnminted",
		    query="SELECT c FROM RsSubcollection c INNER JOIN FETCH c.rsCollection LEFT JOIN FETCH c.igsnLog where c.igsnLog.handle is null "
	)
		
})	
public class RsSubcollection implements java.io.Serializable {

	private int id;
	private RsCollection rsCollection;
	private String oldId;
	private String subcollectionId;
	private String igsn;
	private String locationInStorage;	
	private String storageType;
	private Boolean hazardous;
	private String source;
	private Integer totalPallet;	
	private SampleRangeBySubcollection sampleRangeBySubcollection;
	private String lastUpdateUser;
	private String previousPalletId;
	private boolean disposedInsufficientInfo;
	private IGSNLog igsnLog;

	public RsSubcollection() {
	}

	

	public RsSubcollection(RsCollection rsCollection, String oldId,
			 String locationInStorage, String storageType, Boolean hazardous,
			String source, Integer totalPallet, String lastUpdateUser, String previousPalletId, boolean disposedInsufficientInfo) {
		
		this.rsCollection = rsCollection;
		this.oldId = oldId;		
		this.locationInStorage = locationInStorage;		
		this.storageType = storageType;
		this.hazardous = hazardous;
		this.source = source;
		this.totalPallet = totalPallet;
		this.lastUpdateUser = lastUpdateUser;
		this.previousPalletId = previousPalletId;
		this.disposedInsufficientInfo = disposedInsufficientInfo;
		
	}
	
	public RsSubcollection update(RsCollection rsCollection, String oldId,
			 String locationInStorage, String storageType, Boolean hazardous,
			String source, Integer totalPallet,String lastUpdateUser, String previousPalletId, boolean disposedInsufficientInfo) {				
		this.setRsCollection(rsCollection);
		this.setOldId(oldId);
		this.setLocationInStorage(locationInStorage);		
		this.setStorageType(storageType);
		this.setHazardous(hazardous);
		this.setSource(source);
		this.setTotalPallet(totalPallet);
		this.setLastUpdateUser(lastUpdateUser);
		this.setPreviousPalletId(previousPalletId);
		this.setDisposedInsufficientInfo(disposedInsufficientInfo);
		return this;
	}

	@Id
	@Column(name = "id",updatable=false, unique = true, nullable = false)
	@SequenceGenerator(name="rs_subcollection_id_seq",sequenceName="rs_subcollection_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="rs_subcollection_id_seq")
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "collection_id", referencedColumnName="collection_id", nullable = false)
	@Fetch(FetchMode.JOIN) 
	@JsonManagedReference
	public RsCollection getRsCollection() {
		return this.rsCollection;
	}

	public void setRsCollection(RsCollection rsCollection) {
		this.rsCollection = rsCollection;
	}

	@Column(name = "old_id", length = 100)
	public String getOldId() {
		return this.oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
	
    @Column(insertable=false,updatable=false, name = "igsn", unique = true, length = 100) 
    @Generated(GenerationTime.INSERT)
	public String getIgsn() {
		return this.igsn;
	}
	
	public String setIgsn(String igsn) {
		return this.igsn = igsn;
	}
	

	@Column(insertable=false,updatable=false, name = "subcollection_id", unique = true, length = 100)
    @Generated(GenerationTime.INSERT)
	public String getSubcollectionId() {
		return this.subcollectionId;
	}

	public void setSubcollectionId(String subcollectionId) {
		this.subcollectionId = subcollectionId;
	}

	@Column(name = "location_in_storage", length = 100)
	public String getLocationInStorage() {
		return this.locationInStorage;
	}

	public void setLocationInStorage(String locationInStorage) {
		this.locationInStorage = locationInStorage;
	}
	
	@Column(name = "storage_type", length = 100)
	public String getStorageType() {
		return this.storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	@Column(name = "hazardous")
	public Boolean getHazardous() {
		return this.hazardous;
	}

	public void setHazardous(Boolean hazardous) {
		this.hazardous = hazardous;
	}

	@Column(name = "source", length = 200)
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "total_pallet")
	public Integer getTotalPallet() {
		return this.totalPallet;
	}

	public void setTotalPallet(Integer totalPallet) {
		this.totalPallet = totalPallet;
	}

	
	@OneToOne
	@NotFound(action=NotFoundAction.IGNORE)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(insertable=false, updatable = false, name = "subcollection_id", referencedColumnName="subcollection_id")	
	public SampleRangeBySubcollection getSampleRangeBySubcollection() {
		return this.sampleRangeBySubcollection;
	}
	
	public void setSampleRangeBySubcollection(SampleRangeBySubcollection sampleRangeBySubcollection) {
		 this.sampleRangeBySubcollection = sampleRangeBySubcollection;
	}
	
	@Column(name = "last_update_user")
	public String getLastUpdateUser() {
		return this.lastUpdateUser;
	}
	
	public void  setLastUpdateUser(String lastUpdateUser) {
		 this.lastUpdateUser=lastUpdateUser;
	}
	
	
	@Column(name = "previous_pallet_id")
	public String getPreviousPalletId() {
		return this.previousPalletId;
	}
	
	public void setPreviousPalletId(String previousPalletId) {
		 this.previousPalletId = previousPalletId;
	}
	
	@Column(name = "disposed_insufficient_info")
	public boolean getDisposedInsufficientInfo() {
		return this.disposedInsufficientInfo;
	}
	
	public void setDisposedInsufficientInfo(boolean disposedInsufficientInfo) {
		 this.disposedInsufficientInfo = disposedInsufficientInfo;
	}


	@OneToOne
	@NotFound(action=NotFoundAction.IGNORE)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(insertable=false,updatable=false,name = "igsn", referencedColumnName="igsn")	
	public IGSNLog getIgsnLog() {
		return igsnLog;
	}



	public void setIgsnLog(IGSNLog igsnLog) {
		this.igsnLog = igsnLog;
	}
	
	

}
