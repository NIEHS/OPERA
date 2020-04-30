package com.sciome.opera.descriptors.cdk2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.DefaultIteratingChemObjectReader;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.io.iterator.IteratingSMILESReader;
import org.openscience.cdk.qsar.DescriptorEngine;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IDescriptor;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.qsar.result.IntegerArrayResult;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

import com.sciome.opera.descriptors.OPERAChemicalDescriptors;
import com.sciome.opera.descriptors.OPERADescriptorValue;
import com.sciome.opera.descriptors.padel.ExceptionInfo;

public class CDK2DescriptorsUtil
{

	List<IDescriptor> instances;

	public CDK2DescriptorsUtil() throws IOException
	{

		// get the cdk2 descriptors
		DescriptorEngine engine = new DescriptorEngine(IMolecularDescriptor.class,
				SilentChemObjectBuilder.getInstance());
		List<String> classNames = engine.getDescriptorClassNames();

		List<String> validClassNames = new ArrayList<String>();
		validClassNames.addAll(classNames);

		instances = engine.instantiateDescriptors(validClassNames);
		engine.setDescriptorInstances(instances);

	}

	public List<OPERAChemicalDescriptors> batchDescriptor(String input,
			List<OPERAChemicalDescriptors> descriptorsToReturn) throws Exception
	{

		Map<String, Boolean> sels = null;

		DefaultIteratingChemObjectReader iterReader = null;

		// ok, we've got the desc engine set up, lets check inputs and start the fun
		String inputFormat = "invalid";
		if (CDK2DescUtils.isMDLFormat(new ByteArrayInputStream(input.getBytes())))
			inputFormat = "mdl";
		else if (CDK2DescUtils.isSMILESFormat(new ByteArrayInputStream(input.getBytes())))
			inputFormat = "smi";

		if (inputFormat.equals("smi"))
			iterReader = new IteratingSMILESReader(new ByteArrayInputStream(input.getBytes()),
					SilentChemObjectBuilder.getInstance());
		else if (inputFormat.equals("mdl"))
		{
			iterReader = new IteratingSDFReader(new ByteArrayInputStream(input.getBytes()),
					DefaultChemObjectBuilder.getInstance());
			// iterReader.addChemObjectIOListener(new SDFormatListener());
			((IteratingSDFReader) iterReader).customizeJob();
		}

		// ITextOutput textOutput = new PlainTextOutput(tmpWriter);
		// textOutput.setItemSeparator("\t");

		// lets get the header line first
		List<String> headerItems = new ArrayList<String>();
		headerItems.add("Title");
		for (IDescriptor descriptor : instances)
		{
			String[] names = descriptor.getDescriptorNames();
			headerItems.addAll(Arrays.asList(names));
		}

		// assert textOutput != null;
		// textOutput.writeHeader(headerItems.toArray(new String[] {}));

		double elapsedTime = System.currentTimeMillis();

		List<ExceptionInfo> exceptionList = new ArrayList<ExceptionInfo>();
		int nmol = 0;

		while (iterReader.hasNext())
		{ // loop over molecules
			IAtomContainer molecule = (IAtomContainer) iterReader.next();
			String title = (String) molecule.getProperty(CDKConstants.TITLE);
			if (title == null || title.trim().isEmpty())
				title = "Mol" + String.valueOf(nmol + 1);

			try
			{
				molecule = (IAtomContainer) CDK2DescUtils.checkAndCleanMolecule(molecule);
			}
			catch (CDKException e)
			{
				exceptionList.add(new ExceptionInfo(nmol + 1, molecule, e, ""));
				nmol++;
				continue;
			}

			// get corresponding opera chemical with descriptors. assume in same order because first this goes
			// through padel
			// chem id/title must match
			OPERAChemicalDescriptors descriptorToReturn = descriptorsToReturn.get(nmol);
			if (!descriptorToReturn.getChemID().equals(title))
				throw new Exception("Chemicals from padel do not match cdk2");

			// OK, we can now eval the descriptors
			List<String> dataItems = new ArrayList<String>();
			dataItems.add(title);

			List<String> header = new ArrayList<>();
			int ndesc = 0;
			String exceptions = "";
			for (Object object : instances)
			{

				IMolecularDescriptor descriptor = (IMolecularDescriptor) object;

				String[] comps = descriptor.getSpecification().getSpecificationReference().split("#");

				DescriptorValue value = descriptor.calculate(molecule);
				header.addAll(Arrays.asList(descriptor.getDescriptorNames()));
				if (value.getException() != null)
				{
					exceptionList.add(new ExceptionInfo(nmol + 1, molecule, value.getException(), comps[1]));
					exceptions += value.getException() + "\n";
					for (int i = 0; i < value.getNames().length; i++)
						dataItems.add("NA");
					continue;
				}

				IDescriptorResult result = value.getValue();
				if (result instanceof DoubleResult)
				{
					dataItems.add(String.valueOf(((DoubleResult) result).doubleValue()));
				}
				else if (result instanceof IntegerResult)
				{
					dataItems.add(String.valueOf(((IntegerResult) result).intValue()));
				}
				else if (result instanceof DoubleArrayResult)
				{
					for (int i = 0; i < ((DoubleArrayResult) result).length(); i++)
					{
						dataItems.add(String.valueOf(((DoubleArrayResult) result).get(i)));
					}
				}
				else if (result instanceof IntegerArrayResult)
				{
					for (int i = 0; i < ((IntegerArrayResult) result).length(); i++)
					{
						dataItems.add(String.valueOf(((IntegerArrayResult) result).get(i)));
					}
				}

				ndesc++;

			}

			dataItems.remove(0);
			for (int i = 1; i < dataItems.size(); i++)
			{
				if (dataItems.get(i).equals("NaN"))
					dataItems.set(i, "NA");
			}

			for (int i = 0; i < header.size(); i++)
				descriptorToReturn.addCDK2(new OPERADescriptorValue(header.get(i), dataItems.get(i)));

			descriptorToReturn.setCDK2Exceptions(exceptions);
			nmol++;
		}

		// calculation is done, lets eval the elapsed time
		elapsedTime = ((System.currentTimeMillis() - elapsedTime) / 1000.0);

		try
		{
			iterReader.close();
		}
		catch (IOException e)
		{
			System.exit(-1);
		}

		if (exceptionList.size() > 0)
		{
			String exceptions = "";
			exceptions += "=============== Exceptions ===============\n";
			for (ExceptionInfo ei : exceptionList)
			{
				exceptions += ei.getMolecule().getProperty(CDKConstants.TITLE) + " " + ei.getDescriptorName()
						+ " " + ei.getException() + "\n";
			}
		}

		return descriptorsToReturn;
	}

	public boolean isSmiles(String input)
	{
		boolean isSmiles = false;
		if (CDK2DescUtils.isSMILESFormat(new ByteArrayInputStream(input.getBytes())))
			isSmiles = true;
		return isSmiles;
	}

}
