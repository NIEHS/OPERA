/*
 *  $RCSfile$
 *  $Author: yapchunwei $
 *  $Date: 2008-06-10 14:50:01 +0800 (Tue, 10 Jun 2008) $
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

import java.util.LinkedHashMap;

import java.util.Map;
import org.openscience.cdk1.atomtype.EStateAtomTypeMatcher;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.graph.matrix.TopologicalMatrix;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IAtomType;
import org.openscience.cdk1.interfaces.IRingSet;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.DoubleResult;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.ringsearch.AllRingsFinder;
import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;


/**
 * Evaluates Atom Type Electrotopological descriptors.
 * <p/>
 * The code currently computes all of the sum of Atom Type Electrotopological 
 * descriptors that are calculated by the MolConn-Z software. 
 * <p/>
 *
 * @author Yap Chun Wei
 * @cdk.created 2008-06-10
 * @cdk.module qsarmolecular
 * @cdk.svnrev $Revision: 1 $
 * @cdk.set qsar-descriptors
 * @cdk.dictref qsar-descriptors:eStateAtomType
 * @cdk.keyword molecular type electrotopological descriptor
 * @cdk.keyword descriptor
 */
public class EStateAtomTypeDescriptor implements IMolecularDescriptor {
    
    private static final String sumI = "sumI";
    private static final String meanI = "meanI";
    private static final String hmax = "hmax";
    private static final String gmax = "gmax";
    private static final String hmin = "hmin";
    private static final String gmin = "gmin";
    private static final String LipoaffinityIndex = "LipoaffinityIndex";
    private static final String MAXDN = "MAXDN";
    private static final String MAXDP = "MAXDP";
    private static final String DELS = "DELS";
    private static final String MAXDN2 = "MAXDN2";
    private static final String MAXDP2 = "MAXDP2";
    private static final String DELS2 = "DELS2";

