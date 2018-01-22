package com.noomtech.thebody.gastrointestine;

/**
 * Represents a type of external material that represents a {@link ExternalMaterialParticle}.
 */
public abstract class ExternalMaterialType {

    /**
     * Represents the various states a material can be in in relation to the body
     */
    public enum State {
        UNTOUCHED_BY_BODY,
        CHEWED,
        BROKEN_DOWN
    }
}
