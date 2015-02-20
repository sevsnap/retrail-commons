/*
 * CNR - IIT
 * Coded by: 2014 Enrico "KMcC;) Carniani
 */
package it.cnr.iit.retrail.commons.automata;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oneadmin
 */
public class Automaton implements AutomatonInterface {
    protected static final Logger log = LoggerFactory.getLogger(Automaton.class); 
    protected final StateInterface begin;
    protected final StateInterface end;
    protected StateInterface state;
    protected Map<StateInterface,Map<ActionInterface,StateInterface>> states;
  
    
    public Automaton() {
        this.begin = new State(this);
        this.end = begin;
        this.state = begin;
    }
    
    public Automaton(State begin, State end) {
        this.begin = begin;
        this.end = end;
        this.state = begin;
    }
    
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public StateInterface getBegin() {
        return getState(begin.getName());
    }

    @Override
    public StateInterface getEnd() {
        return getState(end.getName());
    }

    @Override
    public StateInterface getState(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
