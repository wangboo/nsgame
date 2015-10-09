package com.wangboo.nsgame.framework.server;

import com.wangboo.nsgame.framework.Reportable;

/**
 * 处理器
 * @author wangboo
 *
 * @param <T> 接受报文
 */
public interface IProcessor<T> extends Runnable, Reportable {
	
	public void process(T t);
	
	public void shutdown();
	
}
