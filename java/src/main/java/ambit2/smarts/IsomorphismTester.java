/*     */ package ambit2.smarts;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.openscience.cdk1.interfaces.IAtom;
/*     */ import org.openscience.cdk1.interfaces.IAtomContainer;
/*     */ import org.openscience.cdk1.interfaces.IBond;
/*     */ import org.openscience.cdk1.isomorphism.matchers.IQueryAtom;
/*     */ import org.openscience.cdk1.isomorphism.matchers.IQueryBond;
/*     */ import org.openscience.cdk1.isomorphism.matchers.QueryAtomContainer;
/*     */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSAtom;
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
/*     */ public class IsomorphismTester
/*     */ {
/*     */   QueryAtomContainer query;
/*     */   IAtomContainer target;
/*     */   boolean isomorphismFound;
/*  48 */   boolean FlagStoreIsomorphismNode = false;
/*  49 */   Vector<Node> isomorphismNodes = new Vector();
/*  50 */   Stack<Node> stack = new Stack();
/*  51 */   Vector<IAtom> targetAt = new Vector();
/*  52 */   Vector<QuerySequenceElement> sequence = new Vector();
/*  53 */   Vector<IQueryAtom> sequencedAtoms = new Vector();
/*  54 */   Vector<IQueryAtom> sequencedBondAt1 = new Vector();
/*  55 */   Vector<IQueryAtom> sequencedBondAt2 = new Vector();
/*     */   
/*     */ 
/*     */   public void setQuery(QueryAtomContainer container)
/*     */   {
/*  60 */     this.query = container;
/*  61 */     TopLayer.setAtomTopLayers(this.query, "TL");
/*  62 */     setQueryAtomSequence(null);
/*     */   }
/*     */   
/*     */   public Vector<QuerySequenceElement> getSequence()
/*     */   {
/*  67 */     return this.sequence;
/*     */   }
/*     */   
/*     */   public void setSequence(QueryAtomContainer queryContainer, Vector<QuerySequenceElement> externalSequence)
/*     */   {
/*  72 */     this.query = queryContainer;
/*  73 */     this.sequence = externalSequence;
/*     */   }
/*     */   
/*     */   public Vector<QuerySequenceElement> transferSequenceToOwner()
/*     */   {
/*  78 */     Vector<QuerySequenceElement> ownerSeq = this.sequence;
/*  79 */     this.sequence = new Vector();
/*  80 */     return ownerSeq;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void setQueryAtomSequence(IQueryAtom firstAt)
/*     */   {
/*     */     IQueryAtom firstAtom;
/*     */     
/*     */     
/*  91 */     if (firstAt == null) {
/*  92 */       firstAtom = (IQueryAtom)this.query.getFirstAtom();
/*     */     } else
/*  94 */       firstAtom = firstAt;
/*  95 */     this.sequence.clear();
/*  96 */     this.sequencedAtoms.clear();
/*  97 */     this.sequencedBondAt1.clear();
/*  98 */     this.sequencedBondAt2.clear();
/*     */     
/*     */ 
/* 101 */     this.sequencedAtoms.add(firstAtom);
/* 102 */     QuerySequenceElement seqEl = new QuerySequenceElement();
/* 103 */     seqEl.center = firstAtom;
/* 104 */     TopLayer topLayer = (TopLayer)firstAtom.getProperty("TL");
/* 105 */     int n = topLayer.atoms.size();
/* 106 */     seqEl.atoms = new IQueryAtom[n];
/* 107 */     seqEl.bonds = new IQueryBond[n];
/* 108 */     for (int i = 0; i < n; i++)
/*     */     {
/* 110 */       this.sequencedAtoms.add((IQueryAtom)topLayer.atoms.get(i));
/* 111 */       seqEl.atoms[i] = ((IQueryAtom)topLayer.atoms.get(i));
/* 112 */       seqEl.bonds[i] = ((IQueryBond)topLayer.bonds.get(i));
/* 113 */       addSeqBond(seqEl.center, seqEl.atoms[i]);
/*     */     }
/* 115 */     this.sequence.add(seqEl);
/*     */     
/*     */ 
/* 118 */     Stack<QuerySequenceElement> stack = new Stack();
/* 119 */     stack.push(seqEl);
/* 120 */     while (!stack.empty())
/*     */     {
/*     */ 
/* 123 */       QuerySequenceElement curSeqAt = (QuerySequenceElement)stack.pop();
/* 124 */       for (int i = 0; i < curSeqAt.atoms.length; i++)
/*     */       {
/* 126 */         topLayer = (TopLayer)curSeqAt.atoms[i].getProperty("TL");
/* 127 */         if (topLayer.atoms.size() != 1)
/*     */         {
/* 129 */           int[] a = getSeqAtomsInLayer(topLayer);
/*     */           
/* 131 */           n = 0;
/* 132 */           for (int k = 0; k < a.length; k++) {
/* 133 */             if (a[k] == 0)
/* 134 */               n++;
/*     */           }
/* 136 */           if (n > 0)
/*     */           {
/* 138 */             seqEl = new QuerySequenceElement();
/* 139 */             seqEl.center = curSeqAt.atoms[i];
/* 140 */             seqEl.atoms = new IQueryAtom[n];
/* 141 */             seqEl.bonds = new IQueryBond[n];
/* 142 */             this.sequence.add(seqEl);
/* 143 */             stack.add(seqEl);
/*     */           }
/*     */           
/* 146 */           int j = 0;
/* 147 */           for (int k = 0; k < a.length; k++)
/*     */           {
/* 149 */             if (a[k] == 0)
/*     */             {
/* 151 */               seqEl.atoms[j] = ((IQueryAtom)topLayer.atoms.get(k));
/* 152 */               seqEl.bonds[j] = ((IQueryBond)topLayer.bonds.get(k));
/* 153 */               addSeqBond(seqEl.center, seqEl.atoms[j]);
/* 154 */               this.sequencedAtoms.add(seqEl.atoms[j]);
/*     */               
/* 156 */               j++;
/*     */ 
/*     */ 
/*     */             }
/* 160 */             else if (curSeqAt.center != topLayer.atoms.get(k))
/*     */             {
/*     */ 
/*     */ 
/* 164 */               if (getSeqBond(curSeqAt.atoms[i], (IAtom)topLayer.atoms.get(k)) == -1)
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/* 169 */                 QuerySequenceElement newSeqEl = new QuerySequenceElement();
/* 170 */                 newSeqEl.center = null;
/* 171 */                 newSeqEl.atoms = new IQueryAtom[2];
/* 172 */                 newSeqEl.bonds = new IQueryBond[1];
/* 173 */                 newSeqEl.atoms[0] = curSeqAt.atoms[i];
/* 174 */                 newSeqEl.atoms[1] = ((IQueryAtom)topLayer.atoms.get(k));
/* 175 */                 addSeqBond(newSeqEl.atoms[0], newSeqEl.atoms[1]);
/* 176 */                 newSeqEl.bonds[0] = ((IQueryBond)topLayer.bonds.get(k));
/* 177 */                 this.sequence.add(newSeqEl);
/*     */               }
/*     */             } }
/*     */         }
/*     */       }
/*     */     }
/* 183 */     for (int i = 0; i < this.sequence.size(); i++) {
/* 184 */       ((QuerySequenceElement)this.sequence.get(i)).setAtomNums(this.query);
/*     */     }
/*     */   }
/*     */   
/*     */   boolean containsAtom(Vector<IQueryAtom> v, IQueryAtom atom) {
/* 189 */     for (int i = 0; i < v.size(); i++)
/* 190 */       if (v.get(i) == atom)
/* 191 */         return true;
/* 192 */     return false;
/*     */   }
/*     */   
/*     */   boolean containsAtom(IAtom[] a, IAtom atom)
/*     */   {
/* 197 */     for (int i = 0; i < a.length; i++)
/* 198 */       if (a[i] == atom)
/* 199 */         return true;
/* 200 */     return false;
/*     */   }
/*     */   
/*     */   int[] getSeqAtomsInLayer(TopLayer topLayer)
/*     */   {
/* 205 */     int[] a = new int[topLayer.atoms.size()];
/* 206 */     for (int i = 0; i < topLayer.atoms.size(); i++)
/*     */     {
/* 208 */       if (containsAtom(this.sequencedAtoms, (IQueryAtom)topLayer.atoms.get(i)))
/*     */       {
/* 210 */         a[i] = 1;
/*     */       }
/*     */       else
/* 213 */         a[i] = 0;
/*     */     }
/* 215 */     return a;
/*     */   }
/*     */   
/*     */   void addSeqBond(IQueryAtom at1, IQueryAtom at2)
/*     */   {
/* 220 */     this.sequencedBondAt1.add(at1);
/* 221 */     this.sequencedBondAt2.add(at2);
/*     */   }
/*     */   
/*     */   int getSeqBond(IAtom at1, IAtom at2)
/*     */   {
/* 226 */     for (int i = 0; i < this.sequencedBondAt1.size(); i++)
/*     */     {
/* 228 */       if (this.sequencedBondAt1.get(i) == at1)
/*     */       {
/* 230 */         if (this.sequencedBondAt2.get(i) == at2) {
/* 231 */           return i;
/*     */         }
/*     */       }
/* 234 */       else if (this.sequencedBondAt1.get(i) == at2)
/*     */       {
/* 236 */         if (this.sequencedBondAt2.get(i) == at1)
/* 237 */           return i;
/*     */       }
/*     */     }
/* 240 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean hasIsomorphism(IAtomContainer container)
/*     */   {
/* 245 */     this.target = container;
/* 246 */     this.FlagStoreIsomorphismNode = false;
/* 247 */     this.isomorphismNodes.clear();
/*     */     
/* 249 */     if (this.query.getAtomCount() == 1) {
/* 250 */       return singleAtomIsomorphism();
/*     */     }
/* 252 */     TopLayer.setAtomTopLayers(this.target, "TL");
/* 253 */     executeSequence(true);
/* 254 */     return this.isomorphismFound;
/*     */   }
/*     */   
/*     */   public Vector<Integer> getIsomorphismPositions(IAtomContainer container)
/*     */   {
/* 259 */     this.target = container;
/* 260 */     this.FlagStoreIsomorphismNode = false;
/* 261 */     this.isomorphismNodes.clear();
/*     */     
/* 263 */     Vector<Integer> v = new Vector();
/* 264 */     if (this.query.getAtomCount() == 1)
/*     */     {
/* 266 */       SMARTSAtom qa = (SMARTSAtom)this.query.getAtom(0);
/* 267 */       for (int i = 0; i < this.target.getAtomCount(); i++)
/*     */       {
/* 269 */         if (qa.matches(this.target.getAtom(i)))
/* 270 */           v.add(new Integer(i));
/*     */       }
/* 272 */       return v;
/*     */     }
/*     */     
/* 275 */     TopLayer.setAtomTopLayers(this.target, "TL");
/* 276 */     for (int i = 0; i < this.target.getAtomCount(); i++)
/*     */     {
/* 278 */       executeSequenceAtPos(i);
/* 279 */       if (this.isomorphismFound)
/* 280 */         v.add(new Integer(i));
/*     */     }
/* 282 */     return v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vector<IAtom> getIsomorphismMapping(IAtomContainer container)
/*     */   {
/* 292 */     if (this.query == null) return null;
/* 293 */     this.target = container;
/* 294 */     this.FlagStoreIsomorphismNode = true;
/* 295 */     this.isomorphismNodes.clear();
/*     */     
/* 297 */     if (this.query.getAtomCount() == 1)
/*     */     {
/* 299 */       SMARTSAtom qa = (SMARTSAtom)this.query.getAtom(0);
/* 300 */       for (int i = 0; i < this.target.getAtomCount(); i++)
/*     */       {
/* 302 */         if (qa.matches(this.target.getAtom(i)))
/*     */         {
/* 304 */           Vector<IAtom> v = new Vector();
/* 305 */           v.add(this.target.getAtom(i));
/* 306 */           return v;
/*     */         }
/*     */       }
/* 309 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 313 */     TopLayer.setAtomTopLayers(this.target, "TL");
/* 314 */     executeSequence(true);
/*     */     
/* 316 */     if (this.isomorphismFound)
/*     */     {
/*     */ 
/* 319 */       Node node = (Node)this.isomorphismNodes.get(0);
/* 320 */       Vector<IAtom> v = new Vector();
/* 321 */       for (int i = 0; i < node.atoms.length; i++) {
/* 322 */         v.add(node.atoms[i]);
/*     */       }
/* 324 */       return v;
/*     */     }
/*     */     
/* 327 */     return null;
/*     */   }
/*     */   
/*     */   public Vector<IBond> generateBondMapping(IAtomContainer container, Vector<IAtom> atomMapping)
/*     */   {
/* 332 */     if (this.query == null) {
/* 333 */       return null;
/*     */     }
/* 335 */     Vector<IBond> v = new Vector();
/* 336 */     for (int i = 0; i < this.query.getBondCount(); i++)
/*     */     {
/* 338 */       IAtom qa0 = this.query.getBond(i).getAtom(0);
/* 339 */       IAtom qa1 = this.query.getBond(i).getAtom(1);
/* 340 */       IAtom a0 = (IAtom)atomMapping.get(this.query.getAtomNumber(qa0));
/* 341 */       IAtom a1 = (IAtom)atomMapping.get(this.query.getAtomNumber(qa1));
/*     */       
/* 343 */       v.add(container.getBond(a0, a1));
/*     */     }
/*     */     
/* 346 */     return v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vector<Vector<IAtom>> getAllIsomorphismMappings(IAtomContainer container)
/*     */   {
/* 357 */     if (this.query == null) return null;
/* 358 */     this.target = container;
/* 359 */     this.FlagStoreIsomorphismNode = true;
/* 360 */     this.isomorphismNodes.clear();
/* 361 */     Vector<Vector<IAtom>> result = new Vector();
/*     */     
/*     */ 
/* 364 */     if (this.query.getAtomCount() == 1)
/*     */     {
/* 366 */       SMARTSAtom qa = (SMARTSAtom)this.query.getAtom(0);
/* 367 */       for (int i = 0; i < this.target.getAtomCount(); i++)
/*     */       {
/* 369 */         if (qa.matches(this.target.getAtom(i)))
/*     */         {
/* 371 */           Vector<IAtom> v = new Vector();
/* 372 */           v.add(this.target.getAtom(i));
/* 373 */           result.add(v);
/*     */         }
/*     */       }
/* 376 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 380 */     TopLayer.setAtomTopLayers(this.target, "TL");
/* 381 */     executeSequence(false);
/*     */     
/* 383 */     if (this.isomorphismFound)
/*     */     {
/*     */ 
/* 386 */       for (int k = 0; k < this.isomorphismNodes.size(); k++)
/*     */       {
/* 388 */         Node node = (Node)this.isomorphismNodes.get(k);
/* 389 */         Vector<IAtom> v = new Vector();
/* 390 */         for (int i = 0; i < node.atoms.length; i++)
/* 391 */           v.add(node.atoms[i]);
/* 392 */         result.add(v);
/*     */       }
/*     */     }
/* 395 */     return result;
/*     */   }
/*     */   
/*     */   public Vector<Vector<IAtom>> getNonIdenticalMappings(IAtomContainer container)
/*     */   {
/* 400 */     Vector<Vector<IAtom>> result = new Vector();
/* 401 */     Vector<Vector<IAtom>> allMaps = getAllIsomorphismMappings(container);
/* 402 */     if (allMaps.isEmpty()) {
/* 403 */       return result;
/*     */     }
/* 405 */     int nMaps = allMaps.size();
/* 406 */     int nAtoms = ((Vector)allMaps.get(0)).size();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 412 */     for (int i = 0; i < nMaps; i++)
/*     */     {
/* 414 */       Vector<IAtom> map = (Vector)allMaps.get(i);
/* 415 */       boolean FlagOK = true;
/* 416 */       for (int j = 0; j < result.size(); j++)
/*     */       {
/*     */ 
/* 419 */         Vector<IAtom> map1 = (Vector)result.get(j);
/* 420 */         boolean FlagOneFifferent = false;
/* 421 */         for (int k = 0; k < nAtoms; k++)
/*     */         {
/*     */ 
/* 424 */           if (!map1.contains(map.get(k)))
/*     */           {
/* 426 */             FlagOneFifferent = true;
/* 427 */             break;
/*     */           }
/*     */         }
/*     */         
/* 431 */         if (!FlagOneFifferent)
/*     */         {
/* 433 */           FlagOK = false;
/* 434 */           break;
/*     */         }
/*     */       }
/*     */       
/* 438 */       if (FlagOK) {
/* 439 */         result.add(map);
/*     */       }
/*     */     }
/* 442 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Vector<Vector<IAtom>> getNonOverlappingMappings(IAtomContainer container)
/*     */   {
/* 448 */     Vector<Vector<IAtom>> result = new Vector();
/* 449 */     Vector<Vector<IAtom>> allMaps = getAllIsomorphismMappings(container);
/* 450 */     if (allMaps.isEmpty()) {
/* 451 */       return result;
/*     */     }
/* 453 */     int nMaps = allMaps.size();
/* 454 */     int nAtoms = ((Vector)allMaps.get(0)).size();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 459 */     for (int i = 0; i < nMaps; i++)
/*     */     {
/* 461 */       Vector<IAtom> map = (Vector)allMaps.get(i);
/* 462 */       boolean FlagOK = true;
/* 463 */       for (int j = 0; j < result.size(); j++)
/*     */       {
/*     */ 
/* 466 */         Vector<IAtom> map1 = (Vector)result.get(j);
/* 467 */         for (int k = 0; k < nAtoms; k++)
/*     */         {
/*     */ 
/* 470 */           if (map1.contains(map.get(k)))
/*     */           {
/* 472 */             FlagOK = false;
/* 473 */             break;
/*     */           }
/*     */         }
/*     */         
/* 477 */         if (!FlagOK) {
/*     */           break;
/*     */         }
/*     */       }
/* 481 */       if (FlagOK) {
/* 482 */         result.add(map);
/*     */       }
/*     */     }
/* 485 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Vector<Vector<Integer>> getOverlappedMappingClusters(Vector<Vector<IAtom>> maps)
/*     */   {
/* 494 */     Vector<Vector<Integer>> v = new Vector();
/* 495 */     if (maps.size() == 0) {
/* 496 */       return v;
/*     */     }
/*     */     
/*     */ 
/* 500 */     Vector<Integer> vInt = new Vector();
/* 501 */     vInt.add(new Integer(0));
/* 502 */     v.add(vInt);
/*     */     
/*     */ 
/* 505 */     if (maps.size() == 1) {
/* 506 */       return v;
/*     */     }
/*     */     
/* 509 */     for (int i = 1; i < maps.size(); i++)
/*     */     {
/* 511 */       Vector<IAtom> map = (Vector)maps.get(i);
/*     */       
/* 513 */       boolean FlagOverlap = false;
/* 514 */       for (int k = 0; k < v.size(); k++)
/*     */       {
/* 516 */         if (overlapsWithCluster(map, (Vector)v.get(k), maps))
/*     */         {
/* 518 */           ((Vector)v.get(k)).add(new Integer(i));
/* 519 */           FlagOverlap = true;
/* 520 */           break;
/*     */         }
/*     */       }
/*     */       
/* 524 */       if (!FlagOverlap)
/*     */       {
/*     */ 
/* 527 */         vInt = new Vector();
/* 528 */         vInt.add(new Integer(i));
/* 529 */         v.add(vInt);
/*     */       }
/*     */     }
/*     */     
/* 533 */     return v;
/*     */   }
/*     */   
/*     */   boolean overlapsWithCluster(Vector<IAtom> map, Vector<Integer> cluster, Vector<Vector<IAtom>> maps)
/*     */   {
/* 538 */     for (int i = 0; i < cluster.size(); i++)
/*     */     {
/* 540 */       int mapNum = ((Integer)cluster.get(i)).intValue();
/* 541 */       Vector<IAtom> clMap = (Vector)maps.get(mapNum);
/* 542 */       for (int k = 0; k < map.size(); k++) {
/* 543 */         if (clMap.contains(map.get(k)))
/* 544 */           return true;
/*     */       }
/*     */     }
/* 547 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   boolean singleAtomIsomorphism()
/*     */   {
/* 553 */     SMARTSAtom qa = (SMARTSAtom)this.query.getAtom(0);
/* 554 */     this.isomorphismFound = false;
/* 555 */     for (int i = 0; i < this.target.getAtomCount(); i++)
/*     */     {
/* 557 */       if (qa.matches(this.target.getAtom(i)))
/*     */       {
/* 559 */         this.isomorphismFound = true;
/* 560 */         break;
/*     */       }
/*     */     }
/* 563 */     return this.isomorphismFound;
/*     */   }
/*     */   
/*     */ 
/*     */   void executeSequence(boolean stopAtFirstMapping)
/*     */   {
/* 569 */     this.isomorphismFound = false;
/* 570 */     this.stack.clear();
/*     */     
/*     */ 
/* 573 */     QuerySequenceElement el = (QuerySequenceElement)this.sequence.get(0);
/* 574 */     for (int k = 0; k < this.target.getAtomCount(); k++)
/*     */     {
/* 576 */       IAtom at = this.target.getAtom(k);
/* 577 */       if (el.center.matches(at))
/*     */       {
/* 579 */         Node node = new Node();
/* 580 */         node.sequenceElNum = 0;
/* 581 */         node.nullifyAtoms(this.query.getAtomCount());
/* 582 */         node.atoms[el.centerNum] = at;
/* 583 */         this.stack.push(node);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 588 */     if (stopAtFirstMapping) {
/*     */       do {
/* 590 */         if (this.stack.isEmpty())
/*     */           break;
/* 592 */         expandNode((Node)this.stack.pop());
/* 593 */       } while (!this.isomorphismFound);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 599 */       while (!this.stack.isEmpty()) {
/* 600 */         expandNode((Node)this.stack.pop());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void executeSequenceAtPos(int pos)
/*     */   {
/* 607 */     this.isomorphismFound = false;
/* 608 */     this.stack.clear();
/*     */     
/*     */ 
/* 611 */     QuerySequenceElement el = (QuerySequenceElement)this.sequence.get(0);
/* 612 */     IAtom at = this.target.getAtom(pos);
/* 613 */     if (el.center.matches(at))
/*     */     {
/* 615 */       Node node = new Node();
/* 616 */       node.sequenceElNum = 0;
/* 617 */       node.nullifyAtoms(this.query.getAtomCount());
/* 618 */       node.atoms[el.centerNum] = at;
/* 619 */       this.stack.push(node);
/*     */     }
/*     */     
/*     */ 
/* 623 */     while (!this.stack.isEmpty())
/*     */     {
/* 625 */       expandNode((Node)this.stack.pop());
/* 626 */       if (this.isomorphismFound) {
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void expandNode(Node node)
/*     */   {
/* 634 */     QuerySequenceElement el = (QuerySequenceElement)this.sequence.get(node.sequenceElNum);
/*     */     
/* 636 */     if (el.center == null)
/*     */     {
/*     */ 
/* 639 */       IAtom tAt0 = node.atoms[this.query.getAtomNumber(el.atoms[0])];
/* 640 */       IAtom tAt1 = node.atoms[this.query.getAtomNumber(el.atoms[1])];
/* 641 */       IBond tBo = this.target.getBond(tAt0, tAt1);
/* 642 */       if ((tBo != null) && 
/* 643 */         (el.bonds[0].matches(tBo)))
/*     */       {
/* 645 */         node.sequenceElNum += 1;
/*     */         
/* 647 */         if (node.sequenceElNum == this.sequence.size())
/*     */         {
/*     */ 
/* 650 */           this.isomorphismFound = true;
/* 651 */           if (this.FlagStoreIsomorphismNode) {
/* 652 */             this.isomorphismNodes.add(node);
/*     */           }
/*     */         } else {
/* 655 */           this.stack.push(node);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 660 */       this.targetAt.clear();
/* 661 */       IAtom tAt = node.atoms[el.centerNum];
/* 662 */       List<IAtom> conAt = this.target.getConnectedAtomsList(tAt);
/* 663 */       for (int i = 0; i < conAt.size(); i++)
/*     */       {
/* 665 */         if (!containsAtom(node.atoms, (IAtom)conAt.get(i))) {
/* 666 */           this.targetAt.add(conAt.get(i));
/*     */         }
/*     */       }
/* 669 */       if (el.atoms.length <= this.targetAt.size()) {
/* 670 */         generateNodes(node);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void generateNodes(Node node)
/*     */   {
/* 677 */     QuerySequenceElement el = (QuerySequenceElement)this.sequence.get(node.sequenceElNum);
/*     */     
/* 679 */     if (el.atoms.length == 1)
/*     */     {
/* 681 */       for (int i = 0; i < this.targetAt.size(); i++)
/*     */       {
/* 683 */         if ((el.atoms[0].matches((IAtom)this.targetAt.get(i))) && 
/* 684 */           (matchBond(node, el, 0, (IAtom)this.targetAt.get(i))))
/*     */         {
/* 686 */           Node newNode = node.cloneNode();
/* 687 */           newNode.atoms[el.atomNums[0]] = ((IAtom)this.targetAt.get(i));
/* 688 */           node.sequenceElNum += 1;
/*     */           
/* 690 */           if (newNode.sequenceElNum == this.sequence.size())
/*     */           {
/*     */ 
/* 693 */             this.isomorphismFound = true;
/* 694 */             if (this.FlagStoreIsomorphismNode) {
/* 695 */               this.isomorphismNodes.add(newNode);
/*     */             }
/*     */           } else {
/* 698 */             this.stack.push(newNode);
/*     */           }
/*     */         } }
/* 701 */       return;
/*     */     }
/*     */     
/* 704 */     if (el.atoms.length == 2)
/*     */     {
/* 706 */       for (int i = 0; i < this.targetAt.size(); i++)
/* 707 */         if ((el.atoms[0].matches((IAtom)this.targetAt.get(i))) && 
/* 708 */           (matchBond(node, el, 0, (IAtom)this.targetAt.get(i))))
/* 709 */           for (int j = 0; j < this.targetAt.size(); j++)
/* 710 */             if ((i != j) && 
/* 711 */               (el.atoms[1].matches((IAtom)this.targetAt.get(j))) && 
/* 712 */               (matchBond(node, el, 1, (IAtom)this.targetAt.get(j))))
/*     */             {
/* 714 */               Node newNode = node.cloneNode();
/* 715 */               newNode.atoms[el.atomNums[0]] = ((IAtom)this.targetAt.get(i));
/* 716 */               newNode.atoms[el.atomNums[1]] = ((IAtom)this.targetAt.get(j));
/* 717 */               node.sequenceElNum += 1;
/*     */               
/* 719 */               if (newNode.sequenceElNum == this.sequence.size())
/*     */               {
/*     */ 
/* 722 */                 this.isomorphismFound = true;
/* 723 */                 if (this.FlagStoreIsomorphismNode) {
/* 724 */                   this.isomorphismNodes.add(newNode);
/*     */                 }
/*     */               } else {
/* 727 */                 this.stack.push(newNode);
/*     */               } }
/* 729 */       return;
/*     */     }
/*     */     
/* 732 */     if (el.atoms.length == 3)
/*     */     {
/* 734 */       for (int i = 0; i < this.targetAt.size(); i++)
/* 735 */         if ((el.atoms[0].matches((IAtom)this.targetAt.get(i))) && 
/* 736 */           (matchBond(node, el, 0, (IAtom)this.targetAt.get(i))))
/* 737 */           for (int j = 0; j < this.targetAt.size(); j++)
/* 738 */             if ((i != j) && 
/* 739 */               (el.atoms[1].matches((IAtom)this.targetAt.get(j))) && 
/* 740 */               (matchBond(node, el, 1, (IAtom)this.targetAt.get(j))))
/* 741 */               for (int k = 0; k < this.targetAt.size(); k++)
/* 742 */                 if ((k != i) && (k != j) && 
/* 743 */                   (el.atoms[2].matches((IAtom)this.targetAt.get(k))) && 
/* 744 */                   (matchBond(node, el, 2, (IAtom)this.targetAt.get(k))))
/*     */                 {
/* 746 */                   Node newNode = node.cloneNode();
/* 747 */                   newNode.atoms[el.atomNums[0]] = ((IAtom)this.targetAt.get(i));
/* 748 */                   newNode.atoms[el.atomNums[1]] = ((IAtom)this.targetAt.get(j));
/* 749 */                   newNode.atoms[el.atomNums[2]] = ((IAtom)this.targetAt.get(k));
/* 750 */                   node.sequenceElNum += 1;
/*     */                   
/* 752 */                   if (newNode.sequenceElNum == this.sequence.size())
/*     */                   {
/*     */ 
/* 755 */                     this.isomorphismFound = true;
/* 756 */                     if (this.FlagStoreIsomorphismNode) {
/* 757 */                       this.isomorphismNodes.add(newNode);
/*     */                     }
/*     */                   } else {
/* 760 */                     this.stack.push(newNode);
/*     */                   } }
/* 762 */       return;
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
/* 773 */     Stack<int[]> st = new Stack();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 778 */     for (int i = 0; i < this.targetAt.size(); i++)
/*     */     {
/* 780 */       if ((el.atoms[0].matches((IAtom)this.targetAt.get(i))) && 
/* 781 */         (matchBond(node, el, 0, (IAtom)this.targetAt.get(i))))
/*     */       {
/* 783 */         int[] t = new int[el.atoms.length + 1];
/* 784 */         t[(t.length - 1)] = 1;
/* 785 */         t[0] = i;
/* 786 */         st.push(t);
/*     */       }
/*     */     }
/*     */     
/* 790 */     while (!st.isEmpty())
/*     */     {
/* 792 */       int[] t = (int[])st.pop();
/* 793 */       int n = t[(t.length - 1)];
/*     */       
/* 795 */       if (n == t.length - 1)
/*     */       {
/*     */ 
/* 798 */         Node newNode = node.cloneNode();
/* 799 */         for (int k = 0; k < t.length - 1; k++)
/* 800 */           newNode.atoms[el.atomNums[k]] = ((IAtom)this.targetAt.get(t[k]));
/* 801 */         node.sequenceElNum += 1;
/*     */         
/* 803 */         if (newNode.sequenceElNum == this.sequence.size())
/*     */         {
/*     */ 
/* 806 */           this.isomorphismFound = true;
/* 807 */           if (this.FlagStoreIsomorphismNode) {
/* 808 */             this.isomorphismNodes.add(newNode);
/*     */           }
/*     */         } else {
/* 811 */           this.stack.push(newNode);
/*     */         }
/*     */       }
/*     */       else {
/* 815 */         for (int i = 0; i < this.targetAt.size(); i++)
/*     */         {
/*     */ 
/* 818 */           boolean Flag = true;
/* 819 */           for (int k = 0; k < n; k++) {
/* 820 */             if (i == t[k])
/*     */             {
/* 822 */               Flag = false;
/* 823 */               break;
/*     */             }
/*     */           }
/* 826 */           if ((Flag) && 
/* 827 */             (el.atoms[n].matches((IAtom)this.targetAt.get(i))) && 
/* 828 */             (matchBond(node, el, n, (IAtom)this.targetAt.get(i))))
/*     */           {
/*     */ 
/* 831 */             int[] tnew = new int[el.atoms.length + 1];
/* 832 */             for (int k = 0; k < n; k++)
/* 833 */               tnew[k] = t[k];
/* 834 */             tnew[n] = i;
/* 835 */             tnew[(t.length - 1)] = (n + 1);
/* 836 */             st.push(tnew);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   boolean matchBond(Node node, QuerySequenceElement el, int qAtNum, IAtom taAt)
/*     */   {
/* 845 */     IBond taBo = this.target.getBond(taAt, node.atoms[el.centerNum]);
/* 846 */     return el.bonds[qAtNum].matches(taBo);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void printDebugInfo()
/*     */   {
/* 857 */     System.out.println("Query Atoms Topological Layers");
/* 858 */     for (int i = 0; i < this.query.getAtomCount(); i++) {
/* 859 */       System.out.println("" + i + "  " + this.query.getAtom(i).getProperty("TL").toString());
/*     */     }
/*     */     
/* 862 */     System.out.println();
/* 863 */     System.out.println("Query Sequence");
/* 864 */     for (int i = 0; i < this.sequence.size(); i++) {
/* 865 */       System.out.println(((QuerySequenceElement)this.sequence.get(i)).toString(this.query));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/IsomorphismTester.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
