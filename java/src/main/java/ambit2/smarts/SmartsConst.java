/*     */ package ambit2.smarts;
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
/*     */ public class SmartsConst
/*     */ {
/*     */   public static final int ABSOLUTE_CIS = 10;
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
/*     */   public static final int ABSOLUTE_TRANS = 11;
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
/*  38 */   public static char[] LogOperationChars = { '!', '&', ',', ';' };
/*     */   
/*     */   public static final int LO_NOT = 0;
/*     */   
/*     */   public static final int LO_AND = 1;
/*     */   public static final int LO_OR = 2;
/*     */   public static final int LO_ANDLO = 3;
/*     */   public static final int LO = 1000;
/*  46 */   public static char[] AtomPrimChars = { '*', 'a', 'A', 'D', 'H', 'h', 'R', 'r', 'v', 'X', '-', '#', '@', 'm', '_', 'x', 'i', 'G', 'X', 'N', 'b', '^' };
/*     */   
/*     */   public static final int AP_ANY = 0;
/*     */   
/*     */   public static final int AP_a = 1;
/*     */   
/*     */   public static final int AP_A = 2;
/*     */   
/*     */   public static final int AP_D = 3;
/*     */   public static final int AP_H = 4;
/*     */   public static final int AP_h = 5;
/*     */   public static final int AP_R = 6;
/*     */   public static final int AP_r = 7;
/*     */   public static final int AP_v = 8;
/*     */   public static final int AP_X = 9;
/*     */   public static final int AP_Charge = 10;
/*     */   public static final int AP_AtNum = 11;
/*     */   public static final int AP_Chiral = 12;
/*     */   public static final int AP_Mass = 13;
/*     */   public static final int AP_Recursive = 14;
/*     */   public static final int AP_x = 15;
/*     */   public static final int AP_iMOE = 16;
/*     */   public static final int AP_GMOE = 17;
/*     */   public static final int AP_XMOE = 18;
/*     */   public static final int AP_NMOE = 19;
/*     */   public static final int AP_vMOE = 20;
/*     */   public static final int AP_OB_Hybr = 21;
/*  73 */   public static char[] BondChars = { '~', '-', '=', '#', ':', '@', '/', '\\' };
/*     */   
/*     */   public static final int BT_ANY = 0;
/*     */   
/*     */   public static final int BT_SINGLE = 1;
/*     */   
/*     */   public static final int BT_DOUBLE = 2;
/*     */   
/*     */   public static final int BT_TRIPLE = 3;
/*     */   
/*     */   public static final int BT_AROMATIC = 4;
/*     */   
/*     */   public static final int BT_RING = 5;
/*     */   
/*     */   public static final int BT_UP = 6;
/*     */   
/*     */   public static final int BT_DOWN = 7;
/*     */   
/*     */   public static final int BT_UPUNSPEC = 8;
/*     */   
/*     */   public static final int BT_DOWNUNSPEC = 9;
/*     */   
/*     */   public static final int BT_CIS = 10;
/*     */   
/*     */   public static final int BT_CISUNSPEC = 11;
/*     */   
/*     */   public static final int BT_TRANS = 12;
/*     */   
/*     */   public static final int BT_TRANSUNSPEC = 13;
/*     */   public static final int BT_UNDEFINED = 100;
/*     */   public static final int ChC_Unspec = 0;
/*     */   public static final int ChC_AntiClock = 1;
/*     */   public static final int ChC_Clock = 2;
/*     */   public static final int ChC_R = 1001;
/*     */   public static final int ChC_S = 1002;
/*     */   public static final int SSM_SINGLE = 0;
/*     */   public static final int SSM_NON_OVERLAPPING = 1;
/*     */   public static final int SSM_NON_IDENTICAL = 2;
/*     */   public static final int SSM_NON_EQUIVALENT = 3;
/*     */   public static final int SSM_ALL = 10;
/*     */   public static final int SSM_NON_IDENTICAL_FIRST = 21;
/*     */   public static final int SMRK_UNSPEC_ATOM = -100000;
/* 115 */   public static int[][] priority = { { -1, 1, 1, 1 }, { -1, 0, 1, 1 }, { -1, -1, 0, 1 }, { -1, -1, -1, 0 } };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */   public static String[] elSymbols = { "", "H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg" };
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
/*     */   public static int getLogOperationCharNumber(char ch)
/*     */   {
/* 155 */     for (int i = 0; i < LogOperationChars.length; i++)
/*     */     {
/* 157 */       if (ch == LogOperationChars[i])
/* 158 */         return i;
/*     */     }
/* 160 */     return -1;
/*     */   }
/*     */   
/*     */   public static int getBondCharNumber(char ch)
/*     */   {
/* 165 */     for (int i = 0; i < BondChars.length; i++)
/*     */     {
/* 167 */       if (ch == BondChars[i])
/* 168 */         return i;
/*     */     }
/* 170 */     return -1;
/*     */   }
/*     */   
/*     */   public static int getElementNumber(String symbol)
/*     */   {
/* 175 */     for (int i = 1; i < elSymbols.length; i++)
/*     */     {
/* 177 */       if (symbol.compareTo(elSymbols[i]) == 0)
/* 178 */         return i;
/*     */     }
/* 180 */     return -1;
/*     */   }
/*     */   
/*     */   public static int getElementNumberFromChar(char symbol)
/*     */   {
/* 185 */     switch (symbol)
/*     */     {
/*     */     case 'C': 
/* 188 */       return 6;
/*     */     case 'N': 
/* 190 */       return 7;
/*     */     case 'O': 
/* 192 */       return 8;
/*     */     case 'H': 
/* 194 */       return 1;
/*     */     case 'B': 
/* 196 */       return 5;
/*     */     case 'F': 
/* 198 */       return 9;
/*     */     case 'I': 
/* 200 */       return 53;
/*     */     case 'P': 
/* 202 */       return 15;
/*     */     case 'S': 
/* 204 */       return 16;
/*     */     case 'K': 
/* 206 */       return 19;
/*     */     case 'V': 
/* 208 */       return 23;
/*     */     case 'Y': 
/* 210 */       return 39;
/*     */     case 'W': 
/* 212 */       return 74;
/*     */     case 'U': 
/* 214 */       return 92;
/*     */     }
/*     */     
/* 217 */     return -1;
/*     */   }
/*     */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-smarts-2.4.7-SNAPSHOT.jar!/ambit2/smarts/SmartsConst.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */