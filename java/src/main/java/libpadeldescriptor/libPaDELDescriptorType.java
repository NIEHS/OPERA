package libpadeldescriptor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Calculate CDK descriptors using different threads.
 * 
 * @author yapchunwei
 */
public class libPaDELDescriptorType
{
    private libPaDELDescriptorType()
    {
        
    }

    public static void SetDescriptorTypes(Set<String> descriptorTypes, List<String> descriptorNames, List<CDK_Descriptor> cdk_descriptors, List<CDK_Fingerprint> cdk_fingerprints, List<CDK_FingerprintCount> cdk_fingerprints_count)
    {
        List<CDK_Descriptor> cdkdescriptors = new ArrayList<CDK_Descriptor>();
        List<CDK_Fingerprint> cdkfingerprints = new ArrayList<CDK_Fingerprint>();
        List<CDK_FingerprintCount> cdkfingerprintscount = new ArrayList<CDK_FingerprintCount>();

        // 1D & 2D descriptors
        if (descriptorTypes.contains("AcidicGroupCount"))
        {
            cdkdescriptors.add(new CDK_AcidicGroupCountDescriptor());
        }

        if (descriptorTypes.contains("ALOGP"))
        {
            cdkdescriptors.add(new CDK_ALOGPDescriptor());
        }

        if (descriptorTypes.contains("AminoAcidCount"))
        {
            cdkdescriptors.add(new CDK_AminoAcidCountDescriptor());
        }

        if (descriptorTypes.contains("APol"))
        {
            cdkdescriptors.add(new CDK_APolDescriptor());
        }

        if (descriptorTypes.contains("AromaticAtomsCount"))
        {
            cdkdescriptors.add(new CDK_AromaticAtomsCountDescriptor());
        }

        if (descriptorTypes.contains("AromaticBondsCount"))
        {
            cdkdescriptors.add(new CDK_AromaticBondsCountDescriptor());
        }

        if (descriptorTypes.contains("AtomCount"))
        {
            cdkdescriptors.add(new CDK_AtomCountDescriptor(new String[] {"*"}));
            cdkdescriptors.add(new CDK_HeavyAtomCountDescriptor());
            cdkdescriptors.add(new CDK_AtomCountDescriptor(new String[] {"H"}));
            cdkdescriptors.add(new CDK_AtomCountDescriptor(new String[] {"B"}));
            cdkdescriptors.add(new CDK_AtomCountDescriptor(new String[] {"C"}));
            cdkdescriptors.add(new CDK_AtomCountDescriptor(new String[] {"N"}));
            cdkdescriptors.add(new CDK_AtomCountDescriptor(new String[] {"O"}));
            cdkdescriptors.add(new CDK_AtomCountDescriptor(new String[] {"S"}));
            cdkdescriptors.add(new CDK_AtomCountDescriptor(new String[] {"P"}));
            cdkdescriptors.add(new CDK_HalogenCountDescriptor());
        }
        
        if (descriptorTypes.contains("Autocorrelation"))
        {
            cdkdescriptors.add(new CDK_AutocorrelationDescriptor());
        }

        if (descriptorTypes.contains("BaryszMatrix"))
        {
            cdkdescriptors.add(new CDK_BaryszMatrixDescriptor());
        }

        if (descriptorTypes.contains("BasicGroupCount"))
        {
            cdkdescriptors.add(new CDK_BasicGroupCountDescriptor());
        }

        if (descriptorTypes.contains("BCUT"))
        {
            cdkdescriptors.add(new CDK_BCUTDescriptor());
        }

        if (descriptorTypes.contains("BondCount"))
        {
            cdkdescriptors.add(new CDK_BondCountDescriptor());
        }

        if (descriptorTypes.contains("BPol"))
        {
            cdkdescriptors.add(new CDK_BPolDescriptor());
        }

        if (descriptorTypes.contains("BurdenModifiedEigenvalues"))
        {
            cdkdescriptors.add(new CDK_BurdenModifiedEigenvaluesDescriptor());
        }

        if (descriptorTypes.contains("CarbonTypes"))
        {
            cdkdescriptors.add(new CDK_CarbonTypesDescriptor());
        }

        if (descriptorTypes.contains("ChiChain"))
        {
            cdkdescriptors.add(new CDK_ChiChainDescriptor());
        }

        if (descriptorTypes.contains("ChiCluster"))
        {
            cdkdescriptors.add(new CDK_ChiClusterDescriptor());
        }

        if (descriptorTypes.contains("ChiPathCluster"))
        {
            cdkdescriptors.add(new CDK_ChiPathClusterDescriptor());
        }

        if (descriptorTypes.contains("ChiPath"))
        {
            cdkdescriptors.add(new CDK_ChiPathDescriptor());
        }
        
        if (descriptorTypes.contains("Constitutional"))
        {
            cdkdescriptors.add(new CDK_ConstitutionalDescriptor());
        }
        
        if (descriptorTypes.contains("Crippen"))
        {
            cdkdescriptors.add(new CDK_CrippenDescriptor());
        }

        if (descriptorTypes.contains("DetourMatrix"))
        {
            cdkdescriptors.add(new CDK_DetourMatrixDescriptor());
        }

        if (descriptorTypes.contains("EccentricConnectivityIndex"))
        {
            cdkdescriptors.add(new CDK_EccentricConnectivityIndexDescriptor());
        }

        if (descriptorTypes.contains("EStateAtomType"))
        {
            cdkdescriptors.add(new CDK_EStateAtomTypeDescriptor());
        }
                        
        if (descriptorTypes.contains("ExtendedTopochemicalAtom"))
        {
            cdkdescriptors.add(new CDK_ExtendedTopochemicalAtomDescriptor());
        }

        if (descriptorTypes.contains("FMF"))
        {
            cdkdescriptors.add(new CDK_FMFDescriptor());
        }

        if (descriptorTypes.contains("FragmentComplexity"))
        {
            cdkdescriptors.add(new CDK_FragmentComplexityDescriptor());
        }

        if (descriptorTypes.contains("HBondAcceptorCount"))
        {
            cdkdescriptors.add(new CDK_HBondAcceptorCountDescriptor());
        }

        if (descriptorTypes.contains("HBondDonorCount"))
        {
            cdkdescriptors.add(new CDK_HBondDonorCountDescriptor());
        }

        if (descriptorTypes.contains("HybridizationRatio"))
        {
            cdkdescriptors.add(new CDK_HybridizationRatioDescriptor());
        }
        
        if (descriptorTypes.contains("InformationContent"))
        {
            cdkdescriptors.add(new CDK_InformationContentDescriptor());
        }

        if (descriptorTypes.contains("IPMolecularLearning"))
        {
            cdkdescriptors.add(new CDK_IPMolecularLearningDescriptor());
        }

        if (descriptorTypes.contains("KappaShapeIndices"))
        {
            cdkdescriptors.add(new CDK_KappaShapeIndicesDescriptor());
        }

        if (descriptorTypes.contains("KierHallSmarts"))
        {
            cdkdescriptors.add(new CDK_KierHallSmartsDescriptor());
        }

        if (descriptorTypes.contains("LargestChain"))
        {
            cdkdescriptors.add(new CDK_LargestChainDescriptor());
        }

        if (descriptorTypes.contains("LargestPiSystem"))
        {
            cdkdescriptors.add(new CDK_LargestPiSystemDescriptor());
        }

        if (descriptorTypes.contains("LongestAliphaticChain"))
        {
            cdkdescriptors.add(new CDK_LongestAliphaticChainDescriptor());
        }

        if (descriptorTypes.contains("MannholdLogP"))
        {
            cdkdescriptors.add(new CDK_MannholdLogPDescriptor());
        }

        if (descriptorTypes.contains("McGowanVolume"))
        {
            cdkdescriptors.add(new CDK_McGowanVolumeDescriptor());
        }

        if (descriptorTypes.contains("MDE"))
        {
            cdkdescriptors.add(new CDK_MDEDescriptor());
        }

        if (descriptorTypes.contains("MLFER"))
        {
            cdkdescriptors.add(new CDK_MLFERDescriptor());
        }

        if (descriptorTypes.contains("PathCount"))
        {
            cdkdescriptors.add(new CDK_PathCountDescriptor());
        }

        if (descriptorTypes.contains("PetitjeanNumber"))
        {
            cdkdescriptors.add(new CDK_PetitjeanNumberDescriptor());
        }

        if (descriptorTypes.contains("RingCount"))
        {
            cdkdescriptors.add(new CDK_RingCountDescriptor());
        }

        if (descriptorTypes.contains("RotatableBondsCount"))
        {
            cdkdescriptors.add(new CDK_RotatableBondsCountDescriptor());
        }

        if (descriptorTypes.contains("RuleOfFive"))
        {
            cdkdescriptors.add(new CDK_RuleOfFiveDescriptor());
        }
        
        if (descriptorTypes.contains("Topological"))
        {
            cdkdescriptors.add(new CDK_TopologicalDescriptor());
        }
        
        if (descriptorTypes.contains("TopologicalCharge"))
        {
            cdkdescriptors.add(new CDK_TopologicalChargeDescriptor());
        }

        if (descriptorTypes.contains("TopologicalDistanceMatrix"))
        {
            cdkdescriptors.add(new CDK_TopologicalDistanceMatrixDescriptor());
        }

        if (descriptorTypes.contains("TPSA"))
        {
            cdkdescriptors.add(new CDK_TPSADescriptor());
        }

        if (descriptorTypes.contains("VABC"))
        {
            cdkdescriptors.add(new CDK_VABCDescriptor());
        }

        if (descriptorTypes.contains("VAdjMa"))
        {
            cdkdescriptors.add(new CDK_VAdjMaDescriptor());
        }

        if (descriptorTypes.contains("WalkCount"))
        {
            cdkdescriptors.add(new CDK_WalkCountDescriptor());
        }

        if (descriptorTypes.contains("Weight"))
        {
            cdkdescriptors.add(new CDK_WeightDescriptor());
        }

        if (descriptorTypes.contains("WeightedPath"))
        {
            cdkdescriptors.add(new CDK_WeightedPathDescriptor());
        }

        if (descriptorTypes.contains("WienerNumbers"))
        {
            cdkdescriptors.add(new CDK_WienerNumbersDescriptor());
        }

        if (descriptorTypes.contains("XLogP"))
        {
            cdkdescriptors.add(new CDK_XLogPDescriptor(new Boolean[] {false, true}));
        }

        if (descriptorTypes.contains("ZagrebIndex"))
        {
            cdkdescriptors.add(new CDK_ZagrebIndexDescriptor());
        }

        // 3D descriptors
        if (descriptorTypes.contains("Autocorrelation3D"))
        {
            cdkdescriptors.add(new CDK_Autocorrelation3DDescriptor());
        }

        if (descriptorTypes.contains("CPSA"))
        {
            cdkdescriptors.add(new CDK_CPSADescriptor());
        }

        if (descriptorTypes.contains("GravitationalIndex"))
        {
            cdkdescriptors.add(new CDK_GravitationalIndexDescriptor());
        }

        if (descriptorTypes.contains("LengthOverBreadth"))
        {
            cdkdescriptors.add(new CDK_LengthOverBreadthDescriptor());
        }

        if (descriptorTypes.contains("MomentOfInertia"))
        {
            cdkdescriptors.add(new CDK_MomentOfInertiaDescriptor());
        }

        if (descriptorTypes.contains("PetitjeanShapeIndex"))
        {
            cdkdescriptors.add(new CDK_PetitjeanShapeIndexDescriptor());
        }

        if (descriptorTypes.contains("RDF"))
        {
            cdkdescriptors.add(new CDK_RDFDescriptor());
        }
 
        if (descriptorTypes.contains("WHIM"))
        {
            cdkdescriptors.add(new CDK_WHIMDescriptor());
        }

        // Fingerprints
        if (descriptorTypes.contains("Fingerprinter"))
        {
            cdkfingerprints.add(new CDK_Fingerprinter());
        }

        if (descriptorTypes.contains("ExtendedFingerprinter"))
        {
            cdkfingerprints.add(new CDK_ExtendedFingerprinter());
        }

        if (descriptorTypes.contains("EStateFingerprinter"))
        {
            cdkfingerprints.add(new CDK_EStateFingerprinter());
        }

        if (descriptorTypes.contains("GraphOnlyFingerprinter"))
        {
            cdkfingerprints.add(new CDK_GraphOnlyFingerprinter());
        }

        if (descriptorTypes.contains("MACCSFingerprinter"))
        {
            cdkfingerprints.add(new CDK_MACCSFingerprinter());
        }

        if (descriptorTypes.contains("PubchemFingerprinter"))
        {
            cdkfingerprints.add(new CDK_PubchemFingerprinter());
        }

        if (descriptorTypes.contains("SubstructureFingerprinter"))
        {
            cdkfingerprints.add(new CDK_SubstructureFingerprinter());
        }

        if (descriptorTypes.contains("KlekotaRothFingerprinter"))
        {
            cdkfingerprints.add(new CDK_KlekotaRothFingerprinter());
        }

        if (descriptorTypes.contains("AtomPairs2DFingerprinter"))
        {
            cdkfingerprints.add(new CDK_AtomPairs2DFingerprinter());
        }

        // Fingerprints counts
        if (descriptorTypes.contains("SubstructureFingerprintCount"))
        {
            cdkfingerprintscount.add(new CDK_SubstructureFingerprintCount());
        }

        if (descriptorTypes.contains("KlekotaRothFingerprintCount"))
        {
            cdkfingerprintscount.add(new CDK_KlekotaRothFingerprintCount());
        }

        if (descriptorTypes.contains("AtomPairs2DFingerprintCount"))
        {
            cdkfingerprintscount.add(new CDK_AtomPairs2DFingerprintCount());
        }

        // Get descriptor names
        if (descriptorNames!=null)
        {
            descriptorNames.clear();
            for (CDK_Descriptor cdkdescriptor : cdkdescriptors)
            {
                descriptorNames.addAll(Arrays.asList(cdkdescriptor.getDescriptorNames()));
            }

            for (CDK_Fingerprint cdkfingerprint : cdkfingerprints)
            {
                descriptorNames.addAll(Arrays.asList(cdkfingerprint.getDescriptorNames()));
            }

            for (CDK_FingerprintCount cdkfingerprintcount : cdkfingerprintscount)
            {
                descriptorNames.addAll(Arrays.asList(cdkfingerprintcount.getDescriptorNames()));
            }
        }

        if (cdk_descriptors!=null)
        {
            cdk_descriptors.clear();
            cdk_descriptors.addAll(cdkdescriptors);
        }

        if (cdk_fingerprints!=null)
        {
            cdk_fingerprints.clear();
            cdk_fingerprints.addAll(cdkfingerprints);
        }

        if (cdk_fingerprints_count!=null)
        {
            cdk_fingerprints_count.clear();
            cdk_fingerprints_count.addAll(cdkfingerprintscount);
        }
    }

