package db;

/**
 * Delete records in database.
 */
public class Delete {

	/**
	 * Delete course student from a course.
	 *
	 * @param buid     Student BU ID.
	 * @param courseId Course ID.
	 * @return Status.
	 */
	public static boolean removeCourseStudentFromCourse(String buid, int courseId) {
		String sql = "Delete from CourseStudent where BUID ='" + buid + "'" +
				" and courseID = '" + courseId + "'";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Delete category from a course.
	 *
	 * @param categoryId Category ID.
	 * @param courseId   Course ID.
	 * @return Status.
	 */
	public static boolean removeCategoryFromCourse(int categoryId, int courseId) {
		String sql = "Delete from Category where id ='" + categoryId + "'" +
				" and courseID = '" + courseId + "'";
		return SQLHelper.performQuery(sql);
	}

	/**
	 * Delete item from category in a course.
	 *
	 * @param itemId     Item ID.
	 * @param categoryId Category ID.
	 * @param courseID   Course ID.
	 * @return Status.
	 */
	public static boolean removeItemFromCategoryInCourse(int itemId, int categoryId, int courseID) {
		String sql = "Delete from Item where id ='" + itemId + "' and courseID = '" + courseID + "'" +
				" and categoryID = '" + categoryId + "'";
		return SQLHelper.performQuery(sql);
	}

}
