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


import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.graph.matrix.TopologicalMatrix;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IBond;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;

/**
 * Topological distance matrix descriptors
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-11
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:Topological distance matrix
 * @cdk.keyword molecular type Topological distance matrix descriptor
 * @cdk.keyword descriptor
 */

public class TopologicalDistanceMatrixDescriptor implements IMolecularDescriptor {

    public String[] names;
    private static final String[] dtypes = {"SpMax", "SpDiam", "SpAD", "SpMAD", "EE", "VE1", "VE2", "VE3", "VR1", "VR2", "VR3"};

    public TopologicalDistanceMatrixDescriptor()
    {
        names = new String[dtypes.length];
        int index = 0;

        for (String dtype : dtypes)
        {
            names[index++] = dtype + "_D";
        }
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "Topological distance matrix",
                this.getClass().getName(),
                "$Id: TopologicalDistanceMatrixDescriptor.java 1 2014-06-12 13:30:00Z yapchunwei $",
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

        DoubleArrayResult retval = new DoubleArrayResult();
        int[][] distance = TopologicalMatrix.getMatrix(local);
        Matrix matrix = new Matrix(natom, natom);
        for (int i=0; i<natom; ++i)
        {
            for (int j=0; j<natom; ++j)
            {
                matrix.set(i, j, distance[i][j]);
            }
        }

        EigenvalueDecomposition eigenDecomposition = new EigenvalueDecomposition(matrix);
        double[] eigenvalues = eigenDecomposition.getRealEigenvalues();
        double[][] eigenvectors = eigenDecomposition.getV().getArray();
        double SpMax = eigenvalues[natom - 1];
        double SpDiam = SpMax - eigenvalues[0];
        double SpAD = 0.0;
        double SpMAD = 0.0;
        double EE = 0.0;
        double SM1 = 0.0;
        double VED1 = 0.0;
        double VED2 = 0.0;
        double VED3 = 0.0;
        double VRD1 = 0.0;
        double VRD2 = 0.0;
        double VRD3 = 0.0;
        for (int i=0; i<natom; ++i)
        {
            SM1 += eigenvalues[i];
            VED1 += eigenvectors[i][0];
            EE += Math.exp(eigenvalues[i]);
        }
        EE = Double.isInfinite(EE) ? Math.log(Double.MAX_VALUE) : Math.log(1+EE);
        VED1 = Math.abs(VED1);
        VED2 = VED1 / natom;
        VED3 = VED1==0.0 ? 0 : 0.1*natom * Math.log(VED1);

        final double smallEigenPdt = Math.pow(1e-12, -0.5); // Guard against very small eigenvalues which give rise to very large VRD1 and VRD2.
        for (IBond bond : local.bonds())
        {
            int a1 = local.getAtomNumber(bond.getAtom(0));
            int a2 = local.getAtomNumber(bond.getAtom(1));
            VRD1 += Math.abs(eigenvectors[a1][0]*eigenvectors[a2][0])<1e-12 ? smallEigenPdt : Math.pow(Math.abs(eigenvectors[a1][0]*eigenvectors[a2][0]), -0.5);
        }
        VRD2 = VRD1 / natom;
        VRD3 = 0.1*natom * Math.log(VRD1);

        double meanEigenValue = SM1 / natom;
        for (int i=0; i<natom; ++i)
        {
            double absdiff = Math.abs(eigenvalues[i]-meanEigenValue);
            SpAD += absdiff;
            SpMAD += absdiff / natom;
        }

        retval.add(SpMax);
        retval.add(SpDiam);
        retval.add(SpAD);
        retval.add(SpMAD);
        retval.add(EE);
        retval.add(VED1);
        retval.add(VED2);
        retval.add(VED3);
        retval.add(VRD1);
        retval.add(VRD2);
        retval.add(VRD3);

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