    /**
     *
     * @param file                  XML file containing list of descriptors to calculate.
     * @return                      Names of descriptors to calculate.
     */
    public static Set<DescriptorStruct> GetDescriptorTypes(String file)
    {
        try
        {
            return GetDescriptorTypes(new FileInputStream(file));
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     *
     * @param in                    input stream containing list of descriptors to calculate.
     * @return                      Names of descriptors.
     */
    public static Set<DescriptorStruct> GetDescriptorTypes(InputStream in)
    {
        Set<DescriptorStruct> descriptors = new LinkedHashSet<DescriptorStruct>();

        if (in != null)
        {
            Document document = null;
            try
            {
                document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
            }
            catch (Exception ex)
            {
                Logger.getLogger("global").log(Level.SEVERE, "Cannot read descriptor types file.", ex);
                return null;
            }
            Element rootElement = document.getDocumentElement();
            NodeList groups = rootElement.getElementsByTagName("Group");
            int maxGroups = groups.getLength();
            for (int i=0; i<maxGroups; ++i)
            {
                Node node = groups.item(i);
                if (node instanceof Element)
                {
                    Element groupTag = (Element)node;
                    String type = groupTag.getAttribute("name");

                    NodeList descriptorList = groupTag.getElementsByTagName("Descriptor");
                    int maxDescriptors = descriptorList.getLength();
                    for (int j=0; j<maxDescriptors; ++j)
                    {
                        Node descriptor = descriptorList.item(j);
                        if (descriptor instanceof Element)
                        {
                            Element descriptorTag = (Element)descriptor;
                            String name = descriptorTag.getAttribute("name");
                            boolean active = Boolean.valueOf(descriptorTag.getAttribute("value"));
                            descriptors.add(new DescriptorStruct(name, type, active));
                        }
                    }
                }
            }

            try
            {
                in.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger("global").log(Level.FINE, null, ex);
            }
        }

        return descriptors;
    }

    /**
     *
     * @param file                  XML file to save descriptor types.
     * @param descriptors           oSet containing descriptor types information.
     * @return                      Names of descriptors.
     */
    public static void SaveDescriptorTypes(String file, Set<DescriptorStruct> descriptors)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            SaveDescriptorTypes(fos, descriptors);
            fos.close();
        }
        catch (Exception ex)
        {
            Logger.getLogger("global").log(Level.FINE, "Cannot save descriptor types.", ex);
        }
    }

