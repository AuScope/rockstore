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
 * CvVariables generated by hbm2java
 */
@Entity
@Table(name = "cv_variables")
public class CvVariables implements java.io.Serializable {

	private String term;
	private String definition;

	public CvVariables() {
	}

	public CvVariables(String term) {
		this.term = term;
	}

	public CvVariables(String term, String definition) {
		this.term = term;
		this.definition = definition;
	}

	@Id
	@Column(name = "term", unique = true, nullable = false)
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
