package com.wangboo.nsgame.gate.framework.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.wangboo.nsgame.protocol.C2SMessageProto.C2SMessage;

/**
 * 解压
 * @author wangboo
 *
 */
public class CSProtocolDecode extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		CodedInputStream cis = CodedInputStream.newInstance(in.array());
		C2SMessage msg = cis.readMessage(C2SMessage.PARSER, ExtensionRegistryLite.getEmptyRegistry());
		
		return false;
	}

}
