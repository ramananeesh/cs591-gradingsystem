package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import db.Create;
import db.Delete;
import db.Read;
import db.Update;
import model.Category;
import model.Course;
import model.CourseStudent;
import model.FinalGrade;
import model.GradeEntry;
import model.Item;
import model.Student;

/**
 * The master controller for the grading system.
 */
public class Master extends Observable {

	/** List of all the courses. */
	private ArrayList<Course> courses;

	/** Current course. */
	private Course currentCourse;

	/**
	 * Initiate a newly created Master object by loading data from database.
	 */
	public Master() {
		this.courses = new ArrayList<>();
		this.currentCourse = null;

		initialize();
	}

	/**
	 * Initiate a newly created Master object with a list
	 *
	 * @param courses List of courses.
	 */
	public Master(ArrayList<Course> courses) {
		this.courses = courses;
		this.currentCourse = null;
	}

	/**
	 * Loading course data from database.
	 */
	public void initialize() {
		this.courses.addAll(Read.getAllCourses());

		setChanged();
		notifyObservers();
	}

	// ------------------------------------------------- Add -------------------------------------------------

	/**
	 * Add a new course.
	 *
	 * @param courseNumber Course number.
	 * @param courseName   Course name.
	 * @param term         Term.
	 */
	public void addNewCourse(String courseNumber, String courseName, String term) {
		Course newCourse = new Course(generateCourseId(), courseNumber, courseName, term);
		this.courses.add(newCourse);

		// write to db
		Create.insertNewCourse(newCourse);

		setChanged();
		notifyObservers();
	}

