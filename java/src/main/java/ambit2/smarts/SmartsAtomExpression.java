/*     */ package ambit2.smarts;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.openscience.cdk1.interfaces.IAtom;
/*     */ import org.openscience.cdk1.interfaces.IAtomContainer;
/*     */ import org.openscience.cdk1.interfaces.IBond;
/*     */ import org.openscience.cdk1.interfaces.IBond.Order;
/*     */ import org.openscience.cdk1.isomorphism.matchers.QueryAtomContainer;
/*     */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSAtom;
/*     */ import org.openscience.cdk1.silent.SilentChemObjectBuilder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SmartsAtomExpression
/*     */   extends SMARTSAtom
/*     */ {
/*     */   private static final long serialVersionUID = -123453467895564563L;
/*  45 */   public Vector<SmartsExpressionToken> tokens = new Vector();
/*     */   
/*  47 */   public Vector<String> recSmartsStrings = new Vector();
/*  48 */   public Vector<QueryAtomContainer> recSmartsContainers = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   public Vector<Vector<IAtom>> recSmartsMatches = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean matches(IAtom atom)
/*     */   {
/*  59 */     SmartsLogicalExpression sle = new SmartsLogicalExpression();
/*  60 */     for (int i = 0; i < this.tokens.size(); i++)
/*     */     {
/*  62 */       SmartsExpressionToken tok = (SmartsExpressionToken)this.tokens.get(i);
/*  63 */       if (tok.type < 1000)
/*     */       {
/*  65 */         sle.addArgument(getArgument(tok, atom));
/*     */       }
/*     */       else
/*  68 */         sle.addLogOperation(tok.type - 1000);
/*     */     }
/*  70 */     return sle.getValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public SmartsExpressionToken getLastToken()
/*     */   {
/*  76 */     return (SmartsExpressionToken)this.tokens.lastElement();
/*     */   }
/*     */   
/*     */   boolean getArgument(SmartsExpressionToken tok, IAtom atom)
/*     */   {
/*  81 */     switch (tok.type)
/*     */     {
/*     */     case 0: 
/*  84 */       return true;
/*     */     
/*     */     case 1: 
/*  87 */       if (atom.getFlag(5))
/*     */       {
/*  89 */         if (tok.param == 0) {
/*  90 */           return true;
/*     */         }
/*  92 */         if (SmartsConst.elSymbols[tok.param].equals(atom.getSymbol())) {
/*  93 */           return true;
/*     */         }
/*  95 */         return false;
/*     */       }
/*     */       
/*  98 */       return false;
/*     */     
/*     */     case 2: 
/* 101 */       if (!atom.getFlag(5))
/*     */       {
/* 103 */         if (tok.param == 0) {
/* 104 */           return true;
/*     */         }
/* 106 */         if (SmartsConst.elSymbols[tok.param].equals(atom.getSymbol())) {
/* 107 */           return true;
/*     */         }
/* 109 */         return false;
/*     */       }
/*     */       
/* 112 */       return false;
/*     */     
/*     */     case 3: 
/* 115 */       if (tok.param == atom.getFormalNeighbourCount().intValue()) {
/* 116 */         return true;
/*     */       }
/* 118 */       return false;
/*     */     
/*     */     case 8: 
/* 121 */       if (tok.param == atom.getValency().intValue()) {
/* 122 */         return true;
/*     */       }
/* 124 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 9: 
/* 133 */       Integer hci = atom.getImplicitHydrogenCount();
/* 134 */       int hc = 0;
/* 135 */       if (hci != null) {
/* 136 */         hc = hci.intValue();
/*     */       }
/* 138 */       if (tok.param == atom.getFormalNeighbourCount().intValue() + hc) {
/* 139 */         return true;
/*     */       }
/* 141 */       return false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 4: 
/* 149 */       Integer hci1 = atom.getImplicitHydrogenCount();
/* 150 */       int totalH = 0;
/* 151 */       if (hci1 != null) {
/* 152 */         totalH = hci1.intValue();
/*     */       }
/* 154 */       Integer explicitH = (Integer)atom.getProperty("ExplicitH");
/* 155 */       if (explicitH != null)
/* 156 */         totalH += explicitH.intValue();
/* 157 */       if (tok.param == totalH) {
/* 158 */         return true;
/*     */       }
/* 160 */       return false;
/*     */     
/*     */     case 6: 
/* 163 */       int[] atomRings = (int[])atom.getProperty("RingData");
/* 164 */       return match_R(atomRings, tok.param, atom);
/*     */     
/*     */     case 7: 
/* 167 */       int[] atomRings2 = (int[])atom.getProperty("RingData");
/* 168 */       return match_r(atomRings2, tok.param, atom);
/*     */     
/*     */ 
/*     */     case 13: 
/* 172 */       if (atom.getMassNumber() == null)
/* 173 */         return false;
/* 174 */       if (atom.getMassNumber().intValue() == 0) {
/* 175 */         return false;
/*     */       }
/* 177 */       if (atom.getMassNumber().intValue() == tok.param) {
/* 178 */         return true;
/*     */       }
/* 180 */       return false;
/*     */     
/*     */     case 10: 
/* 183 */       if (atom.getFormalCharge().intValue() == tok.param) {
/* 184 */         return true;
/*     */       }
/* 186 */       return false;
/*     */     
/*     */     case 11: 
/* 189 */       if (SmartsConst.elSymbols[tok.param].equals(atom.getSymbol())) {
/* 190 */         return true;
/*     */       }
/* 192 */       return false;
/*     */     
/*     */ 
/*     */     case 12: 
/* 196 */       if (tok.param == 1001)
/*     */       {
/* 198 */         if (atom.getStereoParity().intValue() == -1) {
/* 199 */           return false;
/*     */         }
/* 201 */         if (atom.getStereoParity().intValue() == 1) {
/* 202 */           return true;
/*     */         }
/* 204 */         return true;
/*     */       }
/*     */       
/* 207 */       if (tok.param == 1002)
/*     */       {
/* 209 */         if (atom.getStereoParity().intValue() == -1) {
/* 210 */           return true;
/*     */         }
/* 212 */         if (atom.getStereoParity().intValue() == 1) {
/* 213 */           return false;
/*     */         }
/* 215 */         return true;
/*     */       }
/*     */       
/* 218 */       return true;
/*     */     
/*     */ 
/*     */     case 14: 
/* 222 */       if (this.recSmartsMatches == null) {
/* 223 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 227 */       Vector<IAtom> atomMaps = (Vector)this.recSmartsMatches.get(tok.param);
/* 228 */       for (int i = 0; i < atomMaps.size(); i++)
/* 229 */         if (atomMaps.get(i) == atom)
/* 230 */           return true;
/* 231 */       return false;
/*     */     
/*     */     case 15: 
/* 234 */       return match_x(tok.param, atom);
/*     */     
/*     */     case 16: 
/* 237 */       return match_iMOE(tok.param, atom);
/*     */     
/*     */     case 17: 
/* 240 */       return match_GMOE(tok.param, atom);
/*     */     
/*     */     case 18: 
/* 243 */       return match_XMOE(atom);
/*     */     
/*     */     case 19: 
/* 246 */       return match_NMOE(atom);
/*     */     
/*     */ 
/*     */     case 20: 
/* 250 */       return match_vMOE(tok.param, atom);
/*     */     
/*     */     case 21: 
/* 253 */       return match_OB_Hybr(tok.param, atom);
/*     */     }
/*     */     
/* 256 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   String tokenToString(SmartsExpressionToken tok)
/*     */   {
/* 262 */     if (tok.type >= 1000) {
/* 263 */       return Character.toString(SmartsConst.LogOperationChars[(tok.type - 1000)]);
/*     */     }
/*     */     
/* 266 */     switch (tok.type)
/*     */     {
/*     */     case 0: 
/* 269 */       return "*";
/*     */     case 1: 
/* 271 */       if (tok.param > 0) {
/* 272 */         return SmartsConst.elSymbols[tok.param].toLowerCase();
/*     */       }
/* 274 */       return "a";
/*     */     case 2: 
/* 276 */       if (tok.param > 0) {
/* 277 */         return SmartsConst.elSymbols[tok.param];
/*     */       }
/* 279 */       return "A";
/*     */     
/*     */     case 3: 
/*     */     case 4: 
/*     */     case 5: 
/*     */     case 6: 
/*     */     case 7: 
/*     */     case 8: 
/*     */     case 9: 
/*     */     case 15: 
/*     */     case 20: 
/* 290 */       String s = Character.toString(SmartsConst.AtomPrimChars[tok.type]);
/* 291 */       if (tok.param != 1)
/* 292 */         s = s + tok.param;
/* 293 */       return s;
/*     */     
/*     */     case 21: 
/* 296 */       String sOBHybr = Character.toString(SmartsConst.AtomPrimChars[tok.type]);
/* 297 */       sOBHybr = sOBHybr + tok.param;
/* 298 */       return sOBHybr;
/*     */     
/*     */     case 16: 
/* 301 */       return "i";
/*     */     
/*     */     case 17: 
/* 304 */       String sG = "G";
/* 305 */       sG = sG + tok.param;
/* 306 */       return sG;
/*     */     
/*     */     case 18: 
/* 309 */       return "#X";
/*     */     
/*     */     case 19: 
/* 312 */       return "#N";
/*     */     case 10: 
/*     */       String s1;
/* 316 */       if (tok.param > 0) {
/* 317 */         s1 = "+";
/*     */       } else
/* 319 */         s1 = "-";
/* 320 */       if (Math.abs(tok.param) != 1)
/* 321 */         s1 = s1 + Math.abs(tok.param);
/* 322 */       return s1;
/*     */     
/*     */     case 11: 
/* 325 */       return "#" + tok.param;
/*     */     
/*     */     case 12: 
/* 328 */       if (tok.param == 1) {
/* 329 */         return "@";
/*     */       }
/* 331 */       return "@@";
/*     */     
/*     */     case 13: 
/* 334 */       return "" + tok.param;
/*     */     
/*     */     case 14: 
/* 337 */       if (this.recSmartsContainers.isEmpty()) {
/* 338 */         return "$()";
/*     */       }
/* 340 */       SmartsHelper sw = new SmartsHelper(SilentChemObjectBuilder.getInstance());
/* 341 */       return "$(" + sw.toSmarts((QueryAtomContainer)this.recSmartsContainers.get(tok.param)) + ")";
/*     */     }
/*     */     
/* 344 */     return "";
/*     */   }
/*     */   
/*     */   public boolean match_R(int[] atomRings, int param, IAtom atom)
/*     */   {
/* 349 */     if (atomRings == null)
/*     */     {
/* 351 */       if (param == 0) {
/* 352 */         return true;
/*     */       }
/* 354 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 358 */     if (param == -1)
/*     */     {
/* 360 */       if (atomRings.length > 0) {
/* 361 */         return true;
/*     */       }
/* 363 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 367 */     if (param == atomRings.length) {
/* 368 */       return true;
/*     */     }
/* 370 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean match_r(int[] atomRings, int param, IAtom atom)
/*     */   {
/* 377 */     if (atomRings == null)
/*     */     {
/* 379 */       if (param == 0) {
/* 380 */         return true;
/*     */       }
/* 382 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 386 */     if (param < 3)
/*     */     {
/* 388 */       if (atomRings.length > 0) {
/* 389 */         return true;
/*     */       }
/* 391 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 395 */     for (int i = 0; i < atomRings.length; i++)
/*     */     {
/* 397 */       if (atomRings[i] == param)
/* 398 */         return true;
/*     */     }
/* 400 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean match_x(int param, IAtom atom)
/*     */   {
/* 407 */     int[] atomRings = (int[])atom.getProperty("RingData2");
/* 408 */     if (atomRings == null) {
/* 409 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 414 */     IAtomContainer mol = (IAtomContainer)atom.getProperty("ParentMoleculeData");
/* 415 */     List ca = mol.getConnectedAtomsList(atom);
/* 416 */     int rbonds = 0;
/* 417 */     for (int i = 0; i < ca.size(); i++)
/*     */     {
/* 419 */       int[] atrings = (int[])((IAtom)ca.get(i)).getProperty("RingData2");
/* 420 */       if (atrings != null)
/*     */       {
/*     */ 
/*     */ 
/* 424 */         if (commonRingBond(atomRings, atrings)) {
/* 425 */           rbonds++;
/*     */         }
/*     */       }
/*     */     }
/* 429 */     if (param == -1)
/*     */     {
/* 431 */       if (rbonds > 0) {
/* 432 */         return true;
/*     */       }
/* 434 */       return false;
/*     */     }
/*     */     
/* 437 */     return param == rbonds;
/*     */   }
/*     */   
/*     */   public boolean match_iMOE(int param, IAtom atom)
/*     */   {
/* 442 */     if (atom.getFlag(5)) {
/* 443 */       return true;
/*     */     }
/*     */     
/* 446 */     IAtomContainer mol = (IAtomContainer)atom.getProperty("ParentMoleculeData");
/* 447 */     List ca = mol.getConnectedAtomsList(atom);
/* 448 */     for (int i = 0; i < ca.size(); i++)
/*     */     {
/* 450 */       IBond b = mol.getBond(atom, (IAtom)ca.get(i));
/* 451 */       if ((b.getOrder() == IBond.Order.DOUBLE) || (b.getOrder() == IBond.Order.TRIPLE)) {
/* 452 */         return true;
/*     */       }
/*     */     }
/* 455 */     return false;
/*     */   }
/*     */   
/*     */   public boolean match_GMOE(int param, IAtom atom)
/*     */   {
/* 460 */     if (param == 4)
/*     */     {
/*     */ 
/* 463 */       if ((atom.getSymbol().equals("C")) || (atom.getSymbol().equals("Si")) || (atom.getSymbol().equals("Ge")) || (atom.getSymbol().equals("Sn")) || (atom.getSymbol().equals("Pb")))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 468 */         return true;
/*     */       }
/* 470 */       return false;
/*     */     }
/*     */     
/* 473 */     if (param == 6)
/*     */     {
/*     */ 
/* 476 */       if ((atom.getSymbol().equals("O")) || (atom.getSymbol().equals("S")) || (atom.getSymbol().equals("Ge")) || (atom.getSymbol().equals("Te")) || (atom.getSymbol().equals("Po")))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 481 */         return true;
/*     */       }
/* 483 */       return false;
/*     */     }
/*     */     
/* 486 */     if (param == 7)
/*     */     {
/*     */ 
/* 489 */       if ((atom.getSymbol().equals("F")) || (atom.getSymbol().equals("Cl")) || (atom.getSymbol().equals("Br")) || (atom.getSymbol().equals("I")) || (atom.getSymbol().equals("At")))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 494 */         return true;
/*     */       }
/* 496 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 500 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean match_XMOE(IAtom atom)
/*     */   {
/* 506 */     if ((atom.getSymbol().equals("H")) || (atom.getSymbol().equals("C"))) {
/* 507 */       return false;
/*     */     }
/* 509 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean match_NMOE(IAtom atom)
/*     */   {
/* 515 */     if ((atom.getSymbol().equals("O")) || (atom.getSymbol().equals("N")) || (atom.getSymbol().equals("F")) || (atom.getSymbol().equals("Cl")) || (atom.getSymbol().equals("Br")))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 520 */       return true;
/*     */     }
/* 522 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean match_vMOE(int param, IAtom atom)
/*     */   {
/* 528 */     int nB = 0;
/* 529 */     IAtomContainer mol = (IAtomContainer)atom.getProperty("ParentMoleculeData");
/* 530 */     List ca = mol.getConnectedAtomsList(atom);
/* 531 */     for (int i = 0; i < ca.size(); i++)
/*     */     {
/* 533 */       IAtom a = (IAtom)ca.get(i);
/* 534 */       if (!a.getSymbol().equals("H"))
/*     */       {
/*     */ 
/* 537 */         nB++;
/*     */       }
/*     */     }
/* 540 */     return nB == param;
/*     */   }
/*     */   
/*     */   public boolean match_OB_Hybr(int param, IAtom atom)
/*     */   {
/* 545 */     if (atom.getFlag(5))
/*     */     {
/* 547 */       if (param == 2) {
/* 548 */         return true;
/*     */       }
/* 550 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 554 */     IAtomContainer mol = (IAtomContainer)atom.getProperty("ParentMoleculeData");
/* 555 */     List ca = mol.getConnectedAtomsList(atom);
/* 556 */     int nDB = 0;
/* 557 */     int nTB = 0;
/* 558 */     for (int i = 0; i < ca.size(); i++)
/*     */     {
/* 560 */       IBond b = mol.getBond(atom, (IAtom)ca.get(i));
/* 561 */       if (b.getOrder() == IBond.Order.DOUBLE) {
/* 562 */         nDB++;
/*     */       }
/* 564 */       else if (b.getOrder() == IBond.Order.TRIPLE) {
/* 565 */         nTB++;
/*     */       }
/*     */     }
/*     */     
/* 569 */     if (param == 3)
/*     */     {
/* 571 */       if ((nDB == 0) && (nTB == 0)) {
/* 572 */         return true;
/*     */       }
/*     */     }
/* 575 */     else if (param == 2)
/*     */     {
/* 577 */       if ((nDB == 1) && (nTB == 0)) {
/* 578 */         return true;
/*     */       }
/*     */       
/*     */     }
/* 582 */     else if ((nDB == 2) || (nTB == 1)) {
/* 583 */       return true;
/*     */     }
/*     */     
/* 586 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   boolean commonRingBond(int[] atomRingData1, int[] atomRingData2)
/*     */   {
/* 592 */     for (int i = 0; i < atomRingData1.length; i++) {
/* 593 */       for (int k = 0; k < atomRingData2.length; i++)
/*     */       {
/* 595 */         if (atomRingData1[i] == atomRingData1[k])
/* 596 */           return true;
/*     */       }
/*     */     }
/* 599 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 605 */     StringBuffer sb = new StringBuffer();
/* 606 */     sb.append("[");
/* 607 */     for (int i = 0; i < this.tokens.size(); i++)
/* 608 */       sb.append(tokenToString((SmartsExpressionToken)this.tokens.get(i)));
/* 609 */     sb.append("]");
/* 610 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SmartsAtomExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
