package com.wangboo.nsgame.framework.gate.mina;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;

/**
 * 网关报文
 * @author wangboo
 *
 */
public class MinaC2SMessage {

	private GeneratedMessage m_msg;
	private IoSession m_session;
	
	public MinaC2SMessage(GeneratedMessage msg, IoSession session) {
		m_msg = msg;
		m_session = session;
	}
	
	public GeneratedMessage getMessage() {
		return m_msg;
	}
	
	public IoSession getSession() {
		return m_session;
	}
	
	public void write(Object msg) {
		m_session.write(msg);
	}
	
}
