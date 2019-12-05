package db;

import model.*;

import java.util.ArrayList;

public class Update {
    public static boolean updateCourseStudentActive(String courseID, String BUID, int active){
        String sql = "Update CourseStudent set active = " + active + " where courseID = '" + courseID + "'" +
                " and BUID = '" + BUID + "'";
        return SQLHelper.performQuery(sql);
    }

    public static boolean updateCourseStudentPointsEarned(CourseStudent courseStudent, int courseID, int categoryID, int itemID){
        String sql = "Update GradeEntry set pointsEarned = '" + courseStudent.getGradeEntryForItemInCategory(courseID, categoryID, itemID).getPointsEarned() +
                "' where courseID = '" + courseID + "' and categoryID = '" + categoryID + "' and itemID = '" + itemID + "'";
        return SQLHelper.performQuery(sql);
    }

    public static boolean updateCourseStudentGradeEntryComment(CourseStudent courseStudent, int courseID, int categoryID, int itemID){
        String sql = "Update GradeEntry set comment = '" + courseStudent.getGradeEntryForItemInCategory(courseID, categoryID, itemID).getComments() +
                "' where courseID = '" + courseID + "' and categoryID = '" + categoryID + "' and itemID = '" + itemID + "'";
        return SQLHelper.performQuery(sql);
    }

    public static boolean updateCourseStudentGradeEntry(CourseStudent courseStudent, int courseID, int categoryID, int itemID){
        String sql = "Update GradeEntry set pointsEarned = '" + courseStudent.getGradeEntryForItemInCategory(courseID, categoryID, itemID).getPointsEarned() +
                "' and comment = '" + courseStudent.getGradeEntryForItemInCategory(courseID, categoryID, itemID).getComments() +
                "' where courseID = '" + courseID + "' and categoryID = '" + categoryID + "' and itemID = '" + itemID + "'";
        return SQLHelper.performQuery(sql);
    }
    //edit student call, syntax might not be correct.
    // changes both Student AND CourseStudent db tables because the fields are spread across both of them
    public static boolean updateCourseStudent(CourseStudent courseStudent){
        String sql = "Update Student set BUID = '" + courseStudent.getBuid() + "' and type ='" + courseStudent.getType() + "'" +
                " and email = '" + courseStudent.getEmail() + "' where fName = '" + courseStudent.getFname() + "' and lName = '" + courseStudent.getLname() + "'";

        String sql2 = "Update CourseStudent set BUID = '" + courseStudent.getBuid() + "' where courseID = '" + courseStudent.getCourseId() + "'";
        return SQLHelper.performQuery(sql) && SQLHelper.performQuery(sql2);
    }

    //edit category call. fieldname can't be changed on the front-end so it's not being updated here
    public static boolean updateCategory(Category category){
        String sql = "Update Category set weight = '" + category.getWeight() + "' where courseID ='" + category.getCourseId() + "" +
                "' and ID = '" + category.getId() + "'";
        return SQLHelper.performQuery(sql);
    }

    //edit item call. fieldname also currently can't be changed
    public static boolean updateItem(Item item){
        String sql = "Update Item set weight = '" + item.getWeight() + "' where courseID ='" + item.getCourseId() + "" +
                "' and ID = '" + item.getId() + "' and categoryID = '" + item.getCategoryId() + "'";
        return SQLHelper.performQuery(sql);
    }




}
