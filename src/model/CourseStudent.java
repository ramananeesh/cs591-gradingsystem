package model;

public class CourseStudent extends Student {

	private int courseId;
	private boolean active;

	public CourseStudent(String fname, String lname, String buid, String email, String type, int courseId,
			boolean active) {
		super(fname, lname, buid, email, type);
		this.courseId = courseId;
		this.active = active;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
