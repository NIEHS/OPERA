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


import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;

/**
 * Some constitutional descriptors
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-09
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:constitutional
 * @cdk.keyword molecular type constitutional descriptor
 * @cdk.keyword descriptor
 */

public class ConstitutionalDescriptor implements IMolecularDescriptor {

    public String[] names;
    private static final String[] wtypes = {"v", "se", "pe", "are", "p", "i"};

    public ConstitutionalDescriptor()
    {
        names = new String[wtypes.length*2];
        int index = 0;

        for (String suffix : wtypes)
        {
            names[index++] = "S" + suffix;
        }

        for (String suffix : wtypes)
        {
            names[index++] = "M" + suffix;
        }
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "constitutional",
                this.getClass().getName(),
                "$Id: ConstitutionalDescriptor.java 1 2014-06-09 17:40:00Z yapchunwei $",
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
        double[] volumes = new double[natom];
        double[] sandersonelnegs = new double[natom];
        double[] paulingelnegs = new double[natom];
        double[] allredrochowelnegs = new double[natom];
        double[] polarizibilities = new double[natom];
        double[] ionpotentials = new double[natom];
        for (int i=0; i<natom; ++i)
        {
            int atomicNumber = container.getAtom(i).getAtomicNumber();
            if (atomicNumber<AtomConstants.volumes.length)
            {
                volumes[i] = AtomConstants.volumes[atomicNumber] / AtomConstants.volumes[6]; // Scaled on carbon atom.
            }
            else volumes[i] = Double.NaN;

            if (atomicNumber<AtomConstants.sandersonelnegativities.length)
            {
                sandersonelnegs[i] = AtomConstants.sandersonelnegativities[atomicNumber] / AtomConstants.sandersonelnegativities[6]; // Scaled on carbon atom.
            }
            else sandersonelnegs[i] = Double.NaN;

            if (atomicNumber<AtomConstants.paulingelnegativities.length)
            {
                paulingelnegs[i] = AtomConstants.paulingelnegativities[atomicNumber] / AtomConstants.paulingelnegativities[6]; // Scaled on carbon atom.
            }
            else paulingelnegs[i] = Double.NaN;

            if (atomicNumber<AtomConstants.allredrochowelnegativities.length)
            {
                allredrochowelnegs[i] = AtomConstants.allredrochowelnegativities[atomicNumber] / AtomConstants.allredrochowelnegativities[6]; // Scaled on carbon atom.
            }
            else allredrochowelnegs[i] = Double.NaN;

            if (atomicNumber<AtomConstants.polarizabilities.length)
            {
                polarizibilities[i] = AtomConstants.polarizabilities[atomicNumber] / AtomConstants.polarizabilities[6]; // Scaled on carbon atom.
            }
            else polarizibilities[i] = Double.NaN;

            if (atomicNumber<AtomConstants.ionpotentials.length)
            {
                ionpotentials[i] = AtomConstants.ionpotentials[atomicNumber] / AtomConstants.polarizabilities[6]; // Scaled on carbon atom.
            }
            else ionpotentials[i] = Double.NaN;
        }

        double[] S = new double[wtypes.length];
        for (int i=0; i<natom; ++i)
        {
            S[0] += volumes[i];
            S[1] += sandersonelnegs[i];
            S[2] += paulingelnegs[i];
            S[3] += allredrochowelnegs[i];
            S[4] += polarizibilities[i];
            S[5] += ionpotentials[i];
        }

        double[] M = new double[wtypes.length];
        for (int i=0; i<wtypes.length; ++i)
        {
            M[i] = S[i] / natom;
        }

        DoubleArrayResult retval = new DoubleArrayResult();
        for (int i=0; i<wtypes.length; ++i)
        {
            retval.add(S[i]);
        }

        for (int i=0; i<wtypes.length; ++i)
        {
            retval.add(M[i]);
        }

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, getDescriptorNames());
    }

    private DescriptorValue getDummyDescriptorValue(Exception e) {
        int ndesc = getDescriptorNames().length;
        DoubleArrayResult results = new DoubleArrayResult(ndesc);
        for (int i = 0; i < ndesc; i++) results.add(Double.NaN);
        return new DescriptorValue(getSpecification(), getParameterNames(),
                getParameters(), results, getDescriptorNames(), e);
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
