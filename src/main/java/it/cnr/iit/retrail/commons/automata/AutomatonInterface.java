/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons.automata;

/**
 *
 * @author oneadmin
 */
public interface AutomatonInterface {
    String getName();
    StateInterface getBegin();
    StateInterface getEnd();
    StateInterface getState(String name);
    StateInterface getCurrentState();
    void move(String actionName);
    Object doAction(String actionName, Object[] parms);
    void setCurrentState(StateInterface state);
    void setCurrentState(String stateName);
    
}
