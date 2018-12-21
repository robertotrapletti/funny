package compiler;

import compiler.exceptions.InterpreterException;

import java.util.HashMap;
import java.util.List;

// TODO: MIGLIORIA EXTRA: come miglioria -> il compilatore potrebbe trasformare ogni nome in un indice, si potrebbero usare quindi degli indici
// invece di nomi emette quindi indici hashmap


public class Frame {
    private HashMap<String, Val> map = new HashMap<>();

    public Frame(List<String> params, List<String> locals,
                 List<Val> val) {
        checkEqLength(params,val);
        for (int i = 0; i < params.size(); i++) {
            map.put(params.get(i), val.get(i));
        }
        for (int i = 0; i < locals.size(); i++) {
            map.put(locals.get(i), NilVal.nil);
        }
    }

    private void checkEqLength(List<String> params, List<Val> vals) {
        if(params.size()!=vals.size()) throw new InterpreterException("checkNum of params != checkNum of vals");
    }

    public boolean contains(String id) {
        return map.containsKey(id);
    }

    public Val getVal(String id) {
        return map.get(id);
    }

    public Val setVal(String id, Val val) {
        map.put(id, val);
        return val;
    }
}
