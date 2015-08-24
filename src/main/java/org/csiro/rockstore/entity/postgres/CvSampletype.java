package org.csiro.rockstore.entity.postgres;

// Generated 04/08/2015 2:17:37 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * CvSampletype generated by hbm2java
 */
@Entity
@Table(name = "cv_sampletype")
public class CvSampletype implements java.io.Serializable {

	private int sampleTypeId;
	private String term;
	private String definition;
	private String link;
	private Set<Sample> samples = new HashSet<Sample>(0);

	public CvSampletype() {
	}

	public CvSampletype(int sampleTypeId, String term) {
		this.sampleTypeId = sampleTypeId;
		this.term = term;
	}

	public CvSampletype(int sampleTypeId, String term, String definition,
			String link, Set<Sample> samples) {
		this.sampleTypeId = sampleTypeId;
		this.term = term;
		this.definition = definition;
		this.link = link;
		this.samples = samples;
	}

	@Id
	@Column(name = "sample_type_id", unique = true, nullable = false)
	@SequenceGenerator(name="cv_sampletype_sample_type_id_seq",sequenceName="cv_sampletype_sample_type_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="cv_sampletype_sample_type_id_seq")
	public int getSampleTypeId() {
		return this.sampleTypeId;
	}

	public void setSampleTypeId(int sampleTypeId) {
		this.sampleTypeId = sampleTypeId;
	}

	@Column(name = "term", nullable = false)
	public String getTerm() {
		return this.term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	@Column(name = "definition")
	public String getDefinition() {
		return this.definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	@Column(name = "link")
	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cvSampletype")
	public Set<Sample> getSamples() {
		return this.samples;
	}

	public void setSamples(Set<Sample> samples) {
		this.samples = samples;
	}

}