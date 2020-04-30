/*    */ package ambit2.smarts;
/*    */ 
/*    */ import org.openscience.cdk1.interfaces.IAtom;
/*    */ import org.openscience.cdk1.interfaces.IAtomContainer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Node
/*    */ {
/*    */   int sequenceElNum;
/*    */   IAtom[] atoms;
/*    */   
/*    */   public void nullifyAtoms(int n)
/*    */   {
/* 51 */     this.atoms = new IAtom[n];
/* 52 */     for (int i = 0; i < n; i++) {
/* 53 */       this.atoms[i] = null;
/*    */     }
/*    */   }
/*    */   
/*    */   public void copyAtoms(Node node) {
/* 58 */     this.atoms = new IAtom[node.atoms.length];
/* 59 */     for (int i = 0; i < this.atoms.length; i++) {
/* 60 */       this.atoms[i] = node.atoms[i];
/*    */     }
/*    */   }
/*    */   
/*    */   public Node cloneNode() {
/* 65 */     Node node = new Node();
/* 66 */     node.copyAtoms(this);
/* 67 */     return node;
/*    */   }
/*    */   
/*    */   public String toString(IAtomContainer container)
/*    */   {
/* 72 */     StringBuffer sb = new StringBuffer();
/* 73 */     sb.append("Node: el_num=" + this.sequenceElNum + " at:");
/* 74 */     for (int i = 0; i < this.atoms.length; i++) {
/* 75 */       if (this.atoms[i] != null)
/* 76 */         sb.append(" " + i + "->" + container.getAtomNumber(this.atoms[i]));
/*    */     }
/* 78 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
