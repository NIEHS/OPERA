package libpadeldescriptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk1.CDKConstants;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtomType.Hybridization;
import org.openscience.cdk1.interfaces.*;
import org.openscience.cdk1.ringsearch.RingPartitioner;
import org.openscience.cdk1.ringsearch.SSSRFinder;

// Dearomatize all rings.

public class PaDELDearomatize
{
    private PaDELDearomatize()
    {

    }

    public static IAtomContainer Dearomatize(IAtomContainer molecule)
    {
        IAtomContainer tempMolecule = null;
	try
	{
	    tempMolecule = (IAtomContainer)molecule.clone();
	}
	catch (Exception ex)
	{
            return molecule;
	}

        SSSRFinder finder = new SSSRFinder(tempMolecule);
        IRingSet sssr = finder.findEssentialRings();
        List<IRingSet> rings = RingPartitioner.partitionRings(sssr);
        for (IRingSet ring : rings)
        {
            RemoveNonAromaticRings(ring);
            if (Dearomatize666Rings(ring)) continue;
            if (Dearomatize566Rings(ring)) continue;
            if (Dearomatize66Rings(ring)) continue;
            if (Dearomatize56Rings(ring)) continue;
            if (Dearomatize6Ring(ring)) continue;
            if (Dearomatize5Ring(ring)) continue;
//            DearomatizeAnyBenzeneRing(ring);
//            DearomatizeAny5Ring(ring);
        }
        
        // Set remaining aromatic bonds to new CDKConstants.SINGLE_OR_DOUBLE
        // To remove after upgrading to CDK 1.5.x
        for (IBond bond : tempMolecule.bonds())
        {
            if (bond.getFlag(CDKConstants.ISAROMATIC))
            {
                bond.setFlag(12, true);
            }
        }
               
        try 
        {
            new AtomTypeAwareSaturationChecker().decideBondOrder(tempMolecule);
        } 
        catch (Exception ex)
        {
            
        }

        DearomatizeConjugation(tempMolecule);
               
        // Removed in v2.18 because it is causing instability in TopoPSA calculation for Carol Marchant's compounds.
//        // Uses CDK FixBondOrdersTool to remove any other types of aromatic rings.
//        try
//        {
//            tempMolecule = new FixBondOrdersTool().kekuliseAromaticRings((IMolecule)tempMolecule);
//            // Remove aromatic flags from rings because the FixBondOrdersTool.kekuliseAromaticRings does not do so.
//            finder = new SSSRFinder(tempMolecule);
//            sssr = finder.findEssentialRings();
//            rings = RingPartitioner.partitionRings(sssr);
//            for (IRingSet ring : rings)
//            {
//                for (IAtomContainer subring : ring.atomContainers())
//                {
//                    RemoveAromaticFlag(subring);
//                }
//            }
//        }
//        catch (Exception ex)
//        {
//
//        }
                    

//        DearomatizeAmide(tempMolecule);

        return tempMolecule;
    }

    private static boolean DearomatizeConjugation(IAtomContainer molecule)
    {
        // Find the start atom of the conjugation.
        IAtom startAtom = null;
        for (IAtom atom : molecule.atoms())
        {
            if (atom.getFlag(CDKConstants.ISAROMATIC))
            {
                startAtom = GetConjugatedStartAtom(molecule, atom, null, new HashSet<IAtom>());
                if (CountConjugatedAtoms(molecule, startAtom, null, new HashSet<IAtom>(), 1)<3) startAtom = null; // Conjugation should involve more than 2 atoms.
                break;
            }
        }

        if (startAtom!=null)
        {
            ConvertConjugation(molecule, startAtom, null, false);
        }

        return true;
    }

