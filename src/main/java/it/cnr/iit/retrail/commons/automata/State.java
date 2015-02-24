/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons.automata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oneadmin
 */
public class State implements StateInterface {
    protected static final Logger log = LoggerFactory.getLogger(State.class); 
    protected final Collection<ActionInterface> actions = new ArrayList<>();
    
    public State() {
    }
    
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Collection<String> getNextInputs() {
        Collection<String> nextInputs = new HashSet<>();
        for(ActionInterface a: actions)
            nextInputs.add(a.getName());
        return nextInputs;
    }

    @Override
    public Collection<ActionInterface> getNextActions() {
        return actions;
    }

    @Override
    public void addAction(ActionInterface action) {
        assert(action.getOriginState() == this);
        actions.add(action);
    }

    @Override
    public ActionInterface getAction(String actionName) {
        for(ActionInterface a: actions)
            if(a.getName().equals(actionName))
                return a;
        throw new UnsupportedOperationException("Invalid action "+actionName+" in state "+this);
    }
    
    @Override
    public String toString() {
        return getName()+"[class="+getClass().getSimpleName()+"]";
    }
    
}
