package com.sciome.opera.descriptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OPERAChemicalDescriptors
{

	String						chemID;

	List<OPERADescriptorValue>	padelDescriptorValues;

	Map<String, String>			padelDescriptorValuesMap;

	List<OPERADescriptorValue>	cdk2DescriptorValues;

	Map<String, String>			cdk2DescriptorValuesMap;

	private String				cdk2Exceptions;
	private String				padelExceptions;

	public OPERAChemicalDescriptors(String cid)
	{
		chemID = cid;
		padelDescriptorValues = new ArrayList<>();
		padelDescriptorValuesMap = new HashMap<>();
		cdk2DescriptorValues = new ArrayList<>();
		cdk2DescriptorValuesMap = new HashMap<>();
	}

	public void addPadel(OPERADescriptorValue dv)
	{
		padelDescriptorValues.add(dv);
		padelDescriptorValuesMap.put(dv.getLabel().toLowerCase(), dv.getValue());
	}

	public void addCDK2(OPERADescriptorValue dv)
	{
		cdk2DescriptorValues.add(dv);
		cdk2DescriptorValuesMap.put(dv.getLabel().toLowerCase(), dv.getValue());
	}

	public void clear()
	{
		padelDescriptorValues.clear();
		padelDescriptorValuesMap.clear();
		cdk2DescriptorValues.clear();
		cdk2DescriptorValuesMap.clear();
	}

	public void removePadel(OPERADescriptorValue dv)
	{
		padelDescriptorValues.remove(dv);
		padelDescriptorValuesMap.remove(dv.getLabel().toLowerCase());
	}

	public void removeCDK2(OPERADescriptorValue dv)
	{
		cdk2DescriptorValues.remove(dv);
		cdk2DescriptorValuesMap.remove(dv.getLabel().toLowerCase());
	}

	/*
	 * These get methods should take care of the various situations required.
	 * 
	 * some models may only be interested in padel descriptors some models may use cdk2 over padel some may
	 * only use cdk2. some may use padel over cdk2, but take cdk2 if not in padel
	 */
	public String getPadelValue(String label)
	{
		return padelDescriptorValuesMap.get(label.toLowerCase());
	}

	public String getCDK2Value(String label)
	{
		return cdk2DescriptorValuesMap.get(label.toLowerCase());
	}

	public String getValueWithCDK2Preference(String label)
	{
		if (cdk2DescriptorValuesMap.containsKey(label.toLowerCase()))
			return cdk2DescriptorValuesMap.get(label.toLowerCase());
		else
			return padelDescriptorValuesMap.get(label.toLowerCase());
	}

	public String getValueWithPadelPreference(String label)
	{
		if (padelDescriptorValuesMap.containsKey(label.toLowerCase()))
			return padelDescriptorValuesMap.get(label.toLowerCase());
		else
			return cdk2DescriptorValuesMap.get(label.toLowerCase());
	}

	public String getChemID()
	{
		return chemID;
	}

	public void setChemID(String chemID)
	{
		this.chemID = chemID;
	}

	public List<OPERADescriptorValue> getPadelDescriptorValues()
	{
		return padelDescriptorValues;
	}

	public List<OPERADescriptorValue> getCDKDescriptorValues()
	{
		return cdk2DescriptorValues;
	}

	public void setCDK2Exceptions(String exceptions)
	{
		this.cdk2Exceptions = exceptions;

	}

	public String getCDK2Exceptions()
	{
		return cdk2Exceptions;

	}

	public void setPADELExceptions(String exceptions)
	{
		this.padelExceptions = exceptions;

	}

	public String getPADELExceptions()
	{
		return padelExceptions;

	}

}
