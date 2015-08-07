package org.csiro.igsn.entity.postgres;

// Generated 04/08/2015 2:17:37 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * CvVerticaldatum generated by hbm2java
 */
@Entity
@Table(name = "cv_verticaldatum")
public class CvVerticaldatum implements java.io.Serializable {

	private int verticalDatumId;
	private String term;
	private String definition;

	public CvVerticaldatum() {
	}

	public CvVerticaldatum(int verticalDatumId, String term) {
		this.verticalDatumId = verticalDatumId;
		this.term = term;
	}

	public CvVerticaldatum(int verticalDatumId, String term, String definition) {
		this.verticalDatumId = verticalDatumId;
		this.term = term;
		this.definition = definition;
	}

	@Id
	@Column(name = "vertical_datum_id", unique = true, nullable = false)
	@SequenceGenerator(name="cv_verticaldatum_vertical_datum_id_seq",sequenceName="cv_verticaldatum_vertical_datum_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="cv_verticaldatum_vertical_datum_id_seq")
	public int getVerticalDatumId() {
		return this.verticalDatumId;
	}

	public void setVerticalDatumId(int verticalDatumId) {
		this.verticalDatumId = verticalDatumId;
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

}
