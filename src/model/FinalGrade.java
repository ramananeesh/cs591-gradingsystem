package model;

/**
 * Final grade.
 */
public class FinalGrade {

	/** Student. */
	private CourseStudent student;

	/** Actual percentage. */
	private Double actualPercentage;

	/** Curved percentage. */
	private Double curvedPercentage;

	/** Letter grade. */
	private String letterGrade;

	public FinalGrade(CourseStudent student, double actualPercentage, double curvedPercentage, String letterGrade) {
		this.student = student;
		this.actualPercentage = actualPercentage;
		this.curvedPercentage = curvedPercentage;
		this.letterGrade = letterGrade;
	}

	public FinalGrade(CourseStudent student, double actualPercentage, String letterGrade) {
		this.student = student;
		this.actualPercentage = actualPercentage;
		this.letterGrade = letterGrade;
		this.curvedPercentage = null;
	}

	public CourseStudent getStudent() {
		return student;
	}

	public void setStudent(CourseStudent student) {
		this.student = student;
	}

	public double getActualPercentage() {
		return actualPercentage;
	}

	public void setActualPercentage(double actualPercentage) {
		this.actualPercentage = actualPercentage;
	}

	public double getCurvedPercentage() {
		return curvedPercentage;
	}

	public void setCurvedPercentage(double curvedPercentage) {
		this.curvedPercentage = curvedPercentage;
	}

	public String getLetterGrade() {
		return letterGrade;
	}

	public void setLetterGrade(String letterGrade) {
		this.letterGrade = letterGrade;
	}

	public String[] getDetailsForList() {
		if (curvedPercentage != null) {
			return new String[]{this.student.getName(), this.student.getStudentId(), this.actualPercentage.toString(),
					this.curvedPercentage.toString(), this.letterGrade};
		} else {
			return new String[]{this.student.getName(), this.student.getStudentId(), this.actualPercentage.toString(), "",
					this.letterGrade};
		}
	}
}
