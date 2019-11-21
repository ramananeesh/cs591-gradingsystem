package model;

public class Student extends Person{

	private String buid; 
	private String type; 
	
	public Student(String fname, String lname, String buid, String type) {
		super(fname, lname);
		
		this.buid = buid; 
		this.type = type; 
	}

	public String getBuid() {
		return buid;
	}

	public void setBuid(String buid) {
		this.buid = buid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	 
}
