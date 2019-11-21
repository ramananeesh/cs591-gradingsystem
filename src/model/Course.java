package model;

public class Course {

	private int courseId;
	private String courseName;
	private String term;

	public Course(int courseId, String courseName, String term) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.term = term;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
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
		return this.courseId + " - " + this.courseName + " - " + this.term;
	}
}
