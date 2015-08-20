package org.csiro.rockstore.entity.postgres;

// Generated 12/08/2015 3:14:54 PM by Hibernate Tools 4.3.1

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vividsolutions.jts.geom.Point;

/**
 * RsSample generated by hbm2java
 */
@Entity
@Table(name = "rs_sample")
@NamedQueries({
	@NamedQuery(
			name="RsSample.findSampleById",
		    query="SELECT c FROM RsSample c WHERE c.id = :id"
		),
	@NamedQuery(
			name="RsSample.getAllSample",
		    query="SELECT c FROM RsSample c"
		)
		
})	
public class RsSample implements java.io.Serializable {

	private int id;
	private RsCollection rsCollection;
	private RsSubcollection rsSubcollection;
	private String igsn;
	private String csiroSampleId;
	private String sampleType;
	private String bhid;
	private Double depth;
	private String datum;
	private String zone;
	private String containerId;
	private String externalRef;
	private String sampleCollector;
	private Date dateSampled;
	private Boolean sampleDispose;
	private Date dateDisposed;
	private String staffidDisposed;
	private Point location;

	public RsSample() {
	}


	public RsSample( RsCollection rsCollection,
			RsSubcollection rsSubcollection, String igsn, String csiroSampleId,
			String sampleType, String bhid, Double depth, String datum,
			String zone, String containerId, String externalRef,
			String sampleCollector, Date dateSampled, Boolean sampleDispose,
			Date dateDisposed, String staffidDisposed, Point location) {		
		this.rsCollection = rsCollection;
		this.rsSubcollection = rsSubcollection;
		this.igsn = igsn;
		this.csiroSampleId = csiroSampleId;
		this.sampleType = sampleType;
		this.bhid = bhid;
		this.depth = depth;
		this.datum = datum;
		this.zone = zone;
		this.containerId = containerId;
		this.externalRef = externalRef;
		this.sampleCollector = sampleCollector;
		this.dateSampled = dateSampled;
		this.sampleDispose = sampleDispose;
		this.dateDisposed = dateDisposed;
		this.staffidDisposed = staffidDisposed;
		this.location = location;
	}
	
	public RsSample update(RsCollection rsCollection,
			RsSubcollection rsSubcollection, String igsn, String csiroSampleId,
			String sampleType, String bhid, Double depth, String datum,
			String zone, String containerId, String externalRef,
			String sampleCollector, Date dateSampled, Boolean sampleDispose,
			Date dateDisposed, String staffidDisposed, Point location) {		
		this.setRsCollection(rsCollection);
		this.setRsSubcollection(rsSubcollection);
		this.setIgsn(igsn);
		this.setCsiroSampleId(csiroSampleId);
		this.setSampleType(sampleType);
		this.setBhid(bhid);
		this.setDepth(depth);
		this.setDatum(datum);
		this.setZone(zone);
		this.setContainerId(containerId);
		this.setExternalRef(externalRef);
		this.setSampleCollector(sampleCollector);
		this.setDateSampled(dateSampled);
		this.setSampleDispose(sampleDispose);
		this.setDateDisposed(dateDisposed);
		this.setStaffidDisposed(staffidDisposed);
		this.setLocation(location);
		return this;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@SequenceGenerator(name="rs_sample_id_seq",sequenceName="rs_sample_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="rs_sample_id_seq")
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "collection_id",referencedColumnName="collection_id")
	@JsonManagedReference
	public RsCollection getRsCollection() {
		return this.rsCollection;
	}

	public void setRsCollection(RsCollection rsCollection) {
		this.rsCollection = rsCollection;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subcollection_id",referencedColumnName="subcollection_id")
	@JsonManagedReference
	public RsSubcollection getRsSubcollection() {
		return this.rsSubcollection;
	}

	public void setRsSubcollection(RsSubcollection rsSubcollection) {
		this.rsSubcollection = rsSubcollection;
	}

	@Column(name = "igsn", length = 200)
	public String getIgsn() {
		return this.igsn;
	}

	public void setIgsn(String igsn) {
		this.igsn = igsn;
	}

	@Column(name = "csiro_sample_id", length = 200)
	public String getCsiroSampleId() {
		return this.csiroSampleId;
	}

	public void setCsiroSampleId(String csiroSampleId) {
		this.csiroSampleId = csiroSampleId;
	}

	@Column(name = "sample_type", length = 50)
	public String getSampleType() {
		return this.sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	@Column(name = "bhid")
	public String getBhid() {
		return this.bhid;
	}

	public void setBhid(String bhid) {
		this.bhid = bhid;
	}

	@Column(name = "depth", precision = 17, scale = 17)
	public Double getDepth() {
		return this.depth;
	}

	public void setDepth(Double depth) {
		this.depth = depth;
	}

	@Column(name = "datum", length = 50)
	public String getDatum() {
		return this.datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	@Column(name = "zone", length = 50)
	public String getZone() {
		return this.zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	@Column(name = "container_id", length = 100)
	public String getContainerId() {
		return this.containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	@Column(name = "external_ref", length = 200)
	public String getExternalRef() {
		return this.externalRef;
	}

	public void setExternalRef(String externalRef) {
		this.externalRef = externalRef;
	}

	@Column(name = "sample_collector", length = 200)
	public String getSampleCollector() {
		return this.sampleCollector;
	}

	public void setSampleCollector(String sampleCollector) {
		this.sampleCollector = sampleCollector;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_sampled", length = 29)
	public Date getDateSampled() {
		return this.dateSampled;
	}

	public void setDateSampled(Date dateSampled) {
		this.dateSampled = dateSampled;
	}

	@Column(name = "sample_dispose")
	public Boolean getSampleDispose() {
		return this.sampleDispose;
	}

	public void setSampleDispose(Boolean sampleDispose) {
		this.sampleDispose = sampleDispose;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_disposed", length = 29)
	public Date getDateDisposed() {
		return this.dateDisposed;
	}

	public void setDateDisposed(Date dateDisposed) {
		this.dateDisposed = dateDisposed;
	}

	@Column(name = "staffid_disposed", length = 100)
	public String getStaffidDisposed() {
		return this.staffidDisposed;
	}

	public void setStaffidDisposed(String staffidDisposed) {
		this.staffidDisposed = staffidDisposed;
	}

	@Column(name = "location")
	@Type(type="org.hibernate.spatial.GeometryType")
	@JsonIgnore
	public Point getLocation() {
		return this.location;
	}
	
	
	@Transient
	public Double getLon() {
		return this.getLocation().getX();
	}
	
	@Transient
	public Double getLat() {
		return this.getLocation().getY();
	}

	public void setLocation(Point location) {
		this.location = location;
	}

}
