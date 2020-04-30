/*
*  $RCSfile$
*  $Author$
*  $Date$
*  $Revision$
*
*  Copyright (C) 2004-2007  Yap Chun Wei <yapchunwei@gmail.com>
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


import Jama.Matrix;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.graph.matrix.AdjacencyMatrix;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;

/**
 * Walk count descriptors
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-11
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:walk count
 * @cdk.keyword molecular type Walk count descriptor
 * @cdk.keyword descriptor
 */

public class WalkCountDescriptor implements IMolecularDescriptor {

    private static final int maxLength = 10;
    public String[] names;

    public WalkCountDescriptor()
    {
        names = new String[(maxLength-1)+1+(maxLength-1)+1];
        int index = 0;

        for (int k=2; k<=maxLength; ++k) names[index++] = "MWC" + k;
        names[index++] = "TWC";
        for (int k=2; k<=maxLength; ++k) names[index++] = "SRW" + k;
        names[index++] = "TSRW";
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "Walk count",
                this.getClass().getName(),
                "$Id: WalkCountDescriptor.java 1 2014-06-06 15:00:00Z yapchunwei $",
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

    @Override
    public String[] getDescriptorNames() {
        return names;
    }


    @Override
    public DescriptorValue calculate(IAtomContainer container)
    {
        IAtomContainer local = AtomContainerManipulator.removeHydrogens(container);

        int natom = local.getAtomCount();
        int[][] A = AdjacencyMatrix.getMatrix(local);
        Matrix mA = new Matrix(natom, natom);
        for (int i=0; i<natom; ++i)
        {
            for (int j=0; j<natom; ++j)
            {
                mA.set(i, j, A[i][j]);
            }
        }

        Matrix m = mA;
        double[] mwc = new double[maxLength];
        double[] srw = new double[maxLength];
        double TWC = natom;
        double TSRW = natom;
        for (int k=0; k<maxLength; ++k)
        {
            mwc[k] = 0.0;
            srw[k] = 0.0;
            for (int i=0; i<natom; ++i)
            {
                for (int j=0; j<natom; ++j)
                {
                    mwc[k] += m.get(i, j);
                }
                srw[k] += m.get(i, i);
            }

            if (k==0) mwc[k] *= 0.5;
            else mwc[k] = Math.log(1+mwc[k]);
            TWC += mwc[k];
            TSRW += srw[k];
            srw[k] = Math.log(1+srw[k]);

            m = m.times(mA);
        }
        TSRW = Math.log(1+TSRW);
        DoubleArrayResult retval = new DoubleArrayResult();
        for (int k=1; k<maxLength; ++k) retval.add(mwc[k]);
        retval.add(TWC);
        for (int k=1; k<maxLength; ++k) retval.add(srw[k]);
        retval.add(TSRW);

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
    @Override
    public IDescriptorResult getDescriptorResultType() {
        return new DoubleArrayResultType(names.length);
    }
}
