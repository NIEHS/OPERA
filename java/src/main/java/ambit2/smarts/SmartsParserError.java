/*    */ package ambit2.smarts;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SmartsParserError
/*    */ {
/*    */   public String sourceSmarts;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String message;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int position;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String param;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SmartsParserError(String smarts, String msg, int pos, String par)
/*    */   {
/* 40 */     this.sourceSmarts = smarts;
/* 41 */     this.message = msg;
/* 42 */     this.position = pos;
/* 43 */     this.param = par;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 48 */     return this.message;
/*    */   }
/*    */   
/*    */   public String getError()
/*    */   {
/* 53 */     if (this.position < 0) {
/* 54 */       return this.message + " " + this.param;
/*    */     }
/*    */     
/* 57 */     if (this.position > this.sourceSmarts.length())
/* 58 */       this.position = this.sourceSmarts.length();
/* 59 */     return this.message + ": " + this.sourceSmarts.substring(0, this.position) + "  " + this.param;
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SmartsParserError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */