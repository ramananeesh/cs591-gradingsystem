package db;

import model.*;

public class Create {

	public static boolean insertNewCourse(Course course) {
		double curve = 0;
		if(course.getCurve()==null)
			curve=-1;
		else 
			curve = course.getCurve();
		String sql = "insert into Course (id, courseName, term, courseNumber, curveApplied, curve, finalized) values ('" + course.getCourseId() + "','" + course.getCourseName() + "','"
				+ course.getTerm() + "','" + course.getCourseNumber() + "'," +course.isCurveApplied()+ ",'" +curve+ "'," +course.isFinalized()+")";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewCategory(Category category) {
		String sql = "insert into Category  (id, courseId, fieldName, weight) values(" + category.getId() + ","
				+ category.getCourseId() + ",'" + category.getFieldName() + "'," + category.getWeight() + ")";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewCourseStudent(CourseStudent cs) {
		String sql = "insert into CourseStudent (courseId, buid, active) values ('" + cs.getCourseId() + "','"
				+ cs.getBuid() + "'," + cs.isActive() + ")";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewGradeEntry(GradeEntry ge, String buid) {
		String sql = "insert into GradeEntry (entryName, itemId, categoryId, maxPoint, pointsEarned, percentage, courseId, comment, buid) values ('"
				+ ge.getEntryName() + "'," + ge.getItemId() + "," + ge.getCategoryId() + "," + ge.getMaxPoints() + ","
				+ ge.getPointsEarned() + "," + ge.getPercentage() + "," + +ge.getCourseId() + ",'" + ge.getComments()
				+ "','" + buid + "')";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewItem(Item item) {
		String sql = "insert into Item values (" + item.getId() + "," + item.getCourseId() + ",'" + item.getFieldName()
				+ "'," + item.getCategoryId() + "," + item.getWeight() + "," + item.getMaxPoints() + ")";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewStudent(Student student) {
		String sql = "insert into Student values ('" + student.getBuid() + "','" + student.getFname() + "','"
				+ student.getLname() + "','" + student.getType() + "','" + student.getEmail() + "')";
		// student.getEmail()
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewTemplate(Template template) {
		String sql = "insert into Template values('" + template.getTemplateName() + "')";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewFinalGrade(FinalGrade grade, int courseId) {
		String sql = "insert into finalGrade (buid, actualPercentage, curvedPercentage, letterGrade, courseId) values('" + grade.getStudent().getBuid() + "','"
				+ grade.getActualPercentage() + "','" + grade.getCurvedPercentage() + "','" + grade.getLetterGrade() + "','"
				+ courseId + "')";
		return SQLHelper.performQuery(sql);
	}
}