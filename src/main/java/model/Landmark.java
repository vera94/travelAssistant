package model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Landmark {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private String description;
	
	private String photoUrl;
	
	private float lat;
	
	private float lng;
	
	@Transient
	private byte[] photo;
	
	@Transient
	private String landmarkTypeName;
	
	@Enumerated(EnumType.STRING)
	private LandmarkType type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public LandmarkType getType() {
		return type;
	}

	public void setType(LandmarkType type) {
		this.type = type;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] file) {
		this.photo = file;
	}

	public String getLandmarkTypeName() {
		return landmarkTypeName;
	}

	public void setLandmarkTypeName(String landmarkTypeName) {
		this.landmarkTypeName = landmarkTypeName;
	}
	
}
