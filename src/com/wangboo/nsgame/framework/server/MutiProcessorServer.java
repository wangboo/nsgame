package com.wangboo.nsgame.framework.server;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

/**
 * 具有多核功能的服务器
 * @author wangboo
 *
 * @param <T>
 */
public class MutiProcessorServer<T> extends AbsServer<T> {
	
	@FunctionalInterface
	public interface ProcessorFactory<T> {
		public IProcessor<T> create();
	}
	
	private int m_size;
	private ProcessorFactory<T> m_factory;
	private List<IProcessor<T>> m_processorList;
	
	public MutiProcessorServer(int size) {
		m_size = size;
		m_processorList = new ArrayList<>();
	}
	
	public void setProcessorFactory(ProcessorFactory<T> factory) {
		m_factory = factory;
	}
	
	@Override
	public boolean start() {
		for(int i=0;i<m_size;i++) {
			IProcessor<T> processor = m_factory.create();
			Thread thread = new Thread(processor);
			m_processorList.add(processor);
			thread.start();
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean shutdown() {
		for(IProcessor processor : m_processorList) {
			processor.shutdown();
		}
		return true;
	}

	@Override
	public void report(Logger log) {
		m_processorList.stream().forEach(d -> d.report(log));
	}

}
