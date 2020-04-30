/*    */ package ambit2.smarts;
/*    */ 
/*    */ import org.openscience.cdk1.interfaces.IBond;
/*    */ import org.openscience.cdk1.interfaces.IBond.Order;
/*    */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSBond;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingleNonAromaticBond
/*    */   extends SMARTSBond
/*    */ {
/*    */   private static final long serialVersionUID = -9341634575490679L;
/*    */   
/*    */   public boolean matches(IBond bond)
/*    */   {
/* 20 */     if ((bond.getOrder() == IBond.Order.SINGLE) && 
/* 21 */       (!bond.getFlag(5))) {
/* 22 */       return true;
/*    */     }
/* 24 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SingleNonAromaticBond.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
