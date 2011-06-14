/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jml.protocol.msnftp;

import net.sf.jml.MsnProtocol;

/**
 * Client have ready to receive file.
 * <p>
 * Syntax: TFR
 * <p>
 * Supported Protocol: MSNC0
 * 
 * @author Roger Chen
 */
public class MsnftpTFR extends MsnftpMessage {

    public MsnftpTFR(MsnProtocol protocol) {
        super(protocol);
        setCommand("TFR");
    }

    @Override
	protected void messageReceived(MsnftpSession session) {
        super.messageReceived(session);

        if (session.getFileTransfer().isSender()) {
            //TODO 
        } else {
            session.close();
        }
    }

}