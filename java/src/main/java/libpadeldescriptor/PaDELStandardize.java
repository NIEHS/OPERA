package libpadeldescriptor;

import ambit2.smarts.SMIRKSManager;
import ambit2.smarts.SMIRKSReaction;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk1.CDKConstants;
import org.openscience.cdk1.DefaultChemObjectBuilder;
import org.openscience.cdk1.Molecule;
import org.openscience.cdk1.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.exception.InvalidSmilesException;
import org.openscience.cdk1.graph.ConnectivityChecker;
import org.openscience.cdk1.interfaces.IAtomType.Hybridization;
import org.openscience.cdk1.interfaces.*;
import org.openscience.cdk1.smiles.SmilesGenerator;
import org.openscience.cdk1.smiles.SmilesParser;
import org.openscience.cdk1.tools.AtomTypeTools;
import org.openscience.cdk1.tools.CDKHydrogenAdder;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;


public class PaDELStandardize
{
    private boolean removeSalt_ = true;
    private boolean dearomatize_ = true;
    private boolean standardizeTautomers_ = true;
    private boolean standardizeNitro_ = true;
    private boolean retain3D_ = false;
    private String[] tautomerList_;

    public PaDELStandardize()
    {

    }

    public void setRemoveSalt(boolean removeSalt)
    {
        removeSalt_ = removeSalt;
    }

    public void setDearomatize(boolean dearomatize)
    {
        dearomatize_ = dearomatize;
    }

    public void setStandardizeTautomers(boolean standardizeTautomers)
    {
        standardizeTautomers_ = standardizeTautomers;
    }

    public String[] getTautomerList()
    {
        return tautomerList_;
    }

    public void setTautomerList(String[] tautomerList)
    {
        tautomerList_ = tautomerList;
    }

    public void setTautomerList(File tautomerFile)
    {
        setTautomerList(getTautomerList(tautomerFile));
    }

    public static String[] getTautomerList(File tautomerFile)
    {
        ArrayList<String> tautomers = new ArrayList<String>();
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(tautomerFile));
            String line;
            while ((line=in.readLine())!=null)
            {
                if (line.trim().isEmpty() || line.startsWith("//")) continue;
                String[] result = line.split("\\t");
                tautomers.add(result[0]);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger("global").log(Level.FINE, ex.getMessage());
            return null;
        }

