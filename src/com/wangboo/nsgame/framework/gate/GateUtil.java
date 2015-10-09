package com.wangboo.nsgame.framework.gate;

import org.apache.mina.core.session.IoSession;

/**
 * 工具方法
 * @author wangboo
 *
 */
public class GateUtil {

	private static final String SESSION_USER_ID_FLAG = "__userId__";
	private static final Long DEFAULT_USER_ID = -1L;
	
	/** 获取userId， 如果为登陆，返回-1*/
	public static Long getUserId(IoSession session) {
		return (Long) session.getAttribute(SESSION_USER_ID_FLAG, DEFAULT_USER_ID);
	}
	
}