    /**
     *
     * @param out                   output stream containing list of descriptors to calculate.
     * @param descriptors           oSet containing descriptor types information.
     * @return                      Names of descriptors.
     */
    public static void SaveDescriptorTypes(OutputStream out, Set<DescriptorStruct> descriptors)
    {
        if (out!=null)
        {
            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                DOMImplementation impl = builder.getDOMImplementation();
                Document doc = impl.createDocument(null, null, null);
                Element root = doc.createElement("Root");
                doc.appendChild(root);
                Element group2D = doc.createElement("Group");
                group2D.setAttribute("name", DescriptorStruct.TYPE_2D);
                Element group3D = doc.createElement("Group");
                group3D.setAttribute("name", DescriptorStruct.TYPE_3D);
                Element groupFingerprint = doc.createElement("Group");
                groupFingerprint.setAttribute("name", DescriptorStruct.TYPE_FINGERPRINT);
                for (DescriptorStruct descriptor : descriptors)
                {
                    Element d = doc.createElement("Descriptor");
                    d.setAttribute("name", descriptor.getName());
                    d.setAttribute("value", Boolean.toString(descriptor.isActive()));

                    if (descriptor.getType().equals(DescriptorStruct.TYPE_2D))
                    {
                        group2D.appendChild(d);
                    }
                    else if (descriptor.getType().equals(DescriptorStruct.TYPE_3D))
                    {
                        group3D.appendChild(d);
                    }
                    else if (descriptor.getType().equals(DescriptorStruct.TYPE_FINGERPRINT))
                    {
                        groupFingerprint.appendChild(d);
                    }
                }
                root.appendChild(group2D);
                root.appendChild(group3D);
                root.appendChild(groupFingerprint);
                DOMSource domSource = new DOMSource(doc);
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                StringWriter sw = new StringWriter();
                StreamResult sr = new StreamResult(out);
                transformer.transform(domSource, sr);
            }
            catch (Exception ex)
            {
                Logger.getLogger("global").log(Level.FINE, "Cannot save descriptor types.", ex);
            }
        }
    }

    /**
     *
     * @param file                  XML file containing list of descriptors to calculate.
     * @param compute2D             If false, will not calculate 1D, 2D descriptors.
     * @param compute3D             If false, will not calculate 3D descriptors.
     * @param computeFingerprints   If false, will not calculate fingerprints.
     * @return                      Names of descriptors to calculate.
     */
    public static Set<String> GetActiveDescriptorTypes(String file, boolean compute2D, boolean compute3D, boolean computeFingerprints)
    {
        try
        {
            return GetActiveDescriptorTypes(new FileInputStream(file), compute2D, compute3D, computeFingerprints);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    /**
     *
     * @param in                    input stream containing list of descriptors to calculate.
     * @param compute2D             If false, will not calculate 1D, 2D descriptors.
     * @param compute3D             If false, will not calculate 3D descriptors.
     * @param computeFingerprints   If false, will not calculate fingerprints.
     * @return                      Names of descriptors to calculate.
     */
    public static Set<String> GetActiveDescriptorTypes(InputStream in, boolean compute2D, boolean compute3D, boolean computeFingerprints)
    {
        return GetActiveDescriptorTypes(GetDescriptorTypes(in), compute2D, compute3D, computeFingerprints);
    }

    /**
     *
     * @param descriptors           Set containing descriptor types information.
     * @param compute2D             If false, will not calculate 1D, 2D descriptors.
     * @param compute3D             If false, will not calculate 3D descriptors.
     * @param computeFingerprints   If false, will not calculate fingerprints.
     * @return                      Names of descriptors to calculate.
     */
    public static Set<String> GetActiveDescriptorTypes(Set<DescriptorStruct> descriptors, boolean compute2D, boolean compute3D, boolean computeFingerprints)
    {
        Set<String> activeDescriptors = new HashSet<String>();
        if (descriptors!=null)
        {
            for (DescriptorStruct descriptor : descriptors)
            {
                String type = descriptor.getType();
                if (type.equals(DescriptorStruct.TYPE_2D) && !compute2D) continue;
                if (type.equals(DescriptorStruct.TYPE_3D) && !compute3D) continue;
                if (type.equals(DescriptorStruct.TYPE_FINGERPRINT) && !computeFingerprints) continue;
                if (descriptor.isActive()) activeDescriptors.add(descriptor.getName());
            }
            return activeDescriptors;
        }
        else return null;
    }
}

