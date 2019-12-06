package model;

import java.sql.Date;
import java.util.*;

public class Item {

	private int id;
	private String fieldName;
	private int categoryId;
	private double weight;
	private int courseId;
	// private int templateId;
	// private Date dateAssigned;
	// private Date dateDue;
	private double maxPoints;

	// Item(int id, String fieldName, String categoryName, double weight, String
	// courseId, int templateId, Date dateAssigned, Date dateDue)
	/*
	 * public Item(int id, String fieldName, int categoryId, double weight, double
	 * maxPoints, int courseId, ) { super(); this.id = id; this.fieldName =
	 * fieldName; this.categoryId = categoryId; this.weight = weight; this.courseId
	 * = courseId; // this.templateId = templateId; // this.dateAssigned =
	 * dateAssigned; // this.dateDue = dateDue; this.maxPoints = maxPoints; }
	 */

	public Item(int id, String fieldName, int categoryId, double weight, double maxPoints, int courseId) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.categoryId = categoryId;
		this.weight = weight;
		this.courseId = courseId;
		this.maxPoints = maxPoints;
		// this.templateId = templateId;
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

	public String[] getDetails() {
		return new String[] { this.fieldName, Double.toString(this.weight) };
	}

	public String[] getDetailsWithMaxPoints() {
		return new String[] { this.fieldName, Double.toString(this.weight), Double.toString(this.maxPoints) };
	}

	public double getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(double maxPoints) {
		this.maxPoints = maxPoints;
	}
}
