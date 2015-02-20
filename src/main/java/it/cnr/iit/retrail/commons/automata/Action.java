/*
 * CNR - IIT
 * Coded by: 2015 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons.automata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oneadmin
 */
public class Action implements ActionInterface {
    protected final StateInterface targetState;
    protected static final Logger log = LoggerFactory.getLogger(Action.class);  
    
    public Action(StateInterface targetState) {
        this.targetState = targetState;
    }
    
    @Override
    public Object execute(Object[] args) {
        log.debug("null action body -- ignoring");
        return null;
    }

    @Override
    public StateInterface getTargetState() {
        return targetState;
    }

    @Override
    public AutomatonInterface getAutomaton() {
        return targetState.getAutomaton();
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
    
}
