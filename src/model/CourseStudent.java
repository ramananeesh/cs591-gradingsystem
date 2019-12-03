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
}
