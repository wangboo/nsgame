package com.wangboo.nsgame.framework.gate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.google.protobuf.GeneratedMessage;
import com.wangboo.nsgame.framework.gate.annotation.ParamType;
import com.wangboo.nsgame.framework.gate.define.GateDefine;
import com.wangboo.nsgame.framework.gate.define.ParamDefine;
import com.wangboo.nsgame.protocol.MessageProto.MessageId;

public class GateMapping {
	
	private Class<?> messageClass;
	private Class<?> messageIdClass;
	private Method parseMethod;
	private List<GateDefine> gateList;
	private ApplicationContext context;
	
	private Map<Integer, IGateSpecFieldInject> specInjector = new HashMap<>();
	
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
	
	public void mapping(GeneratedMessage msg, Object session) {
		try{
			MessageId messageId = (MessageId) msg.getClass().getMethod("getMsgId").invoke(msg);
			int id = messageId.getNumber();
			boolean processed = false;
			for(GateDefine gd : gateList) {
				if(gd.accept(id)) {
					// 处理消息
					processed = true;
					process((GeneratedMessage)msg, gd, session);
					break;
				}
			}
			if(!processed) {
				System.out.println("消息没有被处理：\n" + msg);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 映射
	 * @param data
	 */
	public void mapping(byte[] data, Object session) {
		try{
			Object msg = parseMethod.invoke(null, data);
			mapping((GeneratedMessage)msg, session);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void process(GeneratedMessage msg, GateDefine gateDefine, Object session) {
		try{
			GeneratedMessage data = (GeneratedMessage) msg.getField(gateDefine.fieldDescriptor);
			int size = gateDefine.argDefines.length;
			Object[] args = new Object[size];
			for(int i=0;i<size;i++) {
				ParamDefine paramDefine = gateDefine.argDefines[i];
				int fieldId = paramDefine.fieldId;
				if(fieldId >= 0) {
					args[i] = data.getField(paramDefine.fieldDescriptor);
				}else if(fieldId == ParamType.SESSION) {
					args[i] = session;
				}else if(fieldId == ParamType.DATA_PKG) {
					args[i] = data;
				}else if(fieldId < 0) {
					IGateSpecFieldInject injector = specInjector.get(fieldId);
					args[i] = injector.getSpecField(msg, session);
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
	
	/**
	 * 增加特殊字段注入器
	 * @param flag
	 * @param injector
	 */
	public void addSpecInjector(Integer flag, IGateSpecFieldInject injector) {
		if(flag >= 0) {
			throw new IllegalArgumentException("flag can`t be positive!");
		}
		if(specInjector.containsKey(flag)) {
			System.err.println("addSpecInjector injector repeat !!! ");
		}
		specInjector.put(flag, injector);
	}
	
	

}
