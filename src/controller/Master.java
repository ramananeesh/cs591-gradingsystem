package controller;

import java.util.*;
import model.*;

public class Master extends Observable {

	private ArrayList<Course> courses;
	private Course currentCourse;

	public Master() {
		this.courses = new ArrayList<Course>();
		this.currentCourse = null;
	}

	public Master(ArrayList<Course> courses) {
		this.courses = courses;
		this.currentCourse = null;
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

	public void addNewCourse(String courseId, String courseName, String term, ArrayList<Category> categories) {
		Course newCourse = new Course(courseId, courseName, term, categories);
		this.courses.add(newCourse);

		setChanged();
		notifyObservers();
	}

	public void addNewCategoryForCourse(Course course, String fieldName, double weight, String courseName) {
		course.addCategory(new Category(course.getCategories().size() + 1, fieldName, weight, courseName));

		setChanged();
		notifyObservers();
	}

	public void addNewCategoryForCourse(Course course, int id, String fieldName, double weight, String courseName) {
		course.addCategory(new Category(id, fieldName, weight, courseName));

		setChanged();
		notifyObservers();
	}

	public void addNewCategoryForCourse(Course course, int id, String fieldName, double weight, String courseName,
			ArrayList<Item> items) {
		course.addCategory(new Category(id, fieldName, weight, courseName, items));

		setChanged();
		notifyObservers();
	}

	public void addItemForCourseCategory(Course course, int categoryIndex, String fieldName, double weight) {

		Category category = course.getCategories().get(categoryIndex);
		category.addItem(new Item(category.getItems().size() + 1, fieldName, category.getFieldName(), weight,
				course.getCourseName()));
		
		setChanged();
		notifyObservers();
	}
	
	public void addStudentsForCourse(Course course, ArrayList<HashMap<String, String>> students) {
		
		for(HashMap<String,String> student: students) {
			CourseStudent cs = new CourseStudent(student.get("fname"), student.get("lname"), student.get("buid"), student.get("email"),student.get("type"), course.getCourseId(), true);
			course.addStudent(cs);
		}
		
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

		int i = 0;
		for (Course c : this.courses) {
			allCourseDetails[i] = c.getDetails();
			i++;
		}

		return allCourseDetails;
	}

	public int getCourseCount() {
		return this.courses.size();
	}

	public Course getCourse(int index) {
		return this.courses.get(index);
	}

	public void setCurrentCourse(Course course) {
		this.currentCourse = course;
	}

	public void setCurrentCourse(int index) {
		this.currentCourse = this.courses.get(index);
	}

	public Course getCurrentCourse() {
		return this.currentCourse;
	}

	public String[][] getAllItemsDetailsForCourse(Course course) {
		ArrayList<String[]> str = new ArrayList<String[]>();
		for (Category c : course.getCategories()) {
			String[][] itemsDetails = c.getItemsForList();
			for (int i = 0; i < itemsDetails.length; i++) {
				str.add(itemsDetails[i]);
			}
		}

		String[][] ans = new String[str.size()][];

		for (int i = 0; i < ans.length; i++) {
			ans[i] = str.get(i);
		}
		return ans;
	}
	
	public String[][] getItemDetailsForCourseCategory(Course course, int categoryIndex){
		return course.getCategories().get(categoryIndex).getItemsForList();
	}
	
	public String[][] getAllStudentsForCourse(Course course){
		ArrayList<String[]> str = new ArrayList<String[]>();
		
		for(CourseStudent student: course.getStudents()) {
			str.add(student.getStudentDataForTable());
		}
		
		String [][]ans = new String[str.size()][];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = str.get(i);
		}
		
		return ans; 
	}
}
