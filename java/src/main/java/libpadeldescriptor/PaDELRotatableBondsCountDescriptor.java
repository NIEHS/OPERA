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
import org.openscience.cdk1.annotations.TestClass;
import org.openscience.cdk1.annotations.TestMethod;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.exception.NoSuchAtomException;
import org.openscience.cdk1.graph.SpanningTree;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IBond;
import org.openscience.cdk1.interfaces.IRingSet;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.tools.manipulator.BondManipulator;

import java.util.List;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;

/**
 *  The number of rotatable bonds is given by the SMARTS specified by Daylight on
 *  <a href="http://www.daylight.com/dayhtml_tutorials/languages/smarts/smarts_examples.html#EXMPL">SMARTS tutorial</a><p>
 *
 * Returns <i>nRotB</i>, <i>RotBFrac</i>, <i>nRotBt</i>, <i>RotBtFrac</i>
 *
 * @author      mfe4
 * @cdk.created 2004-11-03
 * @cdk.module  qsarmolecular
 * @cdk.githash
 * @cdk.set     qsar-descriptors
 * @cdk.dictref qsar-descriptors:rotatableBondsCount
 * 
 * @cdk.keyword bond count, rotatable
 * @cdk.keyword descriptor
 */
@TestClass("org.openscience.cdk1.qsar.descriptors.molecular.RotatableBondsCountDescriptorTest")
public class PaDELRotatableBondsCountDescriptor implements IMolecularDescriptor {

    private static final String[] names = { "nRotB", "RotBFrac", "nRotBt", "RotBtFrac"};

    /**
     *  Constructor for the RotatableBondsCountDescriptor object
     */
    public PaDELRotatableBondsCountDescriptor() { }


    /**
     *  Gets the specification attribute of the RotatableBondsCountDescriptor
     *  object
     *
     *@return    The specification value
     */
    @TestMethod("testGetSpecification")
    public DescriptorSpecification getSpecification() 
    {
        return new DescriptorSpecification(
            "http://www.blueobelisk.org/ontologies/chemoinformatics-algorithms/#rotatableBondsCount",
                    this.getClass().getName(),
                    "$Id: 75ae921845de8bd2afa9094077b54a7f8a79e4c1 $",
            "The Chemistry Development Kit");
    }

    @Override
    public String[] getParameterNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getParameterType(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setParameters(Object[] params) throws CDKException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object[] getParameters() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @TestMethod(value="testNamesConsistency")
    /**
     * Gets the names of descriptors
     *
     * @return  Names of descriptors
     */
    @Override
    public String[] getDescriptorNames() {
        return names;
    }

    /**
     *  The method calculates the number of rotatable bonds of an atom container.
     *  If the boolean parameter is set to true, terminal bonds are included.
     *
     *@param  ac                AtomContainer
     *@return                   number of rotatable bonds
     */
    @TestMethod("testCalculate_IAtomContainer")
    public DescriptorValue calculate(IAtomContainer ac) 
    {
        int nRotB = 0;
        int nRotBt = 0;
        int nBonds = 0; // Number of bonds (excluding bonds to hydrogens)
        int degree0;
        int degree1;
        IRingSet ringSet;
        try 
        {
            ringSet = new SpanningTree(ac).getBasicRings();
        } 
        catch (NoSuchAtomException e) 
        {
            DoubleArrayResult retVal = new DoubleArrayResult();
            retVal.add(Double.NaN);     // Total number of rotatable bonds, excluding terminal bonds to hydrogens.
            retVal.add(Double.NaN);    // Fraction of rotatable bonds, excluding terminal bonds to hydrogens.
            retVal.add(Double.NaN);    // Total number of rotatable bonds, including terminal bonds to hydrogens.
            retVal.add(Double.NaN);   // Fraction of rotatable bonds, including terminal bonds to hydrogens.

            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retVal, names);
        }
        for (IBond bond : ac.bonds()) 
        {
            if (ringSet.getRings(bond).getAtomContainerCount() > 0) 
            {
                bond.setFlag(CDKConstants.ISINRING, true);
            }
        }
		
        for (IBond bond : ac.bonds()) 
        {
            IAtom atom0 = bond.getAtom(0);
            IAtom atom1 = bond.getAtom(1);
            if (atom0.getSymbol().equals("H") || atom1.getSymbol().equals("H")) continue;
            ++nBonds;
            if (bond.getOrder() == CDKConstants.BONDORDER_SINGLE) 
            {
                if ((BondManipulator.isLowerOrder(ac.getMaximumBondOrder(atom0), IBond.Order.TRIPLE)) && 
                    (BondManipulator.isLowerOrder(ac.getMaximumBondOrder(atom1), IBond.Order.TRIPLE))) 
                {
                    if (!bond.getFlag(CDKConstants.ISINRING)) 
                    {
                        ++nRotBt;
                        
                        // if there are explicit H's we should ignore those bonds
                        degree0 = ac.getConnectedBondsCount(atom0) - getConnectedHCount(ac, atom0);
                        degree1 = ac.getConnectedBondsCount(atom1) - getConnectedHCount(ac, atom1);
                        if ((degree0 != 1) && (degree1 != 1)) ++nRotB;
                    }
		}
            }
	}
        
        double RotBFrac = 0.0;
        double RotBtFrac = 0.0;
        if (nBonds>0)
        {
            RotBFrac = (double)nRotB / (double)nBonds;
            RotBtFrac = (double)nRotBt / (double)nBonds;
        }
        
        DoubleArrayResult retVal = new DoubleArrayResult();
        retVal.add(nRotB);     // Total number of rotatable bonds, excluding terminal bonds to hydrogens.
        retVal.add(RotBFrac);    // Fraction of rotatable bonds, excluding terminal bonds to hydrogens.
        retVal.add(nRotBt);    // Total number of rotatable bonds, including terminal bonds to hydrogens.
        retVal.add(RotBtFrac);   // Fraction of rotatable bonds, including terminal bonds to hydrogens.

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retVal, names);
    }

    private int getConnectedHCount(IAtomContainer atomContainer, IAtom atom) 
    {
        List<IAtom> connectedAtoms = atomContainer.getConnectedAtomsList(atom);
        int n = 0;
        for (IAtom anAtom : connectedAtoms) if (anAtom.getSymbol().equals("H")) n++;
        return n;
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
    public IDescriptorResult getDescriptorResultType() 
    {
        return new DoubleArrayResultType(names.length);
    }
}

