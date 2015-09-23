package com.wangboo.nsgame.gate.framework.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.google.protobuf.GeneratedMessage;

/**
 * 服务器发送给客户端报文压缩
 * @author wangboo
 *
 */
public class CSProtocolEncoder implements ProtocolEncoder {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		GeneratedMessage msg = (GeneratedMessage) message;
		byte[] data = msg.toByteArray();
		out.write(data);
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		
	}

}
