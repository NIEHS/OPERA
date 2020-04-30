package com.sciome.opera.calc.calculators;

import java.util.HashSet;
import java.util.Set;

import com.sciome.opera.enums.Models;

public class ModelFactory
{
	public Set<ModelCalculator> getModelCalculator(Set<Models> models)
	{
		Set<ModelCalculator> calculators = new HashSet<>();
		boolean pKaAdded = false;
		boolean logPAdded = false;
		for (Models model : models)
		{
			switch (model)
			{
				case AOH:
					calculators.add(new AOHCalculator());
					break;
				case BCF:
					calculators.add(new BCFCalculator());
					break;
				case BIODEG:
					calculators.add(new BIODEGCalculator());
					break;
				case BP:
					calculators.add(new BPCalculator());
					break;
				case CATMOS:
					calculators.add(new CATMOSCalculator());
					break;
				case CERAPP:
					calculators.add(new CERAPP_COMPARACalculator(true));
					break;
				case COMPARA:
					calculators.add(new CERAPP_COMPARACalculator(false));
					break;
				case HL:
					calculators.add(new HLCalculator());
					break;
				case KOA:
					calculators.add(new KOACalculator());
					break;
				case KOC:
					calculators.add(new KOCCalculator());
					break;
				case KM:
					calculators.add(new KMCalculator());
					break;
				case LOGP:
					calculators.add(new LogPCalculator());
					logPAdded = true;
					break;
				case MP:
					calculators.add(new MPCalculator());
					break;
				case PKA:
					calculators.add(new PKACalculator());
					pKaAdded = true;
					break;
				case RBIODEG:
					calculators.add(new RBIODEGCalculator());
					break;
				case RT:
					calculators.add(new RTCalculator());
					break;
				case VP:
					calculators.add(new VPCalculator());
					break;
				case WS:
					calculators.add(new WSCalculator());
					break;
				default:
			}
		}
		
		//Special case for LogD
		if(models.contains(Models.LOGD)) {
			if(!pKaAdded) {
				calculators.add(new PKACalculator());
			}
			if(!logPAdded) {
				calculators.add(new LogPCalculator());
			}
		}
		return calculators;

	}
}
