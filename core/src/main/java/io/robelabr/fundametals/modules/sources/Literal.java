package io.robelabr.fundametals.modules.sources;

public enum Literal implements SignalSource {
    TRUE, FALSE;
    @Override public boolean eval() { return this == TRUE; }
}
