package model;

import java.sql.Date;
import java.util.*;

public class Item {

	private int id;
	private String fieldName;
	private String categoryName;
	private double weight;
	private String courseName;
//	private int templateId;
	private Date dateAssigned;
	private Date dateDue;
	private ArrayList<GradeEntry> gradeEntries;

	// Item(int id, String fieldName, String categoryName, double weight, String
	// courseName, int templateId, Date dateAssigned, Date dateDue)
	public Item(int id, String fieldName, String categoryName, double weight, String courseName, Date dateAssigned,
			Date dateDue) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.categoryName = categoryName;
		this.weight = weight;
		this.courseName = courseName;
//		this.templateId = templateId;
		this.dateAssigned = dateAssigned;
		this.dateDue = dateDue;
		this.gradeEntries = new ArrayList<GradeEntry>();
	}

	public Item(int id, String fieldName, String categoryName, double weight, String courseName) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.categoryName = categoryName;
		this.weight = weight;
		this.courseName = courseName;
//		this.templateId = templateId;
		this.gradeEntries = new ArrayList<GradeEntry>();
	}
	
	// Item(int id, String fieldName, String categoryName, double weight, String
	// courseName, int templateId, Date dateAssigned, Date dateDue,
	// ArrayList<GradeEntry> gradeEntries)
	public Item(int id, String fieldName, String categoryName, double weight, String courseName, Date dateAssigned,
			Date dateDue, ArrayList<GradeEntry> gradeEntries) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.categoryName = categoryName;
		this.weight = weight;
		this.courseName = courseName;
//		this.templateId = templateId;
		this.dateAssigned = dateAssigned;
		this.dateDue = dateDue;
		this.gradeEntries = gradeEntries;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/*
	 * public int getTemplateId() { return templateId; }
	 * 
	 * public void setTemplateId(int templateId) { this.templateId = templateId; }
	 */

	public Date getDateAssigned() {
		return dateAssigned;
	}

	public void setDateAssigned(Date dateAssigned) {
		this.dateAssigned = dateAssigned;
	}

	public Date getDateDue() {
		return dateDue;
	}

	public void setDateDue(Date dateDue) {
		this.dateDue = dateDue;
	}

	public String[] getDetails() {
		return new String[]{this.fieldName,Double.toString(this.weight)};
	}
}
