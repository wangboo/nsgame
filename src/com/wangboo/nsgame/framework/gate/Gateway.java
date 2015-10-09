package com.wangboo.nsgame.framework.gate;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.google.protobuf.GeneratedMessage;

public class Gateway {

	private static final Gateway s_gate = new Gateway();
	
	private ConcurrentHashMap<Long, IoSession> sessions;
	
	private ThreadLocal<IoSession> sessionLocal;
	
	private ThreadLocal<Long> userIdLocal;
	
	private Gateway(){
		sessions = new ConcurrentHashMap<>(1024);
		sessionLocal = new ThreadLocal<>();
		userIdLocal = new ThreadLocal<>();
	}
	
	public static Gateway getInstance() {
		return s_gate;
	}
	
	public void login(Long userId, IoSession session) {
		sessions.put(userId, session);
	}
	
	public void logout(Long userId) {
		sessions.remove(userId);
	}
	
	public void setCurrentSession(IoSession session) {
		sessionLocal.set(session);
	}
	
	public IoSession getCurrentSession() {
		return sessionLocal.get();
	}
	
	public void setCurrentUserId(Long userId) {
		userIdLocal.set(userId);
	}
	
	public Long getCurrentUserId() {
		return userIdLocal.get();
	}
	
	public boolean send(GeneratedMessage msg) {
		IoSession session = getCurrentSession();
		if(session != null) {
			session.write(msg.toByteArray());
			return true;
		}
		return false;
	}
	
	public boolean sendTo(Long userId, GeneratedMessage msg) {
		if(sessions.containsKey(userId)) {
			sessions.get(userId).write(msg.toByteArray());
			return true;
		}
		return false;
	}
	
	
}
