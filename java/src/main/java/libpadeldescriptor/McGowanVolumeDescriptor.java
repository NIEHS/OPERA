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


import java.util.Iterator;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.smiles.smarts.SMARTSQueryTool;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;


/**
 * McGowan characteristic volume
 * <p/>
 * The code currently computes the McGowan characteristic volume. 
 * Abraham MH, McGowan JC. The use of characteristic volumes to measure cavity terms in reversed phase liquid chromatography. Chromatographia. 1987;23:243-6.
 * <p/>
 * The value returned is McGowan_V
 * <p/>
 *
 * @author Yap Chun Wei
 * @cdk.created 2008-07-07
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:McGowanV
 * @cdk.keyword molecular type McGowanV descriptor
 * @cdk.keyword descriptor
 */
public class McGowanVolumeDescriptor implements IMolecularDescriptor {
    
    public static final String[] names = {
                                            "McGowan_Volume"
                                         };
  
    private static String[] atomTypes = {
                                            "[#6]", // C
                                            "[#7]", // N
                                            "[#8]", // O
                                            "[#9]", // F
                                            "[#1]", // H
                                            "[#14]",// Si
                                            "[#15]",// P
                                            "[#16]",// S
                                            "[#17]",// Cl
                                            "[#5]", // B
                                            "[#32]",// Ge
                                            "[#33]",// As
                                            "[#34]",// Se
                                            "[#35]",// Br
                                            "[#50]",// Sn
                                            "[#51]",// Sb
                                            "[#52]",// Te
                                            "[#53]",// I		     
                                         };
    
    private static double[] coefAtomTypes = {    
                                                16.35,	// C
                                                14.39,	// N
                                                12.43,	// O
                                                10.48,	// F
                                                8.71,	// H
                                                26.83,	// Si
                                                24.87,	// P
                                                22.91,	// S
                                                20.95,	// Cl
                                                18.32,	// B
                                                31.02,	// Ge
                                                29.42,	// As
                                                27.81,	// Se
                                                26.21,	// Br
                                                39.35,	// Sn
                                                37.74,	// Sb
                                                36.14,	// Te
                                                34.53,	// I	
                                             };

    public McGowanVolumeDescriptor() {

    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "McGowen_V",
                this.getClass().getName(),
                "$Id: McGowenVolumeDescriptor.java 1 2008-07-07 06:50:01Z yapchunwei $",
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
        try
        {
            // Calculate McGowan characteristic volume, V
            SMARTSQueryTool sqt = new SMARTSQueryTool("C");        
            int maxAtomTypes = atomTypes.length;
            double V = 0.0;
            for (int i=0; i<maxAtomTypes; ++i)
            {
                if (atomTypes[i].equals("[#1]")) 
                {
                    // Have to count hydrogen atoms separately because SMARTS cannot work properly on hydrogen atoms alone.
                    for (Iterator<IAtom> atoms = container.atoms().iterator(); atoms.hasNext();) 
                    {
                        IAtom atom = atoms.next();
                        if (!atom.getSymbol().equals("H"))
                        {
                            V += AtomContainerManipulator.countHydrogens(container, atom) * coefAtomTypes[i];
                        }                    
                    }
                }
                else
                {
                    sqt.setSmarts(atomTypes[i]);
                    if (sqt.matches(container))
                    {
                         V += sqt.getUniqueMatchingAtoms().size() * coefAtomTypes[i];
                    }
                }
            }
            V -= container.getBondCount() * 6.56;
            V /= 100.0;        
            DoubleArrayResult retval = new DoubleArrayResult();
            retval.add(V);

            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, names);
        }
        catch (Exception e)
        {
            return getDummyDescriptorValue(new CDKException("Error in SMARTSQueryTool: " + e.getMessage()));
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

