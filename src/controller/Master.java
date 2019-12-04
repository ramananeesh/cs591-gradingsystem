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
		Course newCourse = new Course(generateCourseId(), courseId, courseName, term);
		this.courses.add(newCourse);

		setChanged();
		notifyObservers();
	}

	public void addNewCourse(String courseId, String courseName, String term, ArrayList<Category> categories,
			ArrayList<CourseStudent> students) {
		Course newCourse = new Course(generateCourseId(), courseId, courseName, term, categories, students);
		this.courses.add(newCourse);

		setChanged();
		notifyObservers();
	}

	public void addNewCourse(String courseId, String courseName, String term, ArrayList<Category> categories) {
		Course newCourse = new Course(generateCourseId(), courseId, courseName, term, categories);
		this.courses.add(newCourse);

		setChanged();
		notifyObservers();
	}

	public void addNewCategoryForCourse(Course course, String fieldName, double weight, int courseId) {
		course.addCategory(new Category(course.getCategories().size() + 1, fieldName, weight, courseId));

		setChanged();
		notifyObservers();
	}

	public void addNewCategoryForCourse(Course course, int id, String fieldName, double weight, int courseId) {
		course.addCategory(new Category(id, fieldName, weight, courseId));

		setChanged();
		notifyObservers();
	}

	public void addNewCategoryForCourse(Course course, int id, String fieldName, double weight, int courseId,
			ArrayList<Item> items) {
		course.addCategory(new Category(id, fieldName, weight, courseId, items));

		setChanged();
		notifyObservers();
	}

	public void addItemForCourseCategory(Course course, int categoryIndex, String fieldName, double weight, double maxPoints) {

		Category category = course.getCategories().get(categoryIndex);
		category.addItem(
				new Item(category.getItems().size() + 1, fieldName, category.getId(), weight, maxPoints, course.getCourseId()));

		setChanged();
		notifyObservers();
	}

	public void addStudentsForCourse(Course course, ArrayList<HashMap<String, String>> students) {

		for (HashMap<String, String> student : students) {
			CourseStudent cs = new CourseStudent(student.get("fname"), student.get("lname"), student.get("buid"),
					student.get("email"), student.get("type"), course.getCourseId(), true);
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
			newCategories.add(new Category(c.getId(), c.getFieldName(), c.getWeight(), c.getCourseId(), items));
		}
		return newCategories;
	}

	public ArrayList<Item> getTemplateItemsForCourseCategory(Category category) {
		ArrayList<Item> newItems = new ArrayList<Item>();

		ArrayList<Item> existingItems = category.getItems();

		for (Item i : existingItems) {
			newItems.add(new Item(i.getId(), i.getFieldName(), i.getCategoryId(), i.getWeight(), i.getMaxPoints(),
					i.getCourseId(), i.getDateAssigned(), i.getDateDue()));
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

	public ArrayList<Item> getAllItemsForCourseCategory(Course course, int categoryIndex) {
		return course.getCategory(categoryIndex).getItems();
	}

	public ArrayList<Category> getAllCategoriesForCourse(Course course) {
		return course.getCategories();
	}

	public ArrayList<HashMap<String, Double>> getAllGradeEntriesForAllStudents(Course course) {
		ArrayList<HashMap<String, Double>> l = new ArrayList<>();
		for (CourseStudent student : course.getStudents()) {
			l.add(student.getAllGradeEntries());
		}

		return l;
	}

	/*
	 * Method loops through all students to get the student with particular buid
	 * Then, goes through all grade entry's for the student and checks if there
	 * exists a grade entry for the given categoryId and itemId if yes, returns the
	 * index of grade entry. If not returns -1 Every grade entry for a student is
	 * unique to a (categoryId + itemId)
	 */
	public int doesGradeEntryExistForStudent(Course course, String buid, int categoryId, int itemId) {
		ArrayList<CourseStudent> students = course.getStudents();

		for (CourseStudent student : students) {
			if (student.getBuid().equals(buid)) {
				ArrayList<GradeEntry> entries = student.getGrades();
				for (int i = 0; i < entries.size(); i++) {
					GradeEntry e = entries.get(i);
					if (e.getCategoryId() == categoryId && e.getItemId() == itemId) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	public CourseStudent editGradeEntryForStudent(Course course, CourseStudent student, GradeEntry gradeEntry) {

		int gradeEntryIndex = doesGradeEntryExistForStudent(course, student.getBuid(), gradeEntry.getCategoryId(),
				gradeEntry.getItemId());

		// if index returns as -1, add new gradeEntry
		if (gradeEntryIndex == -1) {
			student.addGradeEntry(gradeEntry);
		} else {
			student.setGradeEntry(gradeEntryIndex, gradeEntry);
		}

		return student;
	}

	public void editGradesForCategoryItemInCourse(Course course, int categoryId, int itemId,
			ArrayList<HashMap<String, String>> maps) {
		Category category = course.getCategoryById(categoryId);
		Item item = category.getItemById(itemId);

		for (HashMap<String, String> h : maps) {
			String buid = h.get("Buid");
			int studentIndex = course.getStudentIndexById(buid);
			CourseStudent student = course.getStudent(studentIndex);
			GradeEntry newEntry = new GradeEntry(item.getFieldName(), itemId, categoryId, item.getMaxPoints(),
					Double.parseDouble(h.get("Score")),Double.parseDouble(h.get("Percentage")), course.getCourseId(), h.get("Comments"));
			student = editGradeEntryForStudent(course, student, newEntry);
			course.setStudent(studentIndex, student);
		}
	}

	public ArrayList<String> getAllItemNames(Course course) {
		ArrayList<String> names = new ArrayList<>();

		for (Category category : course.getCategories()) {
			for (Item i : category.getItems()) {
				names.add(i.getFieldName());
			}
		}
		return names;
	}

	public String[][] getItemDetailsForCourseCategory(Course course, int categoryIndex) {
		return course.getCategories().get(categoryIndex).getItemsForList();
	}

	public String[][] getAllStudentsForCourse(Course course) {
		ArrayList<String[]> str = new ArrayList<String[]>();

		for (CourseStudent student : course.getStudents()) {
			str.add(student.getStudentDataForTable());
		}

		String[][] ans = new String[str.size()][];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = str.get(i);
		}

		return ans;
	}

	public boolean isIdUnique(int id) {
		for (Course course : this.courses) {
			if (id == course.getCourseId())
				return false;
		}
		return true;
	}

	public int generateCourseId() {
		Random rand = new Random();
		int id;
		while (true) {
			id = rand.nextInt(99999) + 1;
			if (isIdUnique(id)) {
				return id;
			}
		}
	}

	public void setCourse(int index, Course newCourse) {
		this.courses.remove(index);
		this.courses.add(index, newCourse);
	}
}
