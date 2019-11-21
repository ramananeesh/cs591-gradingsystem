package model;

public class Category {

	private int id;
	private String fieldName;
	private double weight;
	private String courseName;

	public Category(int id, String fieldName, double weight, String courseName) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.weight = weight;
		this.courseName = courseName;
	}

	private int templateId;

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

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String toString() {
		return this.id + " - " + this.fieldName + " - " + this.weight + " - " + this.courseName + " - "
				+ this.templateId;
	}
}
