package model;

import java.util.ArrayList;
import java.util.List;

public enum LandmarkType {
	HISTORICAL_SIGHT("Historical sight"), CITY("City");
	private final String name;
	
	private LandmarkType(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static List<KeyValuePair<LandmarkType, String>> getTypesAsKeyValuePairs(){
		List<KeyValuePair<LandmarkType, String>> result = new ArrayList<>();
		for (LandmarkType landmarkType : values()) {
			result.add(new KeyValuePair<LandmarkType, String>(landmarkType, landmarkType.getName()));
		}
		return result;
	}
}
