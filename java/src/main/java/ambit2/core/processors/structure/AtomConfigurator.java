/*     */ package ambit2.core.processors.structure;

/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;

/*     */ import org.openscience.cdk1.atomtype.CDKAtomTypeMatcher;
/*     */ import org.openscience.cdk1.interfaces.IAtom;
/*     */ import org.openscience.cdk1.interfaces.IAtomContainer;
/*     */ import org.openscience.cdk1.interfaces.IAtomType;
/*     */ import org.openscience.cdk1.interfaces.IPseudoAtom;
/*     */ import org.openscience.cdk1.tools.manipulator.AtomTypeManipulator;
/*     */ import org.openscience.cdk1.tools.periodictable.PeriodicTable;

/*     */
/*     */ import ambit2.base.config.Preferences;
/*     */ import ambit2.base.exceptions.AmbitException;
/*     */ import ambit2.base.processors.DefaultAmbitProcessor;

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
/*     */ public class AtomConfigurator/*     */ extends DefaultAmbitProcessor<IAtomContainer, IAtomContainer>
/*     */ {
	/*     */ private static final long serialVersionUID = -1245226849382037921L;

	/*     */
	/*     */ public IAtomContainer process(IAtomContainer paramIAtomContainer)/*     */ throws AmbitException
	/*     */ {
		/* 73 */ if (paramIAtomContainer == null)
			throw new AmbitException("Null molecule!");
		/* 74 */ if (paramIAtomContainer.getAtomCount() == 0)
		{
			throw new AmbitException("No atoms!");
			/*     */ }
		/*     */
		/*     */
		/* 79 */ CDKAtomTypeMatcher localCDKAtomTypeMatcher = CDKAtomTypeMatcher
				.getInstance(paramIAtomContainer.getBuilder());
		/* 80 */ Iterator localIterator = paramIAtomContainer.atoms().iterator();
		/* 81 */ ArrayList localArrayList = null;
		/* 82 */ IAtom localIAtom;
		while (localIterator.hasNext())
		{
			/* 83 */ localIAtom = (IAtom) localIterator.next();
			/* 84 */ if (!(localIAtom instanceof IPseudoAtom))
			{
				/*     */ try
				{
					/* 86 */ IAtomType localIAtomType = localCDKAtomTypeMatcher
							.findMatchingAtomType(paramIAtomContainer, localIAtom);
					/* 87 */ if (localIAtomType != null)
					{
						/* 88 */ AtomTypeManipulator.configure(localIAtom, localIAtomType);
						/* 89 */ localIAtom.setValency(localIAtomType.getValency());
						/* 90 */ localIAtom.setAtomicNumber(localIAtomType.getAtomicNumber());
						/* 91 */ localIAtom.setExactMass(localIAtomType.getExactMass());
						/*     */ }
					else
					{
						/* 93 */ if (localArrayList == null)
							localArrayList = new ArrayList();
						/* 94 */ if (localArrayList.indexOf(localIAtom.getSymbol()) < 0)
							/* 95 */ localArrayList
									.add(String.format("%s", new Object[] { localIAtom.getSymbol() }));
						/*     */ }
					/*     */ }
				catch (Exception localException)
				{
					/* 98 */ if (localArrayList == null)
						localArrayList = new ArrayList();
					/* 99 */ if (localArrayList.indexOf(localIAtom.getSymbol()) < 0)
					{
						/* 100 */ localArrayList
								.add(String.format("%s", new Object[] { localIAtom.getSymbol() }));
						/*     */ }
					/*     */ }
				/*     */ }
			/*     */ }
		/*     */
		/*     */
		/* 107 */ if ((localArrayList != null)
				&& ("true".equals(Preferences.getProperty(Preferences.STOP_AT_UNKNOWNATOMTYPES))))
		{
			/* 108 */ Collections.sort(localArrayList);
			/* 109 */ throw new AmbitException(localArrayList.toString());
			/*     */ }
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
		/* 132 */ localIterator = paramIAtomContainer.atoms().iterator();
		/* 133 */ while (localIterator.hasNext())
		{
			/* 134 */ localIAtom = (IAtom) localIterator.next();
			/* 135 */ if ((localIAtom.getAtomicNumber() == null)
					|| (localIAtom.getAtomicNumber().intValue() == 0))
			{
				/* 136 */ Integer localInteger = PeriodicTable.getAtomicNumber(localIAtom.getSymbol());
				/* 137 */ if (localInteger != null)
				{
					/* 138 */ localIAtom.setAtomicNumber(Integer.valueOf(localInteger.intValue()));
					/*     */ }
				/*     */ }
			/*     */ }
		/* 142 */ return paramIAtomContainer;
		/*     */ }
	/*     */ }

/*
 * Location: /Users/jasonphillips/Desktop/ambit2-core-2.4.7-SNAPSHOT.jar!/ambit2/core/processors/structure/
 * AtomConfigurator.class Java compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */
