package com.sciome.opera.model.training.model.pKa;

import java.util.List;

public class PKAModel {
	List<Integer>		class_calc;
	PKAClassParam		class_param;
	PKASet				set;
	
	public List<Integer> getClass_calc() {
		return class_calc;
	}
	public void setClass_calc(List<Integer> class_calc) {
		this.class_calc = class_calc;
	}
	public PKAClassParam getClass_param() {
		return class_param;
	}
	public void setClass_param(PKAClassParam class_param) {
		this.class_param = class_param;
	}
	public PKASet getSet() {
		return set;
	}
	public void setSet(PKASet set) {
		this.set = set;
	}
}
