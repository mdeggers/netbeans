/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.php.dbgp.packets;

import org.netbeans.modules.php.dbgp.DebugSession;
import org.netbeans.modules.php.dbgp.breakpoints.BreakpointModel;
import org.w3c.dom.Node;

public class RunResponse extends StatusResponse {
    private static final String BREAKPOINT = "breakpoint"; //NOI18N
    private static final String BREAKPOINT_ID = "id"; //NOI18N

    RunResponse(Node node) {
        super(node);
    }

    @Override
    public void process(DebugSession dbgSession, DbgpCommand command) {
        Status status = getStatus();
        Reason reason = getReason();
        if (status != null && reason != null) {
            dbgSession.processStatus(status, reason, command);
        }

        Node breakpoint = getChild(getNode(), BREAKPOINT);
        if (breakpoint != null) {
            String id = DbgpMessage.getAttribute(breakpoint, BREAKPOINT_ID);
            if (id != null) {
                updateBreakpointsView(dbgSession, id);
            }
        }
    }

    private void updateBreakpointsView(DebugSession session, String id) {
        DebugSession.IDESessionBridge bridge = session.getBridge();
        if (bridge != null) {
            BreakpointModel breakpointModel = bridge.getBreakpointModel();
            if (breakpointModel != null && breakpointModel.isSearchCurrentBreakpointById()) {
                breakpointModel.setCurrentBreakpoint(session, id);
            }
        }
    }

}
