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
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.qsar.result.IntegerArrayResult;

/**
 * This descriptor calculates the number of hydrogen bond donors using various algorithms.
 *
 * CDK
 * Slightly simplified version of the <a href="http://www.chemie.uni-erlangen.de/model2001/abstracts/rester.html">PHACIR atom types</a>.
 * The following groups are counted as hydrogen bond donors:
 * <ul>
 * <li>Any-OH where the formal charge of the oxygen is non-negative (i.e. formal charge >= 0)</li>
 * <li>Any-NH where the formal charge of the nitrogen is non-negative (i.e. formal charge >= 0)</li>
 * </ul>
 *
 * Lipinski
 *  The following groups are counted as hydrogen bond donors:
 * <ul>
 * <li>Any OH. Each available hydrogen atom is counted as one hydrogen bond donor.</li>
 * <li>Any NH. Each available hydrogen atom is counted as one hydrogen bond donor.</li>
 * </ul>
 *
 * <p>
 * This descriptor uses no parameters.
 * <p>
 * This descriptor works properly with AtomContainers whose atoms contain either <b>implicit</b> or <b>explicit
 * hydrogen</b> atoms. It does not work with atoms that contain neither implicit nor explicit hydrogens.
 *
 * Returns <i>nHBDon</i>, <i>nHBDon2</i>, <i>nHBDon_Lipinski</i>.
 *
 * <p>This descriptor uses these parameters:
 * <table border="1">
 *   <tr>
 *     <td>Name</td>
 *     <td>Default</td>
 *     <td>Description</td>
 *   </tr>
 *   <tr>
 *     <td></td>
 *     <td></td>
 *     <td>no parameters</td>
 *   </tr>
 * </table>
 */
public class PaDELHBondDonorCountDescriptor implements IMolecularDescriptor {
    private static final String[] names = {"nHBDon", "nHBDon_Lipinski"};

    /**
     *  Constructor for the PaDELHBondDonorCountDescriptor object
     */
    public PaDELHBondDonorCountDescriptor() { }


    /**
     * Gets the specification attribute of the PaDELHBondDonorCountDescriptor
     * object
     *
     * @return    The specification value
     */
    @TestMethod("testGetSpecification")
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
            "PaDELHBondDonorCountDescriptor",
            this.getClass().getName(),
            "$Id$",
            "The Chemistry Development Kit");
    }


    /**
     * Sets the parameter of this PaDELHBondDonorCountDescriptor instance.
     *
     * @param  params            this descriptor does not have any parameters
     * @exception  CDKException  Description of the Exception
     */
    @TestMethod("testSetParameters_arrayObject")
    public void setParameters(Object[] params) throws CDKException {
    // this descriptor has no parameters; nothing has to be done here
    }


    /**
     * Gets the parameters of the PaDELHBondDonorCountDescriptor instance.
     *
     * @return    null as this descriptor does not have any parameters
     */
    @TestMethod("testGetParameters")
    public Object[] getParameters() {
    // no parameters; thus we return null
        return null;
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
     * Calculates the number of H bond donors.
     *
     * @param  atomContainer               AtomContainer
     * @return                   number of H bond donors
     */
    @TestMethod("testCalculate_IAtomContainer")
    public DescriptorValue calculate(IAtomContainer atomContainer) {
        int nHBDon_CDK = 0;
        int nHBDon_Lipinski = 0;

        IAtomContainer ac;
        try {
            ac = (IAtomContainer) atomContainer.clone();
        } catch (CloneNotSupportedException e) {
            return getDummyDescriptorValue(e);
        }

        //org.openscience.cdk1.interfaces.IAtom[] atoms = ac.getAtoms();
        // iterate over all atoms of this AtomContainer; use label atomloop to allow for labelled continue
        for (int atomIndex = 0; atomIndex < ac.getAtomCount(); atomIndex++) {
            IAtom atom = (IAtom) ac.getAtom(atomIndex);
            // CDK, nHBDon_2, Lipinski: checking for O and N atoms
            if (atom.getSymbol().equals("O") || atom.getSymbol().equals("N")) 
            {
                // Lipinski: implicit hydrogens
                Integer implicitH = atom.getImplicitHydrogenCount();
                if (implicitH == CDKConstants.UNSET) implicitH = 0;
                nHBDon_Lipinski += implicitH;

                // Lipinski: explicit hydrogens
                java.util.List neighbours = ac.getConnectedAtomsList(atom);
                for (Object neighbour : neighbours)
                {
                    if (((IAtom) neighbour).getSymbol().equals("H"))
                    {
                        ++nHBDon_Lipinski;
                    }
                }

                // CDK: checking for O and N atoms where the formal charge is >= 0  
                boolean isHBDon_CDK = false;
                if (!isHBDon_CDK)
                {
                    // CDK: implicit hydrogens
                    if (implicitH > 0)
                    {
                        isHBDon_CDK = true;
                    }
                    

                    // CDK: explicit hydrogens
                    for (Object neighbour : neighbours)
                    {
                        if (((IAtom) neighbour).getSymbol().equals("H"))
                        {
                            isHBDon_CDK = true;                            
                        }
                    }
                    if (isHBDon_CDK && atom.getFormalCharge() >= 0) ++nHBDon_CDK;
                }
            }
        }

        IntegerArrayResult retval = new IntegerArrayResult();
        retval.add(nHBDon_CDK);
        retval.add(nHBDon_Lipinski);

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
     * Gets the parameterNames of the PaDELHBondDonorCountDescriptor.
     *
     * @return    null as this descriptor does not have any parameters
     */
    @TestMethod("testGetParameterNames")
    public String[] getParameterNames() {
    // no parameters; thus we return null
        return null;
    }



    /**
     * Gets the parameterType of the PaDELHBondDonorCountDescriptor.
     *
     * @param  name  Description of the Parameter
     * @return       null as this descriptor does not have any parameters
     */
    @TestMethod("testGetParameterType_String")
    public Object getParameterType(String name) {
    // no parameters; thus we return null
        return null;
    }
}

