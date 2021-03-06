package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private Collection<Role> grantedAuthoritiesList = new ArrayList<>();

	@ElementCollection()
	private Collection<LandmarkType> prefferedLandmarkTypes = new ArrayList<>();
	
	@ElementCollection()
	@Cascade({CascadeType.ALL})
	private Collection<DirectionsRequest> savedRequests = new ArrayList<>();
	
	public UserEntity() {
	}


	@Override
	public String toString() {
		return String.format("Customer[id=%d, firstName='%s', lastName='%s']", id, firstName, lastName);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Collection<Role> getGrantedAuthoritiesList() {
		return grantedAuthoritiesList;
	}

	public void setGrantedAuthoritiesList(Collection<Role> grantedAuthoritiesList) {
		this.grantedAuthoritiesList = grantedAuthoritiesList;
	}


	public Collection<LandmarkType> getPrefferedLandmarkTypes() {
		return prefferedLandmarkTypes;
	}


	public void setPrefferedLandmarkTypes(Collection<LandmarkType> prefferedLandmarkTypes) {
		this.prefferedLandmarkTypes = prefferedLandmarkTypes;
	}


	public Collection<String> getPrefferedLandmarkTypesAsStrings() {
		List<String> result= new ArrayList<>();
		for (LandmarkType type : prefferedLandmarkTypes) {
			result.add(type.getType());
		}
		return result;
	}


	public Collection<DirectionsRequest> getSavedRequests() {
		return savedRequests;
	}


	public void setSavedRequests(Collection<DirectionsRequest> savedRequests) {
		this.savedRequests = savedRequests;
	}
}
