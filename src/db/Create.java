package db;

import model.*;

public class Create {

	public static boolean insertNewCourse(Course course) {
		String sql = "insert into Course values ('" + course.getCourseName() + "','" + course.getTerm() + "')";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewCategory(Category category) {
		String sql = "insert into Category values ('" + category.getCourseId() + "','" + category.getFieldName()
				+ "','" + category.getWeight() + "','" + category.getTemplateId() + "')";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewCourseStudent(CourseStudent cs) {
		String sql = "insert into CourseStudent values ('" + cs.getCourseId() + "','" + cs.getBuid() + "','"
				+ cs.isActive() + "')";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewGradeEntry(GradeEntry ge) {
		String sql = "insert into GradeEntry values ('" + ge.getEntryName() + "','" + ge.getItemId() + "','"
				+ ge.getCategoryId() + "','" + ge.getMaxPoints() + "','" + ge.getPointsEarned() + "','"
				+ ge.getCourseId() + "','" + ge.getComments() + "')";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewItem(Item item) {
		String sql = "insert into Item values ('" + item.getId()+"','"+item.getCourseId() + "','" + item.getFieldName() + "','"
				+ item.getCategoryId() + "','" + item.getWeight() + "','" + item.getWeight() + "','"
				+ item.getDateAssigned() + "','" + item.getDateDue() + "')";
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewStudent(Student student) {
		String sql = "insert into Student values ('" + student.getBuid() + "','" + student.getFname() + "','"
				+ student.getLname() + student.getType() + "')";
		// student.getEmail()
		return SQLHelper.performQuery(sql);
	}

	public static boolean insertNewTemplate(Template template) {
		String sql = "insert into Template values('" + template.getTemplateName() + "')";
		return SQLHelper.performQuery(sql);
	}
}