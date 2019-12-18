package model;

/**
 * Grade entry.
 */
public class GradeEntry {

	/** Grade entry name. */
	private String gradeEntryName;

	/** Item ID. */
	private int itemId;

	/** Category ID. */
	private int categoryId;

	/** Maximum points. */
	private double maxPoints;

	/** Points earned. */
	private double pointsEarned;

	/** Course ID. */
	private int courseId;

	/** Percentage. */
	private double percentage;

	/** Comments. */
	private String comments;

	public GradeEntry(String gradeEntryName, int itemId, int categoryId, double maxPoints,
	                  double pointsEarned, double percentage, int courseId, String comments) {
		this.gradeEntryName = gradeEntryName;
		this.itemId = itemId;
		this.categoryId = categoryId;
		this.maxPoints = maxPoints;
		this.pointsEarned = pointsEarned;
		this.courseId = courseId;
		this.percentage = percentage;
		this.comments = comments;
	}

	public String getGradeEntryName() {
		return gradeEntryName;
	}

	public void setGradeEntryName(String gradeEntryName) {
		this.gradeEntryName = gradeEntryName;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public double getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(double maxPoints) {
		this.maxPoints = maxPoints;
	}

	public double getPointsEarned() {
		return pointsEarned;
	}

	public void setPointsEarned(double pointsEarned) {
		this.pointsEarned = pointsEarned;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

}