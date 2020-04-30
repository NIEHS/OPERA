package com.sciome.opera.model.results;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sciome.opera.enums.Models;

public class OperaResult {
	Map<Models, List<OperaModelResult>> operaResults;
	
	public OperaResult(Map<Models, List<OperaModelResult>> operaResults) {
		this.operaResults = operaResults;
	}
	
	public List<DefaultModelResult> getAOHResult() {
		if(operaResults.containsKey(Models.AOH)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.AOH);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getBCFResult() {
		if(operaResults.containsKey(Models.BCF)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.BCF);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getBIODEGResult() {
		if(operaResults.containsKey(Models.BIODEG)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.BIODEG);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getBPResult() {
		if(operaResults.containsKey(Models.BP)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.BP);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<CATMOSModelResult> getCATMOSResult() {
		if(operaResults.containsKey(Models.CATMOS)) {
			List<CATMOSModelResult> modelList = new ArrayList<CATMOSModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.CATMOS);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((CATMOSModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<CERAPP_COMPARAModelResult> getCERAPPResult() {
		if(operaResults.containsKey(Models.CERAPP)) {
			List<CERAPP_COMPARAModelResult> modelList = new ArrayList<CERAPP_COMPARAModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.CERAPP);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((CERAPP_COMPARAModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<CERAPP_COMPARAModelResult> getCOMPARAResult() {
		if(operaResults.containsKey(Models.COMPARA)) {
			List<CERAPP_COMPARAModelResult> modelList = new ArrayList<CERAPP_COMPARAModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.COMPARA);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((CERAPP_COMPARAModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getHLResult() {
		if(operaResults.containsKey(Models.HL)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.HL);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getKMResult() {
		if(operaResults.containsKey(Models.KM)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.KM);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getKOAResult() {
		if(operaResults.containsKey(Models.KOA)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.KOA);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getKOCResult() {
		if(operaResults.containsKey(Models.KOC)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.KOC);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getLogPResult() {
		if(operaResults.containsKey(Models.LOGP)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.LOGP);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getMPResult() {
		if(operaResults.containsKey(Models.MP)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.MP);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<PKAModelResult> getPKAResult() {
		if(operaResults.containsKey(Models.PKA)) {
			List<PKAModelResult> modelList = new ArrayList<PKAModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.PKA);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((PKAModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getRBIODEGResult() {
		if(operaResults.containsKey(Models.RBIODEG)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.RBIODEG);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getRTResult() {
		if(operaResults.containsKey(Models.RT)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.RT);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getVPResult() {
		if(operaResults.containsKey(Models.VP)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.VP);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public List<DefaultModelResult> getWSResult() {
		if(operaResults.containsKey(Models.WS)) {
			List<DefaultModelResult> modelList = new ArrayList<DefaultModelResult>();
			List<OperaModelResult> resultList = operaResults.get(Models.WS);
			for(int i = 0; i < resultList.size(); i++) {
				modelList.add((DefaultModelResult)resultList.get(i));
			}
			return modelList;
		} else {
			return null;
		}
	}
	
	public Map<Models, List<OperaModelResult>> getOperaResults() {
		return this.operaResults;
	}
}
