package controller;

import java.util.*;
import model.*;

public class Master {

	private ArrayList<Course> courses; 
	
	public Master() {
		this.courses = new ArrayList<Course>();	
	}
	
	public Master(ArrayList<Course> courses) {
		this.courses = courses; 
	}
	
	/*
	 * Create Methods
	 */
	//-------------------------------------------------------------------------------------------------------------------------------------//
	
	public void addNewCourse(int courseId, String courseName, String term) {
		Course newCourse = new Course(courseId, courseName, term);
		this.courses.add(newCourse);
	}
	
	public void addNewCourse(int courseId, String courseName, String term, ArrayList<Category> categories, ArrayList<CourseStudent> students) {
		Course newCourse = new Course(courseId, courseName, term, categories, students);
		this.courses.add(newCourse);
	}
	
	
}
