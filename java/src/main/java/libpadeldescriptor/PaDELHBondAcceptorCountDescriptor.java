/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 2004-2007  The Chemistry Development Kit (CDK) project
 *
 *  Contact: cdk-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package libpadeldescriptor;

import org.openscience.cdk1.CDKConstants;
import org.openscience.cdk1.annotations.TestMethod;
import org.openscience.cdk1.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IBond.Order;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.qsar.result.IntegerArrayResult;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;

/**
 * This descriptor calculates the number of hydrogen bond acceptors using various algorithms.
 *
 * CDK
 * Slightly simplified version of the <a href="http://www.chemie.uni-erlangen.de/model2001/abstracts/rester.html">PHACIR atom types</a>.
 * The following groups are counted as hydrogen bond acceptors:
 * <ul>
 * <li>any oxygen where the formal charge of the oxygen is non-positive (i.e. formal charge <= 0) <b>except</b></li>
 * <ol>
 * <li>an aromatic ether oxygen (i.e. an ether oxygen that is adjacent to at least one aromatic carbon)</li>
 * <li>an oxygen that is adjacent to a nitrogen</li>
 * </ol>
 * <li>any nitrogen where the formal charge of the nitrogen is non-positive (i.e. formal charge <= 0) <b>except</b></li>
 * <ol>
 * <li>a nitrogen that is adjacent to an oxygen</li>
 * </ol>
 * </ul>
 *
 * nHBAcc2
 * The following groups are counted as hydrogen bond acceptors:
 * <ul>
 * <li>any oxygen</li>
 * <li>any nitrogen where the formal charge of the nitrogen is non-positive (i.e. formal charge <= 0) <b>except</b></li>
 * <ol>
 * <li>a non-aromatic nitrogen that is adjacent to an oxygen and aromatic ring</li>
 * <li>an aromatic nitrogen with a hydrogen atom in a ring</li>
 * <li>an aromatic nitrogen with 3 neighouring atoms in a ring</li>
 * <li>a nitrogen with total bond order >=4</li>
 * </ol>
 * <li>any fluorine where the formal charge of the fluorine is non-positive (i.e. formal charge <= 0)</li>
 * </ul>
 *
 * nHBAcc3
 * The following groups are counted as hydrogen bond acceptors:
 * <ul>
 * <li>any oxygen</li>
 * <li>any nitrogen where the formal charge of the nitrogen is non-positive (i.e. formal charge <= 0) <b>except</b></li>
 * <ol>
 * <li>a non-aromatic nitrogen that is adjacent to an oxygen and aromatic ring</li>
 * <li>an aromatic nitrogen with a hydrogen atom in a ring</li>
 * <li>an aromatic nitrogen with 3 neighouring atoms in a ring</li>
 * <li>a nitrogen with total bond order >=4</li>
 * <li>a nitrogen in an amide bond</li>
 * </ol>
 * <li>any fluorine where the formal charge of the fluorine is non-positive (i.e. formal charge <= 0)</li>
 * </ul>
 *
 * Lipinski
 * Lipinski CA, Lombardo F, Dominy BW, Feeney PJ: Experimental and computational approaches to estimate solubility and permeability in drug discovery and development settings. Adv Drug Deliv Rev 1997, 23:3-25.
 * The following groups are counted as hydrogen bond acceptors:
 * <ul>
 * <li>Any nitrogen</li>
 * <li>Any oxygen</li>
 * </ul>
 *
 * Hugo Kubinyi
 * <ul>
 * <li>Any nitrogen</li>
 * <li>Any oxygen</li>
 * </ul>
 *
 * Returns <i>nHBAcc</i>, <i>nHBAcc2</i>, <i>nHBAcc3</i>, <i>nHBAcc_Lipinski</i>.
 *
 * <p>This descriptor uses these parameters:
 * <table>
 *   <tr>
 *     <td>Name</td>
 *     <td>Default</td>
 *     <td>Description</td>
 *   </tr>
 *   <tr>
 *     <td>checkAromaticity</td>
 *     <td>false</td>
 *     <td>true if the aromaticity has to be checked</td>
 *   </tr>
 * </table>
 * <p>
 * This descriptor works properly with AtomContainers whose atoms contain <b>implicit hydrogens</b> or <b>explicit
 * hydrogens</b>.
 */
