///////////////////////////////////////////////////////////////////////////////
//  Filename: $RCSfile: BasicAromaticityTyper.java,v $
//  Purpose:  Aromatic typer.
//  Language: Java
//  Compiler: JDK 1.4
//  Authors:  Joerg Kurt Wegner
//  Version:  $Revision: 1.7 $
//            $Date: 2005/03/03 07:13:36 $
//            $Author: wegner $
//  Original Author: ???, OpenEye Scientific Software
//  Original Version: babel 2.0a1
//
// Copyright OELIB:          OpenEye Scientific Software, Santa Fe,
//                           U.S.A., 1999,2000,2001
// Copyright JOELIB/JOELib2: Dept. Computer Architecture, University of
//                           Tuebingen, Germany, 2001,2002,2003,2004,2005
// Copyright JOELIB/JOELib2: ALTANA PHARMA AG, Konstanz, Germany,
//                           2003,2004,2005
//
//  This program is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation version 2 of the License.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
///////////////////////////////////////////////////////////////////////////////
package libpadeldescriptor;

import java.util.ArrayList;
import java.util.List;
import org.openscience.cdk1.Atom;
import org.openscience.cdk1.Bond;
import org.openscience.cdk1.CDKConstants;
import org.openscience.cdk1.Molecule;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IAtomType.Hybridization;
import org.openscience.cdk1.interfaces.IBond;
import org.openscience.cdk1.interfaces.IRingSet;
import org.openscience.cdk1.ringsearch.SSSRFinder;
import org.openscience.cdk1.smiles.smarts.SMARTSQueryTool;




/**
 *  Aromatic typer.
 * The definition file can be defined in the
 * <tt>joelib2.data.JOEAromaticTyper.resourceFile</tt> property in the {@link wsi.ra.tool.BasicPropertyHolder}.
 * The {@link wsi.ra.tool.BasicResourceLoader} loads the <tt>joelib2.properties</tt> file for default.
 *
 * <p>
 * Default:<br>
 * joelib2.data.JOEAromaticTyper.resourceFile=<a href="http://cvs.sourceforge.net/cgi-bin/viewcvs.cgi/joelib/joelib/src/joelib2/data/plain/aromatic.txt?rev=HEAD&content-type=text/vnd.viewcvs-markup">joelib2/data/plain/aromatic.txt</a>
 *
 * @.author     wegnerj
 * @.wikipedia Aromaticity
 * @.wikipedia Friedrich August Kekulé von Stradonitz
 * @.wikipedia Molecule
 * @.license GPL
 * @.cvsversion    $Revision: 1.7 $, $Date: 2005/03/03 07:13:36 $
 * @see wsi.ra.tool.BasicPropertyHolder
 * @see wsi.ra.tool.BasicResourceLoader
 */
public class JOELib2BasicAromaticityTyper 
{
    private final static boolean DEFAULT_AVOID_INNER_RING_FLAG = true;    
    private boolean[] isRoot;
    private boolean[] isVisited;
    
    private static final String[] smarts = {
        "[#6]",
        "[#6-]",
        "[#7]",
        "[#7]=*",
        "[#7+]",
        "[#7]=O",
        "[#7-]",
        "[#8]",
        "[#8+]",
        "[#16]",
        "[#16+]",
        "[#15]",
        "[#34]"
    };

    private static final int[] minElectrons = {
        1,
        2,
        1,
        1,
        1,
        1,
        2,
        2,
        1,
        2,
        1,
        2,
        2
    };
    
    private static final int[] maxElectrons = {
        1,
        2,
        2,
        1,
        1,
        1,
        2,
        2,
        1,
        2,
        1,
        2,
        2
    };    
    
    private int[][] numberOfElectrons;
    
    
    /**
     * Potentially aromatic atoms.
     */
    private boolean[] potentiallyAromatic;

    //~ Constructors ///////////////////////////////////////////////////////////

