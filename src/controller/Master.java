package controller;

import java.util.*;

import com.sun.jmx.snmp.agent.SnmpUserDataFactory;
import db.*;
import model.*;
import helper.*;

public class Master extends Observable {

	private ArrayList<Course> courses;
	private Course currentCourse;

	public Master() {
		this.courses = new ArrayList<Course>();
		this.currentCourse = null;

		initialize();
	}

	public Master(ArrayList<Course> courses) {
		this.courses = courses;
		this.currentCourse = null;
	}

	public void initialize() {
		this.courses.addAll(Read.getAllCourses());

		setChanged();
		notifyObservers();
	}

	/*
	 * Create Methods
	 */
	// -------------------------------------------------------------------------------------------------------------------------------------//

	public void addNewCourse(String courseNumber, String courseName, String term) {
		Course newCourse = new Course(generateCourseId(), courseNumber, courseName, term);
		this.courses.add(newCourse);

		// write to db
		Create.insertNewCourse(newCourse);

		setChanged();
		notifyObservers();
	}

	public void addNewCourse(String courseNumber, String courseName, String term, ArrayList<Category> categories,
			ArrayList<CourseStudent> students) {
		Course newCourse = new Course(generateCourseId(), courseNumber, courseName, term, categories, students);
		this.courses.add(newCourse);

		// write to db
		Create.insertNewCourse(newCourse);

		setChanged();
		notifyObservers();
	}

	public void addNewCourse(String courseId, String courseName, String term, ArrayList<Category> categories) {
		Course newCourse = new Course(generateCourseId(), courseId, courseName, term, categories);
		this.courses.add(newCourse);

		// write to db
		Create.insertNewCourse(newCourse);

		setChanged();
		notifyObservers();
	}

	public void addNewCategoryForCourse(Course course, String fieldName, double weight, int courseId) {
		Category category = new Category(generateCategoryId(course), fieldName, weight, courseId);
		course.addCategory(category);

		Create.insertNewCategory(category);

		setChanged();
		notifyObservers();
	}

	public void addNewCategoryForCourse(Course course, int id, String fieldName, double weight, int courseId) {
		Category category = new Category(id, fieldName, weight, courseId);
		course.addCategory(category);

		Create.insertNewCategory(category);

		setChanged();
		notifyObservers();
	}

	public void addNewCategoryForCourse(Course course, int id, String fieldName, double weight, int courseId,
			ArrayList<Item> items) {
		Category category = new Category(id, fieldName, weight, courseId, items);
		course.addCategory(category);

		Create.insertNewCategory(category);

		setChanged();
		notifyObservers();
	}

	public void addItemForCourseCategory(Course course, int categoryIndex, String fieldName, double weight,
			double maxPoints) {

		Category category = course.getCategories().get(categoryIndex);
		Item item = new Item(generateItemId(category), fieldName, category.getId(), weight, maxPoints,
				course.getCourseId());
		category.addItem(item);

		Create.insertNewItem(item);

		setChanged();
		notifyObservers();
	}

