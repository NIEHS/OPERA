package com.sciome.opera.model.training.model.RBIODEG;

import java.util.List;

public class RBIODEGModel {
	List<Integer>		class_calc;
	RBIODEGClassParam	class_param;
	RBIODEGSet			set;
	List<Integer>		desc_i;
	
	public List<Integer> getClass_calc() {
		return class_calc;
	}
	public void setClass_calc(List<Integer> class_calc) {
		this.class_calc = class_calc;
	}
	public RBIODEGClassParam getClass_param() {
		return class_param;
	}
	public void setClass_param(RBIODEGClassParam class_param) {
		this.class_param = class_param;
	}
	public RBIODEGSet getSet() {
		return set;
	}
	public void setSet(RBIODEGSet set) {
		this.set = set;
	}
	public List<Integer> getDesc_i() {
		return desc_i;
	}
	public void setDesc_i(List<Integer> desc_i) {
		this.desc_i = desc_i;
	}
}
