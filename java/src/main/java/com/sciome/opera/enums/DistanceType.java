package com.sciome.opera.enums;

public enum DistanceType {
	euclidean("euclidean"), 
	mahalanobis("mahalanobis"), 
	cityblock("city block"),
	minkowski("minkowski"), 
	sm("sm"),
	rt("rt"),
	jt("jt"),
	gle("gle"),
	ct4("ct4"),
	ac("ac");
	
	private final String name;
	
	private DistanceType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static DistanceType findByName(String distanceString) {
		for(DistanceType filter : DistanceType.values()) {
			if(filter.name.equals(distanceString)) {
				return filter;
			}
		}
		
		return null;
	}
}
