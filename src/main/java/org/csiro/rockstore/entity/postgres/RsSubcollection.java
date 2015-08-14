package org.csiro.rockstore.entity.postgres;

// Generated 12/08/2015 3:14:54 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * RsSubcollection generated by hbm2java
 */
@Entity
@Table(name = "rs_subcollection", uniqueConstraints = @UniqueConstraint(columnNames = "subcollection_id"))
public class RsSubcollection implements java.io.Serializable {

	private int id;
	private RsCollection rsCollection;
	private String oldId;
	private String subcollectionId;
	private String locationInStorage;
	private String containerFrom;
	private String containerTo;
	private String sampleFrom;
	private String sampleTo;
	private String storageType;
	private Boolean hazardous;
	private String source;
	private Integer totalPallet;
	private Set<RsSample> rsSamples = new HashSet<RsSample>(0);

	public RsSubcollection() {
	}

	public RsSubcollection(int id) {
		this.id = id;
	}

	public RsSubcollection(int id, RsCollection rsCollection, String oldId,
			 String locationInStorage,String containerFrom, String containerTo, String sampleFrom,
			String sampleTo, String storageType, Boolean hazardous,
			String source, Integer totalPallet, Set<RsSample> rsSamples) {
		this.id = id;
		this.rsCollection = rsCollection;
		this.oldId = oldId;		
		this.locationInStorage = locationInStorage;
		this.containerFrom = containerFrom;
		this.containerTo = containerTo;
		this.sampleFrom = sampleFrom;
		this.sampleTo = sampleTo;
		this.storageType = storageType;
		this.hazardous = hazardous;
		this.source = source;
		this.totalPallet = totalPallet;
		this.rsSamples = rsSamples;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@SequenceGenerator(name="rs_subcollection_id_seq",sequenceName="rs_subcollection_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="rs_subcollection_id_seq")
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "collection_id", referencedColumnName="collection_id")
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

	@Column(insertable=false, name = "subcollection_id", unique = true, length = 100)
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

	@Column(name = "container_from", length = 200)
	public String getContainerFrom() {
		return this.containerFrom;
	}

	public void setContainerFrom(String containerFrom) {
		this.containerFrom = containerFrom;
	}

	@Column(name = "container_to", length = 200)
	public String getContainerTo() {
		return this.containerTo;
	}

	public void setContainerTo(String containerTo) {
		this.containerTo = containerTo;
	}

	@Column(name = "sample_from", length = 100)
	public String getSampleFrom() {
		return this.sampleFrom;
	}

	public void setSampleFrom(String sampleFrom) {
		this.sampleFrom = sampleFrom;
	}

	@Column(name = "sample_to", length = 100)
	public String getSampleTo() {
		return this.sampleTo;
	}

	public void setSampleTo(String sampleTo) {
		this.sampleTo = sampleTo;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rsSubcollection")
	public Set<RsSample> getRsSamples() {
		return this.rsSamples;
	}

	public void setRsSamples(Set<RsSample> rsSamples) {
		this.rsSamples = rsSamples;
	}

}
