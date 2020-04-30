package com.sciome.opera.enums;

public enum PretreatmentType
{
	cent("cent"),
	scal("scal"),
	auto("auto"),
	rang("rang"),
	fp("fp");
	
	private final String name;
	
	private PretreatmentType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static PretreatmentType findByName(String pretreatmentString) {
		for(PretreatmentType filter : PretreatmentType.values()) {
			if(filter.name.equals(pretreatmentString)) {
				return filter;
			}
		}
		
		return null;
	}
}
