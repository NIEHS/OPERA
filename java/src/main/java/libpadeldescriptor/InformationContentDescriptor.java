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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscience.cdk1.CDKConstants;
import org.openscience.cdk1.config.IsotopeFactory;
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

/**
 * Information content descriptors
 * 
 * @author Yap Chun Wei
 * @cdk.created 2014-06-03
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:InformationContent
 * @cdk.keyword molecular type InformationContent descriptor
 * @cdk.keyword descriptor
 */

public class InformationContentDescriptor implements IMolecularDescriptor {
   
    public static final String[] names = {
                                            "IC0",
                                            "IC1",
                                            "IC2",
                                            "IC3",
                                            "IC4",
                                            "IC5",
                                            "TIC0",
                                            "TIC1",
                                            "TIC2",
                                            "TIC3",
                                            "TIC4",
                                            "TIC5",
                                            "SIC0",
                                            "SIC1",
                                            "SIC2",
                                            "SIC3",
                                            "SIC4",
                                            "SIC5",
                                            "CIC0",
                                            "CIC1",
                                            "CIC2",
                                            "CIC3",
                                            "CIC4",
                                            "CIC5",
                                            "BIC0",
                                            "BIC1",
                                            "BIC2",
                                            "BIC3",
                                            "BIC4",
                                            "BIC5",
//                                            "RIC0",
//                                            "RIC1",
//                                            "RIC2",
//                                            "RIC3",
//                                            "RIC4",
//                                            "RIC5",
                                            "MIC0",
                                            "MIC1",
                                            "MIC2",
                                            "MIC3",
                                            "MIC4",
                                            "MIC5",
                                            "ZMIC0",
                                            "ZMIC1",
                                            "ZMIC2",
                                            "ZMIC3",
                                            "ZMIC4",
                                            "ZMIC5"
                                         };

