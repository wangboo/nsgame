package com.wangboo.nsgame.gate.framework;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.google.protobuf.GeneratedMessage;
import com.wangboo.nsgame.gate.framework.annotation.ParamType;
import com.wangboo.nsgame.gate.framework.define.GateDefine;
import com.wangboo.nsgame.gate.framework.define.ParamDefine;
import com.wangboo.nsgame.protocol.MessageProto.MessageId;

public class GateMapping {
	
	private Class<?> messageClass;
	private Class<?> messageIdClass;
	private Method parseMethod;
	private List<GateDefine> gateList;
	private ApplicationContext context;
	
	public GateMapping(ApplicationContext context, Class<?> messageIdClass, Class<?> messageClass) {
		this.context = context;
		this.messageClass = messageClass;
		this.messageIdClass = messageIdClass;
		init();
	}
	
	private void init() {
		try {
			parseMethod = messageClass.getMethod("parseFrom", byte[].class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void setGateList(List<GateDefine> gateList) {
		this.gateList = gateList;
	}
	
	/**
	 * 映射
	 * @param data
	 */
	public void mapping(byte[] data) {
		try{
			Object msg = parseMethod.invoke(null, data);
//			System.out.println("parse: \n" + msg);
			MessageId messageId = (MessageId) msg.getClass().getMethod("getMsgId").invoke(msg);
			int id = messageId.getNumber();
			boolean processed = false;
			for(GateDefine gd : gateList) {
				if(gd.accept(id)) {
					// 处理消息
					processed = true;
					process((GeneratedMessage)msg, gd);
				}
			}
		}catch(Exception e) {
			
		}
	}

	private void process(GeneratedMessage msg, GateDefine gateDefine) {
		try{
			GeneratedMessage data = (GeneratedMessage) msg.getField(gateDefine.fieldDescriptor);
			int size = gateDefine.argDefines.length;
			Object[] args = new Object[size];
			for(int i=0;i<size;i++) {
				ParamDefine paramDefine = gateDefine.argDefines[i];
				switch(paramDefine.fieldId) {
				case ParamType.SESSION:
					args[i] = null;
					break;
				case ParamType.GAMEPLAYER:
					args[i] = null;
					break;
				case ParamType.DATA_PKG:
					args[i] = data;
					break;
				default:
					args[i] = data.getField(paramDefine.fieldDescriptor);
				}
			}
			gateDefine.gateMethod.invoke(gateDefine.gateObj, args);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void scan(String pkg) throws Exception {
		GateScanner gs = new GateScanner(context, messageIdClass, messageClass);
		gs.setScanPackage(pkg);
		gateList = gs.scan();
	}
	
	

}
