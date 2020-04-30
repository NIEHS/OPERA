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
import org.openscience.cdk1.graph.PathTools;
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
 * Topological charge indices
 * <p/>
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-09
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:topological charge
 * @cdk.keyword molecular type topological charge descriptor
 * @cdk.keyword descriptor
 */
public class TopologicalChargeDescriptor implements IMolecularDescriptor {
    
    public static final String[] names = { 
                                            "GGI1",
                                            "GGI2",
                                            "GGI3",
                                            "GGI4",
                                            "GGI5",
                                            "GGI6",
                                            "GGI7",
                                            "GGI8",
                                            "GGI9",
                                            "GGI10",
                                            "JGI1",
                                            "JGI2",
                                            "JGI3",
                                            "JGI4",
                                            "JGI5",
                                            "JGI6",
                                            "JGI7",
                                            "JGI8",
                                            "JGI9",
                                            "JGI10",
                                            "JGT" };

    public TopologicalChargeDescriptor() {

    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "topologicalcharge",
                this.getClass().getName(),
                "$Id: TopologicalChargeDescriptor.java 1 2014-06-09 11:00:00Z yapchunwei $",
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

        IAtomContainer localContainer = AtomContainerManipulator.removeHydrogens(container);

        int natom = localContainer.getAtomCount();

        int[][] admat = AdjacencyMatrix.getMatrix(localContainer);
        int[][] distanceMatrix = PathTools.computeFloydAPSP(admat);

        double[][] d_rs_dist = new double[distanceMatrix.length][distanceMatrix[0].length];
        for (int i=0; i<distanceMatrix.length; ++i)
        {
            for (int j=0; j<distanceMatrix[i].length; ++j)
            {
                double x = (double) distanceMatrix[i][j];

                if (x != 0.0)
                {
                    d_rs_dist[i][j] = 1.0 / (x * x);
                }
                else
                {
                    d_rs_dist[i][j] = 0.0;
                }
            }
        }

        double[][] galvez = new double[distanceMatrix.length][distanceMatrix[0].length];
        for (int i=0; i<admat.length; ++i)
        {
            for (int j=0; j<d_rs_dist[0].length; ++j)
            {
                for (int k=0; k<admat[0].length; ++k)
                {
                    galvez[i][j] += admat[i][k] * d_rs_dist[k][j];
                }
            }
        }

        double[][] charge_term = new double[natom][natom];
        for (int i=0; i<natom; ++i)
        {
            for (int j=0; j<natom; ++j)
            {
                charge_term[i][j] = galvez[i][j] - galvez[j][i];

                if (i == j)
                {
                    charge_term[i][j] = localContainer.getConnectedAtomsCount(localContainer.getAtom(i));
                }
            }
        }

        double[] tci_gk = new double[Math.max(natom, 11)];
        int[] edges = new int[Math.max(natom, 11)];
        for (int k=0; k<natom; ++k)
        {
            for (int i=0; i<natom; ++i)
            {
                for (int j=0; j<natom; ++j)
                {
                    double abs_ij = charge_term[i][j];

                    if (abs_ij < 0.0)
                    {
                        abs_ij *= -1.0;
                    }

                    double delta = 0.0;

                    if (k == distanceMatrix[i][j])
                    {
                        delta = 1.0;
                        ++edges[k];
                    }

                    tci_gk[k] += (0.5 * (abs_ij * delta));
                }
            }
            edges[k] /= 2;
        }

        double[] tci_jk = new double[Math.max(natom, 11)];
        for (int i=0; i<tci_jk.length; ++i)
        {
            tci_jk[i] = edges[i]>0 ? tci_gk[i] / edges[i] : 0;
        }

        double globalTopoChargeIndex = 0.0;
        for (int i=1; i<tci_jk.length; ++i)
        {
            globalTopoChargeIndex += tci_jk[i];
        }        
        
        DoubleArrayResult retval = new DoubleArrayResult();        
        for (int k=1; k<=10; ++k)
        {
            retval.add(tci_gk[k]);
        }
        for (int k=1; k<=10; ++k)
        {
            retval.add(tci_jk[k]);
        }
        retval.add(globalTopoChargeIndex);
        
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

