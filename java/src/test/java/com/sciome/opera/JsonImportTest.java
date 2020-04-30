package com.sciome.opera;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.sciome.opera.model.training.model.AOHTraining;
import com.sciome.opera.model.training.model.BCFTraining;
import com.sciome.opera.model.training.model.BIODEGTraining;
import com.sciome.opera.model.training.model.BPTraining;
import com.sciome.opera.model.training.model.HLTraining;
import com.sciome.opera.model.training.model.KMTraining;
import com.sciome.opera.model.training.model.KOATraining;
import com.sciome.opera.model.training.model.KOCTraining;
import com.sciome.opera.model.training.model.LOGPTraining;
import com.sciome.opera.model.training.model.MPTraining;
import com.sciome.opera.model.training.model.STRPTraining;
import com.sciome.opera.model.training.model.VPTraining;
import com.sciome.opera.model.training.model.WSTraining;
import com.sciome.opera.model.training.model.CATMoS.CATMOSTraining;
import com.sciome.opera.model.training.model.CERAPP_COMPARA.CERAPPTraining;
import com.sciome.opera.model.training.model.CERAPP_COMPARA.COMPARATraining;
import com.sciome.opera.model.training.model.RBIODEG.RBIODEGTraining;
import com.sciome.opera.model.training.model.RT.RTTraining;
import com.sciome.opera.model.training.model.pKa.PKATraining;

public class JsonImportTest {

	@Test
	public void test() {
		try {
			AOHTraining aoh = AOHTraining.getInstance();
			BCFTraining bcf = BCFTraining.getInstance();
			BIODEGTraining biodeg = BIODEGTraining.getInstance();
			BPTraining bp = BPTraining.getInstance();
			CATMOSTraining catmos = CATMOSTraining.getInstance();
			CERAPPTraining cerapp = CERAPPTraining.getInstance();
			COMPARATraining compara = COMPARATraining.getInstance();
			HLTraining hl = HLTraining.getInstance();
			KMTraining km = KMTraining.getInstance();
			KOATraining koa = KOATraining.getInstance();
			KOCTraining koc = KOCTraining.getInstance();
			LOGPTraining logp = LOGPTraining.getInstance();
			MPTraining mp = MPTraining.getInstance();
			PKATraining pka = PKATraining.getInstance();
			RBIODEGTraining rbiodeg = RBIODEGTraining.getInstance();
			RTTraining rt = RTTraining.getInstance();
			STRPTraining strp = STRPTraining.getInstance();
			VPTraining vp = VPTraining.getInstance();
			WSTraining ws = WSTraining.getInstance();
			
			strp.loadData();
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
