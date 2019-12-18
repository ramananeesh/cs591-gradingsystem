package model;

public class GenericCategory {

	/** Category ID. */
	private int categoryId;

	/** Category name. */
	private String categoryName;

	/** weight. */
	private double weight;

	/** Course ID. */
	private int courseId;

	/** Template ID. */
	private int templateId;

	public GenericCategory(int categoryId, String categoryName, double weight, int courseId) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.weight = weight;
		this.courseId = courseId;
		this.templateId = -1;
	}

	public GenericCategory(int categoryId, String categoryName, double weight, int courseId, int templateId) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.weight = weight;
		this.courseId = courseId;
		this.templateId = templateId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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
		return this.categoryId + " - " + this.categoryName + " - " + this.weight + " - "
				+ this.courseId + " - " + this.templateId;
	}

	public String[] getDataForList() {
		return new String[]{this.categoryName, Double.toString(this.weight)};
	}

}