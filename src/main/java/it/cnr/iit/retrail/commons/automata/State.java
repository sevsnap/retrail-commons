/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons.automata;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oneadmin
 */
public class State implements StateInterface {
    protected static final Logger log = LoggerFactory.getLogger(State.class); 
    protected final AutomatonInterface automaton;
    protected final Map<ActionInterface, StateInterface> map = new HashMap<>();
    
    public State(AutomatonInterface automaton) {
        this.automaton = automaton;
    }
    
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public AutomatonInterface getAutomaton() {
        return automaton;
    }

    @Override
    public Collection<String> getNextInputs() {
        Collection<String> nextInputs = new HashSet<>();
        for(ActionInterface a: map.keySet())
            nextInputs.add(a.getName());
        return nextInputs;
    }

    @Override
    public Collection<ActionInterface> getNextActions() {
        return map.keySet();
    }

    @Override
    public Collection<StateInterface> getNextStates() {
        return map.values();
    }

    @Override
    public void addArc(ActionInterface action, StateInterface toState) {
        map.put(action, toState);
    }

    @Override
    public ActionInterface getAction(String actionName) {
        for(ActionInterface a: map.keySet())
            if(a.getName().equals(actionName))
                return a;
        throw new UnsupportedOperationException("Invalid action "+actionName+" in state "+this);
    }
    
}
