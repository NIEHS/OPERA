package com.sciome.opera.descriptors.padel;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openscience.cdk1.interfaces.IAtomContainer;

import com.sciome.opera.descriptors.OPERAChemicalDescriptors;
import com.sciome.opera.descriptors.OPERADescriptorValue;

import libpadeldescriptor.CDK_Descriptor;
import libpadeldescriptor.CDK_Fingerprint;
import libpadeldescriptor.CDK_FingerprintCount;
import libpadeldescriptor.PaDELStandardize;
import libpadeldescriptor.libPaDELDescriptorType;

/**
 * Calculate CDK descriptors using different threads.
 * 
 * @author yapchunwei
 */
public class PadelDescriptorsUtil
{

	protected List<CDK_Descriptor>		cdk_descriptors;

	private boolean						compute2D				= false;	// Calculate 1D,
																			// 2D descriptors.
	private boolean						compute3D				= false;	// Calculate 3D
																			// descriptors.
	private boolean						removeSalt				= true;		// If true, remove
																			// salts like Na,
																			// Cl from the
																			// molecule before
																			// calculation of
																			// descriptors.
	private boolean						detectAromaticity		= true;		// If true, remove
																			// existing
																			// aromaticity
																			// information and
																			// automatically
																			// detect
																			// aromaticity in
																			// the molecule
																			// before
																			// calculation of
																			// descriptors.
	private boolean						standardizeTautomers	= true;		// If true,
																			// standardize
																			// tautomers.
	private boolean						standardizeNitro		= true;		// If true,
																			// standardize
																			// nitro groups to
																			// N(:O):O.
	private boolean						retain3D				= false;	// If true, retain
																			// 3D coordinates
																			// when
																			// standardizing
																			// structure.
																			// However, this
																			// may prevent
																			// some structures
																			// from being
																			// standardized.

	private String[]					tautomerList;						// SMIRKS list to
																			// convert
																			// tautomers.
	private boolean						calculateFingerPrints	= false;

	// Variables containing progress of descriptor calculation

	private List<String>				descriptorNames;
	private Set<String>					descriptorTypes;

	private IAtomContainer				molStructure;

	private List<CDK_Fingerprint>		cdk_fingerprints;

	private List<CDK_FingerprintCount>	cdk_fingerprints_count;

