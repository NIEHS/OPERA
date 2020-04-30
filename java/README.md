This project is a port of the Matlab version of OPERA which can be found here: https://github.com/kmansouri/OPERA. The currently ported version is 2.1

There are 3 input parameters that are required:
* Text from a smiles string file. Either .smi or .sdf format
* A set of models to run
* Boolean for including neighbors in output
	
The list of currently available models include:  
* AOH: (LogOH) in cm3/molecule-sec: The OH rate constant for the atmospheric, gas-phase reaction between photochemically produced hydroxyl radicals and organic chemicals.  
* BCF: (Log) Fish bioconcentration factor  
* BIODEG: (LogHalfLife) in days biodegradation half-life for compounds containing only carbon and hydrogen (i.e. hydrocarbons).  
* BP: (in deg C) Boiling Point at 760 mm Hg  
* CATMOS: Collaborative Acute Toxicity Modeling Suite. Very-Toxic, Non-Toxic, EPA categories, GHS categories, LD50 (Log mg/kg) (https://doi.org/10.1016/j.comtox.2018.08.002)  
* CERAPP: Collaborative Estrogen Receptor Activity Prediction Project. Binding, Agonist and Antagonist Estrogen Receptor activity (https://ehp.niehs.nih.gov/15-10267/)  
* COMPARA:  Collaborative Modeling Project for Androgen Receptor Activity. Binding, Agonist and Antagonist Androgen Receptor activity (https://doi.org/10.13140/RG.2.2.19612.80009, https://doi.org/10.13140/RG.2.2.21850.03520)  
* HL: (LogHL) in atm-m3/mole. The Henrys Law constant (air/water partition coefficient) at 25C  
* KM: (Log KmHL) half-lives in days. The whole body primary biotransformation rate constant for organic chemicals in fish.  
* KOA: (Log) The octanol/air partition coefficient.  
* KOC: (Log) in L/Kg the soil adsorption coefficient of organic compounds.  The ratio of the amount of chemical adsorbed per unit weight of organic carbon in the soil or sediment to the concentration of the chemical in solution at equilibrium.  
* LogP: Octanol-water partition coefficient, log KOW, of chemicals.  
* MP: Melting Point in deg C  
* PKA: acid dissociation constant  
* RBIODEG: Ready biodegradability of organic chemicals  (classification: 0/1)  
* RT: (in minutes) HPLC retention time.  
* VP: (Log) in mmHg Vapor Pressure experimental values between 15 and 30 deg C (majority at 25-20C)  
* WS: (Log) in Molar moles/L Water solubility at 25C.  

Models that will eventually be added are:  
* Clint: hepatic intrinsic clearance (human)  
* FuB: Plasma fraction unbound (human)  

Running the models will provide you with an OperaResults object with data for all the models that were ran.  
Trying to retrieve data from models that weren't run will give you null as a result.  

Here are some code examples:  

	//Running a single chemical through one model with neighbor output
	Opera opera = new Opera();
	Set<Models> models = new HashSet<Models>();
	models.add(Models.AOH);
	OperaResult result = opera.calc("NC(N)=N 50-01-1", models, true);
	DefaultModelResult aohResult = result.getAOHResult().get(0);
	//Get chemical descriptor values
	aohResult.getChemicalDescriptors().getPadelDescriptorValues();
	aohResult.getChemicalDescriptors().getCDKDescriptorValues();
	
	//Running a single chemical through all models without neighbor output
	Opera opera = new Opera();
	Set<Models> models = new HashSet<Models>(Arrays.asList(Models.values()));
	OperaResult result = opera.calc("NC(N)=N 50-01-1", models, false);
	//Get results for each model with getter methods
	DefaultModelResult aohResult = result.getAOHResult().get(0);
	DefaultModelResult bcfResult = result.getBCFResult().get(0);
	CATMOSModelResult catmosResult = result.getCATMOSResult().get(0);
	DefaultModelResult vtResult = catmosResult.getVt();
	...and so on
	
	//Running multiple chemicals through all models without neighbor output
	Set<Models> models = new HashSet<Models>(Arrays.asList(Models.values()));
	OperaResult result = opera.calc("NC(N)=N 50-01-1\nCC1CC2C3CCC4=CC(=O)C=CC4(C)C3(F)C(O)CC2(C)C1(O)C(=O)CO	50-02-2", models, false);
	//Get output by looping through models instead of calling methods individually
	for(Models model : models) {
		//Loop through chemicals
		for (OperaModelResult operaResult : result.getOperaResults().get(model))
		{
			String chemId = operaResult.getChemicalDescriptors().getChemID();
			if (operaResult instanceof DefaultModelResult) {
				DefaultModelResult defaultResult = (DefaultModelResult) operaResult;
				System.out.println(chemId + " | " + model + " predicted value: " + defaultResult.getPredictedValue());
			} else if (operaResult instanceof PKAModelResult) {
				PKAModelResult pKaResult = (PKAModelResult) operaResult;
				System.out.println(chemId + " | " + model + " pKa_a predicted: " + pKaResult.getpKa_a_pred());
				System.out.println(chemId + " | " + model + " pKa_b predicted: " + pKaResult.getpKa_b_pred());
			} else if (operaResult instanceof CERAPP_COMPARAModelResult) {
				CERAPP_COMPARAModelResult cerappResult = (CERAPP_COMPARAModelResult) operaResult;
				System.out.println(chemId + " | " + model + " Ago predicted value: " + cerappResult.getAgo().getPredictedValue());
				System.out.println(chemId + " | " + model + " Anta predicted value: " + cerappResult.getAnta().getPredictedValue());
				System.out.println(chemId + " | " + model + " Bind predicted value: " + cerappResult.getBind().getPredictedValue());
			} else if (operaResult instanceof CATMOSModelResult) {
				CATMOSModelResult catmosResult = (CATMOSModelResult) operaResult;
				System.out.println(chemId + " | " + model + " VT predicted value: " + catmosResult.getVt().getPredictedValue());
				System.out.println(chemId + " | " + model + " NT predicted value: " + catmosResult.getNt().getPredictedValue());
				System.out.println(chemId + " | " + model + " EPA predicted value: " + catmosResult.getEpa().getPredictedValue());
				System.out.println(chemId + " | " + model + " GHS predicted value: " + catmosResult.getGhs().getPredictedValue());
				System.out.println(chemId + " | " + model + " LD50 predicted value: " + catmosResult.getLd50().getPredictedValue());
			} else if (operaResult instanceof LogDModelResult) {
				LogDModelResult logDResult = (LogDModelResult)operaResult;
				System.out.println(chemId + " | " + model + " LogD55 predicted: " + logDResult.getLogD55_pred());
				System.out.println(chemId + " | " + model + " LogD74 predicted: " + logDResult.getLogD74_pred());
		   }
		}
	}