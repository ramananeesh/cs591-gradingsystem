package db;

import model.Category;
import model.Course;
import model.CourseStudent;
import model.FinalGrade;
import model.GradeEntry;
import model.Item;
import model.Student;
import model.Template;

/**
 * Create records in database.
 */
public class Create {

	/**
	 * Create a new course.
	 *
	 * @param course Course.
	 * @return Status.
	 */
	public static boolean insertNewCourse(Course course) {
		double curve = 0;
		if (course.getCurve() == null)
			curve = -1;
		else
			curve = course.getCurve();
		String sql = "insert into Course (id, courseName, term, courseNumber, curveApplied, curve, finalized) values ('" + course.getCourseId() + "','" + course.getCourseName() + "','"
				+ course.getTerm() + "','" + course.getCourseNumber() + "'," + course.isCurveApplied() + ",'" + curve + "'," + course.isFinalized() + ")";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Create a new category.
	 *
	 * @param category Category.
	 * @return Status.
	 */
	public static boolean insertNewCategory(Category category) {
		String sql = "insert into Category  (id, courseId, fieldName, weight) values(" + category.getCategoryId() + ","
				+ category.getCourseId() + ",'" + category.getCategoryName() + "'," + category.getWeight() + ")";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Create a new course student.
	 *
	 * @param courseStudent Course student.
	 * @return Status.
	 */
	public static boolean insertNewCourseStudent(CourseStudent courseStudent) {
		String sql = "insert into CourseStudent (courseId, buid, active) values ('" + courseStudent.getCourseId() + "','"
				+ courseStudent.getStudentId() + "'," + courseStudent.isActive() + ")";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Inset a new grade entry.
	 *
	 * @param gradeEntry Grade entry.
	 * @param studentId  Student ID.
	 * @return Status.
	 */
	public static boolean insertNewGradeEntry(GradeEntry gradeEntry, String studentId) {
		String sql = "insert into GradeEntry (entryName, itemId, categoryId, maxPoint, pointsEarned, percentage, courseId, comment, buid) values ('"
				+ gradeEntry.getGradeEntryName() + "'," + gradeEntry.getItemId() + "," + gradeEntry.getCategoryId() + "," + gradeEntry.getMaxPoints() + ","
				+ gradeEntry.getPointsEarned() + "," + gradeEntry.getPercentage() + "," + +gradeEntry.getCourseId() + ",'" + gradeEntry.getComments()
				+ "','" + studentId + "')";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Create a new item.
	 *
	 * @param item Item.
	 * @return Status.
	 */
	public static boolean insertNewItem(Item item) {
		String sql = "insert into Item values (" + item.getItemId() + "," + item.getCourseId() + ",'" + item.getItemName()
				+ "'," + item.getCategoryId() + "," + item.getWeight() + "," + item.getMaxPoints() + ")";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Create a new student.
	 *
	 * @param student Student.
	 * @return Status.
	 */
	public static boolean insertNewStudent(Student student) {
		String sql = "insert into Student values ('" + student.getStudentId() + "','" + student.getFirstName() + "','"
				+ student.getLastName() + "','" + student.getType() + "','" + student.getEmail() + "')";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Create a new template.
	 *
	 * @param template Template.
	 * @return Status.
	 */
	public static boolean insertNewTemplate(Template template) {
		String sql = "insert into Template values('" + template.getTemplateName() + "')";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Create a new final grade.
	 *
	 * @param grade    Final grade.
	 * @param courseId Course ID.
	 * @return Status.
	 */
	public static boolean insertNewFinalGrade(FinalGrade grade, int courseId) {
		String sql = "insert into finalGrade (buid, actualPercentage, curvedPercentage, letterGrade, courseId) values('" + grade.getStudent().getStudentId() + "','"
				+ grade.getActualPercentage() + "','" + grade.getCurvedPercentage() + "','" + grade.getLetterGrade() + "','"
				+ courseId + "')";
		return SQLHelper.performQuery(sql);
	}

}