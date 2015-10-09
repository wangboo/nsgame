package com.wangboo.nsgame.framework.gate.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 客户端发送给服务端数据解压缩
 * @author wangboo
 *
 */
public class C2SProtocolCodecFactory implements ProtocolCodecFactory {

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return null;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return null;
	}

}
