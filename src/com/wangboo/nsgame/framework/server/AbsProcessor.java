package com.wangboo.nsgame.framework.server;

import org.slf4j.Logger;

/**
 * 处理器
 * @author wangboo
 *
 * @param <T>
 */
public abstract class AbsProcessor<T> implements IProcessor<T> {

	protected boolean m_running = false;
	protected long m_processed_count = 0;
	protected IServer<T> m_server;
	
	public AbsProcessor(IServer<T> server) {
		m_running = true;
		m_server = server;
	}
	
	@Override
	public void run() {
		while(m_running) {
			T msg = m_server.pullMessage();
			if(msg != null) {
				process(msg);
				m_processed_count += 1;
			}
		}
	}
	
	public void shutdown() {
		m_running = false;
	}

	public void report(Logger log) {
		String runningState = m_running ? "running" : "shutdown";
		log.debug(Thread.currentThread().getName() + " is {}, is processed {} msg.", runningState, m_processed_count);
	}
	
}
