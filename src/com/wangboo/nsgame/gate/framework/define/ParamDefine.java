package com.wangboo.nsgame.gate.framework.define;

import com.google.protobuf.Descriptors.FieldDescriptor;

/**
 * 参数定义
 * @author wangboo
 *
 */
public class ParamDefine {

	public final int fieldId;
	
	public FieldDescriptor fieldDescriptor;
	
	public ParamDefine(int fieldId) {
		this.fieldId = fieldId;
	}

	@Override
	public String toString() {
		return "ParamDefine [fieldId=" + fieldId + "]";
	}
	
}
