/*    */ package ambit2.base.exceptions;
/*    */ 
/*    */ 
/*    */ public class AmbitException
/*    */   extends Exception
/*    */ {
/*    */   public AmbitException() {}
/*    */   
/*    */   public AmbitException(String paramString)
/*    */   {
/* 11 */     super(paramString);
/*    */   }
/*    */   
/*    */   public AmbitException(Throwable paramThrowable)
/*    */   {
/* 16 */     super(paramThrowable);
/*    */   }
/*    */   
/*    */   public AmbitException(String paramString, Throwable paramThrowable)
/*    */   {
/* 21 */     super(paramString, paramThrowable);
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-base-2.4.7-SNAPSHOT.jar!/ambit2/base/exceptions/AmbitException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */