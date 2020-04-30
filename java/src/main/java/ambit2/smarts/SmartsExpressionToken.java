/*    */ package ambit2.smarts;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SmartsExpressionToken
/*    */ {
/*    */   public int type;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int param;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SmartsExpressionToken(int nType, int nParam)
/*    */   {
/* 34 */     this.type = nType;
/* 35 */     this.param = nParam;
/*    */   }
/*    */   
/*    */   public boolean isLogicalOperation()
/*    */   {
/* 40 */     if (this.type >= 1000)
/* 41 */       return true;
/* 42 */     return false;
/*    */   }
/*    */   
/*    */   public int getLogOperation()
/*    */   {
/* 47 */     return this.type - 1000;
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SmartsExpressionToken.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */