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
public interface StateInterface {
    String getName();
    AutomatonInterface getAutomaton();
    Collection<String> getNextInputs();
    Collection<ActionInterface> getNextActions();
    Collection<StateInterface> getNextStates();
    ActionInterface getAction(String actionName);
    void addArc(ActionInterface action, StateInterface toState);
}
