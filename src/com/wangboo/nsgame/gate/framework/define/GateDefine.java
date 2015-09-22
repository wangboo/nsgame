package com.wangboo.nsgame.gate.framework.define;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.google.protobuf.Descriptors.FieldDescriptor;

/**
 * 网关服务定义
 * @author wangboo
 *
 */
public class GateDefine {
	/** 消息id*/
	public final int messageId;
//	/** 网关类*/
//	public final Class<?> gateClass;
	/** 数据报文类*/
	public final Class<?> dataClass;
	/** 类实例*/
	public final Object gateObj;
	/** 类方法*/
	public final Method gateMethod;
	/** 入参*/
	public final ParamDefine[] argDefines;
	/** 报文内容字段*/
	public final FieldDescriptor fieldDescriptor;
	
	public GateDefine(int messageId, Class<?> dataClass, Object gateObj, Method gateMethod, ParamDefine[] argDefines,
			FieldDescriptor fieldDescriptor) {
		this.messageId = messageId;
//		this.gateClass = clazz;
		this.dataClass = dataClass;
		this.gateObj = gateObj;
		this.gateMethod = gateMethod;
		this.argDefines = argDefines;
		this.fieldDescriptor = fieldDescriptor;
	}

	public boolean accept(int msgId) {
		return messageId == msgId;
	}
	
	@Override
	public String toString() {
		return "GateDefine [messageId=" + messageId + ", dataClass=" + dataClass + ", method=" + gateMethod + ", argDefines="
				+ Arrays.toString(argDefines) + "]";
	}

}