    /**
     * Initializes the aromatic typer factory class.
     */
    public JOELib2BasicAromaticityTyper()
    {
        potentiallyAromatic = null;
        isVisited = null;
        isRoot = null;
        numberOfElectrons = null;
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    /**
     * Assign the aromaticity flag to atoms and bonds.
     *
     * 3 rings will be excluded.
     * Please remember that the aromaticity typer JOEAromaticTyper.assignAromaticFlags(Molecule)
     * assign ONLY aromaticity flags and NOT the internal aromatic bond order Bond.JOE_AROMATIC_BOND_ORDER.
     *
     * @param  mol  the molecule
     */
    public void assignAromaticFlags(IAtomContainer mol)
    {
        int size = mol.getAtomCount();
        
        potentiallyAromatic = new boolean[size];
        isRoot = new boolean[size];
        isVisited = new boolean[size];
        
        numberOfElectrons = new int [size][2];

        markPotentiallyAromatic(mol);

//        printPotentialAromatic(mol, "Marked ");
        //sanity check - exclude all 4 substituted atoms and sp centers
        sanityCheck(mol);

//        printPotentialAromatic(mol, "Sanity ");
        //propagate potentially aromatic atoms
        propagatePotArom(mol);

//        printPotentialAromatic(mol, "Propagate ");
        // select root atom
        selectRootAtoms(mol, DEFAULT_AVOID_INNER_RING_FLAG);

        //remove 3 membered rings from consideration
        excludeSmallRing(mol);

        int bondsSize = mol.getBondCount();
        boolean[] aromBondsArr = new boolean[bondsSize];
        int atomsSize = mol.getAtomCount();
        boolean[] aromAtomsArr = new boolean[atomsSize];

        //loop over root atoms and look for 5-6 membered aromatic rings
        checkAromaticity(mol, aromAtomsArr, aromBondsArr);
        
        for (int i=0, endi=atomsSize; i<endi; ++i)
        {
            if (aromAtomsArr[i])
            {
                mol.getAtom(i).setFlag(CDKConstants.ISAROMATIC, true);
            }
        }
        
        for (int i=0, endi=bondsSize; i<endi; ++i)
        {
            if (aromBondsArr[i])
            {
                mol.getBond(i).setFlag(CDKConstants.ISAROMATIC, true);
            }
        }
    }


    /**
     * @param mol
     */
    private void checkAromaticity(IAtomContainer mol, boolean[] aromAtoms, boolean[] aromBonds)
    {
        for (int atomIdx=0, endatomIdx=mol.getAtomCount(); atomIdx<endatomIdx; ++atomIdx)
        {
            IAtom atom = mol.getAtom(atomIdx);

            // check only for root atoms
            if (isRoot[atomIdx])
            {
                checkAromaticity(mol, atom, 6, aromAtoms, aromBonds);
            }
        }

        for (int atomIdx=0, endatomIdx=mol.getAtomCount(); atomIdx<endatomIdx; ++atomIdx)
        {
            IAtom atom = mol.getAtom(atomIdx);

            // check only for root atoms
            if (isRoot[atomIdx])
            {
                checkAromaticity(mol, atom, 20, aromAtoms, aromBonds);
            }
        }
    }

    /**
     * Check aromaticity starting from the root atom.
     *
     * @param  atom   the root atom
     * @param  depth  the search depth, e.g. 6 or 20 for typical aromatic systems
     *
     * @see @see #selectRootAtoms(Molecule, boolean}
     */
    private void checkAromaticity(IAtomContainer mol, IAtom atom, int depth, boolean[] aromAtoms, boolean[] aromBonds)
    {
        List<IAtom> neighbours = mol.getConnectedAtomsList(atom);
        for (IAtom nbr : neighbours)
        {
            IBond bond = mol.getBond(atom, nbr);

            if (atom.getFlag(CDKConstants.ISINRING) && nbr.getFlag(CDKConstants.ISINRING) && !aromBonds[mol.getBondNumber(bond)])
            {
                int[] erange = numberOfElectrons[mol.getAtomNumber(atom)];

                if (traverseCycle(mol, atom, nbr, bond, erange, depth - 1, aromAtoms, aromBonds))
                {
                    aromAtoms[mol.getAtomNumber(atom)] = true;
                    aromBonds[mol.getBondNumber(bond)] = true;
                }
            }
        }
    }

    /**
     * Remove 3 membered rings from consideration.
     *
     * @param  mol  the molecule
     */
    private void excludeSmallRing(IAtomContainer mol)
    {
        for (int atomIdx=0, endatomIdx=mol.getAtomCount(); atomIdx<endatomIdx; ++atomIdx)
        {
            IAtom atom = mol.getAtom(atomIdx);

            if (isRoot[atomIdx])
            {
                List<IAtom> neighbours1 = mol.getConnectedAtomsList(atom);
                for (IAtom nbr1 : neighbours1)
                {
                    IBond bond = mol.getBond(atom, nbr1);
                    if (atom.getFlag(CDKConstants.ISINRING) && nbr1.getFlag(CDKConstants.ISINRING) && potentiallyAromatic[mol.getAtomNumber(nbr1)])
                    {
                        List<IAtom> neighbours2 = mol.getConnectedAtomsList(nbr1);
                        for (IAtom nbr2 : neighbours2)
                        {
                            if ((nbr2 != atom) && nbr2.getFlag(CDKConstants.ISINRING) && potentiallyAromatic[mol.getAtomNumber(nbr2)])
                            {
                                if (mol.getBond(atom, nbr2)!=null)
                                {
                                    isRoot[atomIdx] = false;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
            

    /**
     * @param potentiallyAromatic2
     */
    private void markPotentiallyAromatic(IAtomContainer mol)
    {
        try
        {
            //mark atoms as potentially aromatic
            SMARTSQueryTool sqt = new SMARTSQueryTool("C");
            for (int idx=0, k=0; k<smarts.length; ++k, ++idx)
            {
                String pattern = smarts[k];
                sqt.setSmarts(pattern);
                if (sqt.matches(mol))
                {
                    List<List<Integer> > matchList = sqt.getUniqueMatchingAtoms();
                    for (int m=0; m<matchList.size(); ++m)
                    {
                        List<Integer> itmp = matchList.get(m);
                        for (Integer atomIdx : itmp)
                        {
                            potentiallyAromatic[atomIdx] = true;
                            numberOfElectrons[atomIdx][0] = minElectrons[idx];
                            numberOfElectrons[atomIdx][1] = maxElectrons[idx];
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            
        }    
    }

    /**
     * @param string
     */
    private void printPotentialAromatic(IAtomContainer mol, String string)
    {
        for (int i = 0; i < potentiallyAromatic.length; i++)
        {
            System.out.println(string + " pot. aromatic (" + i + "_" + mol.getAtom(i).getAtomTypeName() + ")=" + potentiallyAromatic[i]);
        }
    }

    /**
     * @param mol
     */
    private void propagatePotArom(IAtomContainer mol)
    {
        for (int atomIdx=0, endatomIdx=mol.getAtomCount(); atomIdx<endatomIdx; ++atomIdx)
        {
            IAtom atom = mol.getAtom(atomIdx);
            
            if (potentiallyAromatic[atomIdx])
            {
                propagatePotArom(mol, atom);
            }
        }
    }

    /**
     *  Description of the Method
     *
     * @param  atom  Description of the Parameter
     */
    private void propagatePotArom(IAtomContainer mol, IAtom atom)
    {
        int count = 0;

        // count potentially aromatic neighbour atoms of
        // this atoms which are included in a ring
        List<IAtom> neighbours = mol.getConnectedAtomsList(atom);
        for (IAtom nbr : neighbours)
        {
            IBond bond = mol.getBond(atom, nbr);
            
            if (atom.getFlag(CDKConstants.ISINRING) && nbr.getFlag(CDKConstants.ISINRING) && potentiallyAromatic[mol.getAtomNumber(nbr)])
            {
                ++count;
            }
        }
        
        if (count < 2)
        {
            potentiallyAromatic[mol.getAtomNumber(atom)] = false;

            if (count == 1)
            {
                for (IAtom nbr : neighbours)
                {
                    if (atom.getFlag(CDKConstants.ISINRING) && nbr.getFlag(CDKConstants.ISINRING) && potentiallyAromatic[mol.getAtomNumber(nbr)])
                    {
                        propagatePotArom(mol, nbr);
                    }
                }
            }
        }
    }

    /**
     * @param mol
     * @param potentiallyAromatic2
     */
    private void sanityCheck(IAtomContainer mol)
    {
        for (int atomIdx=0, endatomIdx=mol.getAtomCount(); atomIdx<endatomIdx; ++atomIdx)
        {
            IAtom atom = mol.getAtom(atomIdx);

            if (atom.getFormalNeighbourCount() > 3)
            {
                potentiallyAromatic[atomIdx] = false;

                continue;
            }

            switch (atom.getAtomicNumber())
            {
                //phosphorus and sulfur may be initially typed as sp3
                case 6:

                    if (atom.getHybridization()!=Hybridization.SP2)
                    {
                        potentiallyAromatic[atomIdx] = false;
                    }

                    break;
            }
        }
    }

    /**
     * Select the root atoms for traversing atoms in rings.
     *
     * Picking only the begin atom of a closure bond can cause
     * difficulties when the selected atom is an inner atom
     * with three neighbour ring atoms. Why ? Because this atom
     * can get trapped by the other atoms when determining aromaticity,
     * because a simple visited flag is used in the
     * {@link #traverseCycle(Atom, Atom, Bond, IntInt, int)}
     *
     * @param mol the molecule
     * @param avoidInnerRingAtoms inner closure ring atoms with more than 2 neighbours will be avoided
     *
     * @see #traverseCycle(Atom, Atom, Bond, IntInt, int)
     */
    private void selectRootAtoms(IAtomContainer mol, boolean avoidInnerRingAtoms)
    {
        // Find Smallest Set of Smallest Rings.
        SSSRFinder finder = new SSSRFinder(mol);
        IRingSet sssRings = finder.findEssentialRings();
       
        IAtom rootAtom;
        IAtom newRoot = null;
        ArrayList<IAtom> tmpRootAtoms = new ArrayList<IAtom>();
        
        // PaDEL: Pick the first atom of every sssRing as part of closure bond?
        for (int i=0, endi=sssRings.getAtomContainerCount(); i<endi; ++i)
        {
            IAtomContainer ring = sssRings.getAtomContainer(i);
            rootAtom = ring.getFirstAtom();
            tmpRootAtoms.add(ring.getFirstAtom());
        }

        for (int i=0, endi=sssRings.getAtomContainerCount(); i<endi; ++i)
        {
            // BASIC APPROACH
            // pick begin atom at closure bond
            // this is really greedy, isn't it !;-)
            rootAtom = sssRings.getAtomContainer(i).getFirstAtom();
            isRoot[mol.getAtomNumber(rootAtom)] = true;

            // EXTENDED APPROACH
            if (avoidInnerRingAtoms)
            {
                // count the number of neighbour ring atoms
                List<IAtom> neighbours = mol.getConnectedAtomsList(rootAtom);
                int ringNbrs = 0;
                int heavyNbrs = 0;
                for (IAtom nbrAtom : neighbours)
                {
                    if (!nbrAtom.getSymbol().equals("H"))
                    {
                        ++heavyNbrs;

                        if (nbrAtom.getFlag(CDKConstants.ISINRING))
                        {
                            ++ringNbrs;
                        }
                    }
                }

                // if this atom has more than 2 neighbour
                // ring atoms, we could get trapped later
                // when traversing the cycles, which
                // can cause aromaticity false detection
                newRoot = null;

                if (ringNbrs > 2)
                {
                    //try to find an other root atom
                    for (int r=0; r<sssRings.getAtomContainerCount(); ++r)
                    {
                        IAtomContainer ring = sssRings.getAtomContainer(r);

                        boolean checkThisRing = false;
                        int rootAtomNumber = 0;

                        // avoiding two root atoms in one ring !
                        for (int rootIdx=0; rootIdx<tmpRootAtoms.size(); ++rootIdx)
                        {
                            IAtom idx = tmpRootAtoms.get(rootIdx);

                            if (ring.getAtomNumber(idx)!=-1)
                            {
                                ++rootAtomNumber;

                                if (rootAtomNumber >= 2)
                                {
                                    break;
                                }
                            }
                        }

                        if (rootAtomNumber < 2)
                        {
                            for (int ringAtomIdx=0; ringAtomIdx<ring.getAtomCount(); ++ringAtomIdx)
                            {
                                // find critical ring
                                if (ring.getAtom(i) == rootAtom)
                                {
                                    checkThisRing = true;
                                }
                                else
                                {
                                    // second root atom in this ring ?
                                    if (isRoot[mol.getAtomNumber(ring.getAtom(i))] == true)
                                    {
                                        // when there is a second root
                                        // atom this ring can not be
                                        // used for getting an other
                                        // root atom
                                        checkThisRing = false;

                                        break;
                                    }
                                }
                            }
                        }

                        // check ring for getting an other
                        // root atom to aromaticity typer avoid
                        // problems
                        if (checkThisRing)
                        {
                            // check if we can find another root
                            // atom
                            for (int ringAtomIdx=0; ringAtomIdx<ring.getAtomCount(); ++ringAtomIdx)
                            {
                                neighbours = mol.getConnectedAtomsList(ring.getAtom(ringAtomIdx));
                                ringNbrs = heavyNbrs = 0;
                                for (IAtom nbrAtom : neighbours)
                                { 
                                    if (!nbrAtom.getSymbol().equals("H"))
                                    {
                                        ++heavyNbrs;

                                        if (nbrAtom.getFlag(CDKConstants.ISINRING))
                                        {
                                            ++ringNbrs;
                                        }
                                    }
                                }

                                // if the number of neighboured heavy atoms is also
                                // the number of neighboured ring atoms, the aromaticity
                                // typer could be stuck in a local traversing trap
                                if ((ringNbrs<=2) && ring.getAtom(ringAtomIdx).getFlag(CDKConstants.ISINRING))
                                {
                                    newRoot = ring.getAtom(ringAtomIdx);
                                }
                            }
                        }
                    }

                    if ((newRoot != null) && (rootAtom != newRoot))
                    {
                        // unset root atom
                        isRoot[mol.getAtomNumber(rootAtom)] = false;

                        // pick new root atom
                        isRoot[mol.getAtomNumber(newRoot)] = true;
                    }
                }
            }
        }
    }

    /**
     * Traverse cycles to assign aromaticity flags to the atoms and bonds starting from a root atom.
     *
     * It's important that the root atoms are not trapped by further neighboured ring atoms.
     * See {@link #selectRootAtoms(Molecule, boolean}} for details.
     *
     * @param  root   the root atom from which we will start
     * @param  atom   the actual atom which will be checked
     * @param  prev   previous atom
     * @param  electrons     minimal and maximal number of electrons
     * @param  depth  depth of the search, e.g. 6 or 20 for typical aromatic systems
     * @return        <tt>true</tt> if the actual visited atom is aromatic
     *
     * @see #selectRootAtoms(Molecule, boolean}
     */
    private boolean traverseCycle(IAtomContainer mol, IAtom root, IAtom atom, IBond prev, int[] electrons, int depth, boolean[] aromAtoms, boolean[] aromBonds)
    {
        int atomIdx = mol.getAtomNumber(atom);
        if (atom == root)
        {
            for (int elNum=electrons[0]; elNum<=electrons[1]; ++elNum)
            {
                if (((elNum % 4) == 2) && (elNum > 2))
                {
                    return true;
                }
            }

            return false;
        }

        if ((depth == 0) || !potentiallyAromatic[atomIdx] || isVisited[atomIdx])
        {
            return false;
        }

        boolean result = false;
        --depth;
        electrons[0] += numberOfElectrons[atomIdx][0];
        electrons[1] += numberOfElectrons[atomIdx][1];
        isVisited[atomIdx] = true;
          
        List<IAtom> neighbours = mol.getConnectedAtomsList(atom);
        for (IAtom nbr : neighbours)            
        {
            IBond bond = mol.getBond(atom, nbr);

            if ((bond != prev) && atom.getFlag(CDKConstants.ISINRING) && nbr.getFlag(CDKConstants.ISINRING) && potentiallyAromatic[mol.getAtomNumber(nbr)])
            {
                if (traverseCycle(mol, root, nbr, bond, electrons, depth, aromAtoms, aromBonds))
                {
                    result = true;
                    aromBonds[mol.getBondNumber(bond)] = true;
                }
            }
        }

        isVisited[atomIdx] = false;

        if (result)
        {
            aromAtoms[atomIdx] = true;
        }

        electrons[0] -= numberOfElectrons[atomIdx][0];
        electrons[1] -= numberOfElectrons[atomIdx][1];

        return result;
    }
}

///////////////////////////////////////////////////////////////////////////////
//  END OF FILE.
///////////////////////////////////////////////////////////////////////////////
