package model;

public class GenericCourse {

	private int courseId;
	private String courseNumber;
	private String courseName;
	private String term;

	public GenericCourse(int courseId, String courseNumber, String courseName, String term) {
		this.courseId = courseId;
		this.courseNumber = courseNumber;
		this.courseName = courseName;
		this.term = term;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String toString() {
		return this.courseNumber + " - " + this.courseName + " - " + this.term;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String[] getDetails() {
		String[] str = { this.courseNumber, this.courseName, this.term };

		return str;
	}
}
