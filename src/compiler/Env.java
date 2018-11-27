package compiler;

public class Env {
    private Env parentEnv;
    private Frame frame;

    public Env(Env parentEnv, Frame frame) {
        this.parentEnv = parentEnv;
        this.frame = frame;
    }

    Val getVal(String id){
        return frame.contains(id) ? frame.getVal(id) : parentEnv.getVal(id);
    }

    Val setVal(String id, Val val){
        return frame.contains(id) ? frame.setVal(id, val) : parentEnv.setVal(id, val);
    }

}
