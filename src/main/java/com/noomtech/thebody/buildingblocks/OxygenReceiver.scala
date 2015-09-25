package com.noomtech.thebody.buildingblocks

import com.noomtech.thebody.movement.Muscle

/**
 * Should be implemented by anything that receives a supply of oxygen e.g. a [[Muscle]]
 */
trait OxygenReceiver {

  /** Called to deliver the oxygen to this receiver **/
  def submitOxygen(oxygen : Int)
}
