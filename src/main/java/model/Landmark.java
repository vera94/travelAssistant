package model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Landmark implements Comparable<Landmark>{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private String description;
	
	private String photoUrl;
	
	private float lat;
	
	private float lng;
	
	private double rating;
	
	@Transient
	private byte[] photo;
	
	@OneToMany
	private List<LandmarkType> types;

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

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] file) {
		this.photo = file;
	}

	public List<LandmarkType> getTypes() {
		return types;
	}

	public void setTypes(List<LandmarkType> types) {
		this.types = types;
	}
	
	@Override
	public int compareTo(Landmark other) {
        return rating < other.rating? 1 : rating > other.rating ? -1 : 0;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Landmark other = (Landmark) obj;
		if (Float.floatToIntBits(lat) != Float.floatToIntBits(other.lat))
			return false;
		if (Float.floatToIntBits(lng) != Float.floatToIntBits(other.lng))
			return false;
		return true;
	}
}
