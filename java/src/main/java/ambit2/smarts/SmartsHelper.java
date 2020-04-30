/*     */ package ambit2.smarts;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringWriter;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.openscience.cdk1.Atom;
/*     */ import org.openscience.cdk1.graph.ConnectivityChecker;
/*     */ import org.openscience.cdk1.interfaces.IAtom;
/*     */ import org.openscience.cdk1.interfaces.IAtomContainer;
/*     */ import org.openscience.cdk1.interfaces.IBond;
/*     */ import org.openscience.cdk1.interfaces.IBond.Order;
/*     */ import org.openscience.cdk1.interfaces.IChemObjectBuilder;
/*     */ import org.openscience.cdk1.interfaces.IMolecule;
/*     */ import org.openscience.cdk1.interfaces.IMoleculeSet;
/*     */ import org.openscience.cdk1.io.SMILESWriter;
/*     */ import org.openscience.cdk1.isomorphism.matchers.QueryAtomContainer;
/*     */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSAtom;
/*     */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSBond;
/*     */ import org.openscience.cdk1.silent.SilentChemObjectBuilder;
/*     */ import org.openscience.cdk1.smiles.SmilesParser;
/*     */ import org.openscience.cdk1.tools.CDKHydrogenAdder;
/*     */ import org.openscience.cdk1.tools.manipulator.AtomContainerManipulator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SmartsHelper
/*     */ {
/*     */   static SmilesParser smilesparser;
/*     */   int curIndex;
/*  53 */   HashMap<IAtom, TopLayer> firstSphere = new HashMap();
/*     */   
/*  55 */   HashMap<IAtom, AtomSmartsNode> nodes = new HashMap();
/*  56 */   HashMap<IAtom, String> atomIndexes = new HashMap();
/*  57 */   Vector<IBond> ringClosures = new Vector();
/*     */   
/*     */   int nAtom;
/*     */   int nBond;
/*     */   
/*     */   public SmartsHelper(IChemObjectBuilder builder)
/*     */   {
/*  64 */     smilesparser = new SmilesParser(builder);
/*     */   }
/*     */   
/*     */   public static String getAtomsString(QueryAtomContainer query) {
/*  68 */     StringBuffer sb = new StringBuffer();
/*     */     
/*  70 */     for (int i = 0; i < query.getAtomCount(); i++)
/*     */     {
/*  72 */       sb.append(atomToString((SMARTSAtom)query.getAtom(i)) + " ");
/*     */     }
/*  74 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getAtomExpressionTokens(SmartsAtomExpression expression)
/*     */   {
/*  80 */     StringBuffer sb = new StringBuffer();
/*     */     
/*  82 */     for (int k = 0; k < expression.tokens.size(); k++)
/*     */     {
/*  84 */       sb.append("tok(" + ((SmartsExpressionToken)expression.tokens.get(k)).type + "," + ((SmartsExpressionToken)expression.tokens.get(k)).param + ") ");
/*     */     }
/*  86 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String getAtomsString(IAtomContainer container)
/*     */   {
/*  91 */     StringBuffer sb = new StringBuffer();
/*     */     
/*  93 */     for (int i = 0; i < container.getAtomCount(); i++)
/*  94 */       sb.append(container.getAtom(i).getSymbol() + " ");
/*  95 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String getAtomsAttributes(IAtomContainer container)
/*     */   {
/* 100 */     StringBuffer sb = new StringBuffer();
/*     */     
/* 102 */     for (int i = 0; i < container.getAtomCount(); i++)
/*     */     {
/* 104 */       IAtom at = container.getAtom(i);
/* 105 */       sb.append("  #" + i + "  ");
/* 106 */       sb.append(at.getSymbol());
/* 107 */       Integer explHInt = (Integer)at.getProperty("ExplicitH");
/* 108 */       int explHAt = 0;
/* 109 */       if (explHInt != null) {
/* 110 */         explHAt = explHInt.intValue();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 115 */       sb.append(" NumH=" + (at.getImplicitHydrogenCount().intValue() + explHAt));
/* 116 */       if (at.getFlag(5))
/* 117 */         sb.append(" aromatic");
/* 118 */       sb.append("\n");
/*     */     }
/* 120 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String getBondAttributes(IAtomContainer container)
/*     */   {
/* 125 */     StringBuffer sb = new StringBuffer();
/* 126 */     for (int i = 0; i < container.getBondCount(); i++)
/*     */     {
/* 128 */       IBond bo = container.getBond(i);
/* 129 */       IAtom at0 = bo.getAtom(0);
/* 130 */       IAtom at1 = bo.getAtom(1);
/* 131 */       int at0_num = container.getAtomNumber(at0);
/* 132 */       int at1_num = container.getAtomNumber(at1);
/* 133 */       sb.append("  #" + i + "  (" + at0_num + "," + at1_num + ")");
/*     */       
/* 135 */       if (bo.getFlag(5)) {
/* 136 */         sb.append(" aromatic");
/*     */       }
/*     */       
/* 139 */       sb.append("\n");
/*     */     }
/*     */     
/* 142 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String getBondsString(IAtomContainer query)
/*     */   {
/* 147 */     StringBuffer sb = new StringBuffer();
/*     */     
/* 149 */     for (int i = 0; i < query.getBondCount(); i++)
/*     */     {
/* 151 */       sb.append(query.getBond(i).getOrder() + " ");
/*     */     }
/* 153 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String bondToStringExhaustive(QueryAtomContainer query, IBond bond)
/*     */   {
/* 158 */     StringBuffer sb = new StringBuffer();
/* 159 */     sb.append(bondToString(bond) + " " + bondAtomNumbersToString(query, bond) + "  " + atomToString(bond.getAtom(0)) + " " + atomToString(bond.getAtom(1)) + "\n");
/*     */     
/*     */ 
/* 162 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String getBondsString(QueryAtomContainer query)
/*     */   {
/* 167 */     StringBuffer sb = new StringBuffer();
/* 168 */     for (int i = 0; i < query.getBondCount(); i++)
/*     */     {
/* 170 */       sb.append(bondToString(query.getBond(i)) + " " + bondAtomNumbersToString(query, query.getBond(i)) + "  " + atomToString(query.getBond(i).getAtom(0)) + " " + atomToString(query.getBond(i).getAtom(1)) + "\n");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 175 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static QueryAtomContainer getQueryAtomContainer(IAtomContainer ac, boolean HandleAromaticity)
/*     */   {
/* 180 */     QueryAtomContainer query = new QueryAtomContainer();
/* 181 */     for (int i = 0; i < ac.getAtomCount(); i++)
/*     */     {
/* 183 */       IAtom a = ac.getAtom(i);
/* 184 */       if (HandleAromaticity)
/*     */       {
/* 186 */         if (a.getFlag(5))
/*     */         {
/* 188 */           AromaticSymbolQueryAtom newAt = new AromaticSymbolQueryAtom();
/* 189 */           newAt.setSymbol(a.getSymbol());
/* 190 */           query.addAtom(newAt);
/*     */         }
/*     */         else
/*     */         {
/* 194 */           AliphaticSymbolQueryAtom newAt = new AliphaticSymbolQueryAtom();
/* 195 */           newAt.setSymbol(a.getSymbol());
/* 196 */           query.addAtom(newAt);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 201 */         SymbolQueryAtomAromaticityNotSpecified newAt = new SymbolQueryAtomAromaticityNotSpecified();
/* 202 */         newAt.setSymbol(a.getSymbol());
/* 203 */         query.addAtom(newAt);
/*     */       }
/*     */     }
/*     */     
/* 207 */     for (int i = 0; i < ac.getBondCount(); i++)
/*     */     {
/* 209 */       IBond b = ac.getBond(i);
/* 210 */       IAtom at0 = b.getAtom(0);
/* 211 */       IAtom at1 = b.getAtom(1);
/* 212 */       int index0 = ac.getAtomNumber(at0);
/* 213 */       int index1 = ac.getAtomNumber(at1);
/*     */       
/*     */       SMARTSBond newBo;
/* 217 */       if (b.getOrder() == IBond.Order.TRIPLE) {
/* 218 */         newBo = new TripleBondAromaticityNotSpecified();
/*     */       } else {
/* 221 */         if (b.getOrder() == IBond.Order.DOUBLE) {
/* 222 */           newBo = new DoubleBondAromaticityNotSpecified();
/*     */         } else {
/* 225 */           if (HandleAromaticity)
/*     */           {
/* 227 */             boolean isArom = b.getFlag(5);
/* 228 */              if (isArom) {
/* 229 */               newBo = new SingleOrAromaticBond();
/*     */             } else {
/* 231 */               newBo = new SingleNonAromaticBond();
/*     */             }
/*     */           } else {
/* 234 */             newBo = new SingleBondAromaticityNotSpecified();
/*     */           }
/*     */         }
/*     */       }
/* 238 */       IAtom[] atoms = new Atom[2];
/* 239 */       atoms[0] = query.getAtom(index0);
/* 240 */       atoms[1] = query.getAtom(index1);
/* 241 */       newBo.setAtoms(atoms);
/* 242 */       query.addBond(newBo);
/*     */     }
/*     */     
/* 245 */     return query;
/*     */   }
/*     */   
/*     */   public static int bondOrderToIntValue(IBond b)
/*     */   {
/* 250 */     if (b.getOrder() == IBond.Order.SINGLE)
/* 251 */       return 1;
/* 252 */     if (b.getOrder() == IBond.Order.DOUBLE)
/* 253 */       return 2;
/* 254 */     if (b.getOrder() == IBond.Order.TRIPLE)
/* 255 */       return 3;
/* 256 */     if (b.getOrder() == IBond.Order.QUADRUPLE) {
/* 257 */       return 4;
/*     */     }
/*     */     
/* 260 */     return 0;
/*     */   }
/*     */   
/*     */   public static String atomToString(IAtom a)
/*     */   {
/* 265 */     if ((a instanceof SmartsAtomExpression))
/* 266 */       return a.toString();
/* 267 */     if ((a instanceof AliphaticSymbolQueryAtom))
/* 268 */       return a.getSymbol();
/* 269 */     if ((a instanceof AromaticSymbolQueryAtom)) {
/* 270 */       return "Ar-" + a.getSymbol();
/*     */     }
/* 272 */     return a.getSymbol();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String bondToString(IBond b)
/*     */   {
/* 279 */     if ((b instanceof SmartsBondExpression))
/* 280 */       return b.toString();
/* 281 */     if ((b instanceof SingleOrAromaticBond)) {
/* 282 */       return "";
/*     */     }
/* 284 */     if (b.getOrder() == IBond.Order.SINGLE)
/* 285 */       return "-";
/* 286 */     if (b.getOrder() == IBond.Order.DOUBLE)
/* 287 */       return "=";
/* 288 */     if (b.getOrder() == IBond.Order.TRIPLE) {
/* 289 */       return "#";
/*     */     }
/* 291 */     return "-";
/*     */   }
/*     */   
/*     */   public static String smilesBondToString(IBond b, boolean aromaticity)
/*     */   {
/* 296 */     if ((aromaticity) && 
/* 297 */       (b.getFlag(5))) {
/* 298 */       return "";
/*     */     }
/* 300 */     if (b.getOrder() == IBond.Order.SINGLE)
/* 301 */       return "";
/* 302 */     if (b.getOrder() == IBond.Order.DOUBLE)
/* 303 */       return "=";
/* 304 */     if (b.getOrder() == IBond.Order.TRIPLE) {
/* 305 */       return "#";
/*     */     }
/* 307 */     return "";
/*     */   }
/*     */   
/*     */   public static String bondAtomNumbersToString(IAtomContainer container, IBond b)
/*     */   {
/* 312 */     return " " + container.getAtomNumber(b.getAtom(0)) + " " + container.getAtomNumber(b.getAtom(1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void determineFirstSheres(QueryAtomContainer query)
/*     */   {
/* 319 */     this.firstSphere.clear();
/* 320 */     this.nAtom = query.getAtomCount();
/* 321 */     this.nBond = query.getBondCount();
/*     */     
/* 323 */     for (int i = 0; i < this.nAtom; i++)
/*     */     {
/* 325 */       this.firstSphere.put(query.getAtom(i), new TopLayer());
/*     */     }
/*     */     
/* 328 */     for (int i = 0; i < this.nBond; i++)
/*     */     {
/* 330 */       IBond bond = query.getBond(i);
/* 331 */       IAtom at0 = bond.getAtom(0);
/* 332 */       IAtom at1 = bond.getAtom(1);
/* 333 */       ((TopLayer)this.firstSphere.get(at0)).atoms.add(at1);
/* 334 */       ((TopLayer)this.firstSphere.get(at0)).bonds.add(bond);
/* 335 */       ((TopLayer)this.firstSphere.get(at1)).atoms.add(at0);
/* 336 */       ((TopLayer)this.firstSphere.get(at1)).bonds.add(bond);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toSmarts(QueryAtomContainer query)
/*     */   {
/* 342 */     determineFirstSheres(query);
/* 343 */     this.nodes.clear();
/* 344 */     this.atomIndexes.clear();
/* 345 */     this.ringClosures.clear();
/* 346 */     this.curIndex = 1;
/* 347 */     AtomSmartsNode node = new AtomSmartsNode();
/* 348 */     node.parent = null;
/* 349 */     node.atom = query.getAtom(0);
/* 350 */     this.nodes.put(node.atom, node);
/* 351 */     return nodeToString(node.atom);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void addIndexToAtom(String ind, IAtom atom)
/*     */   {
/* 358 */     if (this.atomIndexes.containsKey(atom))
/*     */     {
/* 360 */       String old_ind = (String)this.atomIndexes.get(atom);
/* 361 */       this.atomIndexes.remove(atom);
/* 362 */       this.atomIndexes.put(atom, old_ind + ind);
/*     */     }
/*     */     else {
/* 365 */       this.atomIndexes.put(atom, ind);
/*     */     }
/*     */   }
/*     */   
/*     */   String nodeToString(IAtom atom) {
/* 370 */     StringBuffer sb = new StringBuffer();
/* 371 */     TopLayer afs = (TopLayer)this.firstSphere.get(atom);
/* 372 */     AtomSmartsNode curNode = (AtomSmartsNode)this.nodes.get(atom);
/* 373 */     Vector<String> branches = new Vector();
/* 374 */     for (int i = 0; i < afs.atoms.size(); i++)
/*     */     {
/* 376 */       IAtom neighborAt = (IAtom)afs.atoms.get(i);
/* 377 */       if (neighborAt != curNode.parent)
/*     */       {
/*     */ 
/* 380 */         AtomSmartsNode neighborNode = (AtomSmartsNode)this.nodes.get(neighborAt);
/* 381 */         if (neighborNode == null)
/*     */         {
/*     */ 
/* 384 */           AtomSmartsNode newNode = new AtomSmartsNode();
/* 385 */           newNode.atom = neighborAt;
/* 386 */           newNode.parent = atom;
/* 387 */           this.nodes.put(newNode.atom, newNode);
/*     */           
/* 389 */           String bond_str = bondToString((IBond)afs.bonds.get(i));
/* 390 */           String newBranch = bond_str + nodeToString(neighborAt);
/* 391 */           branches.add(newBranch);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 396 */           IBond neighborBo = (IBond)afs.bonds.get(i);
/* 397 */           if (!this.ringClosures.contains(neighborBo))
/*     */           {
/* 399 */             this.ringClosures.add(neighborBo);
/* 400 */             String ind = "" + this.curIndex;
/* 401 */             addIndexToAtom(bondToString(neighborBo) + ind, atom);
/* 402 */             addIndexToAtom(ind, neighborAt);
/* 403 */             this.curIndex += 1;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 408 */     sb.append(atomToString((SMARTSAtom)atom));
/*     */     
/*     */ 
/* 411 */     if (this.atomIndexes.containsKey(atom)) {
/* 412 */       sb.append((String)this.atomIndexes.get(atom));
/*     */     }
/*     */     
/* 415 */     if (branches.size() == 0) {
/* 416 */       return sb.toString();
/*     */     }
/* 418 */     for (int i = 0; i < branches.size() - 1; i++)
/* 419 */       sb.append("(" + ((String)branches.get(i)).toString() + ")");
/* 420 */     sb.append(((String)branches.lastElement()).toString());
/* 421 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String moleculeToSMILES(IAtomContainer mol)
/*     */     throws Exception
/*     */   {
/* 427 */     StringWriter result = new StringWriter();
/* 428 */     SMILESWriter writer = new SMILESWriter(result);
/*     */     
/* 430 */     writer.write(mol);
/* 431 */     writer.close();
/*     */     
/* 433 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static void convertToCarbonSkelleton(IAtomContainer mol)
/*     */   {
/* 439 */     for (int i = 0; i < mol.getAtomCount(); i++)
/*     */     {
/* 441 */       IAtom at = mol.getAtom(i);
/* 442 */       at.setSymbol("C");
/* 443 */       at.setFormalCharge(Integer.valueOf(0));
/* 444 */       at.setMassNumber(Integer.valueOf(0));
/*     */     }
/*     */     
/* 447 */     for (int i = 0; i < mol.getBondCount(); i++)
/*     */     {
/* 449 */       IBond bo = mol.getBond(i);
/* 450 */       bo.setOrder(IBond.Order.SINGLE);
/*     */     }
/*     */   }
/*     */   
/*     */   public static IMolecule getMoleculeFromSmiles(String smi) throws Exception {
/* 455 */     IMolecule mol = null;
/* 456 */     SmilesParser sp = new SmilesParser(SilentChemObjectBuilder.getInstance());
/* 457 */     mol = sp.parseSmiles(smi);
/* 458 */     return mol;
/*     */   }
/*     */   
/*     */   public static IMolecule getMoleculeFromSmiles(String smi, boolean FlagExplicitHatoms) throws Exception {
/* 462 */     IMolecule mol = null;
/* 463 */     SmilesParser sp = new SmilesParser(SilentChemObjectBuilder.getInstance());
/* 464 */     mol = sp.parseSmiles(smi);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 470 */     AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
/* 471 */     CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(SilentChemObjectBuilder.getInstance());
/* 472 */     adder.addImplicitHydrogens(mol);
/* 473 */     if (FlagExplicitHatoms) {
/* 474 */       AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
/*     */     }
/* 476 */     return mol;
/*     */   }
/*     */   
/*     */   public static String[] getCarbonSkelletonsFromString(String smiles) throws Exception
/*     */   {
/* 481 */     IMolecule mol = getMoleculeFromSmiles(smiles);
/* 482 */     IMoleculeSet ms = ConnectivityChecker.partitionIntoMolecules(mol);
/* 483 */     int n = ms.getAtomContainerCount();
/* 484 */     String[] res = new String[n];
/* 485 */     for (int i = 0; i < n; i++)
/*     */     {
/* 487 */       IAtomContainer frag = ms.getAtomContainer(i);
/* 488 */       convertToCarbonSkelleton(frag);
/* 489 */       res[i] = moleculeToSMILES(frag);
/*     */     }
/* 491 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void printIntArray(int[] c)
/*     */   {
/* 497 */     if (c == null)
/*     */     {
/* 499 */       System.out.println("null");
/* 500 */       return;
/*     */     }
/* 502 */     for (int i = 0; i < c.length; i++)
/* 503 */       System.out.print(c[i] + " ");
/* 504 */     System.out.println();
/*     */   }
/*     */   
/*     */   public static String toString(int[] c)
/*     */   {
/* 509 */     StringBuffer sb = new StringBuffer();
/* 510 */     if (c == null) {
/* 511 */       sb.append("null");
/*     */     } else {
/* 513 */       for (int i = 0; i < c.length; i++)
/* 514 */         sb.append(" " + c[i]);
/*     */     }
/* 516 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String atomPropertiesToString(IAtom atom)
/*     */   {
/* 521 */     StringBuffer sb = new StringBuffer();
/* 522 */     if (atom.getProperties() == null) {
/* 523 */       return "";
/*     */     }
/* 525 */     Object[] keys = atom.getProperties().keySet().toArray();
/* 526 */     for (int i = 0; i < keys.length; i++)
/*     */     {
/* 528 */       if ((keys[i].toString().toString().equals("RingData")) || (keys[i].toString().toString().equals("RingData2"))) {
/* 529 */         sb.append(keys[i].toString() + " = " + toString((int[])atom.getProperties().get(keys[i])) + "\n");
/*     */       } else
/* 531 */         sb.append(keys[i].toString() + " = " + atom.getProperties().get(keys[i]) + "\n");
/*     */     }
/* 533 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Vector<Integer> getSmartsPositions(String smartsQuery, IAtomContainer target, boolean FlagSupportDoubleBondAromaticityNotSpecified)
/*     */     throws Exception
/*     */   {
/* 542 */     SmartsParser sp = new SmartsParser();
/* 543 */     sp.mSupportDoubleBondAromaticityNotSpecified = FlagSupportDoubleBondAromaticityNotSpecified;
/* 544 */     IsomorphismTester isoTester = new IsomorphismTester();
/*     */     
/* 546 */     QueryAtomContainer query = sp.parse(smartsQuery);
/* 547 */     sp.setNeededDataFlags();
/* 548 */     String errorMsg = sp.getErrorMessages();
/* 549 */     if (!errorMsg.equals(""))
/*     */     {
/* 551 */       System.out.println("Smarts Parser errors:\n" + errorMsg);
/* 552 */       return null;
/*     */     }
/*     */     
/* 555 */     isoTester.setQuery(query);
/* 556 */     sp.setSMARTSData(target);
/*     */     
/* 558 */     return isoTester.getIsomorphismPositions(target);
/*     */   }
/*     */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SmartsHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
