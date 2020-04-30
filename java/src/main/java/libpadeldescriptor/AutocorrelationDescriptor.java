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


import org.openscience.cdk1.Molecule;
import org.openscience.cdk1.charges.GasteigerMarsiliPartialCharges;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.graph.matrix.TopologicalMatrix;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;

/**
 * Autocorrelation descriptors
 *
 * @author Yap Chun Wei
 * @cdk.created 2014-06-06
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:Autocorrelation
 * @cdk.keyword molecular type Autocorrelation descriptor
 * @cdk.keyword descriptor
 */

public class AutocorrelationDescriptor implements IMolecularDescriptor {

    private static final int maxLag = 8;
    public String[] names;
    private static final String[] atstypes = {"ATS", "AATS", "ATSC", "AATSC", "MATS", "GATS"};
    private static final String[] wtypes = {"c", "m", "v", "e", "p", "i", "s"};

    public AutocorrelationDescriptor()
    {
        names = new String[(maxLag+1)*2*(wtypes.length-1)+(maxLag+1)*2*wtypes.length+maxLag*2*wtypes.length];
        int index = 0;

        // No weighted by charges descriptors for ATS and AATS because charges are centered.
        // So ATS and AATS weighted by charges descriptors will be the same as ATSC and AATSC weighted by charges descriptors.
        for (int i=0; i<2; ++i)
        {
            String prefix = atstypes[i];
            for (int j=1; j<wtypes.length; ++j)
            {
                String suffix = wtypes[j];
                for (int k=0; k<=maxLag; ++k)
                {
                    names[index++] = prefix + k + suffix;
                }
            }
        }

        for (int i=2; i<4; ++i)
        {
            String prefix = atstypes[i];
            for (String suffix : wtypes)
            {
                for (int k=0; k<=maxLag; ++k)
                {
                    names[index++] = prefix + k + suffix;
                }
            }
        }

        // No MATS0 because it will have a value of 1
        // No GATS0 because it will have a value of 0
        for (int i=4; i<6; ++i)
        {
            String prefix = atstypes[i];
            for (String suffix : wtypes)
            {
                for (int k=1; k<=maxLag; ++k)
                {
                    names[index++] = prefix + k + suffix;
                }
            }
        }
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "Autocorrelation",
                this.getClass().getName(),
                "$Id: AutocorrelationDescriptor.java 1 2014-06-06 12:00:00Z yapchunwei $",
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
        double[] c = new double[natom];
        double[] m = new double[natom];
        double[] v = new double[natom];
        double[] e = new double[natom];
        double[] p = new double[natom];
        double[] ip = new double[natom];
        double[] s = new double[natom];
        try
        {
            Molecule mol = new Molecule((IAtomContainer)container.clone());
            GasteigerMarsiliPartialCharges peoe = new GasteigerMarsiliPartialCharges();
            peoe.assignGasteigerMarsiliSigmaPartialCharges(mol, true);
            IntrinsicStateDescriptor isd = new IntrinsicStateDescriptor();
            for (int i=0; i<natom; ++i)
            {
                int atomicNumber = container.getAtom(i).getAtomicNumber();
                c[i] = mol.getAtom(i).getCharge();

                if (atomicNumber<AtomConstants.masses.length) m[i] = AtomConstants.masses[atomicNumber];
                else m[i] = Double.NaN;

                if (atomicNumber<AtomConstants.volumes.length) v[i] = AtomConstants.volumes[atomicNumber];
                else v[i] = Double.NaN;

                if (atomicNumber<AtomConstants.sandersonelnegativities.length) e[i] = AtomConstants.sandersonelnegativities[atomicNumber];
                else e[i] = Double.NaN;

                if (atomicNumber<AtomConstants.polarizabilities.length) p[i] = AtomConstants.polarizabilities[atomicNumber];
                else p[i] = Double.NaN;

                if (atomicNumber<AtomConstants.ionpotentials.length) ip[i] = AtomConstants.ionpotentials[atomicNumber];
                else ip[i] = Double.NaN;

                IAtom atom = container.getAtom(i);
                if (atomicNumber==1)
                {
                    s[i] = 1;
                }
                else
                {
                    DoubleArrayResult results = (DoubleArrayResult)isd.calculate(atom, container).getValue();
                    s[i] = results.get(0);
                }
            }
        }
        catch (Exception ex)
        {
            DoubleArrayResult result = new DoubleArrayResult();
            for (int i=0; i<getDescriptorNames().length; i++) result.add(Double.NaN);
            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                    result, getDescriptorNames(), new CDKException("Error during calculation of properties: " + ex.getMessage(), ex));
        }

        double[] cc = getCenteredValues(c);
        double[] cm = getCenteredValues(m);
        double[] cv = getCenteredValues(v);
        double[] ce = getCenteredValues(e);
        double[] cp = getCenteredValues(p);
        double[] cip = getCenteredValues(ip);
        double[] cs = getCenteredValues(s);

