/*    */ package ambit2.smarts;
/*    */ 
/*    */ import org.openscience.cdk1.interfaces.IBond;
/*    */ import org.openscience.cdk1.isomorphism.matchers.IQueryAtom;
/*    */ import org.openscience.cdk1.isomorphism.matchers.IQueryBond;
/*    */ import org.openscience.cdk1.isomorphism.matchers.QueryAtomContainer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QuerySequenceElement
/*    */ {
/*    */   IQueryAtom center;
/*    */   IQueryAtom[] atoms;
/*    */   IQueryBond[] bonds;
/*    */   int[] atomNums;
/*    */   int centerNum;
/*    */   
/*    */   public void setAtomNums(QueryAtomContainer container)
/*    */   {
/* 51 */     if (this.center != null) {
/* 52 */       this.centerNum = container.getAtomNumber(this.center);
/*    */     } else
/* 54 */       this.centerNum = -1;
/* 55 */     this.atomNums = new int[this.atoms.length];
/* 56 */     for (int i = 0; i < this.atoms.length; i++) {
/* 57 */       this.atomNums[i] = container.getAtomNumber(this.atoms[i]);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString() {
/* 62 */     StringBuffer sb = new StringBuffer();
/* 63 */     if (this.center == null)
/*    */     {
/* 65 */       sb.append("Bond " + SmartsHelper.atomToString(this.atoms[0]) + " " + SmartsHelper.atomToString(this.atoms[1]) + "   " + SmartsHelper.bondToString((IBond)this.bonds[0]));
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/*    */ 
/* 71 */       sb.append("Center = " + SmartsHelper.atomToString(this.center) + "  atoms: ");
/* 72 */       for (int i = 0; i < this.atoms.length; i++) {
/* 73 */         sb.append("(" + SmartsHelper.atomToString(this.atoms[i]) + "," + SmartsHelper.bondToString((IBond)this.bonds[i]) + ") ");
/*    */       }
/*    */     }
/* 76 */     return sb.toString();
/*    */   }
/*    */   
/*    */   public String toString(QueryAtomContainer query)
/*    */   {
/* 81 */     StringBuffer sb = new StringBuffer();
/* 82 */     if (this.center == null)
/*    */     {
/* 84 */       sb.append("Bond " + SmartsHelper.atomToString(this.atoms[0]) + query.getAtomNumber(this.atoms[0]) + " " + SmartsHelper.atomToString(this.atoms[1]) + query.getAtomNumber(this.atoms[1]) + "   " + SmartsHelper.bondToString((IBond)this.bonds[0]));
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/*    */ 
/* 90 */       sb.append("Center = " + SmartsHelper.atomToString(this.center) + query.getAtomNumber(this.center) + "  atoms: ");
/*    */       
/* 92 */       for (int i = 0; i < this.atoms.length; i++) {
/* 93 */         sb.append("(" + SmartsHelper.atomToString(this.atoms[i]) + query.getAtomNumber(this.atoms[i]) + "," + SmartsHelper.bondToString((IBond)this.bonds[i]) + ") ");
/*    */       }
/*    */     }
/* 96 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/QuerySequenceElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
