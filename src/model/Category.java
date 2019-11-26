package model;

import java.util.ArrayList;

public class Category extends GenericCategory {

	private ArrayList<Item> items;

	public Category(int id, String fieldName, double weight, String courseName) {
		super(id, fieldName, weight, courseName);

		this.items = new ArrayList<Item>();
	}

	public Category(int id, String fieldName, double weight, String courseName, int templateId) {
		super(id, fieldName, weight, courseName, templateId);

		this.items = new ArrayList<Item>();
	}

	public Category(int id, String fieldName, double weight, String courseName, ArrayList<Item> items) {
		super(id, fieldName, weight, courseName);

		this.items = items;
	}

	public Category(int id, String fieldName, double weight, String courseName, int templateId, ArrayList<Item> items) {
		super(id, fieldName, weight, courseName, templateId);

		this.items = items;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	

}
