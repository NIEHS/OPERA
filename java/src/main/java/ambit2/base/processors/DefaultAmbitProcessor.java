/*    */ package ambit2.base.processors;

/*    */ import java.beans.PropertyChangeListener;
/*    */ import java.beans.PropertyChangeSupport;

/*    */
/*    */ import ambit2.base.interfaces.IProcessor;

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
/*    */ public abstract class DefaultAmbitProcessor<Target, Result>
		/*    */ implements IProcessor<Target, Result>
/*    */ {
	/*    */ private static final long			serialVersionUID		= -4959472085664049891L;
	/* 48 */ protected PropertyChangeSupport	propertyChangeSupport	= null;
	/* 50 */ protected boolean					enabled					= true;

	/*    */
	/*    */
	/*    */
	/*    */
	/*    */ public DefaultAmbitProcessor()
	/*    */ {
		/* 57 */ this.propertyChangeSupport = new PropertyChangeSupport(this);
		/*    */ }

	/*    */
	/*    */ public String toString()
	/*    */ {
		/* 62 */ return "Default processor";
		/*    */ }

	/*    */
	/* 65 */ public synchronized boolean isEnabled()
	{
		return this.enabled;
	}

	/*    */
	/*    */ public synchronized void setEnabled(boolean paramBoolean)
	{
		/* 68 */ this.enabled = paramBoolean;
		/*    */ }

	/*    */
	/*    */ public void addPropertyChangeListener(String paramString,
			PropertyChangeListener paramPropertyChangeListener)
	/*    */ {
		/* 73 */ this.propertyChangeSupport.addPropertyChangeListener(paramString,
				paramPropertyChangeListener);
		/*    */ }

	/*    */
	/*    */
	/*    */ public void removePropertyChangeListener(String paramString,
			PropertyChangeListener paramPropertyChangeListener)
	/*    */ {
		/* 79 */ this.propertyChangeSupport.removePropertyChangeListener(paramString,
				paramPropertyChangeListener);
		/*    */ }

	/*    */
	/*    */ public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
	/*    */ {
		/* 84 */ this.propertyChangeSupport.addPropertyChangeListener(paramPropertyChangeListener);
		/*    */ }

	/*    */
	/*    */ public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
	{
		/* 88 */ this.propertyChangeSupport.removePropertyChangeListener(paramPropertyChangeListener);
		/*    */ }

	/*    */
	/*    */ public long getID()
	{
		/* 92 */ return -4959472085664049891L;
		/*    */ }
	/*    */ }

/*
 * Location:
 * /Users/jasonphillips/Desktop/ambit2-base-2.4.7-SNAPSHOT.jar!/ambit2/base/processors/DefaultAmbitProcessor.
 * class Java compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */