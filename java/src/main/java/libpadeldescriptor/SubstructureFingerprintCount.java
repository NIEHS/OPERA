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
import org.openscience.cdk1.fingerprint.StandardSubstructureSets;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.smiles.smarts.SMARTSQueryTool;


/**
 * Count of substructures.
 *
 * @author Yap Chun Wei
 * @cdk.created 2010-05-01
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.keyword descriptor
 */
public class SubstructureFingerprintCount implements IMolecularDescriptor {

    private String[] names;
    private String[] smarts;

    public SubstructureFingerprintCount() {
        try {
            smarts = StandardSubstructureSets.getFunctionalGroupSMARTS();
        } catch (Exception e) {
            smarts = null;
        }
    }

    public SubstructureFingerprintCount(String[] smarts) {
        this.smarts = smarts;
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "SubstructureCount",
                this.getClass().getName(),
                "$Id: SubstructureCountDescriptor.java 1 2010-05-01 22:05:01Z yapchunwei $",
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

    public void setDescriptorNames(String[] names) {
        this.names = names;
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
        if (smarts == null) {
            return getDummyDescriptorValue(new CDKException("No substructures were defined"));
        }

        try
        {
            DoubleArrayResult retval = new DoubleArrayResult();
            SMARTSQueryTool sqt = new SMARTSQueryTool("C");
            for (int i = 0; i < smarts.length; i++) {
                String pattern = smarts[i];

                sqt.setSmarts(pattern);
                if (sqt.matches(container))
                {
                    retval.add(sqt.getUniqueMatchingAtoms().size());
                }
                else
                {
                    retval.add(0);
                }
            }
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

