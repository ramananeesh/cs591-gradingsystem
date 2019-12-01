package controller;

import java.util.*;
import model.*;

public class Master extends Observable {

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
	// -------------------------------------------------------------------------------------------------------------------------------------//

	public void addNewCourse(String courseId, String courseName, String term) {
		Course newCourse = new Course(courseId, courseName, term);
		this.courses.add(newCourse);

		setChanged();
		notifyObservers();
	}

	public void addNewCourse(String courseId, String courseName, String term, ArrayList<Category> categories,
			ArrayList<CourseStudent> students) {
		Course newCourse = new Course(courseId, courseName, term, categories, students);
		this.courses.add(newCourse);
		
		setChanged();
		notifyObservers();
	}

	// helper methods
	public Course getTemplateCourse(int templateIndex) {
		return this.courses.get(templateIndex);
	}

	public ArrayList<Category> getTemplateCategoriesForCourse(Course course) {
		ArrayList<Category> newCategories = new ArrayList<Category>();

		ArrayList<Category> existingCategories = course.getCategories();

		for (Category c : existingCategories) {
			ArrayList<Item> items = getTemplateItemsForCourseCategory(c);
			newCategories.add(new Category(c.getId(), c.getFieldName(), c.getWeight(), c.getCourseName(), items));
		}
		return newCategories;
	}

	public ArrayList<Item> getTemplateItemsForCourseCategory(Category category) {
		ArrayList<Item> newItems = new ArrayList<Item>();

		ArrayList<Item> existingItems = category.getItems();

		for (Item i : existingItems) {
			newItems.add(new Item(i.getId(), i.getFieldName(), i.getCategoryName(), i.getWeight(), i.getCourseName(),
					i.getDateAssigned(), i.getDateDue()));
		}

		return newItems;
	}

	public String[][] getAllCourseDetails() {
		String[][] allCourseDetails = new String[this.getCourseCount()][];
		
		int i=0;
		for(Course c: this.courses) {
			allCourseDetails[i] = c.getDetails();
			i++;
		}
		
		return allCourseDetails;
	}
	
	public int getCourseCount() {
		return this.courses.size();
	}
}
