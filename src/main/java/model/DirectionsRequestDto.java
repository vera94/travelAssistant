package model;

public class DirectionsRequestDto {
	private String origin;
	private String destination;
	private String travelMode;
	private int stopovers;
	private int hotelStays;
	private String email;
	private int maxDeviationFromPath;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getMaxDeviationFromPath() {
		return maxDeviationFromPath;
	}
	public void setMaxDeviationFromPath(int maxDeviationFromPath) {
		this.maxDeviationFromPath = maxDeviationFromPath;
	}
}
