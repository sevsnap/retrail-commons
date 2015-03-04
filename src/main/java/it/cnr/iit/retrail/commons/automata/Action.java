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
    protected final StateInterface originState;
    protected final StateInterface targetState;
    protected static final Logger log = LoggerFactory.getLogger(Action.class);  
    
    public Action(StateInterface originState, StateInterface targetState) {
        this.originState = originState;
        this.targetState = targetState == null? originState : targetState;
        if(originState == null) 
            throw new RuntimeException(this+" must have originState set");
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
    public StateInterface getOriginState() {
        return originState;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
    
    @Override 
    public String toString() {
        return getName()+"() -> "+getTargetState();
    }
    
}
