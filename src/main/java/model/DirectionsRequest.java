package model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class DirectionsRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String origin;
	private String destination;
	private String travelMode;
	private int stopovers;
	private int hotelStays;
	private int maxDeviationFromPath;
	@Transient
	private String email;
	@Transient
	private List<Float[]> waypoints;
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getTravelMode() {
		return travelMode;
	}
	public void setTravelMode(String travelMode) {
		this.travelMode = travelMode;
	}
	public int getStopovers() {
		return stopovers;
	}
	public void setStopovers(int stopovers) {
		this.stopovers = stopovers;
	}
	public int getHotelStays() {
		return hotelStays;
	}
	public void setHotelStays(int hotelStays) {
		this.hotelStays = hotelStays;
	}
	public int getMaxDeviationFromPath() {
		return maxDeviationFromPath;
	}
	public void setMaxDeviationFromPath(int maxDeviationFromPath) {
		this.maxDeviationFromPath = maxDeviationFromPath;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Float[]> getWaypoints() {
		return waypoints;
	}
	public void setWaypoints(List<Float[]> waypoints) {
		this.waypoints = waypoints;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
