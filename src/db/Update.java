package db;

public class Update {
    public static boolean updateCourseStudentActive(String courseID, String BUID, int active){
        String sql = "Update CourseStudent set active = " + active + " where courseID = '" + courseID + "'" +
                " and BUID = '" + BUID + "'";
        return SQLHelper.performQuery(sql);
    }




}