        String[] tautomerList = new String[tautomers.size()];
        tautomers.toArray(tautomerList);
        return tautomerList;
    }

    public static String[] getTautomerList(InputStream is)
    {
        ArrayList<String> tautomers = new ArrayList<String>();
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line=in.readLine())!=null)
            {
                if (line.trim().isEmpty() || line.startsWith("//")) continue;
                String[] result = line.split("\\t");
                tautomers.add(result[0]);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger("global").log(Level.FINE, ex.getMessage());
            return null;
        }

        String[] tautomerList = new String[tautomers.size()];
        tautomers.toArray(tautomerList);
        return tautomerList;
    }

    public void setStandardizeNitro(boolean standardizeNitro)
    {
        standardizeNitro_ = standardizeNitro;
    }

    public void setRetain3D(boolean retain3D)
    {
        retain3D_ = retain3D;
    }

    public IAtomContainer Standardize(IAtomContainer molecule) throws Exception
    {
        if (removeSalt_)
        {
            molecule = RemoveSalt(molecule);
        }

	CleanMolecule(molecule);
        
        PerceiveAtomTypes(molecule);
        
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance());
        adder.addImplicitHydrogens(molecule);

        if (dearomatize_)
        {
            molecule = Dearomatize(molecule);
        }

        if (standardizeNitro_)
        {
            StandardizeNitro2(molecule);
        }

        if (standardizeTautomers_)
        {
            molecule = StandardizeTautomers(molecule);
        }

        // Tidy up molecule
        // Assign some properties like ISINRING, ISNOTINRING, ISALIPHATIC to atoms
        new AtomTypeTools().assignAtomTypePropertiesToAtom((IMolecule)molecule, false);

        // Add hydrogens
        adder.addImplicitHydrogens(molecule);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);

        PerceiveAtomTypes(molecule);

        return molecule;
    }

    private IAtomContainer RemoveSalt(IAtomContainer molecule)
    {
        // Get biggest connected molecule.
        if (!ConnectivityChecker.isConnected(molecule))
        {
            IMoleculeSet molSet = ConnectivityChecker.partitionIntoMolecules(molecule);
            IMolecule biggest = molSet.getMolecule(0);
            for (int i = 1; i < molSet.getMoleculeCount(); i++)
            {
                if (molSet.getMolecule(i).getBondCount() > biggest.getBondCount())
                {
                    biggest = molSet.getMolecule(i);
                }
            }

            molecule = (Molecule) biggest;
        }
        return molecule;
    }
    
    private void CleanMolecule(IAtomContainer molecule)
    {	
        // Remove obvious errors in molecule.
	for (IAtom atom : molecule.atoms())
	{
            // Make sure all atoms have atomic number information as it may not be present for some files.
	    if (atom.getAtomicNumber() == CDKConstants.UNSET) atom.setAtomicNumber(AtomConstants.atomic_number.get(atom.getSymbol()));
                       
            // Make sure nitrogen atoms with 4 bonds have a formal charge of 1.
            if (atom.getAtomicNumber()==7 && molecule.getConnectedBondsCount(atom)==4 && atom.getFormalCharge()==0)
            {
                atom.setFormalCharge(1);
            }
	}        
    }

    private void PerceiveAtomTypes(IAtomContainer molecule) throws CDKException
    {
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
        // Ensure if bond is aromatic, corresponding atoms are aromatic too and vice versa.
	boolean hasModified = false;
	do
	{
	    hasModified = false;
	    for (IBond bond : molecule.bonds())
	    {
		if (bond.getFlag(CDKConstants.ISAROMATIC))
		{
		    for (IAtom atom : bond.atoms())
		    {
			if (!atom.getFlag(CDKConstants.ISAROMATIC))
			{
			    atom.setFlag(CDKConstants.ISAROMATIC, true);
			    hasModified = true;
			}
		    }
		}
	    }

	    for (IBond bond : molecule.bonds())
	    {
		if (!bond.getFlag(CDKConstants.ISAROMATIC))
		{
		    boolean allAtomsAromatic = true;
		    for (IAtom atom : bond.atoms())
		    {
			if (!atom.getFlag(CDKConstants.ISAROMATIC))
			{
			    allAtomsAromatic = false;
			    break;
			}
		    }
		    if (allAtomsAromatic)
		    {
			if (!bond.getFlag(CDKConstants.ISAROMATIC))
			{
			    bond.setFlag(CDKConstants.ISAROMATIC, true);
			    hasModified = true;
			}
		    }
		}
	    }
	}
	while (hasModified);
    }

    private IAtomContainer Dearomatize(IAtomContainer molecule) throws InvalidSmilesException, CDKException
    {
        // Get molecule name.
        StringBuffer molName = new StringBuffer();
        molName.setLength(0);
        if (molecule.getProperty("cdk:Title")!=null)
        {
           molName.append(molecule.getProperty("cdk:Title"));
        }

        if (!retain3D_)
        {
            molecule = AtomContainerManipulator.removeHydrogens(molecule);
        }

        molecule = PaDELDearomatize.Dearomatize(molecule);

        // Add implicit hydrogens. Not really necessary but just in case.
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance());
        adder.addImplicitHydrogens(molecule);

        if (!retain3D_)
        {
            molecule = new SmilesParser(DefaultChemObjectBuilder.getInstance()).parseSmiles(new SmilesGenerator(true).createSMILES(molecule));
            molecule.setProperty("cdk:Title", molName.toString());
        }

        // Detect aromaticity in the molecule.
        CDKHueckelAromaticityDetector.detectAromaticity(molecule);

        return molecule;
    }

    private IAtomContainer StandardizeTautomers(IAtomContainer molecule) throws CDKException
    {
        // Get molecule name.
        StringBuffer molName = new StringBuffer();
        molName.setLength(0);
        if (molecule.getProperty("cdk:Title")!=null)
        {
           molName.append(molecule.getProperty("cdk:Title"));
        }

        // Add hydrogens. Necessary for InChITautomerGenerator.
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance());
        adder.addImplicitHydrogens(molecule);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
        PerceiveAtomTypes(molecule);

