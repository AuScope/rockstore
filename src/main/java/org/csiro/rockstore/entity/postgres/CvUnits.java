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
 * CvUnits generated by hbm2java
 */
@Entity
@Table(name = "cv_units")
public class CvUnits implements java.io.Serializable {

	private int unitsId;
	private String unitsName;
	private String unitsType;
	private String unitsAbbreviation;
	private Set<Variables> variableses = new HashSet<Variables>(0);
	private Set<Sample> samplesForSampleSizeUnit = new HashSet<Sample>(0);
	private Set<Sample> samplesForElevationUnit = new HashSet<Sample>(0);

	public CvUnits() {
	}

	public CvUnits(int unitsId, String unitsName, String unitsType,
			String unitsAbbreviation) {
		this.unitsId = unitsId;
		this.unitsName = unitsName;
		this.unitsType = unitsType;
		this.unitsAbbreviation = unitsAbbreviation;
	}

	public CvUnits(int unitsId, String unitsName, String unitsType,
			String unitsAbbreviation, Set<Variables> variableses,
			Set<Sample> samplesForSampleSizeUnit,
			Set<Sample> samplesForElevationUnit) {
		this.unitsId = unitsId;
		this.unitsName = unitsName;
		this.unitsType = unitsType;
		this.unitsAbbreviation = unitsAbbreviation;
		this.variableses = variableses;
		this.samplesForSampleSizeUnit = samplesForSampleSizeUnit;
		this.samplesForElevationUnit = samplesForElevationUnit;
	}

	@Id
	@Column(name = "units_id", unique = true, nullable = false)
	@SequenceGenerator(name="cv_units_units_id_seq",sequenceName="cv_units_units_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="cv_units_units_id_seq")
	public int getUnitsId() {
		return this.unitsId;
	}

	public void setUnitsId(int unitsId) {
		this.unitsId = unitsId;
	}

	@Column(name = "units_name", nullable = false)
	public String getUnitsName() {
		return this.unitsName;
	}

	public void setUnitsName(String unitsName) {
		this.unitsName = unitsName;
	}

	@Column(name = "units_type", nullable = false)
	public String getUnitsType() {
		return this.unitsType;
	}

	public void setUnitsType(String unitsType) {
		this.unitsType = unitsType;
	}

	@Column(name = "units_abbreviation", nullable = false)
	public String getUnitsAbbreviation() {
		return this.unitsAbbreviation;
	}

	public void setUnitsAbbreviation(String unitsAbbreviation) {
		this.unitsAbbreviation = unitsAbbreviation;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cvUnits")
	public Set<Variables> getVariableses() {
		return this.variableses;
	}

	public void setVariableses(Set<Variables> variableses) {
		this.variableses = variableses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cvUnitsBySampleSizeUnit")
	public Set<Sample> getSamplesForSampleSizeUnit() {
		return this.samplesForSampleSizeUnit;
	}

	public void setSamplesForSampleSizeUnit(Set<Sample> samplesForSampleSizeUnit) {
		this.samplesForSampleSizeUnit = samplesForSampleSizeUnit;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cvUnitsByElevationUnit")
	public Set<Sample> getSamplesForElevationUnit() {
		return this.samplesForElevationUnit;
	}

	public void setSamplesForElevationUnit(Set<Sample> samplesForElevationUnit) {
		this.samplesForElevationUnit = samplesForElevationUnit;
	}

}