	public PadelDescriptorsUtil(boolean compute2D, boolean compute3D, boolean removeSalt,
			boolean detectAromaticity, boolean standardizeTautomers, boolean standardizeNitro,
			boolean retain3D, boolean calculateFingerPrints)
	{
		this.compute2D = compute2D;
		this.compute3D = compute3D;
		this.removeSalt = removeSalt;
		this.detectAromaticity = detectAromaticity;
		this.standardizeTautomers = standardizeTautomers;
		this.standardizeNitro = standardizeNitro;
		this.retain3D = retain3D;
		this.calculateFingerPrints = calculateFingerPrints;
		descriptorNames = new ArrayList<String>();

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("operadescriptors.xml");

		try
		{
			this.SetDescriptorTypes(is);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void SetDescriptorTypes(InputStream descriptorFile) throws Exception
	{
		cdk_descriptors = new ArrayList<CDK_Descriptor>();

		if (calculateFingerPrints)
		{
			cdk_fingerprints = new ArrayList<CDK_Fingerprint>();
			cdk_fingerprints_count = new ArrayList<CDK_FingerprintCount>();
		}
		descriptorTypes = libPaDELDescriptorType.GetActiveDescriptorTypes(descriptorFile, compute2D,
				compute3D, calculateFingerPrints);
		libPaDELDescriptorType.SetDescriptorTypes(descriptorTypes, descriptorNames, cdk_descriptors,
				cdk_fingerprints, cdk_fingerprints_count);

	}

	public List<OPERAChemicalDescriptors> processData(String moleculeFileContents, boolean isSmiles)
			throws FileNotFoundException
	{
		return processData(new ByteArrayInputStream(moleculeFileContents.getBytes()), isSmiles);
	}

	public List<OPERAChemicalDescriptors> processData(InputStream moleculeStream, boolean isSmiles)
	{

		List<OPERAChemicalDescriptors> chemDescriptors = new ArrayList<>();
		// Variables for each iteration
		CustomIteratingPadelReader reader = null;
		try
		{
			reader = new CustomIteratingPadelReader(moleculeStream, isSmiles);

			int nmol = 1;
			while (reader.hasNext())
			{
				// Still have another molecule to read in file.

				molStructure = (IAtomContainer) reader.next();

				// Get molecule name
				StringBuffer name = new StringBuffer();
				name.setLength(0);

				if (molStructure.getProperty("cdk:Title") != null
						&& !((String) molStructure.getProperty("cdk:Title")).trim().isEmpty())
				{
					name.append(molStructure.getProperty("cdk:Title"));
				}
				else
				{
					name.append("Mol" + String.valueOf(nmol++));
				}
				chemDescriptors.add(this.calculateDescriptorsForMolecule(molStructure, name.toString()));
			}

			try
			{
				reader.close();
				reader = null;
			}
			catch (IOException ex)
			{
				Logger.getLogger("global").log(Level.FINE, "Cannot close IteratingPaDELReader", ex);
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return chemDescriptors;

	}

	public List<String> getDescriptorNames()
	{
		return descriptorNames;
	}

	private OPERAChemicalDescriptors calculateDescriptorsForMolecule(IAtomContainer molecule, String name)
	{

		// Standardize molecule.
		PaDELStandardize standardize = new PaDELStandardize();
		standardize.setRemoveSalt(this.removeSalt);
		standardize.setDearomatize(this.detectAromaticity);
		standardize.setStandardizeTautomers(this.standardizeTautomers);
		standardize.setTautomerList(this.tautomerList);
		standardize.setStandardizeNitro(this.standardizeNitro);
		standardize.setRetain3D(this.retain3D);

		List<String> headers = new ArrayList<>();
		List<String> headers1 = new ArrayList<>();
		List<String> headers2 = new ArrayList<>();

		try
		{

			molecule = standardize.Standardize(molecule);
		}
		catch (Exception ex)
		{
			Logger.getLogger("global").log(Level.FINE, null, ex);
		}

		ArrayList<String> descriptors = new ArrayList<String>();
		ArrayList<String> descriptors1 = new ArrayList<String>();
		ArrayList<String> descriptors2 = new ArrayList<String>();

		String exceptions = "";
		for (CDK_Descriptor cdk_descriptor : cdk_descriptors)
		{
			try
			{
				cdk_descriptor.setMolecule((IAtomContainer) molecule.clone());
				cdk_descriptor.run();
			}
			catch (Exception ex)
			{
				Logger.getLogger("global").log(Level.FINE, null, ex);
				exceptions += ex + "\n";
			}
			descriptors.addAll(Arrays.asList(cdk_descriptor.getDescriptorValues()));
			headers.addAll(Arrays.asList(cdk_descriptor.getDescriptorNames()));
		}

		if (cdk_fingerprints != null)
			for (CDK_Fingerprint cdk_fingerprint : cdk_fingerprints)
			{
				try
				{
					cdk_fingerprint.setMolecule((IAtomContainer) molecule.clone());
					cdk_fingerprint.run();
				}
				catch (Exception ex)
				{
					Logger.getLogger("global").log(Level.FINE, null, ex);
					exceptions += ex + "\n";
				}
				descriptors1.addAll(Arrays.asList(cdk_fingerprint.getDescriptorValues()));
				headers1.addAll(Arrays.asList(cdk_fingerprint.getDescriptorNames()));
			}

		if (cdk_fingerprints_count != null)
			for (CDK_FingerprintCount cdk_fingerprint_count : cdk_fingerprints_count)
			{
				try
				{
					cdk_fingerprint_count.setMolecule((IAtomContainer) molecule.clone());
					cdk_fingerprint_count.run();
				}
				catch (Exception ex)
				{
					Logger.getLogger("global").log(Level.FINE, null, ex);
					exceptions += ex + "\n";
				}
				descriptors2.addAll(Arrays.asList(cdk_fingerprint_count.getDescriptorValues()));
				headers2.addAll(Arrays.asList(cdk_fingerprint_count.getDescriptorNames()));
			}

		OPERAChemicalDescriptors descriptorToReturn = new OPERAChemicalDescriptors(name);
		descriptorToReturn.setPADELExceptions(exceptions);
		for (int i = 0; i < headers.size(); i++)
			descriptorToReturn.addPadel(new OPERADescriptorValue(headers.get(i), descriptors.get(i)));
		for (int i = 0; i < headers1.size(); i++)
			descriptorToReturn.addPadel(new OPERADescriptorValue(headers1.get(i), descriptors1.get(i)));
		for (int i = 0; i < headers2.size(); i++)
			descriptorToReturn.addPadel(new OPERADescriptorValue(headers2.get(i), descriptors2.get(i)));

		return descriptorToReturn;

	}

}
