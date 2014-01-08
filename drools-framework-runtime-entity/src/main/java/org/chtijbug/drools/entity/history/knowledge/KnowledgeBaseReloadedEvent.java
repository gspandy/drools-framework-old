package org.chtijbug.drools.entity.history.knowledge;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 05/06/13
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
public class KnowledgeBaseReloadedEvent extends KnowledgeBaseEvent {
    public KnowledgeBaseReloadedEvent(int eventID, Date dateEvent, int ruleBaseID) {
        super(eventID, dateEvent, ruleBaseID);
    }

    public KnowledgeBaseReloadedEvent(int eventID, Date dateEvent, int ruleBaseID, String baseUrl, String webappName,
                                      String packageName, String packageVersion) {
        super(eventID, dateEvent, ruleBaseID);
        this.setGuvnor_url(baseUrl);
        this.setGuvnor_appName(webappName);
        this.setGuvnor_packageName(packageName);
        this.setGuvnor_packageVersion(packageVersion);
    }


    public KnowledgeBaseReloadedEvent() {
    }
}