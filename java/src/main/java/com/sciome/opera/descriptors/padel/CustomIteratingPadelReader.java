package com.sciome.opera.descriptors.padel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openscience.cdk1.ChemFile;
import org.openscience.cdk1.DefaultChemObjectBuilder;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IChemFile;
import org.openscience.cdk1.interfaces.IChemObject;
import org.openscience.cdk1.interfaces.IChemObjectBuilder;
import org.openscience.cdk1.interfaces.IChemSequence;
import org.openscience.cdk1.interfaces.IMoleculeSet;
import org.openscience.cdk1.io.ISimpleChemObjectReader;
import org.openscience.cdk1.io.ReaderFactory;
import org.openscience.cdk1.io.formats.HINFormat;
import org.openscience.cdk1.io.formats.IResourceFormat;
import org.openscience.cdk1.io.formats.MDLFormat;
import org.openscience.cdk1.io.formats.MDLV2000Format;
import org.openscience.cdk1.io.formats.MDLV3000Format;
import org.openscience.cdk1.io.formats.PDBFormat;
import org.openscience.cdk1.io.formats.PubChemSubstancesASNFormat;
import org.openscience.cdk1.io.formats.SMILESFormat;
import org.openscience.cdk1.io.iterator.DefaultIteratingChemObjectReader;
import org.openscience.cdk1.io.iterator.IteratingMDLReader;
import org.openscience.cdk1.io.iterator.IteratingPCCompoundASNReader;
import org.openscience.cdk1.tools.ILoggingTool;
import org.openscience.cdk1.tools.LoggingToolFactory;
import org.openscience.cdk1.tools.manipulator.ChemFileManipulator;

import libpadeldescriptor.PaDELHINReader;
import libpadeldescriptor.PaDELIteratingSMILESReader;
import libpadeldescriptor.PaDELPDBReader;

/**
 *
 */

public class CustomIteratingPadelReader extends DefaultIteratingChemObjectReader
{

	private BufferedReader					input;
	private static ILoggingTool				logger		= LoggingToolFactory
			.createLoggingTool(CustomIteratingPadelReader.class);
	private IResourceFormat					currentFormat;
	private boolean							nextAvailableIsKnown;
	private boolean							hasNext;
	private IChemObjectBuilder				builder;
	private IAtomContainer					nextMolecule;

	private File							molecule;
	private PaDELIteratingSMILESReader		iteratingSMILESReader;
	private IteratingMDLReader				iteratingMDLReader;
	private IteratingPCCompoundASNReader	iteratingPCCompoundASNReader;
	private ISimpleChemObjectReader			iSimpleChemObjectReader;
	private int								curLigIndex;
	private boolean							isSmiles	= false;
	private List<IAtomContainer>			container	= new ArrayList<IAtomContainer>();

	/**
	 * Constructs a new IteratingPaDELReader that can read Molecule from a given Reader.
	 *
	 * @param in
	 *            The Reader to read from
	 * @param builder
	 *            The builder to use
	 * @see org.openscience.cdk1.DefaultChemObjectBuilder
	 * @see org.openscience.cdk1.nonotify.NoNotificationChemObjectBuilder
	 */
	public CustomIteratingPadelReader(File molecule) throws FileNotFoundException
	{
		this.builder = DefaultChemObjectBuilder.getInstance();
		this.molecule = molecule;
		setReader(new FileInputStream(molecule));
	}

	/**
	 * Constructs a new IteratingPaDELReader that can read Molecule from a given Reader.
	 *
	 * @param in
	 *            The Reader to read from
	 * @param builder
	 *            The builder to use
	 * @see org.openscience.cdk1.DefaultChemObjectBuilder
	 * @see org.openscience.cdk1.nonotify.NoNotificationChemObjectBuilder
	 */
	public CustomIteratingPadelReader(Reader in, IChemObjectBuilder builder, boolean smiles)
	{
		this.builder = builder;
		this.isSmiles = smiles;
		setReader(in);
	}

	/**
	 * Constructs a new IteratingPaDELReader that can read Molecule from a given InputStream.
	 *
	 * This method will use @link{DefaultChemObjectBuilder} to build the actual molecules
	 *
	 * @param in
	 *            The InputStream to read from
	 */
	public CustomIteratingPadelReader(InputStream in, boolean smiles)
	{
		this(new InputStreamReader(in), DefaultChemObjectBuilder.getInstance(), smiles);
	}

	/**
	 * Get the format for this reader.
	 *
	 * @return
	 */
	@Override
	public IResourceFormat getFormat()
	{
		return currentFormat;
	}

