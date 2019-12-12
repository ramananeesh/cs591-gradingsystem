package db;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Read {

	// might want to start using courseID instead of courseName to avoid instances
	// where for example:
	// CS591 Spring 2019 has Exam, but CS591 Fall 2020 has Project. selecting by
	// courseName will result in CS591 Fall
	// 2020 having both Exam and Project when it should only have Project. selecting
	// by courseID will avoid this.
	public static ArrayList<Category> getCategoriesByCourse(int courseId) {
		ArrayList<Category> categories = new ArrayList<>();
		String query = "select id, fieldName, weight, courseId from Category where courseId ='" + courseId + "'";
		ResultSet rs = SQLHelper.performRead(query);

		try {
			while (rs.next()) {
				ArrayList<Item> items = getItemsByCategory(rs.getInt("ID"), rs.getInt("courseID"));

				Category category = new Category(rs.getInt("ID"), rs.getString("fieldName"), rs.getDouble("weight"),
						rs.getInt("courseId"), items);
				categories.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}

	// same concern with courseName -> courseID. categoryName could also be
	// categoryID to avoid overlaps
	// public static ArrayList<Item> getItemByCourse(int courseID) {
	// ArrayList<Item> items = new ArrayList<>();
	// String query = "select id, fieldName, categoryName, weight, templateID, "
	// + "dateAssigned, dateDue from Item where courseID='" + courseID + "'";
	// ResultSet rs = SQLHelper.performRead(query);
	//
	// try {
	// while (rs.next()) {
	//
	// Item item = new Item(rs.getInt("ID"), rs.getString("fieldName"),
	// rs.getInt("categoryID"),
	// rs.getDouble("weight"), courseID);
	// items.add(item);
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return items;
	// } commented out, outdated code

	public static ArrayList<Item> getItemsByCategory(int categoryID, int courseID) {
		ArrayList<Item> items = new ArrayList<>();
		String query = "select id, courseID, fieldName, weight, categoryId, maxPoints from Item where categoryID = '"
				+ categoryID + "' and courseID = '" + courseID + "'";
		ResultSet rs = SQLHelper.performRead(query);
		try {
			while (rs.next()) {

				Item item = new Item(rs.getInt("ID"), rs.getString("fieldName"), rs.getInt("categoryID"),
						rs.getDouble("weight"), rs.getDouble("maxPoints"), rs.getInt("courseID"));
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public static ArrayList<GradeEntry> getStudentGradeEntriesByItem(String buid, int itemID, int categoryID,
			int courseID) {
		ArrayList<GradeEntry> gradeEntries = new ArrayList<>();
		String query = "Select * from GradeEntry where buid = '" + buid + " and itemID = '" + itemID
				+ "' and categoryID = '" + categoryID + "' and " + "courseID = '" + courseID + "'";
		ResultSet rs = SQLHelper.performRead(query);
		try {
			while (rs.next()) {
				GradeEntry ge = new GradeEntry(rs.getString("entryName"), rs.getInt("itemID"), rs.getInt("categoryID"),
						rs.getDouble("maxPoint"), rs.getDouble("pointsEarned"), rs.getDouble("percentage"),
						rs.getInt("courseID"), rs.getString("comment"));
				gradeEntries.add(ge);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gradeEntries;
	}

	public static ArrayList<Course> getAllCourses() {
		ArrayList<Course> courses = new ArrayList<>();
		String query = "Select * from Course";
		ResultSet rs = SQLHelper.performRead(query);

		try {
			while (rs.next()) {
				ArrayList<Category> categories = getCategoriesByCourse(rs.getInt("ID"));
				ArrayList<CourseStudent> students = getCourseStudentsByCourse(rs.getInt("ID"));

				Course course = new Course(rs.getInt("ID"), rs.getString("courseNumber"), rs.getString("courseName"),
						rs.getString("term"), categories, students);
				courses.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}

	public static ArrayList<GradeEntry> getGradeEntriesByCourse(int courseID) {
		ArrayList<GradeEntry> gradeEntries = new ArrayList<>();
		String query = "Select * from GradeEntry where courseID='" + courseID + "'";
		ResultSet rs = SQLHelper.performRead(query);
		try {
			while (rs.next()) {
				GradeEntry ge = new GradeEntry(rs.getString("entryName"), rs.getInt("itemID"), rs.getInt("categoryID"),
						rs.getDouble("maxPoint"), rs.getDouble("pointsEarned"), rs.getDouble("percentage"),
						rs.getInt("courseID"), rs.getString("comment"));
				gradeEntries.add(ge);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gradeEntries;
	}

	public static ArrayList<GradeEntry> getGradeEntriesByCourseStudent(String BUID, int courseId) {
		ArrayList<GradeEntry> gradeEntries = new ArrayList<>();
		String query = "Select * from GradeEntry where BUID='" + BUID + "' and courseId=" + courseId;
		ResultSet rs = SQLHelper.performRead(query);
		try {
			while (rs.next()) {
				GradeEntry ge = new GradeEntry(rs.getString("entryName"), rs.getInt("itemID"), rs.getInt("categoryID"),
						rs.getDouble("maxPoint"), rs.getDouble("pointsEarned"), rs.getDouble("percentage"),
						rs.getInt("courseID"), rs.getString("comment"));
				gradeEntries.add(ge);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gradeEntries;
	}

	// now properly makes the coursestudent object
	public static ArrayList<CourseStudent> getCourseStudentsByCourse(int courseID) {
		ArrayList<CourseStudent> students = new ArrayList<>();
		// String query = "Select A.* from Student A where A.BUID in (select B.BUID from
		// CourseStudent B where B.courseID ='"
		// + courseID + "')";
		//String query = "Select Student.BUID, Student.fName, Student.lName, Student.type, Student.email, CourseStudent.courseID,"
		//		+ "CourseStudent.active from Student, CourseStudent JOIN CourseStudent ON Student.BUID = CourseStudent.BUID";
		String query = "Select s.buid, s.fname, s.lname, s.type, s.email, cs.courseId, cs.active from student s, coursestudent cs where "
				+ "s.buid=cs.buid and courseId=" +courseID;
		ResultSet rs = SQLHelper.performRead(query);
		try {
			while (rs.next()) {
				ArrayList<GradeEntry> gradeEntries = getGradeEntriesByCourseStudent(rs.getString("BUID"), courseID);

				CourseStudent student = new CourseStudent(rs.getString("fName"), rs.getString("lName"),
						rs.getString("BUID"), rs.getString("email"), rs.getString("type"), rs.getInt("courseID"),
						rs.getBoolean("active"), gradeEntries);
				students.add(student);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}
}