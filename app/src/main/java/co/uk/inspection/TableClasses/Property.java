package co.uk.inspection.TableClasses;

public class Property {
	
	int id;
	String name;
	String address;
	String email;
	String postCode;
	
	
	
	public Property(int id, String name, String address, String email,
			String postCode) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.email = email;
		this.postCode = postCode;
	}
	
	
	
	public Property() {
		// TODO Auto-generated constructor stub
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	

}
