/*     */ package ambit2.smarts;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.openscience.cdk1.interfaces.IAtom;
/*     */ import org.openscience.cdk1.interfaces.IAtomContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EquivalenceTester
/*     */ {
/*     */   public IAtomContainer target;
/*     */   public int[] atomClasses;
/*     */   public int nClasses;
/*     */   
/*     */   public void setTarget(IAtomContainer t)
/*     */   {
/*  19 */     this.target = t;
/*  20 */     this.atomClasses = new int[this.target.getAtomCount()];
/*  21 */     this.nClasses = 0;
/*  22 */     for (int i = 0; i < this.atomClasses.length; i++) {
/*  23 */       this.atomClasses[i] = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public void quickFindEquivalentTerminalHAtoms() {
/*  28 */     for (int i = 0; i < this.target.getAtomCount(); i++)
/*     */     {
/*  30 */       IAtom at = this.target.getAtom(i);
/*  31 */       if (!at.getSymbol().equals("H"))
/*     */       {
/*     */ 
/*  34 */         List<IAtom> list = this.target.getConnectedAtomsList(at);
/*  35 */         List<IAtom> hlist = new ArrayList();
/*     */         
/*  37 */         for (int k = 0; k < list.size(); k++)
/*     */         {
/*  39 */           IAtom a = (IAtom)list.get(k);
/*  40 */           if (a.getSymbol().equals("H")) {
/*  41 */             hlist.add(a);
/*     */           }
/*     */         }
/*  44 */         if (!hlist.isEmpty())
/*     */         {
/*  46 */           this.nClasses += 1;
/*  47 */           for (int k = 0; k < hlist.size(); k++)
/*     */           {
/*  49 */             IAtom a = (IAtom)hlist.get(k);
/*  50 */             int index = this.target.getAtomNumber(a);
/*  51 */             this.atomClasses[index] = this.nClasses;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equivalentMaps(Vector<IAtom> map1, Vector<IAtom> map2)
/*     */   {
/*  60 */     if (map1.size() != map2.size()) {
/*  61 */       return false;
/*     */     }
/*  63 */     for (int i = 0; i < map1.size(); i++)
/*     */     {
/*  65 */       if ((map1.get(i) != map2.get(i)) && 
/*  66 */         (!equivalentAtoms((IAtom)map1.get(i), (IAtom)map2.get(i)))) {
/*  67 */         return false;
/*     */       }
/*     */     }
/*  70 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public Vector<Vector<IAtom>> filterEquivalentMappings(Vector<Vector<IAtom>> maps)
/*     */   {
/*  76 */     if (maps.size() == 0) {
/*  77 */       return maps;
/*     */     }
/*  79 */     Vector<Vector<IAtom>> res = new Vector();
/*  80 */     res.add(maps.get(0));
/*     */     
/*  82 */     for (int i = 1; i < maps.size(); i++)
/*     */     {
/*  84 */       boolean FlagEquivalent = false;
/*  85 */       for (int k = 0; k < res.size(); k++) {
/*  86 */         if (equivalentMaps((Vector)maps.get(i), (Vector)res.get(k)))
/*     */         {
/*  88 */           FlagEquivalent = true;
/*  89 */           break;
/*     */         }
/*     */       }
/*  92 */       if (!FlagEquivalent) {
/*  93 */         res.add(maps.get(i));
/*     */       }
/*     */     }
/*  96 */     return res;
/*     */   }
/*     */   
/*     */   public boolean equivalentAtoms(IAtom a1, IAtom a2) {
/* 100 */     int a1_ind = this.target.getAtomNumber(a1);
/* 101 */     int a2_ind = this.target.getAtomNumber(a2);
/*     */     
/* 103 */     if ((this.atomClasses[a1_ind] > 0) && (this.atomClasses[a2_ind] > 0) && 
/* 104 */       (this.atomClasses[a1_ind] == this.atomClasses[a2_ind])) {
/* 105 */       return true;
/*     */     }
/* 107 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/EquivalenceTester.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
