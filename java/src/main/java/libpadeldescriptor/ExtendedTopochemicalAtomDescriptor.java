/*  $Revision$ $Author$ $Date$
 *
 *  Copyright (C) 2004-2007  Matteo Floris <mfe4@users.sf.net>
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

import java.util.List;
import org.openscience.cdk1.CDKConstants;
import org.openscience.cdk1.annotations.TestMethod;
import org.openscience.cdk1.exception.CDKException;
import org.openscience.cdk1.graph.matrix.TopologicalMatrix;
import org.openscience.cdk1.interfaces.IAtom;
import org.openscience.cdk1.interfaces.IAtomContainer;
import org.openscience.cdk1.interfaces.IBond;
import org.openscience.cdk1.interfaces.IBond.Order;
import org.openscience.cdk1.interfaces.IRingSet;
import org.openscience.cdk1.qsar.AtomValenceTool;
import org.openscience.cdk1.qsar.DescriptorSpecification;
import org.openscience.cdk1.qsar.DescriptorValue;
import org.openscience.cdk1.qsar.IMolecularDescriptor;
import org.openscience.cdk1.qsar.result.DoubleArrayResult;
import org.openscience.cdk1.qsar.result.DoubleArrayResultType;
import org.openscience.cdk1.qsar.result.IDescriptorResult;
import org.openscience.cdk1.ringsearch.SSSRFinder;

/**
 * Extended Topochemical Atom (ETA) descriptors.
 *
 *
 * @author      Yap Chun Wei
 */
public class ExtendedTopochemicalAtomDescriptor implements IMolecularDescriptor {

    private static final String[] names = {
                                            "ETA_Alpha",
                                            "ETA_AlphaP",
                                            "ETA_dAlpha_A",
                                            "ETA_dAlpha_B",
                                            "ETA_Epsilon_1",
                                            "ETA_Epsilon_2",
                                            "ETA_Epsilon_3",
                                            "ETA_Epsilon_4",
                                            "ETA_Epsilon_5",
                                            "ETA_dEpsilon_A",
                                            "ETA_dEpsilon_B",
                                            "ETA_dEpsilon_C",
                                            "ETA_dEpsilon_D",
                                            "ETA_Psi_1",
                                            "ETA_dPsi_A",
                                            "ETA_dPsi_B",
                                            "ETA_Shape_P",
                                            "ETA_Shape_Y",
                                            "ETA_Shape_X",
                                            "ETA_Beta",
                                            "ETA_BetaP",
                                            "ETA_Beta_s",
                                            "ETA_BetaP_s",
                                            "ETA_Beta_ns",
                                            "ETA_BetaP_ns",
                                            "ETA_dBeta",
                                            "ETA_dBetaP",
                                            "ETA_Beta_ns_d",
                                            "ETA_BetaP_ns_d",
                                            "ETA_Eta",
                                            "ETA_EtaP",
                                            "ETA_Eta_R",
                                            "ETA_Eta_F",
                                            "ETA_EtaP_F",
                                            "ETA_Eta_L",
                                            "ETA_EtaP_L",
                                            "ETA_Eta_R_L",
                                            "ETA_Eta_F_L",
                                            "ETA_EtaP_F_L",
                                            "ETA_Eta_B",
                                            "ETA_EtaP_B",
                                            "ETA_Eta_B_RC",
                                            "ETA_EtaP_B_RC"
                                          };

    private int decimalPlaces = 5;

    /**
     *  Constructor for the ExtendedTopochemicalAtomDescriptor object
     */
    public ExtendedTopochemicalAtomDescriptor() {}


