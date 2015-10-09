package com.wangboo.nsgame.framework.gate;

import com.google.protobuf.GeneratedMessage;

/**
 * 网关调用服务特殊字段注入
 * @author wangboo
 *
 */
@FunctionalInterface
public interface IGateSpecFieldInject {

	public Object getSpecField(GeneratedMessage msg, Object session);
	
}
