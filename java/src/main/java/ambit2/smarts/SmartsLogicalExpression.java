/*     */ package ambit2.smarts;
/*     */ 
/*     */ import java.util.Stack;
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
/*     */ public class SmartsLogicalExpression
/*     */ {
/*  35 */   Stack<Integer> operations = new Stack();
/*  36 */   Stack<Boolean> arguments = new Stack();
/*     */   
/*     */   public boolean getValue()
/*     */   {
/*  40 */     while (!this.operations.empty()) {
/*  41 */       doStackOperation();
/*     */     }
/*  43 */     return ((Boolean)this.arguments.pop()).booleanValue();
/*     */   }
/*     */   
/*     */   public void addLogOperation(int op)
/*     */   {
/*  48 */     boolean StackHighPriority = true;
/*  49 */     while ((!this.operations.empty()) && (StackHighPriority))
/*     */     {
/*  51 */       StackHighPriority = doPriorityOperation(op);
/*     */     }
/*     */     
/*  54 */     this.operations.push(new Integer(op));
/*     */   }
/*     */   
/*     */   public void addArgument(boolean arg)
/*     */   {
/*  59 */     this.arguments.push(new Boolean(arg));
/*     */   }
/*     */   
/*     */   void doNOT()
/*     */   {
/*  64 */     boolean arg = ((Boolean)this.arguments.pop()).booleanValue();
/*  65 */     this.arguments.push(new Boolean(!arg));
/*     */   }
/*     */   
/*     */   void doAND()
/*     */   {
/*  70 */     boolean arg1 = ((Boolean)this.arguments.pop()).booleanValue();
/*  71 */     boolean arg2 = ((Boolean)this.arguments.pop()).booleanValue();
/*  72 */     this.arguments.push(new Boolean((arg1) && (arg2)));
/*     */   }
/*     */   
/*     */   void doOR()
/*     */   {
/*  77 */     boolean arg1 = ((Boolean)this.arguments.pop()).booleanValue();
/*  78 */     boolean arg2 = ((Boolean)this.arguments.pop()).booleanValue();
/*  79 */     this.arguments.push(new Boolean((arg1) || (arg2)));
/*     */   }
/*     */   
/*     */   boolean doPriorityOperation(int op)
/*     */   {
/*  84 */     int top = ((Integer)this.operations.peek()).intValue();
/*  85 */     if (SmartsConst.priority[top][op] < 0) {
/*  86 */       return false;
/*     */     }
/*  88 */     this.operations.pop();
/*  89 */     switch (top)
/*     */     {
/*     */     case 0: 
/*  92 */       doNOT();
/*  93 */       break;
/*     */     
/*     */     case 1: 
/*     */     case 3: 
/*  97 */       doAND();
/*  98 */       break;
/*     */     
/*     */     case 2: 
/* 101 */       doOR();
/*     */     }
/*     */     
/* 104 */     return true;
/*     */   }
/*     */   
/*     */   void doStackOperation()
/*     */   {
/* 109 */     int top = ((Integer)this.operations.pop()).intValue();
/* 110 */     switch (top)
/*     */     {
/*     */     case 0: 
/* 113 */       doNOT();
/* 114 */       break;
/*     */     
/*     */     case 1: 
/*     */     case 3: 
/* 118 */       doAND();
/* 119 */       break;
/*     */     
/*     */     case 2: 
/* 122 */       doOR();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SmartsLogicalExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */