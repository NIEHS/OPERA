/*      */ package ambit2.smarts;
/*      */ 
/*      */ import java.util.List;
/*      */ import java.util.Stack;
/*      */ import java.util.TreeMap;
/*      */ import java.util.Vector;
/*      */ import org.openscience.cdk1.Atom;
/*      */ import org.openscience.cdk1.interfaces.IAtom;
/*      */ import org.openscience.cdk1.interfaces.IAtomContainer;
/*      */ import org.openscience.cdk1.interfaces.IBond;
/*      */ import org.openscience.cdk1.interfaces.IBond.Order;
/*      */ import org.openscience.cdk1.interfaces.IRingSet;
/*      */ import org.openscience.cdk1.isomorphism.matchers.IQueryAtom;
/*      */ import org.openscience.cdk1.isomorphism.matchers.IQueryBond;
/*      */ import org.openscience.cdk1.isomorphism.matchers.QueryAtomContainer;
/*      */ import org.openscience.cdk1.isomorphism.matchers.smarts.AliphaticAtom;
/*      */ import org.openscience.cdk1.isomorphism.matchers.smarts.AnyAtom;
/*      */ import org.openscience.cdk1.isomorphism.matchers.smarts.AnyOrderQueryBond;
/*      */ import org.openscience.cdk1.isomorphism.matchers.smarts.AromaticAtom;
/*      */ import org.openscience.cdk1.isomorphism.matchers.smarts.AromaticQueryBond;
/*      */ import org.openscience.cdk1.isomorphism.matchers.smarts.OrderQueryBond;
/*      */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSBond;
/*      */ import org.openscience.cdk1.ringsearch.SSSRFinder;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SmartsParser
/*      */ {
/*      */   String smarts;
/*      */   QueryAtomContainer container;
/*   57 */   Vector<SmartsParserError> errors = new Vector();
/*   58 */   Stack<IQueryAtom> brackets = new Stack();
/*   59 */   Vector<SMARTSBond> directionalBonds = new Vector();
/*   60 */   Vector<Integer> directions = new Vector();
/*      */   
/*   62 */   Vector<SMARTSBond> processedDirBonds = new Vector();
/*   63 */   Vector<SMARTSBond> processedDoubleBonds = new Vector();
/*   64 */   Vector<SMARTSBond> newStereoDoubleBonds = new Vector();
/*   65 */   TreeMap<Integer, RingClosure> indexes = new TreeMap();
/*      */   boolean mNeedNeighbourData;
/*      */   boolean mNeedValencyData;
/*      */   boolean mNeedRingData;
/*      */   boolean mNeedRingData2;
/*      */   boolean mNeedExplicitHData;
/*      */   boolean mNeedParentMoleculeData;
/*      */   public boolean hasRecursiveSmarts;
/*   73 */   public boolean mSupportMOEExtension = true;
/*   74 */   public boolean mUseMOEvPrimitive = false;
/*   75 */   public boolean mSupportOpenEyeExtension = true;
/*   76 */   public boolean mSupportOpenBabelExtension = true;
/*   77 */   public boolean mSupportSmirksSyntax = false;
/*   78 */   public boolean mSupportDoubleBondAromaticityNotSpecified = false;
/*      */   
/*      */ 
/*   81 */   boolean FlagCLG = false;
/*      */   int curComponent;
/*      */   public int numFragments;
/*      */   public int maxCompNumber;
/*   85 */   public Vector<QueryAtomContainer> fragments = new Vector();
/*   86 */   public Vector<Integer> fragmentComponents = new Vector();
/*      */   
/*      */   QueryAtomContainer curFragment;
/*      */   
/*      */   int curChar;
/*      */   
/*      */   IQueryAtom prevAtom;
/*      */   
/*      */   SMARTSBond curBond;
/*      */   
/*      */   SmartsAtomExpression curAtExpr;
/*      */   int curBondType;
/*      */   int nChars;
/*      */   boolean insideRecSmarts;
/*  100 */   int curSmirksMapIndex = -1;
/*      */   
/*      */   public QueryAtomContainer parse(String sm)
/*      */   {
/*  104 */     this.smarts = sm;
/*  105 */     this.container = new QueryAtomContainer();
/*  106 */     this.errors.clear();
/*  107 */     nullifyDataFlags();
/*  108 */     init();
/*  109 */     parse();
/*  110 */     return this.container;
/*      */   }
/*      */   
/*      */   void init()
/*      */   {
/*  115 */     this.nChars = this.smarts.length();
/*  116 */     this.brackets.clear();
/*  117 */     this.indexes.clear();
/*  118 */     this.directionalBonds.clear();
/*  119 */     this.directions.clear();
/*  120 */     this.prevAtom = null;
/*  121 */     this.curBond = null;
/*  122 */     this.curBondType = 100;
/*  123 */     this.curChar = 0;
/*  124 */     this.insideRecSmarts = false;
/*      */     
/*      */ 
/*  127 */     this.fragments.clear();
/*  128 */     this.fragmentComponents.clear();
/*  129 */     this.curComponent = 0;
/*  130 */     this.numFragments = 0;
/*  131 */     this.maxCompNumber = 0;
/*      */   }
/*      */   
/*      */   void parse()
/*      */   {
/*  136 */     while ((this.curChar < this.nChars) && (this.errors.size() == 0))
/*      */     {
/*  138 */       if (Character.isLetter(this.smarts.charAt(this.curChar)))
/*      */       {
/*  140 */         parseAtom();
/*      */ 
/*      */       }
/*  143 */       else if (Character.isDigit(this.smarts.charAt(this.curChar)))
/*      */       {
/*  145 */         parseAtomIndex();
/*      */       }
/*      */       else
/*      */       {
/*  149 */         parseSpecialSymbol();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  154 */     if (!this.brackets.empty()) {
/*  155 */       newError("There are unclosed brackets", -1, "");
/*      */     }
/*      */     
/*  158 */     if (this.indexes.size() != 0)
/*      */     {
/*  160 */       newError("There are unused atom indexes", -1, "");
/*      */     }
/*      */     
/*  163 */     if (this.directionalBonds.size() > 0) {
/*  164 */       setDoubleBondsStereoInfo();
/*      */     }
/*  166 */     setNeededDataFlags();
/*      */     
/*      */ 
/*  169 */     QueryAtomContainer curContainer = this.container;
/*  170 */     for (int i = 0; i < curContainer.getAtomCount(); i++)
/*      */     {
/*  172 */       if ((curContainer.getAtom(i) instanceof SmartsAtomExpression))
/*      */       {
/*  174 */         SmartsAtomExpression sa = (SmartsAtomExpression)curContainer.getAtom(i);
/*  175 */         for (int j = 0; j < sa.recSmartsStrings.size(); j++)
/*      */         {
/*  177 */           this.hasRecursiveSmarts = true;
/*  178 */           this.smarts = ((String)sa.recSmartsStrings.get(j));
/*  179 */           this.container = new QueryAtomContainer();
/*  180 */           init();
/*  181 */           this.insideRecSmarts = true;
/*  182 */           parse();
/*  183 */           sa.recSmartsContainers.add(this.container);
/*  184 */           this.insideRecSmarts = false;
/*      */         }
/*  186 */         convertChirality(sa);
/*      */       }
/*      */     }
/*      */     
/*  190 */     this.container = curContainer;
/*      */   }
/*      */   
/*      */   public void setComponentLevelGrouping(boolean flag)
/*      */   {
/*  195 */     this.FlagCLG = flag;
/*      */   }
/*      */   
/*      */   public boolean needNeighbourData()
/*      */   {
/*  200 */     return this.mNeedNeighbourData;
/*      */   }
/*      */   
/*      */   public boolean needExplicitHData()
/*      */   {
/*  205 */     return this.mNeedExplicitHData;
/*      */   }
/*      */   
/*      */   public boolean needValencyData()
/*      */   {
/*  210 */     return this.mNeedValencyData;
/*      */   }
/*      */   
/*      */   public boolean needRingData()
/*      */   {
/*  215 */     return this.mNeedRingData;
/*      */   }
/*      */   
/*      */   public boolean needRingData2()
/*      */   {
/*  220 */     return this.mNeedRingData2;
/*      */   }
/*      */   
/*      */   public boolean needParentMoleculeData()
/*      */   {
/*  225 */     return this.mNeedParentMoleculeData;
/*      */   }
/*      */   
/*      */   void nullifyDataFlags()
/*      */   {
/*  230 */     this.mNeedNeighbourData = false;
/*  231 */     this.mNeedValencyData = false;
/*  232 */     this.mNeedRingData = false;
/*  233 */     this.mNeedRingData2 = false;
/*  234 */     this.mNeedExplicitHData = false;
/*  235 */     this.mNeedParentMoleculeData = false;
/*  236 */     this.hasRecursiveSmarts = false;
/*      */   }
/*      */   
/*      */   public void setNeededDataFlags()
/*      */   {
/*  241 */     for (int i = 0; i < this.container.getAtomCount(); i++)
/*      */     {
/*  243 */       if ((this.container.getAtom(i) instanceof SmartsAtomExpression))
/*      */       {
/*  245 */         SmartsAtomExpression sa = (SmartsAtomExpression)this.container.getAtom(i);
/*  246 */         for (int j = 0; j < sa.tokens.size(); j++)
/*      */         {
/*  248 */           SmartsExpressionToken tok = (SmartsExpressionToken)sa.tokens.get(j);
/*  249 */           if (tok.type == 4) {
/*  250 */             this.mNeedExplicitHData = true;
/*      */           }
/*  252 */           if ((tok.type == 3) || (tok.type == 9) || (tok.type == 4) || (tok.type == 5))
/*      */           {
/*      */ 
/*      */ 
/*  256 */             this.mNeedNeighbourData = true;
/*      */ 
/*      */           }
/*  259 */           else if ((tok.type == 16) || (tok.type == 20) || (tok.type == 21))
/*      */           {
/*      */ 
/*  262 */             this.mNeedParentMoleculeData = true;
/*      */           }
/*  264 */           else if (tok.type == 15)
/*      */           {
/*  266 */             this.mNeedParentMoleculeData = true;
/*  267 */             this.mNeedRingData2 = true;
/*      */ 
/*      */ 
/*      */           }
/*  271 */           else if (tok.type == 8) {
/*  272 */             this.mNeedValencyData = true;
/*      */           }
/*  274 */           else if ((tok.type == 6) || (tok.type == 7))
/*      */           {
/*  276 */             this.mNeedRingData = true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  284 */     if (!this.mNeedRingData)
/*      */     {
/*  286 */       for (int i = 0; i < this.container.getBondCount(); i++)
/*      */       {
/*  288 */         if ((this.container.getBond(i) instanceof SmartsBondExpression))
/*      */         {
/*  290 */           SmartsBondExpression sb = (SmartsBondExpression)this.container.getBond(i);
/*  291 */           for (int j = 0; j < sb.tokens.size(); j++)
/*      */           {
/*  293 */             if (((Integer)sb.tokens.get(j)).intValue() == 5)
/*      */             {
/*  295 */               this.mNeedRingData2 = true;
/*  296 */               break;
/*      */             }
/*      */           }
/*  299 */           if (this.mNeedRingData2) {
/*      */             break;
/*      */           }
/*      */         }
/*  303 */         else if ((this.container.getBond(i) instanceof RingQueryBond))
/*      */         {
/*  305 */           this.mNeedRingData2 = true;
/*  306 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void newError(String msg, int pos, String param)
/*      */   {
/*      */     SmartsParserError err;
/*  316 */     if (this.insideRecSmarts) {
/*  317 */       err = new SmartsParserError(this.smarts, "Inside recursive Smarts: " + msg, pos, param);
/*      */     } else
/*  319 */       err = new SmartsParserError(this.smarts, msg, pos, param);
/*  320 */     this.errors.add(err);
/*      */   }
/*      */   
/*      */   public String getErrorMessages()
/*      */   {
/*  325 */     StringBuffer sb = new StringBuffer();
/*  326 */     for (int i = 0; i < this.errors.size(); i++)
/*      */     {
/*  328 */       sb.append(((SmartsParserError)this.errors.get(i)).getError() + "\n");
/*      */     }
/*  330 */     return sb.toString();
/*      */   }
/*      */   
/*      */   void newFragment()
/*      */   {
/*  335 */     this.numFragments += 1;
/*  336 */     this.curFragment = new QueryAtomContainer();
/*  337 */     this.fragments.add(this.curFragment);
/*  338 */     this.fragmentComponents.add(new Integer(this.curComponent));
/*      */   }
/*      */   
/*      */   void addAtom(IQueryAtom atom)
/*      */   {
/*  343 */     this.container.addAtom(atom);
/*  344 */     if (this.prevAtom != null)
/*      */     {
/*  346 */       this.curFragment.addAtom(atom);
/*  347 */       addBond(this.prevAtom, atom);
/*      */     }
/*      */     else
/*      */     {
/*  351 */       newFragment();
/*  352 */       this.curFragment.addAtom(atom);
/*      */     }
/*      */     
/*      */ 
/*  356 */     if (this.mSupportSmirksSyntax)
/*      */     {
/*  358 */       if (this.curSmirksMapIndex > -1) {
/*  359 */         atom.setProperty("SmirksMapIndex", new Integer(this.curSmirksMapIndex));
/*      */       }
/*      */       
/*  362 */       this.curSmirksMapIndex = -1;
/*      */     }
/*      */     
/*      */ 
/*  366 */     this.prevAtom = atom;
/*  367 */     this.curBond = null;
/*  368 */     this.curBondType = 100;
/*      */   }
/*      */   
/*      */   void addBond(IQueryAtom atom0, IQueryAtom atom1)
/*      */   {
/*  373 */     if (this.curBond == null)
/*      */     {
/*  375 */       switch (this.curBondType)
/*      */       {
/*      */       case 0: 
/*  378 */         this.curBond = new AnyOrderQueryBond();
/*  379 */         break;
/*      */       case 1: 
/*  381 */         this.curBond = new SingleNonAromaticBond();
/*  382 */         break;
/*      */       case 2: 
/*  384 */         if (this.mSupportDoubleBondAromaticityNotSpecified) {
/*  385 */           this.curBond = new DoubleBondAromaticityNotSpecified();
/*      */         } else
/*  387 */           this.curBond = new DoubleNonAromaticBond();
/*  388 */         break;
/*      */       case 3: 
/*  390 */         this.curBond = new OrderQueryBond(IBond.Order.TRIPLE);
/*  391 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       case 4: 
/*  398 */         this.curBond = new AromaticQueryBond();
/*  399 */         break;
/*      */       case 5: 
/*  401 */         this.curBond = new RingQueryBond();
/*  402 */         break;
/*      */       
/*      */ 
/*      */       case 6: 
/*      */       case 7: 
/*      */       case 8: 
/*      */       case 9: 
/*  409 */         this.curBond = new OrderQueryBond(IBond.Order.SINGLE);
/*  410 */         this.directionalBonds.add(this.curBond);
/*  411 */         this.directions.add(new Integer(this.curBondType));
/*  412 */         break;
/*      */       
/*      */       case 100: 
/*  415 */         this.curBond = new SingleOrAromaticBond();
/*      */       }
/*      */       
/*      */     }
/*      */     
/*  420 */     IAtom[] atoms = new Atom[2];
/*  421 */     atoms[0] = atom0;
/*  422 */     atoms[1] = atom1;
/*  423 */     this.curBond.setAtoms(atoms);
/*  424 */     this.container.addBond(this.curBond);
/*  425 */     this.curFragment.addBond(this.curBond);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void parseAtom()
/*      */   {
/*  434 */     IQueryAtom curAtom = null;
/*  435 */     String symb = null;
/*  436 */     switch (this.smarts.charAt(this.curChar))
/*      */     {
/*      */ 
/*      */     case 'a': 
/*  440 */       curAtom = new AromaticAtom();
/*  441 */       this.curChar += 1;
/*  442 */       break;
/*      */     case 'c': 
/*      */     case 'n': 
/*      */     case 'o': 
/*      */     case 'p': 
/*      */     case 's': 
/*  448 */       char ch = Character.toUpperCase(this.smarts.charAt(this.curChar));
/*  449 */       curAtom = new AromaticSymbolQueryAtom();
/*  450 */       curAtom.setSymbol(Character.toString(ch));
/*  451 */       this.curChar += 1;
/*  452 */       break;
/*      */     
/*      */     case 'C': 
/*  455 */       symb = "C";
/*  456 */       this.curChar += 1;
/*  457 */       if (this.curChar < this.nChars)
/*      */       {
/*  459 */         if (this.smarts.charAt(this.curChar) == 'l')
/*      */         {
/*  461 */           symb = "Cl";
/*  462 */           this.curChar += 1;
/*      */         }
/*      */       }
/*  465 */       curAtom = new AliphaticSymbolQueryAtom();
/*  466 */       curAtom.setSymbol(symb);
/*  467 */       break;
/*      */     
/*      */     case 'B': 
/*  470 */       symb = "B";
/*  471 */       this.curChar += 1;
/*  472 */       if (this.curChar < this.nChars)
/*      */       {
/*  474 */         if (this.smarts.charAt(this.curChar) == 'r')
/*      */         {
/*  476 */           symb = "Br";
/*  477 */           this.curChar += 1;
/*      */         }
/*      */       }
/*  480 */       curAtom = new AliphaticSymbolQueryAtom();
/*  481 */       curAtom.setSymbol(symb);
/*  482 */       break;
/*      */     
/*      */     case 'A': 
/*  485 */       curAtom = new AliphaticAtom();
/*  486 */       this.curChar += 1;
/*  487 */       break;
/*      */     case 'F': 
/*      */     case 'I': 
/*      */     case 'N': 
/*      */     case 'O': 
/*      */     case 'P': 
/*      */     case 'S': 
/*  494 */       curAtom = new AliphaticSymbolQueryAtom();
/*  495 */       curAtom.setSymbol(Character.toString(this.smarts.charAt(this.curChar)));
/*  496 */       this.curChar += 1;
/*      */     }
/*      */     
/*      */     
/*  500 */     if (curAtom == null) {
/*  501 */       newError("Incorrect atomic symbol", this.curChar + 1, "");
/*      */     } else {
/*  503 */       addAtom(curAtom);
/*      */     }
/*      */   }
/*      */   
/*      */   void parseAtomIndex() {
/*  508 */     if (this.smarts.charAt(this.curChar) == '%')
/*      */     {
/*  510 */       this.curChar += 1;
/*  511 */       if (this.curChar == this.nChars)
/*      */       {
/*  513 */         newError("Incorrect ring closure", this.curChar, "");
/*  514 */         return;
/*      */       }
/*      */       
/*  517 */       if (Character.isDigit(this.smarts.charAt(this.curChar))) {
/*  518 */         registerIndex(getInteger());
/*      */       } else {
/*  520 */         newError("Incorrect ring closure", this.curChar, "");
/*      */       }
/*      */     }
/*      */     else {
/*  524 */       registerIndex(Character.getNumericValue(this.smarts.charAt(this.curChar)));
/*  525 */       this.curChar += 1;
/*      */     }
/*      */   }
/*      */   
/*      */   int getInteger()
/*      */   {
/*  531 */     if (!Character.isDigit(this.smarts.charAt(this.curChar))) {
/*  532 */       return -1;
/*      */     }
/*  534 */     int n = 0;
/*  535 */     while (this.curChar < this.nChars)
/*      */     {
/*  537 */       char ch = this.smarts.charAt(this.curChar);
/*  538 */       if (!Character.isDigit(ch))
/*      */         break;
/*  540 */       n = 10 * n + Character.getNumericValue(ch);
/*  541 */       this.curChar += 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  546 */     return n;
/*      */   }
/*      */   
/*      */   void registerIndex(int n)
/*      */   {
/*  551 */     Integer i = new Integer(n);
/*  552 */     RingClosure rc = (RingClosure)this.indexes.get(i);
/*  553 */     if (rc == null)
/*      */     {
/*      */ 
/*  556 */       RingClosure rc1 = new RingClosure();
/*  557 */       rc1.firstAtom = this.prevAtom;
/*  558 */       if (this.curBond == null) {
/*  559 */         rc1.firstBond = this.curBondType;
/*      */       } else {
/*  561 */         newError("Use of a bond expression for the first appearence of atom index", this.curChar + 1, "");
/*      */       }
/*  563 */       this.indexes.put(i, rc1);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  568 */       this.curBond = null;
/*  569 */       this.curBondType = 100;
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*  578 */       if (rc.firstBond == 100)
/*      */       {
/*  580 */         addBond(rc.firstAtom, this.prevAtom);
/*      */         
/*  582 */         this.curBond = null;
/*  583 */         this.curBondType = 100;
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*  588 */       else if (this.curBond == null)
/*      */       {
/*  590 */         if (this.curBondType == 100)
/*      */         {
/*      */ 
/*      */ 
/*  594 */           this.curBondType = rc.firstBond;
/*  595 */           addBond(rc.firstAtom, this.prevAtom);
/*      */           
/*  597 */           this.curBond = null;
/*  598 */           this.curBondType = 100;
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*  603 */         else if (rc.firstBond != this.curBondType) {
/*  604 */           newError("Atom index " + n + " is associated with two different bond types", -1, "");
/*      */         }
/*      */         else {
/*  607 */           addBond(rc.firstAtom, this.prevAtom);
/*      */           
/*  609 */           this.curBond = null;
/*  610 */           this.curBondType = 100;
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  618 */         newError("Atom index " + n + " is associated with two different bond types", -1, "");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  624 */       this.indexes.remove(i);
/*      */     }
/*      */   }
/*      */   
/*      */   void parseSpecialSymbol()
/*      */   {
/*  630 */     switch (this.smarts.charAt(this.curChar))
/*      */     {
/*      */     case '*': 
/*  633 */       IQueryAtom curAtom = new AnyAtom();
/*  634 */       this.curChar += 1;
/*  635 */       addAtom(curAtom);
/*  636 */       break;
/*      */     
/*      */     case '!': 
/*      */     case '#': 
/*      */     case '&': 
/*      */     case ',': 
/*      */     case '-': 
/*      */     case '/': 
/*      */     case ':': 
/*      */     case ';': 
/*      */     case '=': 
/*      */     case '@': 
/*      */     case '\\': 
/*      */     case '~': 
/*  650 */       parseBondExpression();
/*  651 */       break;
/*      */     case '%': 
/*  653 */       parseAtomIndex();
/*  654 */       break;
/*      */     case '(': 
/*  656 */       if (this.prevAtom == null)
/*      */       {
/*  658 */         if (this.FlagCLG)
/*      */         {
/*  660 */           if (this.curComponent > 0)
/*      */           {
/*  662 */             newError("Incorrect nested componet brackets", this.curChar + 1, "");
/*      */           }
/*      */           else
/*      */           {
/*  666 */             this.brackets.push(this.prevAtom);
/*  667 */             this.maxCompNumber += 1;
/*  668 */             this.curComponent = this.maxCompNumber;
/*      */           }
/*      */           
/*      */         }
/*      */         else {
/*  673 */           newError("Component Level Grouping is off: incorrect openning brackect", this.curChar + 1, "");
/*      */         }
/*      */       } else {
/*  676 */         this.brackets.push(this.prevAtom);
/*      */       }
/*  678 */       this.curChar += 1;
/*  679 */       break;
/*      */     case ')': 
/*  681 */       if (this.brackets.empty())
/*      */       {
/*      */ 
/*  684 */         newError("Incorrect closing brackect", this.curChar + 1, "");
/*  685 */         return;
/*      */       }
/*      */       
/*  688 */       if (this.smarts.charAt(this.curChar - 1) == '(')
/*      */       {
/*  690 */         newError("Empty branch/substituent ", this.curChar + 1, "");
/*  691 */         this.brackets.pop();
/*  692 */         return;
/*      */       }
/*      */       
/*  695 */       this.prevAtom = ((IQueryAtom)this.brackets.pop());
/*  696 */       if (this.prevAtom == null)
/*  697 */         this.curComponent = 0;
/*  698 */       this.curChar += 1;
/*  699 */       break;
/*      */     case '[': 
/*  701 */       parseAtomExpression();
/*  702 */       break;
/*      */     case ']': 
/*  704 */       newError("Incorrect opening bracket ']' ", this.curChar + 1, "");
/*  705 */       break;
/*      */     case '.': 
/*  707 */       if (this.FlagCLG)
/*      */       {
/*  709 */         this.curChar += 1;
/*  710 */         this.prevAtom = null;
/*  711 */         this.curBond = null;
/*  712 */         this.curBondType = 100;
/*      */       }
/*      */       else {
/*  715 */         newError("Zero bond order (disclosure) is not allowed", this.curChar + 1, ""); }
/*  716 */       break;
/*      */     
/*      */     default: 
/*  719 */       newError("Incorrect symbol", this.curChar + 1, "");
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void parseBondExpression()
/*      */   {
/*  728 */     int lo = -1;
/*  729 */     int bo = SmartsConst.getBondCharNumber(this.smarts.charAt(this.curChar));
/*  730 */     SmartsBondExpression sbe; if (bo != -1)
/*      */     {
/*  732 */       this.curChar += 1;
/*  733 */       if (this.curChar == this.nChars)
/*      */       {
/*  735 */         newError("Smarts string ends incorrectly with a bond expression", this.curChar, "");
/*  736 */         return;
/*      */       }
/*  738 */       this.curBondType = bo;
/*      */       
/*  740 */       if (((this.curBondType == 6) || (this.curBondType == 7)) && (this.smarts.charAt(this.curChar) == '?'))
/*      */       {
/*      */ 
/*  743 */         this.curChar += 1;
/*  744 */         if (this.curChar == this.nChars)
/*      */         {
/*  746 */           newError("Smarts string ends incorrectly with a bond expression", this.curChar, "");
/*  747 */           return;
/*      */         }
/*      */         
/*  750 */         if (this.curBondType == 6) {
/*  751 */           this.curBondType = 8;
/*      */         }
/*  753 */         else if (this.curBondType == 7) {
/*  754 */           this.curBondType = 9;
/*      */         }
/*      */       }
/*  757 */       if ((SmartsConst.getBondCharNumber(this.smarts.charAt(this.curChar)) == -1) && (SmartsConst.getLogOperationCharNumber(this.smarts.charAt(this.curChar)) == -1))
/*      */       {
/*      */ 
/*  760 */         return;
/*      */       }
/*      */       
/*  763 */       this.curBond = new SmartsBondExpression();
/*  764 */       sbe = (SmartsBondExpression)this.curBond;
/*  765 */       sbe.tokens.add(new Integer(bo));
/*      */     }
/*      */     else
/*      */     {
/*  769 */       lo = SmartsConst.getLogOperationCharNumber(this.smarts.charAt(this.curChar));
/*  770 */       if (lo == 0)
/*      */       {
/*  772 */         this.curBond = new SmartsBondExpression();
/*  773 */         sbe = (SmartsBondExpression)this.curBond;
/*  774 */         sbe.tokens.add(new Integer(1000 + lo));
/*      */       }
/*      */       else
/*      */       {
/*  778 */         newError("Incorrect bond expression", this.curChar + 1, "");
/*  779 */         return;
/*      */       }
/*  781 */       this.curChar += 1;
/*      */     }
/*      */     
/*      */ 
/*  785 */     bo = SmartsConst.getBondCharNumber(this.smarts.charAt(this.curChar));
/*      */     
/*  787 */     if ((bo == 6) || (bo == 7))
/*      */     {
/*  789 */       if (this.curChar + 1 == this.nChars)
/*      */       {
/*  791 */         newError("Smarts string ends incorrectly with a bond expression", this.curChar + 1, "");
/*  792 */         return;
/*      */       }
/*  794 */       if (this.smarts.charAt(this.curChar + 1) == '?')
/*      */       {
/*  796 */         if (bo == 6) {
/*  797 */           bo = 8;
/*      */         } else
/*  799 */           bo = 9;
/*  800 */         this.curChar += 1;
/*      */       }
/*      */     }
/*      */     
/*  804 */     if (bo == -1) {
/*  805 */       lo = SmartsConst.getLogOperationCharNumber(this.smarts.charAt(this.curChar));
/*      */     } else {
/*  807 */       lo = -1;
/*      */     }
/*  809 */     while ((bo != -1) || (lo != -1))
/*      */     {
/*  811 */       int prevToken = ((Integer)sbe.tokens.lastElement()).intValue();
/*      */       
/*      */ 
/*  814 */       if (bo != -1)
/*      */       {
/*  816 */         if (prevToken < 1000)
/*      */         {
/*      */ 
/*  819 */           sbe.tokens.add(new Integer(1001));
/*      */         }
/*  821 */         sbe.tokens.add(new Integer(bo));
/*      */       }
/*      */       else
/*      */       {
/*  825 */         if (prevToken >= 1000)
/*      */         {
/*  827 */           if (lo != 0)
/*      */           {
/*  829 */             newError("Incorrect bond expression - no oprenad between logical operation", this.curChar + 1, "");
/*  830 */             return;
/*      */           }
/*      */         }
/*  833 */         sbe.tokens.add(new Integer(1000 + lo));
/*      */       }
/*      */       
/*  836 */       this.curChar += 1;
/*  837 */       if (this.curChar == this.nChars)
/*      */       {
/*  839 */         newError("Smarts string ends incorrectly with a bond expression", this.curChar, "");
/*  840 */         return;
/*      */       }
/*  842 */       bo = SmartsConst.getBondCharNumber(this.smarts.charAt(this.curChar));
/*      */       
/*  844 */       if ((bo == 6) || (bo == 7))
/*      */       {
/*  846 */         if (this.curChar + 1 == this.nChars)
/*      */         {
/*  848 */           newError("Smarts string ends incorrectly with a bond expression", this.curChar + 1, "");
/*  849 */           return;
/*      */         }
/*  851 */         if (this.smarts.charAt(this.curChar + 1) == '?')
/*      */         {
/*  853 */           if (bo == 6) {
/*  854 */             bo = 8;
/*      */           } else
/*  856 */             bo = 9;
/*  857 */           this.curChar += 1;
/*      */         }
/*      */       }
/*      */       
/*  861 */       if (bo == -1) {
/*  862 */         lo = SmartsConst.getLogOperationCharNumber(this.smarts.charAt(this.curChar));
/*      */       } else {
/*  864 */         lo = -1;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void parseAtomExpression()
/*      */   {
/*  871 */     this.curChar += 1;
/*  872 */     int openBrackets = 1;
/*  873 */     this.curAtExpr = new SmartsAtomExpression();
/*  874 */     while ((this.curChar < this.nChars) && (openBrackets > 0) && (this.errors.size() == 0))
/*      */     {
/*  876 */       if (this.smarts.charAt(this.curChar) == '[')
/*      */       {
/*  878 */         openBrackets++;
/*  879 */         this.curChar += 1;
/*      */ 
/*      */       }
/*  882 */       else if (this.smarts.charAt(this.curChar) == ']')
/*      */       {
/*  884 */         openBrackets--;
/*  885 */         this.curChar += 1;
/*      */       }
/*      */       else {
/*  888 */         parseAtomPrimitive();
/*      */       }
/*      */     }
/*  891 */     if (this.errors.size() > 0) {
/*  892 */       return;
/*      */     }
/*  894 */     if (openBrackets > 0) {
/*  895 */       newError("Incorrect atom expression - [] block is not closed", this.curChar, "");
/*      */     } else {
/*  897 */       addAtom(this.curAtExpr);
/*      */     }
/*      */   }
/*      */   
/*      */   public void testForDefaultAND() {
/*  902 */     int tok = getLastAtomToken();
/*  903 */     if ((tok >= 0) && (tok < 1000)) {
/*  904 */       this.curAtExpr.tokens.add(new SmartsExpressionToken(1001, 0));
/*      */     }
/*      */   }
/*      */   
/*      */   public int testFor2CharElement() {
/*  909 */     if (this.curChar < this.nChars - 1)
/*      */     {
/*  911 */       if (Character.isLowerCase(this.smarts.charAt(this.curChar + 1)))
/*      */       {
/*  913 */         String symbol = this.smarts.substring(this.curChar, this.curChar + 2);
/*  914 */         this.curChar += 2;
/*  915 */         int par = SmartsConst.getElementNumber(symbol);
/*  916 */         if (par == -1) {
/*  917 */           newError("Incorrect atom type in atom expression", this.curChar, "");
/*      */         } else
/*  919 */           this.curAtExpr.tokens.add(new SmartsExpressionToken(2, par));
/*  920 */         return 1;
/*      */       }
/*      */     }
/*  923 */     return 0;
/*      */   }
/*      */   
/*      */   public int getLastAtomToken()
/*      */   {
/*  928 */     if (this.curAtExpr.tokens.size() == 0)
/*  929 */       return -1;
/*  930 */     SmartsExpressionToken tok = (SmartsExpressionToken)this.curAtExpr.tokens.lastElement();
/*  931 */     return tok.type;
/*      */   }
/*      */   
/*      */   void parseAtomPrimitive()
/*      */   {
/*  936 */     if (Character.isLetter(this.smarts.charAt(this.curChar)))
/*      */     {
/*  938 */       switch (this.smarts.charAt(this.curChar))
/*      */       {
/*      */       case 'a': 
/*  941 */         testForDefaultAND();
/*  942 */         this.curAtExpr.tokens.add(new SmartsExpressionToken(1, 0));
/*  943 */         this.curChar += 1;
/*  944 */         break;
/*      */       case 'A': 
/*  946 */         parseAP_A();
/*  947 */         break;
/*      */       case 'D': 
/*  949 */         parseAP_AtomPrimitive(3, true);
/*  950 */         break;
/*      */       case 'H': 
/*  952 */         parseAP_AtomPrimitive(4, true);
/*  953 */         break;
/*      */       case 'h': 
/*  955 */         parseAP_AtomPrimitive(5, false);
/*  956 */         break;
/*      */       case 'R': 
/*  958 */         parseAP_RPrimitive(true);
/*  959 */         break;
/*      */       case 'r': 
/*  961 */         parseAP_rPrimitive(false);
/*  962 */         break;
/*      */       case 'v': 
/*  964 */         if ((this.mSupportMOEExtension) && (this.mUseMOEvPrimitive)) {
/*  965 */           parseAP_AtomPrimitive(20, false);
/*      */         } else
/*  967 */           parseAP_AtomPrimitive(8, false);
/*  968 */         break;
/*      */       case 'X': 
/*  970 */         parseAP_AtomPrimitive(9, true);
/*  971 */         break;
/*      */       case 'x': 
/*  973 */         parseAP_xPrimitive(false);
/*  974 */         break;
/*      */       case 'i': 
/*  976 */         if (this.mSupportMOEExtension) {
/*  977 */           parseAP_iPrimitive(false);
/*      */         } else
/*  979 */           parseAP_AtomSymbol();
/*  980 */         break;
/*      */       case 'q': 
/*  982 */         if (this.mSupportMOEExtension) {
/*  983 */           parseAP_xPrimitive(false);
/*      */         } else
/*  985 */           parseAP_AtomSymbol();
/*  986 */         break;
/*      */       default: 
/*  988 */         parseAP_AtomSymbol();break;
/*      */       
/*      */       }
/*      */       
/*  992 */     } else if (Character.isDigit(this.smarts.charAt(this.curChar)))
/*      */     {
/*  994 */       parseAP_AtomMass();
/*      */     }
/*      */     else
/*      */     {
/*  998 */       switch (this.smarts.charAt(this.curChar))
/*      */       {
/*      */       case ' ': 
/* 1001 */         this.curChar += 1;
/* 1002 */         break;
/*      */       case '!': 
/* 1004 */         parseAP_NOT();
/* 1005 */         break;
/*      */       case '&': 
/* 1007 */         parseAP_LogOperation(1);
/* 1008 */         break;
/*      */       case ',': 
/* 1010 */         parseAP_LogOperation(2);
/* 1011 */         break;
/*      */       case ';': 
/* 1013 */         parseAP_LogOperation(3);
/* 1014 */         break;
/*      */       case '#': 
/* 1016 */         parseAP_AtomNumber();
/* 1017 */         break;
/*      */       case '@': 
/* 1019 */         parseAP_Chirality();
/* 1020 */         break;
/*      */       case '-': 
/* 1022 */         parseAP_Charge(-1);
/* 1023 */         break;
/*      */       case '+': 
/* 1025 */         parseAP_Charge(1);
/* 1026 */         break;
/*      */       case '$': 
/* 1028 */         parseAP_RecursiveSmarts();
/* 1029 */         break;
/*      */       case '*': 
/* 1031 */         testForDefaultAND();
/* 1032 */         this.curAtExpr.tokens.add(new SmartsExpressionToken(0, 0));
/* 1033 */         this.curChar += 1;
/* 1034 */         break;
/*      */       case '^': 
/* 1036 */         if (this.mSupportOpenBabelExtension) {
/* 1037 */           parseAP_OpenBabel_Hybridization();
/*      */         }
/*      */         else {
/* 1040 */           newError("Incorrect symbol in atom expression", this.curChar + 1, "");
/* 1041 */           this.curChar += 1;
/*      */         }
/* 1043 */         break;
/*      */       case ':': 
/* 1045 */         if (this.mSupportSmirksSyntax) {
/* 1046 */           parseAP_SmirksMaping();
/*      */         }
/*      */         else {
/* 1049 */           newError("Smirks mapping is not supported!", this.curChar + 1, "");
/* 1050 */           this.curChar += 1;
/*      */         }
/* 1052 */         break;
/*      */       default: 
/* 1054 */         newError("Incorrect symbol in atom expression", this.curChar + 1, "");
/* 1055 */         this.curChar += 1;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void parseAP_AtomSymbol()
/*      */   {
/* 1062 */     testForDefaultAND();
/* 1063 */     if (Character.isLowerCase(this.smarts.charAt(this.curChar)))
/*      */     {
/* 1065 */       char symbol = Character.toUpperCase(this.smarts.charAt(this.curChar));
/* 1066 */       int par = SmartsConst.getElementNumberFromChar(symbol);
/* 1067 */       this.curChar += 1;
/* 1068 */       if (par == -1) {
/* 1069 */         newError("Incorrect aromatic atom type in atom expression", this.curChar, "");
/*      */       } else {
/* 1071 */         this.curAtExpr.tokens.add(new SmartsExpressionToken(1, par));
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1076 */       int n = 1;
/* 1077 */       if ((this.curChar < this.nChars - 1) && 
/* 1078 */         (Character.isLowerCase(this.smarts.charAt(this.curChar + 1)))) {
/* 1079 */         n = 2;
/*      */       }
/* 1081 */       String symbol = this.smarts.substring(this.curChar, this.curChar + n);
/* 1082 */       this.curChar += n;
/* 1083 */       int par = SmartsConst.getElementNumber(symbol);
/* 1084 */       if (par == -1) {
/* 1085 */         newError("Incorrect aliphatic atom type in atom expression", this.curChar, "");
/*      */       } else {
/* 1087 */         this.curAtExpr.tokens.add(new SmartsExpressionToken(2, par));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void parseAP_NOT() {
/* 1093 */     testForDefaultAND();
/* 1094 */     this.curAtExpr.tokens.add(new SmartsExpressionToken(1000, 0));
/* 1095 */     this.curChar += 1;
/*      */   }
/*      */   
/*      */   void parseAP_LogOperation(int logOp)
/*      */   {
/* 1100 */     int prevTok = getLastAtomToken();
/* 1101 */     if (prevTok < 0) {
/* 1102 */       newError("Atom expression incorrectly starts with logical opreation", this.curChar + 1, "");
/*      */ 
/*      */     }
/* 1105 */     else if (prevTok >= 1000) {
/* 1106 */       newError("Incorrect expression - missing operand", this.curChar + 1, "");
/*      */     } else {
/* 1108 */       this.curAtExpr.tokens.add(new SmartsExpressionToken(1000 + logOp, 0));
/*      */     }
/* 1110 */     this.curChar += 1;
/*      */   }
/*      */   
/*      */   void parseAP_AtomMass()
/*      */   {
/* 1115 */     testForDefaultAND();
/* 1116 */     int mass = getInteger();
/* 1117 */     this.curAtExpr.tokens.add(new SmartsExpressionToken(13, mass));
/*      */   }
/*      */   
/*      */   void parseAP_A()
/*      */   {
/* 1122 */     testForDefaultAND();
/* 1123 */     if (testFor2CharElement() == 1)
/* 1124 */       return;
/* 1125 */     this.curAtExpr.tokens.add(new SmartsExpressionToken(2, 0));
/* 1126 */     this.curChar += 1;
/*      */   }
/*      */   
/*      */ 
/*      */   void parseAP_AtomPrimitive(int logOpType, boolean elTest)
/*      */   {
/* 1132 */     testForDefaultAND();
/* 1133 */     if ((elTest) && 
/* 1134 */       (testFor2CharElement() == 1))
/* 1135 */       return;
/* 1136 */     int symbolPos = this.curChar;
/* 1137 */     this.curChar += 1;
/* 1138 */     int par = getInteger();
/* 1139 */     if (par == -1)
/*      */     {
/* 1141 */       if ((logOpType == 4) && 
/* 1142 */         (isHydrogenAtom(symbolPos)))
/*      */       {
/*      */ 
/*      */ 
/* 1146 */         this.curAtExpr.tokens.add(new SmartsExpressionToken(2, 1));
/* 1147 */         return;
/*      */       }
/* 1149 */       par = 1;
/*      */     }
/* 1151 */     this.curAtExpr.tokens.add(new SmartsExpressionToken(logOpType, par));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean isHydrogenAtom(int symbolPos)
/*      */   {
/* 1161 */     char prevCh = this.smarts.charAt(symbolPos - 1);
/* 1162 */     if (prevCh == '*')
/* 1163 */       return false;
/* 1164 */     if (prevCh == '[')
/* 1165 */       return true;
/* 1166 */     if (prevCh == '&')
/*      */     {
/* 1168 */       if (this.curAtExpr.tokens.size() > 0)
/*      */       {
/* 1170 */         SmartsExpressionToken tok = (SmartsExpressionToken)this.curAtExpr.tokens.lastElement();
/* 1171 */         if (tok.type == 13) {
/* 1172 */           return true;
/*      */         }
/*      */       }
/*      */     }
/* 1176 */     return false;
/*      */   }
/*      */   
/*      */   void parseAP_RPrimitive(boolean elTest)
/*      */   {
/* 1181 */     testForDefaultAND();
/* 1182 */     if ((elTest) && 
/* 1183 */       (testFor2CharElement() == 1))
/* 1184 */       return;
/* 1185 */     this.curChar += 1;
/* 1186 */     int par = getInteger();
/* 1187 */     this.curAtExpr.tokens.add(new SmartsExpressionToken(6, par));
/*      */   }
/*      */   
/*      */   void parseAP_rPrimitive(boolean elTest)
/*      */   {
/* 1192 */     testForDefaultAND();
/* 1193 */     if ((elTest) && 
/* 1194 */       (testFor2CharElement() == 1))
/* 1195 */       return;
/* 1196 */     this.curChar += 1;
/* 1197 */     int par = getInteger();
/* 1198 */     if (par == -1)
/*      */     {
/* 1200 */       par = 1;
/*      */ 
/*      */ 
/*      */     }
/* 1204 */     else if (par < 3) {
/* 1205 */       newError("Incorrect integer value for r-primitive!", this.curChar, "");
/*      */     }
/* 1207 */     this.curAtExpr.tokens.add(new SmartsExpressionToken(7, par));
/*      */   }
/*      */   
/*      */   void parseAP_xPrimitive(boolean elTest)
/*      */   {
/* 1212 */     testForDefaultAND();
/* 1213 */     if ((elTest) && 
/* 1214 */       (testFor2CharElement() == 1))
/* 1215 */       return;
/* 1216 */     this.curChar += 1;
/* 1217 */     int par = getInteger();
/* 1218 */     if (par == -1)
/*      */     {
/*      */ 
/*      */ 
/* 1222 */       par = -1;
/*      */     }
/* 1224 */     this.curAtExpr.tokens.add(new SmartsExpressionToken(15, par));
/*      */   }
/*      */   
/*      */   void parseAP_iPrimitive(boolean elTest)
/*      */   {
/* 1229 */     testForDefaultAND();
/* 1230 */     this.curChar += 1;
/* 1231 */     int par = 0;
/* 1232 */     this.curAtExpr.tokens.add(new SmartsExpressionToken(16, par));
/*      */   }
/*      */   
/*      */   void parseAP_AtomNumber()
/*      */   {
/* 1237 */     testForDefaultAND();
/* 1238 */     this.curChar += 1;
/*      */     
/* 1240 */     if (this.mSupportMOEExtension)
/*      */     {
/* 1242 */       if (this.curChar < this.nChars)
/*      */       {
/* 1244 */         if (parseMOEExpression()) {
/* 1245 */           return;
/*      */         }
/*      */       }
/*      */     }
/* 1249 */     int par = getInteger();
/* 1250 */     if (par == -1) {
/* 1251 */       newError("Incorrect atomic number after #", this.curChar, "");
/*      */ 
/*      */     }
/* 1254 */     else if ((par < 1) || (par >= SmartsConst.elSymbols.length)) {
/* 1255 */       newError("Incorrect atomic number after #", this.curChar, "");
/*      */     } else {
/* 1257 */       this.curAtExpr.tokens.add(new SmartsExpressionToken(11, par));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   void parseAP_OpenBabel_Hybridization()
/*      */   {
/* 1264 */     testForDefaultAND();
/* 1265 */     this.curChar += 1;
/*      */     
/* 1267 */     int par = getInteger();
/* 1268 */     if (par == -1) {
/* 1269 */       newError("Missing hybridization parameter after ^", this.curChar, "");
/*      */ 
/*      */     }
/* 1272 */     else if ((par < 1) || (par > 3)) {
/* 1273 */       newError("Incorrect hybridization after ^", this.curChar, "");
/*      */     } else {
/* 1275 */       this.curAtExpr.tokens.add(new SmartsExpressionToken(21, par));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   void parseAP_SmirksMaping()
/*      */   {
/* 1282 */     this.curChar += 1;
/*      */     
/* 1284 */     int par = getInteger();
/* 1285 */     if (par == -1) {
/* 1286 */       newError("Missing Smirks Mapping index after ", this.curChar, "");
/*      */ 
/*      */     }
/* 1289 */     else if ((par < 0) || (par > 10000)) {
/* 1290 */       newError("Incorrect Smirks Mapping index ", this.curChar, "");
/*      */ 
/*      */ 
/*      */     }
/* 1294 */     else if (this.curSmirksMapIndex > 0)
/*      */     {
/* 1296 */       newError("Smirks Mapping index is specified more than once per atom ", this.curChar, "");
/*      */     }
/*      */     else
/*      */     {
/* 1300 */       this.curSmirksMapIndex = par;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   boolean parseMOEExpression()
/*      */   {
/* 1308 */     if (this.smarts.charAt(this.curChar) == 'G')
/*      */     {
/* 1310 */       this.curChar += 1;
/* 1311 */       int par = getInteger();
/* 1312 */       if (par == -1) {
/* 1313 */         newError("Incorrect atomic number after #G", this.curChar, "");
/*      */ 
/*      */       }
/* 1316 */       else if ((par < 1) || (par > 8)) {
/* 1317 */         newError("Incorrect atomic number after #G", this.curChar, "");
/*      */       } else {
/* 1319 */         this.curAtExpr.tokens.add(new SmartsExpressionToken(17, par));
/*      */       }
/*      */       
/* 1322 */       return true;
/*      */     }
/*      */     
/* 1325 */     if (this.smarts.charAt(this.curChar) == 'N')
/*      */     {
/* 1327 */       this.curChar += 1;
/* 1328 */       this.curAtExpr.tokens.add(new SmartsExpressionToken(19, 0));
/* 1329 */       return true;
/*      */     }
/*      */     
/* 1332 */     if (this.smarts.charAt(this.curChar) == 'X')
/*      */     {
/* 1334 */       this.curChar += 1;
/* 1335 */       this.curAtExpr.tokens.add(new SmartsExpressionToken(18, 0));
/* 1336 */       return true;
/*      */     }
/*      */     
/* 1339 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   void parseAP_Chirality()
/*      */   {
/* 1345 */     testForDefaultAND();
/* 1346 */     this.curChar += 1;
/* 1347 */     int par = 2;
/* 1348 */     if (this.smarts.charAt(this.curChar) == '@') {
/* 1349 */       this.curChar += 1;
/*      */     } else {
/* 1351 */       par = 1;
/*      */     }
/* 1353 */     this.curAtExpr.tokens.add(new SmartsExpressionToken(12, par));
/*      */   }
/*      */   
/*      */   void parseAP_Charge(int sign)
/*      */   {
/* 1358 */     testForDefaultAND();
/* 1359 */     this.curChar += 1;
/* 1361 */     char ch; if (sign < 0) {
/* 1362 */       ch = '-';
/*      */     } else
/* 1364 */       ch = '+';
/* 1365 */     int num = 1;
/* 1366 */     while (this.curChar < this.nChars)
/*      */     {
/* 1368 */       if (this.smarts.charAt(this.curChar) != ch)
/*      */         break;
/* 1370 */       num++;
/* 1371 */       this.curChar += 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1377 */     if (num > 1) {
/* 1378 */       this.curAtExpr.tokens.add(new SmartsExpressionToken(10, sign * num));
/*      */ 
/*      */     }
/* 1381 */     else if (this.curChar < this.nChars)
/*      */     {
/* 1383 */       if (Character.isDigit(this.smarts.charAt(this.curChar)))
/*      */       {
/* 1385 */         int par = getInteger();
/* 1386 */         if (par == -1) {
/* 1387 */           newError("Incorrect charge ", this.curChar, "");
/*      */         } else {
/* 1389 */           this.curAtExpr.tokens.add(new SmartsExpressionToken(10, sign * par));
/*      */         }
/*      */       } else {
/* 1392 */         this.curAtExpr.tokens.add(new SmartsExpressionToken(10, sign));
/*      */       }
/*      */     } else {
/* 1395 */       this.curAtExpr.tokens.add(new SmartsExpressionToken(10, sign));
/*      */     }
/*      */   }
/*      */   
/*      */   public void parseAP_RecursiveSmarts()
/*      */   {
/* 1401 */     this.curChar += 1;
/* 1402 */     if (this.curChar >= this.nChars)
/*      */     {
/* 1404 */       newError("Incorrect recursive smarts", this.curChar, "");
/* 1405 */       return;
/*      */     }
/*      */     
/* 1408 */     if (this.smarts.charAt(this.curChar) != '(')
/*      */     {
/* 1410 */       newError("Incorrect recursive smarts", this.curChar + 1, "");
/* 1411 */       return;
/*      */     }
/*      */     
/* 1414 */     this.curChar += 1;
/* 1415 */     int openBrackets = 1;
/* 1416 */     int firstChar = this.curChar;
/* 1417 */     while ((this.curChar < this.nChars) && (openBrackets > 0))
/*      */     {
/* 1419 */       if (this.smarts.charAt(this.curChar) == '(') {
/* 1420 */         openBrackets++;
/*      */       }
/* 1422 */       else if (this.smarts.charAt(this.curChar) == ')')
/* 1423 */         openBrackets--;
/* 1424 */       this.curChar += 1;
/*      */     }
/*      */     
/* 1427 */     if ((this.curChar >= this.nChars) && (openBrackets > 0))
/*      */     {
/* 1429 */       newError("Incorrect recursive smarts. String end is reached within $(expression)", this.curChar, "");
/* 1430 */       return;
/*      */     }
/*      */     
/* 1433 */     if (firstChar == this.curChar - 1)
/* 1434 */       newError("Empty recursive smarts", this.curChar, "");
/* 1435 */     this.curAtExpr.tokens.add(new SmartsExpressionToken(14, this.curAtExpr.recSmartsStrings.size()));
/* 1436 */     this.curAtExpr.recSmartsStrings.add(this.smarts.substring(firstChar, this.curChar - 1));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int getAbsoluteChirality(IAtom atom, int relChirality)
/*      */   {
/* 1448 */     List ca = this.container.getConnectedAtomsList(atom);
/* 1449 */     if (ca.size() != 4) {
/* 1450 */       return 0;
/*      */     }
/* 1452 */     int[][] atNCode = new int[4][];
/* 1453 */     for (int i = 0; i < 4; i++)
/*      */     {
/* 1455 */       atNCode[i] = getAtomNeighbourCode(atom, (IAtom)ca.get(i));
/* 1456 */       if (atNCode[i] == null)
/* 1457 */         return 0;
/*      */     }
/*      */     boolean FlagClockWise;
/* 1461 */     if (relChirality == 2) {
/* 1462 */       FlagClockWise = true;
/*      */     } else {
/* 1464 */       FlagClockWise = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1470 */     for (int i = 2; i >= 0; i--) {
/* 1471 */       for (int j = 0; j <= i; j++)
/*      */       {
/* 1473 */         if (compareNeighbourCodes(atNCode[j], atNCode[(j + 1)]) > 0)
/*      */         {
/* 1475 */           FlagClockWise = !FlagClockWise;
/*      */           
/* 1477 */           int[] temp = atNCode[j];
/* 1478 */           atNCode[j] = atNCode[(j + 1)];
/* 1479 */           atNCode[(j + 1)] = temp;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1490 */     if (FlagClockWise == true) {
/* 1491 */       return 1001;
/*      */     }
/* 1493 */     return 1002;
/*      */   }
/*      */   
/*      */   int compareNeighbourCodes(int[] atCode1, int[] atCode2) {
/*      */     int n;
/* 1499 */     if (atCode1.length < atCode2.length) {
/* 1500 */       n = atCode1.length;
/*      */     } else {
/* 1502 */       n = atCode2.length;
/*      */     }
/* 1504 */     for (int i = 0; i < n; i++)
/*      */     {
/* 1506 */       if (atCode1[i] < atCode2[i]) {
/* 1507 */         return -1;
/*      */       }
/* 1509 */       if (atCode1[i] > atCode2[i]) {
/* 1510 */         return 1;
/*      */       }
/*      */     }
/*      */     
/* 1514 */     if (atCode1.length < atCode2.length) {
/* 1515 */       return -1;
/*      */     }
/* 1517 */     if (atCode1.length > atCode2.length) {
/* 1518 */       return 1;
/*      */     }
/* 1520 */     return 0;
/*      */   }
/*      */   
/*      */   int getAtomType(IAtom atom)
/*      */   {
/* 1525 */     if ((atom instanceof SmartsAtomExpression))
/*      */     {
/* 1527 */       SmartsAtomExpression at = (SmartsAtomExpression)atom;
/*      */       
/* 1529 */       for (int i = 0; i < at.tokens.size(); i++)
/*      */       {
/* 1531 */         SmartsExpressionToken tok = (SmartsExpressionToken)at.tokens.get(i);
/* 1532 */         if (tok.type == 0) {
/* 1533 */           return -1;
/*      */         }
/* 1535 */         if ((tok.type == 2) || (tok.type == 1) || (tok.type == 11))
/*      */         {
/*      */ 
/*      */ 
/* 1539 */           if ((i > 0) && 
/* 1540 */             (((SmartsExpressionToken)at.tokens.get(i - 1)).type == 1000))
/*      */           {
/* 1542 */             return -1;
/*      */           }
/* 1544 */           if (tok.param > 0) {
/* 1545 */             return tok.param;
/*      */           }
/* 1547 */           return -1;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1552 */     if (((atom instanceof AliphaticSymbolQueryAtom)) || ((atom instanceof AromaticSymbolQueryAtom)))
/*      */     {
/*      */ 
/* 1555 */       return SmartsConst.getElementNumber(atom.getSymbol());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1562 */     return -1;
/*      */   }
/*      */   
/*      */   static int getBondType(IBond.Order order)
/*      */   {
/* 1567 */     if (order == IBond.Order.SINGLE)
/* 1568 */       return 1;
/* 1569 */     if (order == IBond.Order.DOUBLE)
/* 1570 */       return 2;
/* 1571 */     if (order == IBond.Order.TRIPLE)
/* 1572 */       return 3;
/* 1573 */     return 1;
/*      */   }
/*      */   
/*      */   int getBondType(IBond bond)
/*      */   {
/* 1578 */     if ((bond instanceof OrderQueryBond))
/* 1579 */       return getBondType(bond.getOrder());
/* 1580 */     if ((bond instanceof SingleOrAromaticBond))
/* 1581 */       return 1;
/* 1582 */     if ((bond instanceof DoubleNonAromaticBond))
/* 1583 */       return 2;
/* 1584 */     if ((bond instanceof DoubleBondAromaticityNotSpecified))
/* 1585 */       return 2;
/* 1586 */     if ((bond instanceof DoubleStereoBond))
/* 1587 */       return 2;
/* 1588 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */   public int[] getAtomNeighbourCode(IAtom center, IAtom neighAtom)
/*      */   {
/* 1594 */     Vector<Integer> code = new Vector();
/* 1595 */     Vector<IAtom> usedAtoms = new Vector();
/* 1596 */     Vector<IAtom> layer = new Vector();
/*      */     
/*      */ 
/* 1599 */     int atType = getAtomType(neighAtom);
/* 1600 */     if (atType == -1)
/* 1601 */       return null;
/* 1602 */     code.add(new Integer(atType));
/* 1603 */     usedAtoms.add(center);
/* 1604 */     usedAtoms.add(neighAtom);
/* 1605 */     layer.add(neighAtom);
/*      */     
/* 1607 */     Vector<IAtom> nextLayer = addLayerToCode(code, layer, usedAtoms);
/* 1608 */     layer = nextLayer;
/* 1609 */     nextLayer = addLayerToCode(code, layer, usedAtoms);
/* 1610 */     int[] result = new int[code.size()];
/* 1611 */     for (int i = 0; i < result.length; i++)
/* 1612 */       result[i] = ((Integer)code.get(i)).intValue();
/* 1613 */     return result;
/*      */   }
/*      */   
/*      */   Vector<IAtom> addLayerToCode(Vector<Integer> code, Vector<IAtom> layer, Vector<IAtom> usedAtoms)
/*      */   {
/* 1618 */     if (layer == null) {
/* 1619 */       return null;
/*      */     }
/* 1621 */     Vector<IAtom> nextLayer = new Vector();
/* 1622 */     Vector<Integer> atTypes = new Vector();
/* 1623 */     Vector<Integer> boTypes = new Vector();
/*      */     
/* 1625 */     for (int i = 0; i < layer.size(); i++)
/*      */     {
/* 1627 */       List ca = this.container.getConnectedAtomsList((IAtom)layer.get(i));
/* 1628 */       for (int k = 0; k < ca.size(); k++)
/*      */       {
/* 1630 */         IAtom at = (IAtom)ca.get(k);
/* 1631 */         if (!isAtomUsed(at, usedAtoms))
/*      */         {
/* 1633 */           int atType = getAtomType(at);
/* 1634 */           if (atType == -1)
/* 1635 */             return null;
/* 1636 */           IBond bo = this.container.getBond((IAtom)layer.get(i), at);
/* 1637 */           int boType = getBondType(bo);
/* 1638 */           if (boType == -1)
/* 1639 */             return null;
/* 1640 */           usedAtoms.add(at);
/* 1641 */           nextLayer.add(at);
/* 1642 */           atTypes.add(new Integer(atType));
/* 1643 */           boTypes.add(new Integer(boType));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1648 */     int n = atTypes.size();
/*      */     
/*      */ 
/* 1651 */     for (int i = n - 2; i >= 0; i--) {
/* 1652 */       for (int k = 0; k <= i; k++)
/*      */       {
/* 1654 */         if (((Integer)atTypes.get(k)).intValue() < ((Integer)atTypes.get(k + 1)).intValue())
/*      */         {
/* 1656 */           Integer tmp = Integer.valueOf(((Integer)atTypes.get(k)).intValue());
/* 1657 */           atTypes.set(k, Integer.valueOf(((Integer)atTypes.get(k + 1)).intValue()));
/* 1658 */           atTypes.set(k + 1, tmp);
/* 1659 */           tmp = Integer.valueOf(((Integer)boTypes.get(k)).intValue());
/* 1660 */           boTypes.set(k, Integer.valueOf(((Integer)boTypes.get(k + 1)).intValue()));
/* 1661 */           boTypes.set(k + 1, tmp);
/*      */         }
/*      */       }
/*      */     }
/* 1665 */     for (int i = 0; i < n; i++)
/* 1666 */       code.add(atTypes.get(i));
/* 1667 */     for (int i = 0; i < n; i++) {
/* 1668 */       code.add(boTypes.get(i));
/*      */     }
/* 1670 */     return nextLayer;
/*      */   }
/*      */   
/*      */   boolean isAtomUsed(IAtom atom, Vector<IAtom> v)
/*      */   {
/* 1675 */     for (int i = 0; i < v.size(); i++)
/* 1676 */       if (atom == v.get(i))
/* 1677 */         return true;
/* 1678 */     return false;
/*      */   }
/*      */   
/*      */   void convertChirality(SmartsAtomExpression atom)
/*      */   {
/* 1683 */     for (int i = 0; i < atom.tokens.size(); i++)
/*      */     {
/* 1685 */       SmartsExpressionToken tok = (SmartsExpressionToken)atom.tokens.get(i);
/* 1686 */       if (tok.type == 12)
/*      */       {
/* 1688 */         tok.param = getAbsoluteChirality(atom, tok.param);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   void setDoubleBondsStereoInfo()
/*      */   {
/* 1696 */     this.processedDirBonds.clear();
/* 1697 */     this.processedDoubleBonds.clear();
/* 1698 */     this.newStereoDoubleBonds.clear();
/* 1699 */     for (int i = 0; i < this.directionalBonds.size(); i++)
/*      */     {
/* 1701 */       SMARTSBond dirBond = (SMARTSBond)this.directionalBonds.get(i);
/*      */       
/* 1703 */       if (!isBondProcessed(dirBond, this.processedDirBonds))
/*      */       {
/*      */ 
/* 1706 */         IAtom at0 = dirBond.getAtom(0);
/* 1707 */         IAtom at1 = dirBond.getAtom(1);
/* 1708 */         if (isAtomForStereoDoubleBond(at0))
/*      */         {
/* 1710 */           SMARTSBond dBo = getNeighborDoubleBond(dirBond, 0);
/* 1711 */           if ((dBo != null) && 
/* 1712 */             (!isBondProcessed(dBo, this.processedDoubleBonds)))
/* 1713 */             setDoubleStereoBond(dBo, at0, at1, dirBond, ((Integer)this.directions.get(i)).intValue(), 0);
/*      */         }
/* 1715 */         if (isAtomForStereoDoubleBond(at1))
/*      */         {
/* 1717 */           SMARTSBond dBo = getNeighborDoubleBond(dirBond, 1);
/* 1718 */           if ((dBo != null) && 
/* 1719 */             (!isBondProcessed(dBo, this.processedDoubleBonds)))
/* 1720 */             setDoubleStereoBond(dBo, at1, at0, dirBond, ((Integer)this.directions.get(i)).intValue(), 1);
/*      */         }
/* 1722 */         this.processedDirBonds.add(dirBond);
/*      */       }
/*      */     }
/*      */     
/* 1726 */     for (int i = 0; i < this.processedDoubleBonds.size(); i++)
/*      */     {
/* 1728 */       this.container.removeBond((IBond)this.processedDoubleBonds.get(i));
/* 1729 */       this.container.addBond((IBond)this.newStereoDoubleBonds.get(i));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   boolean isDirectionalBond(SMARTSBond bond)
/*      */   {
/* 1736 */     for (int i = 0; i < this.directionalBonds.size(); i++)
/* 1737 */       if (this.directionalBonds.get(i) == bond)
/* 1738 */         return true;
/* 1739 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   boolean isAtomForStereoDoubleBond(IAtom atom)
/*      */   {
/* 1745 */     if (atom.getSymbol().equals("C"))
/* 1746 */       return true;
/* 1747 */     if (atom.getSymbol().equals("N"))
/* 1748 */       return true;
/* 1749 */     if (atom.getSymbol().equals("P"))
/* 1750 */       return true;
/* 1751 */     if (atom.getSymbol().equals("Si"))
/* 1752 */       return true;
/* 1753 */     return false;
/*      */   }
/*      */   
/*      */   boolean isBondProcessed(SMARTSBond bond, Vector<SMARTSBond> processedBonds)
/*      */   {
/* 1758 */     for (IQueryBond bo : processedBonds)
/*      */     {
/* 1760 */       if (bo == bond)
/* 1761 */         return true;
/*      */     }
/* 1763 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   SMARTSBond getNeighborDoubleBond(SMARTSBond bond, int atNum)
/*      */   {
/* 1769 */     IAtom at = bond.getAtom(atNum);
/* 1770 */     List ca = this.container.getConnectedAtomsList(at);
/* 1771 */     for (int k = 0; k < ca.size(); k++)
/*      */     {
/* 1773 */       IBond bo = this.container.getBond(at, (IAtom)ca.get(k));
/* 1774 */       if ((bo != bond) && 
/* 1775 */         ((bo instanceof OrderQueryBond)))
/*      */       {
/* 1777 */         if (bo.getOrder() == IBond.Order.DOUBLE)
/* 1778 */           return (SMARTSBond)bo;
/*      */       }
/*      */     }
/* 1781 */     return null;
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
/*      */ 
/*      */ 
/*      */   void setDoubleStereoBond(SMARTSBond doubleBond, IAtom atom, IAtom at0, SMARTSBond dirBond, int direction, int atomPos)
/*      */   {
/* 1813 */     IAtom at1 = null;
/* 1814 */     IAtom at2 = null;
/* 1815 */     IAtom at3 = null;
/* 1816 */     IAtom atom1; if (doubleBond.getAtom(0) == atom) {
/* 1817 */       atom1 = doubleBond.getAtom(1);
/*      */     } else {
/* 1819 */       atom1 = doubleBond.getAtom(0);
/*      */     }
/* 1821 */     List ca = this.container.getConnectedAtomsList(atom);
/* 1822 */     for (int k = 0; k < ca.size(); k++)
/*      */     {
/* 1824 */       if ((ca.get(k) != at0) && (ca.get(k) != atom1))
/*      */       {
/* 1826 */         at1 = (IAtom)ca.get(k);
/* 1827 */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1833 */     boolean FlagDir2 = false;
/* 1834 */     boolean FlagDir3 = false;
/* 1835 */     ca = this.container.getConnectedAtomsList(atom1);
/* 1836 */     for (int k = 0; k < ca.size(); k++)
/*      */     {
/* 1838 */       IAtom otherAt = (IAtom)ca.get(k);
/* 1839 */       if (otherAt != atom)
/*      */       {
/* 1841 */         IBond bo = this.container.getBond(atom1, otherAt);
/* 1842 */         if (at2 == null)
/*      */         {
/* 1844 */           FlagDir2 = isDirectionalBond((SMARTSBond)bo);
/* 1845 */           at2 = otherAt;
/*      */         }
/*      */         else
/*      */         {
/* 1849 */           if (at3 != null)
/*      */             break;
/* 1851 */           FlagDir3 = isDirectionalBond((SMARTSBond)bo);
/* 1852 */           at3 = otherAt;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1861 */     if ((!FlagDir2) && (!FlagDir3)) {
/* 1862 */       return;
/*      */     }
/*      */     
/* 1865 */     int[] pAt0 = getAtomNeighbourCode(atom, at0);
/*      */     
/*      */     int[] pAt1;
/*      */     
/* 1869 */     if (at1 == null)
/*      */     {
/* 1871 */       pAt1 = new int[1];
/* 1872 */       pAt1[0] = 1;
/*      */     }
/*      */     else {
/* 1875 */       pAt1 = getAtomNeighbourCode(atom, at1); }
/*      */     int[] pAt2;
/* 1877 */     if (at2 == null)
/*      */     {
/* 1879 */       pAt2 = new int[1];
/* 1880 */       pAt2[0] = 1;
/*      */     }
/*      */     else {
/* 1883 */       pAt2 = getAtomNeighbourCode(atom1, at2); }
/*      */     int[] pAt3;
/* 1885 */     if (at3 == null)
/*      */     {
/* 1887 */        pAt3 = new int[1];
/* 1888 */       pAt3[0] = 1;
/*      */     }
/*      */     else {
/* 1891 */       pAt3 = getAtomNeighbourCode(atom1, at3);
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1910 */     int direction2 = 0;
/*      */     SMARTSBond dirBond2;
/* 1913 */     if (FlagDir2) {
/* 1914 */       dirBond2 = (SMARTSBond)this.container.getBond(atom1, at2);
/*      */     } else
/* 1916 */       dirBond2 = (SMARTSBond)this.container.getBond(atom1, at3);
/* 1917 */     for (int i = 0; i < this.directions.size(); i++)
/*      */     {
/* 1919 */       if (this.directionalBonds.get(i) == dirBond2)
/*      */       {
/* 1921 */         direction2 = ((Integer)this.directions.get(i)).intValue();
/* 1922 */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1930 */     boolean isUp = (direction == 6) || (direction == 8);
/* 1931 */     boolean isUp2 = (direction2 == 6) || (direction2 == 8);
/* 1932 */     boolean isCis = isUp == isUp2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1938 */     if (this.container.getAtomNumber(atom) > this.container.getAtomNumber(at0))
/* 1939 */       isCis = !isCis;
/* 1940 */     if (FlagDir2)
/*      */     {
/* 1942 */       if (this.container.getAtomNumber(atom1) > this.container.getAtomNumber(at2)) {
/* 1943 */         isCis = !isCis;
/*      */       }
/*      */       
/*      */     }
/* 1947 */     else if (this.container.getAtomNumber(atom1) > this.container.getAtomNumber(at3)) {
/* 1948 */       isCis = !isCis;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1954 */     if (compareNeighbourCodes(pAt0, pAt1) < 0)
/* 1955 */       isCis = !isCis;
/* 1956 */     if (FlagDir2)
/*      */     {
/*      */ 
/* 1959 */       if (compareNeighbourCodes(pAt2, pAt3) < 0) {
/* 1960 */         isCis = !isCis;
/*      */       }
/*      */       
/*      */ 
/*      */     }
/* 1965 */     else if (compareNeighbourCodes(pAt3, pAt2) < 0) {
/* 1966 */       isCis = !isCis;
/*      */     }
/*      */     
/* 1969 */     boolean isUnspecified = (direction == 8) || (direction == 9) || (direction2 == 8) || (direction2 == 9);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1974 */     DoubleStereoBond stereoBond = new DoubleStereoBond();
/* 1975 */     stereoBond.setAtom(atom, 0);
/* 1976 */     stereoBond.setAtom(atom1, 1);
/* 1977 */     if (isCis)
/*      */     {
/* 1979 */       if (isUnspecified) {
/* 1980 */         stereoBond.stereoParameter = 11;
/*      */       } else {
/* 1982 */         stereoBond.stereoParameter = 10;
/*      */       }
/*      */       
/*      */     }
/* 1986 */     else if (isUnspecified) {
/* 1987 */       stereoBond.stereoParameter = 13;
/*      */     } else {
/* 1989 */       stereoBond.stereoParameter = 12;
/*      */     }
/* 1991 */     this.processedDoubleBonds.add(doubleBond);
/* 1992 */     this.newStereoDoubleBonds.add(stereoBond);
/*      */     
/*      */ 
/* 1995 */     this.processedDirBonds.add(dirBond2);
/*      */   }
/*      */   
/*      */   public void setSMARTSData(IAtomContainer container)
/*      */     throws Exception
/*      */   {
/* 2001 */     prepareTargetForSMARTSSearch(this.mNeedNeighbourData, this.mNeedValencyData, this.mNeedRingData, this.mNeedRingData2, this.mNeedExplicitHData, this.mNeedParentMoleculeData, container);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void prepareTargetForSMARTSSearch(boolean neighbourData, boolean valenceData, boolean ringData, boolean ringData2, boolean explicitHData, boolean parentMoleculeData, IAtomContainer container)
/*      */     throws Exception
/*      */   {
/* 2010 */     if (neighbourData) {
/* 2011 */       setNeighbourData(container);
/*      */     }
/* 2013 */     if (valenceData) {
/* 2014 */       setValenceData(container);
/*      */     }
/* 2016 */     if ((ringData) || (ringData2)) {
/* 2017 */       setRingData(container, ringData, ringData2);
/*      */     }
/* 2019 */     if (explicitHData) {
/* 2020 */       setExplicitHAtomData(container);
/*      */     }
/* 2022 */     if (parentMoleculeData) {
/* 2023 */       setParentMoleculeData(container);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void prepareTargetForSMARTSSearch(SmartsFlags flags, IAtomContainer container) {
/* 2028 */     if (flags.mNeedNeighbourData) {
/* 2029 */       setNeighbourData(container);
/*      */     }
/* 2031 */     if (flags.mNeedValenceData) {
/* 2032 */       setValenceData(container);
/*      */     }
/* 2034 */     if ((flags.mNeedRingData) || (flags.mNeedRingData2)) {
/* 2035 */       setRingData(container, flags.mNeedRingData, flags.mNeedRingData2);
/*      */     }
/* 2037 */     if (flags.mNeedExplicitHData) {
/* 2038 */       setExplicitHAtomData(container);
/*      */     }
/* 2040 */     if (flags.mNeedParentMoleculeData) {
/* 2041 */       setParentMoleculeData(container);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void setNeighbourData(IAtomContainer container)
/*      */   {
/* 2047 */     IBond bond = null;
/* 2048 */     for (int i = 0; i < container.getAtomCount(); i++)
/*      */     {
/* 2050 */       IAtom at = container.getAtom(i);
/* 2051 */       at.setFormalNeighbourCount(Integer.valueOf(0));
/*      */     }
/*      */     
/* 2054 */     for (int f = 0; f < container.getBondCount(); f++)
/*      */     {
/* 2056 */       bond = container.getBond(f);
/* 2057 */       IAtom at1 = bond.getAtom(0);
/* 2058 */       IAtom at2 = bond.getAtom(1);
/* 2059 */       at1.setFormalNeighbourCount(Integer.valueOf(at1.getFormalNeighbourCount().intValue() + 1));
/* 2060 */       at2.setFormalNeighbourCount(Integer.valueOf(at2.getFormalNeighbourCount().intValue() + 1));
/*      */     }
/*      */   }
/*      */   
/*      */   public static void setValenceData(IAtomContainer container)
/*      */   {
/* 2066 */     IBond bond = null;
/*      */     
/*      */ 
/* 2069 */     for (int i = 0; i < container.getAtomCount(); i++)
/*      */     {
/* 2071 */       IAtom at = container.getAtom(i);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2076 */       Integer hci = at.getImplicitHydrogenCount();
/* 2077 */       int hc = 0;
/* 2078 */       if (hci != null) {
/* 2079 */         hc = hci.intValue();
/*      */       }
/* 2081 */       at.setValency(Integer.valueOf(hc));
/*      */     }
/*      */     
/* 2084 */     for (int f = 0; f < container.getBondCount(); f++)
/*      */     {
/* 2086 */       bond = container.getBond(f);
/* 2087 */       IAtom at1 = bond.getAtom(0);
/* 2088 */       IAtom at2 = bond.getAtom(1);
/* 2089 */       at1.setValency(Integer.valueOf(at1.getValency().intValue() + getBondType(bond.getOrder())));
/* 2090 */       at2.setValency(Integer.valueOf(at2.getValency().intValue() + getBondType(bond.getOrder())));
/*      */     }
/*      */   }
/*      */   
/*      */   public static void setExplicitHAtomData(IAtomContainer container)
/*      */   {
/* 2096 */     for (int i = 0; i < container.getAtomCount(); i++) {
/* 2097 */       container.getAtom(i).removeProperty("ExplicitH");
/*      */     }
/* 2099 */     for (int i = 0; i < container.getBondCount(); i++)
/*      */     {
/* 2101 */       IBond bo = container.getBond(i);
/* 2102 */       if (bo.getAtom(0).getSymbol().equals("H"))
/*      */       {
/* 2104 */         IAtom at = bo.getAtom(1);
/* 2105 */         Integer ha = (Integer)at.getProperty("ExplicitH");
/* 2106 */         if (ha == null) {
/* 2107 */           at.setProperty("ExplicitH", new Integer(1));
/*      */         } else
/* 2109 */           at.setProperty("ExplicitH", new Integer(1 + ha.intValue()));
/*      */       }
/* 2111 */       if (bo.getAtom(1).getSymbol().equals("H"))
/*      */       {
/* 2113 */         IAtom at = bo.getAtom(0);
/* 2114 */         Integer ha = (Integer)at.getProperty("ExplicitH");
/* 2115 */         if (ha == null) {
/* 2116 */           at.setProperty("ExplicitH", new Integer(1));
/*      */         } else {
/* 2118 */           at.setProperty("ExplicitH", new Integer(1 + ha.intValue()));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static int[] getExplicitHAtomData(IAtomContainer container) {
/* 2125 */     int[] numH = new int[container.getAtomCount()];
/* 2126 */     for (int i = 0; i < numH.length; i++) {
/* 2127 */       numH[0] = 0;
/*      */     }
/* 2129 */     for (int i = 0; i < container.getBondCount(); i++)
/*      */     {
/* 2131 */       IBond bo = container.getBond(i);
/* 2132 */       if (bo.getAtom(0).getSymbol().equals("H"))
/*      */       {
/* 2134 */         IAtom at = bo.getAtom(1);
/* 2135 */         numH[container.getAtomNumber(at)] += 1;
/*      */       }
/* 2137 */       if (bo.getAtom(1).getSymbol().equals("H"))
/*      */       {
/* 2139 */         IAtom at = bo.getAtom(0);
/* 2140 */         numH[container.getAtomNumber(at)] += 1;
/*      */       }
/*      */     }
/* 2143 */     return numH;
/*      */   }
/*      */   
/*      */ 
/*      */   public static void setRingData(IAtomContainer container, boolean rData, boolean rData2)
/*      */   {
/* 2149 */     SSSRFinder sssrf = new SSSRFinder(container);
/* 2150 */     IRingSet ringSet = sssrf.findSSSR();
/*      */     
/*      */ 
/* 2153 */     if (rData)
/*      */     {
/* 2155 */       for (int i = 0; i < container.getAtomCount(); i++)
/*      */       {
/* 2157 */         IAtom atom = container.getAtom(i);
/* 2158 */         IRingSet atomRings = ringSet.getRings(atom);
/* 2159 */         int n = atomRings.getAtomContainerCount();
/* 2160 */         if (n > 0)
/*      */         {
/* 2162 */           int[] ringData = new int[n];
/* 2163 */           for (int k = 0; k < n; k++)
/*      */           {
/* 2165 */             ringData[k] = atomRings.getAtomContainer(k).getAtomCount();
/*      */           }
/* 2167 */           atom.setProperty("RingData", ringData);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2172 */     if (rData2)
/*      */     {
/* 2174 */       for (int i = 0; i < container.getAtomCount(); i++)
/*      */       {
/* 2176 */         IAtom atom = container.getAtom(i);
/* 2177 */         IRingSet atomRings = ringSet.getRings(atom);
/* 2178 */         int n = atomRings.getAtomContainerCount();
/* 2179 */         if (n > 0)
/*      */         {
/* 2181 */           int[] ringData2 = new int[n];
/* 2182 */           for (int k = 0; k < n; k++)
/*      */           {
/* 2184 */             ringData2[k] = getRingNumberInRingSet(atomRings.getAtomContainer(k), ringSet);
/*      */           }
/* 2186 */           atom.setProperty("RingData2", ringData2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static Vector<int[]> getRindData(IAtomContainer container, IRingSet ringSet)
/*      */   {
/* 2195 */     Vector<int[]> v = new Vector();
/*      */     
/* 2197 */     for (int i = 0; i < container.getAtomCount(); i++)
/*      */     {
/* 2199 */       IAtom atom = container.getAtom(i);
/* 2200 */       IRingSet atomRings = ringSet.getRings(atom);
/* 2201 */       int n = atomRings.getAtomContainerCount();
/* 2202 */       if (n > 0)
/*      */       {
/* 2204 */         int[] ringData = new int[n];
/* 2205 */         for (int k = 0; k < n; k++)
/* 2206 */           ringData[k] = atomRings.getAtomContainer(k).getAtomCount();
/* 2207 */         v.add(ringData);
/*      */       }
/*      */       else {
/* 2210 */         v.add(null);
/*      */       } }
/* 2212 */     return v;
/*      */   }
/*      */   
/*      */   public static Vector<int[]> getRindData2(IAtomContainer container, IRingSet ringSet)
/*      */   {
/* 2217 */     Vector<int[]> v = new Vector();
/*      */     
/* 2219 */     for (int i = 0; i < container.getAtomCount(); i++)
/*      */     {
/* 2221 */       IAtom atom = container.getAtom(i);
/* 2222 */       IRingSet atomRings = ringSet.getRings(atom);
/* 2223 */       int n = atomRings.getAtomContainerCount();
/* 2224 */       if (n > 0)
/*      */       {
/* 2226 */         int[] ringData2 = new int[n];
/* 2227 */         for (int k = 0; k < n; k++)
/* 2228 */           ringData2[k] = getRingNumberInRingSet(atomRings.getAtomContainer(k), ringSet);
/* 2229 */         v.add(ringData2);
/*      */       }
/*      */       else {
/* 2232 */         v.add(null);
/*      */       } }
/* 2234 */     return v;
/*      */   }
/*      */   
/*      */   public static int getRingNumberInRingSet(IAtomContainer ring, IRingSet rs)
/*      */   {
/* 2239 */     for (int i = 0; i < rs.getAtomContainerCount(); i++)
/*      */     {
/* 2241 */       if (ring == rs.getAtomContainer(i))
/* 2242 */         return i;
/*      */     }
/* 2244 */     return -1;
/*      */   }
/*      */   
/*      */   public static void setParentMoleculeData(IAtomContainer container)
/*      */   {
/* 2249 */     for (int i = 0; i < container.getAtomCount(); i++)
/*      */     {
/* 2251 */       IAtom atom = container.getAtom(i);
/* 2252 */       atom.setProperty("ParentMoleculeData", container);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SmartsParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
