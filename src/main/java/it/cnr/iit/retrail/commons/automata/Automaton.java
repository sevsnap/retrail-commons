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
    protected final Collection<StateInterface> end = new ArrayList<>();
    protected StateInterface state;
    protected final Map<String,StateInterface> states = new HashMap<>();
  
    public Automaton() {
    }
    
    public void addState(StateInterface state) {
        this.states.put(state.getName(), state);
    }
    
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Collection<StateInterface> getStates() {
        return states.values();
    }
    
    @Override
    public StateInterface getBegin() {
        return begin;
    }
    
     public void setBegin(StateInterface begin) {
        if(this.begin != null)
             throw new RuntimeException("begin state already defined");
        if(!this.states.containsValue(begin))
             throw new RuntimeException("begin state not declared in the automaton");
        this.begin = begin;
    }
     
    public void addEnd(StateInterface end) {
        if(!this.states.containsValue(end))  
             throw new RuntimeException("end state not declared in the automaton");
        this.end.add(end);
    }
    
    @Override
    public Collection<StateInterface> getEnd() {
        return end;
    }
    
    @Override
    public boolean isFinished() {
        return end.contains(getCurrentState());
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
        // Check automaton is complete
        if(this.end.isEmpty())
             throw new RuntimeException("incomplete automaton: end state(s) not defined");
        if(this.begin == null)
             throw new RuntimeException("incomplete automaton: begin state not defined");
        if(state == null)
            throw new NullPointerException("automaton state cannot be null");
        this.state = state;
    }

    @Override
    public void setCurrentState(String stateName) {
        setCurrentState(getState(stateName));
    }
    
    @Override
    public void checkIntegrity() {
        log.info("checking automaton integrity");
        StateInterface b = getBegin();
        if(b == null)
            throw new RuntimeException("automaton has no initial state");
        Collection<StateInterface> el = getEnd();
        if(b.getNextActions().isEmpty() && !el.contains(b))
            throw new RuntimeException("initial state "+b+" is a dead-end (has no outgoing action)");
        if(el.isEmpty())
            throw new RuntimeException("automaton has no end state");            
        for(StateInterface e: el) {
            if(!e.getNextActions().isEmpty() && b != e)
                throw new RuntimeException("end state "+e+" must be a dead-end (must not have outgoing actions)");
        }
        for(StateInterface e: getStates()) {
            if(e.getNextActions().isEmpty() && !el.contains(e))
                throw new RuntimeException("state "+e+" is a dead-end (has no outgoing actions)");
        }
        log.info("automaton integrity looks ok");
    }
    
    public void printInfo() {
        log.info(this+": state->actions mapping");
        for(StateInterface s: getStates()) {
            for(ActionInterface a: s.getNextActions())
                log.info("\t"+s+"."+a);
        }
    }
    
}