	/**
	 * Add a new course.
	 *
	 * @param courseNumber Course number.
	 * @param courseName   Course name.
	 * @param term         Term.
	 * @param categories   List of categories.
	 * @param students     List of students.
	 * @param flag         flag.
	 */
	public void addNewCourse(String courseNumber, String courseName, String term, ArrayList<Category> categories,
	                         ArrayList<CourseStudent> students, boolean flag) {

		int id = generateCourseId();

		if (flag) {
			// replace all category and items with course ID
			for (int i = 0; i < categories.size(); i++) {
				categories.get(i).setCourseId(id);
				categories.get(i).replaceAllCourseIdsInItems(id);
			}
		}
		Course newCourse = new Course(id, courseNumber, courseName, term, categories, students);
		this.courses.add(newCourse);

		// write to db
		Create.insertNewCourse(newCourse);

		// write categories to db
		for (Category c : newCourse.getCategories()) {
			Create.insertNewCategory(c);

			for (Item i : c.getItems()) {
				Create.insertNewItem(i);
			}
		}

		for (CourseStudent student : newCourse.getStudents()) {
			Create.insertNewStudent(student);
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Add a new course.
	 *
	 * @param courseId   Course ID.
	 * @param courseName Course name.
	 * @param term       Term.
	 * @param categories List of categories.
	 * @param flag       Flag.
	 */
	public void addNewCourse(String courseId, String courseName, String term, ArrayList<Category> categories,
	                         boolean flag) {
		int id = generateCourseId();

		if (flag) {
			// replace all category and items with course ID
			for (Category category : categories) {
				category.setCourseId(id);
				category.replaceAllCourseIdsInItems(id);
			}
		}
		Course newCourse = new Course(id, courseId, courseName, term, categories);
		this.courses.add(newCourse);

		// write to db
		Create.insertNewCourse(newCourse);

		// write categories to db
		for (Category c : newCourse.getCategories()) {
			Create.insertNewCategory(c);

			for (Item i : c.getItems()) {
				Create.insertNewItem(i);
			}
		}

		for (CourseStudent student : newCourse.getStudents()) {
			Create.insertNewStudent(student);
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Add a new category for a course.
	 *
	 * @param course    Course object.
	 * @param fieldName Category name.
	 * @param weight    Category weight.
	 * @param courseId  Course ID.
	 */
	public void addNewCategoryForCourse(Course course, String fieldName, double weight, int courseId) {
		Category category = new Category(generateCategoryId(course), fieldName, weight, courseId);
		course.addCategory(category);

		Create.insertNewCategory(category);

		setChanged();
		notifyObservers();
	}

	/**
	 * Add a new category for a course.
	 *
	 * @param course    Course object.
	 * @param id        Category ID.
	 * @param fieldName Category name.
	 * @param weight    Category weight.
	 * @param courseId  Course ID.
	 */
	public void addNewCategoryForCourse(Course course, int id, String fieldName, double weight, int courseId) {
		Category category = new Category(id, fieldName, weight, courseId);
		course.addCategory(category);

		Create.insertNewCategory(category);

		setChanged();
		notifyObservers();
	}

	/**
	 * Add a new category for a course.
	 *
	 * @param course    Course object.
	 * @param id        Item ID.
	 * @param fieldName Item name.
	 * @param weight    Item weight.
	 * @param courseId  Course ID.
	 * @param items     List of items.
	 */
	public void addNewCategoryForCourse(Course course, int id, String fieldName, double weight, int courseId,
	                                    ArrayList<Item> items) {
		Category category = new Category(id, fieldName, weight, courseId, items);
		course.addCategory(category);

		Create.insertNewCategory(category);

		setChanged();
		notifyObservers();
	}

	/**
	 * Add a item for a category.
	 *
	 * @param course        Course object.
	 * @param categoryIndex Category index.
	 * @param fieldName     Item name.
	 * @param weight        Item weight.
	 * @param maxPoints     Maximum points of the item.
	 */
	public void addItemForCourseCategory(Course course, int categoryIndex, String fieldName, double weight,
	                                     double maxPoints) {

		Category category = course.getCategories().get(categoryIndex);
		Item item = new Item(generateItemId(category), fieldName, category.getCategoryId(), weight, maxPoints,
				course.getCourseId());
		category.addItem(item);

		Create.insertNewItem(item);

		setChanged();
		notifyObservers();
	}

	/**
	 * Add new students for a course.
	 *
	 * @param course   Course object.
	 * @param students List of Student objects.
	 */
	public void addStudentsForCourse(Course course, ArrayList<HashMap<String, String>> students) {

		for (HashMap<String, String> student : students) {
			CourseStudent cs = new CourseStudent(student.get("fname"), student.get("lname"), student.get("buid"),
					student.get("email"), student.get("type"), course.getCourseId(), true);
			course.addStudent(cs);
			Student st = new Student(cs.getFirstName(), cs.getLastName(), cs.getStudentId(), cs.getEmail(), cs.getType());

			Create.insertNewStudent(st);
			Create.insertNewCourseStudent(cs);
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Add new students for a course.
	 *
	 * @param course  Course object.
	 * @param student Array of Student objects.
	 */
	public void addStudentForCourse(Course course, String[] student) {
		CourseStudent cs = new CourseStudent(student[0], student[1], student[2], student[3], student[4],
				course.getCourseId(), true);
		course.addStudent(cs);
		Student st = new Student(cs.getFirstName(), cs.getLastName(), cs.getStudentId(), cs.getEmail(), cs.getType());

		Create.insertNewStudent(st);
		Create.insertNewCourseStudent(cs);

		setChanged();
		notifyObservers();
	}

	/**
	 * Get a template course.
	 *
	 * @param templateIndex Template index.
	 * @return A template course.
	 */
	public Course getTemplateCourse(int templateIndex) {
		return this.courses.get(templateIndex);
	}

	/**
	 * Get template categories for a course.
	 *
	 * @param course Course object.
	 * @return List of categories.
	 */
	public ArrayList<Category> getTemplateCategoriesForCourse(Course course) {
		ArrayList<Category> newCategories = new ArrayList<>();

		ArrayList<Category> existingCategories = course.getCategories();

		for (Category c : existingCategories) {
			ArrayList<Item> items = getTemplateItemsForCourseCategory(c);
			newCategories.add(new Category(c.getCategoryId(), c.getCategoryName(), c.getWeight(), c.getCourseId(), items));
		}
		return newCategories;
	}

	/**
	 * Get template items for course category.
	 *
	 * @param category Category.
	 * @return List of items.
	 */
	private ArrayList<Item> getTemplateItemsForCourseCategory(Category category) {
		ArrayList<Item> newItems = new ArrayList<>();

		ArrayList<Item> existingItems = category.getItems();

		for (Item i : existingItems) {
			newItems.add(new Item(i.getItemId(), i.getItemName(), i.getCategoryId(), i.getWeight(), i.getMaxPoints(),
					i.getCourseId()));
		}

		return newItems;
	}

	/**
	 * Get all course details.
	 *
	 * @return A two-dimensional array that contains course details represented by String.
	 */
	public String[][] getAllCourseDetails() {
		String[][] allCourseDetails = new String[this.getCourseCount()][];

		int i = 0;
		for (Course c : this.courses) {
			allCourseDetails[i] = c.getDetails();
			i++;
		}

		return allCourseDetails;
	}

	/**
	 * Get count of courses.
	 *
	 * @return Count of courses.
	 */
	private int getCourseCount() {
		return this.courses.size();
	}

	/**
	 * Get a course.
	 *
	 * @param index Course index.
	 * @return A course corresponded to index.
	 */
	public Course getCourse(int index) {
		return this.courses.get(index);
	}

	/**
	 * Get current course.
	 *
	 * @return Current course.
	 */
	public Course getCurrentCourse() {
		return this.currentCourse;
	}

	/**
	 * Set current course.
	 *
	 * @param course Course object.
	 */
	public void setCurrentCourse(Course course) {
		this.currentCourse = course;
	}

	/**
	 * Set current course.
	 *
	 * @param index Course index.
	 */
	public void setCurrentCourse(int index) {
		this.currentCourse = this.courses.get(index);
	}

	/**
	 * Get all items details for a course.
	 *
	 * @param course Course object.
	 * @return A two-dimensional array that contains items details represented by String.
	 */
	public String[][] getAllItemsDetailsForCourse(Course course) {
		ArrayList<String[]> str = new ArrayList<>();
		for (Category c : course.getCategories()) {
			String[][] itemsDetails = c.getItemsForList();
			str.addAll(Arrays.asList(itemsDetails));
		}

		String[][] ans = new String[str.size()][];

		for (int i = 0; i < ans.length; i++) {
			ans[i] = str.get(i);
		}
		return ans;
	}

	/**
	 * Get all items for a category.
	 *
	 * @param course        Course object.
	 * @param categoryIndex Category index.
	 * @return List of all items for a category.
	 */
	public ArrayList<Item> getAllItemsForCourseCategory(Course course, int categoryIndex) {
		return course.getCategory(categoryIndex).getItems();
	}

	/**
	 * Get all categories for a course.
	 *
	 * @param course A course object.
	 * @return All categories for a course.
	 */
	public ArrayList<Category> getAllCategoriesForCourse(Course course) {
		return course.getCategories();
	}

	/**
	 * Get grade entries for all students in a course.
	 *
	 * @param course Course object.
	 * @return Grade entries for all students in a course.
	 */
	public ArrayList<HashMap<String, Double>> getAllGradeEntriesForAllStudents(Course course) {
		ArrayList<HashMap<String, Double>> gradeEntryMapList = new ArrayList<>();
		for (CourseStudent student : course.getStudents()) {
			gradeEntryMapList.add(student.getAllGradeEntries());
		}

		return gradeEntryMapList;
	}

	/**
	 * Method loops through all students to get the student with particular buid.
	 * Then, goes through all grade entry's for the student,
	 * and checks if there exists a grade entry for the given categoryId and itemId.
	 * If yes, returns the index of grade entry. If not, returns -1.
	 * Every grade entry for a student is unique to a (categoryId + itemId)
	 *
	 * @param course     Course object.
	 * @param studentId       Student ID.
	 * @param categoryId Category ID.
	 * @param itemId     Item ID.
	 * @return Index of grade entry if exists, otherwise -1.
	 */
	private int doesGradeEntryExistForStudent(Course course, String studentId, int categoryId, int itemId) {
		ArrayList<CourseStudent> students = course.getStudents();

		for (CourseStudent student : students) {
			if (student.getStudentId().equals(studentId)) {
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

	/**
	 * Edit grade entry for a student.
	 *
	 * @param course     Course object.
	 * @param student    Student.
	 * @param gradeEntry Grade entry.
	 * @return Student with updated grade entry.
	 */
	private CourseStudent editGradeEntryForStudent(Course course, CourseStudent student, GradeEntry gradeEntry) {

		int gradeEntryIndex = doesGradeEntryExistForStudent(course, student.getStudentId(), gradeEntry.getCategoryId(),
				gradeEntry.getItemId());

		// if index returns as -1, add new gradeEntry
		if (gradeEntryIndex == -1) {
			student.addGradeEntry(gradeEntry);
			Create.insertNewGradeEntry(gradeEntry, student.getStudentId());
		} else {
			student.setGradeEntry(gradeEntryIndex, gradeEntry);
			Update.updateCourseStudentGradeEntry(student, course.getCourseId(), gradeEntry.getCategoryId(),
					gradeEntry.getItemId());
		}

		return student;
	}

	/**
	 * Edit grades for a category and a item in a course.
	 *
	 * @param course             Course object.
	 * @param categoryId         Category ID.
	 * @param itemId             Item ID.
	 * @param gradeEntryDataMaps List of grade entry maps.
	 */
	public void editGradesForCategoryItemInCourse(Course course, int categoryId, int itemId,
	                                              ArrayList<HashMap<String, String>> gradeEntryDataMaps) {
		Category category = course.getCategoryById(categoryId);
		Item item = category.getItemById(itemId);

		for (HashMap<String, String> map : gradeEntryDataMaps) {
			String buid = map.get("Buid");
			int studentIndex = course.getStudentIndexById(buid);
			CourseStudent student = course.getStudent(studentIndex);
			GradeEntry newEntry = new GradeEntry(item.getItemName(), itemId, categoryId, item.getMaxPoints(),
					Double.parseDouble(map.get("Score")), Double.parseDouble(map.get("Percentage")), course.getCourseId(),
					map.get("Comments"));
			editGradeEntryForStudent(course, student, newEntry);
			course.setStudent(studentIndex, student);
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Get names of all items.
	 *
	 * @param course Course object.
	 * @return List of names of all items.
	 */
	public ArrayList<String> getAllItemNames(Course course) {
		ArrayList<String> names = new ArrayList<>();

		for (Category category : course.getCategories()) {
			for (Item i : category.getItems()) {
				names.add(i.getItemName());
			}
		}
		return names;
	}

	/**
	 * Get item details for category.
	 *
	 * @param course           Course object.
	 * @param categoryIndex    Category index.
	 * @param includeMaxPoints Whether include max points or not.
	 * @return Item details for category represented by two-dimensional String array.
	 */
	public String[][] getItemDetailsForCourseCategory(Course course, int categoryIndex, boolean includeMaxPoints) {
		if (!includeMaxPoints)
			return course.getCategories().get(categoryIndex).getItemsForList();
		else
			return course.getCategories().get(categoryIndex).getItemsForListWithMaxPoints();
	}

	/**
	 * Get all item details for a course.
	 *
	 * @param course           Course object.
	 * @param includeMaxPoints Whether include max points or not.
	 * @return Item details for a course represented by two-dimensional String array.
	 */
	public String[][] getAllItemDetailsForCourse(Course course, boolean includeMaxPoints) {
		String[][] details = new String[getAllItemsForCourse(course).size()][];
		int i = 0;
		for (Item item : getAllItemsForCourse(course)) {
			if (!includeMaxPoints)
				details[i++] = item.getDetailsWithCategory(course);
			else
				details[i++] = item.getDetailsWithMaxPointsWithCategory(course);
		}
		return details;
	}

	/**
	 * Get all students details for a course.
	 *
	 * @param course Course object.
	 * @return Student details for a course represented by two-dimensional String array.
	 */
	public String[][] getAllStudentsForCourse(Course course) {
		ArrayList<String[]> stringList = new ArrayList<>();

		for (CourseStudent student : course.getStudents()) {
			stringList.add(student.getStudentDataForTable());
		}

		String[][] answer = new String[stringList.size()][];
		for (int i = 0; i < answer.length; i++) {
			answer[i] = stringList.get(i);
		}

		return answer;
	}

	/**
	 * Get all students status for a course.
	 *
	 * @param course Course object.
	 * @return Status of all students.
	 */
	public Boolean[] getAllStudentsStatusForCourse(Course course) {
		ArrayList<Boolean> status = new ArrayList<>();
		for (CourseStudent student : course.getStudents()) {
			status.add(student.isActive());
		}
		Boolean[] ans = new Boolean[status.size()];
		return status.toArray(ans);
	}

	/**
	 * Check if an ID is unique.
	 *
	 * @param id ID to be checked.
	 * @return Whether an ID is unique or not.
	 */
	private boolean isIdUnique(int id) {
		for (Course course : this.courses) {
			if (id == course.getCourseId())
				return false;
		}
		return true;
	}

	/**
	 * Generate a new ID for a course.
	 *
	 * @return A new ID for a course.
	 */
	private int generateCourseId() {
		Random rand = new Random();
		int id;
		while (true) {
			id = rand.nextInt(99999) + 1;
			if (isIdUnique(id)) {
				return id;
			}
		}
	}

	/**
	 * Generate a new ID for a category.
	 *
	 * @return A new ID for a category.
	 */
	private int generateCategoryId(Course course) {
		Random rand = new Random();
		int id;
		while (true) {
			id = rand.nextInt(99999) + 1;

			if (isUniqueCategoryId(course, id)) {
				return id;
			}
		}
	}

	/**
	 * Generate a new ID for a item.
	 *
	 * @return A new ID for a item.
	 */
	private int generateItemId(Category category) {
		Random rand = new Random();
		int id;
		while (true) {
			id = rand.nextInt(99999) + 1;
			if (isUniqueItemId(category, id)) {
				return id;
			}
		}
	}

	/**
	 * Set a course.
	 *
	 * @param index     Course index.
	 * @param newCourse A new course.
	 */
	public void setCourse(int index, Course newCourse) {
		this.courses.remove(index);
		this.courses.add(index, newCourse);
	}

	/**
	 * Get all items for a course.
	 *
	 * @param course Course object.
	 * @return All item for the course.
	 */
	public ArrayList<Item> getAllItemsForCourse(Course course) {
		ArrayList<Item> allItem = new ArrayList<>();

		for (Category category : course.getCategories()) {
			allItem.addAll(category.getItems());
		}
		return allItem;
	}

	/**
	 * Get student score by ID.
	 *
	 * @param student    Student.
	 * @param courseID   Course ID.
	 * @param categoryID Category ID.
	 * @param itemID     Item ID.
	 * @return Student score.
	 */
	public String getStudentScoreByID(CourseStudent student, int courseID, int categoryID, int itemID) {
		GradeEntry entry = student.getGradeEntryForItemInCategory(courseID, categoryID, itemID);
		if (entry != null) {
			String grade = Double.toString(entry.getPointsEarned() / entry.getMaxPoints());
			if (!entry.getComments().equals("")) { // has comment
				return grade + " 1";
			} else {
				return grade + " 0";
			}
		}
		return "0.0 0";
	}

	/**
	 * Get student score by ID.
	 *
	 * @param student    Student.
	 * @param courseID   Course ID.
	 * @param categoryID Category ID.
	 * @return Student score.
	 */
	public String getStudentScoreByID(CourseStudent student, int courseID, int categoryID) {
		Course currentCourse = null;
		for (Course course : courses) {
			if (course.getCourseId() == courseID) {
				currentCourse = course;
				break;
			}
		}
		if (currentCourse != null) {
			Category category = currentCourse.getCategoryById(categoryID);
			List<Item> allItem = category.getItems();
			String grade = Double.toString(student.getGradeEntryForCategory(courseID, categoryID, allItem));
			return grade + " 0";
		} else {
			return "";
		}
	}

	/**
	 * Check if a category ID is unique.
	 *
	 * @param course Course.
	 * @param id     Category ID.
	 * @return Whether a category ID is unique or not.
	 */
	private boolean isUniqueCategoryId(Course course, int id) {
		for (Category category : course.getCategories()) {
			if (id == category.getCategoryId())
				return false;
		}
		return true;
	}

	/**
	 * Check if a item ID is unique.
	 *
	 * @param category Category.
	 * @param id       Item ID.
	 * @return Whether a item ID is unique or not.
	 */
	private boolean isUniqueItemId(Category category, int id) {
		for (Item i : category.getItems()) {
			if (id == i.getItemId())
				return false;
		}
		return true;
	}

	// ------------------------------------------------- Modify -------------------------------------------------

	/**
	 * Modify categories for a course.
	 *
	 * @param course           Course.
	 * @param categoryDataMaps Category data maps.
	 */
	public void modifyCategoriesForCourse(Course course, ArrayList<HashMap<String, Double>> categoryDataMaps) {
		ArrayList<Category> categories = course.getCategories();

		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);

			HashMap<String, Double> map = categoryDataMaps.get(i);

			if (map.get(category.getCategoryName()) != category.getWeight()) {
				Category modified = new Category(category.getCategoryId(), category.getCategoryName(),
						map.get(category.getCategoryName()), category.getCourseId(), category.getItems());

				course.setCategory(i, modified);
				// update db for modification to category
				Update.updateCategory(modified);
			}
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Modify items for a category.
	 *
	 * @param course        Course.
	 * @param categoryIndex Category index.
	 * @param itemDataMaps  Item data maps.
	 */
	public void modifyItemsForCourseCategory(Course course, int categoryIndex, HashMap<String, ArrayList<Double>> itemDataMaps) {
		Category category = course.getCategory(categoryIndex);

		ArrayList<Item> items = category.getItems();

		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);

			boolean flag = false;
			ArrayList<Double> l = itemDataMaps.get(item.getItemName());
			if (item.getWeight() != l.get(0))
				flag = true;
			else if (item.getMaxPoints() != l.get(1))
				flag = true;

			if (flag) {
				Item modifiedItem = new Item(item.getItemId(), item.getItemName(), item.getCategoryId(), l.get(0),
						l.get(1), item.getCourseId());
				category.setItem(i, modifiedItem);
				course.setCategory(categoryIndex, category);

				for (CourseStudent student : course.getStudents()) {
					student.modifyMaxPointsInGradeEntryByItem(item.getItemId(), l.get(1));
				}

				// modify in db
				Update.updateItem(modifiedItem);
			}
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Modify items for a course.
	 *
	 * @param course       Course.
	 * @param itemDataMaps Item data maps.
	 */
	public void modifyItemsForCourse(Course course, HashMap<String, ArrayList<Double>> itemDataMaps) {
		ArrayList<Category> categories = getAllCategoriesForCourse(course);
		for (int i = 0; i < categories.size(); i++) {
			Category cat = categories.get(i);
			ArrayList<Item> items = getAllItemsForCourseCategory(course, i);
			for (int j = 0; j < items.size(); j++) {
				Item item = items.get(j);
				boolean flag = false;
				ArrayList<Double> l = itemDataMaps.get(item.getItemName());
				if (item.getWeight() != l.get(0))
					flag = true;
				else if (item.getMaxPoints() != l.get(1))
					flag = true;

				if (flag) {
					Item modifiedItem = new Item(item.getItemId(), item.getItemName(), item.getCategoryId(), l.get(0),
							l.get(1), item.getCourseId());
					cat.setItem(j, modifiedItem);
					course.setCategory(i, cat);

					for (CourseStudent student : course.getStudents()) {
						student.modifyMaxPointsInGradeEntryByItem(item.getItemId(), l.get(1));
					}

					// modify in db
					Update.updateItem(modifiedItem);
				}
			}
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Modify students for a course.
	 *
	 * @param course          Course.
	 * @param studentDataMaps Student data maps.
	 */
	public void modifyStudentForCourse(Course course, HashMap<String, ArrayList<String>> studentDataMaps) {
		ArrayList<CourseStudent> students = course.getStudents();
		for (int i = 0; i < students.size(); i++) {
			CourseStudent student = students.get(i);
			ArrayList<String> mods = studentDataMaps.get(student.getName());

			String modBuid = mods.get(0).trim();
			String modName = mods.get(1).trim();
			String modType = mods.get(2).trim();

			boolean flag = false;
			if (!modBuid.equals("")) {
				student.setStudentId(modBuid);
				flag = true;
			}

			if (!modName.equals("")) {
				student.setName(modName);
				flag = true;
			}

			if (!modType.equals("")) {
				student.setType(modType);
				flag = true;
			}

			if (flag) {
				course.setStudent(i, student);
			}
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Modify a student for a course.
	 *
	 * @param course         Course.
	 * @param studentIndex   Student index.
	 * @param studentDataMap Student data map.
	 * @param active         Student status.
	 */
	public void modifyStudentForCourse(Course course, int studentIndex, HashMap<String, String> studentDataMap, boolean active) {

		CourseStudent student = course.getStudent(studentIndex);

		String modName = studentDataMap.get("Name").trim();
		String modType = studentDataMap.get("Type").trim();

		boolean flag = false;

		if (!modName.equals("")) {
			student.setName(modName);
			flag = true;
		}

		if (!modType.equals("")) {
			student.setType(modType);
			flag = true;
		}

		student.setActive(active);

		if (flag) {
			course.setStudent(studentIndex, student);
			Update.updateCourseStudentActive(course.getCourseId(), student.getStudentId(), student.isActive());
			setChanged();
			notifyObservers();
		}

	}

	/**
	 * Get student indices for comments.
	 *
	 * @param course        Comments.
	 * @param categoryIndex Category index.
	 * @param itemIndex     Item index.
	 * @return List of Student indices for comments.
	 */
	public ArrayList<Integer> getStudentIndicesForComments(Course course, int categoryIndex, int itemIndex) {
		ArrayList<Integer> indeces = new ArrayList<>();
		Category category = null;
		Item item = null;
		if (categoryIndex != -1) {
			category = course.getCategory(categoryIndex);
			if (itemIndex != -1) {
				item = category.getItem(itemIndex);
			}
		}

		int i = 0;
		for (CourseStudent student : course.getStudents()) {
			ArrayList<GradeEntry> entries = student.getGrades();
			for (GradeEntry entry : entries) {
				if (categoryIndex != -1 && itemIndex != -1) {
					if (entry.getCategoryId() == category.getCategoryId() && entry.getItemId() == item.getItemId()) {
						if (!entry.getComments().trim().equals(""))
							indeces.add(i);
					}
				} else if (categoryIndex != -1) {
					if (entry.getCategoryId() == category.getCategoryId()) {
						if (!entry.getComments().trim().equals(""))
							indeces.add(i);
					}
				} else {
					if (!entry.getComments().trim().equals(""))
						indeces.add(i);
				}
			}
			i++;
		}

		return indeces;
	}

	/**
	 * Get comments for a row.
	 *
	 * @param course        Course.
	 * @param studentIndex  Student index.
	 * @param categoryIndex Category index.
	 * @param itemIndex     Item index.
	 * @return Comment for a row.
	 */
	public String getCommentsForRowIndex(Course course, int studentIndex, int categoryIndex, int itemIndex) {
		StringBuilder str = new StringBuilder();
		CourseStudent student = course.getStudent(studentIndex);
		if (categoryIndex == -1) {
			if (itemIndex == -1) {
				// all category and all items
				for (GradeEntry entry : student.getGrades()) {
					if (!entry.getComments().trim().equals("")) {
						Category cat = course.getCategoryById(entry.getCategoryId());
						Item item = cat.getItemById(entry.getItemId());
						str.append("Category: ").append(cat.getCategoryName()).append("\nItem: ").append(item.getItemName()).append("\nComment: ").append(entry.getComments()).append("\n");
					}
				}
			}
		} else {
			if (itemIndex == -1) {
				// specific category all items
				for (GradeEntry entry : student.getGrades()) {
					if (entry.getCategoryId() == categoryIndex + 1) // categoryId starts with 1
						if (!entry.getComments().trim().equals("")) {
							Category cat = course.getCategoryById(entry.getCategoryId());
							Item item = cat.getItemById(entry.getItemId());
							str.append("Category: ").append(cat.getCategoryName()).append("\tItem: ").append(item.getItemName()).append("\nComment: ").append(entry.getComments()).append("\n");
						}
				}
			} else {
				// specific category specific item
				for (GradeEntry entry : student.getGrades()) {
					List<Item> items;
					if (entry.getCategoryId() == categoryIndex + 1) {
						items = getAllItemsForCourseCategory(course, categoryIndex);
						if (entry.getItemId() == items.get(itemIndex).getItemId()) {
							if (!entry.getComments().trim().equals("")) {
								Category cat = course.getCategoryById(entry.getCategoryId());
								Item item = cat.getItemById(entry.getItemId());
								str.append("Category: ").append(cat.getCategoryName()).append("\tItem: ").append(item.getItemName()).append("\nComment: ").append(entry.getComments()).append("\n");
							}
						}
					}
				}
			}
		}
		return str.toString();
	}

	/**
	 * Get total weighed grade for an item.
	 *
	 * @param item  Item.
	 * @param entry Grade entry.
	 * @return Total weighed grade.
	 */
	public double getTotalForItemIncludingWeight(Item item, GradeEntry entry) {
		return item.getWeight() * entry.getPercentage();
	}

	/**
	 * Get grade entries by a category.
	 *
	 * @param category Category.
	 * @param entries  List of grade entries.
	 * @return List of grade entries.
	 */
	private ArrayList<GradeEntry> getEntriesByCategory(Category category, ArrayList<GradeEntry> entries) {
		ArrayList<GradeEntry> grades = new ArrayList<>();

		for (GradeEntry entry : entries) {
			if (entry.getCategoryId() == category.getCategoryId()) {
				grades.add(entry);
			}
		}

		return grades;
	}

	/**
	 * Get grade entry by an item.
	 *
	 * @param item            Item.
	 * @param categoryEntries Grade entries for a category.
	 * @return Grade entry for an item.
	 */
	private GradeEntry getEntryByItem(Item item, ArrayList<GradeEntry> categoryEntries) {
		GradeEntry entry = null;

		for (GradeEntry e : categoryEntries) {
			if (e.getItemId() == item.getItemId()) {
				entry = e;
				break;
			}
		}

		return entry;
	}

	/**
	 * Get total weighed grade for a category.
	 *
	 * @param course     Course.
	 * @param categoryId Category ID.
	 * @param entries    Grade entry for a category.
	 * @return Total weighed grade for a category.
	 */
	private double getTotalForCategoryIncludingWeight(Course course, int categoryId, ArrayList<GradeEntry> entries) {
		double total = 0;

		Category category = course.getCategoryById(categoryId);
		ArrayList<Item> items = category.getItems();

		ArrayList<GradeEntry> categoryEntries = getEntriesByCategory(category, entries);

		for (Item i : items) {
			GradeEntry entry = getEntryByItem(i, categoryEntries);
			if (entry != null)
				total += (i.getWeight() * entry.getPercentage());
		}

		return total * category.getWeight();
	}

	/**
	 * Get final percentage.
	 *
	 * @param course Course.
	 * @return Final percentage.
	 */
	private Double[] getFinalPercentages(Course course) {
		Double[] percentages = new Double[course.getStudents().size()];
		ArrayList<CourseStudent> students = course.getStudents();
		ArrayList<Category> categories = course.getCategories();

		int i = 0;
		for (CourseStudent student : students) {
			double total = 0;
			ArrayList<GradeEntry> studentEntries = student.getGrades();
			for (Category category : categories) {
				total += getTotalForCategoryIncludingWeight(course, category.getCategoryId(), studentEntries);
			}

			percentages[i++] = total;
		}
		return percentages;
	}

	/**
	 * Get final letter grades.
	 *
	 * @param course           Course.
	 * @param finalPercentages Final percentage.
	 * @return Final letter grades.
	 */
	private String[] getFinalLetterGrades(Course course, Double[] finalPercentages) {
		String[] grades = new String[course.getStudents().size()];

		int i = 0;
		for (Double p : finalPercentages) {
			grades[i++] = helper.Statistics.getLetterGrade(p);
		}

		return grades;
	}

	/**
	 * Initiate course finalization.
	 *
	 * @param course Course.
	 */
	public void initiateCourseFinalization(Course course) {
		if (course.isFinilizationInitialized()) {
			Double[] finalPercentages = getFinalPercentages(course);
			String[] letterGrades = getFinalLetterGrades(course, finalPercentages);

			course.initiateFinalize(finalPercentages, letterGrades);
		}
	}

	/**
	 * Get final grades data.
	 *
	 * @param course Course.
	 * @return Final grades data represented by two-dimensional String array.
	 */
	public String[][] getFinalGradesData(Course course) {
		ArrayList<FinalGrade> finalGrades = course.getFinalGrades();
		String[][] data = new String[finalGrades.size()][];

		for (int i = 0; i < data.length; i++) {
			data[i] = finalGrades.get(i).getDetailsForList();
		}

		return data;
	}

	/**
	 * Set curve for a course.
	 *
	 * @param course Course.
	 * @param curve  Curve percentage.
	 * @param flag   Flag.
	 */
	public void setCurveForCourse(Course course, double curve, boolean flag) {
		course.setCurve(curve);

		setCurveOnCoursePercentages(course);
		if (flag) {
			course.setCurveApplied(true);
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Set curve on a course by percentage.
	 *
	 * @param course Course.
	 */
	private void setCurveOnCoursePercentages(Course course) {
		ArrayList<FinalGrade> finalGrades = course.getFinalGrades();

		for (FinalGrade grade : finalGrades) {
			grade.setCurvedPercentage(grade.getActualPercentage() + course.getCurve());
			grade.setLetterGrade(helper.Statistics.getLetterGrade(grade.getCurvedPercentage()));
		}

		course.setFinalGrades(finalGrades);

		fireUpdate();
	}

	/**
	 * Finalize a course.
	 *
	 * @param course Course.
	 */
	public void finalizeCourse(Course course) {

		// check if curve applied. if not, set curved percentage equal to actual
		// percentage
		if (!course.isCurveApplied()) {
			setCurveForCourse(course, 0.0, false);
		}
		// write to db
		for (FinalGrade grade : course.getFinalGrades()) {
			Create.insertNewFinalGrade(grade, course.getCourseId());
		}
		course.setFinalized(true);
		Update.updateCourseFinalized(course);
		// lock all features and editing
		fireUpdate();
	}

	/**
	 * Delete category for course.
	 *
	 * @param course        Course.
	 * @param categoryIndex Category index.
	 */
	public void deleteCategoryForCourse(Course course, int categoryIndex) {
		Category category = course.getCategory(categoryIndex);
		for (int i = 0; i < category.getItems().size(); i++) {
			Item item = category.getItem(i);
			deleteItemFromCourse(course, item);
		}
		Category cat = course.removeCategory(categoryIndex);
		Delete.removeCategoryFromCourse(cat.getCategoryId(), course.getCourseId());
		fireUpdate();
	}

	/**
	 * Check if a course can be finalized.
	 *
	 * @param course Course.
	 * @return Whether a course can be finalized or not.
	 */
	public boolean canBeFinalized(Course course) {
		return course.canBeFinalized();
	}

	/**
	 * Delete item from a course.
	 *
	 * @param course Course.
	 * @param item   Item to be deleted.
	 */
	public void deleteItemFromCourse(Course course, Item item) {
		Category cat = course.getCategoryById(item.getCategoryId());

		Item r = cat.removeItemById(item.getItemId());

		course.setCategory(course.getCategoryIndexById(cat.getCategoryId()), cat);

		Delete.removeItemFromCategoryInCourse(r.getItemId(), cat.getCategoryId(), course.getCourseId());
		fireUpdate();
	}

	/**
	 * Set changed and update observers.
	 */
	public void fireUpdate() {
		setChanged();
		notifyObservers();
	}

}