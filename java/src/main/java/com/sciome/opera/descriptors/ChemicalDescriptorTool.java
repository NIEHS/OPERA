package com.sciome.opera.descriptors;

import java.util.List;

import com.sciome.opera.descriptors.cdk2.CDK2DescriptorsUtil;
import com.sciome.opera.descriptors.padel.PadelDescriptorsUtil;

/**

 */
public class ChemicalDescriptorTool
{

	public ChemicalDescriptorTool()
	{

	}

	public List<OPERAChemicalDescriptors> getDescriptors(String input, boolean calculateFingerPrints,
			boolean useCDK2) throws Exception
	{
		// first get cdk2 descriptors
		CDK2DescriptorsUtil cdk2Util = new CDK2DescriptorsUtil();

		// get the padel descriptors
		PadelDescriptorsUtil padelUtil = new PadelDescriptorsUtil(true, false, true, true, false, true, false,
				calculateFingerPrints);

		List<OPERAChemicalDescriptors> operaDescriptors = padelUtil.processData(input,
				cdk2Util.isSmiles(input));

		if (useCDK2)
			return cdk2Util.batchDescriptor(input, operaDescriptors);

		return operaDescriptors;

	}

}
