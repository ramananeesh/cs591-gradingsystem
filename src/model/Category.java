package model;

import java.util.ArrayList;

public class Category extends GenericCategory {

	private ArrayList<Item> items;

	public Category(int id, String fieldName, double weight, int courseId) {
		super(id, fieldName, weight, courseId);

		this.items = new ArrayList<Item>();
	}

	public Category(int id, String fieldName, double weight, int courseId, int templateId) {
		super(id, fieldName, weight, courseId, templateId);

		this.items = new ArrayList<Item>();
	}

	public Category(int id, String fieldName, double weight, int courseId, ArrayList<Item> items) {
		super(id, fieldName, weight, courseId);

		this.items = items;
	}

	public Category(int id, String fieldName, double weight, int courseId, int templateId, ArrayList<Item> items) {
		super(id, fieldName, weight, courseId, templateId);

		this.items = items;
	}

	/*
	 * Specific methods
	 */
	// -------------------------------------------------------------------------------------------------------------------//

	public void addItem(Item newItem) {
		this.items.add(newItem);
	}

	public Item removeItem(int itemIndex) {
		Item removedItem = this.items.get(itemIndex);
		this.items.remove(itemIndex);

		return removedItem;
	}

	/*
	 * accessors and mutators
	 */
	// -------------------------------------------------------------------------------------------------------------------//
	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public Item getItem(int itemIndex) {
		return this.items.get(itemIndex);
	}

	public void setItem(int itemIndex, Item newItem) {
		// remove existing item at index
		this.removeItem(itemIndex);

		// add newItem at same index
		// this will shift any item at that index
		this.items.add(itemIndex, newItem);
	}
	
	public String[][] getItemsForList(){
		String [][]str = new String[this.items.size()][];
		
		int i=0;
		for(Item item: this.items) {
			str[i++]=item.getDetails();
		}
		
		return str;
	}

}
