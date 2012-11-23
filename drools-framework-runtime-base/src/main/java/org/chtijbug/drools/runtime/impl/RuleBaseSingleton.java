/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chtijbug.drools.runtime.impl;

import org.chtijbug.drools.common.log.Logger;
import org.chtijbug.drools.common.log.LoggerFactory;
import org.chtijbug.drools.entity.history.HistoryContainer;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.mbeans.RuleBaseSupervision;
import org.chtijbug.drools.runtime.mbeans.StatefulSessionSupervision;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author Bertrand Gressier
 */
public class RuleBaseSingleton implements RuleBasePackage {
    /**
     * Class Logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(RuleBaseSingleton.class);
    /**
     * unique indentifier of the RuleBase in the JVM
     */
    private static int ruleBaseCounter = 0;
    /**
     * Rule Base ID
     */
    private int ruleBaseID;
    /**
     * KnwoledgeBase reference
     */
    private KnowledgeBase kbase = null;
    private final List<DroolsResource> listResouces;
    private RuleBaseSupervision mbsRuleBase;
    private StatefulSessionSupervision mbsSession;
    private int maxNumberRuleToExecute = 2000;
    private Semaphore lockKbase = new Semaphore(1);
    MBeanServer server = null;
    private int sessionCounter = 0;

    public RuleBaseSingleton() throws DroolsChtijbugException{
        this.ruleBaseID = addRuleBase();
        listResouces = new ArrayList<DroolsResource>();
        initMBeans();
    }

    public RuleBaseSingleton(int maxNumberRulesToExecute) throws DroolsChtijbugException{
        this.ruleBaseID = addRuleBase();
        listResouces = new ArrayList<DroolsResource>();
        this.maxNumberRuleToExecute = maxNumberRulesToExecute;
        initMBeans();
    }

    private void initMBeans() throws DroolsChtijbugException {

        server = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName nameRuleBase = new ObjectName(HistoryContainer.nameRuleBaseObjectName+"ruleBaseID "+this.ruleBaseID);
            ObjectName nameSession = new ObjectName(HistoryContainer.nameSessionObjectName+"ruleBaseID "+this.ruleBaseID);
            mbsRuleBase = new RuleBaseSupervision(this);
            mbsSession = new StatefulSessionSupervision();
            server.registerMBean(mbsRuleBase, nameRuleBase);
            server.registerMBean(mbsSession, nameSession);
        } catch (Exception e) {
            throw new DroolsChtijbugException(DroolsChtijbugException.ErrorRegisteringMBeans, "", e);
        }
    }

    public boolean isKbaseLoaded() {
        if (kbase == null) {
            return false;
        }
        return true;
    }

    public List<DroolsResource> getListDroolsRessources() {
        return listResouces;
    }

    @Override
    public RuleBaseSession createRuleBaseSession() throws DroolsChtijbugException {
        RuleBaseSession newRuleBaseSession = null;
        newRuleBaseSession = this.createRuleBaseSession(this.maxNumberRuleToExecute);
        return newRuleBaseSession;
    }

    @Override
    public RuleBaseSession createRuleBaseSession(int maxNumberRulesToExecute) throws DroolsChtijbugException {

        RuleBaseSession newRuleBaseSession = null;
        if (kbase != null) {
            try {
                lockKbase.acquire();
            } catch (Exception e) {
                throw new DroolsChtijbugException(DroolsChtijbugException.KbaseAcquire, "", e);
            }
            StatefulKnowledgeSession newDroolsSession = kbase.newStatefulKnowledgeSession();
            this.sessionCounter++;
            newRuleBaseSession = new RuleBaseStatefulSession(this.sessionCounter, newDroolsSession, maxNumberRulesToExecute, mbsSession);
            lockKbase.release();
        } else {
            throw new DroolsChtijbugException(DroolsChtijbugException.KbaseNotInitialised, "", null);
        }
        return newRuleBaseSession;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.chtijbug.drools.runtime.RuleBasePackage#addDroolsResouce(org.chtijbug
     * .drools.runtime.resource.DroolsResource)
     */
    @Override
    public void addDroolsResouce(DroolsResource res) {
        listResouces.add(res);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.chtijbug.drools.runtime.RuleBasePackage#createKBase()
     */
    @Override
    public synchronized void createKBase() throws DroolsChtijbugException {

        if (kbase != null) {
            // TODO dispose all elements
        }

        try {
            KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

            for (DroolsResource res : listResouces) {
                kbuilder.add(res.getResource(), res.getResourceType());
            }

            KnowledgeBase newkbase = KnowledgeBaseFactory.newKnowledgeBase();
            newkbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
            lockKbase.acquire();
            kbase = newkbase;
            lockKbase.release();
        } catch (Exception e) {
            LOGGER.error("error to load Agent", e);
            throw new DroolsChtijbugException(DroolsChtijbugException.ErrorToLoadAgent, "", e);
        }
    }

    private static int addRuleBase() {
        ruleBaseCounter++;
        return ruleBaseCounter;
    }

    public int getRuleBaseID() {
        return ruleBaseID;
    }
}
