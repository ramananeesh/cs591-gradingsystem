package model;

public class GradeEntry {

	private String entryName;
	private int itemId;
	private int categoryId;
	private double maxPoints;
	private double pointsEarned;
	private int courseId;
	private String comments;
	
	public GradeEntry(String entryName, int itemId, int categoryId, double maxPoints, double pointsEarned, int courseId,
			String comments) {
		super();
		this.entryName = entryName;
		this.itemId = itemId;
		this.categoryId = categoryId;
		this.maxPoints = maxPoints;
		this.pointsEarned = pointsEarned;
		this.courseId = courseId;
		this.comments = comments;
	}
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
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

	
}
