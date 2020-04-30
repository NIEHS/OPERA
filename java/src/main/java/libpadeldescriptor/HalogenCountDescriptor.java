/*
 *  $RCSfile$
 *  $Author: yapchunwei $
 *  $Date: 2008-07-07 14:50:01 +0800 (Tue, 07 Jul 2008) $
 *  $Revision: 1 $
 *
 *  Copyright (C) 2004-2007  Yap Chun Wei <yapchunwei@users.sourceforge.net>
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


import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.qsar.result.IntegerResult;


/**
 * Number of halogens
 * <p/>
 * The code calculates the number of fluorine, chlorine, bromine, iodine and total number of halogens (F, Cl, Br, I, At, Uus).
 *
 * @author Yap Chun Wei
 * @cdk.created 2010-05-01
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:nX
 * @cdk.keyword molecular type nX descriptor
 * @cdk.keyword descriptor
 */
public class HalogenCountDescriptor implements IMolecularDescriptor {
    
    public static final String[] names = {
                                            "nF",
                                            "nCl",
                                            "nBr",
                                            "nI",
                                            "nX"
                                         };

    public HalogenCountDescriptor() {

    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "nX",
                this.getClass().getName(),
                "$Id: HalogenCountDescriptor.java 1 2010-05-01 22:05:01Z yapchunwei $",
                "PaDEL");
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
     * 
     * @param container AtomContainer
     * @return
     */
    @Override
    public DescriptorValue calculate(IAtomContainer container) {

        if (container == null) {
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                    new IntegerResult((int) Double.NaN), getDescriptorNames(),
                    new CDKException("The supplied AtomContainer was NULL"));
        }

        if (container.getAtomCount() == 0) {
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                    new IntegerResult((int) Double.NaN), getDescriptorNames(),
                    new CDKException("The supplied AtomContainer did not have any atoms"));
        }

        int nF = 0;
        int nCl = 0;
        int nBr = 0;
        int nI = 0;
        int nOthers = 0;
        for (int i = 0; i < container.getAtomCount(); i++) 
        {
            if (container.getAtom(i).getSymbol().equals("F"))
            {
                ++nF;
            }
            else if (container.getAtom(i).getSymbol().equals("Cl"))
            {
                ++nCl;
            }
            else if (container.getAtom(i).getSymbol().equals("Br"))
            {
                ++nBr;
            }
            else if (container.getAtom(i).getSymbol().equals("I"))
            {
                ++nI;
            }
            else if (container.getAtom(i).getSymbol().equals("At") ||
                     container.getAtom(i).getSymbol().equals("Uus")) 
            {
                ++nOthers;
            }
        }
        nOthers += nF + nCl + nBr + nI;

        // Store set of smallest ring information.
        DoubleArrayResult retval = new DoubleArrayResult();
        retval.add(nF);
        retval.add(nCl);
        retval.add(nBr);
        retval.add(nI);
        retval.add(nOthers);        

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, names);
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

