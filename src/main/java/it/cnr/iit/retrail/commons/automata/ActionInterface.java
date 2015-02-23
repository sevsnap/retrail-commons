/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons.automata;

/**
 *
 * @author oneadmin
 */
public interface ActionInterface {
    String getName();
    Object execute(Object[] args);
    StateInterface getTargetState();
    AutomatonInterface getAutomaton();
    StateInterface getOriginState();
    void setOriginState(StateInterface s);
}
