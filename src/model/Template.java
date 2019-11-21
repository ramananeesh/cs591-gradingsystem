package model;

public class Template {

	private int id;
	private String templateName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Template(int id, String templateName) {
		super();
		this.id = id;
		this.templateName = templateName;
	}

	public String toString() {
		return this.id + " - " + this.templateName;
	}
}
