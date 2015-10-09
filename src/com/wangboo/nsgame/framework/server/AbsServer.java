package com.wangboo.nsgame.framework.server;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 具有消息处理队列的服务器
 * @author wangboo
 *
 * @param <T>
 */
public abstract class AbsServer<T> implements IServer<T> {

	private LinkedBlockingQueue<T> m_queue = new LinkedBlockingQueue<>();
	
	@Override
	public void pushMessage(T msg) {
		try {
			m_queue.put(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public T pullMessage() {
		try {
			return m_queue.poll(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}


}
