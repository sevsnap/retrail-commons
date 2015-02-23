/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons.automata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oneadmin
 */
public class Automaton implements AutomatonInterface {
    protected static final Logger log = LoggerFactory.getLogger(Automaton.class); 
    protected StateInterface begin;
    protected StateInterface[] end;
    protected StateInterface state;
    protected final Map<String,StateInterface> states = new HashMap<>();
  
    public Automaton() {
        this.begin = new State(this);
        this.end = new StateInterface[]{begin};
        this.state = begin;
        this.states.put(begin.getName(), begin);
    }
    
    public void init(StateInterface begin, StateInterface[] end, StateInterface[] states) {
        this.begin = begin;
        this.end = end;
        this.state = begin;
        assert(this.end.length > 0);
        this.states.clear();
        this.states.put(begin.getName(), begin);
        for(StateInterface aState: end)
            this.states.put(aState.getName(), aState);
        for(StateInterface aState: states)
            this.states.put(aState.getName(), aState);
    }
    
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public StateInterface[] getStates() {
        return (StateInterface[]) states.values().toArray();
    }
    
    @Override
    public StateInterface getBegin() {
        return begin;
    }

    @Override
    public StateInterface[] getEnd() {
        return end;
    }
    
    @Override
    public boolean isFinished() {
        for(StateInterface aState: end)
            if(aState == getCurrentState())
                return true;
        return false;
    }

    @Override
    public StateInterface getState(String name) {
        return states.get(name);
    }

    @Override
    public StateInterface getCurrentState() {
        return state;
    }

    @Override
    public void move(String actionName) {
        StateInterface s = getCurrentState();
        ActionInterface a = s.getAction(actionName);
        s = a.getTargetState();
        setCurrentState(s);
    }

    @Override
    public Object doAction(String actionName, Object[] parms) {
        StateInterface s = getCurrentState();
        ActionInterface a = s.getAction(actionName);
        return a.execute(parms);
    }

    @Override
    public void setCurrentState(StateInterface state) {
        this.state = state;
    }

    @Override
    public void setCurrentState(String stateName) {
        setCurrentState(getState(stateName));
    }
    
}
