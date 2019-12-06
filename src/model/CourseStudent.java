package model;

import java.util.*;

public class CourseStudent extends Student {

	private int courseId;
	private boolean active;
	private ArrayList<GradeEntry> grades; 

	public CourseStudent(String fname, String lname, String buid, String email, String type, int courseId,
			boolean active) {
		super(fname, lname, buid, email, type);
		this.courseId = courseId;
		this.active = active;
		this.grades = new ArrayList<GradeEntry>();
	}
	
	public CourseStudent(String fname, String lname, String buid, String email, String type, int courseId,
			boolean active, ArrayList<GradeEntry> grades) {
		super(fname, lname, buid, email, type);
		this.courseId = courseId;
		this.active = active;
		this.grades = grades;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ArrayList<GradeEntry> getGrades() {
		return grades;
	}

	public void setGrades(ArrayList<GradeEntry> grades) {
		this.grades = grades;
	}

	public HashMap<String, Double>getAllGradeEntries(){
		HashMap<String, Double> map = new HashMap<String, Double>();
		for(GradeEntry entry: this.getGrades()){
			map.put(entry.getEntryName(),entry.getPointsEarned());
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
		for(GradeEntry entry: this.grades) {
			if(entry.getCourseId()==courseId&&entry.getCategoryId()==categoryId && entry.getItemId()==itemId) {
				return entry;
			}
		}
		return null; 
	}

	public double getGradeEntryForCategory(int courseId, int categoryId, List<Item> allItems){
		double score = 0;
		for(GradeEntry entry: this.grades) {
			if(entry.getCourseId()==courseId&&entry.getCategoryId()==categoryId) {
				int itemId = entry.getItemId();
				for(Item i : allItems){
					if(itemId == i.getId()){
						double percentage = i.getWeight();
						score += percentage*(entry.getPointsEarned()/entry.getMaxPoints());
						break;
					}
				}
			}
		}
		return score;
	}
}

