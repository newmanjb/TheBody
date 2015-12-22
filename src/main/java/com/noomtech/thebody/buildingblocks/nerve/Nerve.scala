package com.noomtech.thebody.buildingblocks.nerve


/**
 * Contains the methods and types needed to link components via a nerve.
 * Currently a "nerve" is just an abstract concept where a [[com.noomtech.thebody.buildingblocks.nerve.Nerve.NerveImpulseGenerator]]
 * will call a function from the impulse receiver.  This is analagous to the generator sending an impulse to the receiver along a "Nerve" e.g.
 * where the brain might send an impulse to the left bicep to get it to contract.
 *
 * NOTE:  It's a good idea to send impulses in a separate thread, otherwise whatever processes the impulse could
 * hold up the calling thread
 */
final object Nerve {

  /**
   * The function in the receiver that will be called whenever an impulse is sent along this nerve by the generator
   */
  type NerveImpulseReceiver = () => Unit {}

  /**
   * Anything that sends nerve impulses must use this
   */
  trait NerveImpulseGenerator {


    private var nerveImpulseReceiverMap:Map[String,NerveImpulseReceiver] = Map()

    private[Nerve] def + (name : String, nerveImpulseReceiver : NerveImpulseReceiver) = {
      nerveImpulseReceiverMap+=(name -> nerveImpulseReceiver)
    }

    /**
     * It's a good idea to send impulses in a separate thread, otherwise whatever processes the impulse could
     * hold up the calling thread
     */
    protected def sendImpulseToAll(): Unit = {
      for(nerveImpluseReceiver : NerveImpulseReceiver <- nerveImpulseReceiverMap.values) {
        nerveImpluseReceiver()
      }
    }

    /**
     * It's a good idea to send impulses in a separate thread, otherwise whatever processes the impulse could
     * hold up the calling thread
     */
    protected def sendImpulseTo(nerveName : String): Unit = {
      nerveImpulseReceiverMap(nerveName)()
    }
  }

  /**
   * Links two components via a nerve pathway
   * @param name The name of the nerve.  It's better to relate the name to the purpose e.g. "slow the heartbeat" or
   *             "contract bicep".
   * @param nerveImpluseGenerator The object that will call the [[com.noomtech.thebody.buildingblocks.nerve.Nerve.NerveImpulseReceiver]] function
   *                              when an impulse is sent
   * @param nerveImpulseReceiver The function that will be called when an impulse is sent from the [[com.noomtech.thebody.buildingblocks.nerve.Nerve.NerveImpulseGenerator]]
   *                             provided
   */
  def linkByNerve(name : String, nerveImpulseReceiver: NerveImpulseReceiver, nerveImpluseGenerator : NerveImpulseGenerator) = {
    nerveImpluseGenerator + (name,nerveImpulseReceiver)
  }
}
