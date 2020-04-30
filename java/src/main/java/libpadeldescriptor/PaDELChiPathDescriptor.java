/*
*  $RCSfile$
*  $Author$
*  $Date$
*  $Revision$
*
*  Copyright (C) 2004-2007  Rajarshi Guha <rajarshi@users.sourceforge.net>
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
import java.util.Iterator;
import java.util.List;

import org.openscience.cdk1.DefaultChemObjectBuilder;
import org.openscience.cdk1.annotations.TestClass;
import org.openscience.cdk1.annotations.TestMethod;
import org.openscience.cdk1.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.exception.InvalidSmilesException;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IAtomType;
import org.openscience.cdk1.interfaces.IBond;
import org.openscience.cdk1.isomorphism.matchers.QueryAtomContainer;
import org.openscience.cdk1.isomorphism.matchers.QueryAtomContainerCreator;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.smiles.SmilesParser;
import org.openscience.cdk1.tools.CDKHydrogenAdder;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk1.tools.manipulator.AtomTypeManipulator;

/**
 * Evaluates chi path descriptors.
 * <p/>
 * <p/>
 * It utilizes the graph isomorphism code of the CDK to find fragments matching
 * SMILES strings representing the fragments corresponding to each type of chain.
 * <p/>
 * The order of the values returned is
 * <ol>
 * <li>SP-0, SP-1, ..., SP-7 - Simple path, orders 0 to 7
 * <li>VP-0, VP-1, ..., VP-7 - Valence path, orders 0 to 7
 * </ol>
 * <p/>
 * <b>Note</b>: These descriptors are calculated using graph isomorphism to identify
 * the various fragments. As a result calculations may be slow. In addition, recent
 * versions of Molconn-Z use simplified fragment definitions (i.e., rings without
 * branches etc.) whereas these descriptors use the older more complex fragment
 * definitions.
 *
 * @author Rajarshi Guha
 * @cdk.created 2006-11-12
 * @cdk.module qsarmolecular
 * @cdk.githash
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:chiPath
 * @cdk.keyword chi path index
 * @cdk.keyword descriptor
 */
@TestClass("org.openscience.cdk1.qsar.descriptors.molecular.ChiPathDescriptorTest")
public class PaDELChiPathDescriptor implements IMolecularDescriptor {
    private SmilesParser sp;

    public PaDELChiPathDescriptor() {
        sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
    }

