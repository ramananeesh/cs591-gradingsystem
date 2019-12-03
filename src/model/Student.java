package model;

public class Student extends Person {

	private String buid;
	private String email;
	private String type;

	public Student(String fname, String lname, String buid, String email, String type) {
		super(fname, lname);

		this.buid = buid;
		this.email = email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String[] getStudentDataForTable() {
		return new String[]{this.getFname()+" "+this.getLname(), this.getBuid()};
	}
	
	public String getStudentDetails() {
		String str="";
		
		str+="\nName: "+this.getFname()+" "+this.getLname();
		str+="\nBUID: "+this.getBuid();
		str+="\nEmail: "+this.getEmail();
		str+="\nStudent Type: "+this.getType();
		return str; 
	}

}
