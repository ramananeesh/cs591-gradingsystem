package model;

/**
 * Generic course.
 */
public class GenericCourse {

	/** Course ID. */
	private int courseId;

	/** Course number. */
	private String courseNumber;

	/** Course name. */
	private String courseName;

	/** Term. */
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
		return new String[]{this.courseNumber, this.courseName, this.term};
	}

}
