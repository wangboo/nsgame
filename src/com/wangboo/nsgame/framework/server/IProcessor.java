package com.wangboo.nsgame.framework.server;

import com.wangboo.nsgame.framework.Reportable;

public interface IProcessor<T> extends Runnable, Reportable {
	
	public void process(T t);
	
	public void shutdown();
	
}
