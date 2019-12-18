package model;

public class Student extends Person {

	private String studentId;
	private String email;
	private String type;

	public Student(String firstName, String lastName, String studentId, String email, String type) {
		super(firstName, lastName);

		this.studentId = studentId;
		this.email = email;
		this.type = type;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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
		return new String[]{this.getFirstName() + " " + this.getLastName(), this.getEmail()};
	}

	public String getStudentDetails() {
		String str = "";

		str += "\nName: " + this.getFirstName() + " " + this.getLastName();
		str += "\nBUID: " + this.getStudentId();
		str += "\nEmail: " + this.getEmail();
		str += "\nStudent Type: " + this.getType();
		return str;
	}

	public String getName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	public void setName(String name) {
		String[] names = name.split(" ");
		String fname = names[0];
		String lname = names[1];

		this.setFirstName(fname);
		this.setLastName(lname);
	}

}
