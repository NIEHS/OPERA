/*     */ package ambit2.smarts;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import org.openscience.cdk1.interfaces.IAtom;
/*     */ import org.openscience.cdk1.interfaces.IBond;
/*     */ import org.openscience.cdk1.interfaces.IBond.Order;
/*     */ import org.openscience.cdk1.interfaces.IChemObjectBuilder;
/*     */ import org.openscience.cdk1.isomorphism.matchers.QueryAtomContainer;
/*     */ 
/*     */ public class SMIRKSReaction
/*     */ {
/*     */   public String reactantsSmarts;
/*     */   public String agentsSmarts;
/*     */   public String productsSmarts;
/*  15 */   public SmartsFlags reactantFlags = new SmartsFlags();
/*  16 */   public SmartsFlags agentFlags = new SmartsFlags();
/*  17 */   public SmartsFlags productFlags = new SmartsFlags();
/*     */   
/*     */ 
/*  20 */   QueryAtomContainer reactant = new QueryAtomContainer();
/*  21 */   QueryAtomContainer agent = new QueryAtomContainer();
/*  22 */   QueryAtomContainer product = new QueryAtomContainer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  27 */   public Vector<QueryAtomContainer> reactants = new Vector();
/*  28 */   public Vector<QueryAtomContainer> agents = new Vector();
/*  29 */   public Vector<QueryAtomContainer> products = new Vector();
/*     */   
/*  31 */   public Vector<Integer> reactantCLG = new Vector();
/*  32 */   public Vector<Integer> agentsCLG = new Vector();
/*  33 */   public Vector<Integer> productsCLG = new Vector();
/*     */   
/*     */ 
/*     */ 
/*  37 */   public Vector<String> mapErrors = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  45 */   Vector<Integer> reactantMapIndex = new Vector();
/*  46 */   Vector<Integer> reactantFragAtomNum = new Vector();
/*  47 */   Vector<Integer> reactantAtomNum = new Vector();
/*  48 */   Vector<Integer> reactantFragmentNum = new Vector();
/*  49 */   Vector<Integer> reactantNotMappedAt = new Vector();
/*     */   
/*  51 */   Vector<Integer> productMapIndex = new Vector();
/*  52 */   Vector<Integer> productFragAtomNum = new Vector();
/*  53 */   Vector<Integer> productAtomNum = new Vector();
/*  54 */   Vector<Integer> productFragmentNum = new Vector();
/*  55 */   Vector<Integer> productNotMappedAt = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  60 */   Vector<Integer> reactantAtCharge = new Vector();
/*  61 */   Vector<Integer> productAtCharge = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  66 */   Vector<Integer> reactAt1 = new Vector();
/*  67 */   Vector<Integer> reactAt2 = new Vector();
/*  68 */   Vector<IBond.Order> reactBo = new Vector();
/*  69 */   Vector<Integer> prodAt1 = new Vector();
/*  70 */   Vector<Integer> prodAt2 = new Vector();
/*  71 */   Vector<IBond.Order> prodBo = new Vector();
/*     */   
/*     */   protected IChemObjectBuilder builder;
/*     */   
/*     */   public SMIRKSReaction(IChemObjectBuilder builder)
/*     */   {
/*  77 */     this.builder = builder;
/*     */   }
/*     */   
/*     */ 
/*     */   public void checkMappings()
/*     */   {
/*  83 */     for (int i = 0; i < this.reactants.size(); i++) {
/*  84 */       registerMappings("Reactant", this.reactant, (QueryAtomContainer)this.reactants.get(i), i, this.reactantMapIndex, this.reactantNotMappedAt, this.reactantFragAtomNum, this.reactantAtomNum, this.reactantFragmentNum);
/*     */     }
/*     */     
/*  87 */     for (int i = 0; i < this.products.size(); i++) {
/*  88 */       registerMappings("Product", this.product, (QueryAtomContainer)this.products.get(i), i, this.productMapIndex, this.productNotMappedAt, this.productFragAtomNum, this.productAtomNum, this.productFragmentNum);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  93 */     for (int i = 0; i < this.reactantMapIndex.size(); i++)
/*     */     {
/*  95 */       int index = getIntegerObjectIndex(this.productMapIndex, (Integer)this.reactantMapIndex.get(i));
/*  96 */       if (index == -1) {
/*  97 */         this.mapErrors.add("Reactant Map Index " + ((Integer)this.reactantMapIndex.get(i)).intValue() + " is not valid product map index!");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */     for (int i = 0; i < this.productMapIndex.size(); i++)
/*     */     {
/* 108 */       int index = getIntegerObjectIndex(this.reactantMapIndex, (Integer)this.productMapIndex.get(i));
/* 109 */       if (index == -1) {
/* 110 */         this.mapErrors.add("Product Map Index " + ((Integer)this.productMapIndex.get(i)).intValue() + " is not valid reactant map index!");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 115 */     SmartsToChemObject stco = new SmartsToChemObject(this.builder);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */     for (int i = 0; i < this.reactantFragAtomNum.size(); i++)
/*     */     {
/* 125 */       int rAtNum = ((Integer)this.reactantFragAtomNum.get(i)).intValue();
/* 126 */       int rNum = ((Integer)this.reactantFragmentNum.get(i)).intValue();
/* 127 */       Integer rMapInd = (Integer)this.reactantMapIndex.get(i);
/* 128 */       IAtom ra = ((QueryAtomContainer)this.reactants.get(rNum)).getAtom(rAtNum);
/* 129 */       int rGlobAtNum = ((Integer)this.reactantAtomNum.get(i)).intValue();
/* 130 */       IAtom glob_ra = this.reactant.getAtom(rGlobAtNum);
/*     */       
/* 132 */       int pIndex = getIntegerObjectIndex(this.productMapIndex, rMapInd);
/* 133 */       int pAtNum = ((Integer)this.productFragAtomNum.get(pIndex)).intValue();
/* 134 */       int pNum = ((Integer)this.productFragmentNum.get(pIndex)).intValue();
/* 135 */       IAtom pa = ((QueryAtomContainer)this.products.get(pNum)).getAtom(pAtNum);
/* 136 */       int pGlobAtNum = ((Integer)this.productAtomNum.get(pIndex)).intValue();
/* 137 */       IAtom glob_pa = this.product.getAtom(pGlobAtNum);
/*     */       
/* 139 */       if (glob_ra != ra) {
/* 140 */         this.mapErrors.add("Critical Error: Inconsistency between global and gragment atom treatment.");
/*     */       }
/* 142 */       if (glob_pa != pa) {
/* 143 */         this.mapErrors.add("Critical Error: Inconsistency between global and gragment atom treatment.");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 148 */       IAtom ra1 = stco.toAtom(ra);
/* 149 */       IAtom pa1 = stco.toAtom(pa);
/* 150 */       if (ra1 != null)
/*     */       {
/* 152 */         if (pa1 == null) {
/* 153 */           this.mapErrors.add("Map " + rMapInd.intValue() + " atom types are inconsistent!");
/*     */ 
/*     */         }
/* 156 */         else if (!ra1.getSymbol().equals(pa1.getSymbol())) {
/* 157 */           this.mapErrors.add("Map " + rMapInd.intValue() + " atom types are inconsistent!");
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 162 */       else if (pa1 != null) {
/* 163 */         this.mapErrors.add("Map " + rMapInd.intValue() + " atom types are inconsistent!");
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
/*     */   void registerMappings(String compType, QueryAtomContainer globalContainer, QueryAtomContainer fragment, int curFragNum, Vector<Integer> mapIndex, Vector<Integer> notMappedAt, Vector<Integer> atFragNum, Vector<Integer> atGlobalNum, Vector<Integer> fragNum)
/*     */   {
/* 177 */     for (int i = 0; i < fragment.getAtomCount(); i++)
/*     */     {
/* 179 */       IAtom a = fragment.getAtom(i);
/* 180 */       Integer iObj = (Integer)a.getProperty("SmirksMapIndex");
/* 181 */       if (iObj != null)
/*     */       {
/* 183 */         if (containsInteger(mapIndex, iObj)) {
/* 184 */           this.mapErrors.add(compType + " Map Index " + iObj.intValue() + " is repeated!");
/*     */         }
/*     */         else {
/* 187 */           mapIndex.add(iObj);
/* 188 */           atFragNum.add(new Integer(i));
/* 189 */           fragNum.add(new Integer(curFragNum));
/* 190 */           int globalAtNum = globalContainer.getAtomNumber(a);
/* 191 */           atGlobalNum.add(new Integer(globalAtNum));
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 198 */         int globalAtNum = globalContainer.getAtomNumber(a);
/* 199 */         notMappedAt.add(new Integer(globalAtNum));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void generateTransformationData()
/*     */   {
/* 207 */     SmartsToChemObject stco = new SmartsToChemObject(this.builder);
/*     */     
/*     */ 
/* 210 */     generateChargeTransformation();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */     for (int i = 0; i < this.reactant.getBondCount(); i++)
/*     */     {
/* 218 */       IBond rb = this.reactant.getBond(i);
/* 219 */       IBond rb0 = stco.toBond(rb);
/*     */       
/* 221 */       IAtom ra1 = rb.getAtom(0);
/* 222 */       IAtom ra2 = rb.getAtom(1);
/* 223 */       Integer raMapInd1 = (Integer)ra1.getProperty("SmirksMapIndex");
/* 224 */       Integer raMapInd2 = (Integer)ra2.getProperty("SmirksMapIndex");
/*     */       
/* 226 */       if (raMapInd1 == null)
/*     */       {
/* 228 */         int rAt1Num = -this.reactant.getAtomNumber(ra1);
/* 229 */         if (raMapInd2 != null) {}
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
/*     */       }
/* 241 */       else if (raMapInd2 != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 247 */         int pAt1Num = getMappedProductAtom(raMapInd1);
/* 248 */         int pAt2Num = getMappedProductAtom(raMapInd2);
/* 249 */         int rAt1Num = this.reactant.getAtomNumber(ra1);
/* 250 */         int rAt2Num = this.reactant.getAtomNumber(ra2);
/* 251 */         int pbNum = this.product.getBondNumber(this.product.getAtom(pAt1Num), this.product.getAtom(pAt2Num));
/*     */         
/*     */ 
/* 254 */         boolean FlagRegisterTransform = false;
/* 255 */         if (pbNum == -1)
/*     */         {
/* 257 */           this.prodBo.add(null);
/* 258 */           FlagRegisterTransform = true;
/*     */         }
/*     */         else
/*     */         {
/* 262 */           IBond pb = this.product.getBond(pbNum);
/* 263 */           IBond pb0 = stco.toBond(pb);
/*     */           
/* 265 */           if (rb0 == null ? 
/*     */           
/* 267 */             pb0 == null : 
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
/* 278 */             pb0 == null) {}
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
/* 290 */           if (rb0.getOrder() != pb0.getOrder())
/*     */           {
/* 292 */             FlagRegisterTransform = true;
/* 293 */             this.prodBo.add(pb0.getOrder());
/*     */           }
/*     */         }
/*     */         
/* 297 */         if (FlagRegisterTransform)
/*     */         {
/* 299 */           this.prodAt1.add(new Integer(pAt1Num));
/* 300 */           this.prodAt2.add(new Integer(pAt2Num));
/* 301 */           this.reactBo.add(rb0.getOrder());
/* 302 */           this.reactAt1.add(new Integer(rAt1Num));
/* 303 */           this.reactAt2.add(new Integer(rAt2Num));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 312 */     for (int i = 0; i < this.product.getBondCount(); i++)
/*     */     {
/* 314 */       IBond pb = this.product.getBond(i);
/* 315 */       IBond pb0 = stco.toBond(pb);
/* 316 */       if (pb0 == null) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 321 */       IAtom pa1 = pb.getAtom(0);
/* 322 */       IAtom pa2 = pb.getAtom(1);
/* 323 */       int pAt1Num = this.product.getAtomNumber(pa1);
/* 324 */       int pAt2Num = this.product.getAtomNumber(pa2);
/* 325 */       Integer paMapInd1 = (Integer)pa1.getProperty("SmirksMapIndex");
/* 326 */       Integer paMapInd2 = (Integer)pa2.getProperty("SmirksMapIndex");
/*     */       
/* 328 */       if (paMapInd1 == null)
/*     */       {
/* 330 */         if (paMapInd2 == null)
/*     */         {
/*     */ 
/* 333 */           this.prodBo.add(pb0.getOrder());
/* 334 */           this.prodAt1.add(new Integer(pAt1Num));
/* 335 */           this.prodAt2.add(new Integer(pAt2Num));
/* 336 */           this.reactBo.add(null);
/* 337 */           this.reactAt1.add(new Integer(-100000));
/* 338 */           this.reactAt2.add(new Integer(-100000));
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 345 */           this.prodBo.add(pb0.getOrder());
/* 346 */           this.prodAt1.add(new Integer(pAt1Num));
/* 347 */           this.prodAt2.add(new Integer(pAt2Num));
/* 348 */           this.reactBo.add(null);
/* 349 */           this.reactAt1.add(new Integer(-100000));
/* 350 */           int rAt2Num = getMappedReactantAtom(paMapInd2);
/* 351 */           this.reactAt2.add(new Integer(rAt2Num));
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 356 */       else if (paMapInd2 == null)
/*     */       {
/*     */ 
/*     */ 
/* 360 */         this.prodBo.add(pb0.getOrder());
/* 361 */         this.prodAt1.add(new Integer(pAt1Num));
/* 362 */         this.prodAt2.add(new Integer(pAt2Num));
/* 363 */         this.reactBo.add(null);
/* 364 */         int rAt1Num = getMappedReactantAtom(paMapInd1);
/* 365 */         this.reactAt1.add(new Integer(rAt1Num));
/* 366 */         this.reactAt2.add(new Integer(-100000));
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 371 */         int rAt1Num = getMappedReactantAtom(paMapInd1);
/* 372 */         int rAt2Num = getMappedReactantAtom(paMapInd2);
/* 373 */         int rbNum = this.reactant.getBondNumber(this.reactant.getAtom(rAt1Num), this.reactant.getAtom(rAt2Num));
/* 374 */         if (rbNum == -1)
/*     */         {
/* 376 */           this.prodBo.add(pb0.getOrder());
/* 377 */           this.prodAt1.add(new Integer(pAt1Num));
/* 378 */           this.prodAt2.add(new Integer(pAt2Num));
/* 379 */           this.reactBo.add(null);
/* 380 */           this.reactAt1.add(new Integer(rAt1Num));
/* 381 */           this.reactAt2.add(new Integer(rAt2Num));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void generateChargeTransformation()
/*     */   {
/* 394 */     SmartsToChemObject stco = new SmartsToChemObject(this.builder);
/*     */     
/* 396 */     for (int i = 0; i < this.reactant.getAtomCount(); i++)
/*     */     {
/* 398 */       IAtom a = stco.toAtom(this.reactant.getAtom(i));
/* 399 */       if (a == null) {
/* 400 */         this.reactantAtCharge.add(null);
/*     */       } else {
/* 402 */         this.reactantAtCharge.add(a.getFormalCharge());
/*     */       }
/*     */     }
/*     */     
/* 406 */     for (int i = 0; i < this.product.getAtomCount(); i++)
/*     */     {
/* 408 */       IAtom a = stco.toAtom(this.product.getAtom(i));
/* 409 */       if (a == null) {
/* 410 */         this.productAtCharge.add(null);
/*     */       } else {
/* 412 */         this.productAtCharge.add(a.getFormalCharge());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int getMappedProductAtom(Integer raMapInd)
/*     */   {
/* 421 */     int pIndex = getIntegerObjectIndex(this.productMapIndex, raMapInd);
/* 422 */     int pGlobAtNum = ((Integer)this.productAtomNum.get(pIndex)).intValue();
/* 423 */     return pGlobAtNum;
/*     */   }
/*     */   
/*     */ 
/*     */   int getMappedReactantAtom(Integer paMapInd)
/*     */   {
/* 429 */     int rIndex = getIntegerObjectIndex(this.reactantMapIndex, paMapInd);
/* 430 */     int rGlobAtNum = ((Integer)this.reactantAtomNum.get(rIndex)).intValue();
/* 431 */     return rGlobAtNum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int getTransformationReactantBondIndex(int nBondsToCheck, int rAt1, int rAt2)
/*     */   {
/* 438 */     for (int i = 0; i < nBondsToCheck; i++)
/*     */     {
/* 440 */       if ((((Integer)this.reactAt1.get(i)).intValue() == rAt1) && (((Integer)this.reactAt2.get(i)).intValue() == rAt2)) {
/* 441 */         return i;
/*     */       }
/* 443 */       if ((((Integer)this.reactAt1.get(i)).intValue() == rAt2) && (((Integer)this.reactAt2.get(i)).intValue() == rAt1)) {
/* 444 */         return i;
/*     */       }
/*     */     }
/* 447 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   boolean containsInteger(Vector<Integer> v, Integer iObj)
/*     */   {
/* 453 */     for (int i = 0; i < v.size(); i++) {
/* 454 */       if (iObj.intValue() == ((Integer)v.get(i)).intValue())
/* 455 */         return true;
/*     */     }
/* 457 */     return false;
/*     */   }
/*     */   
/*     */   int getIntegerObjectIndex(Vector<Integer> v, Integer iObj)
/*     */   {
/* 462 */     for (int i = 0; i < v.size(); i++)
/*     */     {
/* 464 */       Integer vi = (Integer)v.get(i);
/* 465 */       if (iObj.intValue() == vi.intValue())
/* 466 */         return i;
/*     */     }
/* 468 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public String transformationDataToString()
/*     */   {
/* 474 */     StringBuffer sb = new StringBuffer();
/*     */     
/* 476 */     System.out.println("Mappings:");
/* 477 */     for (int i = 0; i < this.reactantMapIndex.size(); i++)
/*     */     {
/* 479 */       Integer rMapInd = (Integer)this.reactantMapIndex.get(i);
/* 480 */       int rGlobAtNum = ((Integer)this.reactantAtomNum.get(i)).intValue();
/* 481 */       IAtom glob_ra = this.reactant.getAtom(rGlobAtNum);
/* 482 */       int pIndex = getIntegerObjectIndex(this.productMapIndex, rMapInd);
/* 483 */       int pGlobAtNum = ((Integer)this.productAtomNum.get(pIndex)).intValue();
/* 484 */       IAtom glob_pa = this.product.getAtom(pGlobAtNum);
/*     */       
/* 486 */       System.out.println("Map #" + rMapInd.intValue() + "     " + "at# " + rGlobAtNum + "  Charge = " + this.reactantAtCharge.get(rGlobAtNum) + "   -->  " + "at# " + pGlobAtNum + "  Charge = " + this.productAtCharge.get(pGlobAtNum) + "         pIndex = " + pIndex);
/*     */     }
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
/* 499 */     for (int i = 0; i < this.prodBo.size(); i++)
/*     */     {
/*     */ 
/* 502 */       sb.append("BondTransform: (");
/* 503 */       sb.append(((Integer)this.reactAt1.get(i)).intValue() + ", ");
/* 504 */       sb.append(((Integer)this.reactAt2.get(i)).intValue() + ", ");
/* 505 */        String bo; if (this.reactBo.get(i) == null) {
/* 506 */         bo = "null";
/*     */       } else {
/* 508 */         bo = ((IBond.Order)this.reactBo.get(i)).toString();
/*     */       }
/* 510 */       sb.append(bo + ")  -->  (");
/* 511 */       sb.append(((Integer)this.prodAt1.get(i)).intValue() + ", ");
/* 512 */       sb.append(((Integer)this.prodAt2.get(i)).intValue() + ", ");
/* 513 */       if (this.prodBo.get(i) == null) {
/* 514 */         bo = "null";
/*     */       } else
/* 516 */         bo = ((IBond.Order)this.prodBo.get(i)).toString();
/* 517 */       sb.append(bo.toString() + ")");
/* 518 */       sb.append("\n");
/*     */     }
/* 520 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SMIRKSReaction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
