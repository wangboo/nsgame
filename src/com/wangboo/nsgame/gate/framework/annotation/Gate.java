package com.wangboo.nsgame.gate.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 网关声明
 * @author wangboo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface Gate {

	/**
	 * 报文内容字段id
	 * @return
	 */
	public int fieldid();
	/**
	 * 报文类型
	 * @return
	 */
	public int msgid();
	
	/**
	 * 数据结构
	 * @return
	 */
	public Class<?> data();
	
}
