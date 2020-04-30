/*     */ package ambit2.smarts;
/*     */ 
/*     */ import ambit2.base.exceptions.AmbitException;
/*     */ import ambit2.core.processors.structure.AtomConfigurator;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ import org.openscience.cdk1.AtomContainer;
/*     */ import org.openscience.cdk1.AtomContainerSet;
/*     */ import org.openscience.cdk1.Bond;
/*     */ import org.openscience.cdk1.interfaces.IAtom;
/*     */ import org.openscience.cdk1.interfaces.IAtomContainer;
/*     */ import org.openscience.cdk1.interfaces.IAtomContainerSet;
/*     */ import org.openscience.cdk1.interfaces.IBond;
/*     */ import org.openscience.cdk1.interfaces.IBond.Order;
/*     */ import org.openscience.cdk1.interfaces.IChemObjectBuilder;
/*     */ import org.openscience.cdk1.isomorphism.matchers.QueryAtomContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SMIRKSManager
/*     */ {
/*  22 */   SmartsParser parser = new SmartsParser();
/*  23 */   IsomorphismTester isoTester = new IsomorphismTester();
/*     */   SmartsToChemObject stco;
/*  25 */   EquivalenceTester eqTester = new EquivalenceTester();
/*     */   
/*  27 */   Vector<String> parserErrors = new Vector();
/*     */   
/*  29 */   public int FlagSSMode = 1;
/*  30 */   public boolean FlagFilterEquivalentMappings = false;
/*     */   
/*     */ 
/*     */ 
/*     */   public SMIRKSManager(IChemObjectBuilder builder)
/*     */   {
/*  36 */     this.parser.setComponentLevelGrouping(true);
/*  37 */     this.parser.mSupportSmirksSyntax = true;
/*  38 */     this.stco = new SmartsToChemObject(builder);
/*     */   }
/*     */   
/*     */   public void setSSMode(int mode)
/*     */   {
/*  43 */     this.FlagSSMode = mode;
/*     */   }
/*     */   
/*     */   public boolean hasErrors()
/*     */   {
/*  48 */     if (this.parserErrors.isEmpty()) {
/*  49 */       return false;
/*     */     }
/*  51 */     return true;
/*     */   }
/*     */   
/*     */   public String getErrors()
/*     */   {
/*  56 */     StringBuffer sb = new StringBuffer();
/*  57 */     for (int i = 0; i < this.parserErrors.size(); i++)
/*  58 */       sb.append((String)this.parserErrors.get(i) + "\n");
/*  59 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public SMIRKSReaction parse(String smirks)
/*     */   {
/*  64 */     this.parserErrors.clear();
/*  65 */     SMIRKSReaction reaction = new SMIRKSReaction(this.stco.getBuilder());
/*     */     
/*     */ 
/*  68 */     int sep1Pos = smirks.indexOf(">");
/*  69 */     if (sep1Pos == -1)
/*     */     {
/*  71 */       this.parserErrors.add("Invalid SMIRKS: missing separators '>'");
/*  72 */       return reaction;
/*     */     }
/*     */     
/*     */ 
/*  76 */     int sep2Pos = smirks.indexOf(">", sep1Pos + 1);
/*  77 */     if (sep2Pos == -1)
/*     */     {
/*  79 */       this.parserErrors.add("Invalid SMIRKS: missing second separator '>'");
/*  80 */       return reaction;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  85 */     int res = 0;
/*     */     
/*  87 */     reaction.reactantsSmarts = smirks.substring(0, sep1Pos).trim();
/*  88 */     QueryAtomContainer fragment = parseComponent(reaction.reactantsSmarts, "Reactants", reaction.reactantFlags, reaction.reactants, reaction.reactantCLG);
/*     */     
/*  90 */     if (fragment == null) {
/*  91 */       res++;
/*     */     } else {
/*  93 */       reaction.reactant = fragment;
/*     */     }
/*     */     
/*  96 */     reaction.agentsSmarts = smirks.substring(sep1Pos + 1, sep2Pos).trim();
/*  97 */     if (!reaction.agentsSmarts.equals(""))
/*     */     {
/*  99 */       fragment = parseComponent(reaction.agentsSmarts, "Agents", reaction.agentFlags, reaction.agents, reaction.agentsCLG);
/*     */       
/* 101 */       if (fragment == null) {
/* 102 */         res++;
/*     */       } else {
/* 104 */         reaction.agent = fragment;
/*     */       }
/*     */     }
/*     */     
/* 108 */     reaction.productsSmarts = smirks.substring(sep2Pos + 1).trim();
/* 109 */     fragment = parseComponent(reaction.productsSmarts, "Products", reaction.productFlags, reaction.products, reaction.productsCLG);
/*     */     
/* 111 */     if (fragment == null) {
/* 112 */       res++;
/*     */     } else {
/* 114 */       reaction.product = fragment;
/*     */     }
/*     */     
/* 117 */     if (res > 0) {
/* 118 */       return reaction;
/*     */     }
/*     */     
/* 121 */     reaction.checkMappings();
/* 122 */     if (reaction.mapErrors.size() > 0)
/*     */     {
/* 124 */       this.parserErrors.addAll(reaction.mapErrors);
/* 125 */       return reaction;
/*     */     }
/*     */     
/* 128 */     reaction.generateTransformationData();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */     return reaction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public QueryAtomContainer parseComponent(String smarts, String compType, SmartsFlags flags, Vector<QueryAtomContainer> fragments, Vector<Integer> CLG)
/*     */   {
/* 144 */     QueryAtomContainer fragment = this.parser.parse(smarts);
/* 145 */     this.parser.setNeededDataFlags();
/* 146 */     String errorMsg = this.parser.getErrorMessages();
/* 147 */     if (!errorMsg.equals(""))
/*     */     {
/* 149 */       this.parserErrors.add("Invalid " + compType + " part in SMIRKS: " + smarts + "   " + errorMsg);
/*     */       
/* 151 */       return null;
/*     */     }
/*     */     
/* 154 */     flags.hasRecursiveSmarts = this.parser.hasRecursiveSmarts;
/* 155 */     flags.mNeedExplicitHData = this.parser.needExplicitHData();
/* 156 */     flags.mNeedNeighbourData = this.parser.needNeighbourData();
/* 157 */     flags.mNeedParentMoleculeData = this.parser.needParentMoleculeData();
/* 158 */     flags.mNeedRingData = this.parser.needRingData();
/* 159 */     flags.mNeedRingData2 = this.parser.needRingData2();
/* 160 */     flags.mNeedValenceData = this.parser.needValencyData();
/*     */     
/* 162 */     for (int i = 0; i < this.parser.fragments.size(); i++) {
/* 163 */       fragments.add(this.parser.fragments.get(i));
/*     */     }
/* 165 */     for (int i = 0; i < this.parser.fragmentComponents.size(); i++) {
/* 166 */       CLG.add(this.parser.fragmentComponents.get(i));
/*     */     }
/* 168 */     return fragment;
/*     */   }
/*     */   
/*     */   public boolean applyTransformation(IAtomContainer target, SMIRKSReaction reaction) throws Exception {
/* 172 */     return applyTransformation(target, null, reaction);
/*     */   }
/*     */   
/*     */   public boolean applyTransformation(IAtomContainer target, IAcceptable selection, SMIRKSReaction reaction) throws Exception
/*     */   {
/* 177 */     this.isoTester.setQuery(reaction.reactant);
/* 178 */     SmartsParser.prepareTargetForSMARTSSearch(reaction.reactantFlags, target);
/*     */     
/*     */ 
/* 181 */     if (this.FlagSSMode == 0)
/*     */     {
/* 183 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 187 */     if (this.FlagSSMode == 2)
/*     */     {
/* 189 */       Vector<Vector<IAtom>> rMaps = getNonIdenticalMappings(target);
/* 190 */       if (rMaps.size() == 0) { return false;
/*     */       }
/* 192 */       if (this.FlagFilterEquivalentMappings)
/*     */       {
/* 194 */         this.eqTester.setTarget(target);
/* 195 */         this.eqTester.quickFindEquivalentTerminalHAtoms();
/* 196 */         rMaps = this.eqTester.filterEquivalentMappings(rMaps);
/*     */       }
/*     */       
/* 199 */       boolean applied = false;
/* 200 */       for (int i = 0; i < rMaps.size(); i++) {
/* 201 */         if ((selection == null) || ((selection != null) && (selection.accept((Vector)rMaps.get(i))))) {
/* 202 */           applyTransformAtLocation(target, (Vector)rMaps.get(i), reaction);
/* 203 */           applied = true;
/*     */         }
/*     */       }
/*     */       
/* 207 */       AtomConfigurator cfg = new AtomConfigurator();
/*     */       try {
/* 209 */         cfg.process(target);
/*     */       }
/*     */       catch (AmbitException e) {
/* 212 */         throw e;
/*     */       }
/*     */       
/* 215 */       return applied;
/*     */     }
/*     */     
/* 218 */     if (this.FlagSSMode == 21)
/*     */     {
/* 220 */       Vector<Vector<IAtom>> rMaps = getNonIdenticalMappings(target);
/* 221 */       if (rMaps.size() == 0) { return false;
/*     */       }
/*     */       
/*     */ 
/* 225 */       boolean applied = false;
/* 226 */       for (int i = 0; i < rMaps.size(); i++) {
/* 227 */         if ((selection == null) || ((selection != null) && (selection.accept((Vector)rMaps.get(i))))) {
/* 228 */           applyTransformAtLocation(target, (Vector)rMaps.get(i), reaction);
/* 229 */           applied = true;
/*     */           
/*     */ 
/* 232 */           AtomConfigurator cfg = new AtomConfigurator();
/*     */           try {
/* 234 */             cfg.process(target);
/*     */           }
/*     */           catch (AmbitException e) {
/* 237 */             throw e;
/*     */           }
/*     */           
/* 240 */           return applied;
/*     */         }
/*     */       }
/* 243 */       return applied;
/*     */     }
/*     */     
/*     */ 
/* 247 */     if (this.FlagSSMode == 1)
/*     */     {
/* 249 */       Vector<Vector<IAtom>> rMaps = getNonOverlappingMappings(target);
/* 250 */       if (rMaps.size() == 0) { return false;
/*     */       }
/*     */       
/*     */ 
/* 254 */       boolean applied = false;
/* 255 */       for (int i = 0; i < rMaps.size(); i++) {
/* 256 */         if ((selection == null) || ((selection != null) && (selection.accept((Vector)rMaps.get(i))))) {
/* 257 */           applyTransformAtLocation(target, (Vector)rMaps.get(i), reaction);
/* 258 */           applied = true;
/*     */         }
/*     */       }
/*     */       
/* 262 */       AtomConfigurator cfg = new AtomConfigurator();
/* 263 */       cfg.process(target);
/*     */       
/* 265 */       return applied;
/*     */     }
/*     */     
/* 268 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IAtomContainerSet applyTransformationWithCombinedOverlappedPos(IAtomContainer target, IAcceptable selection, SMIRKSReaction reaction)
/*     */     throws Exception
/*     */   {
/* 277 */     this.isoTester.setQuery(reaction.reactant);
/* 278 */     SmartsParser.prepareTargetForSMARTSSearch(reaction.reactantFlags, target);
/*     */     
/*     */ 
/* 281 */     Vector<Vector<IAtom>> rMaps0 = getNonIdenticalMappings(target);
/* 282 */     if (rMaps0.size() == 0) {
/* 283 */       return null;
/*     */     }
/*     */     
/*     */     Vector<Vector<IAtom>> rMaps;
/* 288 */     if (selection == null) {
/* 289 */       rMaps = rMaps0;
/*     */     }
/*     */     else {
/* 292 */       rMaps = new Vector();
/* 293 */       for (int i = 0; i < rMaps0.size(); i++)
/*     */       {
/* 295 */         if (selection.accept((Vector)rMaps0.get(i))) {
/* 296 */           rMaps.add(rMaps0.get(i));
/*     */         }
/*     */       }
/*     */     }
/* 300 */     if (rMaps.size() == 0) {
/* 301 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 308 */     if (this.FlagFilterEquivalentMappings)
/*     */     {
/* 310 */       this.eqTester.setTarget(target);
/* 311 */       this.eqTester.quickFindEquivalentTerminalHAtoms();
/* 312 */       rMaps = this.eqTester.filterEquivalentMappings(rMaps);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 320 */     IAtomContainerSet resSet = new AtomContainerSet();
/*     */     
/* 322 */     if (rMaps.size() == 1)
/*     */     {
/* 324 */       IAtomContainer product = applyTransformationsAtLocationsWithCloning(target, rMaps, reaction);
/* 325 */       resSet.addAtomContainer(product);
/* 326 */       return resSet;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 331 */     Vector<Vector<Integer>> clusterIndexes = this.isoTester.getOverlappedMappingClusters(rMaps);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 338 */     int[] comb = new int[clusterIndexes.size()];
/* 339 */     for (int i = 0; i < comb.length; i++) {
/* 340 */       comb[i] = 0;
/*     */     }
/* 342 */     int digit = 0;
/*     */     
/*     */     do
/*     */     {
/* 346 */       Vector<Vector<IAtom>> combMaps = new Vector();
/* 347 */       for (int i = 0; i < comb.length; i++)
/*     */       {
/* 349 */         int index = ((Integer)((Vector)clusterIndexes.get(i)).get(comb[i])).intValue();
/* 350 */         combMaps.add(rMaps.get(index));
/*     */       }
/*     */       
/*     */ 
/* 354 */       IAtomContainer product = applyTransformationsAtLocationsWithCloning(target, combMaps, reaction);
/* 355 */       resSet.addAtomContainer(product);
/*     */       
/*     */ 
/*     */ 
/* 359 */       digit = 0;
/* 360 */       while (digit < comb.length)
/*     */       {
/* 362 */         comb[digit] += 1;
/* 363 */         if (comb[digit] != ((Vector)clusterIndexes.get(digit)).size())
/*     */           break;
/* 365 */         comb[digit] = 0;
/* 366 */         digit++;
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 373 */     while (digit < comb.length);
/*     */     
/*     */ 
/* 376 */     return resSet;
/*     */   }
/*     */   
/*     */   public IAtomContainerSet applyTransformationWithSingleCopyForEachPos(IAtomContainer target, IAcceptable selection, SMIRKSReaction reaction)
/*     */     throws Exception
/*     */   {
/* 382 */     this.isoTester.setQuery(reaction.reactant);
/* 383 */     SmartsParser.prepareTargetForSMARTSSearch(reaction.reactantFlags, target);
/*     */     
/*     */ 
/* 386 */     Vector<Vector<IAtom>> rMaps0 = getNonIdenticalMappings(target);
/* 387 */     if (rMaps0.size() == 0) {
/* 388 */       return null;
/*     */     }
/*     */     
/*     */     Vector<Vector<IAtom>> rMaps;
/* 393 */     if (selection == null) {
/* 394 */       rMaps = rMaps0;
/*     */     }
/*     */     else {
/* 397 */       rMaps = new Vector();
/* 398 */       for (int i = 0; i < rMaps0.size(); i++)
/*     */       {
/* 400 */         if (selection.accept((Vector)rMaps0.get(i))) {
/* 401 */           rMaps.add(rMaps0.get(i));
/*     */         }
/*     */       }
/*     */     }
/* 405 */     if (rMaps.size() == 0) {
/* 406 */       return null;
/*     */     }
/* 408 */     if (this.FlagFilterEquivalentMappings)
/*     */     {
/* 410 */       this.eqTester.setTarget(target);
/* 411 */       this.eqTester.quickFindEquivalentTerminalHAtoms();
/* 412 */       rMaps = this.eqTester.filterEquivalentMappings(rMaps);
/*     */     }
/*     */     
/* 415 */     IAtomContainerSet resSet = new AtomContainerSet();
/*     */     
/* 417 */     for (int i = 0; i < rMaps.size(); i++)
/*     */     {
/* 419 */       Vector<Vector<IAtom>> vMaps = new Vector();
/* 420 */       vMaps.add(rMaps.get(i));
/* 421 */       IAtomContainer product = applyTransformationsAtLocationsWithCloning(target, vMaps, reaction);
/* 422 */       resSet.addAtomContainer(product);
/*     */     }
/*     */     
/* 425 */     return resSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vector<Vector<IAtom>> getNonOverlappingMappings(IAtomContainer target)
/*     */   {
/* 434 */     Vector<Vector<IAtom>> rMaps = this.isoTester.getNonOverlappingMappings(target);
/* 435 */     return rMaps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vector<Vector<IAtom>> getNonIdenticalMappings(IAtomContainer target)
/*     */   {
/* 443 */     Vector<Vector<IAtom>> rMaps = this.isoTester.getNonIdenticalMappings(target);
/* 444 */     return rMaps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void applyTransformAtLocation(IAtomContainer target, Vector<IAtom> rMap, SMIRKSReaction reaction)
/*     */   {
/* 454 */     Vector<IAtom> newAtoms = new Vector();
/* 455 */     for (int i = 0; i < reaction.productNotMappedAt.size(); i++)
/*     */     {
/* 457 */       int pAtNum = ((Integer)reaction.productNotMappedAt.get(i)).intValue();
/* 458 */       IAtom a = reaction.product.getAtom(pAtNum);
/* 459 */       IAtom a0 = this.stco.toAtom(a);
/* 460 */       newAtoms.add(a0);
/* 461 */       target.addAtom(a0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 467 */     for (int i = 0; i < reaction.reactant.getAtomCount(); i++)
/*     */     {
/* 469 */       IAtom rAt = reaction.reactant.getAtom(i);
/* 470 */       Integer raMapInd = (Integer)rAt.getProperty("SmirksMapIndex");
/* 471 */       if (raMapInd != null)
/*     */       {
/* 473 */         int pAtNum = reaction.getMappedProductAtom(raMapInd);
/* 474 */         Integer charge = (Integer)reaction.productAtCharge.get(pAtNum);
/* 475 */         IAtom tAt = (IAtom)rMap.get(i);
/* 476 */         tAt.setFormalCharge(charge);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 481 */         IAtom tAt = (IAtom)rMap.get(i);
/* 482 */         target.removeAtomAndConnectedElectronContainers(tAt);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 491 */     for (int i = 0; i < reaction.reactBo.size(); i++)
/*     */     {
/* 493 */       int nrAt1 = ((Integer)reaction.reactAt1.get(i)).intValue();
/* 494 */       int nrAt2 = ((Integer)reaction.reactAt2.get(i)).intValue();
/*     */       
/* 496 */       if ((nrAt1 >= 0) && (nrAt2 >= 0))
/*     */       {
/* 498 */         if (reaction.reactBo.get(i) == null)
/*     */         {
/*     */ 
/*     */ 
/* 502 */           IAtom tAt1 = (IAtom)rMap.get(nrAt1);
/* 503 */           IAtom tAt2 = (IAtom)rMap.get(nrAt2);
/* 504 */           IBond tb = new Bond();
/* 505 */           tb.setAtoms(new IAtom[] { tAt1, tAt2 });
/* 506 */           tb.setOrder((IBond.Order)reaction.prodBo.get(i));
/* 507 */           target.addBond(tb);
/*     */         }
/*     */         else
/*     */         {
/* 511 */           IAtom tAt1 = (IAtom)rMap.get(nrAt1);
/* 512 */           IAtom tAt2 = (IAtom)rMap.get(nrAt2);
/* 513 */           IBond tBo = target.getBond(tAt1, tAt2);
/* 514 */           if (reaction.prodBo.get(i) == null) {
/* 515 */             target.removeBond(tBo);
/*     */           } else {
/* 517 */             tBo.setOrder((IBond.Order)reaction.prodBo.get(i));
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 522 */       else if ((nrAt1 == -100000) || (nrAt2 == -100000))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 527 */         IAtom tAt1 = null;
/* 528 */         IAtom tAt2 = null;
/*     */         
/* 530 */         if (nrAt1 == -100000)
/*     */         {
/* 532 */           int pAt1tNotMapIndex = -1;
/* 533 */           int npAt1 = ((Integer)reaction.prodAt1.get(i)).intValue();
/* 534 */           for (int k = 0; k < reaction.productNotMappedAt.size(); k++) {
/* 535 */             if (((Integer)reaction.productNotMappedAt.get(k)).intValue() == npAt1)
/*     */             {
/* 537 */               pAt1tNotMapIndex = k;
/* 538 */               break;
/*     */             }
/*     */           }
/* 541 */           tAt1 = (IAtom)newAtoms.get(pAt1tNotMapIndex);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 546 */           tAt1 = (IAtom)rMap.get(nrAt1);
/*     */         }
/*     */         
/*     */ 
/* 550 */         if (nrAt2 == -100000)
/*     */         {
/* 552 */           int pAt2tNotMapIndex = -1;
/* 553 */           int npAt2 = ((Integer)reaction.prodAt2.get(i)).intValue();
/* 554 */           for (int k = 0; k < reaction.productNotMappedAt.size(); k++) {
/* 555 */             if (((Integer)reaction.productNotMappedAt.get(k)).intValue() == npAt2)
/*     */             {
/* 557 */               pAt2tNotMapIndex = k;
/* 558 */               break;
/*     */             }
/*     */           }
/* 561 */           tAt2 = (IAtom)newAtoms.get(pAt2tNotMapIndex);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 566 */           tAt2 = (IAtom)rMap.get(nrAt2);
/*     */         }
/*     */         
/* 569 */         IBond tb = new Bond();
/* 570 */         tb.setAtoms(new IAtom[] { tAt1, tAt2 });
/* 571 */         tb.setOrder((IBond.Order)reaction.prodBo.get(i));
/* 572 */         target.addBond(tb);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IAtomContainer applyTransformationsAtLocationsWithCloning(IAtomContainer target, Vector<Vector<IAtom>> rMaps, SMIRKSReaction reaction)
/*     */     throws Exception
/*     */   {
/* 588 */     IAtomContainer clone = getCloneStructure(target);
/*     */     
/*     */ 
/* 591 */     Vector<Vector<IAtom>> cloneMaps = new Vector();
/* 592 */     for (int i = 0; i < rMaps.size(); i++) {
/* 593 */       cloneMaps.add(getCloneMapping(target, clone, (Vector)rMaps.get(i)));
/*     */     }
/*     */     
/* 596 */     for (int i = 0; i < cloneMaps.size(); i++) {
/* 597 */       applyTransformAtLocation(clone, (Vector)cloneMaps.get(i), reaction);
/*     */     }
/* 599 */     AtomConfigurator cfg = new AtomConfigurator();
/* 600 */     cfg.process(clone);
/*     */     
/* 602 */     return clone;
/*     */   }
/*     */   
/*     */   IAtomContainer getCloneStructure(IAtomContainer target) throws Exception
/*     */   {
/* 607 */     IAtomContainer mol = new AtomContainer();
/*     */     
/* 609 */     IAtom[] newAtoms = new IAtom[target.getAtomCount()];
/* 610 */     IBond[] newBonds = new IBond[target.getBondCount()];
/*     */     
/*     */ 
/* 613 */     for (int i = 0; i < target.getAtomCount(); i++)
/*     */     {
/* 615 */       IAtom a = target.getAtom(i);
/* 616 */       IAtom a1 = cloneAtom(a);
/* 617 */       mol.addAtom(a1);
/* 618 */       newAtoms[i] = a1;
/*     */     }
/*     */     
/*     */ 
/* 622 */     for (int i = 0; i < target.getBondCount(); i++)
/*     */     {
/* 624 */       IBond b = target.getBond(i);
/* 625 */       IBond b1 = new Bond();
/* 626 */       IAtom[] a01 = new IAtom[2];
/* 627 */       int ind0 = target.getAtomNumber(b.getAtom(0));
/* 628 */       int ind1 = target.getAtomNumber(b.getAtom(1));
/* 629 */       a01[0] = mol.getAtom(ind0);
/* 630 */       a01[1] = mol.getAtom(ind1);
/* 631 */       b1.setAtoms(a01);
/* 632 */       b1.setOrder(b.getOrder());
/* 633 */       boolean FlagArom = b.getFlag(5);
/* 634 */       b1.setFlag(5, FlagArom);
/* 635 */       mol.addBond(b1);
/* 636 */       newBonds[i] = b1;
/*     */     }
/*     */     
/* 639 */     return mol;
/*     */   }
/*     */   
/*     */   IAtom cloneAtom(IAtom a) throws Exception
/*     */   {
/* 644 */     IAtom a1 = (IAtom)a.clone();
/* 645 */     return a1;
/*     */   }
/*     */   
/*     */ 
/*     */   Vector<IAtom> getCloneMapping(IAtomContainer target, IAtomContainer clone, Vector<IAtom> map)
/*     */   {
/* 651 */     Vector<IAtom> cloneMap = new Vector();
/* 652 */     for (int i = 0; i < map.size(); i++)
/*     */     {
/* 654 */       IAtom at = (IAtom)map.get(i);
/* 655 */       int targetIndex = target.getAtomNumber(at);
/* 656 */       cloneMap.add(clone.getAtom(targetIndex));
/*     */     }
/*     */     
/* 659 */     return cloneMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void printSSMap(IAtomContainer target, Vector<IAtom> rMap)
/*     */   {
/* 667 */     System.out.print("Map: ");
/* 668 */     for (int i = 0; i < rMap.size(); i++)
/*     */     {
/* 670 */       IAtom tAt = (IAtom)rMap.get(i);
/* 671 */       System.out.print(" " + target.getAtomNumber(tAt));
/*     */     }
/* 673 */     System.out.println();
/*     */   }
/*     */   
/*     */   public void printMappingClusters(Vector<Vector<Integer>> clusterIndexes, IAtomContainer target)
/*     */   {
/* 678 */     for (int i = 0; i < clusterIndexes.size(); i++)
/*     */     {
/* 680 */       System.out.print("Cluster #" + i + " : ");
/* 681 */       Vector<Integer> v = (Vector)clusterIndexes.get(i);
/* 682 */       for (int k = 0; k < v.size(); k++)
/* 683 */         System.out.print(v.get(k) + " ");
/* 684 */       System.out.println();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SMIRKSManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
