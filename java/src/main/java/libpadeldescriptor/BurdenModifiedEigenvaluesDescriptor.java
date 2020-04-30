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

/**
 * Burden modified eigenvalues descriptors
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-11
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:BME
 * @cdk.keyword molecular type Burden modified eigenvalues descriptor
 * @cdk.keyword descriptor
 */

public class BurdenModifiedEigenvaluesDescriptor implements IMolecularDescriptor {

    private static final int maxEigenValues = 8;
    public String[] names;
    private static final String[] wtypes = {"m", "v", "e", "p", "i", "s"};

    public BurdenModifiedEigenvaluesDescriptor()
    {
        names = new String[maxEigenValues*2*wtypes.length];
        int index = 0;

        for (String type : wtypes)
        {
            for (int k=1; k<=maxEigenValues; ++k)
            {
                names[index++] = "SpMax" + k + "_Bh" + type;
            }

            for (int k=1; k<=maxEigenValues; ++k)
            {
                names[index++] = "SpMin" + k + "_Bh" + type;
            }
        }
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "BME",
                this.getClass().getName(),
                "$Id: BurdenModifiedEigenvaluesDescriptor.java 1 2014-06-06 12:00:00Z yapchunwei $",
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
        int natom = container.getAtomCount();
        double[][] w = new double[wtypes.length][natom];

        IntrinsicStateDescriptor isd = new IntrinsicStateDescriptor();
        int maxC = 0;
        double Cistate = 0;
        for (int i=0; i<natom; ++i)
        {
            int atomicNumber = container.getAtom(i).getAtomicNumber();

            if (atomicNumber<AtomConstants.masses.length) w[0][i] = AtomConstants.masses[atomicNumber] / AtomConstants.masses[6];
            else w[0][i] = Double.NaN;

            if (atomicNumber<AtomConstants.volumes.length) w[1][i] = AtomConstants.volumes[atomicNumber] / AtomConstants.volumes[6];
            else w[1][i] = Double.NaN;

            if (atomicNumber<AtomConstants.sandersonelnegativities.length) w[2][i] = AtomConstants.sandersonelnegativities[atomicNumber] / AtomConstants.sandersonelnegativities[6];
            else w[2][i] = Double.NaN;

            if (atomicNumber<AtomConstants.polarizabilities.length) w[3][i] = AtomConstants.polarizabilities[atomicNumber] / AtomConstants.polarizabilities[6];
            else w[3][i] = Double.NaN;

            if (atomicNumber<AtomConstants.ionpotentials.length) w[4][i] = AtomConstants.ionpotentials[atomicNumber] / AtomConstants.ionpotentials[6];
            else w[4][i] = Double.NaN;

            IAtom atom = container.getAtom(i);
            if (atomicNumber==1)
            {
                w[5][i] = 1;
            }
            else
            {
                DoubleArrayResult results = (DoubleArrayResult)isd.calculate(atom, container).getValue();
                w[5][i] = results.get(0);
                if (atomicNumber==6)
                {
                    ++maxC;
                    Cistate += results.get(0);
                }
            }
        }
        double meanCistate = Cistate / maxC;
        for (int i=0; i<natom; ++i) w[5][i] /= meanCistate;
        
        // Check for NaN values. If there are any, EigenvalueDecomposition will hang.
        for (int t=0; t<wtypes.length; ++t)
        {
            for (int i=0; i<natom; ++i)
            {
                if (Double.isNaN(w[t][i])) return getDummyDescriptorValue(new CDKException("Atom in molecule is not recognized"));
            }
        }

        double[][] burden = getBurdenModifiedMatrixTemplate(container);
        DoubleArrayResult retval = new DoubleArrayResult();

        for (int t=0; t<wtypes.length; ++t)
        {
            for (int i=0; i<natom; ++i) burden[i][i] = w[t][i];
            Matrix matrix = new Matrix(burden);
            EigenvalueDecomposition eigenDecomposition = new EigenvalueDecomposition(matrix);
            double[] eigenvalues = eigenDecomposition.getRealEigenvalues();

            int index = eigenvalues.length - 1;
            for (int k=0; k<maxEigenValues; ++k)
            {
                if (index>=0) retval.add(Math.abs(eigenvalues[index--]));
                else retval.add(Double.NaN);
            }

            index = 0;
            for (int k=0; k<maxEigenValues; ++k)
            {
                if (index<eigenvalues.length) retval.add(Math.abs(eigenvalues[index++]));
                else retval.add(Double.NaN);
            }
        }

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, getDescriptorNames());
    }

    private double[][] getBurdenModifiedMatrixTemplate(IAtomContainer container)
    {
        int natom = container.getAtomCount();
        double[][] matrix = new double[natom][natom];
        for (int i=0; i<natom; ++i)
        {
            for (int j=0; j<natom; ++j)
            {
                matrix[i][j] = 0.0;
            }
        }

        // set the off diagonal entries
        double sqrt15 = Math.sqrt(1.5);
        double sqrt2 = Math.sqrt(2.0);
        double sqrt3 = Math.sqrt(3.0);
        for (IBond bond : container.bonds())
        {
            int i = container.getAtomNumber(bond.getAtom(0));
            int j = container.getAtomNumber(bond.getAtom(1));
            Order bondorder = bond.getOrder();
            if (bond.getFlag(CDKConstants.ISAROMATIC)) matrix[i][j] = sqrt15;
            else if (bondorder==Order.SINGLE) matrix[i][j] = 1;
            else if (bondorder==Order.DOUBLE) matrix[i][j] = sqrt2;
            else if (bondorder==Order.TRIPLE) matrix[i][j] = sqrt3;

            // terminal bonds (According to DRAGON 6 manual, 0.1 is added to terminal bonds)
            if (container.getAtom(i).getAtomicNumber()!=1 && container.getAtom(j).getAtomicNumber()!=1)
            {
                if (container.getConnectedBondsCount(i)==1 || container.getConnectedBondsCount(j)==1)
                {
                    matrix[i][j] += 0.1;
                }
            }

            matrix[j][i] = matrix[i][j];
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