    public InformationContentDescriptor() {
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "InformationContent",
                this.getClass().getName(),
                "$Id: InformationContentDescriptor.java 1 2014-06-03 15:00:00Z yapchunwei $",
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
        HashMap<ArrayList<Coordinate>,Integer> classes0 = getClasses(container, 0);
        HashMap<ArrayList<Coordinate>,Integer> classes1 = getClasses(container, 1);
        HashMap<ArrayList<Coordinate>,Integer> classes2 = getClasses(container, 2);
        HashMap<ArrayList<Coordinate>,Integer> classes3 = getClasses(container, 3);
        HashMap<ArrayList<Coordinate>,Integer> classes4 = getClasses(container, 4);
        HashMap<ArrayList<Coordinate>,Integer> classes5 = getClasses(container, 5);
        double IC0 = evalInfoContent(classes0, container);
        double IC1 = evalInfoContent(classes1, container);
        double IC2 = evalInfoContent(classes2, container);
        double IC3 = evalInfoContent(classes3, container);
        double IC4 = evalInfoContent(classes4, container);
        double IC5 = evalInfoContent(classes5, container);
        
        int A = container.getAtomCount();
        double TIC0 = A * IC0;
        double TIC1 = A * IC1;
        double TIC2 = A * IC2;
        double TIC3 = A * IC3;
        double TIC4 = A * IC4;
        double TIC5 = A * IC5;
        
        double log2A = Math.log(A) / Math.log(2);
        double SIC0 = IC0 / log2A;
        double SIC1 = IC1 / log2A;
        double SIC2 = IC2 / log2A;
        double SIC3 = IC3 / log2A;
        double SIC4 = IC4 / log2A;
        double SIC5 = IC5 / log2A;
        
        double CIC0 = log2A - IC0;
        double CIC1 = log2A - IC1;
        double CIC2 = log2A - IC2;
        double CIC3 = log2A - IC3;
        double CIC4 = log2A - IC4;
        double CIC5 = log2A - IC5;
        
        double log2B = 0.0;
        for (IBond bond : container.bonds())
        {
            Order bondorder = bond.getOrder();
            if (bond.getFlag(CDKConstants.ISAROMATIC)) log2B += 1.5;
            else if (bondorder==Order.SINGLE) log2B += 1;     
            else if (bondorder==Order.DOUBLE) log2B += 2;
            else if (bondorder==Order.TRIPLE) log2B += 3;
            else if (bondorder==Order.QUADRUPLE) log2B += 4;
        }
        log2B = Math.log(log2B) / Math.log(2);
        double BIC0 = IC0 / log2B;
        double BIC1 = IC1 / log2B;
        double BIC2 = IC2 / log2B;
        double BIC3 = IC3 / log2B;
        double BIC4 = IC4 / log2B;
        double BIC5 = IC5 / log2B;
        
//        double RIC0 = 1.0 - SIC0;
//        double RIC1 = 1.0 - SIC1;
//        double RIC2 = 1.0 - SIC2;
//        double RIC3 = 1.0 - SIC3;
//        double RIC4 = 1.0 - SIC4;
//        double RIC5 = 1.0 - SIC5;
        
        double MIC0 = evalModInfoContent(classes0, container);
        double MIC1 = evalModInfoContent(classes1, container);
        double MIC2 = evalModInfoContent(classes2, container);
        double MIC3 = evalModInfoContent(classes3, container);
        double MIC4 = evalModInfoContent(classes4, container);
        double MIC5 = evalModInfoContent(classes5, container);
        
        double ZMIC0 = evalZModInfoContent(classes0, container);
        double ZMIC1 = evalZModInfoContent(classes1, container);
        double ZMIC2 = evalZModInfoContent(classes2, container);
        double ZMIC3 = evalZModInfoContent(classes3, container);
        double ZMIC4 = evalZModInfoContent(classes4, container);
        double ZMIC5 = evalZModInfoContent(classes5, container);

        DoubleArrayResult retval = new DoubleArrayResult();
        retval.add(IC0);
        retval.add(IC1);
        retval.add(IC2);
        retval.add(IC3);
        retval.add(IC4);
        retval.add(IC5);
        retval.add(TIC0);
        retval.add(TIC1);
        retval.add(TIC2);
        retval.add(TIC3);
        retval.add(TIC4);
        retval.add(TIC5);
        retval.add(SIC0);
        retval.add(SIC1);
        retval.add(SIC2);
        retval.add(SIC3);
        retval.add(SIC4);
        retval.add(SIC5);
        retval.add(CIC0);
        retval.add(CIC1);
        retval.add(CIC2);
        retval.add(CIC3);
        retval.add(CIC4);
        retval.add(CIC5);
        retval.add(BIC0);
        retval.add(BIC1);
        retval.add(BIC2);
        retval.add(BIC3);
        retval.add(BIC4);
        retval.add(BIC5);
//        retval.add(RIC0);
//        retval.add(RIC1);
//        retval.add(RIC2);
//        retval.add(RIC3);
//        retval.add(RIC4);
//        retval.add(RIC5);
        retval.add(MIC0);
        retval.add(MIC1);
        retval.add(MIC2);
        retval.add(MIC3);
        retval.add(MIC4);
        retval.add(MIC5);
        retval.add(ZMIC0);
        retval.add(ZMIC1);
        retval.add(ZMIC2);
        retval.add(ZMIC3);
        retval.add(ZMIC4);
        retval.add(ZMIC5);

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, getDescriptorNames());
    }
     
    private class Coordinate implements Comparable<Coordinate>
    {
        public int a = 0;       // Chemical element
        public int delta = 0;   // vertex degree
        public Order pi = null; // bonder order of edge with previous neighbour
        
        @Override
        public boolean equals(Object o) 
        {
            if (!(o instanceof Coordinate)) return false;
            Coordinate c = (Coordinate) o;
            return a==c.a && delta==c.delta && pi==c.pi;
        }

        @Override
        public int hashCode() 
        {
            int hash = 3;
            hash = 31 * hash + this.a;
            hash = 31 * hash + this.delta;
            hash = 31 * hash + (this.pi != null ? this.pi.hashCode() : 0);
            return hash;
        }

        @Override
        public int compareTo(Coordinate o) 
        {
            int retVal = a - o.a;
            if (retVal==0) retVal = delta - o.delta;
            if (retVal==0 && pi!=null && o.pi!=null) retVal = pi.compareTo(o.pi);
            return retVal;
        }        
    }
    
    private HashMap<ArrayList<Coordinate>,Integer> getClasses(IAtomContainer atomContainer, Integer pathLength)
    {
        HashMap<ArrayList<Coordinate>,Integer> classes = new HashMap<ArrayList<Coordinate>,Integer>();
        for (IAtom atom : atomContainer.atoms())
        {
            List<List<IAtom> > paths = PathTools.getPathsOfLength(atomContainer, atom, pathLength);
            if (paths.isEmpty()) continue;
            ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
            for (List<IAtom> path : paths)
            {
                IAtom prevAtom = null;
                for (IAtom currentAtom : path)
                {
                    if (prevAtom!=null)
                    {
                        Coordinate coord = new Coordinate();
                        coord.a = currentAtom.getAtomicNumber();
                        coord.delta = atomContainer.getConnectedAtomsCount(currentAtom);
                        coord.pi = atomContainer.getBond(currentAtom, prevAtom).getOrder();
                        coordinates.add(coord);
                    }
                    prevAtom = currentAtom;                    
                }
            }
            Collections.sort(coordinates);
            ArrayList<Coordinate> key = new ArrayList<Coordinate>();
            Coordinate coord = new Coordinate();
            coord.a = atom.getAtomicNumber();
            key.add(coord);
            key.addAll(coordinates);
            if (classes.containsKey(key)) classes.put(key, classes.get(key)+1);
            else classes.put(key, 1);
        }
        
        return classes;
    }
    
    private double evalInfoContent(HashMap<ArrayList<Coordinate>,Integer> classes, IAtomContainer atomContainer)
    {   
        int maxAtoms = atomContainer.getAtomCount();
        double sum = 0;
        for (Integer n : classes.values())
        {
            double p = n.doubleValue() / maxAtoms;
            sum += p * Math.log(p) / Math.log(2);
        } 
        return -sum;
    }
    
    private double evalModInfoContent(HashMap<ArrayList<Coordinate>,Integer> classes, IAtomContainer atomContainer)
    {   
        int maxAtoms = atomContainer.getAtomCount();
        double sum = 0;
        for (ArrayList<Coordinate> coordinates : classes.keySet())
        {
            Integer n = classes.get(coordinates);
            double p = n.doubleValue() / maxAtoms;
            double m = 0.0;
            try 
            {
                m = IsotopeFactory.getInstance(atomContainer.getBuilder()).getMajorIsotope(coordinates.get(0).a).getExactMass();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(InformationContentDescriptor.class.getName()).log(Level.SEVERE, null, ex);                
            }
            sum += m * p * Math.log(p) / Math.log(2);
        } 
        return -sum;
    }
    
    private double evalZModInfoContent(HashMap<ArrayList<Coordinate>,Integer> classes, IAtomContainer atomContainer)
    {   
        int maxAtoms = atomContainer.getAtomCount();
        double sum = 0;
        for (ArrayList<Coordinate> coordinates : classes.keySet())
        {
            Integer n = classes.get(coordinates);
            double p = n.doubleValue() / maxAtoms;
            sum += coordinates.get(0).a * n * p * Math.log(p) / Math.log(2);
        } 
        return -sum;
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