	/**
	 * Checks whether there is another molecule to read.
	 *
	 * @return true if there are molecules to read, false otherwise
	 */
	@Override
	public boolean hasNext()
	{
		if (!nextAvailableIsKnown)
		{
			hasNext = false;

			// now try to parse the next Molecule
			if (currentFormat instanceof SMILESFormat)
			{
				if (iteratingSMILESReader.hasNext())
				{
					nextMolecule = (IAtomContainer) iteratingSMILESReader.next();
					++curLigIndex;
					hasNext = true;
				}
				else
				{
					hasNext = false;
				}
			}
			else if (currentFormat instanceof MDLFormat || currentFormat instanceof MDLV2000Format
					|| currentFormat instanceof MDLV3000Format)
			{
				if (iteratingMDLReader.hasNext())
				{
					nextMolecule = (IAtomContainer) iteratingMDLReader.next();
					++curLigIndex;
					hasNext = true;
				}
				else
				{
					hasNext = false;
				}
			}
			else if (currentFormat instanceof PubChemSubstancesASNFormat)
			{
				if (iteratingPCCompoundASNReader.hasNext())
				{
					nextMolecule = (IAtomContainer) iteratingPCCompoundASNReader.next();
					++curLigIndex;
					hasNext = true;
				}
				else
				{
					hasNext = false;
				}
			}
			else
			{
				if (curLigIndex < container.size())
				{
					nextMolecule = container.get(curLigIndex++);
					hasNext = true;
				}
				else
				{
					hasNext = false;
				}
			}
			if (!hasNext)
				nextMolecule = null;
			nextAvailableIsKnown = true;
		}
		return hasNext;
	}

	/**
	 * Get the next molecule from the stream.
	 *
	 * @return The next molecule
	 */
	@Override
	public IChemObject next()
	{
		if (!nextAvailableIsKnown)
		{
			hasNext();
		}
		nextAvailableIsKnown = false;
		if (!hasNext)
		{
			throw new NoSuchElementException();
		}
		return nextMolecule;
	}

	/**
	 * Close the reader.
	 *
	 * @throws IOException
	 *             if there is an error during closing
	 */
	@Override
	public void close() throws IOException
	{
		input.close();
		if (iteratingSMILESReader != null)
			iteratingSMILESReader.close();
		if (iteratingMDLReader != null)
			iteratingMDLReader.close();
		if (iteratingPCCompoundASNReader != null)
			iteratingPCCompoundASNReader.close();
		if (iSimpleChemObjectReader != null)
			iSimpleChemObjectReader.close();
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setReader(Reader reader)
	{
		if (reader instanceof BufferedReader)
		{
			input = (BufferedReader) reader;
		}
		else
		{
			input = new BufferedReader(reader);
		}
		nextAvailableIsKnown = false;
		curLigIndex = 0;

		try
		{
			if (isSmiles || (molecule != null && molecule.getName().endsWith(".smi")))
			{
				// Have to manually check for SMILES file because CDK cannot detect it properly yet.
				currentFormat = SMILESFormat.getInstance();
			}
			else if (molecule != null && molecule.getName().endsWith(".hin"))
			{
				// Have to manually check for HIN file because CDK cannot detect it properly yet.
				iSimpleChemObjectReader = new PaDELHINReader(input);
				currentFormat = HINFormat.getInstance();
			}
			else
			{
				iSimpleChemObjectReader = new ReaderFactory().createReader(input);
				currentFormat = iSimpleChemObjectReader.getFormat();
			}

			if (currentFormat instanceof SMILESFormat)
			{
				iteratingSMILESReader = new PaDELIteratingSMILESReader(input, builder);
			}
			else if (currentFormat instanceof MDLFormat || currentFormat instanceof MDLV2000Format
					|| currentFormat instanceof MDLV3000Format)
			{
				iteratingMDLReader = new IteratingMDLReader(input, builder);
			}
			else if (currentFormat instanceof PubChemSubstancesASNFormat)
			{
				iteratingPCCompoundASNReader = new IteratingPCCompoundASNReader(input, builder);
			}

			else if (currentFormat instanceof PDBFormat)
			{
				try
				{
					// Have to manually check for PDB file because CDK cannot read it properly yet so have to
					// use a modified version of PDBReader
					PaDELPDBReader pdb = new PaDELPDBReader(input);
					ChemFile cf = new ChemFile();
					pdb.read(cf);
					container.clear();
					for (int i = 0, endi = cf.getChemSequenceCount(); i < endi; ++i)
					{
						IChemSequence cs = cf.getChemSequence(i);
						for (int j = 0, endj = cs.getChemModelCount(); j < endj; ++j)
						{
							IMoleculeSet ms = cs.getChemModel(j).getMoleculeSet();
							for (int k = 0, endk = ms.getMoleculeCount(); k < endk; ++k)
							{
								container.add(ms.getMolecule(k));
							}
						}
					}
				}
				catch (CDKException ex1)
				{
					logger.error("Error while reading molecule: " + ex1.getMessage());
				}
			}
			else
			{
				IChemFile content = (IChemFile) iSimpleChemObjectReader
						.read(builder.newInstance(IChemFile.class));
				if (content != null)
				{
					container = ChemFileManipulator.getAllAtomContainers(content);
				}
			}
		}
		catch (Exception ex)
		{
			logger.error("Error while reading molecule: " + ex.getMessage());
			logger.debug(ex);
			ex.printStackTrace();
			nextAvailableIsKnown = true;
		}
		nextMolecule = null;
		hasNext = false;
	}

	@Override
	public void setReader(InputStream reader)
	{
		setReader(new InputStreamReader(reader));
	}
}
