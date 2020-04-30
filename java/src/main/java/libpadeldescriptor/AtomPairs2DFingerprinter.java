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
import java.util.BitSet;
import org.openscience.cdk1.fingerprint.IFingerprinter;
import org.openscience.cdk1.graph.matrix.TopologicalMatrix;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;


/**
 * Presence/absence of 2D atom pairs.
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-13
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.keyword descriptor
 */
public class AtomPairs2DFingerprinter implements IFingerprinter {

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

    public AtomPairs2DFingerprinter()
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
                    names[index++] = "AP2D" + d + "_" + atypes[i] + "_" + atypes[j];
                }
            }
        }
    }

    @Override
    public int getSize()
    {
        return maxDistance*atomPairs.length;
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
    public BitSet getFingerprint(IAtomContainer container)
    {
        IAtomContainer local = AtomContainerManipulator.removeHydrogens(container);
        int natom = local.getAtomCount();
        int[][] distance = TopologicalMatrix.getMatrix(local);

        BitSet fp = new BitSet(maxDistance*atomPairs.length);
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
                            fp.set((distance[i][j]-1)*atomPairs.length + a, true);
                        }
                    }
                }
            }
        }

        return fp;
    }
}