    private static IAtom GetConjugatedStartAtom(IAtomContainer molecule, IAtom current, IAtom previous, Set<IAtom> seen)
    {
        seen.add(current);
        List<IAtom> neighbours = molecule.getConnectedAtomsList(current);
        for (IAtom neighbour : neighbours)
        {
            if (neighbour!=previous && neighbour.getFlag(CDKConstants.ISAROMATIC))
            {
                if (seen.contains(neighbour)) return null;
                else return GetConjugatedStartAtom(molecule, neighbour, current, seen);
            }
        }
        return current; // Cannot find any more neighouring aromatic atom so current atom must be starting atom.
    }
    
    private static int CountConjugatedAtoms(IAtomContainer molecule, IAtom current, IAtom previous, Set<IAtom> seen, int count)
    {
        seen.add(current);
        List<IAtom> neighbours = molecule.getConnectedAtomsList(current);
        for (IAtom neighbour : neighbours)
        {
            if (neighbour!=previous && neighbour.getFlag(CDKConstants.ISAROMATIC))
            {
                if (seen.contains(neighbour)) return 0;
                else return CountConjugatedAtoms(molecule, neighbour, current, seen, ++count);
            }
        }
        return count; // Cannot find any more neighouring aromatic atom so current atom must be starting atom.
    }

    private static void ConvertConjugation(IAtomContainer molecule, IAtom current, IAtom previous, boolean isSingle)
    {
        current.setFlag(CDKConstants.ISAROMATIC, false);
        if (current.getAtomicNumber()==8) // Oxygen
        {
            current.setFormalCharge(0);
            current.setHybridization(Hybridization.SP2);
            current.setAtomTypeName("O.sp2");
        }
        List<IAtom> neighbours = molecule.getConnectedAtomsList(current);
        for (IAtom neighbour : neighbours)
        {
            if (neighbour!=previous && neighbour.getFlag(CDKConstants.ISAROMATIC))
            {
                IBond bond = molecule.getBond(current, neighbour);
                bond.setFlag(CDKConstants.ISAROMATIC, false);
                if (isSingle)
                {
                    bond.setOrder(IBond.Order.SINGLE);
                }
                else
                {
                    bond.setOrder(IBond.Order.DOUBLE);
                }
                ConvertConjugation(molecule, neighbour, current, !isSingle);
                break;
            }
        }
//        for (IBond bond : molecule.getConnectedBondsList(current))
//        {
//            // Ensure all bonds connected to non-aromatic atoms are non-aromatic
//            bond.setFlag(CDKConstants.ISAROMATIC, false);
//        }
    }
    
