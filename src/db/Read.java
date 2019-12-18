package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import model.Category;
import model.Course;
import model.CourseStudent;
import model.FinalGrade;
import model.GradeEntry;
import model.Item;

/**
 * Read records in database.
 */
public class Read {

	/**
	 * Get categories by course.
	 *
	 * @param courseId Course ID.
	 * @return List of categories.
	 */
	private static ArrayList<Category> getCategoriesByCourse(int courseId) {
		ArrayList<Category> categories = new ArrayList<>();
		String sql = "select id, fieldName, weight, courseId from Category where courseId ='" + courseId + "'";
		ResultSet resultSet = SQLHelper.performRead(sql);

		try {
			while (resultSet.next()) {
				ArrayList<Item> items = getItemsByCategory(resultSet.getInt("ID"), resultSet.getInt("courseID"));

				Category category = new Category(resultSet.getInt("ID"), resultSet.getString("fieldName"), resultSet.getDouble("weight"),
						resultSet.getInt("courseId"), items);
				categories.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}

	/**
	 * Get items by category.
	 *
	 * @param categoryId Category ID.
	 * @param courseId   Course ID.
	 * @return List of items.
	 */
	private static ArrayList<Item> getItemsByCategory(int categoryId, int courseId) {
		ArrayList<Item> items = new ArrayList<>();
		String sql = "select id, courseID, fieldName, weight, categoryId, maxPoints from Item where categoryID = '"
				+ categoryId + "' and courseID = '" + courseId + "'";
		ResultSet resultSet = SQLHelper.performRead(sql);
		try {
			while (resultSet.next()) {

				Item item = new Item(resultSet.getInt("ID"), resultSet.getString("fieldName"), resultSet.getInt("categoryID"),
						resultSet.getDouble("weight"), resultSet.getDouble("maxPoints"), resultSet.getInt("courseID"));
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * Get student grade entries by items.
	 *
	 * @param buid       BU ID.
	 * @param itemId     Item ID.
	 * @param categoryId Category ID.
	 * @param courseId   Course ID.
	 * @return List of grade entries.
	 */
	public static ArrayList<GradeEntry> getStudentGradeEntriesByItem(String buid, int itemId,
	                                                                 int categoryId, int courseId) {
		ArrayList<GradeEntry> gradeEntries = new ArrayList<>();
		String sql = "Select * from GradeEntry where buid = '" + buid + " and itemID = '" + itemId
				+ "' and categoryID = '" + categoryId + "' and " + "courseID = '" + courseId + "'";
		ResultSet resultSet = SQLHelper.performRead(sql);
		try {
			while (resultSet.next()) {
				GradeEntry gradeEntry = new GradeEntry(resultSet.getString("entryName"), resultSet.getInt("itemID"), resultSet.getInt("categoryID"),
						resultSet.getDouble("maxPoint"), resultSet.getDouble("pointsEarned"), resultSet.getDouble("percentage"),
						resultSet.getInt("courseID"), resultSet.getString("comment"));
				gradeEntries.add(gradeEntry);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gradeEntries;
	}

	/**
	 * Get all courses.
	 *
	 * @return List of all courses.
	 */
	public static ArrayList<Course> getAllCourses() {
		ArrayList<Course> courses = new ArrayList<>();
		String sql = "Select * from Course";
		ResultSet resultSet = SQLHelper.performRead(sql);
		try {
			while (resultSet.next()) {
				ArrayList<Category> categories = getCategoriesByCourse(resultSet.getInt("ID"));
				ArrayList<CourseStudent> students = getCourseStudentsByCourse(resultSet.getInt("ID"));
				ArrayList<FinalGrade> finalGrades = getAllFinalGradesByCourse(resultSet.getInt("ID"), students);
				if (finalGrades.size() == 0) {
					Course course = new Course(resultSet.getInt("ID"), resultSet.getString("courseNumber"),
							resultSet.getString("courseName"), resultSet.getString("term"), categories, students);
					courses.add(course);
				} else {
					boolean curveApplied = resultSet.getBoolean("curveApplied");
					Double curve = resultSet.getDouble("curve");
					if (!curveApplied)
						curve = null;
					Course course = new Course(resultSet.getInt("ID"), resultSet.getString("courseNumber"),
							resultSet.getString("courseName"), resultSet.getString("term"), categories, students, finalGrades,
							curveApplied, curve, resultSet.getBoolean("finalized"));
					courses.add(course);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}

	private static CourseStudent getStudentByBuid(ArrayList<CourseStudent> students, String buid) {
		for (CourseStudent student : students) {
			if (student.getStudentId().equals(buid))
				return student;
		}
		return null;
	}

	private static ArrayList<FinalGrade> getAllFinalGradesByCourse(int courseId, ArrayList<CourseStudent> students) {
		ArrayList<FinalGrade> finalGrades = new ArrayList<>();
		String sql = "Select * from FinalGrade where courseID='" + courseId + "'";
		ResultSet resultSet = SQLHelper.performRead(sql);
		try {
			while (resultSet.next()) {
				CourseStudent student = getStudentByBuid(students, resultSet.getString("buid"));
				FinalGrade finalGrade = new FinalGrade(student, resultSet.getDouble("actualPercentage"),
						resultSet.getDouble("curvedPercentage"), resultSet.getString("letterGrade"));
				finalGrades.add(finalGrade);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return finalGrades;

	}

	public static ArrayList<GradeEntry> getGradeEntriesByCourse(int courseID) {
		ArrayList<GradeEntry> gradeEntries = new ArrayList<>();
		String sql = "Select * from GradeEntry where courseID='" + courseID + "'";
		ResultSet resultSet = SQLHelper.performRead(sql);
		try {
			while (resultSet.next()) {
				GradeEntry gradeEntry = new GradeEntry(resultSet.getString("entryName"), resultSet.getInt("itemID"), resultSet.getInt("categoryID"),
						resultSet.getDouble("maxPoint"), resultSet.getDouble("pointsEarned"), resultSet.getDouble("percentage"),
						resultSet.getInt("courseID"), resultSet.getString("comment"));
				gradeEntries.add(gradeEntry);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gradeEntries;
	}

	private static ArrayList<GradeEntry> getGradeEntriesByCourseStudent(String BUID, int courseId) {
		ArrayList<GradeEntry> gradeEntries = new ArrayList<>();
		String sql = "Select * from GradeEntry where BUID='" + BUID + "' and courseId=" + courseId;
		ResultSet resultSet = SQLHelper.performRead(sql);
		try {
			while (resultSet.next()) {
				GradeEntry gradeEntry = new GradeEntry(resultSet.getString("entryName"), resultSet.getInt("itemID"), resultSet.getInt("categoryID"),
						resultSet.getDouble("maxPoint"), resultSet.getDouble("pointsEarned"), resultSet.getDouble("percentage"),
						resultSet.getInt("courseID"), resultSet.getString("comment"));
				gradeEntries.add(gradeEntry);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gradeEntries;
	}

	/**
	 * Get users for system.
	 *
	 * @return Users.
	 */
	public static HashMap<String, String> getUsersForSystem() {
		HashMap<String, String> userMap = new HashMap<>();

		String sql = "Select username, password from users ";
		ResultSet resultSet = SQLHelper.performRead(sql);

		try {
			while (resultSet.next()) {
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");
				userMap.put(username, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userMap;
	}

	/**
	 * Get course students by course.
	 *
	 * @param courseId Course ID.
	 * @return List of course students.
	 */
	public static ArrayList<CourseStudent> getCourseStudentsByCourse(int courseId) {
		ArrayList<CourseStudent> students = new ArrayList<>();
		String sql = "Select s.buid, s.fname, s.lname, s.type, s.email, cs.courseId, cs.active from student s, coursestudent cs where "
				+ "s.buid=cs.buid and courseId=" + courseId;
		ResultSet resultSet = SQLHelper.performRead(sql);
		try {
			while (resultSet.next()) {
				ArrayList<GradeEntry> gradeEntries = getGradeEntriesByCourseStudent(resultSet.getString("BUID"), courseId);

				CourseStudent student = new CourseStudent(resultSet.getString("fName"), resultSet.getString("lName"),
						resultSet.getString("BUID"), resultSet.getString("email"), resultSet.getString("type"), resultSet.getInt("courseID"),
						resultSet.getBoolean("active"), gradeEntries);
				students.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}

}