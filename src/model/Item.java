package model;

import java.sql.Date;
import java.util.*;

public class Item {

	private int id;
	private String fieldName;
	private int categoryId;
	private double weight;
	private int courseId;
//	private int templateId;
	private Date dateAssigned;
	private Date dateDue;
	private ArrayList<GradeEntry> gradeEntries;

	// Item(int id, String fieldName, String categoryName, double weight, String
	// courseId, int templateId, Date dateAssigned, Date dateDue)
	public Item(int id, String fieldName, int categoryId, double weight, int courseId, Date dateAssigned,
			Date dateDue) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.categoryId = categoryId;
		this.weight = weight;
		this.courseId = courseId;
//		this.templateId = templateId;
		this.dateAssigned = dateAssigned;
		this.dateDue = dateDue;
		this.gradeEntries = new ArrayList<GradeEntry>();
	}

	public Item(int id, String fieldName, int categoryId, double weight, int courseId) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.categoryId = categoryId;
		this.weight = weight;
		this.courseId = courseId;
//		this.templateId = templateId;
		this.gradeEntries = new ArrayList<GradeEntry>();
	}
	
	// Item(int id, String fieldName, String categoryName, double weight, String
	// courseId, int templateId, Date dateAssigned, Date dateDue,
	// ArrayList<GradeEntry> gradeEntries)
	public Item(int id, String fieldName, int categoryId, double weight, int courseId, Date dateAssigned,
			Date dateDue, ArrayList<GradeEntry> gradeEntries) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.categoryId = categoryId;
		this.weight = weight;
		this.courseId = courseId;
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

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
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
