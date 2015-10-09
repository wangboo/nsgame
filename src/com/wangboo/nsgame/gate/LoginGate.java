package com.wangboo.nsgame.gate;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import com.wangboo.nsgame.framework.gate.Gateway;
import com.wangboo.nsgame.framework.gate.annotation.Gate;
import com.wangboo.nsgame.framework.gate.annotation.Param;
import com.wangboo.nsgame.framework.gate.annotation.ParamType;
import com.wangboo.nsgame.protocol.C2SMessageProto.C2SLogin;
import com.wangboo.nsgame.protocol.C2SMessageProto.C2SMessage;
import com.wangboo.nsgame.protocol.MessageProto.MessageId;

@Component
public class LoginGate {

	@Gate(msgid=MessageId.C2SLoginId_VALUE, fieldid=C2SMessage.C2SLOGIN_FIELD_NUMBER, data=C2SLogin.class)
	public void login(
			@Param(fieldid=ParamType.SESSION) IoSession session,
			@Param(fieldid=C2SLogin.USERNAME_FIELD_NUMBER)String username, 
			@Param(fieldid=C2SLogin.PASSWORD_FIELD_NUMBER)String password) {
		Gateway.getInstance().login(100L, session);
	}
	
}
