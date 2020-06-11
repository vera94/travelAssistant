package model;

import java.util.List;

public class UserEntityDto {
	private String email;
	private String firstName;
	private String lastName;
	private String password;
//	private List<LandmarkTyp> prefferedLandmarkTypes;
	
	public UserEntityDto(){
		super();
	}
	
	public UserEntityDto(UserEntity user){
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.password = "";
//		this.setPrefferedLandmarkTypes((List<LandmarkTyp>) user.getPrefferedLandmarkTypes());
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

//	public List<LandmarkTyp> getPrefferedLandmarkTypes() {
//		return prefferedLandmarkTypes;
//	}
//
//	public void setPrefferedLandmarkTypes(List<LandmarkTyp> prefferedLandmarkTypes) {
//		this.prefferedLandmarkTypes = prefferedLandmarkTypes;
//	}
}
