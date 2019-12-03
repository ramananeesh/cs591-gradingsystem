package db;
import model.*;


public class Delete {
    public static boolean removeCourseStudentFromCourse(String BUID, int courseID){
        String sql = "Delete from CourseStudent where BUID ='" + BUID + "'" +
                " and courseID = '" + courseID + "'";
        return SQLHelper.performQuery(sql);
    }
}
