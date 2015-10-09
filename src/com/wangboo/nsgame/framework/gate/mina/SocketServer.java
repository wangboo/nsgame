package com.wangboo.nsgame.framework.gate.mina;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.firewall.BlacklistFilter;
import org.apache.mina.filter.firewall.Subnet;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * Socket服务器
 * @author wangboo
 *
 */
public class SocketServer {

	private int m_port;
	private NioSocketAcceptor m_ioAcceptor;
	private BlacklistFilter m_blacklistFilter;
	
	public SocketServer(int port) {
		m_port = port;
	}
	
	public void start() {
		m_ioAcceptor = new NioSocketAcceptor(4);
		m_blacklistFilter = new BlacklistFilter();
		m_ioAcceptor.getFilterChain().addFirst("BACKLIST", m_blacklistFilter);
		m_ioAcceptor.getFilterChain().addLast("CODEC", new ProtocolCodecFilter(new C2SProtocolCodecFactory()));
	}
	
	public void block(Subnet subnet) {
		m_blacklistFilter.block(subnet);
	}
	
	public void unblock(Subnet subnet) {
		m_blacklistFilter.unblock(subnet);
	}
	
}
