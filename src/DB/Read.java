package DB;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Read {

    //might want to start using courseID instead of courseName to avoid instances where for example:
    // CS591 Spring 2019 has Exam, but CS591 Fall 2020 has Project. selecting by courseName will result in CS591 Fall
    // 2020 having both Exam and Project when it should only have Project. selecting by courseID will avoid this.
    public static ArrayList<Category> getCategoriesByCourse (String courseName){
        ArrayList<Category> categories = new ArrayList<>();
        String query = "select id, fieldName, weight, courseName, templateID from Category where courseName ='" + courseName +"'";
        ResultSet rs = SQLHelper.performRead(query);

        try{
            while (rs.next()){
                Category category = new Category(rs.getInt("ID"),
                        rs.getString("fieldName"), rs.getDouble("weight"),
                        rs.getString("courseName"), rs.getInt("templateID"));
                categories.add(category);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return categories;
    }

    //this one selects by templateID
    public static ArrayList<Category> getCategoriesByTemplate (int id){
        ArrayList<Category> categories = new ArrayList<>();
        String query = "select id, fieldName, weight, courseName, templateID from Category where templateID ='" + id +"'";
        ResultSet rs = SQLHelper.performRead(query);

        try{
            while (rs.next()){
                Category category = new Category(rs.getInt("ID"),
                        rs.getString("fieldName"), rs.getDouble("weight"),
                        rs.getString("courseName"), rs.getInt("templateID"));
                categories.add(category);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return categories;
    }
    public static ArrayList<Category> getCategoriesByTemplate (String templateName){
        ArrayList<Category> categories = new ArrayList<>();
        String query = "Select A.* from Category A where A.TemplateID in (select B.ID from Template B where B.templateName ='" + templateName +"')";
        ResultSet rs = SQLHelper.performRead(query);
        try {
            while (rs.next()) {
                Category category = new Category(rs.getInt("ID"),
                        rs.getString("fieldName"), rs.getDouble("weight"),
                        rs.getString("courseName"), rs.getInt("templateID"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static ArrayList<Item> getItemsByTemplate (String templateName){
        ArrayList<Item> items = new ArrayList<>();
        String query = "Select A.* from Item A where A.TemplateID in (select B.ID from Template B where B.templateName ='" + templateName + "')";
        ResultSet rs = SQLHelper.performRead(query);
        try {
            while (rs.next()) {
                Item item = new Item(rs.getInt("ID"),
                        rs.getString("fieldName"), rs.getString("categoryName"), rs.getDouble("weight"),
                        courseName, rs.getInt("templateID"), rs.getDate("dateAssigned"),
                        rs.getDate("dateDue"));
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    //same concern with courseName -> courseID. categoryName could also be categoryID to avoid overlaps
    public static ArrayList<Item> getItemByCourse (String courseName){
        ArrayList<Item> items = new ArrayList<>();
        String query = "select id, fieldName, categoryName, weight, templateID, " +
                "dateAssigned, dateDue from Item where courseName='" + courseName +"'";
        ResultSet rs = SQLHelper.performRead(query);

        try{
            while (rs.next()){
                Item item = new Item(rs.getInt("ID"),
                        rs.getString("fieldName"), rs.getString("categoryName"), rs.getDouble("weight"),
                        courseName, rs.getInt("templateID"), rs.getDate("dateAssigned"),
                        rs.getDate("dateDue"));
                items.add(item);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }


    public static ArrayList<Course> getAllCourses(){
        ArrayList<Course> courses = new ArrayList<>();
        String query = "Select * from Course";
        ResultSet rs = SQLHelper.performRead(query);

        try{
            while(rs.next()){
                Course course = new Course(rs.getInt("ID"), rs.getString("courseName"),
                        rs.getString("term"));
                courses.add(course);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return courses;
    }

    public static ArrayList<GradeEntry> getGradeEntries(int courseID){
        ArrayList<GradeEntry> gradeEntries = new ArrayList<>();
        String query = "Select * from GradeEntry where courseID='" + courseID +"'";
        ResultSet rs = SQLHelper.performRead(query);
        try{
            while(rs.next()){
                GradeEntry ge = new GradeEntry(rs.getInt("ID"), rs.getString("entryName"),
                        rs.getInt("itemID"), rs.getInt("categoryID"), rs.getDouble("maxPoint"),
                        rs.getDouble("pointsEarned"), rs.getInt("courseID"), rs.getString("BUID"),
                        rs.getString("comment"), rs.getDate("date"));
                gradeEntries.add(ge);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return gradeEntries;
    }

    //does not currently obtain active status of the students
    public static ArrayList<Student> getStudentsByCourse(int courseID) {
        ArrayList<Student> students = new ArrayList<>();
        String query = "Select A.* from Student A where A.BUID in (select B.BUID from CourseStudent B where B.courseID ='" + courseID + "')";
        ResultSet rs = SQLHelper.performRead(query);
        try {
            while (rs.next()) {
                Student student = new Student(rs.getString("fName"),
                        rs.getString("lName"), rs.getString("BUID"), rs.getString("type"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}
