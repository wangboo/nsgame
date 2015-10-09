package com.wangboo.nsgame.framework.server;

import com.wangboo.nsgame.framework.Reportable;

public interface IServer<T> extends Reportable {

	public void pushMessage(T msg);
	
	public T pullMessage();
	
	public boolean start();
	
	public boolean shutdown();
	
}
