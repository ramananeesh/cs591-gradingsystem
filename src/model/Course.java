package model;

import java.util.*;

public class Course extends GenericCourse {

	private ArrayList<Category> categories; 
	private ArrayList<CourseStudent> students; 
	
	public Course(int courseId, String courseName, String term) {
		super(courseId, courseName, term);
		
		this.categories = new ArrayList<Category>();
		this.students = new ArrayList<CourseStudent>();
	}
	
	public Course(int courseId, String courseName, String term, ArrayList<Category> categories, ArrayList<CourseStudent> students) {
		super(courseId, courseName, term);
		
		this.categories = categories; 
		this.students = students; 
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<Category> categories) {
		this.categories = categories;
	}

	public ArrayList<CourseStudent> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<CourseStudent> students) {
		this.students = students;
	}
	
}
