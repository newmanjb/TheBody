package com.noomtech.thebody.circulatorysystem

import com.noomtech.thebody.Oxygen
import com.noomtech.thebody.buildingblocks.transport.{Particle, ParticleProcessor}
import com.noomtech.thebody.cells.RedBloodCell


/**
 * Can be added to the circulatory system by something that uses [[com.noomtech.thebody.Oxygen]] like an organ e.g. a [[com.noomtech.thebody.movement.muscle.Muscle]],
 * in order to deliver a supply of oxygen to it
 * @constructor
 * @param maxOxygenToTake The maximum amount of oxygen to take from any [[com.noomtech.thebody.cells.RedBloodCell]] at any one time.  If less than this amount is
 *                  available in a [[com.noomtech.thebody.cells.RedBloodCell]] then just this particular amount will be taken
 * @param oxygenReceiver The function used to deliver the oxygen
 */
class CirculatorySystemOxygenTaker(maxOxygenToTake : Int, oxygenReceiver: Oxygen => Unit) extends ParticleProcessor {


   def processParticle (p : Particle) : Particle = {
     p match {
       case p:RedBloodCell =>
         val redBloodCell : RedBloodCell = p.asInstanceOf[RedBloodCell];
         val oxygenTaken : Int = redBloodCell.takeO2(maxOxygenToTake);
         oxygenReceiver(Oxygen(oxygenTaken))
         p
       case _ => p
     }
   }
}