    private static final String SHBd = "SHBd";
    private static final String SwHBd = "SwHBd";
    private static final String SHBa = "SHBa";
    private static final String SwHBa = "SwHBa";
    private static final String SHBint2 = "SHBint2";
    private static final String SHBint3 = "SHBint3";
    private static final String SHBint4 = "SHBint4";
    private static final String SHBint5 = "SHBint5";
    private static final String SHBint6 = "SHBint6";
    private static final String SHBint7 = "SHBint7";
    private static final String SHBint8 = "SHBint8";
    private static final String SHBint9 = "SHBint9";
    private static final String SHBint10 = "SHBint10";
    private static final String SHsOH = "SHsOH";
    private static final String SHdNH = "SHdNH";
    private static final String SHsSH = "SHsSH";
    private static final String SHsNH2 = "SHsNH2";
    private static final String SHssNH = "SHssNH";
    private static final String SHaaNH = "SHaaNH";
    private static final String SHsNH3p = "SHsNH3p";
    private static final String SHssNH2p = "SHssNH2p";
    private static final String SHsssNHp = "SHsssNHp";
    private static final String SHtCH = "SHtCH";
    private static final String SHdCH2 = "SHdCH2";
    private static final String SHdsCH = "SHdsCH";
    private static final String SHaaCH = "SHaaCH";
    private static final String SHCHnX = "SHCHnX";
    private static final String SHCsats = "SHCsats";
    private static final String SHCsatu = "SHCsatu";
    private static final String SHAvin = "SHAvin";
    private static final String SHother = "SHother";
    private static final String SHmisc = "SHmisc";
    private static final String SsLi = "SsLi";
    private static final String SssBe = "SssBe";
    private static final String SssssBem = "SssssBem";
    private static final String SsBH2 = "SsBH2";
    private static final String SssBH = "SssBH";
    private static final String SsssB = "SsssB";
    private static final String SssssBm = "SssssBm";
    private static final String SsCH3 = "SsCH3";
    private static final String SdCH2 = "SdCH2";
    private static final String SssCH2 = "SssCH2";
    private static final String StCH = "StCH";
    private static final String SdsCH = "SdsCH";
    private static final String SaaCH = "SaaCH";
    private static final String SsssCH = "SsssCH";
    private static final String SddC = "SddC";
    private static final String StsC = "StsC";
    private static final String SdssC = "SdssC";
    private static final String SsaaC = "SsaaC";
    private static final String SaaaC = "SaaaC";
    private static final String SssssC = "SssssC";
    private static final String SsNpH3 = "SsNpH3";
    private static final String SsNH2 = "SsNH2";
    private static final String SssNpH2 = "SssNpH2";
    private static final String SdNH = "SdNH";
    private static final String SssNH = "SssNH";
    private static final String SaaNH = "SaaNH";
    private static final String StN = "StN";
    private static final String SsssNpH = "SsssNpH";
    private static final String SdsN = "SdsN";
    private static final String SaaN = "SaaN";
    private static final String SsssN = "SsssN";
    private static final String SddsN = "SddsN";
    private static final String SsaaN = "SsaaN";
    private static final String SssssNp = "SssssNp";
    private static final String SsOH = "SsOH";
    private static final String SdO = "SdO";
    private static final String SssO = "SssO";
    private static final String SaaO = "SaaO";
    private static final String SaOm = "SaOm";
    private static final String SsOm = "SsOm";
    private static final String SsF = "SsF";
    private static final String SsSiH3 = "SsSiH3";
    private static final String SssSiH2 = "SssSiH2";
    private static final String SsssSiH = "SsssSiH";
    private static final String SssssSi = "SssssSi";
    private static final String SsPH2 = "SsPH2";
    private static final String SssPH = "SssPH";
    private static final String SsssP = "SsssP";
    private static final String SdsssP = "SdsssP";
    private static final String SddsP = "SddsP";
    private static final String SsssssP = "SsssssP";
    private static final String SsSH = "SsSH";
    private static final String SdS = "SdS";
    private static final String SssS = "SssS";
    private static final String SaaS = "SaaS";
    private static final String SdssS = "SdssS";
    private static final String SddssS = "SddssS";
    private static final String SssssssS = "SssssssS";
    private static final String SSm = "SSm";
    private static final String SsCl = "SsCl";
    private static final String SsGeH3 = "SsGeH3";
    private static final String SssGeH2 = "SssGeH2";
    private static final String SsssGeH = "SsssGeH";
    private static final String SssssGe = "SssssGe";
    private static final String SsAsH2 = "SsAsH2";
    private static final String SssAsH = "SssAsH";
    private static final String SsssAs = "SsssAs";
    private static final String SdsssAs = "SdsssAs";
    private static final String SddsAs = "SddsAs";
    private static final String SsssssAs = "SsssssAs";
    private static final String SsSeH = "SsSeH";
    private static final String SdSe = "SdSe";
    private static final String SssSe = "SssSe";
    private static final String SaaSe = "SaaSe";
    private static final String SdssSe = "SdssSe";
    private static final String SssssssSe = "SssssssSe";
    private static final String SddssSe = "SddssSe";
    private static final String SsBr = "SsBr";
    private static final String SsSnH3 = "SsSnH3";
    private static final String SssSnH2 = "SssSnH2";
    private static final String SsssSnH = "SsssSnH";
    private static final String SssssSn = "SssssSn";
    private static final String SsI = "SsI";
    private static final String SsPbH3 = "SsPbH3";
    private static final String SssPbH2 = "SssPbH2";
    private static final String SsssPbH = "SsssPbH";
    private static final String SssssPb = "SssssPb";
    
    private static final String[] prefixes = { "n", "S", "min", "max" };

