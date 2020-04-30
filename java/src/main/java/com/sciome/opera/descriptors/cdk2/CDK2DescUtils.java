package com.sciome.opera.descriptors.cdk2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.Aromaticity;
import org.openscience.cdk.aromaticity.ElectronDonation;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.graph.Cycles;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.io.ReaderFactory;
import org.openscience.cdk.io.formats.IResourceFormat;
import org.openscience.cdk.qsar.IDescriptor;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.AtomTypeAwareSaturationChecker;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

/**
 * @author Rajarshi Guha
 */

public class CDK2DescUtils
{

	CDK2DescUtils()
	{
	}

	/**
	 * Checks whether the input file is in SMI format.
	 * <p/>
	 * The approach I take is to read the first line of the file. This should splittable to give two parts.
	 * The first part should be a valid SMILES string. If so this method returns true, otherwise false
	 *
	 * @param filename
	 *            The file to consider
	 * @return true if the file is in SMI format, otherwise false
	 */
	public static boolean isSMILESFormat(InputStream inputStream)
	{
		String line1 = null;
		String line2 = null;
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			line1 = in.readLine();
			line2 = in.readLine();
			in.close();
		}
		catch (FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}

		assert line1 != null;

		String[] tokens = line1.split("\\s+");
		if (tokens.length == 0)
			return false;

		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		try
		{
			IAtomContainer m = sp.parseSmiles(tokens[0].trim());
		}
		catch (InvalidSmilesException ise)
		{
			return false;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return false;
		}

		// now check the second line
		// if there is no second line this probably a smiles
		// file
		if (line2 == null)
			return true;

		// o0k we have a second line, so lets see if it's a smiles
		tokens = line2.split("\\s+");
		if (tokens.length == 0)
			return false;

		sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		try
		{
			IAtomContainer m = sp.parseSmiles(tokens[0].trim());
		}
		catch (InvalidSmilesException ise)
		{
			return false;
		}

		return true;
	}

	public static boolean isMDLFormat(InputStream inputStream)
	{
		boolean flag;
		try
		{
			ReaderFactory factory = new ReaderFactory();
			IChemObjectReader reader = factory.createReader(inputStream);
			if (reader == null)
				return false;
			IResourceFormat format = reader.getFormat();
			flag = format.getFormatName().startsWith("MDL ");
		}
		catch (Exception exception)
		{
			return false;
		}
		return flag;
	}

	public static boolean isMacOs()
	{
		String lcOSName = System.getProperty("os.name").toLowerCase();
		return lcOSName.startsWith("mac os x");
	}

	public static Comparator getDescriptorComparator()
	{
		return new Comparator() {
			public int compare(Object o1, Object o2)
			{
				IDescriptor desc1 = (IDescriptor) o1;
				IDescriptor desc2 = (IDescriptor) o2;

				String[] comp1 = desc1.getSpecification().getSpecificationReference().split("#");
				String[] comp2 = desc2.getSpecification().getSpecificationReference().split("#");

				return comp1[1].compareTo(comp2[1]);
			}
		};
	}

	public static IAtomContainer checkAndCleanMolecule(IAtomContainer molecule) throws CDKException
	{
		boolean isMarkush = false;
		for (IAtom atom : molecule.atoms())
		{
			if (atom.getSymbol().equals("R"))
			{
				isMarkush = true;
				break;
			}
		}

		if (isMarkush)
		{
			throw new CDKException("Skipping Markush structure");
		}

		// Check for salts and such
		String title = molecule.getProperty(CDKConstants.TITLE);
		if (!ConnectivityChecker.isConnected(molecule))
		{
			// lets see if we have >1 parts if so, we assume its a salt and just work
			// on the biggest part. Ideally we should have a check to ensure that the smaller
			// part is a metal/halogen etc.
			IAtomContainerSet fragments = ConnectivityChecker.partitionIntoMolecules(molecule);
			int maxFragSize = -1;
			int maxFragPos = -1;
			for (int i = 0; i < fragments.getAtomContainerCount(); i++)
			{
				if (fragments.getAtomContainer(i).getAtomCount() > maxFragSize)
				{
					maxFragSize = fragments.getAtomContainer(i).getAtomCount();
					maxFragPos = i;
				}
			}
			molecule = fragments.getAtomContainer(maxFragPos);
			molecule.setProperty(CDKConstants.TITLE, title);
		}

		// Do the configuration
		try
		{
			AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
		}
		catch (CDKException e)
		{
			throw new CDKException("Error in atom typing" + e.toString());
		}

		CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
		adder.addImplicitHydrogens(molecule);
		AtomTypeAwareSaturationChecker sat = new AtomTypeAwareSaturationChecker();
		sat.decideBondOrder(molecule);

		// do a aromaticity check
		try
		{
			Aromaticity aromaticity = new Aromaticity(ElectronDonation.daylight(), Cycles.vertexShort());
			aromaticity.apply(molecule);
		}
		catch (CDKException e)
		{
			throw new CDKException("Error in aromaticity detection");
		}

		return molecule;
	}

	public static Map<String, Boolean> loadDescriptorSelections(String fileName)
			throws ParsingException, IOException
	{
		Map<String, Boolean> selDecMap = new HashMap<String, Boolean>();
		Builder parser = new Builder();
		Document doc;
		doc = parser.build(fileName);
		Element root = doc.getRootElement();
		Elements elems = root.getChildElements("descriptorList");
		Elements descriptorList = elems.get(0).getChildElements();
		for (int i = 0; i < descriptorList.size(); i++)
		{
			Element desc = descriptorList.get(i);
			String spec = desc.getAttribute("spec").getValue();
			String value = desc.getFirstChildElement("selected").getValue();
			Boolean isSelected = value.equals("true");
			selDecMap.put(spec, isSelected);
		}
		return selDecMap;
	}
}