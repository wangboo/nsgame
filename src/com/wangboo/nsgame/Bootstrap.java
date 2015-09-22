package com.wangboo.nsgame;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wangboo.nsgame.gate.LoginGate;
import com.wangboo.nsgame.gate.framework.GateMapping;
import com.wangboo.nsgame.gate.framework.GateScanner;
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
		
		int times = 10 * 1000;
		
		System.gc();
		byte[] data = reqData();
		long begin = System.currentTimeMillis();
		for(int i=0;i<times;i++) {
			gm.mapping(data);
		}
		long end = System.currentTimeMillis();
		long cost = (end - begin);
		System.out.println("mapping cost " + cost);
		System.gc();
		begin = System.currentTimeMillis();
		LoginGate g = new LoginGate();
		for(int i=0;i<times;i++) {
			C2SMessage msg = C2SMessage.parseFrom(data);
			if(msg.getMsgId() == MessageId.C2SLoginId) {
				g.login(msg.getC2SLogin().getUsername(), msg.getC2SLogin().getPassword());
			}
		}
		end = System.currentTimeMillis();
		long cost2 = (end - begin);
		System.out.println("normal cost " + cost2);
		System.out.println(cost/cost2 + " times slower");
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
