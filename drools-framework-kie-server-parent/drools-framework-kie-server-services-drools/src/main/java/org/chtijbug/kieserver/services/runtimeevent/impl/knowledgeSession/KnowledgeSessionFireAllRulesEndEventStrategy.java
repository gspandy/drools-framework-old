/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.kieserver.services.runtimeevent.impl.knowledgeSession;


import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesEndEvent;
import org.chtijbug.drools.logging.FireAllRulesExecution;
import org.chtijbug.drools.logging.FireAllRulesExecutionStatus;
import org.chtijbug.kieserver.services.runtimeevent.AbstractMemoryEventHandlerStrategy;
import org.chtijbug.kieserver.services.runtimeevent.SessionContext;

public class KnowledgeSessionFireAllRulesEndEventStrategy implements AbstractMemoryEventHandlerStrategy {

    @Override
    public void handleMessageInternally(HistoryEvent historyEvent, SessionContext sessionContext) {
        SessionFireAllRulesEndEvent sessionFireAllRulesEndEvent = (SessionFireAllRulesEndEvent) historyEvent;
        FireAllRulesExecution fireAllRulesExecution = sessionContext.getFireAllRulesExecution();
        fireAllRulesExecution.setStopEventID(sessionFireAllRulesEndEvent.getEventID());
        fireAllRulesExecution.setEndDate(sessionFireAllRulesEndEvent.getDateEvent());
        fireAllRulesExecution.setFireAllRulesExecutionStatus(FireAllRulesExecutionStatus.STOPPED);
        fireAllRulesExecution.setNbreRulesFired(Long.valueOf(sessionFireAllRulesEndEvent.getNumberRulesExecuted()));
        fireAllRulesExecution.setExecutionTime(Long.valueOf(sessionFireAllRulesEndEvent.getExecutionTime()));
        sessionContext.setFireAllRulesExecution(null);

    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionFireAllRulesEndEvent;
    }

}
