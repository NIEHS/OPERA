/*     */ package ambit2.base.config;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Properties;
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
/*     */ public class Preferences
/*     */ {
/*     */   protected static PropertyChangeSupport propertyChangeSupport;
/*  38 */   public static String FASTSMARTS = "FASTSMARTS";
/*  39 */   public static String SHOW_AROMATICITY = "showAromaticity";
/*  40 */   public static String GENERATE2D = "generate2D";
/*  41 */   public static String DEFAULT_DIR = "defaultDir";
/*  42 */   public static String START_MYSQL = "startMySQL";
/*  43 */   public static String MAXRECORDS = "MAXRECORDS";
/*  44 */   public static String SMILESPARSER = "smilesParser";
/*  45 */   public static String STOP_AT_UNKNOWNATOMTYPES = "atomtypes.stop";
/*  46 */   public static String MOPAC_DIR = "mopac.dir";
/*  47 */   public static String MOPAC_EXE = "mopac.exe";
/*  48 */   public static String MENGINE_WIN = "mengine.win";
/*  49 */   public static String MENGINE_LINUX = "mengine.linux";
/*  50 */   public static String OPENBABEL_WIN = "obabel.win";
/*  51 */   public static String OPENBABEL_LINUX = "obabel.linux";
/*  52 */   public static String SMI2SDF_WIN = "smi2sdf.win";
/*  53 */   public static String SMI2SDF_LINUX = "smi2sdf.linux";
/*  54 */   public static String SMILES_FIELD = "SMILES";
/*  55 */   public static String SMILES_GEN = "smi2sdf.smiles.generate";
/*  56 */   public static String SCHEME = "Scheme";
/*  57 */   public static String DATABASE = "Database";
/*  58 */   public static String HOST = "Host";
/*  59 */   public static String PORT = "Port";
/*  60 */   public static String USER = "User";
/*  61 */   public static String PASSWORD = "Password";
/*  62 */   public static String TIMEOUT = "timeout";
/*  63 */   public static String REMOTELOOKUP = "remotelookup";
/*  64 */   public static String REMOTELOOKUP_URI = "remotelookup_uri";
/*     */   
/*     */   protected static final String filename = "ambit2.pref";
/*  67 */   protected static Properties props = null;
/*  68 */   public static enum VTAGS { General,  Structure,  Conversion3D,  Database,  RemoteQuery;
/*  69 */     private VTAGS() {} } public static enum VINDEX { NAME,  TITLE,  VALUE,  CLASS,  HINT,  HIDDEN,  TAG;
/*     */     private VINDEX() {} }
/*  71 */   public static Object[][] default_values = { { DATABASE, "Default database schema", "ambit2", String.class, "This is the default database schema AmbitXT will attempt to connect to when a database connection is required.", Boolean.valueOf(false), VTAGS.Database }, { PORT, "Default database port", "3306", String.class, "This is the default port AmbitXT will attempt to connect with when a database connection is required. It is assumed MySQL server runs on this port.", Boolean.valueOf(false), VTAGS.Database }, { USER, "Default user", "guest", String.class, "This is the default user name AmbitXT will attempt to connect with when a database connection is required.", Boolean.valueOf(false), VTAGS.Database }, { HOST, "Host", "localhost", String.class, "This is the default host AmbitXT will attempt to connect with when a database connection is required. It is assumed MySQL server runs on this host.", Boolean.valueOf(false), VTAGS.Database }, { SCHEME, "Scheme", "jdbc:mysql", String.class, "", Boolean.valueOf(true), VTAGS.Database }, { START_MYSQL, "Start MySQL automatically", "true", Boolean.class, "If checked, the embedded MySQL server will be automatically started upon application launch", Boolean.valueOf(false), VTAGS.Database }, { MAXRECORDS, "Maximum number of records", Integer.valueOf(2000), String.class, "Maximum number of records to be returned by a search query", Boolean.valueOf(false), VTAGS.Database }, { TIMEOUT, "Timeout for search results, ms", Integer.valueOf(60000), String.class, "Timeout of search queries, in milliseconds ", Boolean.valueOf(false), VTAGS.Database }, { FASTSMARTS, "Use SMARTS accelerator", "true", Boolean.class, "Accelerate SMARTS search", Boolean.valueOf(false), VTAGS.Structure }, { SHOW_AROMATICITY, "Show circle in an aromatic ring", "true", Boolean.class, "Toggles displaying aromatic rings", Boolean.valueOf(true), VTAGS.Structure }, { REMOTELOOKUP, "Remote lookup enabled", "false", Boolean.class, "Enable remote lookup for CAS and EINECS", Boolean.valueOf(false), VTAGS.RemoteQuery }, { REMOTELOOKUP_URI, "Remote lookup URI", "http://apps.ideaconsult.net:8080/ambit2/query/compound/search/all?search=%s&max=1&media=chemical%%2Fx-mdl-sdfile", String.class, "OpenTox web service address for compound lookup", Boolean.valueOf(false), VTAGS.RemoteQuery }, { GENERATE2D, "Generate 2d coordinates if none exist", "true", Boolean.class, "Generate 2D coordinates of the structures, entered as SMILES", Boolean.valueOf(false), VTAGS.Structure }, { SMILESPARSER, "Use Openbabel SMILES parser", "true", Boolean.class, "Toggles usage of Openbabel vs. CDK SMILES parser. Openbabel available at http://openbabel.org/", Boolean.valueOf(false), VTAGS.Structure }, { STOP_AT_UNKNOWNATOMTYPES, "Stop at unknown atom types", "false", Boolean.class, "If checked, will report an error when an unknown atom type is encountered.", Boolean.valueOf(false), VTAGS.Structure }, { DEFAULT_DIR, "Default directory", "", String.class, "This folder will appear as a default in the file open or file save dialogs", Boolean.valueOf(false), VTAGS.General }, { SMILES_GEN, "Generate the smiles submitted to smi2sdf", "true", Boolean.class, "Generate the smiles , submitted to smi2sdf, or use the one in the file", Boolean.valueOf(true), VTAGS.Conversion3D } };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static Properties getDefault()
/*     */   {
/* 109 */     Properties localProperties = new Properties();
/* 110 */     for (int i = 0; i < default_values.length; i++) {
/* 111 */       localProperties.setProperty(default_values[i][0].toString(), default_values[i][2].toString());
/*     */     }
/* 113 */     return localProperties;
/*     */   }
/*     */   
/* 116 */   protected static Properties loadProperties() throws IOException { Properties localProperties = new Properties();
/* 117 */     FileInputStream localFileInputStream = new FileInputStream(new File("ambit2.pref"));
/* 118 */     localProperties.load(localFileInputStream);
/* 119 */     localFileInputStream.close();
/* 120 */     return localProperties;
/*     */   }
/*     */   
/* 123 */   public static Properties getProperties() { if (props == null)
/*     */       try {
/* 125 */         props = loadProperties();
/* 126 */         if (props.size() == 0)
/* 127 */           props = getDefault();
/* 128 */         propertyChangeSupport = new PropertyChangeSupport(props);
/*     */       } catch (Exception localException) {
/* 130 */         props = getDefault();
/* 131 */         propertyChangeSupport = new PropertyChangeSupport(props);
/*     */       }
/* 133 */     return props;
/*     */   }
/*     */   
/*     */   public static void saveProperties(String paramString) throws IOException {
/* 137 */     if (props == null) return;
/* 138 */     FileOutputStream localFileOutputStream = new FileOutputStream(new File("ambit2.pref"));
/* 139 */     props.store(localFileOutputStream, paramString);
/* 140 */     localFileOutputStream.close();
/*     */   }
/*     */   
/* 143 */   public static void setProperty(String paramString1, String paramString2) { Properties localProperties = getProperties();
/* 144 */     String str = localProperties.getProperty(paramString1);
/* 145 */     localProperties.put(paramString1, paramString2);
/* 146 */     propertyChangeSupport.firePropertyChange(paramString1, str, paramString2);
/*     */   }
/*     */   
/* 149 */   public static String getProperty(String paramString) { Properties localProperties1 = getProperties();
/* 150 */     Object localObject = localProperties1.get(paramString);
/* 151 */     if (localObject == null) {
/* 152 */       Properties localProperties2 = getDefault();
/* 153 */       localObject = localProperties2.get(paramString);
/* 154 */       if (localObject == null) localObject = "";
/* 155 */       setProperty(paramString, localObject.toString());
/*     */     }
/* 157 */     return localObject.toString();
/*     */   }
/*     */   
/* 160 */   public static PropertyChangeSupport getPropertyChangeSupport() { if (propertyChangeSupport == null)
/* 161 */       getProperties();
/* 162 */     return propertyChangeSupport;
/*     */   }
/*     */   
/*     */   public static void setPropertyChangeSupport(PropertyChangeSupport paramPropertyChangeSupport) {
/* 166 */     propertyChangeSupport = paramPropertyChangeSupport;
/*     */   }
/*     */ }


/* Location:              /Users/jasonphillips/Desktop/ambit2-base-2.4.7-SNAPSHOT.jar!/ambit2/base/config/Preferences.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */