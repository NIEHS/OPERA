/*
 *  $RCSfile$
 *  $Author: yapchunwei $
 *  $Date: 2008-06-10 18:12:38 +0800 (Tue, 10 Jun 2008) $
 *  $Revision: 1 $
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



import org.openscience.cdk1.annotations.TestClass;
import org.openscience.cdk1.annotations.TestMethod;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.AtomValenceTool;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IAtomicDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;

/**
 *  Intrinsic State of an atom. 
 *  There must not be any implicit hydrogens for all the atoms.<p>
 *
 *  This descriptor uses these parameters:
 *  <tableborder="1">
 *
 *    <tr>
 *
 *      <td>
 *        Name
 *      </td>
 *
 *      <td>
 *        Default
 *      </td>
 *
 *      <td>
 *        Description
 *      </td>
 *
 *    </tr>
 *
 *    <tr>
 *
 *      <td>
 *        
 *      </td>
 *
 *      <td>
 *        
 *      </td>
 *
 *      <td>
 *        no parameters
 *      </td>
 *
 *    </tr>
 *
 *  </table>
 *
 *
 *@author         yapchunwei
 *@cdk.created    2008-06-10
 *@cdk.module     qsaratomic
 * @cdk.svnrev  $Revision: 1 $
 *@cdk.set        qsar-descriptors
 * @cdk.dictref qsar-descriptors:intrinsicState
 */
@TestClass(value="org.openscience.cdk1.qsar.descriptors.atomic.IntrinsicStateDescriptorTest")
public class IntrinsicStateDescriptor implements IAtomicDescriptor {

    private static final String[] names = {"IntrinsicState", "IntrinsicState2"};

	/**
	 *  Constructor for the IntrinsicStateDescriptor object
	 *
	 */
	public IntrinsicStateDescriptor() {
	}


	/**
	 *  Gets the specification attribute of the IntrinsicStateDescriptor
	 *  object
	 *
	 *@return    The specification value
	 */
	@TestMethod(value="testGetSpecification")
    @Override
    public DescriptorSpecification getSpecification() {
		return new DescriptorSpecification(
				"intrinsicState",
				this.getClass().getName(),
				"$Id: IntrinsicStateDescriptor.java 1 2008-06-10 10:12:38Z yapchunwei $",
				"PaDEL");
	}


	/**
     * This descriptor does have any parameter.
     */
    @TestMethod(value="testSetParameters_arrayObject")
    @Override
    public void setParameters(Object[] params) throws CDKException {
    }


	/**
	 *  Gets the parameters attribute of the IntrinsicStateDescriptor
	 *  object
	 *
	 * @return    The parameters value
     * @see #setParameters
     */
    @TestMethod(value="testGetParameters")
    @Override
    public Object[] getParameters() {
        return null;
    }
    
    /**
     * Gets the names of descriptors
     * 
     * @return  Names of descriptors
     */
    @TestMethod(value="testNamesConsistency")
    @Override
    public String[] getDescriptorNames() {
        return names;
    }
    
    /**
     * Get dummy descriptor values when error occurs.
     * 
     * @param e Exception that prevents the proper calculation of the descriptors
     * @return  Dummy descriptor values
     */
    private DescriptorValue getDummyDescriptorValue(Exception e) {
        int ndesc = getDescriptorNames().length;
        DoubleArrayResult results = new DoubleArrayResult(ndesc);
        for (int i = 0; i < ndesc; i++) results.add(Double.NaN);
        return new DescriptorValue(getSpecification(), getParameterNames(),
                getParameters(), results, getDescriptorNames(), e);
    }

	/**
	 *  There must not be any implicit hydrogens for all the atoms.
	 *
	 *@param  atom              The IAtom for which the DescriptorValue is requested
     *@param  ac                AtomContainer
	 *@return                   a double with intrinsic state of the heavy atom
	 */
    @TestMethod(value="testCalculate_IAtomContainer")
    @Override
    public DescriptorValue calculate(IAtom atom, IAtomContainer ac) {
        try
        {
            double intrinsicState = 0.0;
            double intrinsicState2 = 0.0;
            int atomicNumber = atom.getAtomicNumber();
            if (atomicNumber!=1)
            {            
                int maxBondedHydrogens = AtomContainerManipulator.countHydrogens(ac, atom);
                double delta = ac.getConnectedBondsCount(atom) - maxBondedHydrogens;
                int Zv = AtomValenceTool.getValence(atom);                    
                double deltaV2 = (double)(Zv-maxBondedHydrogens); // Used by examples in Molconn-Z manual.
                double deltaV = (double)(Zv-maxBondedHydrogens) / (double)(atomicNumber-Zv-1); // Stated in Molconn-Z manual.
                int period = AtomConstants.period[atomicNumber];

                intrinsicState = (4.0/(period*period) * deltaV + 1) / delta; 
                intrinsicState2 = (4.0/(period*period) * deltaV2 + 1) / delta; 
            }
            DoubleArrayResult retval = new DoubleArrayResult();
            retval.add(intrinsicState);
            retval.add(intrinsicState2);
            
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, names);
        }
        catch (Exception e)
        {
            return getDummyDescriptorValue(new CDKException("Error in ElementPTFactory: " + e.getMessage()));
        }
    }

	/**
	 *  Gets the parameterNames attribute of the IntrinsicStateDescriptor  object.
	 *
	 * @return    The parameterNames value
	 */
	@TestMethod(value="testGetParameterNames")
    @Override
    public String[] getParameterNames() {
        return new String[0];
    }


	/**
	 *  Gets the parameterType attribute of the IntrinsicStateDescriptor object.
	 *
	 * @param  name  Description of the Parameter
     * @return       An Object of class equal to that of the parameter being requested
     */
    @TestMethod(value="testGetParameterType_String")
    @Override
    public Object getParameterType(String name) {
        return null;
    }
}