//        // Standardize tautomer using the first tautomer generated by InChITautomerGenerator.
//        InChITautomerGenerator tautomerGenerator = new InChITautomerGenerator();
//        List<IAtomContainer> tautomers = null;
//        try
//        {
//            tautomers = tautomerGenerator.getTautomers(molecule);
//            if (retain3D_)
//            {
//                molecule = tautomers.get(0);
//            }
//            else
//            {
//                molecule = new SmilesParser(DefaultChemObjectBuilder.getInstance()).parseSmiles(new SmilesGenerator().createSMILES(tautomers.get(0)));
//                molecule.setProperty("cdk:Title", molName.toString());
//            }
//        }
//        catch (Exception ex)
//        {
//            StringWriter sw = new StringWriter();
//            ex.printStackTrace(new PrintWriter(sw));
//            String exceptionAsString = sw.toString();
//            Logger.getLogger("global").log(Level.FINE, "InChITautomerGenerator:" + molName.toString() + ":" + exceptionAsString);
//        }

//        // Standardize tautomer using ambit2
//        TautomerManager tautMan = new TautomerManager();
//        try
//        {
//            tautMan.setStructure(molecule);
//            tautomers = tautMan.generateTautomers();
//            if (tautomers.size()>1)
//            {
//                if (retain3D_)
//                {
//                    molecule = tautomers.get(1);
//                }
//                else
//                {
//                    molecule = new SmilesParser(DefaultChemObjectBuilder.getInstance()).parseSmiles(new SmilesGenerator().createSMILES(tautomers.get(1)));
//                    molecule.setProperty("cdk:Title", molName.toString());
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//            Logger.getLogger("global").log(Level.FINE, "TautomerManager:" + molName.toString() + ":" + ex.toString());
//        }
//
        // Standardize tautomers using a list.
        if (tautomerList_!=null)
        {
            int curPos = 0;
            int lastPosTransform = -1;
            int remainingLoops = 100;
            boolean nextLoop = true;
            int maxTautomerList = tautomerList_.length;
            SMIRKSManager smrkMan = new SMIRKSManager(DefaultChemObjectBuilder.getInstance());
            do
            {
            	String smirks = tautomerList_[curPos];
            	SMIRKSReaction reaction = smrkMan.parse(smirks);
                try
                {
                    if (lastPosTransform==curPos) nextLoop = false;
                    else if (smrkMan.applyTransformation(molecule, reaction))
                    {
                    	lastPosTransform = curPos;
                    	if (!retain3D_)
                        {
                            molecule = new SmilesParser(DefaultChemObjectBuilder.getInstance()).parseSmiles(new SmilesGenerator(true).createSMILES(molecule));
                            molecule.setProperty("cdk:Title", molName.toString());
                        }
                    }
                }
                catch (Exception ex)
                {
                    Logger.getLogger("global").log(Level.FINE, "SMIRKSManager:" + molName.toString() + ":" + ex.getMessage());
                }

                if (curPos==(maxTautomerList-1))
                {
                    if (lastPosTransform == -1) nextLoop = false;
                    else curPos = 0;
                }
                else ++curPos;
                --remainingLoops;
            } while (nextLoop &&  (remainingLoops>0));
        }

        return molecule;
    }

    // Standardize nitro group to N(:O):O
    public static void StandardizeNitro(IAtomContainer molecule)
    {
        for (IAtom atom : molecule.atoms())
        {
            if (atom.getAtomicNumber()==7)
            {
                List<IAtom> neighbours = molecule.getConnectedAtomsList(atom);
                if (neighbours.size()!=3) continue;

                int countOxygens = 0;
                boolean isBothOH = true;
                for (IAtom neighbour : neighbours)
                {
                    if (neighbour.getAtomicNumber()==8)
                    {
                        ++countOxygens;
                        List<IAtom> neighbours1 = molecule.getConnectedAtomsList(neighbour);
                        for (IAtom neighbour1 : neighbours1)
                        {
                            if (neighbour1!=atom)
                            {
                                if (neighbour1.getAtomicNumber()!=1)
                                {
                                    isBothOH = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (countOxygens!=2 || !isBothOH) continue; // Skip if not NO2 or either O is not bonded only to N and H.

                atom.setFlag(CDKConstants.ISAROMATIC, true);
                atom.setFormalCharge(0);
                atom.setHybridization(Hybridization.SP2);
                atom.setImplicitHydrogenCount(0);
                atom.setAtomTypeName("N.sp2");
                for (IAtom neighbour : neighbours)
                {
                    if (neighbour.getAtomicNumber()==8)
                    {
                        neighbour.setFlag(CDKConstants.ISAROMATIC, true);
                        neighbour.setFormalCharge(0);
                        neighbour.setHybridization(Hybridization.SP2);
                        neighbour.setAtomTypeName("O.sp2");
                        neighbour.setImplicitHydrogenCount(0);
                        List<IAtom> neighbours1 = molecule.getConnectedAtomsList(neighbour);
                        for (IAtom neighbour1 : neighbours1)
                        {
                            if (neighbour1!=atom)
                            {
                                // Remove hydrogen from O if present.
                                molecule.removeBond(neighbour, neighbour1);
                                molecule.removeAtom(neighbour1);
                            }
                        }
                        IBond bond = molecule.getBond(atom, neighbour);
                        bond.setOrder(IBond.Order.SINGLE);
                        bond.setFlag(CDKConstants.ISAROMATIC, true);
                    }
                    else if (neighbour.getFlag(CDKConstants.ISAROMATIC))
                    {
                        // If nitrogen is attached to an aromatic atom, then the bond between them should also be aromatic
                        molecule.getBond(atom, neighbour).setFlag(CDKConstants.ISAROMATIC, true);
                    }
                }
            }
        }
    }

    // Standardize nitro group to N+(=O)O-
    public static void StandardizeNitro2(IAtomContainer molecule)
    {
        for (IAtom atom : molecule.atoms())
        {
            if (atom.getAtomicNumber()==7)
            {
                List<IAtom> neighbours = molecule.getConnectedAtomsList(atom);
                if (neighbours.size()!=3) continue;

                int countOxygens = 0;
                boolean isBothOH = true;
                for (IAtom neighbour : neighbours)
                {
                    if (neighbour.getAtomicNumber()==8)
                    {
                        ++countOxygens;
                        List<IAtom> neighbours1 = molecule.getConnectedAtomsList(neighbour);
                        for (IAtom neighbour1 : neighbours1)
                        {
                            if (neighbour1!=atom)
                            {
                                if (neighbour1.getAtomicNumber()!=1)
                                {
                                    isBothOH = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (countOxygens!=2 || !isBothOH) continue; // Skip if not NO2 or either O is not bonded only to N and H.

		atom.setFlag(CDKConstants.ISAROMATIC, false);
                atom.setFormalCharge(1);
                atom.setHybridization(Hybridization.SP2);
                atom.setImplicitHydrogenCount(0);
                atom.setAtomTypeName("N.plus.sp2");
                boolean isOMinus = true;
                for (IAtom neighbour : neighbours)
                {
                    if (neighbour.getAtomicNumber()==8)
                    {
                        neighbour.setImplicitHydrogenCount(0);
                        List<IAtom> neighbours1 = molecule.getConnectedAtomsList(neighbour);
                        for (IAtom neighbour1 : neighbours1)
                        {
                            if (neighbour1!=atom)
                            {
                                // Remove hydrogen from O if present.
                                molecule.removeBond(neighbour, neighbour1);
                                molecule.removeAtom(neighbour1);
                            }
                        }
                        IBond bond = molecule.getBond(atom, neighbour);
                        if (isOMinus)
                        {
                            neighbour.setFlag(CDKConstants.ISAROMATIC, false);
			    neighbour.setFormalCharge(-1);
                            neighbour.setHybridization(Hybridization.SP3);
                            neighbour.setAtomTypeName("O.minus");
                            bond.setOrder(IBond.Order.SINGLE);
			    bond.setFlag(CDKConstants.ISAROMATIC, false);
                            isOMinus = false;
                        }
                        else
                        {
                            neighbour.setFlag(CDKConstants.ISAROMATIC, false);
			    neighbour.setFormalCharge(0);
                            neighbour.setHybridization(Hybridization.SP2);
                            neighbour.setAtomTypeName("O.sp2");
                            bond.setOrder(IBond.Order.DOUBLE);
			    bond.setFlag(CDKConstants.ISAROMATIC, false);
                            isOMinus = true;
                        }
                    }
		    else if (neighbour.getFlag(CDKConstants.ISAROMATIC))
                    {
                        // If nitrogen is attached to an aromatic atom, make sure that the bond is not aromatic.
			// This is used to remove the bond aromatic flag created by StandardizeNitro or added by the users.
                        molecule.getBond(atom, neighbour).setFlag(CDKConstants.ISAROMATIC, false);
                    }
                }
            }
        }
    }

    // Standardize nitro group to N(=O)=O
    public static void StandardizeNitro3(IAtomContainer molecule)
    {
        for (IAtom atom : molecule.atoms())
        {
            if (atom.getAtomicNumber()==7)
            {
                List<IAtom> neighbours = molecule.getConnectedAtomsList(atom);
                if (neighbours.size()!=3) continue;

                int countOxygens = 0;
                boolean isBothOH = true;
                for (IAtom neighbour : neighbours)
                {
                    if (neighbour.getAtomicNumber()==8)
                    {
                        ++countOxygens;
                        List<IAtom> neighbours1 = molecule.getConnectedAtomsList(neighbour);
                        for (IAtom neighbour1 : neighbours1)
                        {
                            if (neighbour1!=atom)
                            {
                                if (neighbour1.getAtomicNumber()!=1)
                                {
                                    isBothOH = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (countOxygens!=2 || !isBothOH) continue; // Skip if not NO2 or either O is not bonded only to N and H.

		atom.setFlag(CDKConstants.ISAROMATIC, false);
                atom.setFormalCharge(1);
                atom.setHybridization(Hybridization.SP2);
                atom.setImplicitHydrogenCount(0);
                atom.setAtomTypeName("N.sp2");
                for (IAtom neighbour : neighbours)
                {
                    if (neighbour.getAtomicNumber()==8)
                    {
                        neighbour.setImplicitHydrogenCount(0);
                        List<IAtom> neighbours1 = molecule.getConnectedAtomsList(neighbour);
                        for (IAtom neighbour1 : neighbours1)
                        {
                            if (neighbour1!=atom)
                            {
                                // Remove hydrogen from O if present.
                                molecule.removeBond(neighbour, neighbour1);
                                molecule.removeAtom(neighbour1);
                            }
                        }
                        IBond bond = molecule.getBond(atom, neighbour);
                        neighbour.setFlag(CDKConstants.ISAROMATIC, false);
			neighbour.setFormalCharge(0);
			neighbour.setHybridization(Hybridization.SP2);
			neighbour.setAtomTypeName("O.sp2");
			bond.setOrder(IBond.Order.DOUBLE);
			bond.setFlag(CDKConstants.ISAROMATIC, false);
                    }
		    else if (neighbour.getFlag(CDKConstants.ISAROMATIC))
                    {
                        // If nitrogen is attached to an aromatic atom, make sure that the bond is not aromatic.
			// This is used to remove the bond aromatic flag created by StandardizeNitro or added by the users.
                        molecule.getBond(atom, neighbour).setFlag(CDKConstants.ISAROMATIC, false);
                    }
                }
            }
        }
    }
}