        double[][] ATS = new double[maxLag+1][wtypes.length]; // [][0] = charges (not used), [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[][] AATS = new double[maxLag+1][wtypes.length]; // [][0] = charges (not used), [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[][] ATSC = new double[maxLag+1][wtypes.length]; // [][0] = charges, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[][] AATSC = new double[maxLag+1][wtypes.length]; // [][0] = charges, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[][] MATS = new double[maxLag+1][wtypes.length]; // [][0] = charges, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        double[][] GATS = new double[maxLag+1][wtypes.length]; // [][0] = charges, [][1] = masses, [][2] = volumes, [][3] = electronegativities, [][4] = polariziabilities, [][5] = ionization potential, [][6] = I-state
        for (int i=0; i<natom; ++i)
        {
            ATS[0][0] += c[i] * c[i];
            ATS[0][1] += m[i] * m[i];
            ATS[0][2] += v[i] * v[i];
            ATS[0][3] += e[i] * e[i];
            ATS[0][4] += p[i] * p[i];
            ATS[0][5] += ip[i] * ip[i];
            ATS[0][6] += s[i] * s[i];

            ATSC[0][0] += cc[i] * cc[i];
            ATSC[0][1] += cm[i] * cm[i];
            ATSC[0][2] += cv[i] * cv[i];
            ATSC[0][3] += ce[i] * ce[i];
            ATSC[0][4] += cp[i] * cp[i];
            ATSC[0][5] += cip[i] * cip[i];
            ATSC[0][6] += cs[i] * cs[i];
        }

        for (int i=0; i<wtypes.length; ++i)
        {
            AATS[0][i] = ATS[0][i] / natom;
            AATSC[0][i] = ATSC[0][i] / natom;
            MATS[0][i] = AATSC[0][i];
            GATS[0][i] = ATSC[0][i] / (natom-1);
        }

        int[][] distMatrix = TopologicalMatrix.getMatrix(container);
        for (int k=1; k<=maxLag; ++k)
        {
            int maxkVertexPairs = 0;
            for (int i=0; i<natom; ++i)
            {
                for (int j=i+1; j<natom; ++j)
                {
                    if (distMatrix[i][j]==k)
                    {
                        ATS[k][0] += c[i] * c[j];
                        ATS[k][1] += m[i] * m[j];
                        ATS[k][2] += v[i] * v[j];
                        ATS[k][3] += e[i] * e[j];
                        ATS[k][4] += p[i] * p[j];
                        ATS[k][5] += ip[i] * ip[j];
                        ATS[k][6] += s[i] * s[j];

                        ATSC[k][0] += cc[i] * cc[j];
                        ATSC[k][1] += cm[i] * cm[j];
                        ATSC[k][2] += cv[i] * cv[j];
                        ATSC[k][3] += ce[i] * ce[j];
                        ATSC[k][4] += cp[i] * cp[j];
                        ATSC[k][5] += cip[i] * cip[j];
                        ATSC[k][6] += cs[i] * cs[j];

                        GATS[k][0] += (c[i]-c[j])*(c[i]-c[j]);
                        GATS[k][1] += (m[i]-m[j])*(m[i]-m[j]);
                        GATS[k][2] += (v[i]-v[j])*(v[i]-v[j]);
                        GATS[k][3] += (e[i]-e[j])*(e[i]-e[j]);
                        GATS[k][4] += (p[i]-p[j])*(p[i]-p[j]);
                        GATS[k][5] += (ip[i]-ip[j])*(ip[i]-ip[j]);
                        GATS[k][6] += (s[i]-s[j])*(s[i]-s[j]);

                        ++maxkVertexPairs;
                    }
                }
            }

            for (int i=0; i<wtypes.length; ++i)
            {
                AATS[k][i] = maxkVertexPairs>0 ? ATS[k][i] / maxkVertexPairs : 0;
                AATSC[k][i] = maxkVertexPairs>0 ? ATSC[k][i] / maxkVertexPairs : 0;
                MATS[k][i] = MATS[0][i]>0 ? AATSC[k][i] / MATS[0][i] : 0;
                GATS[k][i] = maxkVertexPairs>0 & GATS[0][i]>0 ? GATS[k][i] / (2*maxkVertexPairs*GATS[0][i]) : 0;
            }
        }

        DoubleArrayResult retval = new DoubleArrayResult();
        for (int i=1; i<wtypes.length; ++i)
        {
            for (int k=0; k<=maxLag; ++k)
            {
                retval.add(ATS[k][i]);
            }
        }

        for (int i=1; i<wtypes.length; ++i)
        {
            for (int k=0; k<=maxLag; ++k)
            {
                retval.add(AATS[k][i]);
            }
        }

        for (int i=0; i<wtypes.length; ++i)
        {
            for (int k=0; k<=maxLag; ++k)
            {
                retval.add(ATSC[k][i]);
            }
        }

        for (int i=0; i<wtypes.length; ++i)
        {
            for (int k=0; k<=maxLag; ++k)
            {
                retval.add(AATSC[k][i]);
            }
        }

        for (int i=0; i<wtypes.length; ++i)
        {
            for (int k=1; k<=maxLag; ++k)
            {
                retval.add(MATS[k][i]);
            }
        }

        for (int i=0; i<wtypes.length; ++i)
        {
            for (int k=1; k<=maxLag; ++k)
            {
                retval.add(GATS[k][i]);
            }
        }

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, getDescriptorNames());
    }

    private double[] getCenteredValues(double[] values)
    {
        double sum = 0.0;
        for (int i=0; i<values.length; ++i)
        {
            sum += values[i];
        }
        double mean = sum / values.length;
        double[] centered = new double[values.length];
        for (int i=0; i<values.length; ++i)
        {
            centered[i] = values[i] - mean;
        }
        return centered;
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