    private static final String[] suffixes = {
                                                "HBd",
                                                "wHBd",
                                                "HBa",
                                                "wHBa",
                                                "HBint2",
                                                "HBint3",
                                                "HBint4",
                                                "HBint5",
                                                "HBint6",
                                                "HBint7",
                                                "HBint8",
                                                "HBint9",
                                                "HBint10",
                                                "HsOH",
                                                "HdNH",
                                                "HsSH",
                                                "HsNH2",
                                                "HssNH",
                                                "HaaNH",
                                                "HsNH3p",
                                                "HssNH2p",
                                                "HsssNHp",
                                                "HtCH",
                                                "HdCH2",
                                                "HdsCH",
                                                "HaaCH",
                                                "HCHnX",
                                                "HCsats",
                                                "HCsatu",
                                                "HAvin",
                                                "Hother",
                                                "Hmisc",
                                                "sLi",
                                                "ssBe",
                                                "ssssBem",
                                                "sBH2",
                                                "ssBH",
                                                "sssB",
                                                "ssssBm",
                                                "sCH3",
                                                "dCH2",
                                                "ssCH2",
                                                "tCH",
                                                "dsCH",
                                                "aaCH",
                                                "sssCH",
                                                "ddC",
                                                "tsC",
                                                "dssC",
                                                "aasC",    // saaC in CDK
                                                "aaaC",
                                                "ssssC",
                                                "sNH3p",   // sNpH3 in CDK
                                                "sNH2",
                                                "ssNH2p",  // ssNpH2 in CDK
                                                "dNH",
                                                "ssNH",
                                                "aaNH",
                                                "tN",
                                                "sssNHp",  // sssNpH in CDK
                                                "dsN",
                                                "aaN",
                                                "sssN",
                                                "ddsN",
                                                "aasN",    // saaN in CDK
                                                "ssssNp",
                                                "sOH",
                                                "dO",
                                                "ssO",
                                                "aaO",
                                                "aOm",
                                                "sOm",
                                                "sF",
                                                "sSiH3",
                                                "ssSiH2",
                                                "sssSiH",
                                                "ssssSi",
                                                "sPH2",
                                                "ssPH",
                                                "sssP",
                                                "dsssP",
                                                "ddsP",
                                                "sssssP",
                                                "sSH",
                                                "dS",
                                                "ssS",
                                                "aaS",
                                                "dssS",
                                                "ddssS",
                                                "ssssssS",
                                                "Sm",
                                                "sCl",
                                                "sGeH3",
                                                "ssGeH2",
                                                "sssGeH",
                                                "ssssGe",
                                                "sAsH2",
                                                "ssAsH",
                                                "sssAs",
                                                "dsssAs",
                                                "ddsAs",
                                                "sssssAs",
                                                "sSeH",
                                                "dSe",
                                                "ssSe",
                                                "aaSe",
                                                "dssSe",
                                                "ssssssSe",
                                                "ddssSe",
                                                "sBr",
                                                "sSnH3",
                                                "ssSnH2",
                                                "sssSnH",
                                                "ssssSn",
                                                "sI",
                                                "sPbH3",
                                                "ssPbH2",
                                                "sssPbH",
                                                "ssssPb"
                                            };
    
    private static final String[] others = {
                                                sumI,
                                                meanI,
                                                hmax,
                                                gmax,
                                                hmin,
                                                gmin,
                                                LipoaffinityIndex,
                                                MAXDN,
                                                MAXDP,
                                                DELS,
                                                MAXDN2,
                                                MAXDP2,
                                                DELS2};

