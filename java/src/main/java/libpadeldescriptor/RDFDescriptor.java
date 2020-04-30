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
 * RDF descriptors
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-12
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:RDF
 * @cdk.keyword molecular type RDF descriptor
 * @cdk.keyword descriptor
 */

public class RDFDescriptor implements IMolecularDescriptor {

    private static final int maxLength = 30;
    public String[] names;
    private static final String[] wtypes = {"u", "m", "v", "e", "p", "i", "s"};

    public RDFDescriptor()
    {
        names = new String[wtypes.length*maxLength];
        int index = 0;

        for (String suffix : wtypes)
        {
            for (int k=0; k<maxLength; ++k)
            {
                names[index++] = "RDF" + (k*5+10) + suffix;
            }
        }
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "RDF",
                this.getClass().getName(),
                "$Id: RDFDescriptor.java 1 2014-06-12 15:30:00Z yapchunwei $",
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
        for (int i=0; i<natom; ++i) w[6][i] /= meanCistate;

        double[][] RDF = new double[maxLength][wtypes.length]; // [][0] = unweighted, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        final double B = 100;
        double R = 1.0;
        for (int k=0; k<maxLength; ++k)
        {
            for (int i=0; i<natom; ++i)
            {
                for (int j=i+1; j<natom; ++j)
                {
                    Point3d a = container.getAtom(i).getPoint3d();
                    Point3d b = container.getAtom(j).getPoint3d();
                    double rij = Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));

                    for (int t=0; t<wtypes.length; ++t)
                    {
                        RDF[k][t] += w[t][i] * w[t][j] * Math.exp(-B*(R-rij)*(R-rij));
                    }
                }
            }
            R += 0.5;
        }

        DoubleArrayResult retval = new DoubleArrayResult();
        for (int t=0; t<wtypes.length; ++t)
        {
            for (int k=0; k<maxLength; ++k)
            {
                retval.add(RDF[k][t]);
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
