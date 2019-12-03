package model;

public class GenericCourse {

	private String courseId;
	private String courseName;
	private String term;

	public GenericCourse(String courseId, String courseName, String term) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.term = term;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
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
	
	public String[] getDetails() {
		String[] str= {this.courseId,this.courseName,this.term};
		
		return str;
	}
}
