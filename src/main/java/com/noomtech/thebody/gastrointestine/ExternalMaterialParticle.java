package com.noomtech.thebody.gastrointestine;

import com.noomtech.thebody.buildingblocks.transport.Particle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a particle of material that is external to the body e.g food.  This contains one or
 * more {@link ExternalMaterialType}, each in a particular amount.
 * This amount can be updated by various processes e.g. digestion.  If you don't want your implementation
 * to be mutable in this way then override these methods and throw {@link UnsupportedOperationException}.
 */
public abstract class ExternalMaterialParticle extends Particle {


    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
    private HashMap<ExternalMaterialType, Float> whatThisMaterialContains;


    public ExternalMaterialParticle(String name, Map<ExternalMaterialType, Float> whatThisMaterialContains) {
        super(name);
        this.whatThisMaterialContains = new HashMap<ExternalMaterialType, Float>(whatThisMaterialContains);
    }


    /**
     * Returns a map containing all the types of external material that this instance contains that are
     * instances of the given class together with their amounts.
     */
    public <R extends ExternalMaterialType> Map<R,Float> contains(Class<R> contains) {
        readLock.lock();
        try {
            Map<R, Float> toReturn = new HashMap<R, Float>();
            for (Map.Entry<ExternalMaterialType,Float> entry : whatThisMaterialContains.entrySet()) {
                if (entry.getKey().getClass() == contains) {
                    toReturn.put((R)entry.getKey(), entry.getValue());
                }
            }

            return toReturn;
        }
        finally {
            readLock.unlock();
        }
    }

    /**
     * Adds more material to this particle
     */
    public void addExternalMaterialType(ExternalMaterialType externalMaterialType, Float amountBeingAdded) {
        writeLock.lock();
        try {
            Float newAmount = whatThisMaterialContains.get(externalMaterialType);
            if(newAmount == null) {
                newAmount = amountBeingAdded;
            }
            else {
                newAmount =  newAmount + amountBeingAdded;
            }
            whatThisMaterialContains.put(externalMaterialType, newAmount);
        }
        finally {
            writeLock.unlock();
        }
    }

    /**
     * Remove the specified amount of material from the external material particle.
     * @return The amount removed.  This will be the amount specified if this instance contains enough of the
     * specified material being removed or whatever was actually present if it doesn't (0 if it doesn't
     * exist in this instance at all).
     */
    public Float removeExternalMaterialType(ExternalMaterialType externalMaterialType, Float amount) {
        writeLock.lock();
        try {
            Float existingAmount = whatThisMaterialContains.get(externalMaterialType);
            if(existingAmount == null) {
                return 0f;
            }
            else if (existingAmount <= amount) {
                whatThisMaterialContains.remove(externalMaterialType);
                return existingAmount;
            }
            else {
                existingAmount = existingAmount - amount;
                whatThisMaterialContains.put(externalMaterialType, existingAmount);
                return amount;
            }
        }
        finally {
            writeLock.unlock();
        }
    }
}