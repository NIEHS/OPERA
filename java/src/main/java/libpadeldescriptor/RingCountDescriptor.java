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


import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IRingSet;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.ringsearch.AllRingsFinder;
import org.openscience.cdk1.ringsearch.SSSRFinder;


/**
 * Number of rings
 * <p/>
 * The code calculates the number of 3-membered, 4-membered, ..., 12-membered, >12-membered rings.
 * <p/>
 * The value returned is nRing, n3Ring, n4Ring, n5Ring, n6Ring, n7Ring, n8Ring, n9Ring, n10Ring, n11Ring, n12Ring, nG12Ring,
 * nFRing, nF4Ring, nF5Ring, nF6Ring, nF7Ring, nF8Ring, nF9Ring, nF10Ring, nF11Ring, nF12Ring, nFG12Ring,
 * nTRing, nT4Ring, nT5Ring, nT6Ring, nT7Ring, nT8Ring, nT9Ring, nT10Ring, nT11Ring, nT12Ring, nTG12Ring,
 * nHeteroRing, n3HeteroRing, n4HeteroRing, n5HeteroRing, n6HeteroRing, n7HeteroRing, n8HeteroRing, n9HeteroRing, n10HeteroRing, n11HeteroRing, n12HeteroRing, nG12HeteroRing, 
 * nFHeteroRing, nF4HeteroRing, nF5HeteroRing, nF6HeteroRing, nF7HeteroRing, nF8HeteroRing, nF9HeteroRing, nF10HeteroRing, nF11HeteroRing, nF12HeteroRing, nFG12HeteroRing, 
 * nTHeteroRing, nT4HeteroRing, nT5HeteroRing, nT6HeteroRing, nT7HeteroRing, nT8HeteroRing, nT9HeteroRing, nT10HeteroRing, nT11HeteroRing, nT12HeteroRing, nTG12HeteroRing.
 * The first set of rings is the Smallest Set of Smallest Rings.
 * The second set is the set of fused rings.
 * The third set of rings is the Set of all Rings (i.e. first set + second set).
 * Thus fused rings will also result in a count for a higher-membered ring.
 * For instance, a bicyclohexane will have n6Ring as 2, nF10Ring as 1, nT6Ring as 2 and nT10Ring as 1.
 * <p/>
 *
 * @author Yap Chun Wei
 * @cdk.created 2010-05-01
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:nRing
 * @cdk.keyword molecular type nRing descriptor
 * @cdk.keyword descriptor
 */
public class RingCountDescriptor implements IMolecularDescriptor {
    
    public static final String[] names = {
                                            "nRing",
                                            "n3Ring",
                                            "n4Ring",
                                            "n5Ring",
                                            "n6Ring",
                                            "n7Ring",
                                            "n8Ring",
                                            "n9Ring",
                                            "n10Ring",
                                            "n11Ring",
                                            "n12Ring",
                                            "nG12Ring",
                                            "nFRing",
                                            "nF4Ring",
                                            "nF5Ring",
                                            "nF6Ring",
                                            "nF7Ring",
                                            "nF8Ring",
                                            "nF9Ring",
                                            "nF10Ring",
                                            "nF11Ring",
                                            "nF12Ring",
                                            "nFG12Ring",
                                            "nTRing",
                                            "nT4Ring",
                                            "nT5Ring",
                                            "nT6Ring",
                                            "nT7Ring",
                                            "nT8Ring",
                                            "nT9Ring",
                                            "nT10Ring",
                                            "nT11Ring",
                                            "nT12Ring",
                                            "nTG12Ring",
                                            "nHeteroRing",
                                            "n3HeteroRing",
                                            "n4HeteroRing",
                                            "n5HeteroRing",
                                            "n6HeteroRing",
                                            "n7HeteroRing",
                                            "n8HeteroRing",
                                            "n9HeteroRing",
                                            "n10HeteroRing",
                                            "n11HeteroRing",
                                            "n12HeteroRing",
                                            "nG12HeteroRing",
                                            "nFHeteroRing",
                                            "nF4HeteroRing",
                                            "nF5HeteroRing",
                                            "nF6HeteroRing",
                                            "nF7HeteroRing",
                                            "nF8HeteroRing",
                                            "nF9HeteroRing",
                                            "nF10HeteroRing",
                                            "nF11HeteroRing",
                                            "nF12HeteroRing",
                                            "nFG12HeteroRing",
                                            "nTHeteroRing",
                                            "nT4HeteroRing",
                                            "nT5HeteroRing",
                                            "nT6HeteroRing",
                                            "nT7HeteroRing",
                                            "nT8HeteroRing",
                                            "nT9HeteroRing",
                                            "nT10HeteroRing",
                                            "nT11HeteroRing",
                                            "nT12HeteroRing",
                                            "nTG12HeteroRing"
                                       };

