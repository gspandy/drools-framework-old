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
package org.chtijbug.drools.entity.history.knowledge;

import org.chtijbug.drools.entity.history.WorkbenchResourceFile;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 05/06/13
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
public class KnowledgeBaseReloadedEvent extends KnowledgeBaseEvent {
    public KnowledgeBaseReloadedEvent(Long eventID, Date dateEvent, Long ruleBaseID) {
        super(eventID, dateEvent, ruleBaseID);
    }

    public KnowledgeBaseReloadedEvent(Long eventID, Date dateEvent, Long ruleBaseID, String baseUrl) {
        super(eventID, dateEvent, ruleBaseID);
        WorkbenchResourceFile workbenchResourceFile = new WorkbenchResourceFile(baseUrl, null, null);
        this.getResourceFiles().add(workbenchResourceFile);

    }


    public KnowledgeBaseReloadedEvent() {
    }
}
