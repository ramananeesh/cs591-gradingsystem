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

    public static boolean removeCategoryFromCourse(int catId, int courseID){
        String sql = "Delete from Category where id ='" + catId + "'" +
                " and courseID = '" + courseID + "'";
        return SQLHelper.performQuery(sql);
    }

    public static boolean removeItemFromCategoryInCourse(int itemId, int categoryId, int courseID){
        
        String sql = "Delete from Item where id ='" + itemId + "' and courseID = '" + courseID + "'" +
                " and categoryID = '" + categoryId + "'";
        return SQLHelper.performQuery(sql);

    }
}
