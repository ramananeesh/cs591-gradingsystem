package model;

/**
 * Template.
 */
public class Template {

	/** Template ID. */
	private int templateId;

	/** Template name. */
	private String templateName;

	public Template(int templateId, String templateName) {
		super();
		this.templateId = templateId;
		this.templateName = templateName;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String toString() {
		return this.templateId + " - " + this.templateName;
	}

}