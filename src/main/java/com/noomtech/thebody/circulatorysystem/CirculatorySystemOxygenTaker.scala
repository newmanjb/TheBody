package com.noomtech.thebody.circulatorysystem

import com.noomtech.thebody.buildingblocks.{OxygenReceiver, Particle, ParticleProcessor}
import com.noomtech.thebody.cells.RedBloodCell
import com.noomtech.thebody.movement.Muscle


/**
 * Can be added to the circulatory system by an [[OxygenReceiver]] like an organ e.g. a [[Muscle]], in order to
 * deliver a supply of oxygen to it
 * @constructor
 * @param maxOxygenToTake The maximum amount of oxygen to take from any [[RedBloodCell]] at any one time.  If less than this amount is
 *                  available in a [[RedBloodCell]] then just this particular amount will be taken
 * @param oxygenReceiver The object that the oxygen will be delivered to
 */
class CirculatorySystemOxygenTaker(maxOxygenToTake : Int, oxygenReceiver: OxygenReceiver) extends ParticleProcessor {


   def processParticle (p : Particle) : Particle = {
     p match {
       case p:RedBloodCell =>
         var redBloodCell : RedBloodCell = p.asInstanceOf[RedBloodCell];
         var oxygen : Int = redBloodCell.takeO2(maxOxygenToTake);
         oxygenReceiver.submitOxygen(oxygen)
         redBloodCell
       case _ => p
     }
   }
}