    @TestMethod("testGetSpecification")
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "http://www.blueobelisk.org/ontologies/chemoinformatics-algorithms/#chiPath",
                this.getClass().getName(),
                "$Id: c08e389f50666cdeec7578b2c60e803823d78270 $",
                "The Chemistry Development Kit");
    }

    @TestMethod("testGetParameterNames")
    public String[] getParameterNames() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @TestMethod("testGetParameterType_String")
    public Object getParameterType(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @TestMethod("testSetParameters_arrayObject")
    public void setParameters(Object[] params) throws CDKException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @TestMethod("testGetParameters")
    public Object[] getParameters() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @TestMethod(value="testNamesConsistency")
    public String[] getDescriptorNames() {
        String[] names = new String[32];
        for (int i = 0; i < 8; i++) {
            names[i] = "SP-" + i;
            names[i + 8] = "ASP-" + i;
            names[i + 16] = "VP-" + i;
            names[i + 24] = "AVP-" + i;
        }
        return names;
    }


    @TestMethod("testCalculate_IAtomContainer")
    public DescriptorValue calculate(IAtomContainer container) {

        IAtomContainer localAtomContainer = AtomContainerManipulator.removeHydrogens(container);
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(container.getBuilder());
        Iterator<IAtom> atoms = localAtomContainer.atoms().iterator();
        while (atoms.hasNext()) {
            IAtom atom = atoms.next();
            IAtomType type;
            try {
                type = matcher.findMatchingAtomType(localAtomContainer, atom);
                AtomTypeManipulator.configure(atom, type);
            } catch (Exception e) {
                return getDummyDescriptorValue(new CDKException("Error in atom typing: " + e.getMessage()));
            }
        }
        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(container.getBuilder());
        try {
            hAdder.addImplicitHydrogens(localAtomContainer);
        } catch (CDKException e) {
            return getDummyDescriptorValue(new CDKException("Error in hydrogen addition: " + e.getMessage()));
        }

        try {
            List<List<Integer>> subgraph0 = order0(localAtomContainer);
            List<List<Integer>> subgraph1 = order1(localAtomContainer);
            List<List<Integer>> subgraph2 = order2(localAtomContainer);
            List<List<Integer>> subgraph3 = order3(localAtomContainer);
            List<List<Integer>> subgraph4 = order4(localAtomContainer);
            List<List<Integer>> subgraph5 = order5(localAtomContainer);
            List<List<Integer>> subgraph6 = order6(localAtomContainer);
            List<List<Integer>> subgraph7 = order7(localAtomContainer);

            double order0s = PaDELChiIndexUtils.evalSimpleIndex(localAtomContainer, subgraph0);
            double order1s = PaDELChiIndexUtils.evalSimpleIndex(localAtomContainer, subgraph1);
            double order2s = PaDELChiIndexUtils.evalSimpleIndex(localAtomContainer, subgraph2);
            double order3s = PaDELChiIndexUtils.evalSimpleIndex(localAtomContainer, subgraph3);
            double order4s = PaDELChiIndexUtils.evalSimpleIndex(localAtomContainer, subgraph4);
            double order5s = PaDELChiIndexUtils.evalSimpleIndex(localAtomContainer, subgraph5);
            double order6s = PaDELChiIndexUtils.evalSimpleIndex(localAtomContainer, subgraph6);
            double order7s = PaDELChiIndexUtils.evalSimpleIndex(localAtomContainer, subgraph7);

            double order0v = PaDELChiIndexUtils.evalValenceIndex(localAtomContainer, subgraph0);
            double order1v = PaDELChiIndexUtils.evalValenceIndex(localAtomContainer, subgraph1);
            double order2v = PaDELChiIndexUtils.evalValenceIndex(localAtomContainer, subgraph2);
            double order3v = PaDELChiIndexUtils.evalValenceIndex(localAtomContainer, subgraph3);
            double order4v = PaDELChiIndexUtils.evalValenceIndex(localAtomContainer, subgraph4);
            double order5v = PaDELChiIndexUtils.evalValenceIndex(localAtomContainer, subgraph5);
            double order6v = PaDELChiIndexUtils.evalValenceIndex(localAtomContainer, subgraph6);
            double order7v = PaDELChiIndexUtils.evalValenceIndex(localAtomContainer, subgraph7);

            DoubleArrayResult retval = new DoubleArrayResult();
            retval.add(order0s);
            retval.add(order1s);
            retval.add(order2s);
            retval.add(order3s);
            retval.add(order4s);
            retval.add(order5s);
            retval.add(order6s);
            retval.add(order7s);

            retval.add(subgraph0.isEmpty() ? 0 : order0s / subgraph0.size());
            retval.add(subgraph1.isEmpty() ? 0 : order1s / subgraph1.size());
            retval.add(subgraph2.isEmpty() ? 0 : order2s / subgraph2.size());
            retval.add(subgraph3.isEmpty() ? 0 : order3s / subgraph3.size());
            retval.add(subgraph4.isEmpty() ? 0 : order4s / subgraph4.size());
            retval.add(subgraph5.isEmpty() ? 0 : order5s / subgraph5.size());
            retval.add(subgraph6.isEmpty() ? 0 : order6s / subgraph6.size());
            retval.add(subgraph7.isEmpty() ? 0 : order7s / subgraph7.size());

            retval.add(order0v);
            retval.add(order1v);
            retval.add(order2v);
            retval.add(order3v);
            retval.add(order4v);
            retval.add(order5v);
            retval.add(order6v);
            retval.add(order7v);

            retval.add(subgraph0.isEmpty() ? 0 : order0v / subgraph0.size());
            retval.add(subgraph1.isEmpty() ? 0 : order1v / subgraph1.size());
            retval.add(subgraph2.isEmpty() ? 0 : order2v / subgraph2.size());
            retval.add(subgraph3.isEmpty() ? 0 : order3v / subgraph3.size());
            retval.add(subgraph4.isEmpty() ? 0 : order4v / subgraph4.size());
            retval.add(subgraph5.isEmpty() ? 0 : order5v / subgraph5.size());
            retval.add(subgraph6.isEmpty() ? 0 : order6v / subgraph6.size());
            retval.add(subgraph7.isEmpty() ? 0 : order7v / subgraph7.size());

            return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(),
                    retval, getDescriptorNames());
        } catch (CDKException e) {
            return getDummyDescriptorValue(new CDKException(e.getMessage()));
        }

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
    @TestMethod("testGetDescriptorResultType")
    public IDescriptorResult getDescriptorResultType() {
        return new DoubleArrayResultType(32);
    }

    private List<List<Integer>> order0(IAtomContainer atomContainer) {
        List<List<Integer>> fragments = new ArrayList<List<Integer>>();
        for (IAtom atom : atomContainer.atoms()) {
            List<Integer> tmp = new ArrayList<Integer>();
            tmp.add(atomContainer.getAtomNumber(atom));
            fragments.add(tmp);
        }
        return fragments;
    }

    private List<List<Integer>> order1(IAtomContainer atomContainer) throws CDKException {
        List<List<Integer>> fragments = new ArrayList<List<Integer>>();
        for (IBond bond : atomContainer.bonds()) {
            if (bond.getAtomCount() != 2) throw new CDKException("We only consider 2 center bonds");
            List<Integer> tmp = new ArrayList<Integer>();
            tmp.add(atomContainer.getAtomNumber(bond.getAtom(0)));
            tmp.add(atomContainer.getAtomNumber(bond.getAtom(1)));
            fragments.add(tmp);
        }
        return fragments;
    }


    private List<List<Integer>> order2(IAtomContainer atomContainer) {
        QueryAtomContainer[] queries = new QueryAtomContainer[1];
        try {
            queries[0] = QueryAtomContainerCreator.createAnyAtomAnyBondContainer(sp.parseSmiles("CCC"), false);
        } catch (InvalidSmilesException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return PaDELChiIndexUtils.getFragments(atomContainer, queries);
    }

    private List<List<Integer>> order3(IAtomContainer atomContainer) {
        QueryAtomContainer[] queries = new QueryAtomContainer[1];
        try {
            queries[0] = QueryAtomContainerCreator.createAnyAtomAnyBondContainer(sp.parseSmiles("CCCC"), false);
        } catch (InvalidSmilesException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return PaDELChiIndexUtils.getFragments(atomContainer, queries);
    }

    private List<List<Integer>> order4(IAtomContainer atomContainer) {
        QueryAtomContainer[] queries = new QueryAtomContainer[1];
        try {
            queries[0] = QueryAtomContainerCreator.createAnyAtomAnyBondContainer(sp.parseSmiles("CCCCC"), false);
        } catch (InvalidSmilesException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return PaDELChiIndexUtils.getFragments(atomContainer, queries);
    }

    private List<List<Integer>> order5(IAtomContainer atomContainer) {
        QueryAtomContainer[] queries = new QueryAtomContainer[1];
        try {
            queries[0] = QueryAtomContainerCreator.createAnyAtomAnyBondContainer(sp.parseSmiles("CCCCCC"), false);
        } catch (InvalidSmilesException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return PaDELChiIndexUtils.getFragments(atomContainer, queries);
    }

    private List<List<Integer>> order6(IAtomContainer atomContainer) {
        QueryAtomContainer[] queries = new QueryAtomContainer[1];
        try {
            queries[0] = QueryAtomContainerCreator.createAnyAtomAnyBondContainer(sp.parseSmiles("CCCCCCC"), false);
        } catch (InvalidSmilesException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return PaDELChiIndexUtils.getFragments(atomContainer, queries);
    }

    private List<List<Integer>> order7(IAtomContainer atomContainer) {
        QueryAtomContainer[] queries = new QueryAtomContainer[1];
        try {
            queries[0] = QueryAtomContainerCreator.createAnyAtomAnyBondContainer(sp.parseSmiles("CCCCCCCC"), false);
        } catch (InvalidSmilesException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return PaDELChiIndexUtils.getFragments(atomContainer, queries);
    }

}