    public RingCountDescriptor() {

    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "nRing",
                this.getClass().getName(),
                "$Id: RingCountDescriptor.java 1 2010-05-01 22:05:01Z yapchunwei $",
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
        // Find Smallest Set of Smallest Rings.
        SSSRFinder finder = new SSSRFinder(container);
        IRingSet sssr = finder.findEssentialRings();

        // ringCounts: Index 0, 1, is not used.
        // ringCounts: Index 2 represents nRing
        // ringCounts: Index 3 to 12 represents n3Ring to n12Ring
        // ringCounts: Index 13 represents nG12Ring
        int[] ringCounts = new int[14];
        int[] heteroRingCounts = new int[14];
        for (int i=0; i<ringCounts.length; ++i) 
        {
            ringCounts[i] = 0;
            heteroRingCounts[i] = 0;
        }
        for (int i=0; i<sssr.getAtomContainerCount(); ++i)
        {
            int size = sssr.getAtomContainer(i).getAtomCount();
            ++ringCounts[2];
            boolean isHetero = isHeteroRing(sssr.getAtomContainer(i));
            if (isHetero) ++heteroRingCounts[2];
            if (size>12) 
            {
                ++ringCounts[13];
                if (isHetero) ++heteroRingCounts[13];
            }
            else if (size>=3) 
            {
                ++ringCounts[size];
                if (isHetero) ++heteroRingCounts[size];
            }
        }

        // Store set of smallest ring information.
        DoubleArrayResult retval = new DoubleArrayResult();
        for (int i=2; i<14; ++i)
        {
            retval.add(ringCounts[i]);
        }

        // Find Set of all rings
        AllRingsFinder arf = new AllRingsFinder();
        IRingSet allRings;
        try {
            allRings = arf.findAllRings(container);
        } catch (CDKException e) {
            Logger.getLogger("global").log(Level.SEVERE, null, e);
            return getDummyDescriptorValue(e);
        }

        // TRingCounts: Index 0, 1, 2 is not used.
        // TRingCounts: Index 3 represents nTRing
        // TRingCounts: Index 4 to 12 represents nT4Ring to nT12Ring
        // TRingCounts: Index 13 represents nTG12Ring
        int[] TRingCounts = new int[14];
        int[] heteroTRingCounts = new int[14];
        for (int i=0; i<TRingCounts.length; ++i) 
        {
            TRingCounts[i] = 0;
            heteroTRingCounts[i] = 0;
        }
        for (int i=0; i<allRings.getAtomContainerCount(); ++i)
        {
            int size = allRings.getAtomContainer(i).getAtomCount();
            ++TRingCounts[3];
            boolean isHetero = isHeteroRing(allRings.getAtomContainer(i));
            if (isHetero) ++heteroTRingCounts[2];
            if (size>12) 
            {
                ++TRingCounts[13];
                if (isHetero) ++heteroTRingCounts[13];
            }
            else if (size>=4) 
            {
                ++TRingCounts[size];
                if (isHetero) ++heteroTRingCounts[size];
            }
        }

        // Store fused rings information.
        if (TRingCounts[3]-ringCounts[2]>0) retval.add(TRingCounts[3]-ringCounts[2]);
        else retval.add(0);
        for (int i=4; i<14; ++i)
        {
            if (TRingCounts[i]-ringCounts[i]>0)
            {
                retval.add(TRingCounts[i]-ringCounts[i]);
            }
            else
            {
                retval.add(0);
            }
        }

        // Store Set of all rings
        for (int i=3; i<14; ++i)
        {
            retval.add(TRingCounts[i]);
        }

        // Store hetero rings information
        // Store set of smallest ring information.
        for (int i=2; i<14; ++i)
        {
            retval.add(heteroRingCounts[i]);
        }
        
        // Store fused rings information.
        if (heteroTRingCounts[3]-heteroRingCounts[2]>0) retval.add(heteroTRingCounts[3]-heteroRingCounts[2]);
        else retval.add(0);
        for (int i=4; i<14; ++i)
        {
            if (heteroTRingCounts[i]-heteroRingCounts[i]>0)
            {
                retval.add(heteroTRingCounts[i]-heteroRingCounts[i]);
            }
            else
            {
                retval.add(0);
            }
        }

        // Store Set of all rings
        for (int i=3; i<14; ++i)
        {
            retval.add(heteroTRingCounts[i]);
        }
        
        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, names);
    }
    
    private boolean isHeteroRing(IAtomContainer ring)
    {
        for (int i = 0; i < ring.getAtomCount(); i++) 
        {
            String symbol = ring.getAtom(i).getSymbol();
            if (symbol.equals("N") ||
                symbol.equals("O") ||   
                symbol.equals("P") ||    
                symbol.equals("S") ||
                symbol.equals("F") ||
                symbol.equals("Cl") ||
                symbol.equals("Br") ||
                symbol.equals("I") ||
                symbol.equals("At") ||
                symbol.equals("Uus"))
            {
                return true;
            }
        }
        return false;
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