    private static final String[] atomTypes = {
                                                SHBd,
                                                SwHBd,
                                                SHBa,
                                                SwHBa,
                                                SHBint2,
                                                SHBint3,
                                                SHBint4,
                                                SHBint5,
                                                SHBint6,
                                                SHBint7,
                                                SHBint8,
                                                SHBint9,
                                                SHBint10,
                                                SHsOH,
                                                SHdNH,
                                                SHsSH,
                                                SHsNH2,
                                                SHssNH,
                                                SHaaNH,
                                                SHsNH3p,
                                                SHssNH2p,
                                                SHsssNHp,
                                                SHtCH,
                                                SHdCH2,
                                                SHdsCH,
                                                SHaaCH,
                                                SHCHnX,
                                                SHCsats,
                                                SHCsatu,
                                                SHAvin,
                                                SHother,
                                                SHmisc,
                                                SsLi,
                                                SssBe,
                                                SssssBem,
                                                SsBH2,
                                                SssBH,
                                                SsssB,
                                                SssssBm,
                                                SsCH3,
                                                SdCH2,
                                                SssCH2,
                                                StCH,
                                                SdsCH,
                                                SaaCH,
                                                SsssCH,
                                                SddC,
                                                StsC,
                                                SdssC,
                                                SsaaC,
                                                SaaaC,
                                                SssssC,
                                                SsNpH3,
                                                SsNH2,
                                                SssNpH2,
                                                SdNH,
                                                SssNH,
                                                SaaNH,
                                                StN,
                                                SsssNpH,
                                                SdsN,
                                                SaaN,
                                                SsssN,
                                                SddsN,
                                                SsaaN,
                                                SssssNp,
                                                SsOH,
                                                SdO,
                                                SssO,
                                                SaaO,
                                                SaOm,
                                                SsOm,
                                                SsF,
                                                SsSiH3,
                                                SssSiH2,
                                                SsssSiH,
                                                SssssSi,
                                                SsPH2,
                                                SssPH,
                                                SsssP,
                                                SdsssP,
                                                SddsP,
                                                SsssssP,
                                                SsSH,
                                                SdS,
                                                SssS,
                                                SaaS,
                                                SdssS,
                                                SddssS,
                                                SssssssS,
                                                SSm,
                                                SsCl,
                                                SsGeH3,
                                                SssGeH2,
                                                SsssGeH,
                                                SssssGe,
                                                SsAsH2,
                                                SssAsH,
                                                SsssAs,
                                                SdsssAs,
                                                SddsAs,
                                                SsssssAs,
                                                SsSeH,
                                                SdSe,
                                                SssSe,
                                                SaaSe,
                                                SdssSe,
                                                SssssssSe,
                                                SddssSe,
                                                SsBr,
                                                SsSnH3,
                                                SssSnH2,
                                                SsssSnH,
                                                SssssSn,
                                                SsI,
                                                SsPbH3,
                                                SssPbH2,
                                                SsssPbH,
                                                SssssPb
                                                    };

    private Map<String,Integer> eStatesCount = new LinkedHashMap<String,Integer>();
    private Map<String,Double> eStatesSum = new LinkedHashMap<String,Double>();
    private Map<String,Double> eStatesMin = new LinkedHashMap<String,Double>();
    private Map<String,Double> eStatesMax = new LinkedHashMap<String,Double>();
    private Map<String,Double> eStatesOthers = new LinkedHashMap<String,Double>();

    public String[] names = new String[prefixes.length*suffixes.length + others.length];