	public void addStudentsForCourse(Course course, ArrayList<HashMap<String, String>> students) {

		for (HashMap<String, String> student : students) {
			CourseStudent cs = new CourseStudent(student.get("fname"), student.get("lname"), student.get("buid"),
					student.get("email"), student.get("type"), course.getCourseId(), true);
			course.addStudent(cs);
			Student st = new Student(cs.getFname(), cs.getLname(), cs.getBuid(), cs.getEmail(), cs.getType());

			Create.insertNewStudent(st);
			Create.insertNewCourseStudent(cs);
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
					i.getCourseId()));
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
			Create.insertNewGradeEntry(gradeEntry, student.getBuid());
		} else {
			student.setGradeEntry(gradeEntryIndex, gradeEntry);
			Update.updateCourseStudentGradeEntry(student, course.getCourseId(), gradeEntry.getCategoryId(),
					gradeEntry.getItemId());
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
					Double.parseDouble(h.get("Score")), Double.parseDouble(h.get("Percentage")), course.getCourseId(),
					h.get("Comments"));
			student = editGradeEntryForStudent(course, student, newEntry);
			course.setStudent(studentIndex, student);
		}

		setChanged();
		notifyObservers();
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

	public String[][] getItemDetailsForCourseCategory(Course course, int categoryIndex, boolean includeMaxPoints) {
		if (!includeMaxPoints)
			return course.getCategories().get(categoryIndex).getItemsForList();
		else
			return course.getCategories().get(categoryIndex).getItemsForListWithMaxPoints();
	}

	public String[][] getAllItemDetailsForCourse(Course course, boolean includeMaxPoints) {
		String[][] details = new String[getAllItemsForCourse(course).size()][];
		int i = 0;
		for(Item item : getAllItemsForCourse(course)){
			if (!includeMaxPoints)
				details[i++] = item.getDetailsWithCategory(course);
			else
				details[i++] = item.getDetailsWithMaxPointsWithCategory(course);
		}
		return details;
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

	public int generateCategoryId(Course course) {
		Random rand = new Random();
		int id;
		while (true) {
			id = rand.nextInt(99999) + 1;

			if (isUniqueCategoryId(course, id)) {
				return id;
			}
		}
	}

	public int generateItemId(Category category) {
		Random rand = new Random();
		int id;
		while (true) {
			id = rand.nextInt(99999) + 1;
			if (isUniqueItemId(category, id)) {
				return id;
			}
		}
	}

	public void setCourse(int index, Course newCourse) {
		this.courses.remove(index);
		this.courses.add(index, newCourse);
	}

	public ArrayList<Item> getAllItemsForCourse(Course course) {
		ArrayList<Item> allItem = new ArrayList<>();

		for (Category category : course.getCategories()) {
			for (Item i : category.getItems()) {
				allItem.add(i);
			}
		}
		return allItem;
	}

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

	public String getStudentScoreByID(CourseStudent student, int courseID, int categoryID) {
		Course currCourse = null;
		for (Course c : courses) {
			if (c.getCourseId() == courseID) {
				currCourse = c;
				break;
			}
		}
		Category category = currCourse.getCategoryById(categoryID);
		List<Item> allItem = category.getItems();
		String grade = Double.toString(student.getGradeEntryForCategory(courseID, categoryID, allItem));
		return grade + " 0";
	}

	public boolean isUniqueCategoryId(Course course, int id) {
		for (Category category : course.getCategories()) {
			if (id == category.getId())
				return false;
		}
		return true;
	}

	public boolean isUniqueItemId(Category category, int id) {
		for (Item i : category.getItems()) {
			if (id == i.getId())
				return false;
		}
		return true;
	}

	// ----------------------------------------------

	public void modifyCategoriesForCourse(Course course, ArrayList<HashMap<String, Double>> cats) {
		ArrayList<Category> categories = course.getCategories();

		for (int i = 0; i < categories.size(); i++) {
			Category c = categories.get(i);

			HashMap<String, Double> map = cats.get(i);

			if (map.get(c.getFieldName()) != c.getWeight()) {
				Category modified = new Category(c.getId(), c.getFieldName(), map.get(c.getFieldName()),
						c.getCourseId(), c.getItems());

				course.setCategory(i, modified);
				// update db for modification to category
				Update.updateCategory(modified);
			}
		}

		setChanged();
		notifyObservers();
	}

	public void modifyItemsForCourseCategory(Course course, int categoryIndex, HashMap<String, ArrayList<Double>> map) {
		Category cat = course.getCategory(categoryIndex);

		ArrayList<Item> items = cat.getItems();

		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);

			boolean flag = false;
			ArrayList<Double> l = map.get(item.getFieldName());
			if (item.getWeight() != l.get(0))
				flag = true;
			else if (item.getMaxPoints() != l.get(1))
				flag = true;

			if (flag) {
				Item modifiedItem = new Item(item.getId(), item.getFieldName(), item.getCategoryId(), l.get(0),
						l.get(1), item.getCourseId());
				cat.setItem(i, modifiedItem);
				course.setCategory(categoryIndex, cat);

				for (CourseStudent student : course.getStudents()) {
					student.modifyMaxPointsInGradeEntryByItem(item.getId(), l.get(1));
				}

				// modify in db
				Update.updateItem(modifiedItem);
			}
		}

		setChanged();
		notifyObservers();
	}

	public void modifyItemsForCourse(Course course, HashMap<String, ArrayList<Double>> map) {
		ArrayList<Category> categories = getAllCategoriesForCourse(course);
		for(int i = 0; i < categories.size(); i++){
			Category cat = categories.get(i);
			ArrayList<Item> items = getAllItemsForCourseCategory(course, i);
			for(int j = 0; j < items.size(); j++){
				Item item = items.get(j);
				boolean flag = false;
				ArrayList<Double> l = map.get(item.getFieldName());
				if (item.getWeight() != l.get(0))
					flag = true;
				else if (item.getMaxPoints() != l.get(1))
					flag = true;

				if (flag) {
					Item modifiedItem = new Item(item.getId(), item.getFieldName(), item.getCategoryId(), l.get(0),
							l.get(1), item.getCourseId());
					cat.setItem(j, modifiedItem);
					course.setCategory(i, cat);

					for (CourseStudent student : course.getStudents()) {
						student.modifyMaxPointsInGradeEntryByItem(item.getId(), l.get(1));
					}

					// modify in db
					Update.updateItem(modifiedItem);
				}
			}
		}
		setChanged();
		notifyObservers();
	}

	public void modifyStudentForCourse(Course course, HashMap<String, ArrayList<String>> map) {
		ArrayList<CourseStudent> students = course.getStudents();
		for (int i = 0; i < students.size(); i++) {
			CourseStudent student = students.get(i);
			ArrayList<String> mods = map.get(student.getName());

			String modBuid = mods.get(0).trim();
			String modName = mods.get(1).trim();
			String modType = mods.get(2).trim();

			boolean flag = false;
			if (!modBuid.equals("")) {
				student.setBuid(modBuid);
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

	public void modifyStudentForCourse(Course course, int studentIndex, HashMap<String, String> map) {

		CourseStudent student = course.getStudent(studentIndex);

		String modBuid = map.get("Buid").trim();
		String modName = map.get("Name").trim();
		String modEmail = map.get("Email").trim();
		String modType = map.get("Type").trim();

		boolean flag = false;
		if (!modBuid.equals("")) {
			student.setBuid(modBuid);
			flag = true;
		}

		if (!modEmail.equals("")) {
			student.setEmail(modEmail);
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
			course.setStudent(studentIndex, student);
			/**
			 * to do db mod
			 */
			setChanged();
			notifyObservers();
		}

	}

	public ArrayList<Integer> getStudentIndecesForComments(Course course, int categoryIndex, int itemIndex) {
		ArrayList<Integer> indeces = new ArrayList<Integer>();
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
					if (entry.getCategoryId() == category.getId() && entry.getItemId() == item.getId()) {
						if (!entry.getComments().trim().equals(""))
							indeces.add(i);
					}
				} else if (categoryIndex != -1) {
					if (entry.getCategoryId() == category.getId()) {
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

	public String getCommentsForRowIndex(Course course, int studentIndex, int categoryIndex, int itemIndex) {
		String str = "";
		CourseStudent student = course.getStudent(studentIndex);
		if (categoryIndex == -1) {
			if (itemIndex == -1) {
				// all category and all items
				for (GradeEntry entry : student.getGrades()) {
					if (!entry.getComments().trim().equals("")) {
						Category cat = course.getCategoryById(entry.getCategoryId());
						Item item = cat.getItemById(entry.getItemId());
						str += "Category: " + cat.getFieldName() + "\tItem: " + item.getFieldName() + "\nComment: "
								+ entry.getComments() + "\n";
					}
				}
			}
		} else {
			if (itemIndex == -1) {
				// specific category all items
				for (GradeEntry entry : student.getGrades()) {
					if (entry.getCategoryId() == categoryIndex+1)		// categoryId starts with 1
						if (!entry.getComments().trim().equals("")) {
							Category cat = course.getCategoryById(entry.getCategoryId());
							Item item = cat.getItemById(entry.getItemId());
							str += "Category: " + cat.getFieldName() + "\tItem: " + item.getFieldName() + "\nComment: "
									+ entry.getComments() + "\n";
						}
				}
			} else {
				// specific category specific item
				for (GradeEntry entry : student.getGrades()) {
					List<Item> items = null;
					if (entry.getCategoryId() == categoryIndex+1) {
						items = getAllItemsForCourseCategory(course, categoryIndex);
						if (entry.getItemId() == items.get(itemIndex).getId()) {
							if (!entry.getComments().trim().equals("")) {
								Category cat = course.getCategoryById(entry.getCategoryId());
								Item item = cat.getItemById(entry.getItemId());
								str += "Category: " + cat.getFieldName() + "\tItem: " + item.getFieldName()
										+ "\nComment: " + entry.getComments() + "\n";
							}
						}
					}
				}
			}
		}
		return str;
	}

	public double getTotalForItemIncludingWeight(Item item, GradeEntry entry) {
		return item.getWeight() * entry.getPercentage();
	}

	public ArrayList<GradeEntry> getEntriesByCategory(Category category, ArrayList<GradeEntry> entries) {
		ArrayList<GradeEntry> grades = new ArrayList<GradeEntry>();

		for (GradeEntry entry : entries) {
			if (entry.getCategoryId() == category.getId()) {
				grades.add(entry);
			}
		}

		return grades;
	}

	public GradeEntry getEntryByItem(Item item, ArrayList<GradeEntry> categoryEntries) {
		GradeEntry entry = null;

		for (GradeEntry e : categoryEntries) {
			if (e.getItemId() == item.getId()) {
				entry = e;
				break;
			}
		}

		return entry;
	}

	public double getTotalForCategoryIncludingWeight(Course course, int categoryId, ArrayList<GradeEntry> entries) {
		double total = 0;

		Category category = course.getCategoryById(categoryId);
		ArrayList<Item> items = category.getItems();

		ArrayList<GradeEntry> categoryEntries = getEntriesByCategory(category, entries);

		for (Item i : items) {
			GradeEntry entry = getEntryByItem(i, categoryEntries);
			total += (i.getWeight() * entry.getPercentage());
		}

		double totalIncludingCategoryWeight = total * category.getWeight();

		return totalIncludingCategoryWeight;
	}

	public Double[] getFinalPercentages(Course course) {
		Double[] percentages = new Double[course.getStudents().size()];
		ArrayList<CourseStudent> students = course.getStudents();
		ArrayList<Category> categories = course.getCategories();

		int i = 0;
		for (CourseStudent student : students) {
			double total = 0;
			ArrayList<GradeEntry> studentEntries = student.getGrades();
			for (Category category : categories) {
				total += getTotalForCategoryIncludingWeight(course, category.getId(), studentEntries);
			}

			percentages[i++] = total;
		}
		return percentages;
	}

	public String[] getFinalLetterGrades(Course course, Double[] finalPercentages) {
		String[] grades = new String[course.getStudents().size()];

		int i = 0;
		for (Double p : finalPercentages) {
			grades[i++] = helper.Statistics.getLetterGrade(p);
		}

		return grades;
	}

	public void initiateCourseFinalization(Course course) {
		if (course.isFinilizationInitialized()) {
			Double[] finalPercentages = getFinalPercentages(course);
			String[] letterGrades = getFinalLetterGrades(course, finalPercentages);

			course.initiateFinalize(finalPercentages, letterGrades);
		}
	}

	public String[][] getFinalGradesData(Course course) {
		ArrayList<FinalGrade> finalGrades = course.getFinalGrades();
		String[][] data = new String[finalGrades.size()][];

		for (int i = 0; i < data.length; i++) {
			data[i] = finalGrades.get(i).getDetailsForList();
		}

		return data;
	}

	public void setCurveForCourse(Course course, double curve) {
		course.setCurve(curve);
		course.setCurveApplied(true);
		setCurveOnCoursePercentages(course);

		setChanged();
		notifyObservers();
	}

	public void setCurveOnCoursePercentages(Course course) {
		ArrayList<FinalGrade> finalGrades = course.getFinalGrades();

		for (FinalGrade grade : finalGrades) {
			if (course.isCurveApplied()) {
				//if curve is already applied, modify from applied curve 
				grade.setCurvedPercentage(grade.getCurvedPercentage() + course.getCurve());
				grade.setLetterGrade(helper.Statistics.getLetterGrade(grade.getCurvedPercentage()));
			} else {
				grade.setCurvedPercentage(grade.getActualPercentage() + course.getCurve());
				grade.setLetterGrade(helper.Statistics.getLetterGrade(grade.getCurvedPercentage()));
			}
		}

		course.setFinalGrades(finalGrades);
		
		/**
		 * to do - DB update
		 */

		fireUpdate();
	}

	public void fireUpdate() {
		setChanged();
		notifyObservers();
	}
}
