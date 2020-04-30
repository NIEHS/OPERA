/*      */ package ambit2.smarts;
/*      */ 
/*      */ import ambit2.base.exceptions.AmbitException;
/*      */ import ambit2.base.processors.DefaultAmbitProcessor;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Vector;
/*      */ import org.openscience.cdk1.Bond;
/*      */ import org.openscience.cdk1.RingSet;
/*      */ import org.openscience.cdk1.interfaces.IAtom;
/*      */ import org.openscience.cdk1.interfaces.IAtomContainer;
/*      */ import org.openscience.cdk1.interfaces.IBond;
/*      */ import org.openscience.cdk1.interfaces.IBond.Order;
/*      */ import org.openscience.cdk1.interfaces.IChemObjectBuilder;
/*      */ import org.openscience.cdk1.interfaces.IMolecule;
/*      */ import org.openscience.cdk1.interfaces.IRing;
/*      */ import org.openscience.cdk1.interfaces.IRingSet;
/*      */ import org.openscience.cdk1.isomorphism.matchers.QueryAtomContainer;
/*      */ import org.openscience.cdk1.isomorphism.matchers.smarts.AromaticQueryBond;
/*      */ import org.openscience.cdk1.isomorphism.matchers.smarts.OrderQueryBond;
/*      */ import org.openscience.cdk1.ringsearch.SSSRFinder;
/*      */ import org.openscience.cdk1.silent.SilentChemObjectBuilder;
/*      */ import org.openscience.cdk1.tools.periodictable.PeriodicTable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SmartsToChemObject
/*      */   extends DefaultAmbitProcessor<QueryAtomContainer, IAtomContainer>
/*      */ {
/*      */   private static final long serialVersionUID = -5893878673124511317L;
/*      */   public static final String markProperty = "MARKED_AB";
/*   37 */   public boolean forceAromaticBondsAlways = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   43 */   public boolean forceAromaticBondsForNonRingAtoms = true;
/*      */   
/*      */   boolean mFlagConfirmAromaticBond;
/*      */   
/*      */   int mSubAtomType;
/*      */   
/*      */   int mSubAromaticity;
/*      */   int mCurSubArom;
/*      */   int mRecCurSubArom;
/*      */   
/*      */   public IChemObjectBuilder getBuilder()
/*      */   {
/*   55 */     return this.builder;
/*      */   }
/*      */   
/*      */   public void setBuilder(IChemObjectBuilder builder)
/*      */   {
/*   60 */     this.builder = builder;
/*      */   }
/*      */   
/*      */   public SmartsToChemObject(IChemObjectBuilder builder)
/*      */   {
/*   65 */     setBuilder(builder);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IAtomContainer extractAtomContainer(QueryAtomContainer query, IRingSet ringSet)
/*      */   {
/*   81 */     if (query == null) { return null;
/*      */     }
/*   83 */     Vector<IAtom> atoms = new Vector();
/*   84 */     for (int i = 0; i < query.getAtomCount(); i++) {
/*   85 */       atoms.add(toAtom(query.getAtom(i)));
/*      */     }
/*      */     
/*   88 */     IMolecule container = (IMolecule)this.builder.newInstance(IMolecule.class, new Object[0]);
/*   89 */     for (int i = 0; i < atoms.size(); i++)
/*      */     {
/*   91 */       IAtom a = (IAtom)atoms.get(i);
/*   92 */       if (a != null) {
/*   93 */         container.addAtom(a);
/*      */       }
/*      */     }
/*      */     
/*   97 */     for (int i = 0; i < query.getBondCount(); i++)
/*      */     {
/*   99 */       this.mFlagConfirmAromaticBond = false;
/*  100 */       IBond b = toBond(query.getBond(i));
/*  101 */       if (b != null)
/*      */       {
/*  103 */         IAtom[] ats = new IAtom[2];
/*  104 */         int atNum = query.getAtomNumber(query.getBond(i).getAtom(0));
/*  105 */         ats[0] = ((IAtom)atoms.get(atNum));
/*  106 */         if (ats[0] != null)
/*      */         {
/*      */ 
/*  109 */           atNum = query.getAtomNumber(query.getBond(i).getAtom(1));
/*  110 */           ats[1] = ((IAtom)atoms.get(atNum));
/*  111 */           if (ats[1] != null)
/*      */           {
/*  113 */             b.setAtoms(ats);
/*      */             
/*      */ 
/*  116 */             if ((this.mFlagConfirmAromaticBond) && (ats[0].getFlag(5)) && (ats[1].getFlag(5)))
/*      */             {
/*      */ 
/*  119 */               if (this.forceAromaticBondsAlways)
/*      */               {
/*  121 */                 b.setFlag(5, true);
/*      */ 
/*      */ 
/*      */ 
/*      */               }
/*  126 */               else if (ringSet == null)
/*      */               {
/*  128 */                 if (this.forceAromaticBondsForNonRingAtoms) {
/*  129 */                   b.setFlag(5, true);
/*      */                 }
/*      */                 
/*      */               }
/*  133 */               else if (isRingBond(query.getBond(i), ringSet)) {
/*  134 */                 b.setFlag(5, true);
/*      */               }
/*      */             }
/*      */             
/*  138 */             container.addBond(b);
/*      */           }
/*      */         }
/*      */       } }
/*  142 */     return container;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IAtomContainer extractAtomContainerFullyConnected(QueryAtomContainer query, IRingSet ringSet)
/*      */   {
/*  154 */     if (query == null) { return null;
/*      */     }
/*  156 */     Vector<IAtom> atoms = new Vector();
/*  157 */     for (int i = 0; i < query.getAtomCount(); i++)
/*      */     {
/*  159 */       IAtom a = toAtom(query.getAtom(i));
/*  160 */       if (a == null)
/*  161 */         a = getMarkedAtom();
/*  162 */       atoms.add(a);
/*      */     }
/*      */     
/*      */ 
/*  166 */     IMolecule container = (IMolecule)this.builder.newInstance(IMolecule.class, new Object[0]);
/*  167 */     for (int i = 0; i < atoms.size(); i++)
/*      */     {
/*  169 */       IAtom a = (IAtom)atoms.get(i);
/*  170 */       container.addAtom(a);
/*      */     }
/*      */     
/*      */ 
/*  174 */     for (int i = 0; i < query.getBondCount(); i++)
/*      */     {
/*  176 */       this.mFlagConfirmAromaticBond = false;
/*  177 */       IBond b = toBond(query.getBond(i));
/*      */       
/*      */ 
/*  180 */       IAtom[] ats = new IAtom[2];
/*  181 */       int atNum = query.getAtomNumber(query.getBond(i).getAtom(0));
/*  182 */       ats[0] = ((IAtom)atoms.get(atNum));
/*  183 */       atNum = query.getAtomNumber(query.getBond(i).getAtom(1));
/*  184 */       ats[1] = ((IAtom)atoms.get(atNum));
/*      */       
/*  186 */       if (b != null)
/*      */       {
/*  188 */         b.setAtoms(ats);
/*      */         
/*  190 */         if ((this.mFlagConfirmAromaticBond) && (ats[0].getFlag(5)) && (ats[1].getFlag(5)))
/*      */         {
/*      */ 
/*  193 */           if (this.forceAromaticBondsAlways)
/*      */           {
/*  195 */             b.setFlag(5, true);
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*  200 */           else if (ringSet == null)
/*      */           {
/*  202 */             if (this.forceAromaticBondsForNonRingAtoms) {
/*  203 */               b.setFlag(5, true);
/*      */             }
/*      */             
/*      */           }
/*  207 */           else if (isRingBond(query.getBond(i), ringSet)) {
/*  208 */             b.setFlag(5, true);
/*      */           }
/*      */         }
/*      */         
/*  212 */         container.addBond(b);
/*      */       }
/*      */       else
/*      */       {
/*  216 */         b = getMarkedBond();
/*  217 */         b.setAtoms(ats);
/*  218 */         container.addBond(b);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  223 */     return container;
/*      */   }
/*      */   
/*      */   public QueryAtomContainer convertKekuleSmartsToAromatic(QueryAtomContainer query, IRingSet ringSet) throws Exception
/*      */   {
/*  228 */     Vector<IRingSet> rs = getMaxCondensedRingSystems(ringSet);
/*  229 */     if (rs.size() == 0) {
/*  230 */       return query;
/*      */     }
/*  232 */     for (int i = 0; i < rs.size(); i++)
/*      */     {
/*  234 */       QueryAtomContainer qac = getCondensedFragmentFromRingSets(query, (IRingSet)rs.get(i));
/*  235 */       IAtomContainer container = condensedFragmentToContainer(qac);
/*  236 */       if (container != null)
/*      */       {
/*  238 */         String smiles = SmartsHelper.moleculeToSMILES(container);
/*  239 */         System.out.print("condensed: " + smiles);
/*      */       }
/*      */       else {
/*  242 */         System.out.println("condensed: null");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  247 */       IAtomContainer ac = extractAtomContainerFullyConnected(qac, ringSet);
/*  248 */       String smiles = SmartsHelper.moleculeToSMILES(ac);
/*  249 */       System.out.print("extract: " + smiles);
/*      */     }
/*      */     
/*  252 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public IAtomContainer process(QueryAtomContainer target)
/*      */     throws AmbitException
/*      */   {
/*  259 */     return extractAtomContainer(target);
/*      */   }
/*      */   
/*      */ 
/*      */   public IAtomContainer extractAtomContainer(QueryAtomContainer query)
/*      */   {
/*  265 */     SSSRFinder sssrf = new SSSRFinder(query);
/*  266 */     IRingSet ringSet = sssrf.findSSSR();
/*      */     
/*  268 */     return extractAtomContainer(query, ringSet);
/*      */   }
/*      */   
/*      */   public IAtomContainer extractAtomContainerFullyConnected(QueryAtomContainer query)
/*      */   {
/*  273 */     SSSRFinder sssrf = new SSSRFinder(query);
/*  274 */     IRingSet ringSet = sssrf.findSSSR();
/*      */     
/*  276 */     return extractAtomContainerFullyConnected(query, ringSet);
/*      */   }
/*      */   
/*      */   public QueryAtomContainer convertKekuleSmartsToAromatic(QueryAtomContainer query) throws Exception
/*      */   {
/*  281 */     SSSRFinder sssrf = new SSSRFinder(query);
/*  282 */     IRingSet ringSet = sssrf.findSSSR();
/*      */     
/*  284 */     return convertKekuleSmartsToAromatic(query, ringSet);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IAtom toAtom(IAtom a)
/*      */   {
/*  299 */     if ((a instanceof AliphaticSymbolQueryAtom))
/*      */     {
/*  301 */       IAtom atom = (IAtom)this.builder.newInstance(IAtom.class, new Object[0]);
/*  302 */       atom.setSymbol(a.getSymbol());
/*  303 */       atom.setFlag(5, false);
/*  304 */       return atom;
/*      */     }
/*      */     
/*  307 */     if ((a instanceof AromaticSymbolQueryAtom))
/*      */     {
/*  309 */       IAtom atom = (IAtom)this.builder.newInstance(IAtom.class, new Object[0]);
/*  310 */       atom.setSymbol(a.getSymbol());
/*  311 */       atom.setFlag(5, true);
/*  312 */       return atom;
/*      */     }
/*      */     
/*  315 */     if ((a instanceof SmartsAtomExpression)) {
/*  316 */       return smartsExpressionToAtom((SmartsAtomExpression)a);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  323 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   int mSubAtomCharge;
/*      */   
/*      */ 
/*      */   int mCurSubAtCharge;
/*      */   
/*      */ 
/*      */   int mSubBondType;
/*      */   
/*      */   int mSubBoAromaticity;
/*      */   
/*      */   int mCurSubBoArom;
/*      */   
/*      */   protected IChemObjectBuilder builder;
/*      */   
/*      */   public IAtom smartsExpressionToAtom(SmartsAtomExpression a)
/*      */   {
/*  344 */     int atomCharge = 0;
/*      */     
/*  346 */     Vector<SmartsAtomExpression> subs = getSubExpressions(a, 1003);
/*  347 */     int atType = -1;
/*  348 */     int isArom = -1;
/*  349 */     for (int i = 0; i < subs.size(); i++)
/*      */     {
/*  351 */       analyzeSubExpressionsFromLowAnd(a, (SmartsAtomExpression)subs.get(i));
/*      */       
/*      */ 
/*  354 */       if (this.mSubAtomType != -1)
/*      */       {
/*  356 */         if (atType == -1) {
/*  357 */           atType = this.mSubAtomType;
/*      */ 
/*      */         }
/*  360 */         else if (atType != this.mSubAtomType)
/*      */         {
/*  362 */           atType = -1;
/*  363 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  369 */       if (this.mSubAromaticity != -1)
/*      */       {
/*  371 */         if (isArom == -1) {
/*  372 */           isArom = this.mSubAromaticity;
/*      */ 
/*      */         }
/*  375 */         else if (isArom != this.mSubAromaticity)
/*      */         {
/*  377 */           isArom = -1;
/*  378 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  384 */       if (this.mSubAtomCharge != 0)
/*      */       {
/*      */ 
/*  387 */         atomCharge = this.mSubAtomCharge;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  393 */     if (atType != -1)
/*      */     {
/*  395 */       IAtom atom = (IAtom)SilentChemObjectBuilder.getInstance().newInstance(IAtom.class, new Object[0]);
/*  396 */       atom.setSymbol(PeriodicTable.getSymbol(atType));
/*      */       
/*      */ 
/*  399 */       if (isArom != -1)
/*      */       {
/*  401 */         if (isArom == 1) {
/*  402 */           atom.setFlag(5, true);
/*      */         } else {
/*  404 */           atom.setFlag(5, false);
/*      */         }
/*      */       }
/*      */       
/*  408 */       if (atomCharge != 0) {
/*  409 */         atom.setFormalCharge(new Integer(atomCharge));
/*      */       }
/*      */       
/*      */ 
/*  413 */       return atom;
/*      */     }
/*      */     
/*      */ 
/*  417 */     return null;
/*      */   }
/*      */   
/*      */   public Vector<SmartsAtomExpression> getSubExpressions(SmartsAtomExpression a, int separator)
/*      */   {
/*  422 */     Vector<SmartsAtomExpression> v = new Vector();
/*  423 */     SmartsAtomExpression sub = new SmartsAtomExpression();
/*  424 */     for (int i = 0; i < a.tokens.size(); i++)
/*      */     {
/*  426 */       if (((SmartsExpressionToken)a.tokens.get(i)).type == separator)
/*      */       {
/*  428 */         v.add(sub);
/*  429 */         sub = new SmartsAtomExpression();
/*      */       }
/*      */       else {
/*  432 */         sub.tokens.add(a.tokens.get(i));
/*      */       } }
/*  434 */     v.add(sub);
/*  435 */     return v;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void analyzeSubExpressionsFromLowAnd(SmartsAtomExpression atExp, SmartsAtomExpression sub)
/*      */   {
/*  447 */     Vector<SmartsAtomExpression> sub_subs = getSubExpressions(sub, 1002);
/*  448 */     int[] subAtType = new int[sub_subs.size()];
/*  449 */     int[] subArom = new int[sub_subs.size()];
/*  450 */     int[] subCharge = new int[sub_subs.size()];
/*  451 */     for (int i = 0; i < sub_subs.size(); i++)
/*      */     {
/*  453 */       subAtType[i] = getExpressionAtomType(atExp, (SmartsAtomExpression)sub_subs.get(i));
/*  454 */       subArom[i] = this.mCurSubArom;
/*  455 */       subCharge[i] = this.mCurSubAtCharge;
/*      */     }
/*      */     
/*  458 */     this.mSubAtomType = subAtType[0];
/*  459 */     for (int i = 1; i < subAtType.length; i++)
/*      */     {
/*  461 */       if (this.mSubAtomType != subAtType[i])
/*      */       {
/*  463 */         this.mSubAtomType = -1;
/*  464 */         break;
/*      */       }
/*      */     }
/*      */     
/*  468 */     this.mSubAromaticity = subArom[0];
/*  469 */     for (int i = 1; i < subAtType.length; i++)
/*      */     {
/*  471 */       if (this.mSubAromaticity != subArom[i])
/*      */       {
/*  473 */         this.mSubAromaticity = -1;
/*  474 */         break;
/*      */       }
/*      */     }
/*      */     
/*  478 */     this.mSubAtomCharge = subCharge[0];
/*  479 */     for (int i = 1; i < subCharge.length; i++)
/*      */     {
/*  481 */       if (this.mSubAtomCharge != subCharge[i])
/*      */       {
/*  483 */         this.mSubAtomCharge = 0;
/*  484 */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public int getExpressionAtomType(SmartsAtomExpression atExp, SmartsAtomExpression sub)
/*      */   {
/*  492 */     this.mCurSubArom = -1;
/*  493 */     this.mCurSubAtCharge = 0;
/*      */     
/*      */ 
/*  496 */     int[] pos = new int[sub.tokens.size() + 2];
/*  497 */     pos[0] = -1;
/*  498 */     int n = 0;
/*  499 */     for (int i = 0; i < sub.tokens.size(); i++)
/*      */     {
/*  501 */       if (((SmartsExpressionToken)sub.tokens.get(i)).type == 1001)
/*      */       {
/*  503 */         n++;
/*  504 */         pos[n] = i;
/*      */       }
/*      */     }
/*      */     
/*  508 */     n++;
/*  509 */     pos[n] = sub.tokens.size();
/*      */     
/*  511 */     int expAtType = -1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  517 */     for (int i = 1; i <= n; i++)
/*      */     {
/*      */ 
/*  520 */       boolean FlagNot = false;
/*  521 */       for (int k = pos[(i - 1)] + 1; k < pos[i]; k++)
/*      */       {
/*  523 */         SmartsExpressionToken seTok = (SmartsExpressionToken)sub.tokens.get(k);
/*  524 */         if (seTok.isLogicalOperation())
/*      */         {
/*  526 */           if (seTok.getLogOperation() == 0) {
/*  527 */             FlagNot = !FlagNot;
/*      */           }
/*  529 */           if (seTok.getLogOperation() == 1) {
/*  530 */             FlagNot = false;
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  538 */           switch (seTok.type)
/*      */           {
/*      */           case 1: 
/*  541 */             if ((seTok.param > 0) && 
/*  542 */               (!FlagNot))
/*      */             {
/*  544 */               expAtType = seTok.param;
/*  545 */               this.mCurSubArom = 1;
/*      */             }
/*      */             
/*      */             break;
/*      */           case 2: 
/*  550 */             if ((seTok.param > 0) && 
/*  551 */               (!FlagNot))
/*      */             {
/*  553 */               expAtType = seTok.param;
/*  554 */               this.mCurSubArom = 0;
/*      */             }
/*      */             
/*      */ 
/*      */             break;
/*      */           case 11: 
/*  560 */             if ((seTok.param > 0) && 
/*  561 */               (!FlagNot)) {
/*  562 */               expAtType = seTok.param;
/*      */             }
/*      */             
/*      */             break;
/*      */           case 10: 
/*  567 */             if (!FlagNot) {
/*  568 */               this.mCurSubAtCharge = seTok.param;
/*      */             }
/*      */             
/*      */             break;
/*      */           case 14: 
/*  573 */             int recExpAtType = getRecursiveExpressionAtomType(atExp, seTok.param);
/*  574 */             if ((recExpAtType > 0) && 
/*  575 */               (!FlagNot))
/*      */             {
/*  577 */               expAtType = recExpAtType;
/*  578 */               this.mCurSubArom = this.mRecCurSubArom;
/*      */             }
/*      */             
/*      */             break;
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  588 */     return expAtType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRecursiveExpressionAtomType(SmartsAtomExpression atExp, int n)
/*      */   {
/*  596 */     int mSubAtomType_old = this.mSubAtomType;
/*  597 */     int mSubAromaticity_old = this.mSubAromaticity;
/*  598 */     int mCurSubArom_old = this.mCurSubArom;
/*      */     
/*      */ 
/*  601 */     IAtom a0 = ((QueryAtomContainer)atExp.recSmartsContainers.get(n)).getAtom(0);
/*  602 */     IAtom anew = toAtom(a0);
/*  603 */     if (a0.getFlag(5)) {
/*  604 */       this.mRecCurSubArom = 1;
/*      */     } else {
/*  606 */       this.mRecCurSubArom = 0;
/*      */     }
/*      */     
/*  609 */     this.mSubAtomType = mSubAtomType_old;
/*  610 */     this.mSubAromaticity = mSubAromaticity_old;
/*  611 */     this.mCurSubArom = mCurSubArom_old;
/*      */     
/*  613 */     if (anew == null) {
/*  614 */       return -1;
/*      */     }
/*  616 */     return SmartsConst.getElementNumber(anew.getSymbol());
/*      */   }
/*      */   
/*      */ 
/*      */   public IBond toBond(IBond b)
/*      */   {
/*  622 */     if ((b instanceof SmartsBondExpression)) {
/*  623 */       return smartsExpressionToBond((SmartsBondExpression)b);
/*      */     }
/*  625 */     if ((b instanceof SingleOrAromaticBond))
/*      */     {
/*  627 */       Bond bond = new Bond();
/*  628 */       bond.setOrder(IBond.Order.SINGLE);
/*  629 */       this.mFlagConfirmAromaticBond = true;
/*  630 */       return bond;
/*      */     }
/*      */     
/*  633 */     if ((b instanceof AromaticQueryBond))
/*      */     {
/*  635 */       Bond bond = new Bond();
/*  636 */       bond.setOrder(b.getOrder());
/*  637 */       bond.setFlag(5, true);
/*  638 */       return bond;
/*      */     }
/*      */     
/*  641 */     if ((b instanceof OrderQueryBond))
/*      */     {
/*  643 */       Bond bond = new Bond();
/*  644 */       bond.setOrder(b.getOrder());
/*  645 */       return bond;
/*      */     }
/*      */     
/*  648 */     if ((b instanceof SingleNonAromaticBond))
/*      */     {
/*  650 */       Bond bond = new Bond();
/*  651 */       bond.setOrder(IBond.Order.SINGLE);
/*  652 */       return bond;
/*      */     }
/*      */     
/*  655 */     if ((b instanceof DoubleNonAromaticBond))
/*      */     {
/*  657 */       Bond bond = new Bond();
/*  658 */       bond.setOrder(IBond.Order.DOUBLE);
/*  659 */       return bond;
/*      */     }
/*      */     
/*  662 */     if ((b instanceof DoubleBondAromaticityNotSpecified))
/*      */     {
/*  664 */       Bond bond = new Bond();
/*  665 */       bond.setOrder(IBond.Order.DOUBLE);
/*  666 */       return bond;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  672 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IBond smartsExpressionToBond(SmartsBondExpression b)
/*      */   {
/*  689 */     Vector<SmartsBondExpression> subs = getSubExpressions(b, 1003);
/*  690 */     int boType = -1;
/*  691 */     int isArom = -1;
/*  692 */     boolean FlagAromCorrect = true;
/*      */     
/*  694 */     for (int i = 0; i < subs.size(); i++)
/*      */     {
/*  696 */       analyzeSubExpressionsFromLowAnd(b, (SmartsBondExpression)subs.get(i));
/*  697 */       System.out.print("  sub-expression " + ((SmartsBondExpression)subs.get(i)).toString());
/*  698 */       System.out.println("    mSubBondType = " + this.mSubBondType + "  mSubBoAromaticity = " + this.mSubBoAromaticity);
/*      */       
/*  700 */       if (this.mSubBondType != -1)
/*      */       {
/*  702 */         if (boType == -1) {
/*  703 */           boType = this.mSubBondType;
/*      */ 
/*      */         }
/*  706 */         else if (boType != this.mSubBondType)
/*      */         {
/*  708 */           boType = -1;
/*  709 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  715 */       if (FlagAromCorrect)
/*      */       {
/*  717 */         if (this.mSubBoAromaticity != -1)
/*      */         {
/*  719 */           if (isArom == -1) {
/*  720 */             isArom = this.mSubBoAromaticity;
/*      */ 
/*      */           }
/*  723 */           else if (isArom != this.mSubBoAromaticity)
/*      */           {
/*  725 */             isArom = -1;
/*  726 */             FlagAromCorrect = false;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  735 */     System.out.println("boType = " + boType);
/*      */     
/*      */ 
/*  738 */     if (boType != -1)
/*      */     {
/*      */ 
/*  741 */       IBond bond = (IBond)SilentChemObjectBuilder.getInstance().newInstance(IBond.class, new Object[0]);
/*      */       
/*  743 */       switch (boType)
/*      */       {
/*      */       case 1: 
/*  746 */         bond.setOrder(IBond.Order.SINGLE);
/*  747 */         break;
/*      */       case 2: 
/*  749 */         bond.setOrder(IBond.Order.DOUBLE);
/*  750 */         break;
/*      */       case 3: 
/*  752 */         bond.setOrder(IBond.Order.TRIPLE);
/*      */       }
/*      */       
/*      */       
/*      */ 
/*  757 */       if ((FlagAromCorrect) && 
/*  758 */         (isArom != -1))
/*      */       {
/*  760 */         if (isArom == 1) {
/*  761 */           bond.setFlag(5, true);
/*      */         } else {
/*  763 */           bond.setFlag(5, false);
/*      */         }
/*      */       }
/*  766 */       return bond;
/*      */     }
/*      */     
/*  769 */     return null;
/*      */   }
/*      */   
/*      */   public Vector<SmartsBondExpression> getSubExpressions(SmartsBondExpression b, int separator)
/*      */   {
/*  774 */     Vector<SmartsBondExpression> v = new Vector();
/*  775 */     SmartsBondExpression sub = new SmartsBondExpression();
/*  776 */     for (int i = 0; i < b.tokens.size(); i++)
/*      */     {
/*  778 */       if (((Integer)b.tokens.get(i)).intValue() == separator)
/*      */       {
/*  780 */         v.add(sub);
/*  781 */         sub = new SmartsBondExpression();
/*      */       }
/*      */       else {
/*  784 */         sub.tokens.add(b.tokens.get(i));
/*      */       } }
/*  786 */     v.add(sub);
/*  787 */     return v;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void analyzeSubExpressionsFromLowAnd(SmartsBondExpression boExp, SmartsBondExpression sub)
/*      */   {
/*  800 */     Vector<SmartsBondExpression> sub_subs = getSubExpressions(sub, 1002);
/*  801 */     int[] subBoType = new int[sub_subs.size()];
/*  802 */     int[] subArom = new int[sub_subs.size()];
/*      */     
/*  804 */     for (int i = 0; i < sub_subs.size(); i++)
/*      */     {
/*  806 */       subBoType[i] = getExpressionBondType(boExp, (SmartsBondExpression)sub_subs.get(i));
/*  807 */       subArom[i] = this.mCurSubArom;
/*      */     }
/*      */     
/*  810 */     this.mSubBondType = subBoType[0];
/*  811 */     for (int i = 1; i < subBoType.length; i++)
/*      */     {
/*  813 */       if (this.mSubBondType != subBoType[i])
/*      */       {
/*  815 */         this.mSubBondType = -1;
/*  816 */         break;
/*      */       }
/*      */     }
/*      */     
/*  820 */     this.mSubBoAromaticity = subArom[0];
/*  821 */     for (int i = 1; i < subBoType.length; i++)
/*      */     {
/*  823 */       if (this.mSubAromaticity != subArom[i])
/*      */       {
/*  825 */         this.mSubAromaticity = -1;
/*  826 */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public int getExpressionBondType(SmartsBondExpression boExp, SmartsBondExpression sub)
/*      */   {
/*  834 */     this.mCurSubBoArom = -1;
/*      */     
/*      */ 
/*  837 */     int[] pos = new int[sub.tokens.size() + 2];
/*  838 */     pos[0] = -1;
/*  839 */     int n = 0;
/*  840 */     for (int i = 0; i < sub.tokens.size(); i++)
/*      */     {
/*  842 */       if (((Integer)sub.tokens.get(i)).intValue() == 1001)
/*      */       {
/*  844 */         n++;
/*  845 */         pos[n] = i;
/*      */       }
/*      */     }
/*      */     
/*  849 */     n++;
/*  850 */     pos[n] = sub.tokens.size();
/*      */     
/*  852 */     int expBoType = -1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  857 */     for (int i = 1; i <= n; i++)
/*      */     {
/*      */ 
/*  860 */       boolean FlagNot = false;
/*  861 */       for (int k = pos[(i - 1)] + 1; k < pos[i]; k++)
/*      */       {
/*  863 */         int seTok = ((Integer)sub.tokens.get(k)).intValue();
/*  864 */         if (seTok >= 1000)
/*      */         {
/*  866 */           if (seTok == 1000) {
/*  867 */             FlagNot = !FlagNot;
/*      */           }
/*  869 */           if (seTok == 1001) {
/*  870 */             FlagNot = false;
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  877 */           switch (seTok)
/*      */           {
/*      */           case 0: 
/*      */             break;
/*      */           
/*      */           case 1: 
/*  883 */             if (!FlagNot) {
/*  884 */               expBoType = 1;
/*      */             }
/*      */             break;
/*      */           case 2: 
/*  888 */             if (!FlagNot) {
/*  889 */               expBoType = 2;
/*      */             }
/*      */             break;
/*      */           case 3: 
/*  893 */             if (!FlagNot) {
/*  894 */               expBoType = 3;
/*      */             }
/*      */             break;
/*      */           case 4: 
/*  898 */             if (FlagNot) {
/*  899 */               this.mCurSubBoArom = 0;
/*      */             } else
/*  901 */               this.mCurSubBoArom = 1;
/*  902 */             break;
/*      */           
/*      */           case 5: 
/*      */             break;
/*      */           
/*      */ 
/*      */           case 6: 
/*      */           case 7: 
/*      */           case 8: 
/*      */           case 9: 
/*  912 */             if (!FlagNot) {
/*  913 */               expBoType = 1;
/*      */             }
/*      */             break;
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*      */     }
/*  921 */     return expBoType;
/*      */   }
/*      */   
/*      */ 
/*      */   boolean isRingBond(IBond b, IRingSet ringSet)
/*      */   {
/*  927 */     IRingSet atom0Rings = ringSet.getRings(b.getAtom(0));
/*  928 */     IRingSet atom1Rings = ringSet.getRings(b.getAtom(1));
/*      */     
/*  930 */     for (int i = 0; i < atom0Rings.getAtomContainerCount(); i++)
/*      */     {
/*  932 */       IAtomContainer c = atom0Rings.getAtomContainer(i);
/*  933 */       for (int k = 0; k < atom1Rings.getAtomContainerCount(); k++)
/*  934 */         if (atom1Rings.getAtomContainer(k) == c)
/*  935 */           return true;
/*      */     }
/*  937 */     return false;
/*      */   }
/*      */   
/*      */   IAtom getMarkedAtom()
/*      */   {
/*  942 */     IAtom a = (IAtom)this.builder.newInstance(IAtom.class, new Object[] { "C" });
/*  943 */     a.setProperty("MARKED_AB", new Integer(1));
/*  944 */     return a;
/*      */   }
/*      */   
/*      */   IBond getMarkedBond()
/*      */   {
/*  949 */     IBond b = (IBond)this.builder.newInstance(IBond.class, new Object[0]);
/*  950 */     b.setOrder(IBond.Order.SINGLE);
/*  951 */     b.setProperty("MARKED_AB", new Integer(1));
/*  952 */     return b;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Vector<IRingSet> getMaxCondensedRingSystems(IRingSet ringSet)
/*      */   {
/*  961 */     Vector<IRingSet> v = new Vector();
/*  962 */     int n = ringSet.getAtomContainerCount();
/*  963 */     if (n == 0) {
/*  964 */       return v;
/*      */     }
/*  966 */     IRingSet workRS = new RingSet();
/*  967 */     workRS.add(ringSet);
/*      */     
/*      */ 
/*  970 */     while (workRS.getAtomContainerCount() > 0)
/*      */     {
/*  972 */       IAtomContainer ac = workRS.getAtomContainer(0);
/*  973 */       IRingSet rs = getCondenzedRingsTo(ac, ringSet);
/*  974 */       v.add(rs);
/*      */       
/*  976 */       for (int i = 0; i < rs.getAtomContainerCount(); i++)
/*      */       {
/*  978 */         workRS.removeAtomContainer(rs.getAtomContainer(i));
/*      */       }
/*      */     }
/*  981 */     return v;
/*      */   }
/*      */   
/*      */ 
/*      */   IRingSet getCondenzedRingsTo(IAtomContainer startAC, IRingSet ringSet)
/*      */   {
/*  987 */     IRingSet condRS = (IRingSet)this.builder.newInstance(IRingSet.class, new Object[0]);
/*  988 */     int curRing = 0;
/*  989 */     condRS.addAtomContainer(startAC);
/*      */     
/*  991 */     while (curRing < condRS.getAtomContainerCount())
/*      */     {
/*      */ 
/*  994 */       IRingSet rsConnected = ringSet.getConnectedRings((IRing)condRS.getAtomContainer(curRing));
/*  995 */       for (int i = 0; i < rsConnected.getAtomContainerCount(); i++)
/*      */       {
/*  997 */         if (!condRS.contains(rsConnected.getAtomContainer(i)))
/*  998 */           condRS.addAtomContainer(rsConnected.getAtomContainer(i));
/*      */       }
/* 1000 */       curRing++;
/*      */     }
/*      */     
/* 1003 */     return condRS;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   QueryAtomContainer getCondensedFragmentFromRingSets(QueryAtomContainer query, IRingSet rs)
/*      */   {
/* 1011 */     QueryAtomContainer qac = new QueryAtomContainer();
/* 1012 */     for (int i = 0; i < rs.getAtomContainerCount(); i++)
/*      */     {
/* 1014 */       IAtomContainer ac = rs.getAtomContainer(i);
/*      */       
/* 1016 */       for (int k = 0; k < ac.getAtomCount(); k++)
/*      */       {
/* 1018 */         IAtom a = ac.getAtom(k);
/* 1019 */         if (!qac.contains(a)) {
/* 1020 */           qac.addAtom(a);
/*      */         }
/*      */       }
/* 1023 */       for (int k = 0; k < ac.getBondCount(); k++)
/*      */       {
/* 1025 */         IBond b = ac.getBond(k);
/* 1026 */         if (!qac.contains(b)) {
/* 1027 */           qac.addBond(b);
/*      */         }
/*      */       }
/*      */     }
/* 1031 */     return qac;
/*      */   }
/*      */   
/*      */ 
/*      */   IAtomContainer condensedFragmentToContainer(QueryAtomContainer frag)
/*      */   {
/* 1037 */     IMolecule container = (IMolecule)this.builder.newInstance(IMolecule.class, new Object[0]);
/*      */     
/*      */ 
/* 1040 */     Vector<IAtom> atoms = new Vector();
/* 1041 */     for (int i = 0; i < frag.getAtomCount(); i++)
/*      */     {
/* 1043 */       IAtom a = toAtom(frag.getAtom(i));
/* 1044 */       if (a == null)
/* 1045 */         return null;
/* 1046 */       if (a.getFlag(5))
/* 1047 */         return null;
/* 1048 */       atoms.add(a);
/*      */     }
/*      */     
/*      */ 
/* 1052 */     for (int i = 0; i < atoms.size(); i++)
/*      */     {
/* 1054 */       IAtom a = (IAtom)atoms.get(i);
/* 1055 */       container.addAtom(a);
/*      */     }
/*      */     
/* 1058 */     for (int i = 0; i < frag.getBondCount(); i++)
/*      */     {
/* 1060 */       this.mFlagConfirmAromaticBond = false;
/* 1061 */       IBond b = toBond(frag.getBond(i));
/* 1062 */       if (b != null)
/*      */       {
/* 1064 */         IAtom[] ats = new IAtom[2];
/* 1065 */         int atNum = frag.getAtomNumber(frag.getBond(i).getAtom(0));
/* 1066 */         ats[0] = ((IAtom)atoms.get(atNum));
/* 1067 */         atNum = frag.getAtomNumber(frag.getBond(i).getAtom(1));
/* 1068 */         ats[1] = ((IAtom)atoms.get(atNum));
/* 1069 */         b.setAtoms(ats);
/*      */         
/* 1071 */         container.addBond(b);
/*      */       }
/*      */     }
/*      */     
/* 1075 */     return container;
/*      */   }
/*      */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SmartsToChemObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