    /**
     *  Gets the specification attribute of the ExtendedTopochemicalAtomDescriptor object
     *
     *@return    The specification value
     */
    @TestMethod("testGetSpecification")
    @Override
    public DescriptorSpecification getSpecification() {
        return new DescriptorSpecification(
            "ExtendedTopochemicalAtomDescriptor",
            this.getClass().getName(),
            "$Id$",
            "The Chemistry Development Kit");
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
        if (params.length > 1) {
            throw new CDKException("ExtendedTopochemicalAtomDescriptor only expects one parameter");
        }
        if (!(params[0] instanceof Integer)) {
            throw new CDKException("The parameter must be of type Integer");
        }
        decimalPlaces = (Integer) params[0];
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
     *  This method calculate the Extended Topochemical Atom (ETA) descriptors
     *
     *@param  container  AtomContainer
     *@return            Extended Topochemical Atom (ETA) descriptors.
     */
    @TestMethod("testCalculate_IAtomContainer")
    @Override
    public DescriptorValue calculate(IAtomContainer container) {

	IAtomContainer tempContainer = null;
	try
	{
	    tempContainer = (IAtomContainer)container.clone();
	}
	catch (Exception ex)
	{

	}
	PaDELStandardize.StandardizeNitro(tempContainer);

        // Cache pathlengths between atoms.
        int maxAtoms = tempContainer.getAtomCount();

        // Calculate atom level descriptors.
        double[] alpha = new double[maxAtoms];
        double[] alphaR = new double[maxAtoms];
        double[] epsilon = new double[maxAtoms];
        for (int i=0; i<maxAtoms; ++i)
        {
            IAtom atom = tempContainer.getAtom(i);
            int atomicNumber = atom.getAtomicNumber();
            alpha[i] = 0.0;
            alphaR[i] = 0.0;
            epsilon[i] = 0.3;
            if (atomicNumber!=1)
            {
                int Z = atomicNumber;
                int Zv = AtomValenceTool.getValence(atom);
                int period = AtomConstants.period[atomicNumber];

                alpha[i] = Round(((Z-Zv) / (double)Zv) * (1.0 / (period-1)), decimalPlaces);
                alphaR[i] = 0.5;
                epsilon[i] = Round(-alpha[i] + 0.3*AtomValenceTool.getValence(atom), decimalPlaces);
            }
        }

        double[] beta = new double[maxAtoms];
        double[] betaS = new double[maxAtoms];
        double[] betaNS = new double[maxAtoms];
        double[] betaNSd = new double[maxAtoms];
        double[] gamma = new double[maxAtoms];
        double[] gammaRef = new double[maxAtoms];
        for (int i=0; i<maxAtoms; ++i)
        {
            IAtom atom = tempContainer.getAtom(i);
            // VEM contribution for a non-hydrogen vertex
            betaS[i] = 0.0;
            betaNS[i] = 0.0;
            betaNSd[i] = 0.0;
            double betaR = 0.0;
            boolean aromatic = false;
            double nonconjugatedSum = 0.0;
            boolean conjugated = false;
            boolean hasConjugatedSingleBond = false;
            boolean hasDoubleTripleBond = false;
            int conjugatedMultiplier = 1;
            boolean delta = false;
            List<IBond> bonds = tempContainer.getConnectedBondsList(atom);
            for (IBond bond : bonds)
            {
                IAtom atom2 = bond.getConnectedAtom(atom);
                if (atom2.getSymbol().equals("H")) continue;

                betaR += 0.5;

                int j = tempContainer.getAtomNumber(atom2);
                double epsilonDiff = Math.abs(epsilon[i]-epsilon[j]);
                if (epsilonDiff <= 0.3)
                {
                    // contribution of a sigma bond (x) between two atoms of similar electronegativity (epsilon difference <= 0.3)
                    betaS[i] += 0.5;
                }
                else
                {
                    // contribution of a sigma bond (x) between two atoms of different electronegativity (epsilon difference > 0.3)
                    betaS[i] += 0.75;
                }

                if (bond.getFlag(CDKConstants.ISAROMATIC))
                {
                    aromatic = true;
                }
                else if (bond.getOrder()==Order.SINGLE)
                {
                    // Handle conjugated (non–aromatic) pi system where gamma is 1.5
                    if (!conjugated)
                    {
                        if (atom2.getSymbol().equals("C"))
                        {
                            List<IBond> bonds2 = tempContainer.getConnectedBondsList(atom2);
                            for (IBond bond2 : bonds2)
                            {
                                if (bond2.getOrder()==Order.DOUBLE ||
                                    bond2.getFlag(CDKConstants.ISAROMATIC))
                                {
                                    hasConjugatedSingleBond = true;
                                    break;
                                }
                                else if (bond2.getOrder()==Order.TRIPLE)
                                {
                                    conjugatedMultiplier = 2;
                                    hasConjugatedSingleBond = true;
                                    break;
                                }

                            }
                        }
                        else if (AtomValenceTool.getValence(atom2)-atom2.getFormalCharge()-tempContainer.getBondOrderSum(atom2)>=2)
                        {
                            hasConjugatedSingleBond = true;
                        }
                        if (hasDoubleTripleBond && hasConjugatedSingleBond) conjugated = true;
                    }
                }
                else if (bond.getOrder()==Order.DOUBLE || bond.getOrder()==Order.TRIPLE)
                {
                    int multiplier;
                    if (bond.getOrder()==Order.DOUBLE) multiplier = 1;
                    else
                    {
                        multiplier = 2;
                        conjugatedMultiplier = 2;
                    }

                    // Handle conjugated (non–aromatic) pi system where gamma is 1.5
                    hasDoubleTripleBond = true;
                    if (!conjugated)
                    {
                        if (hasConjugatedSingleBond) conjugated = true;
                        else if (atom2.getSymbol().equals("C"))
                        {
                            List<IBond> bonds2 = tempContainer.getConnectedBondsList(atom2);
                            for (IBond bond2 : bonds2)
                            {
                                if (bond2.getOrder()==Order.SINGLE)
                                {
                                    IAtom atom3 = bond2.getConnectedAtom(atom2);
                                    if (atom3.getSymbol().equals("C"))
                                    {
                                        List<IBond> bonds3 = tempContainer.getConnectedBondsList(atom3);
                                        for (IBond bond3 : bonds3)
                                        {
                                            if (bond3.getOrder()==Order.DOUBLE ||
                                                bond3.getFlag(CDKConstants.ISAROMATIC))
                                            {
                                                hasConjugatedSingleBond = true;
                                                break;
                                            }
                                            else if (bond3.getOrder()==Order.TRIPLE)
                                            {
                                                conjugatedMultiplier = 2;
                                                hasConjugatedSingleBond = true;
                                                break;
                                            }
                                        }
                                    }
                                    else if (AtomValenceTool.getValence(atom3)-atom3.getFormalCharge()-tempContainer.getBondOrderSum(atom3)>=2)
                                    {
                                        hasConjugatedSingleBond = true;
                                    }
                                    if (hasConjugatedSingleBond)
                                    {
                                        conjugated = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    double g = 0.0;
                    if (epsilonDiff <= 0.3)
                    {
                        // for pi bond between two atoms of similar electronegativity (epsilon difference <= 0.3)
                        g += 1;
                    }
                    else
                    {
                        // for pi bond between two atoms of different electronegativity (epsilon difference > 0.3)
                        g += 1.5;
                    }

                    nonconjugatedSum += g * multiplier;
                }

                if (AtomValenceTool.getValence(atom)-atom.getFormalCharge()-tempContainer.getBondOrderSum(atom)>=2 &&
                    !atom.getFlag(CDKConstants.ISAROMATIC) &&
                    !atom.getFlag(CDKConstants.ISINRING) &&
                    atom2.getFlag(CDKConstants.ISAROMATIC) &&
                    atom2.getFlag(CDKConstants.ISINRING))
                {
                    delta = true;
                }
            }
            betaNS[i] += (aromatic ? 2.0 : 0.0) + (conjugated ? 1.5*conjugatedMultiplier : nonconjugatedSum) + (delta ? 0.5 : 0.0);
            betaNSd[i] = (delta ? 0.5 : 0.0);

            beta[i] = betaS[i] + betaNS[i];
            gamma[i] = Round(alpha[i] / beta[i], decimalPlaces);
            gammaRef[i] = Round(alphaR[i] / betaR, decimalPlaces);
        }

        // Find Smallest Set of Smallest Rings.
        SSSRFinder finder = new SSSRFinder(tempContainer);
        IRingSet sssr = finder.findEssentialRings();
        int maxRings = sssr.getAtomContainerCount();

        // Start calculation of molecular descriptors
        int N = 0;
        int Nv = 0;
        int Nr = 0;
        int Nss = 0;
        int Nxh = 0;
        double alphaSum = 0.0;
        double alphaRSum = 0.0;
        double epsilonSum = 0.0;
        double epsilonEHSum = 0.0;
        double epsilonRSum = 0.0;
        double epsilonSSSum = 0.0;
        double epsilonXHSum = 0.0;
        double Alpha_P = 0.0;
        double Alpha_Y = 0.0;
        double Alpha_X = 0.0;
        double betaSSum = 0.0;
        double betaNSSum = 0.0;
        double betaNSdSum = 0.0;
        double etaSum = 0.0;
        double etaRSum = 0.0;
        double etaLSum = 0.0;
        double etaRLSum = 0.0;
        int[][] pathLengths = TopologicalMatrix.getMatrix(container);
        for (int i=0; i<maxAtoms; ++i)
        {
            IAtom atom = tempContainer.getAtom(i);
            List<IAtom> neighbours = tempContainer.getConnectedAtomsList(atom);

            ++N;
            ++Nss;

            // Calculate alpha, and alpha and epsilon reference values for the atom
            int atomicNumber = atom.getAtomicNumber();
            if (atomicNumber!=1)
            {
                ++Nv;

                int maxNeighbours = 0;
                for (IAtom neighbour : neighbours)
                {
                    if (!neighbour.getSymbol().equals("H")) ++maxNeighbours;
                }

                if (maxNeighbours == 0)
                {
                    Nr += 5;            // 1 C + 4 H
                    epsilonRSum += 1.9; // 1 C (0.7) + 4 H (0.3)
                }
                else if (maxNeighbours == 1)
                {
                    Alpha_P += alpha[i];
                    Nr += 4;            // 1 C + 3 H
                    epsilonRSum += 1.6; // 1 C (0.7) + 3 H (0.3)
                }
                else if (maxNeighbours == 2)
                {
                    Nr += 3;            // 1 C + 2 H
                    epsilonRSum += 1.3; // 1 C (0.7) + 2 H (0.3)
                }
                else if (maxNeighbours == 3)
                {
                    Alpha_Y += alpha[i];
                    Nr += 2;            // 1 C + 1 H
                    epsilonRSum += 1.0; // 1 C (0.7) + 1 H (0.3)
                }
                else if (maxNeighbours == 4)
                {
                    Alpha_X += alpha[i];
                    Nr += 1;            // 1 C
                    epsilonRSum += 0.7; // 1 C (0.7)
                }

                epsilonEHSum += epsilon[i];

                // Add new hydrogen count to Nss for saturated carbon skeleton
                if (atom.getSymbol().equals("C"))
                {
                    int extraHydrogens = 0;
                    if (atom.getFlag(CDKConstants.ISAROMATIC))
                    {
                        ++extraHydrogens;
                    }
                    else
                    {
                        for (IAtom neighbour : neighbours)
                        {
                            if (neighbour.getSymbol().equals("C"))
                            {
                                IBond bond = tempContainer.getBond(atom, neighbour);
                                if (bond.getOrder()==Order.DOUBLE)
                                {
                                    ++extraHydrogens;
                                }
                                else if (bond.getOrder()==Order.TRIPLE)
                                {
                                    extraHydrogens += 2;
                                }
                            }
                        }
                    }

                    Nss += extraHydrogens;
                    epsilonSSSum += extraHydrogens * 0.3;
                }
                else if (atom.getSymbol().equals("N"))
                {
                    int extraHydrogens = 0;
                    if (atom.getFlag(CDKConstants.ISAROMATIC))
                    {
                        extraHydrogens = 3 + atom.getFormalCharge() - (int)tempContainer.getBondOrderSum(atom);
                    }
                    Nss += extraHydrogens;
                    epsilonSSSum += extraHydrogens * 0.3;
                }

                betaSSum += betaS[i];
                betaNSSum += betaNS[i];
                betaNSdSum += betaNSd[i];
                for (int j=i+1; j<maxAtoms; ++j)
                {
                    IAtom atom2 = tempContainer.getAtom(j);
                    if (atom2.getSymbol().equals("H")) continue;
                    etaSum += Math.pow(gamma[i]*gamma[j] / (pathLengths[i][j]*pathLengths[i][j]), 0.5);
                    etaRSum += Math.pow(gammaRef[i]*gammaRef[j] / (pathLengths[i][j]*pathLengths[i][j]), 0.5);

                    if (pathLengths[i][j] == 1)
                    {
                        etaLSum += Math.pow(gamma[i]*gamma[j], 0.5);
                        etaRLSum += Math.pow(gammaRef[i]*gammaRef[j], 0.5);
                    }
                }
            }
            else
            {
                for (IAtom neighbour : neighbours)
                {
                    if (!neighbour.getSymbol().equals("C"))
                    {
                        ++Nxh;
                        epsilonXHSum += epsilon[i];
                        break;
                    }
                }
            }

            alphaSum += alpha[i];
            alphaRSum += alphaR[i];
            epsilonSum += epsilon[i];
            epsilonSSSum += epsilon[i];
        }
        betaSSum /= 2;
        betaNSSum = (betaNSSum - betaNSdSum) / 2 + betaNSdSum;

        double ETA_Alpha = alphaSum;
        double ETA_AlphaP = alphaSum / Nv;
        double ETA_dAlpha_A = Math.max((alphaSum - alphaRSum) / Nv, 0.0);
        double ETA_dAlpha_B = Math.max((alphaRSum - alphaSum) / Nv, 0.0);
        double ETA_Epsilon_1 = epsilonSum / N;
        double ETA_Epsilon_2 = epsilonEHSum / Nv;
        double ETA_Epsilon_3 = epsilonRSum / Nr;
        double ETA_Epsilon_4 = epsilonSSSum / Nss;
        double ETA_Epsilon_5 = (epsilonEHSum + epsilonXHSum) / (Nv + Nxh);
        double ETA_dEpsilon_A = ETA_Epsilon_1 - ETA_Epsilon_3;
        double ETA_dEpsilon_B = ETA_Epsilon_1 - ETA_Epsilon_4;
        double ETA_dEpsilon_C = ETA_Epsilon_3 - ETA_Epsilon_4;
        double ETA_dEpsilon_D = ETA_Epsilon_2 - ETA_Epsilon_5;
        double ETA_Psi_1 = alphaSum / epsilonEHSum;
        double PsiTemp = Round(0.71429, decimalPlaces);
        double ETA_dPsi_A = Math.max(PsiTemp - ETA_Psi_1, 0.0);
        double ETA_dPsi_B = Math.max(ETA_Psi_1 - PsiTemp, 0.0);
        double ETA_Shape_P = Alpha_P / ETA_Alpha;
        double ETA_Shape_Y = Alpha_Y / ETA_Alpha;
        double ETA_Shape_X = Alpha_X / ETA_Alpha;
        double ETA_Beta = betaSSum + betaNSSum;
        double ETA_BetaP = ETA_Beta / Nv;
        double ETA_Beta_s = betaSSum;
        double ETA_BetaP_s = betaSSum / Nv;
        double ETA_Beta_ns = betaNSSum;
        double ETA_BetaP_ns = betaNSSum / Nv;
        double ETA_dBeta = betaNSSum - betaSSum;
        double ETA_dBetaP = ETA_dBeta / Nv;
        double ETA_Beta_ns_d = betaNSdSum;
        double ETA_BetaP_ns_d = ETA_Beta_ns_d / Nv;
        double ETA_Eta = etaSum;
        double ETA_EtaP = etaSum / Nv;
        double ETA_Eta_R = etaRSum;
        double ETA_Eta_F = etaRSum - etaSum;
        double ETA_EtaP_F = ETA_Eta_F / Nv;
        double ETA_Eta_L = etaLSum;
        double ETA_EtaP_L = etaLSum / Nv;
        double ETA_Eta_R_L = etaRLSum;
        double ETA_Eta_F_L = etaRLSum - etaLSum;
        double ETA_EtaP_F_L = ETA_Eta_F_L / Nv;
        double ETA_Eta_B = (Nv > 3 ? (1.414 + (Nv-3)*0.5) - etaRLSum : 0);
        double ETA_EtaP_B = ETA_Eta_B / Nv;
        double ETA_Eta_B_RC = ETA_Eta_B + 0.086*maxRings;
        double ETA_EtaP_B_RC = ETA_Eta_B_RC / Nv;

        ETA_Alpha = Round(ETA_Alpha, decimalPlaces);
        ETA_AlphaP = Round(ETA_AlphaP, decimalPlaces);
        ETA_dAlpha_A = Round(ETA_dAlpha_A, decimalPlaces);
        ETA_dAlpha_B = Round(ETA_dAlpha_B, decimalPlaces);
        ETA_Epsilon_1 = Round(ETA_Epsilon_1, decimalPlaces);
        ETA_Epsilon_2 = Round(ETA_Epsilon_2, decimalPlaces);
        ETA_Epsilon_3 = Round(ETA_Epsilon_3, decimalPlaces);
        ETA_Epsilon_4 = Round(ETA_Epsilon_4, decimalPlaces);
        ETA_Epsilon_5 = Round(ETA_Epsilon_5, decimalPlaces);
        ETA_dEpsilon_A = Round(ETA_dEpsilon_A, decimalPlaces);
        ETA_dEpsilon_B = Round(ETA_dEpsilon_B, decimalPlaces);
        ETA_dEpsilon_C = Round(ETA_dEpsilon_C, decimalPlaces);
        ETA_dEpsilon_D = Round(ETA_dEpsilon_D, decimalPlaces);
        ETA_Psi_1 = Round(ETA_Psi_1, decimalPlaces);
        ETA_dPsi_A = Round(ETA_dPsi_A, decimalPlaces);
        ETA_dPsi_B = Round(ETA_dPsi_B, decimalPlaces);
        ETA_Shape_P = Round(ETA_Shape_P, decimalPlaces);
        ETA_Shape_Y = Round(ETA_Shape_Y, decimalPlaces);
        ETA_Shape_X = Round(ETA_Shape_X, decimalPlaces);
        ETA_Beta = Round(ETA_Beta, decimalPlaces);
        ETA_BetaP = Round(ETA_BetaP, decimalPlaces);
        ETA_Beta_s = Round(ETA_Beta_s, decimalPlaces);
        ETA_BetaP_s = Round(ETA_BetaP_s, decimalPlaces);
        ETA_Beta_ns = Round(ETA_Beta_ns, decimalPlaces);
        ETA_BetaP_ns = Round(ETA_BetaP_ns, decimalPlaces);
        ETA_dBeta = Round(ETA_dBeta, decimalPlaces);
        ETA_dBetaP = Round(ETA_dBetaP, decimalPlaces);
        ETA_Beta_ns_d = Round(ETA_Beta_ns_d, decimalPlaces);
        ETA_BetaP_ns_d = Round(ETA_BetaP_ns_d, decimalPlaces);
        ETA_Eta = Round(ETA_Eta, decimalPlaces);
        ETA_EtaP = Round(ETA_EtaP, decimalPlaces);
        ETA_Eta_R = Round(ETA_Eta_R, decimalPlaces);
        ETA_Eta_F = Round(ETA_Eta_F, decimalPlaces);
        ETA_EtaP_F = Round(ETA_EtaP_F, decimalPlaces);
        ETA_Eta_L = Round(ETA_Eta_L, decimalPlaces);
        ETA_EtaP_L = Round(ETA_EtaP_L, decimalPlaces);
        ETA_Eta_R_L = Round(ETA_Eta_R_L, decimalPlaces);
        ETA_Eta_F_L = Round(ETA_Eta_F_L, decimalPlaces);
        ETA_EtaP_F_L = Round(ETA_EtaP_F_L, decimalPlaces);
        ETA_Eta_B = Round(ETA_Eta_B, decimalPlaces);
        ETA_EtaP_B = Round(ETA_EtaP_B, decimalPlaces);
        ETA_Eta_B_RC = Round(ETA_Eta_B_RC, decimalPlaces);
        ETA_EtaP_B_RC = Round(ETA_EtaP_B_RC, decimalPlaces);

        DoubleArrayResult retVal = new DoubleArrayResult();
        retVal.add(ETA_Alpha);
        retVal.add(ETA_AlphaP);
        retVal.add(ETA_dAlpha_A);
        retVal.add(ETA_dAlpha_B);
        retVal.add(ETA_Epsilon_1);
        retVal.add(ETA_Epsilon_2);
        retVal.add(ETA_Epsilon_3);
        retVal.add(ETA_Epsilon_4);
        retVal.add(ETA_Epsilon_5);
        retVal.add(ETA_dEpsilon_A);
        retVal.add(ETA_dEpsilon_B);
        retVal.add(ETA_dEpsilon_C);
        retVal.add(ETA_dEpsilon_D);
        retVal.add(ETA_Psi_1);
        retVal.add(ETA_dPsi_A);
        retVal.add(ETA_dPsi_B);
        retVal.add(ETA_Shape_P);
        retVal.add(ETA_Shape_Y);
        retVal.add(ETA_Shape_X);
        retVal.add(ETA_Beta);
        retVal.add(ETA_BetaP);
        retVal.add(ETA_Beta_s);
        retVal.add(ETA_BetaP_s);
        retVal.add(ETA_Beta_ns);
        retVal.add(ETA_BetaP_ns);
        retVal.add(ETA_dBeta);
        retVal.add(ETA_dBetaP);
        retVal.add(ETA_Beta_ns_d);
        retVal.add(ETA_BetaP_ns_d);
        retVal.add(ETA_Eta);
        retVal.add(ETA_EtaP);
        retVal.add(ETA_Eta_R);
        retVal.add(ETA_Eta_F);
        retVal.add(ETA_EtaP_F);
        retVal.add(ETA_Eta_L);
        retVal.add(ETA_EtaP_L);
        retVal.add(ETA_Eta_R_L);
        retVal.add(ETA_Eta_F_L);
        retVal.add(ETA_EtaP_F_L);
        retVal.add(ETA_Eta_B);
        retVal.add(ETA_EtaP_B);
        retVal.add(ETA_Eta_B_RC);
        retVal.add(ETA_EtaP_B_RC);

        return new DescriptorValue(getSpecification(), getParameterNames(), getParameters(), retVal, names);
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

    // PURPOSE: Round off a real number to the desired number of decimal places (dp).
    private double Round(double val, int dp)
    {
        int modifier = 1;
        for (int i=0; i<dp; ++i) modifier *= 10;
        if (val < 0.0) return (Math.ceil(val * modifier - 0.5) / modifier);
        return (Math.floor(val * modifier + 0.5) / modifier);
    }
}
