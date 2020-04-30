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


import java.util.List;
import org.openscience.cdk1.CDKConstants;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.graph.PathTools;
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
 * Path count descriptors
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-11
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:path count
 * @cdk.keyword molecular type Path count descriptor
 * @cdk.keyword descriptor
 */

public class PathCountDescriptor implements IMolecularDescriptor {

    private static final int maxLength = 10;
    public String[] names;

    public PathCountDescriptor()
    {
        names = new String[(maxLength-1)+1+maxLength+1+1];
        int index = 0;

        for (int k=2; k<=maxLength; ++k) names[index++] = "MPC" + k;
        names[index++] = "TPC";
        for (int k=1; k<=maxLength; ++k) names[index++] = "piPC" + k;
        names[index++] = "TpiPC";
        names[index++] = "R_TpiPCTPC";
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "Path count",
                this.getClass().getName(),
                "$Id: PathCountDescriptor.java 1 2014-06-06 17:00:00Z yapchunwei $",
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

        double[] mpc = new double[maxLength];
        double[] pipc = new double[maxLength];
        double TPC = natom;
        double TpiPC = natom;
        for (int k=0; k<maxLength; ++k)
        {
            for (int i=0; i<natom; ++i)
            {
                List<List<IAtom> > paths = PathTools.getPathsOfLength(local, local.getAtom(i), k+1);
                mpc[k] += paths.size();

                for (List<IAtom> path : paths)
                {
                    int maxAtoms = path.size();
                    double pdtBondOrders = 1;
                    for (int j=0; j<maxAtoms-1; ++j)
                    {
                        IBond bond = local.getBond(path.get(j), path.get(j+1));
                        Order bondorder = bond.getOrder();
                        if (bond.getFlag(CDKConstants.ISAROMATIC)) pdtBondOrders *= 1.5;
                        else if (bondorder==Order.SINGLE) pdtBondOrders *= 1;
                        else if (bondorder==Order.DOUBLE) pdtBondOrders *= 2;
                        else if (bondorder==Order.TRIPLE) pdtBondOrders *= 3;
                    }
                    pipc[k] += pdtBondOrders;
                }
            }
            mpc[k] *= 0.5;
            TPC += mpc[k];
            TpiPC += 0.5*pipc[k];
            pipc[k] = Math.log(1+0.5*pipc[k]);
        }
        double R_TpiPCTPC = TpiPC / TPC;
        TpiPC = Math.log(1+TpiPC);
        DoubleArrayResult retval = new DoubleArrayResult();
        for (int k=1; k<maxLength; ++k) retval.add(mpc[k]);
        retval.add(TPC);
        for (int k=0; k<maxLength; ++k) retval.add(pipc[k]);
        retval.add(TpiPC);
        retval.add(R_TpiPCTPC);

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
