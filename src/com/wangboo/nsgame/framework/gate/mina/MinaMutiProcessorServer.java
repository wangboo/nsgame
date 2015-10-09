package com.wangboo.nsgame.framework.gate.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.wangboo.nsgame.framework.gate.GateMapping;
import com.wangboo.nsgame.framework.server.MutiProcessorServer;

/**
 * Mina服务器
 * @author wangboo
 *
 */
public class MinaMutiProcessorServer extends MutiProcessorServer<MinaC2SMessage> {

	private NioSocketAcceptor m_ioAcceptor;
	
	public MinaMutiProcessorServer(int size, GateMapping gateMapping) {
		super(size);
		setProcessorFactory(()-> new MinaGateMappingProcessor(this, gateMapping));
	}
	
	public void startMina(int port) throws IOException {
		m_ioAcceptor = new NioSocketAcceptor(4);
		m_ioAcceptor.getFilterChain().addLast("CODEC", new ProtocolCodecFilter(new C2SProtocolCodecFactory()));
		m_ioAcceptor.bind(new InetSocketAddress(port));
	}
	
	@Override
	public boolean shutdown() {
		m_ioAcceptor.unbind();
		return super.shutdown();
	}

}
