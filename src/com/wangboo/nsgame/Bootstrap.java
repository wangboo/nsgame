package com.wangboo.nsgame;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wangboo.nsgame.framework.gate.GateMapping;
import com.wangboo.nsgame.framework.gate.GateScanner;
import com.wangboo.nsgame.protocol.C2SMessageProto.C2SLogin;
import com.wangboo.nsgame.protocol.C2SMessageProto.C2SMessage;
import com.wangboo.nsgame.protocol.MessageProto.MessageId;

public class Bootstrap {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
//		LoginGate loginGate = context.getBean(LoginGate.class);
//		loginGate.login("wangboo", "123");
		GateScanner.setBinpath("/Users/wangboo/Documents/workspace/nsgame/bin/");
		GateMapping gm = new GateMapping(context, MessageId.class, C2SMessage.class);
		gm.scan("com.wangboo.nsgame.gate");
		
	}
	
	public static byte[] reqData() {
		C2SLogin.Builder loginBuilder =C2SLogin.newBuilder();
		loginBuilder.setUsername("wangboo")
			.setPassword("w231520");
		C2SMessage.Builder builder = C2SMessage.newBuilder();
		builder.setMsgId(MessageId.C2SLoginId)
			.setC2SLogin(loginBuilder.build());
		return builder.build().toByteArray();
	}
	
}
