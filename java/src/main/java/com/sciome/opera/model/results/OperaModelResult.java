package com.sciome.opera.model.results;

import com.sciome.opera.descriptors.OPERAChemicalDescriptors;

public abstract class OperaModelResult
{
	OPERAChemicalDescriptors	chemicalDescriptors;

	public OPERAChemicalDescriptors getChemicalDescriptors() {
		return chemicalDescriptors;
	}

	public void setChemicalDescriptors(OPERAChemicalDescriptors chemicalDescriptors) {
		this.chemicalDescriptors = chemicalDescriptors;
	}
}