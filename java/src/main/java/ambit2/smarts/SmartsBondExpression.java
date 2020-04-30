/*     */ package ambit2.smarts;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import org.openscience.cdk1.interfaces.IBond;
/*     */ import org.openscience.cdk1.interfaces.IBond.Order;
/*     */ import org.openscience.cdk1.isomorphism.matchers.smarts.SMARTSBond;
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
/*     */ public class SmartsBondExpression
/*     */   extends SMARTSBond
/*     */ {
/*     */   private static final long serialVersionUID = -93456789328678678L;
/*  40 */   public Vector<Integer> tokens = new Vector();
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean matches(IBond bond)
/*     */   {
/*  46 */     SmartsLogicalExpression sle = new SmartsLogicalExpression();
/*  47 */     for (int i = 0; i < this.tokens.size(); i++)
/*     */     {
/*  49 */       int tok = ((Integer)this.tokens.get(i)).intValue();
/*  50 */       if (tok < 1000)
/*     */       {
/*  52 */         sle.addArgument(getArgument(tok, bond));
/*     */       }
/*     */       else
/*  55 */         sle.addLogOperation(tok - 1000);
/*     */     }
/*  57 */     return sle.getValue();
/*     */   }
/*     */   
/*     */   boolean getArgument(int boType, IBond bond)
/*     */   {
/*  62 */     switch (boType)
/*     */     {
/*     */     case 1: 
/*  65 */       if ((bond.getOrder() == IBond.Order.SINGLE) && (!bond.getFlag(5)))
/*     */       {
/*  67 */         return true; }
/*     */       break;
/*     */     case 2: 
/*  70 */       if (bond.getOrder() == IBond.Order.DOUBLE)
/*  71 */         return true;
/*     */       break;
/*     */     case 3: 
/*  74 */       if (bond.getOrder() == IBond.Order.TRIPLE)
/*  75 */         return true;
/*     */       break;
/*     */     case 4: 
/*  78 */       if (bond.getFlag(5)) {
/*  79 */         return true;
/*     */       }
/*     */       
/*     */       break;
/*     */     case 5: 
/*  84 */       if (RingQueryBond.isRingBond(bond)) {
/*  85 */         return true;
/*     */       }
/*     */       
/*     */       break;
/*     */     case 6: 
/*     */     case 7: 
/*     */     case 8: 
/*     */     case 9: 
/*  93 */       if (bond.getOrder() == IBond.Order.SINGLE) {
/*  94 */         return true;
/*     */       }
/*     */       
/*     */       break;
/*     */     case 10: 
/*     */       break;
/*     */     case 11: 
/*     */       break;
/*     */     case 12: 
/*     */       break;
/*     */     }
/*     */     
/*     */     
/* 107 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 113 */     StringBuffer sb = new StringBuffer();
/* 114 */     for (int i = 0; i < this.tokens.size(); i++)
/*     */     {
/* 116 */       int tok = ((Integer)this.tokens.get(i)).intValue();
/* 117 */       if (tok < 1000)
/*     */       {
/* 119 */         if (tok < 8) {
/* 120 */           sb.append(SmartsConst.BondChars[tok]);
/*     */         }
/* 122 */         else if (tok == 8) {
/* 123 */           sb.append("/?");
/*     */         } else {
/* 125 */           sb.append("\\?");
/*     */         }
/*     */       } else
/* 128 */         sb.append(SmartsConst.LogOperationChars[(tok - 1000)]);
/*     */     }
/* 130 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SmartsBondExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
