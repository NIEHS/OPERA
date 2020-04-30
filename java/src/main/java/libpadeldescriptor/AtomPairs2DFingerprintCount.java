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


import java.util.ArrayList;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.graph.matrix.TopologicalMatrix;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;


/**
 * Count of 2D atom pairs.
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-13
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.keyword descriptor
 */
public class AtomPairs2DFingerprintCount implements IMolecularDescriptor {

    private static final int maxDistance = 10;
    public String[] names;
    private static final String[] atypes = {"C", "N", "O", "S", "P", "F", "Cl", "Br", "I", "B", "Si", "X"};
    private ArrayList<Integer[]> atypesInt;

    private class Pair
    {
        public Integer[] first;
        public Integer[] second;

        public Pair()
        {
        }

        public Pair(Integer[] first, Integer[] second)
        {
            this.first = first;
            this.second = second;
        }
    }
    private Pair[] atomPairs;

    public AtomPairs2DFingerprintCount()
    {
        atypesInt = new ArrayList<Integer[]>();
        for (int i=0; i<atypes.length-1; ++i)
        {
            atypesInt.add(new Integer[1]);
        }
        atypesInt.add(new Integer[4]);
        atypesInt.get(0)[0] = 6; // C
        atypesInt.get(1)[0] = 7; // N
        atypesInt.get(2)[0] = 8; // O
        atypesInt.get(3)[0] = 16; // S
        atypesInt.get(4)[0] = 15; // P
        atypesInt.get(5)[0] = 9; // F
        atypesInt.get(6)[0] = 17; // Cl
        atypesInt.get(7)[0] = 35; // Br
        atypesInt.get(8)[0] = 53; // I
        atypesInt.get(9)[0] = 5; // B
        atypesInt.get(10)[0] = 14; // Si
        atypesInt.get(11)[0] = 9; // X
        atypesInt.get(11)[1] = 17; // X
        atypesInt.get(11)[2] = 35; // X
        atypesInt.get(11)[3] = 53; // X

        atomPairs = new Pair[atypes.length*atypes.length/2 + atypes.length/2];
        int index = 0;
        for (int i=0; i<atypes.length; ++i)
        {
            for (int j=i; j<atypes.length; ++j)
            {
                atomPairs[index++] = new Pair(atypesInt.get(i), atypesInt.get(j));
            }
        }

        index = 0;
        names = new String[maxDistance*atomPairs.length];
        for (int d=1; d<=maxDistance; ++d)
        {
            for (int i=0; i<atypes.length; ++i)
            {
            for (int j=i; j<atypes.length; ++j)
                {
                    names[index++] = "APC2D" + d + "_" + atypes[i] + "_" + atypes[j];
                }
            }
        }
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "2D atom pairs count",
                this.getClass().getName(),
                "$Id: AtomPairs2DFingerprintCount.java 1 2014-06-13 09:00:00Z yapchunwei $",
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

    private boolean isInArray(Integer[] array, int val)
    {
        for (int i=0; i<array.length; ++i)
        {
            if (array[i]==val) return true;
        }
        return false;
    }

    /**
     *
     * @param container AtomContainer
     * @return
     */
    @Override
    public DescriptorValue calculate(IAtomContainer container)
    {
        IAtomContainer local = AtomContainerManipulator.removeHydrogens(container);
        int natom = local.getAtomCount();
        int[][] distance = TopologicalMatrix.getMatrix(local);

        double[][] counts = new double[maxDistance][atomPairs.length];
        for (int i=0; i<natom; ++i)
        {
            int a1 = local.getAtom(i).getAtomicNumber();
            for (int j=i+1; j<natom; ++j)
            {
                int a2 = local.getAtom(j).getAtomicNumber();
                if (distance[i][j]<=maxDistance)
                {
                    for (int a=0; a<atomPairs.length; ++a)
                    {
                        if ((isInArray(atomPairs[a].first, a1) && isInArray(atomPairs[a].second, a2)) ||
                            (isInArray(atomPairs[a].first, a2) && isInArray(atomPairs[a].second, a1)))
                        {
                            ++counts[distance[i][j]-1][a];
                        }
                    }
                }
            }
        }

        DoubleArrayResult retval = new DoubleArrayResult();
        for (int d=0; d<maxDistance; ++d)
        {
            for (int i=0; i<atomPairs.length; ++i)
            {
                retval.add(counts[d][i]);
            }
        }
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

