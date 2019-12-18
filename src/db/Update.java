package db;

import java.util.ArrayList;

import model.Category;
import model.Course;
import model.CourseStudent;
import model.Item;

/**
 * Update records in database.
 */
public class Update {

	/**
	 * Update course student active status.
	 *
	 * @param courseId  Course ID.
	 * @param studentId Student ID.
	 * @param active    Active status.
	 * @return Status.
	 */
	public static boolean updateCourseStudentActive(int courseId, String studentId, boolean active) {
		String sql = "Update CourseStudent set active = " + active + " where courseID = '" + courseId + "'"
				+ " and BUID = '" + studentId + "'";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Update course students points earned.
	 *
	 * @param courseStudent Course student.
	 * @param courseId      Course ID.
	 * @param categoryId    Category ID.
	 * @param itemId        Item ID.
	 * @return Status.
	 */
	public static boolean updateCourseStudentPointsEarned(CourseStudent courseStudent, int courseId,
	                                                      int categoryId, int itemId) {
		String sql = "Update GradeEntry set pointsEarned = '"
				+ courseStudent.getGradeEntryForItemInCategory(courseId, categoryId, itemId).getPointsEarned()
				+ "' where courseID = '" + courseId + "' and categoryID = '" + categoryId + "' and itemID = '" + itemId
				+ "'";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Update course student grade entry comment.
	 *
	 * @param courseStudent Course student.
	 * @param courseId      Course ID.
	 * @param categoryId    Category ID.
	 * @param itemId        Item ID.
	 * @return Status.
	 */
	public static boolean updateCourseStudentGradeEntryComment(CourseStudent courseStudent, int courseId,
	                                                           int categoryId, int itemId) {
		String sql = "Update GradeEntry set comment = '"
				+ courseStudent.getGradeEntryForItemInCategory(courseId, categoryId, itemId).getComments()
				+ "' where courseID = '" + courseId + "' and categoryID = '" + categoryId + "' and itemID = '" + itemId
				+ "'";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Update course student grade entry.
	 *
	 * @param courseStudent Course student.
	 * @param courseId      Course ID.
	 * @param categoryId    Category ID.
	 * @param itemId        Item ID.
	 * @return Status.
	 */
	public static boolean updateCourseStudentGradeEntry(CourseStudent courseStudent, int courseId,
	                                                    int categoryId, int itemId) {
		String sql = "Update GradeEntry set pointsEarned = "
				+ courseStudent.getGradeEntryForItemInCategory(courseId, categoryId, itemId).getPointsEarned()
				+ " , comment = '"
				+ courseStudent.getGradeEntryForItemInCategory(courseId, categoryId, itemId).getComments()
				+ "' , percentage = "
				+ courseStudent.getGradeEntryForItemInCategory(courseId, categoryId, itemId).getPercentage()
				+ " where courseID = " + courseId + " and categoryID = " + categoryId + " and itemID = " + itemId
				+ " and buid = '" + courseStudent.getStudentId() + "'";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Update course student.
	 *
	 * @param courseStudent Course student.
	 * @return Status.
	 */
	public static boolean updateCourseStudent(CourseStudent courseStudent) {
		ArrayList<CourseStudent> courseStudents = Read.getCourseStudentsByCourse(courseStudent.getCourseId());
		String oldStudentId = "";
		for (CourseStudent student : courseStudents) {
			if ((student.getCourseId() == courseStudent.getCourseId())
					&& student.getFirstName().equals(courseStudent.getFirstName())
					&& student.getLastName().equals(courseStudent.getLastName())) {
				oldStudentId = courseStudent.getStudentId();
				break;
			}
		}
		String sql = "Update Student set BUID = '" + courseStudent.getStudentId() + "' , type ='" + courseStudent.getType()
				+ "'" + ", email = '" + courseStudent.getEmail() + "' where fName = '" + courseStudent.getFirstName()
				+ "' and lName = '" + courseStudent.getLastName() + "'";
		String sql2 = "Update CourseStudent set BUID = '" + courseStudent.getStudentId() + "' where courseID = '"
				+ courseStudent.getCourseId() + "'" + " and BUID ='" + oldStudentId + "'";
		return SQLHelper.performQuery(sql) && SQLHelper.performQuery(sql2);
	}

	/**
	 * Update category.
	 *
	 * @param category Category.
	 * @return Status.
	 */
	public static boolean updateCategory(Category category) {
		String sql = "Update Category set weight = '" + category.getWeight() + "' where courseID ='"
				+ category.getCourseId() + "" + "' and ID = '" + category.getCategoryId() + "'";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Update item.
	 *
	 * @param item Item.
	 * @return Status.
	 */
	public static boolean updateItem(Item item) {
		String sql = "Update Item set weight = '" + item.getWeight() + "' where courseID ='" + item.getCourseId() + ""
				+ "' and ID = '" + item.getItemId() + "' and categoryID = '" + item.getCategoryId() + "'";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Update course finalized.
	 *
	 * @param course Course.
	 * @return Status.
	 */
	public static boolean updateCourseFinalized(Course course) {
		String sql = "Update Course set finalized=" + course.isFinalized();
		if (course.isCurveApplied()) {
			sql += ", curve=" + course.getCurve() + ", curveApplied=" + course.isCurveApplied() + " ";
		}
		sql += " where ID=" + course.getCourseId();
		return SQLHelper.performQuery(sql);
	}

}