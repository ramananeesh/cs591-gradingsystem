package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Course student.
 */
public class CourseStudent extends Student {

	/** Course ID. */
	private int courseId;

	/** Whether student is active or not. */
	private boolean isActive;

	/** List of grade entries. */
	private ArrayList<GradeEntry> grades;

	public CourseStudent(String firstName, String lastName, String studentId, String email,
	                     String type, int courseId, boolean isActive) {
		super(firstName, lastName, studentId, email, type);
		this.courseId = courseId;
		this.isActive = isActive;
		this.grades = new ArrayList<>();
	}

	public CourseStudent(String fname, String lname, String buid, String email, String type, int courseId,
	                     boolean isActive, ArrayList<GradeEntry> grades) {
		super(fname, lname, buid, email, type);
		this.courseId = courseId;
		this.isActive = isActive;
		this.grades = grades;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public ArrayList<GradeEntry> getGrades() {
		return grades;
	}

	public void setGrades(ArrayList<GradeEntry> grades) {
		this.grades = grades;
	}

	public HashMap<String, Double> getAllGradeEntries() {
		HashMap<String, Double> map = new HashMap<String, Double>();
		for (GradeEntry entry : this.getGrades()) {
			map.put(entry.getGradeEntryName(), entry.getPointsEarned());
		}

		return map;
	}

	public void setGradeEntry(int index, GradeEntry gradeEntry) {
		GradeEntry existing = this.grades.get(index);
		this.grades.remove(index);
		this.grades.add(index, gradeEntry);
	}

	public void addGradeEntry(GradeEntry entry) {
		this.grades.add(entry);
	}

	public GradeEntry getGradeEntryForItemInCategory(int courseId, int categoryId, int itemId) {
		for (GradeEntry entry : this.grades) {
			if (entry.getCourseId() == courseId && entry.getCategoryId() == categoryId && entry.getItemId() == itemId) {
				return entry;
			}
		}
		return null;
	}

	public double getGradeEntryForCategory(int courseId, int categoryId, List<Item> allItems) {
		double score = 0;
		for (GradeEntry entry : this.grades) {
			if (entry.getCourseId() == courseId && entry.getCategoryId() == categoryId) {
				int itemId = entry.getItemId();
				for (Item i : allItems) {
					if (itemId == i.getItemId()) {
						double percentage = i.getWeight();
						score += percentage * (entry.getPointsEarned() / entry.getMaxPoints());
						break;
					}
				}
			}
		}
		return score;
	}


	public void modifyMaxPointsInGradeEntryByItem(int itemId, double maxPoints) {
		for (int i = 0; i < this.grades.size(); i++) {
			GradeEntry entry = this.grades.get(i);
			if (entry.getItemId() == itemId) {
				entry.setMaxPoints(maxPoints);
				this.setGradeEntry(i, entry);
			}
		}
	}
}
