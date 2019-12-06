package model;

import java.util.*;

public class Course extends GenericCourse {

	private ArrayList<Category> categories;
	private ArrayList<CourseStudent> students;

	public Course(int courseId, String courseNumber, String courseName, String term) {
		super(courseId, courseNumber, courseName, term);

		this.categories = new ArrayList<Category>();
		this.students = new ArrayList<CourseStudent>();
	}

	public Course(int courseId, String courseNumber, String courseName, String term, ArrayList<Category> categories) {
		super(courseId, courseNumber, courseName, term);

		this.categories = categories;
		this.students = new ArrayList<CourseStudent>();
	}

	public Course(int courseId, String courseNumber, String courseName, String term, ArrayList<Category> categories,
			ArrayList<CourseStudent> students) {
		super(courseId, courseNumber, courseName, term);

		this.categories = categories;
		this.students = students;
	}

	/*
	 * Specific methods
	 */
	// -------------------------------------------------------------------------------------------------------------------//

	public void addCategory(Category newCategory) {
		this.categories.add(newCategory);
	}

	public Category removeCategory(int categoryIndex) {
		Category removedCategory = this.categories.get(categoryIndex);
		this.categories.remove(categoryIndex);

		return removedCategory;
	}

	public void addStudent(CourseStudent newStudent) {
		this.students.add(newStudent);
	}

	public CourseStudent removeStudent(int studentIndex) {
		CourseStudent removedStudent = this.students.get(studentIndex);
		this.students.remove(studentIndex);

		return removedStudent;
	}
	/*
	 * accessors and mutators
	 */
	// -------------------------------------------------------------------------------------------------------------------//

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

	public Category getCategory(int categoryIndex) {
		return this.categories.get(categoryIndex);
	}

	public void setCategory(int categoryIndex, Category newCategory) {
		// remove category at existing position
		this.removeCategory(categoryIndex);

		// add newCategory at the same position.
		// this will shift the category at that index if any
		this.categories.add(categoryIndex, newCategory);
	}

	public CourseStudent getStudent(int studentIndex) {
		return this.students.get(studentIndex);
	}

	public void setStudent(int studentIndex, CourseStudent newStudent) {
		// remove student at existing position
		this.removeStudent(studentIndex);

		// add newStudent at same position
		// this will shift the student at that index if any
		this.students.add(studentIndex, newStudent);
	}

	public String[][] getCategoryDataForList() {
		String[][] str = new String[this.categories.size()][];
		int i = 0;
		for (Category c : this.categories) {
			str[i++] = c.getDataForList();
		}

		return str;
	}

	public Category getCategoryById(int categoryId) {
		for (Category c : this.categories) {
			if (c.getId() == categoryId)
				return c;
		}
		return null;
	}

	public int getStudentIndexById(String buid) {
		for (int i = 0; i < this.students.size(); i++) {
			if (this.students.get(i).getBuid().equals(buid))
				return i;
		}

		return -1;
	}

	public int getItemIdByItemName(int categoryId, String itemName) {
		return this.getCategoryById(categoryId).getItemByItemName(itemName).getId();
	}

	public Item getItemByItemName(int categoryId, String itemName) {
		return this.getCategoryById(categoryId).getItemByItemName(itemName);
	}
	
	public String[] getStudentNamesAsList() {
		String[] str = new String[this.students.size()];
		int i=0;
		for(CourseStudent student: this.students) {
			str[i++] = new String(student.getName() + " - "+student.getBuid());
		}
		
		return str; 
	}
}
