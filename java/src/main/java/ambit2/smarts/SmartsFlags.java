/*    */ package ambit2.smarts;
/*    */ 
/*    */ 
/*    */ public class SmartsFlags
/*    */ {
/*    */   boolean mNeedNeighbourData;
/*    */   
/*    */   boolean mNeedValenceData;
/*    */   
/*    */   boolean mNeedRingData;
/*    */   boolean mNeedRingData2;
/*    */   boolean mNeedExplicitHData;
/*    */   boolean mNeedParentMoleculeData;
/*    */   boolean hasRecursiveSmarts;
/*    */   
/*    */   public String toString()
/*    */   {
/* 18 */     StringBuffer sb = new StringBuffer();
/* 19 */     sb.append("mNeedNeighbourData = " + this.mNeedNeighbourData + "\n");
/* 20 */     sb.append("mNeedValenceData = " + this.mNeedValenceData + "\n");
/* 21 */     sb.append("mNeedRingData = " + this.mNeedRingData + "\n");
/* 22 */     sb.append("mNeedRingData2 = " + this.mNeedRingData2 + "\n");
/* 23 */     sb.append("mNeedExplicitHData = " + this.mNeedExplicitHData + "\n");
/* 24 */     sb.append("mNeedParentMoleculeData = " + this.mNeedParentMoleculeData + "\n");
/* 25 */     sb.append("hasRecursiveSmarts = " + this.hasRecursiveSmarts + "\n");
/*    */     
/* 27 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SmartsFlags.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */