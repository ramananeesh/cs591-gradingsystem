package model;

public class GenericCategory {

	private int id;
	private String fieldName;
	private double weight;
	private int courseId;
	private int templateId; 
	
	public GenericCategory(int id, String fieldName, double weight, int courseId) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.weight = weight;
		this.courseId = courseId;
		this.templateId = -1; 
	}

	public GenericCategory(int id, String fieldName, double weight, int courseId, int templateId) {
		super();
		this.id = id;
		this.fieldName = fieldName;
		this.weight = weight;
		this.courseId = courseId;
		this.templateId = templateId; 
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

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String toString() {
		return this.id + " - " + this.fieldName + " - " + this.weight + " - " + this.courseId + " - "
				+ this.templateId;
	}
	
	public String[] getDataForList() {
		return new String[] {this.fieldName, Double.toString(this.weight)};
	}
	
}
