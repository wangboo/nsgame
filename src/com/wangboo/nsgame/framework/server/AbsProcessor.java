package com.wangboo.nsgame.framework.server;

/**
 * 处理器
 * @author wangboo
 *
 * @param <T>
 */
public abstract class AbsProcessor<T> implements IProcessor<T> {

	private boolean m_running = false;
	private IServer<T> m_server;
	
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
			}
		}
	}
	
	public void shutdown() {
		m_running = false;
	}

}
