package com.wangboo.nsgame.framework.gate.mina;

import com.wangboo.nsgame.framework.gate.Gateway;
import com.wangboo.nsgame.framework.gate.GateMapping;
import com.wangboo.nsgame.framework.gate.GateUtil;
import com.wangboo.nsgame.framework.server.AbsProcessor;
import com.wangboo.nsgame.framework.server.IServer;

/**
 * mina + gateMapping 处理器
 * @author wangboo
 *
 */
public class MinaGateMappingProcessor extends AbsProcessor<MinaC2SMessage> {

	private GateMapping m_gateMapping;
	
	public MinaGateMappingProcessor(IServer<MinaC2SMessage> server, GateMapping gateMapping) {
		super(server);
		m_gateMapping = gateMapping;
	}

	@Override
	public void process(MinaC2SMessage msg) {
		Gateway.getInstance().setCurrentSession(msg.getSession());
		Gateway.getInstance().setCurrentUserId(GateUtil.getUserId(msg.getSession()));
		m_gateMapping.mapping(msg.getMessage(), msg.getSession());
	}

}
