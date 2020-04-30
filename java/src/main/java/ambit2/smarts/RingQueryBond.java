/*    */ package ambit2.smarts;
/*    */ 
/*    */ import org.openscience.cdk1.interfaces.IAtom;
/*    */ import org.openscience.cdk1.interfaces.IBond;
/*    */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSBond;
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
/*    */ public class RingQueryBond
/*    */   extends SMARTSBond
/*    */ {
/*    */   private static final long serialVersionUID = -90236069308675679L;
/*    */   
/*    */   public boolean matches(IBond bond)
/*    */   {
/* 45 */     if (isRingBond(bond))
/* 46 */       return true;
/* 47 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isRingBond(IBond bond)
/*    */   {
/* 57 */     int[] atomRings0 = (int[])bond.getAtom(0).getProperty("RingData2");
/* 58 */     int[] atomRings1 = (int[])bond.getAtom(1).getProperty("RingData2");
/* 59 */     if ((atomRings0 == null) || (atomRings1 == null)) {
/* 60 */       return false;
/*    */     }
/*    */     
/* 63 */     for (int i = 0; i < atomRings0.length; i++) {
/* 64 */       for (int j = 0; j < atomRings1.length; j++)
/* 65 */         if (atomRings0[i] == atomRings1[j])
/* 66 */           return true;
/*    */     }
/* 68 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/RingQueryBond.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
