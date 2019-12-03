package db;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static db.Read.getItemsByCategory;


public class Delete {
    public static boolean removeCourseStudentFromCourse(String BUID, int courseID){
        String sql = "Delete from CourseStudent where BUID ='" + BUID + "'" +
                " and courseID = '" + courseID + "'";
        return SQLHelper.performQuery(sql);
    }

    public static boolean removeCategoryFromCourse(String fieldName, int courseID){
        String sql = "Delete from Category where fieldName ='" + fieldName + "'" +
                " and courseID = '" + courseID + "'";
        return SQLHelper.performQuery(sql);
    }

    public static boolean removeItemFromCategoryInCourse(String itemName, String categoryName, int courseID){
        String query = "select id from Category where courseId ='" + courseID + "' and fieldName = '" + categoryName + "'";
        ResultSet rs = SQLHelper.performRead(query);
        int categoryId = -1;
        try {
            while (rs.next()) {
                categoryId = rs.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "Delete from Item where fieldName ='" + itemName + "' and courseID = '" + courseID + "'" +
                " and categoryID = '" + categoryId + "'";
        return SQLHelper.performQuery(sql);

    }
}