    public EStateAtomTypeDescriptor() {
        int index = 0;
        for (String prefix : prefixes)
        {
            for (String suffix : suffixes)
            {
                names[index] = prefix + suffix;
                ++index;
            }
        }
        for (String other : others)
        {
            names[index] = other;
            ++index;
        }
    }

    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
                "eStateAtomType",
                this.getClass().getName(),
                "$Id: EStateAtomTypeDescriptor.java 1 2008-06-10 06:50:01Z yapchunwei $",
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
     * @throws org.openscience.cdk1.exception.CDKException
     */
    @Override
    public DescriptorValue calculate(IAtomContainer container) {

        IRingSet rs;
        try 
        {
            AllRingsFinder arf = new AllRingsFinder();
            rs = arf.findAllRings(container);
        } 
        catch (Exception e) 
        {
            return getDummyDescriptorValue(new CDKException("Could not find all rings: " + e.getMessage()));
        }

        eStatesCount = new LinkedHashMap<String,Integer>();
        eStatesSum = new LinkedHashMap<String,Double>();
        eStatesMin = new LinkedHashMap<String,Double>();
        eStatesMax = new LinkedHashMap<String,Double>();
        for (String atomType : atomTypes)
        {
            eStatesCount.put(atomType, 0);
            eStatesSum.put(atomType, 0.0);
            eStatesMin.put(atomType, Double.MAX_VALUE);
            eStatesMax.put(atomType, Double.MIN_VALUE);
        }

        eStatesOthers.put(sumI, 0.0);
        eStatesOthers.put(meanI, 0.0);
        eStatesOthers.put(hmax, Double.MIN_VALUE);
        eStatesOthers.put(gmax, Double.MIN_VALUE);
        eStatesOthers.put(hmin, Double.MAX_VALUE);
        eStatesOthers.put(gmin, Double.MAX_VALUE);
        eStatesOthers.put(LipoaffinityIndex, 0.0);
        eStatesOthers.put(MAXDN, 0.0);
        eStatesOthers.put(MAXDP, 0.0);
        eStatesOthers.put(DELS, 0.0);
        eStatesOthers.put(MAXDN2, 0.0);
        eStatesOthers.put(MAXDP2, 0.0);
        eStatesOthers.put(DELS2, 0.0);

        IntrinsicStateDescriptor isd = new IntrinsicStateDescriptor();
        ElectrotopologicalStateDescriptor esd = new ElectrotopologicalStateDescriptor();
        HydrogenEStateDescriptor hesd = new HydrogenEStateDescriptor();
        EStateAtomTypeMatcher ematcher = new EStateAtomTypeMatcher();
        ematcher.setRingSet(rs);
        int maxAtoms = container.getAtomCount();

        double[] IStates = new double[maxAtoms];
        double[] IStates2 = new double[maxAtoms];
        // Cache intrinsic states of each atom.
        for (int i=0; i<maxAtoms; ++i)
        {
            IAtom atom = container.getAtom(i);
            if (atom.getSymbol().equals("H")) 
            {
                IStates[i] = Double.NaN;
                IStates2[i] = Double.NaN;
            }
            else
            {
                DoubleArrayResult results = (DoubleArrayResult)isd.calculate(atom, container).getValue();
                IStates[i] = results.get(0);
                IStates2[i] = results.get(1);
            }
        }

        int[][] pathLengths = TopologicalMatrix.getMatrix(container);
        int maxNonHAtoms = 0;
        for (int i=0; i<maxAtoms; ++i)
        {
            IAtom atom = container.getAtom(i);
            if (atom.getSymbol().equals("H")) 
            {
                continue;
            }
            ++maxNonHAtoms;
            IAtomType type = ematcher.findMatchingAtomType(container, atom);

            boolean hasHydrogens = AtomContainerManipulator.countHydrogens(container, atom)>0 ? true : false;
                
            // Gramatica, P., Corradi, M., and Consonni, V. (2000). Modelling and prediction of soil sorption coefficients of non-ionic organic pesticides by molecular descriptors. Chemosphere 41, 763-777.
            double deltaIi = 0.0;
            double deltaIi2 = 0.0;
            for (int j=0; j<maxAtoms; ++j)
            {
                if (i==j || container.getAtom(j).getSymbol().equals("H")) 
                {
                    continue;
                }
                deltaIi += (IStates[i] - IStates[j]) / ((pathLengths[i][j]+1) * (pathLengths[i][j]+1));
                deltaIi2 += (IStates2[i] - IStates2[j]) / ((pathLengths[i][j]+1) * (pathLengths[i][j]+1));
            }
            double absDeltaIi = Math.abs(deltaIi);
            if (deltaIi < 0)
            {                
                if (absDeltaIi > eStatesOthers.get(MAXDN))
                {
                    eStatesOthers.put(MAXDN, absDeltaIi);
                }                
            }
            else if (deltaIi > 0)
            {
                if (deltaIi > eStatesOthers.get(MAXDP))
                {
                    eStatesOthers.put(MAXDP, deltaIi);
                } 
            }
            eStatesOthers.put(DELS, eStatesOthers.get(DELS) + absDeltaIi);

            double absDeltaIi2 = Math.abs(deltaIi2);
            if (deltaIi2 < 0)
            {                
                if (absDeltaIi2 > eStatesOthers.get(MAXDN2))
                {
                    eStatesOthers.put(MAXDN2, absDeltaIi2);
                }                
            }
            else if (deltaIi2 > 0)
            {
                if (deltaIi2 > eStatesOthers.get(MAXDP2))
                {
                    eStatesOthers.put(MAXDP2, deltaIi2);
                } 
            }
            eStatesOthers.put(DELS2, eStatesOthers.get(DELS2) + absDeltaIi2);

            eStatesOthers.put(sumI, eStatesOthers.get(sumI) + Double.valueOf(IStates[i]));

            double HEState = ((DoubleResult)hesd.calculate(atom, container, pathLengths[i]).getValue()).doubleValue();
            if (type.getAtomTypeName().equals(SsOH))
            {
                StoreValue(SHsOH, HEState);
            }
            else if (type.getAtomTypeName().equals(SdNH))
            {
                StoreValue(SHdNH, HEState);
            }
            else if (type.getAtomTypeName().equals(SsSH))
            {
                StoreValue(SHsSH, HEState);
            }
            else if (type.getAtomTypeName().equals(SsNH2))
            {
                StoreValue(SHsNH2, HEState);
            }
            else if (type.getAtomTypeName().equals(SssNH))
            {
                StoreValue(SHssNH, HEState);
            }
            else if (type.getAtomTypeName().equals(SaaNH))
            {
                StoreValue(SHaaNH, HEState);
            }
            else if (type.getAtomTypeName().equals(SsNpH3))
            {
                StoreValue(SHsNH3p, HEState);
            }
            else if (type.getAtomTypeName().equals(SssNpH2))
            {
                StoreValue(SHssNH2p, HEState);
            }
            else if (type.getAtomTypeName().equals(SsssNpH))
            {
                StoreValue(SHsssNHp, HEState);
            }
            else if (type.getAtomTypeName().equals(StCH))
            {
                StoreValue(SHtCH, HEState);
            }
            else if (type.getAtomTypeName().equals(SdCH2))
            {
                StoreValue(SHdCH2, HEState);
            }
            else if (type.getAtomTypeName().equals(SdsCH))
            {
                StoreValue(SHdsCH, HEState);
            }
            else if (type.getAtomTypeName().equals(SaaCH))
            {
                StoreValue(SHaaCH, HEState);
            }

            if (atom.getSymbol().equals("C") && hasHydrogens)
            {
                for (IAtom cAtom : container.getConnectedAtomsList(atom))
                {
                    if (cAtom.getSymbol().equals("F") || cAtom.getSymbol().equals("Cl"))
                    {
                        StoreValue(SHCHnX, HEState);
                        StoreValue(SwHBd, HEState);
                        break;
                    }
                    else if (cAtom.getSymbol().equals("Br") || cAtom.getSymbol().equals("I"))
                    {
                        // Not given in MolConn-Z manual. Added to make SwHBd different from SHCHnX.
                        StoreValue(SHCHnX, HEState);
                        break;
                    }
                }
            }

            if (type.getAtomTypeName().equals(SsCH3) ||
                type.getAtomTypeName().equals(SssCH2) ||
                type.getAtomTypeName().equals(SsssCH))
            {
                boolean sats = false;
                boolean satu = false;
                for (IAtom cAtom : container.getConnectedAtomsList(atom))
                {
                    if (atom==cAtom) 
                    {
                        continue;
                    }
                    if (sats && satu) 
                    {
                        break;
                    }
                    IAtomType cType = ematcher.findMatchingAtomType(container, cAtom);

                    if (!sats &&
                        (cType.getAtomTypeName().equals(SsCH3) ||
                         cType.getAtomTypeName().equals(SssCH2) ||
                         cType.getAtomTypeName().equals(SsssCH) ||
                         cType.getAtomTypeName().equals(SssssC)))
                    {
                        StoreValue(SHCsats, HEState);
                        sats = true;
                    }

                    if (!satu &&
                        (cType.getAtomTypeName().equals(SdCH2) ||
                         cType.getAtomTypeName().equals(StCH) ||
                         cType.getAtomTypeName().equals(SdsCH) ||
                         cType.getAtomTypeName().equals(SddC) ||
                         cType.getAtomTypeName().equals(StsC) ||
                         cType.getAtomTypeName().equals(SdssC)))
                    {
                        StoreValue(SHCsatu, HEState);
                        satu = true;
                    }
                }   
            }
            else if (type.getAtomTypeName().equals(SdsCH))
            {
                for (IAtom cAtom : container.getConnectedAtomsList(atom))
                {
                    if (atom==cAtom) 
                    {
                        continue;
                    }
                    IAtomType cType = ematcher.findMatchingAtomType(container, cAtom);

                    if (cType.getAtomTypeName().equals(SaaCH) ||
                        cType.getAtomTypeName().equals(SsaaC) ||
                        cType.getAtomTypeName().equals(SaaaC))
                    {
                        StoreValue(SHAvin, HEState);
                        break;
                    }                        
                } 
            }            

            if (type.getAtomTypeName().equals(SaaCH) ||
                type.getAtomTypeName().equals(SdCH2) ||
                type.getAtomTypeName().equals(SdsCH))
            {
                StoreValue(SHother, HEState);
            }
            else if ((atom.getSymbol().equals("B") ||
                      atom.getSymbol().equals("Si") ||
                      atom.getSymbol().equals("P") ||
                      atom.getSymbol().equals("Ge") ||
                      atom.getSymbol().equals("As") ||
                      atom.getSymbol().equals("Se") ||
                      atom.getSymbol().equals("Sn") ||
                      atom.getSymbol().equals("Pb")) 
                     && hasHydrogens)
            {
                StoreValue(SHmisc, HEState);
            }                  

            double eState = ((DoubleResult)esd.calculate(atom, container, pathLengths[i]).getValue()).doubleValue();
            if (eStatesSum.containsKey(type.getAtomTypeName()))
            {
                StoreValue(type.getAtomTypeName(), eState);
            }             

            if (type.getAtomTypeName().equals(SsOH) ||
                type.getAtomTypeName().equals(SdNH) ||
                type.getAtomTypeName().equals(SsNH2) ||
                type.getAtomTypeName().equals(SssNH) ||
                type.getAtomTypeName().equals(SaaNH) ||
                type.getAtomTypeName().equals(SsSH) ||
                type.getAtomTypeName().equals(StCH))
            {
                StoreValue(SHBd, HEState);
            }

            if (type.getAtomTypeName().equals(SsOH) ||
                type.getAtomTypeName().equals(SdNH) ||
                type.getAtomTypeName().equals(SsNH2) ||
                type.getAtomTypeName().equals(SssNH) ||
                type.getAtomTypeName().equals(SaaNH) ||
                type.getAtomTypeName().equals(SsssN) ||
                type.getAtomTypeName().equals(SsaaN) ||
                type.getAtomTypeName().equals(SssO) ||
                type.getAtomTypeName().equals(SaaO) ||    
                type.getAtomTypeName().equals(SdO) ||
                type.getAtomTypeName().equals(SssS) ||
                type.getAtomTypeName().equals(SaaS) ||    
                type.getAtomTypeName().equals(SsF) ||
                type.getAtomTypeName().equals(SsCl))
            {
                StoreValue(SHBa, eState);

                for (int j=0; j<maxAtoms; ++j)
                {
                    IAtom cAtom = container.getAtom(j);
                    if (atom==cAtom) 
                    {
                        continue;
                    }
                    IAtomType cType = ematcher.findMatchingAtomType(container, cAtom);

                    if (cType.getAtomTypeName().equals(SsOH) ||
                        cType.getAtomTypeName().equals(SdNH) ||
                        cType.getAtomTypeName().equals(SsNH2) ||
                        cType.getAtomTypeName().equals(SssNH) ||
                        cType.getAtomTypeName().equals(SaaNH) ||    
                        cType.getAtomTypeName().equals(SsSH) ||
                        cType.getAtomTypeName().equals(StCH))
                    {
                        int shortestPath = pathLengths[i][j]; 
                        if (shortestPath>=2 && shortestPath<=10)
                        {
                            String iHBond = "SHBint" + shortestPath;
                            double cHEState = ((DoubleResult)hesd.calculate(cAtom, container, pathLengths[j]).getValue()).doubleValue();
                            StoreValue(iHBond, Double.valueOf(eState)*Double.valueOf(cHEState));
                        }    
                    }
                }
            }

            if (type.getAtomTypeName().equals(SdCH2) ||
                type.getAtomTypeName().equals(StCH) ||
                type.getAtomTypeName().equals(SdsCH) ||
                type.getAtomTypeName().equals(SaaCH) ||
                type.getAtomTypeName().equals(SddC) ||
                type.getAtomTypeName().equals(StsC) ||
                type.getAtomTypeName().equals(SdssC) ||
                type.getAtomTypeName().equals(SsaaC) ||
                type.getAtomTypeName().equals(SaaaC))
            {
                StoreValue(SwHBa, eState);
            }

            if (HEState > eStatesOthers.get(hmax))
            {
                eStatesOthers.put(hmax, HEState);
            }
            if (HEState < eStatesOthers.get(hmin))
            {
                eStatesOthers.put(hmin, HEState);
            }

            if (eState > eStatesOthers.get(gmax))
            {
                eStatesOthers.put(gmax, eState);
            }
            if (eState < eStatesOthers.get(gmin))
            {
                eStatesOthers.put(gmin, eState);
            }            
        }  
        eStatesOthers.put(meanI, eStatesOthers.get(sumI) / maxNonHAtoms);

        // Calculate lipoaffinity index (Liu R et al. J Chem Inf Comput Sci. 2001;41(6):1623-1632)
        double lipoaffinityIndex = 0.432 * eStatesSum.get(SsCH3).doubleValue();
        lipoaffinityIndex += 0.318 * eStatesSum.get(SdCH2).doubleValue();
        lipoaffinityIndex += 0.482 * eStatesSum.get(SssCH2).doubleValue();
        lipoaffinityIndex += 0.123 * eStatesSum.get(StCH).doubleValue();
        lipoaffinityIndex += 0.280 * eStatesSum.get(SdsCH).doubleValue();
        lipoaffinityIndex += 0.299 * eStatesSum.get(SaaCH).doubleValue();
        lipoaffinityIndex += 0.425 * eStatesSum.get(SsssCH).doubleValue();
        lipoaffinityIndex += 0.333 * eStatesSum.get(SddC).doubleValue();
        lipoaffinityIndex += 0.125 * eStatesSum.get(StsC).doubleValue();
        lipoaffinityIndex += -0.141 * eStatesSum.get(SdssC).doubleValue();
        lipoaffinityIndex += 0.200 * eStatesSum.get(SsaaC).doubleValue();
        lipoaffinityIndex += 0.334 * eStatesSum.get(SaaaC).doubleValue();
        lipoaffinityIndex += 0.245 * eStatesSum.get(SssssC).doubleValue();
        lipoaffinityIndex += 0.137 * eStatesSum.get(SsF).doubleValue();
        lipoaffinityIndex += 0.309 * eStatesSum.get(SsSH).doubleValue();
        lipoaffinityIndex += 0.314 * eStatesSum.get(SdS).doubleValue();
        lipoaffinityIndex += 0.561 * eStatesSum.get(SssS).doubleValue();
        lipoaffinityIndex += 0.821 * eStatesSum.get(SaaS).doubleValue();
        lipoaffinityIndex += 0.307 * eStatesSum.get(SsCl).doubleValue();
        lipoaffinityIndex += 0.755 * eStatesSum.get(SsBr).doubleValue();
        lipoaffinityIndex += 1.616 * eStatesSum.get(SsI).doubleValue();
        eStatesOthers.put(LipoaffinityIndex, lipoaffinityIndex);
   
        DoubleArrayResult retval = new DoubleArrayResult();
        for (Integer value : eStatesCount.values())
        {
            retval.add(value);
        }
        for (Double value : eStatesSum.values())
        {
            retval.add(value);
        }
        for (Double value : eStatesMin.values())
        {
            if (value!=Double.MAX_VALUE) retval.add(value);
            else retval.add(0.0);
        }
        for (Double value : eStatesMax.values())
        {
            if (value!=Double.MIN_VALUE) retval.add(value);
            else retval.add(0.0);
        }
        for (Double value : eStatesOthers.values())
        {
            retval.add(value);
        }
        
        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retval, names);

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

    private void StoreValue(String atomType, Double value)
    {
        eStatesCount.put(atomType, eStatesCount.get(atomType) + 1);
        eStatesSum.put(atomType, eStatesSum.get(atomType) + value);

        if (value > eStatesMax.get(atomType))
        {
            eStatesMax.put(atomType, value);
        }
        if (value < eStatesMin.get(atomType))
        {
            eStatesMin.put(atomType, value);
        }
    }
}

