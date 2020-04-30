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
/*    */ public class DoubleStereoBond
/*    */   extends SMARTSBond
/*    */ {
/*    */   private static final long serialVersionUID = -93579804683457479L;
/* 36 */   public int stereoParameter = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean matches(IBond bond)
/*    */   {
/* 43 */     if (bond.getOrder() != IBond.Order.DOUBLE) {
/* 44 */       return false;
/*    */     }
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
/* 73 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/DoubleStereoBond.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
