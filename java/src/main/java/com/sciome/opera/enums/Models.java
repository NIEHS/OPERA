package com.sciome.opera.enums;

public enum Models
{
	AOH("AOH"),
	BCF("BCF"),
	BP("BP"),
	BIODEG("BioDeg"),
	CATMOS("CATMoS"),
	CERAPP("CERAPP"), 
	COMPARA("CoMPARA"),
	HL("HL"),
	KM("KM"),
	KOA("KOA"),
	KOC("LogKoc"),
	LOGP("LogP"), 
	LOGD("LogD"),
	MP("MP"), 
	RBIODEG("ReadyBiodeg"),
	RT("RT"),
	VP("VP"),
	WS("WS"),
	PKA("pKa");
	
	private String name;
	
	private Models(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}