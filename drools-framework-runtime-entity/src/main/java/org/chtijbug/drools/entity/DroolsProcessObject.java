/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.chtijbug.drools.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nheron
 */
public class DroolsProcessObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4718079763911002405L;
    private final String id;
    private final String name;
    private final String packageName;
    private final String type;
    private final String version;
    private final Map<String, DroolsNodeObject> nodeLists;

    protected DroolsProcessObject(String id, String name, String packageName, String type, String version) {
        this.id = id;
        this.name = name;
        this.packageName = packageName;
        this.type = type;
        this.version = version;
        this.nodeLists = new HashMap<String, DroolsNodeObject>();
    }

        public void addDroolsNodeObject(DroolsNodeObject droolsNodeObject){
            nodeLists.put(droolsNodeObject.getId(), droolsNodeObject);
        }
	
        public DroolsNodeObject getDroolsNodeObjet(String id){
            return nodeLists.get(id);
        }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DroolsProcessObject other = (DroolsProcessObject) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 41 * hash + (this.packageName != null ? this.packageName.hashCode() : 0);
        return hash;
    }

    public static DroolsProcessObject createDroolsProcessObject(String id, String name, String packageName, String type, String version) {

        return new DroolsProcessObject(id, name, packageName, type, version);
    }
}