    private static boolean DearomatizeAmide(IAtomContainer molecule)
    {
        for (IAtom atom : molecule.atoms())
        {
            if (atom.getAtomicNumber()==7)
            {
                List<IAtom> neighbors = molecule.getConnectedAtomsList(atom);
                for (IAtom neighbor : neighbors) 
                {
                    if (neighbor.getAtomicNumber()==6) 
                    {
                        if (CountAttachedBonds(molecule, neighbor, IBond.Order.DOUBLE, "O") == 1)
                        {
                            if (atom.getFlag(CDKConstants.ISAROMATIC) && 
                                neighbor.getFlag(CDKConstants.ISAROMATIC) &&
                                molecule.getBond(atom, neighbor).getFlag(CDKConstants.ISAROMATIC))
                            {
                                atom.setFlag(CDKConstants.ISAROMATIC, false);
                                neighbor.setFlag(CDKConstants.ISAROMATIC, false);
                                molecule.getBond(atom, neighbor).setFlag(CDKConstants.ISAROMATIC, false);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
    
    private static int CountAttachedBonds(IAtomContainer container, IAtom atom, IBond.Order order, String symbol) {
    	// count the number of double bonded oxygens
    	List<IBond> neighbors = container.getConnectedBondsList(atom);
    	int neighborcount = neighbors.size();
    	int doubleBondedAtoms = 0;
    	for (int i=neighborcount-1;i>=0;i--) {
            IBond bond =  neighbors.get(i);
    		if (bond.getOrder() == order) {
    			if (bond.getAtomCount() == 2 && bond.contains(atom)) {
    				if (symbol != null) {
    				    // if other atom is a sulphur
    					if ((bond.getAtom(0) != atom &&
    					     bond.getAtom(0).getSymbol().equals(symbol)) ||
    						(bond.getAtom(1) != atom &&
    						 bond.getAtom(1).getSymbol().equals(symbol))) {
    						doubleBondedAtoms++;
    					}
    				} else {
    					doubleBondedAtoms++;
    				}
    			}
    		}
    	}
    	return doubleBondedAtoms;
    }
    
    private static void RemoveNonAromaticRings(IRingSet rings)
    {
        List<IAtomContainer> ringsToRemove = new ArrayList<IAtomContainer>();
        for (IAtomContainer ring : rings.atomContainers())
        {
            if (!isAllAtomsAromatic(ring))
            {
                ringsToRemove.add(ring);
            }
        }
        for (int i=0, endi=ringsToRemove.size(); i<endi; ++i)
        {
            rings.removeAtomContainer(ringsToRemove.get(i));
        }
    }

    private static boolean Dearomatize666Rings(IRingSet rings)
    {
        if (rings.getAtomContainerCount()!=3)
        {
            // Stop if not three rings fused together.
            return false;
        }
        IAtomContainer ringA = rings.getAtomContainer(0);
        IAtomContainer ringB = rings.getAtomContainer(1);
        IAtomContainer ringC = rings.getAtomContainer(2);

        // Make ringB the middle ring.
        if (isMiddleRing(ringA, ringB, ringC))
        {
            IAtomContainer temp = ringB;
            ringB = ringA;
            ringA = temp;
        }
        if (isMiddleRing(ringC, ringA, ringB))
        {
            IAtomContainer temp = ringB;
            ringB = ringC;
            ringC = temp;
        }

        int maxAtomsRingA = ringA.getAtomCount();
        int maxAtomsRingB = ringB.getAtomCount();
        int maxAtomsRingC = ringC.getAtomCount();
        if (maxAtomsRingA!=6 || maxAtomsRingB!=6 || maxAtomsRingC!=6)
        {
            // Stop if not a 6-membered ring fused to 6-membered ring fused to another 6-membered ring.
            return false;
        }

        if (!isAllAtomsAromatic(ringA) || !isAllAtomsAromatic(ringB) || !isAllAtomsAromatic(ringC))
        {
            // Not all atoms of the three rings are aromatic.
            return false;
        }

        InvalidateBonds(ringA);
        InvalidateBonds(ringB);
        InvalidateBonds(ringC);

        // Change first 6-membered ring.
        DearomatizeRing(ringA, null, null);

        // Change second 6-membered ring.
        DearomatizeRing(ringB, null, null);

        // Change third 6-membered ring.
        DearomatizeRing(ringC, null, null);

        return true;
    }

    private static boolean Dearomatize566Rings(IRingSet rings)
    {
        if (rings.getAtomContainerCount()!=3)
        {
            // Stop if not three rings fused together.
            return false;
        }
        IAtomContainer ringA = rings.getAtomContainer(0);
        IAtomContainer ringB = rings.getAtomContainer(1);
        IAtomContainer ringC = rings.getAtomContainer(2);

        if (ringA.getAtomCount() > ringB.getAtomCount())
        {
            // Ensure ringA is smaller than ringB and ringC
            ringA = rings.getAtomContainer(1);
            ringB = rings.getAtomContainer(0);
        }
        else if (ringA.getAtomCount() > ringC.getAtomCount())
        {
            // Ensure ringA is smaller than ringB and ringC
            ringA = rings.getAtomContainer(2);
            ringC = rings.getAtomContainer(0);
        }

        // Make ringB the middle ring if ringA is not the middle ring.
        if (isMiddleRing(ringC, ringA, ringB))
        {
            IAtomContainer temp = ringB;
            ringB = ringC;
            ringC = temp;
        }

        int maxAtomsRingA = ringA.getAtomCount();
        int maxAtomsRingB = ringB.getAtomCount();
        int maxAtomsRingC = ringC.getAtomCount();
        if (maxAtomsRingA!=5 || maxAtomsRingB!=6 || maxAtomsRingC!=6)
        {
            // Stop if not a 5-membered ring fused to 6-membered ring fused to another 6-membered ring.
            return false;
        }

        if (!isAllAtomsAromatic(ringA) || !isAllAtomsAromatic(ringB) || !isAllAtomsAromatic(ringC))
        {
            // Not all atoms of the three rings are aromatic.
            return false;
        }

        InvalidateBonds(ringA);
        InvalidateBonds(ringB);
        InvalidateBonds(ringC);

        // Find nH, nR, o or s in 5-membered ring if any.
        // If none are found, start from atom furthest away from fused bond.
        boolean hasStartAtom = false;
        IAtom startAtom = ringA.getAtom(0);
        for (int a=0; a<maxAtomsRingA; ++a)
        {
            IAtom atom = ringA.getAtom(a);
            if ((atom.getAtomicNumber()==7 && (atom.getImplicitHydrogenCount()>=1 || atom.getFormalNeighbourCount()==3)) ||    // nH or nR
                atom.getAtomicNumber()==8 ||    // o
                atom.getAtomicNumber()==16)     // s
            {
                startAtom = atom;
                hasStartAtom = true;
                break;
            }
        }

        if (!hasStartAtom)
        {
            for (int a=0; a<maxAtomsRingA; ++a)
            {
                IAtom atom = ringA.getAtom(a);
                if (ringB.contains(atom))
                {
                    // Current atom is part of fused bond
                    List<IAtom> neighbours = ringA.getConnectedAtomsList(atom);
                    for (IAtom neighbour : neighbours)
                    {
                        if (!ringB.contains(neighbour))
                        {
                            List<IAtom> neighbours1 = ringA.getConnectedAtomsList(neighbour);
                            for (IAtom neighbour1 : neighbours1)
                            {
                                if (neighbour1!=atom && ringA.contains(neighbour1))
                                {
                                    // Found start atom
                                    startAtom = neighbour1;
                                    hasStartAtom = true;
                                    break;
                                }
                            }
                        }
                        if (hasStartAtom) break;
                    }
                }
                if (hasStartAtom) break;
            }
        }

        // Change 5-membered ring.
        IBond startBond = ringA.getConnectedBondsList(startAtom).get(0);
        DearomatizeRing(ringA, startAtom, startBond);
        ConvertNtoNH(ringA);

        // Change second 6-membered ring.
        DearomatizeRing(ringB, null, null);

        // Change third 6-membered ring.
        DearomatizeRing(ringC, null, null);

        return true;
    }

    private static boolean Dearomatize66Rings(IRingSet rings)
    {
        if (rings.getAtomContainerCount()!=2)
        {
            // Stop if not two rings fused together.
            return false;
        }
        IAtomContainer ringA = rings.getAtomContainer(0);
        IAtomContainer ringB = rings.getAtomContainer(1);
        int maxAtomsRingA = ringA.getAtomCount();
        int maxAtomsRingB = ringB.getAtomCount();
        if (maxAtomsRingA!=6 || maxAtomsRingB!=6)
        {
            // Stop if not a 6-membered ring fused to 6-membered ring.
            return false;
        }

        if (!isAllAtomsAromatic(ringA) || !isAllAtomsAromatic(ringB))
        {
            // Not all atoms of the two rings are aromatic.
            return false;
        }

        InvalidateBonds(ringA);
        InvalidateBonds(ringB);

        // Change first 6-membered ring.
        DearomatizeRing(ringA, null, null);

        // Change second 6-membered ring.
        DearomatizeRing(ringB, null, null);

        return true;
    }

    private static boolean Dearomatize56Rings(IRingSet rings)
    {
        if (rings.getAtomContainerCount()!=2)
        {
            // Stop if not two rings fused together.
            return false;
        }
        IAtomContainer ringA = rings.getAtomContainer(0);
        IAtomContainer ringB = rings.getAtomContainer(1);
        if (ringA.getAtomCount() > ringB.getAtomCount())
        {
            // Ensure ringA is smaller than ringB
            ringA = rings.getAtomContainer(1);
            ringB = rings.getAtomContainer(0);
        }
        int maxAtomsRingA = ringA.getAtomCount();
        int maxAtomsRingB = ringB.getAtomCount();
        if (maxAtomsRingA!=5 || maxAtomsRingB!=6)
        {
            // Stop if not a 5-membered ring fused to 6-membered ring.
            return false;
        }

        if (!isAllAtomsAromatic(ringA) || !isAllAtomsAromatic(ringB))
        {
            // Not all atoms of the two rings are aromatic.
            return false;
        }

        InvalidateBonds(ringA);
        InvalidateBonds(ringB);

        // Find nH, nR, o or s in 5-membered ring if any.
        // If none are found, start from atom furthest away from fused bond.
        boolean hasStartAtom = false;
        IAtom startAtom = ringA.getAtom(0);
        for (int a=0; a<maxAtomsRingA; ++a)
        {
            IAtom atom = ringA.getAtom(a);
            if ((atom.getAtomicNumber()==7 && (atom.getImplicitHydrogenCount()>=1 || atom.getFormalNeighbourCount()==3)) ||    // nH or nR
                atom.getAtomicNumber()==8 ||    // o
                atom.getAtomicNumber()==16)     // s
            {
                startAtom = atom;
                hasStartAtom = true;                
                break;
            }
        }
        if (!hasStartAtom)
        {
            for (int a=0; a<maxAtomsRingA; ++a)
            {
                IAtom atom = ringA.getAtom(a);
                if (ringB.contains(atom))
                {
                    // Current atom is part of fused bond
                    List<IAtom> neighbours = ringA.getConnectedAtomsList(atom);
                    for (IAtom neighbour : neighbours)
                    {
                        if (!ringB.contains(neighbour))
                        {
                            List<IAtom> neighbours1 = ringA.getConnectedAtomsList(neighbour);
                            for (IAtom neighbour1 : neighbours1)
                            {
                                if (neighbour1!=atom && ringA.contains(neighbour1))
                                {
                                    // Found start atom
                                    startAtom = neighbour1;
                                    hasStartAtom = true;
                                    break;
                                }
                            }
                        }
                        if (hasStartAtom) break;
                    }
                }
                if (hasStartAtom) break;
            }
        }

        // Change 5-membered ring.
        IBond startBond = ringA.getConnectedBondsList(startAtom).get(0);
        DearomatizeRing(ringA, startAtom, startBond);
        ConvertNtoNH(ringA);

        // Change 6-membered ring.
        DearomatizeRing(ringB, null, null);

        return true;
    }

    private static boolean Dearomatize6Ring(IRingSet rings)
    {
        if (rings.getAtomContainerCount()!=1)
        {
            // Stop if not one ring.
            return false;
        }
        IAtomContainer ringA = rings.getAtomContainer(0);
        int maxAtomsRingA = ringA.getAtomCount();
        if (maxAtomsRingA!=6)
        {
            // Stop if not a 6-membered ring.
            return false;
        }

        if (!isAllAtomsAromatic(ringA))
        {
            // Not all atoms of the ring are aromatic.
            return false;
        }

        InvalidateBonds(ringA);

        // Change 6-membered ring.
        IAtom startAtom = ringA.getAtom(0);
        IBond startBond = ringA.getConnectedBondsList(startAtom).get(0);
        DearomatizeRing(ringA, startAtom, startBond);

        return true;
    }

    private static boolean Dearomatize5Ring(IRingSet rings)
    {
        if (rings.getAtomContainerCount()!=1)
        {
            // Stop if not one ring.
            return false;
        }
        IAtomContainer ringA = rings.getAtomContainer(0);
        int maxAtomsRingA = ringA.getAtomCount();
        if (maxAtomsRingA!=5)
        {
            // Stop if not a 5-membered ring.
            return false;
        }

        if (!isAllAtomsAromatic(ringA))
        {
            // Not all atoms of the ring are aromatic.
            return false;
        }

        InvalidateBonds(ringA);

        // Find nH, nR, o or s in 5-membered ring if any.
        IAtom startAtom = ringA.getAtom(0);
        for (int a=0; a<maxAtomsRingA; ++a)
        {
            IAtom atom = ringA.getAtom(a);
            if ((atom.getAtomicNumber()==7 && (atom.getImplicitHydrogenCount()>=1 || atom.getFormalNeighbourCount()==3)) ||    // nH or nR
                atom.getAtomicNumber()==8 ||    // o
                atom.getAtomicNumber()==16)     // s
            {
                startAtom = atom;
                break;
            }
        }

        // Change 5-membered ring.
        IBond startBond = ringA.getConnectedBondsList(startAtom).get(0);
        DearomatizeRing(ringA, startAtom, startBond);
        ConvertNtoNH(ringA);

        return true;
    }
    
    private static boolean DearomatizeAnyBenzeneRing(IRingSet rings)
    {
        boolean retValue = false;
        for (IAtomContainer ringA : rings.atomContainers())
        {
            int maxAtomsRingA = ringA.getAtomCount();
            if (maxAtomsRingA!=6)
            {
                // Stop if not a 6-membered ring.
                continue;
            }

            if (!isAllAtomsAromatic(ringA))
            {
                // Not all atoms of the ring are aromatic.
                continue;
            }
            
            boolean isAllC = true;
            for (IAtom a : ringA.atoms())
            {
                if (a.getAtomicNumber()!=6) 
                {
                    isAllC = false;
                    break;
                }
            }
            if (!isAllC)
            {
                // Not benzene ring.
                continue;
            }
            
            retValue = true;
                       
            InvalidateBonds(ringA);

            // Change 6-membered ring.
            IAtom startAtom = ringA.getAtom(0);
            IBond startBond = ringA.getConnectedBondsList(startAtom).get(0);
            DearomatizeRing(ringA, startAtom, startBond);
        }

        return retValue;
    }
    
    private static boolean DearomatizeAny5Ring(IRingSet rings)
    {
        boolean retValue = false;
        for (IAtomContainer ringA : rings.atomContainers())
        {
            int maxAtomsRingA = ringA.getAtomCount();
            if (maxAtomsRingA!=5)
            {
                // Stop if not a 5-membered ring.
                continue;
            }

            if (!isAllAtomsAromatic(ringA))
            {
                // Not all atoms of the ring are aromatic.
                continue;
            }
            
            retValue = true;

            InvalidateBonds(ringA);

            // Find nH, nR, o or s in 5-membered ring if any.
            IAtom startAtom = ringA.getAtom(0);
            for (int a=0; a<maxAtomsRingA; ++a)
            {
                IAtom atom = ringA.getAtom(a);
                if ((atom.getAtomicNumber()==7 && (atom.getImplicitHydrogenCount()>=1 || atom.getFormalNeighbourCount()==3)) ||    // nH or nR
                    atom.getAtomicNumber()==8 ||    // o
                    atom.getAtomicNumber()==16)     // s
                {
                    startAtom = atom;
                    break;
                }
            }

            // Change 5-membered ring.
            IBond startBond = ringA.getConnectedBondsList(startAtom).get(0);
            DearomatizeRing(ringA, startAtom, startBond);
            ConvertNtoNH(ringA);
        }

        return retValue;
    }

    private static boolean isAllAtomsAromatic(IAtomContainer ring)
    {
        for (IAtom atom : ring.atoms())
        {
            if (!atom.getFlag(CDKConstants.ISAROMATIC))
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isMiddleRing(IAtomContainer ring, IAtomContainer ringA, IAtomContainer ringB)
    {
        boolean hasAtomInRingA = false;
        boolean hasAtomInRingB = false;
        for (IAtom atom : ring.atoms())
        {
            if (!hasAtomInRingA && ringA.contains(atom)) hasAtomInRingA = true;
            else if (!hasAtomInRingB && ringB.contains(atom)) hasAtomInRingB = true;
        }
        return hasAtomInRingA && hasAtomInRingB;
    }

    private static void RemoveAromaticFlag(IAtomContainer ring)
    {
        for (IAtom atom : ring.atoms())
        {
            atom.setFlag(CDKConstants.ISAROMATIC, false);
        }
        for (IBond bond : ring.bonds())
        {
            bond.setFlag(CDKConstants.ISAROMATIC, false);
        }
    }

    private static void InvalidateBonds(IAtomContainer ring)
    {
        for (IBond bond : ring.bonds())
        {
            bond.setOrder(IBond.Order.QUADRUPLE);
        }
    }

    private static void DearomatizeRing(IAtomContainer ring, IAtom startAtom, IBond startBond)
    {
        IAtom curAtom = startAtom;
        IBond curBond = startBond;
        int maxBonds = ring.getBondCount();

        if (curAtom==null)
        {
            curAtom = ring.getAtom(0);
        }
        if (curBond==null)
        {
            curBond = ring.getConnectedBondsList(curAtom).get(0);
        }

        // Find an appropriate starting atom and starting bond
        // This will be run for subsequent fused rings and not for the first fused ring to be dearomatize.
        boolean nextIsSingle = true;
        boolean nextNextIsSingle = false;
        for (int i=0; i<maxBonds; ++i)
        {
            IBond bond = ring.getBond(i);
            if (bond.getOrder()!=IBond.Order.QUADRUPLE)
            {
                curBond = bond;
                curAtom = bond.getAtom(0);
                if (bond.getOrder()==IBond.Order.SINGLE)
                {
                    nextIsSingle = true;
                    nextNextIsSingle = true;
                }
                else
                {
                    nextIsSingle = false;
                    nextNextIsSingle = true;
                }
            }
        }

        for (int i=0; i<maxBonds; ++i)
        {
            if (nextIsSingle || (i==1 && nextNextIsSingle))
            {
                curBond.setOrder(IBond.Order.SINGLE);
                nextIsSingle = false;
            }
            else
            {
                curBond.setOrder(IBond.Order.DOUBLE);
                nextIsSingle = true;
            }
            curAtom = curBond.getConnectedAtom(curAtom);
            List<IBond> bonds = ring.getConnectedBondsList(curAtom);
            for (IBond bond : bonds)
            {
                if (bond!=curBond)
                {
                    curBond = bond;
                    break;
                }
            }
        }

        RemoveAromaticFlag(ring);
    }

    // Change N to sp3 if there is a sp2 N initially in a 5-member ring.
    // This is because after dearomatization, it will become a N with two single bond
    private static void ConvertNtoNH(IAtomContainer ring)
    {
        int maxAtomsRing = ring.getAtomCount();
        for (int a=0; a<maxAtomsRing; ++a)
        {
            IAtom atom = ring.getAtom(a);
            if (atom.getAtomicNumber()==7 && atom.getHybridization()==Hybridization.SP2)
            {
                List<IBond> bonds = ring.getConnectedBondsList(atom);
                boolean isAllSingleBond = true;
                for (IBond bond : bonds)
                {
                    if (bond.getOrder()!=IBond.Order.SINGLE)
                    {
                        isAllSingleBond = false;
                        break;
                    }
                }
                if (isAllSingleBond)
                {
                    atom.setHybridization(Hybridization.PLANAR3);
                    atom.setFormalNeighbourCount(3);
                    atom.setImplicitHydrogenCount(1);
                    atom.setAtomTypeName("N.planar3");
                }
            }
        }
    }
}
