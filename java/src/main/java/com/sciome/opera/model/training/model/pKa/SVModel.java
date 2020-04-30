package com.sciome.opera.model.training.model.pKa;

public class SVModel {
	private String 			_ArrayType_;
	private double[]		_ArraySize_;
	private int				_ArrayIsSparse_;
	private double[][]		_ArrayData_;
	
	public String get_ArrayType_() {
		return _ArrayType_;
	}
	public void set_ArrayType_(String _ArrayType_) {
		this._ArrayType_ = _ArrayType_;
	}
	public double[] get_ArraySize_() {
		return _ArraySize_;
	}
	public void set_ArraySize_(double[] _ArraySize_) {
		this._ArraySize_ = _ArraySize_;
	}
	public int get_ArrayIsSparse_() {
		return _ArrayIsSparse_;
	}
	public void set_ArrayIsSparse_(int _ArrayIsSparse_) {
		this._ArrayIsSparse_ = _ArrayIsSparse_;
	}
	public double[][] get_ArrayData_() {
		return _ArrayData_;
	}
	public void set_ArrayData_(double[][] _ArrayData_) {
		this._ArrayData_ = _ArrayData_;
	}
}
