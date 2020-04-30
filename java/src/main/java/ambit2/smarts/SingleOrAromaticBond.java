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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingleOrAromaticBond
/*    */   extends SMARTSBond
/*    */ {
/*    */   private static final long serialVersionUID = -93436889077894679L;
/*    */   
/*    */   public boolean matches(IBond bond)
/*    */   {
/* 43 */     if (bond.getOrder() == IBond.Order.SINGLE)
/* 44 */       return true;
/* 45 */     if (bond.getFlag(5))
/* 46 */       return true;
/* 47 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SingleOrAromaticBond.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
