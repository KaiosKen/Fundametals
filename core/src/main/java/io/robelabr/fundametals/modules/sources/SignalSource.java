package io.robelabr.fundametals.modules.sources;


/**
 * A SignalSource is something that can be sampled for a boolean logic value.
 */
public interface SignalSource {
    boolean eval();
}
