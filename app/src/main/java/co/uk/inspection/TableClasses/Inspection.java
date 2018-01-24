package co.uk.inspection.TableClasses;

public class Inspection {
	
	private String name;
	private String type;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Inspection(){
		
	}
	
	
	public Inspection(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Inspection [getName()=" + getName() + ", getType()="
				+ getType() + "]";
	}
	

}
