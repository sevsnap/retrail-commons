/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons.automata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oneadmin
 */
public class State implements StateInterface {
    protected static final Logger log = LoggerFactory.getLogger(State.class); 
    private final Map<String,ActionInterface> actionMap = new HashMap<>();
    
    public State() {
    }
    
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Collection<String> getNextInputs() {
        return actionMap.keySet();
    }

    @Override
    public Collection<ActionInterface> getNextActions() {
        return actionMap.values();
    }

    @Override
    public void addAction(ActionInterface action) {
        assert(action.getOriginState() == this);
        if(actionMap.containsKey(action.getName()))
            throw new RuntimeException("cannot add action "+action.getName()+" to "+this+": state already has an action with the same name");
        actionMap.put(action.getName(), action);
    }

    @Override
    public ActionInterface getAction(String actionName) {
        ActionInterface a = actionMap.get(actionName);
        if(a == null)
            throw new UnsupportedOperationException("Invalid action "+actionName+" in state "+this);
        return a;
    }
    
    @Override
    public String toString() {
        return getName()+"[class="+getClass().getSimpleName()+"]";
    }
    
}
