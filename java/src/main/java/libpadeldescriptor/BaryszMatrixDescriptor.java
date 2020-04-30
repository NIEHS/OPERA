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
import org.openscience.cdk1.CDKConstants;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IBond;
import org.openscience.cdk1.interfaces.IBond.Order;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;

/**
 * Barysz matrix descriptors
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-11
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:Barysz matrix
 * @cdk.keyword molecular type Barysz matrix descriptor
 * @cdk.keyword descriptor
 */

public class BaryszMatrixDescriptor implements IMolecularDescriptor {

    public String[] names;
    private static final String[] dtypes = {"SpAbs", "SpMax", "SpDiam", "SpAD", "SpMAD", "EE", "SM1", "VE1", "VE2", "VE3", "VR1", "VR2", "VR3"};
    private static final String[] wtypes = {"Z", "m", "v", "e", "p", "i", "s"};

    public BaryszMatrixDescriptor()
    {
        names = new String[wtypes.length*dtypes.length];
        int index = 0;

        for (String wtype : wtypes)
        {
            for (String dtype : dtypes)
            {
                names[index++] = dtype + "_Dz" + wtype;
            }
        }
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "Barysz matrix",
                this.getClass().getName(),
                "$Id: BaryszMatrixDescriptor.java 1 2014-06-12 11:00:00Z yapchunwei $",
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

    private DescriptorValue getDummyDescriptorValue(Exception e) {
         int ndesc = getDescriptorNames().length;
         DoubleArrayResult results = new DoubleArrayResult(ndesc);
         for (int i = 0; i < ndesc; i++) results.add(Double.NaN);
         return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), results, getDescriptorNames(), e);
    }

    @Override
    public DescriptorValue calculate(IAtomContainer container)
    {
        IAtomContainer local = AtomContainerManipulator.removeHydrogens(container);
        int natom = local.getAtomCount();
        double[][] w = new double[wtypes.length][natom+1];
        
        for (int i=0; i<natom; ++i)
        {
            int atomicNumber = local.getAtom(i).getAtomicNumber();
            w[0][i] = atomicNumber;

            if (atomicNumber<AtomConstants.masses.length) w[1][i] = AtomConstants.masses[atomicNumber];
            else w[1][i] = Double.NaN;

            if (atomicNumber<AtomConstants.volumes.length) w[2][i] = AtomConstants.volumes[atomicNumber];
            else w[2][i] = Double.NaN;

            if (atomicNumber<AtomConstants.sandersonelnegativities.length) w[3][i] = AtomConstants.sandersonelnegativities[atomicNumber];
            else w[3][i] = Double.NaN;

            if (atomicNumber<AtomConstants.polarizabilities.length) w[4][i] = AtomConstants.polarizabilities[atomicNumber];
            else w[4][i] = Double.NaN;

            if (atomicNumber<AtomConstants.ionpotentials.length) w[5][i] = AtomConstants.ionpotentials[atomicNumber];
            else w[5][i] = Double.NaN;
        }
        
        // Calculation of intrinsic state requires hydrogen atoms.
        int index = 0;
        int maxC = 0;
        double Cistate = 0;
        IntrinsicStateDescriptor isd = new IntrinsicStateDescriptor();
        for (int i=0; i<container.getAtomCount(); ++i)
        {   
            IAtom atom = container.getAtom(i);
            int atomicNumber = atom.getAtomicNumber();
            if (atomicNumber!=1)
            {
                DoubleArrayResult results = (DoubleArrayResult)isd.calculate(atom, container).getValue();
                w[6][index++] = results.get(0);
                if (atomicNumber==6)
                {
                    ++maxC;
                    Cistate += results.get(0);
                }
            }
        }
        double meanCistate = Cistate / maxC;
        w[0][natom] = 6;
        w[1][natom] = AtomConstants.masses[6];
        w[2][natom] = AtomConstants.volumes[6];
        w[3][natom] = AtomConstants.sandersonelnegativities[6];
        w[4][natom] = AtomConstants.polarizabilities[6];
        w[5][natom] = AtomConstants.ionpotentials[6];
        w[6][natom] = meanCistate;
        
        // Check for NaN values. If there are any, EigenvalueDecomposition will hang.
        for (int t=0; t<wtypes.length; ++t)
        {
            for (int i=0; i<natom+1; ++i)
            {
                if (Double.isNaN(w[t][i])) return getDummyDescriptorValue(new CDKException("Atom in molecule is not recognized"));
            }
        }

        DoubleArrayResult retval = new DoubleArrayResult();
        for (int t=0; t<wtypes.length; ++t)
        {
            double[][] barysz = getBaryszMatrix(local, w[t]);
            Matrix matrix = new Matrix(barysz);
            EigenvalueDecomposition eigenDecomposition = new EigenvalueDecomposition(matrix);
            double[] eigenvalues = eigenDecomposition.getRealEigenvalues();
            double[][] eigenvectors = eigenDecomposition.getV().getArray();
            double SpAbs = 0.0;
            double SpMax = eigenvalues[natom - 1];
            double SpDiam = SpMax - eigenvalues[0];
            double SpAD = 0.0;
            double SpMAD = 0.0;
            double EE = 0.0;
            double SM1 = 0.0;
            double VEA1 = 0.0;
            double VEA2 = 0.0;
            double VEA3 = 0.0;
            double VRA1 = 0.0;
            double VRA2 = 0.0;
            double VRA3 = 0.0;
            for (int i=0; i<natom; ++i)
            {
                SM1 += eigenvalues[i];
                SpAbs += Math.abs(eigenvalues[i]);
                VEA1 += eigenvectors[i][0];
                EE += Math.exp(eigenvalues[i]);
            }
            EE = Double.isInfinite(EE) ? Math.log(Double.MAX_VALUE) : Math.log(1+EE);
            VEA1 = Math.abs(VEA1);
            VEA2 = VEA1 / natom;
            VEA3 = VEA1==0.0 ? 0 : 0.1*natom * Math.log(VEA1);

            final double smallEigenPdt = Math.pow(1e-12, -0.5); // Guard against very small eigenvalues which give rise to very large VRA1 and VRA2.
            for (IBond bond : local.bonds())
            {
                int a1 = local.getAtomNumber(bond.getAtom(0));
                int a2 = local.getAtomNumber(bond.getAtom(1));
                VRA1 += Math.abs(eigenvectors[a1][0]*eigenvectors[a2][0])<1e-12 ? smallEigenPdt : Math.pow(Math.abs(eigenvectors[a1][0]*eigenvectors[a2][0]), -0.5);
            }
            VRA2 = VRA1 / natom;
            VRA3 = 0.1*natom * Math.log(VRA1);

            double meanEigenValue = SM1 / natom;
            for (int i=0; i<natom; ++i)
            {
                double absdiff = Math.abs(eigenvalues[i]-meanEigenValue);
                SpAD += absdiff;
                SpMAD += absdiff / natom;
            }

            retval.add(SpAbs);
            retval.add(SpMax);
            retval.add(SpDiam);
            retval.add(SpAD);
            retval.add(SpMAD);
            retval.add(EE);
            retval.add(SM1);
            retval.add(VEA1);
            retval.add(VEA2);
            retval.add(VEA3);
            retval.add(VRA1);
            retval.add(VRA2);
            retval.add(VRA3);
        }

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, getDescriptorNames());
    }

    private double[][] getBaryszMatrix(IAtomContainer container, double[] w)
    {
        int natom = container.getAtomCount();
        double[][] matrix = new double[natom][natom];

        // set diagonal entries
        for (int i=0; i<natom; ++i)
        {
            matrix[i][i] = 1.0 - w[natom] / w[i];
        }

        // set off diagonal entries
        double wtC2 = w[natom] * w[natom];
        for (int i=0; i<natom; ++i)
        {
            ShortestPaths sp = new ShortestPaths(container, container.getAtom(i));    
            for (int j=i+1; j<natom; ++j)
            {
                int[] path = sp.pathTo(container.getAtom(j));
                for (int a=0; a<path.length-1; ++a)
                {
                    IAtom a1 = container.getAtom(path[a]);
                    IAtom a2 = container.getAtom(path[a+1]);
                    IBond bond = container.getBond(a1, a2);
                    double weights = wtC2 / (w[container.getAtomNumber(a1)]*w[container.getAtomNumber(a2)]);
                    Order bondorder = bond.getOrder();
                    if (bond.getFlag(CDKConstants.ISAROMATIC)) matrix[i][j] += weights / 1.5;
                    else if (bondorder==Order.SINGLE) matrix[i][j] += weights;
                    else if (bondorder==Order.DOUBLE) matrix[i][j] += 0.5 * weights;
                    else if (bondorder==Order.TRIPLE) matrix[i][j] += weights / 3.0;
                }
                matrix[j][i] = matrix[i][j];
            }
        }

        return matrix;
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
