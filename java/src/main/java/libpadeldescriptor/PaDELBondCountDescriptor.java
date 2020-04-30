/*  $Revision$ $Author$ $Date$
 *
 *  Copyright (C) 2004-2007  Matteo Floris <mfe4@users.sf.net>
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

import java.util.Iterator;
import org.openscience.cdk1.CDKConstants;
import org.openscience.cdk1.annotations.TestMethod;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IBond;
import org.openscience.cdk1.interfaces.IBond.Order;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;

/**
 * Number of bonds.
 *
 * The code calculates
 * nBonds: Total number of bonds, excluding bonds to hydrogens (Same as CDK).
 * nBonds2: Total number of bonds, including bonds to hydrogens.
 * nBondsS: Total number of single bonds, including bonds to hydrogens (Same as CDK).
 * nBondsS2: Total number of single bonds, including bonds to hydrogens, excluding aromatic bonds.
 * nBondsS3: Total number of single bonds, excluding bonds to hydrogens and aromatic bonds.
 * nBondsD: Total number of double bonds (Same as CDK).
 * nBondsD2: Total number of double bonds, excluding bonds to aromatic bonds.
 * nBondsT: Total number of triple bonds (Same as CDK).
 * nBondsQ: Total number of quadruple bonds (Same as CDK).
 * nBondsM: Total number of bonds that have bond order greater than one (aromatic bonds have bond order 1.5).
 *
 * @author      Yap Chun Wei
 */
public class PaDELBondCountDescriptor implements IMolecularDescriptor {

    private static final String[] names = {"nBonds", "nBonds2", "nBondsS", "nBondsS2", "nBondsS3", "nBondsD", "nBondsD2", "nBondsT", "nBondsQ", "nBondsM"};

    /**
     *  Constructor for the BondCountDescriptor object
     */
    public PaDELBondCountDescriptor() {}


    /**
     *  Gets the specification attribute of the BondCountDescriptor object
     *
     *@return    The specification value
     */
    @TestMethod("testGetSpecification")
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
            "PaDELBondCountDescriptor",
            this.getClass().getName(),
            "$Id$",
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
     *  This method calculate the number of bonds of a given type in an atomContainer
     *
     *@param  container  AtomContainer
     *@return            The number of bonds of a certain type.
     */
    @TestMethod("testCalculate_IAtomContainer")
    public DescriptorValue calculate(IAtomContainer container) {

        int nBonds = 0;
        int nBonds2 = 0;
        int nAromBond = 0;  // Calculated to compute nBonds, nBonds2 but not output because already calculated by AromaticBondsCountDescriptor
        int nBondsS = 0;
        int nBondsS2 = 0;
        int nBondsS3 = 0;
        int nBondsD = 0;
        int nBondsD2 = 0;
        int nBondsT = 0;
        int nBondsQ = 0;
        int nBondsM = 0;
        Iterator bonds = container.bonds().iterator();
        while (bonds.hasNext())
        {
            IBond bond = (IBond) bonds.next();
            boolean hasHydrogen = false;
            for (int i=0; i<bond.getAtomCount(); ++i)
            {
                if (bond.getAtom(i).getSymbol().equals("H"))
                {
                    hasHydrogen = true;
                    break;
                }
            }
            if (!hasHydrogen) ++nBonds;

            if (bond.getFlag(CDKConstants.ISAROMATIC)) ++nAromBond;

            if (bond.getOrder()==Order.SINGLE)
            {
                ++nBondsS;

                if (!bond.getFlag(CDKConstants.ISAROMATIC))
                {
                    ++nBondsS2;
                    if (!hasHydrogen) ++nBondsS3;
                }
            }
            else if (bond.getOrder()==Order.DOUBLE)
            {
                ++nBondsD;
                if (!bond.getFlag(CDKConstants.ISAROMATIC)) ++nBondsD2;
            }
            else if (bond.getOrder()==Order.TRIPLE) ++nBondsT;
            else if (bond.getOrder()==Order.QUADRUPLE) ++nBondsQ;
        }

        nBonds2 = nAromBond + nBondsS2 + nBondsD2 + nBondsT + nBondsQ;
        nBondsM = nAromBond + nBondsD2 + nBondsT + nBondsQ;

        DoubleArrayResult retVal = new DoubleArrayResult();
        retVal.add(nBonds);     // Total number of bonds, excluding bonds to hydrogens (Same as CDK).
        retVal.add(nBonds2);    // Total number of bonds, including bonds to hydrogens.
        retVal.add(nBondsS);    // Total number of single bonds, including bonds to hydrogens (Same as CDK).
        retVal.add(nBondsS2);   // Total number of single bonds, including bonds to hydrogens, excluding aromatic bonds.
        retVal.add(nBondsS3);   // Total number of single bonds, excluding bonds to hydrogens and aromatic bonds.
        retVal.add(nBondsD);    // Total number of double bonds (Same as CDK).
        retVal.add(nBondsD2);   // Total number of double bonds, excluding bonds to aromatic bonds.
        retVal.add(nBondsT);    // Total number of triple bonds (Same as CDK).
        retVal.add(nBondsQ);    // Total number of quadruple bonds (Same as CDK).
        retVal.add(nBondsM);    // Total number of bonds that have bond order greater than one (aromatic bonds have bond order 1.5).

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retVal, names);
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
    @Override
    public IDescriptorResult getDescriptorResultType() {
        return new DoubleArrayResultType(names.length);
    }
}
