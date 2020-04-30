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
import javax.vecmath.Point3d;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;

/**
 * WHIM descriptors
 *
 * @author Yap Chun Wei, Rajarshi Guha
 * @cdk.created 2014-06-12
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:WHIM
 * @cdk.keyword molecular type WHIM descriptor
 * @cdk.keyword descriptor
 */

public class PaDELWHIMDescriptor implements IMolecularDescriptor {

    public String[] names;
    private static final String[] wtypes = {"u", "m", "v", "e", "p", "i", "s"};

    public PaDELWHIMDescriptor()
    {
        names = new String[wtypes.length*13];
        int index = 0;

        for (String suffix : wtypes)
        {
            for (int k=1; k<=3; ++k) names[index++] = "L" + k + suffix;
            for (int k=1; k<=2; ++k) names[index++] = "P" + k + suffix;
 //           for (int k=1; k<=3; ++k) names[index++] = "G" + k + suffix;
            for (int k=1; k<=3; ++k) names[index++] = "E" + k + suffix;
            names[index++] = "T" + suffix;
            names[index++] = "A" + suffix;
            names[index++] = "V" + suffix;
            names[index++] = "K" + suffix;
 //           names[index++] = "G" + suffix;
            names[index++] = "D" + suffix;
        }
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "WHIM",
                this.getClass().getName(),
                "$Id: PaDELWHIMDescriptor.java 1 2014-06-12 16:30:00Z yapchunwei $",
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
        int natom = container.getAtomCount();
        double[][] w = new double[wtypes.length][natom];
        IntrinsicStateDescriptor isd = new IntrinsicStateDescriptor();
        int maxC = 0;
        double Cistate = 0;
        for (int i=0; i<natom; ++i)
        {
            int atomicNumber = container.getAtom(i).getAtomicNumber();
            w[0][i] = 1.0;

            if (atomicNumber<AtomConstants.masses.length) w[1][i] = AtomConstants.masses[atomicNumber] / AtomConstants.masses[6];
            else w[1][i] = Double.NaN;

            if (atomicNumber<AtomConstants.volumes.length) w[2][i] = AtomConstants.volumes[atomicNumber] / AtomConstants.volumes[6];
            else w[2][i] = Double.NaN;

            if (atomicNumber<AtomConstants.sandersonelnegativities.length) w[3][i] = AtomConstants.sandersonelnegativities[atomicNumber] / AtomConstants.sandersonelnegativities[6];
            else w[3][i] = Double.NaN;

            if (atomicNumber<AtomConstants.polarizabilities.length) w[4][i] = AtomConstants.polarizabilities[atomicNumber] / AtomConstants.polarizabilities[6];
            else w[4][i] = Double.NaN;

            if (atomicNumber<AtomConstants.ionpotentials.length) w[5][i] = AtomConstants.ionpotentials[atomicNumber] / AtomConstants.ionpotentials[6];
            else w[5][i] = Double.NaN;

            IAtom atom = container.getAtom(i);
            if (atomicNumber==1)
            {
                w[6][i] = 1;
            }
            else
            {
                DoubleArrayResult results = (DoubleArrayResult)isd.calculate(atom, container).getValue();
                w[6][i] = results.get(0);
                if (atomicNumber==6)
                {
                    ++maxC;
                    Cistate += results.get(0);
                }
            }
        }
        double meanCistate = Cistate / maxC;
        for (int i=0; i<natom; ++i) w[6][i] = w[6][i] / meanCistate + 7;

        // get the coordinate matrix
        double[][] cmat = new double[container.getAtomCount()][3];
        for (int i=0; i<natom; ++i)
        {
            Point3d coords = container.getAtom(i).getPoint3d();
            cmat[i][0] = coords.x;
            cmat[i][1] = coords.y;
            cmat[i][2] = coords.z;
        }

        double[][] L = new double[3][wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[][] P = new double[3][wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
  //      double[][] G = new double[3][wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[][] E = new double[3][wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[] NDT = new double[wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[] NDA = new double[wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[] NDV = new double[wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[] NDK = new double[wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
  //      double[] NDG = new double[wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[] NDD = new double[wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        for (int t=0; t<wtypes.length; ++t)
        {
            PCA pcaobject = new PCA(cmat, w[t]);

            // directional WHIM's
            double[] lambda1 = pcaobject.getEigenvalues();
            int[] sortedIndex = new int[3];
            if (lambda1[0]>lambda1[1] && lambda1[0]>lambda1[2]) sortedIndex[0] = 0;
            else if (lambda1[1]>lambda1[0] && lambda1[1]>lambda1[2]) sortedIndex[0] = 1;
            else sortedIndex[0] = 2;

            if ((lambda1[0]<lambda1[1] && lambda1[0]>lambda1[2]) || (lambda1[0]<lambda1[2] && lambda1[0]>lambda1[1])) sortedIndex[1] = 0;
            else if ((lambda1[1]<lambda1[0] && lambda1[1]>lambda1[2]) || (lambda1[1]<lambda1[2] && lambda1[1]>lambda1[0])) sortedIndex[1] = 1;
            else sortedIndex[1] = 2;

            if (lambda1[0]<lambda1[1] && lambda1[0]<lambda1[2]) sortedIndex[2] = 0;
            else if (lambda1[1]<lambda1[0] && lambda1[1]<lambda1[2]) sortedIndex[2] = 1;
            else sortedIndex[2] = 2;

            double[] lambda = new double[3];
            for (int i=0; i<3; ++i)
            {
                lambda[i] = lambda1[sortedIndex[i]];
            }
            double sum = 0.0;
            for (int k=0; k<3; ++k)
            {
                L[k][t] = lambda[k];
                sum += lambda[k];
            }
            for (int k=0; k<3; ++k) P[k][t] = lambda[k] / sum;

            double[][] scores1 = pcaobject.getScores();
            double[][] scores = new double[natom][3];
            for (int i=0; i<3; ++i)
            {
                for (int k=0; k<natom; ++k)
                {
                    scores[k][i] = scores1[k][sortedIndex[i]];
                }
            }
            for (int i=0; i<3; ++i)
            {
                sum = 0.0;
                for (int j=0; j<natom; ++j)
                {
                    sum += scores[j][i] * scores[j][i] * scores[j][i] * scores[j][i];
                }
                sum = sum / (lambda[i] * lambda[i] * natom);
                E[i][t] = 1.0 / sum;
            }
/*
            // look for symmetric & asymmetric atoms for the gamma descriptor
            final double log2 = Math.log(2.0);
            for (int i=0; i<3; ++i)
            {
                double ns = 0.0;
                double na = 0.0;
                for (int j=0; j<natom; ++j)
                {
                    boolean foundmatch = false;
                    for (int k=0; k<natom; ++k)
                    {
                        if (k == j) continue;
                        if (Math.abs(scores[j][i]+scores[k][i])<0.01)
                        {
                            ++ns;
                            foundmatch = true;
                            break;
                        }
                    }
                    if (!foundmatch) ++na;
                }
                double n = natom;
                G[i][t] = -1.0 * ((ns / n) * Math.log(ns / n) / log2 + (na / n) * Math.log(1.0 / n) / log2);
                G[i][t] = 1.0 / (1.0 + G[i][t]);
            }
*/
            // non directional WHIMS's
            NDT[t] = lambda[0] + lambda[1] + lambda[2];
            NDA[t] = lambda[0] * lambda[1] + lambda[0] * lambda[2] + lambda[1] * lambda[2];
            NDV[t] = NDT[t] + NDA[t] + lambda[0] * lambda[1] * lambda[2];

            NDK[t] = 0.0;
            sum = 0.0;
            for (int i=0; i<3; ++i) sum += lambda[i];
            for (int i=0; i<3; ++i) NDK[t] += Math.abs((lambda[i] / sum) - (1.0 / 3.0));
            NDK[t] /= (4.0 / 3.0);

//            NDG[t] = Math.pow(G[0][t] * G[1][t] * G[2][t], 1.0 / 3.0);
            NDD[t] = E[0][t] + E[1][t] + E[2][t];
        }

        DoubleArrayResult retval = new DoubleArrayResult();
        for (int t=0; t<wtypes.length; ++t)
        {
            for (int k=0; k<3; ++k) retval.add(L[k][t]);
            for (int k=0; k<2; ++k) retval.add(P[k][t]);
//            for (int k=0; k<3; ++k) retval.add(G[k][t]);
            for (int k=0; k<3; ++k) retval.add(E[k][t]);
            retval.add(NDT[t]);
            retval.add(NDA[t]);
            retval.add(NDV[t]);
            retval.add(NDK[t]);
//            retval.add(NDG[t]);
            retval.add(NDD[t]);
        }

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, getDescriptorNames());
    }

    private class PCA
    {
        Matrix evec;
        Matrix t;
        double[] eval;

        public PCA(double[][] cmat, double[] wt)
        {
            int ncol = 3;
            int nrow = wt.length;

            // make a copy of the coordinate matrix
            double[][] d = new double[nrow][ncol];
            for (int i=0; i<nrow; ++i)
            {
                for (int j=0; j<ncol; ++j)
                {
                    d[i][j] = cmat[i][j];
                }
            }

            // do mean centering - though the first paper used
            // barymetric centering
            for (int i=0; i<ncol; ++i)
            {
                double mean = 0.0;
                for (int j=0; j<nrow; ++j) mean += d[j][i];
                mean = mean / (double) nrow;
                for (int j=0; j<nrow; ++j) d[j][i] = (d[j][i] - mean);
            }

            // get the covariance matrix
            double[][] covmat = new double[ncol][ncol];
            double sumwt = 0.;
            for (int i=0; i<nrow; ++i)  sumwt += wt[i];
            for (int i=0; i<ncol; ++i)
            {
                double meanx = 0;
                for (int k=0; k<nrow; ++k) meanx += d[k][i];
                meanx = meanx / (double) nrow;
                for (int j=0; j<ncol; ++j)
                {
                    double meany = 0.0;
                    for (int k=0; k<nrow; ++k) meany += d[k][j];
                    meany = meany / (double) nrow;

                    double sum = 0.;
                    for (int k=0; k<nrow; ++k)
                    {
                        sum += wt[k] * (d[k][i] - meanx) * (d[k][j] - meany);
                    }
                    covmat[i][j] = sum / sumwt;
                }
            }

            // get the loadings (ie eigenvector matrix)
            Matrix m = new Matrix(covmat);
            EigenvalueDecomposition ed = m.eig();
            this.eval = ed.getRealEigenvalues();
            this.evec = ed.getV();
            Matrix x = new Matrix(d);
            this.t = x.times(this.evec);
        }

        double[] getEigenvalues() {
            return (this.eval);
        }

        double[][] getScores() {
            return (t.getArray());
        }
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
