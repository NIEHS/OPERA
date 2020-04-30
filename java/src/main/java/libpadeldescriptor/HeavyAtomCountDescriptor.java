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
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.qsar.result.IntegerResult;

/**
 * Returns the number of heavy atoms (i.e. not hydrogens).
 *
 * @author      yapchunwei
 * @cdk.created 2010-05-01
 * @cdk.module  qsarmolecular
 * @cdk.githash
 * @cdk.set     qsar-descriptors
 * @cdk.dictref qsar-descriptors:atomCount
 */
public class HeavyAtomCountDescriptor implements IMolecularDescriptor {

    public static final String[] names = {
                                            "nHeavyAtom"
                                         };

    private String elementName = "*";


    /**
     *  Constructor for the AtomCountDescriptor object.
     */
    public HeavyAtomCountDescriptor() {
        elementName = "*";
    }

    /**
     * Returns a <code>Map</code> which specifies which descriptor
     * is implemented by this class.
     *
     * These fields are used in the map:
     * <ul>
     * <li>Specification-Reference: refers to an entry in a unique dictionary
     * <li>Implementation-Title: anything
     * <li>Implementation-Identifier: a unique identifier for this version of
     *  this class
     * <li>Implementation-Vendor: CDK, JOELib, or anything else
     * </ul>
     *
     * @return An object containing the descriptor specification
     */
    @TestMethod("testGetSpecification")
    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "atomCount",
                this.getClass().getName(),
                "$Id: HeavyAtomCountDescriptor.java 1 2010-05-01 10:12:38Z yapchunwei $",
                "PaDEL");
    }

    /**
     *  Sets the parameters attribute of the AtomCountDescriptor object.
     *
     *@param  params            The new parameters value
     *@throws  CDKException  if the number of parameters is greater than 1
     *or else the parameter is not of type String
     *@see #getParameters
     */
    @TestMethod("testSetParameters_arrayObject")
    @Override
    public void setParameters(Object[] params) throws CDKException {
        if (params.length > 1) {
            throw new CDKException("AtomCount only expects one parameter");
        }
        if (!(params[0] instanceof String)) {
            throw new CDKException("The parameter must be of type String");
        }
        elementName = (String) params[0];
    }


    /**
     *  Gets the parameters attribute of the AtomCountDescriptor object.
     *
     *@return    The parameters value
     *@see #setParameters
     */
    @TestMethod("testGetParameters")
    @Override
    public Object[] getParameters() {
        // return the parameters as used for the descriptor calculation
        Object[] params = new Object[1];
        params[0] = elementName;
        return params;
    }

    @TestMethod(value="testNamesConsistency")
    @Override
    public String[] getDescriptorNames() {
        return names;
    }


    /**
     *  This method calculate the number of atoms of a given type in an {@link IAtomContainer}.
     *
     *@param  container  The atom container for which this descriptor is to be calculated
     *@return            Number of atoms of a certain type is returned.
     */

    // it could be interesting to accept as elementName a SMARTS atom, to get the frequency of this atom
    // this could be useful for other descriptors like polar surface area...
    @TestMethod("testCalculate_IAtomContainer")
    @Override
    public DescriptorValue calculate(IAtomContainer container) {
        elementName = "*";
        int nAtom = CountAtoms(container);
        if (nAtom==-1) {
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                    new IntegerResult((int) Double.NaN), getDescriptorNames(),
                    new CDKException("The supplied AtomContainer was NULL"));
        }
        else if (nAtom==-2) {
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                    new IntegerResult((int) Double.NaN), getDescriptorNames(),
                    new CDKException("The supplied AtomContainer did not have any atoms"));
        }

        elementName = "H";
        int nH = CountAtoms(container);
        if (nH==-1) {
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                    new IntegerResult((int) Double.NaN), getDescriptorNames(),
                    new CDKException("The supplied AtomContainer was NULL"));
        }
        else if (nH==-2) {
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                    new IntegerResult((int) Double.NaN), getDescriptorNames(),
                    new CDKException("The supplied AtomContainer did not have any atoms"));
        }

        int nHeavyAtom = nAtom - nH;
        if (nHeavyAtom < 0) {
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                    new IntegerResult((int) Double.NaN), getDescriptorNames(),
                    new CDKException("Number of heavy atoms is less than zero"));
        }

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                new IntegerResult(nHeavyAtom), getDescriptorNames());
    }

    private int CountAtoms(IAtomContainer container)
    {
        int atomCount = 0;

        if (container == null) return -1;

        if (container.getAtomCount() == 0) return -2;

        if (elementName.equals("*")) {
            for (int i = 0; i < container.getAtomCount(); i++) {
                // we assume that UNSET is equivalent to 0 implicit H's
                Integer hcount = container.getAtom(i).getImplicitHydrogenCount();
                if (hcount != CDKConstants.UNSET) atomCount += hcount;
            }
            atomCount += container.getAtomCount();
        } else if (elementName.equals("H")) {
            for (int i = 0; i < container.getAtomCount(); i++) {
                if (container.getAtom(i).getSymbol().equals(elementName)) {
                    atomCount += 1;
                } else {
                    // we assume that UNSET is equivalent to 0 implicit H's
                    Integer hcount = container.getAtom(i).getImplicitHydrogenCount();
                    if (hcount != CDKConstants.UNSET) atomCount += hcount;
                }
            }
        }
        else {
            for (int i = 0; i < container.getAtomCount(); i++) {
                if (container.getAtom(i).getSymbol().equals(elementName)) {
                    atomCount += 1;
                }
            }
        }
        return atomCount;
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
    @Override
    public IDescriptorResult getDescriptorResultType() {
        return new IntegerResult(1);
    }


    /**
     *  Gets the parameterNames attribute of the AtomCountDescriptor object.
     *
     *@return    The parameterNames value
     */
    @TestMethod("testGetParameterNames")
    @Override
    public String[] getParameterNames() {
        String[] params = new String[1];
        params[0] = "elementName";
        return params;
    }


    /**
     *  Gets the parameterType attribute of the AtomCountDescriptor object.
     *
     *@param  name  Description of the Parameter
     *@return       An Object whose class is that of the parameter requested
     */
    @TestMethod("testGetParameterType_String")
    @Override
    public Object getParameterType(String name) {
        return "";
    }
}

