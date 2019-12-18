package model;

/**
 * Item.
 */
public class Item {

	/** Item ID. */
	private int itemId;

	/** Item name. */
	private String itemName;

	/** Category ID. */
	private int categoryId;

	/** Weight. */
	private double weight;

	/** Course ID. */
	private int courseId;

	/** Max points. */
	private double maxPoints;

	public Item(int itemId, String itemName, int categoryId, double weight, double maxPoints, int courseId) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.categoryId = categoryId;
		this.weight = weight;
		this.courseId = courseId;
		this.maxPoints = maxPoints;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public String[] getDetails() {
		return new String[]{this.itemName, Double.toString(this.weight)};
	}

	public String[] getDetailsWithMaxPoints() {
		return new String[]{this.itemName, Double.toString(this.weight), Double.toString(this.maxPoints)};
	}

	public String[] getDetailsWithCategory(Course course) {
		return new String[]{course.getCategoryById(this.categoryId).getCategoryName(), this.itemName, Double.toString(this.weight)};
	}

	public String[] getDetailsWithMaxPointsWithCategory(Course course) {
		return new String[]{course.getCategoryById(this.categoryId).getCategoryName(), this.itemName, Double.toString(this.weight), Double.toString(this.maxPoints)};
	}

	public double getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(double maxPoints) {
		this.maxPoints = maxPoints;
	}

}