public class PaDELHBondAcceptorCountDescriptor implements IMolecularDescriptor {
    // only parameter of this descriptor; true if aromaticity has to be checked prior to descriptor calculation, false otherwise
    private boolean checkAromaticity = false;
    private static final String[] names = {"nHBAcc", "nHBAcc2", "nHBAcc3", "nHBAcc_Lipinski"};

    /**
     *  Constructor for the PaDELHBondAcceptorCountDescriptor object
     */
    public PaDELHBondAcceptorCountDescriptor() { }

    /**
     * Gets the specification attribute of the PaDELHBondAcceptorCountDescriptor object.
     *
     * @return    The specification value
     */
    @TestMethod("testGetSpecification")
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
            "PaDELHBondAcceptorCountDescriptor",
            this.getClass().getName(),
            "$Id$",
            "The Chemistry Development Kit");
    }

    /**
     * Sets the parameters attribute of the PaDELHBondAcceptorCountDescriptor object.
     *
     * @param  params            a boolean true means that aromaticity has to be checked
     * @exception  CDKException  Description of the Exception
     */
    @TestMethod("testSetParameters_arrayObject")
    public void setParameters(Object[] params) throws CDKException {
        if (params.length != 1) {
            throw new CDKException("PaDELHBondAcceptorCountDescriptor expects a single parameter");
        }
        if (!(params[0] instanceof Boolean)) {
            throw new CDKException("The parameter must be of type Boolean");
        }
        // ok, all should be fine
        checkAromaticity = (Boolean) params[0];
    }

    /**
     * Gets the parameters attribute of the PaDELHBondAcceptorCountDescriptor object.
     *
     * @return    The parameters value
     */
    @TestMethod("testGetParameters")
    public Object[] getParameters() {
        // return the parameters as used for the descriptor calculation
        Object[] params = new Object[1];
        params[0] = checkAromaticity;
        return params;
    }

    @TestMethod(value="testNamesConsistency")
    public String[] getDescriptorNames() {
        return names;
    }

    private DescriptorValue getDummyDescriptorValue(Exception e) {
        IntegerArrayResult retval = new IntegerArrayResult(getDescriptorNames().length);
        for (int i=0, endi=getDescriptorNames().length; i<endi; ++i) retval.add((int) Double.NaN);
        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, getDescriptorNames(), e);
    }

    /**
     *  Calculates the number of H bond acceptors.
     *
     * @param  atomContainer             AtomContainer
     * @return                   number of H bond acceptors     
     */
    @TestMethod("testCalculate_IAtomContainer")
    public DescriptorValue calculate(IAtomContainer atomContainer) {
        int nHBAcc_CDK = 0;
        int nHBAcc_2 = 0;
        int nHBAcc_3 = 0;
        int nHBAcc_Lipinski = 0;
        
        IAtomContainer ac;
        try {
            ac = (IAtomContainer) atomContainer.clone();
        } catch (CloneNotSupportedException e) {
            return getDummyDescriptorValue(e);
        }

        // aromaticity is detected prior to descriptor calculation if the respective parameter is set to true

        if (checkAromaticity) {
            try {
                AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(ac);
                CDKHueckelAromaticityDetector.detectAromaticity(ac);
            } catch (CDKException e) {
                return getDummyDescriptorValue(e);
            }
        }

        //org.openscience.cdk1.interfaces.IAtom[] atoms = ac.getAtoms();
        // labelled for loop to allow for labelled continue statements within the loop
        for (int atomIndex = 0; atomIndex < ac.getAtomCount(); atomIndex++)
        {
            IAtom atom = (IAtom) ac.getAtom(atomIndex);
            java.util.List neighbours = ac.getConnectedAtomsList(atom);
            // CDK, nHBAcc2, nHBAcc3, Lipinski: looking for suitable nitrogen atoms
            if (atom.getSymbol().equals("N"))
            {
                ++nHBAcc_Lipinski;
                
                boolean isHBAcc_CDK = true;
                boolean isHBAcc_2 = true;
                boolean isHBAcc_3 = true;

                if (atom.getFormalCharge() > 0)
                {
                    isHBAcc_CDK = false;
                    isHBAcc_2 = false;
                    isHBAcc_3 = false;
                }

                if (isHBAcc_2 || isHBAcc_3)
                {
                    // nHBAcc2, nHBAcc3: excluding aromatic nitrogens in a ring with (i) a hydrogen atom or (ii) three neighbours                    
                    if (atom.getFlag(CDKConstants.ISAROMATIC) && atom.getFlag(CDKConstants.ISINRING))
                    {
                        Integer implicitH = atom.getImplicitHydrogenCount();
                        if (implicitH == CDKConstants.UNSET) implicitH = 0;
                        if (implicitH > 0)
                        {
                            isHBAcc_2 = false; // we skip the explicit hydrogens part cause we found implicit hydrogens
                            isHBAcc_3 = false; // we skip the explicit hydrogens part cause we found implicit hydrogens
                        }

                        if (neighbours.size()>=3)
                        {
                            isHBAcc_2 = false;
                            isHBAcc_3 = false;
                        }

                        if (isHBAcc_2 || isHBAcc_3)
                        {
                            for (Object neighbour : neighbours)
                            {
                                if (((IAtom) neighbour).getSymbol().equals("H"))
                                {
                                    isHBAcc_2 = false;
                                    isHBAcc_3 = false;
                                    break;
                                }
                            }
                        }
                    }
                }

                if (isHBAcc_2 || isHBAcc_3)
                {
                    // nHBAcc2, nHBAcc3: excluding nitrogens with total bond order >=4                  
                    int totalBondOrder = 0;
                    for (Object neighbour : neighbours)
                    {
                        Order bondOrder = ac.getBond(atom, (IAtom) neighbour).getOrder();
                        if (bondOrder==Order.SINGLE) totalBondOrder += 1;
                        else if (bondOrder==Order.DOUBLE) totalBondOrder += 2;
                        else if (bondOrder==Order.TRIPLE) totalBondOrder += 3;
                        else if (bondOrder==Order.QUADRUPLE) totalBondOrder += 4;
                    }
                    if (totalBondOrder>=4)
                    {
                        isHBAcc_2 = false;
                        isHBAcc_3 = false;
                    }
                }

                if (isHBAcc_3)
                {
                    // nHBAcc3: excluding nitrogens in an amide bond.
                    for (Object neighbour : neighbours)
                    {
                        if (((IAtom) neighbour).getSymbol().equals("C"))
                        {
                            java.util.List CNeighbours = ac.getConnectedAtomsList((IAtom) neighbour);
                            for (Object CNeighbour : CNeighbours)
                            {
                                if (((IAtom) CNeighbour).getSymbol().equals("O") &&
                                    ac.getBond((IAtom) neighbour, (IAtom) CNeighbour).getOrder()==Order.DOUBLE)
                                {
                                    isHBAcc_3 = false;
                                    break;
                                }
                            }
                            if (!isHBAcc_3) break;
                        }
                    }
                }

                // CDK: excluding nitrogens that are adjacent to an oxygen
                // nHBAcc2, nHBAcc3: excluding non-aromatic nitrogens that are adjacent to an oxygen and to an aromatic atom
                if (isHBAcc_CDK || isHBAcc_2 || isHBAcc_3)
                {
                    boolean nextOxygen = false;
                    boolean nextAromaticAtom = false;
                    for (Object neighbour : neighbours)
                    {
                        if (((IAtom) neighbour).getSymbol().equals("O"))
                        {
                            nextOxygen = true;
                        }
                        if (((IAtom) neighbour).getFlag(CDKConstants.ISAROMATIC))
                        {
                            nextAromaticAtom = true;
                        }
                        if (nextOxygen)
                        {
                            isHBAcc_CDK = false;
                            if (nextAromaticAtom && !atom.getFlag(CDKConstants.ISAROMATIC))
                            {
                                isHBAcc_2 = false;
                                isHBAcc_3 = false;
                                break;
                            }
                        }
                    }                    
                }
                
                if (isHBAcc_CDK) ++nHBAcc_CDK;
                if (isHBAcc_2) ++nHBAcc_2;
                if (isHBAcc_3) ++nHBAcc_3;
            }

            // CDK, nHBAcc2, nHBAcc3, Lipinski: looking for suitable oxygen atoms
            if (atom.getSymbol().equals("O"))
            {
                ++nHBAcc_Lipinski;
                ++nHBAcc_2;
                ++nHBAcc_3;

                // CDK: excluding oxygens that are adjacent to a nitrogen or to an aromatic carbon
                boolean isHBAcc_CDK = true;
                if (atom.getFormalCharge() > 0)
                {
                    isHBAcc_CDK = false;
                }
                if (isHBAcc_CDK)
                {
                    for (Object neighbour : neighbours)
                    {
                        if (((IAtom) neighbour).getSymbol().equals("N") ||
                            (((IAtom) neighbour).getSymbol().equals("C") && ((IAtom) neighbour).getFlag(CDKConstants.ISAROMATIC)))
                        {
                            isHBAcc_CDK = false;
                            break;
                        }
                    }
                }
                if (isHBAcc_CDK) ++nHBAcc_CDK;
            }

            // nHBAcc2: looking for suitable fluorine atoms
            if (atom.getSymbol().equals("F"))
            {
                ++nHBAcc_2;
                ++nHBAcc_3;
            }
        }

        IntegerArrayResult retval = new IntegerArrayResult();
        retval.add(nHBAcc_CDK);
        retval.add(nHBAcc_2);
        retval.add(nHBAcc_3);
        retval.add(nHBAcc_Lipinski);

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, getDescriptorNames());
    }

    /**
     * Returns the specific type of the DescriptorResult object.
     * <p/>
     * The return value from this method really indicates what type of result will
     * be obtained from the {@link org.openscience.cdk1.qsar.DescriptorValue} object. Note that the same result
     * can be achieved by interrogating the {@link org.openscience.cdk1.qsar.DescriptorValue} object; this method
     * allows you to do the same thing, without actually calculating the descriptor.
     *
     * @return an object that implements the {@link org.openscience.cdk1.qsar.result.IDescriptorResult} interface indicating
     *         the actual type of values returned by the descriptor in the {@link org.openscience.cdk1.qsar.DescriptorValue} object
     */
    @TestMethod("testGetDescriptorResultType")
    public IDescriptorResult getDescriptorResultType() {
        return new IntegerArrayResult(names.length);
    }

    /**
     * Gets the parameterNames attribute of the PaDELHBondAcceptorCountDescriptor object.
     *
     * @return    The parameterNames value
     */
    @TestMethod("testGetParameterNames")
    public String[] getParameterNames() {
        String[] params = new String[1];
        params[0] = "checkAromaticity";
        return params;
    }

    /**
     * Gets the parameterType attribute of the PaDELHBondAcceptorCountDescriptor object.
     *
     * @param  name  Description of the Parameter
     * @return       The parameterType value
     */
    @TestMethod("testGetParameterType_String")
    public Object getParameterType(String name) {
        return false;
    }
}

