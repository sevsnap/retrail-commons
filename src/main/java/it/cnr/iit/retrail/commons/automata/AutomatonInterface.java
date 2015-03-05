/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons.automata;

import java.util.Collection;

/**
 *
 * @author oneadmin
 */
public interface AutomatonInterface {
    String getName();
    StateInterface getBegin();
    Collection<StateInterface> getEnd();
    StateInterface getState(String name);
    Collection<StateInterface> getStates();
    StateInterface getCurrentState();
    void move(String actionName);
    Object doAction(String actionName, Object[] parms);
    void setCurrentState(StateInterface state);
    void setCurrentState(String stateName);
    boolean isFinished();
    void checkIntegrity();
}
