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
				ArrayList<FinalGrade> finalGrades = getAllFinalGradesByCourse(rs.getInt("ID"), students);
				if (finalGrades.size() == 0) {
					Course course = new Course(rs.getInt("ID"), rs.getString("courseNumber"),
							rs.getString("courseName"), rs.getString("term"), categories, students);
					courses.add(course);
				} else {
					boolean curveApplied = rs.getBoolean("curveApplied");
					Double curve = rs.getDouble("curve");
					if (curveApplied == false)
						curve = null;
					Course course = new Course(rs.getInt("ID"), rs.getString("courseNumber"),
							rs.getString("courseName"), rs.getString("term"), categories, students, finalGrades,
							curveApplied, curve, rs.getBoolean("finalized"));
					courses.add(course);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}

	public static CourseStudent getStudentByBuid(ArrayList<CourseStudent> students, String buid) {
		for (CourseStudent student : students) {
			if (student.getBuid().equals(buid))
				return student;
		}
		return null;
	}

	public static ArrayList<FinalGrade> getAllFinalGradesByCourse(int courseId, ArrayList<CourseStudent> students) {
		ArrayList<FinalGrade> finalGrades = new ArrayList<FinalGrade>();
		String query = "Select * from FinalGrade where courseID='" + courseId + "'";
		ResultSet rs = SQLHelper.performRead(query);
		try {
			while (rs.next()) {
				CourseStudent student = getStudentByBuid(students, rs.getString("buid"));
				FinalGrade f = new FinalGrade(student, rs.getDouble("actualPercentage"),
						rs.getDouble("curvedPercentage"), rs.getString("letterGrade"));
				finalGrades.add(f);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return finalGrades;

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
		// String query = "Select Student.BUID, Student.fName, Student.lName,
		// Student.type, Student.email, CourseStudent.courseID,"
		// + "CourseStudent.active from Student, CourseStudent JOIN CourseStudent ON
		// Student.BUID = CourseStudent.BUID";
		String query = "Select s.buid, s.fname, s.lname, s.type, s.email, cs.courseId, cs.active from student s, coursestudent cs where "
				+ "s.buid=cs.buid and courseId=" + courseID;
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