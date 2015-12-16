package com.noomtech.thebody.movement.muscle

import java.util.concurrent.{CountDownLatch, TimeUnit}

import com.noomtech.thebody.IntegerPercentage.IntegerPercent
import com.noomtech.thebody.buildingblocks.SingleConnectorPipe
import com.noomtech.thebody.cells.RedBloodCell
import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers;


/**
 TEST-PLAN
 1 - Create a muscle and a pipe to deliver O2 to it and make sure that O2 levels are as they should be DONE
 2 - contract from zero to 1% DONE
 3 - zero to 50% DONE
 4 - zero to 100% DONE
 5 - zero to 1% then in 1% increments to 10% DONE
 6 - 90 to 100% in 1% increments DONE
 7 - 0 to 100% in one DONE
 8 - 0 to 2% but with not enough O2 DONE
 9 - cont. from 9 and top up 02 a bit and then move 10%, then top up again but not enough and move 20% DONE
 10 - Try and contract to LESS than the current contraction level DONE
 11 - Try and contract to 0% DONE
 12 - Contract 50% then interrupt it with another contraction then interrupt it with another and then wait for that to finish
  **/
class MuscleTest extends FlatSpec with MustMatchers {

  //1
  "A Muscle" should "consume oxygen correctly in direct relation to its size" in {
    val oxygenDeliverer = new SingleConnectorPipe("Oxygen Deliverer", null)
    val muscle = new Muscle(oxygenDeliverer, IntegerPercent(35), "Oxygen Consumer");

    muscle.oxygenStored must be (0)

    var redBloodCell = new RedBloodCell("Oxygen carrier")
    redBloodCell.addO2(50)
    oxygenDeliverer.submit(redBloodCell)

    Thread.sleep(500)

    muscle.oxygenStored must be (35)
    redBloodCell.getO2Level must be (15)

    oxygenDeliverer.submit(redBloodCell)

    Thread.sleep(500)

    muscle.oxygenStored must be (50)
    redBloodCell.getO2Level must be (0)

    oxygenDeliverer.submit(redBloodCell)

    Thread.sleep(500)

    muscle.oxygenStored must be (50)
    redBloodCell.getO2Level must be (0)
  }

  //2
  "A Muscle" should "be able to contract from 0% to 1% in the correct amount of time and use the correct amount of oxygen" in {

    var muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");
    val endTime = doContraction(muscle, IntegerPercent(1), 1)

    endTime > 980 must be (true)
    endTime < 1020 must be (true)
    muscle.oxygenStored must be (99.5)
  }

  //3
  "A Muscle" should "be able to contract from 0% to 50% in the correct amount of time and use the correct amount of oxygen" in {

    var muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");
    val endTime = doContraction(muscle, IntegerPercent(50), 10)

    System.out.println(endTime)
    endTime > 4900 must be (true)
    endTime < 5100 must be (true)
    muscle.oxygenStored must be (75)
  }

  //4
  "A Muscle" should "be able to contract from 0% to 100% in the correct amount of time and use the correct amount of oxygen" in {

    var muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");
    val endTime = doContraction(muscle, IntegerPercent(100), 10)

    System.out.println(endTime)
    endTime > 9200 must be (true)
    endTime < 10200 must be (true)
    muscle.oxygenStored must be (50)
  }

  //5
  "A muscle" should "be able to contract to 10% in increments of 1%" in {
    var muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");

    var ctr = 1
    for(ctr <- 1 to 10) {
      doContraction(muscle, IntegerPercent(ctr), 10)
    }

    muscle.oxygenStored must be (95)
    muscle.contractionLevel must be (10)
  }

  //6
  "A muscle" should "be able to contract from 95% to 100% in increments of 1%" in {
    var muscle = new Muscle(IntegerPercent(50), 100, 95, "Muscle test");

    var ctr = 96
    for(ctr <- 96 to 100) {
      doContraction(muscle, IntegerPercent(ctr), 10)
    }

    muscle.oxygenStored must be (97.5)
    muscle.contractionLevel must be (100)
  }

  //7
  "A muscle" should "be able to contract from 0% to 100% in in one go" in {
    var muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");

    doContraction(muscle, IntegerPercent(100), 10)

    muscle.oxygenStored must be (50)
    muscle.contractionLevel must be (100)
  }

  //8 and 9
  "A muscle" should "only be able to contract as far as its oxygen levels will allow it" in {
    var muscle = new Muscle(IntegerPercent(50), 1, 0, "Muscle test");

    doContraction(muscle, IntegerPercent(3), 10)

    muscle.oxygenStored must be (0)
    muscle.contractionLevel must be (2)

    muscle = new Muscle(IntegerPercent(40), 1, 0, "Muscle test");

    doContraction(muscle, IntegerPercent(3), 10)

    muscle.oxygenStored must be (0.2)
    muscle.contractionLevel must be (2)

    muscle.oxygenStored = 8.3

    doContraction(muscle, IntegerPercent(10), 10);

    muscle.contractionLevel must be (10)
    muscle.oxygenStored must be (5.1)

    doContraction(muscle, IntegerPercent(23), 10)

    muscle.contractionLevel must be (22)
    muscle.oxygenStored must be (0.3)
  }

  //10
  "A muscle" should "not respond to contraction requests requiring it to contract to less than or equal to what it is already contracted to" in {
    var muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");
    var contracting = muscle.startContracting(IntegerPercent(0), 10)

    contracting must be (false)

    muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");
    muscle.startContracting(IntegerPercent(10), 100)
    Thread.sleep(200)

    muscle.contractionLevel must be (10)

    contracting = muscle.startContracting(IntegerPercent(10), 100)

    contracting must be (false)

    contracting = muscle.startContracting(IntegerPercent(9), 100)

    contracting must be (false)

  }

  //11
  "A muscle" should "only respond to a requests to contract to levels of between 1 and 100 inclusive" in {

    var muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");
    var contracting = muscle.startContracting(IntegerPercent(100), 10)

    contracting must be (true)

    muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");
    contracting = muscle.startContracting(IntegerPercent(1), 10)

    contracting must be (true)

    muscle = new Muscle(IntegerPercent(50), 100, 0, "Muscle test");
    contracting = muscle.startContracting(IntegerPercent(50), 10)

    contracting must be (true)
  }

  //12
  "When a muscle is contracting and it receives a new contraction request it" must "stop the current contraction before processing the new request" in {
    var muscle = new Muscle(IntegerPercent(100), 100, 0, "Muscle test");
    muscle.startContracting(IntegerPercent(11), 1)
    Thread.sleep(3500)
    muscle.startContracting(IntegerPercent(10), 1)
    muscle.contractionLevel must be (4)
    muscle.oxygenStored must be (96)
    Thread.sleep(4500)
    muscle.startContracting(IntegerPercent(15), 1)
    muscle.contractionLevel must be (9)
    muscle.oxygenStored must be (91)
    Thread.sleep(2500)
    muscle.startContracting(IntegerPercent(16), 1)
    muscle.contractionLevel must be (12)
    muscle.oxygenStored must be (88)
    Thread.sleep(4500)
    muscle.contractionLevel must be (16)
    muscle.oxygenStored must be (84)
  }

  private def doContraction(muscle : Muscle, requiredContractionLevel : IntegerPercent, contractionsPerSecond : Int ): Long = {
    var startTime : Long = 0
    var endTime : Long = 0
    val countDownLatch = new CountDownLatch(1);
    muscle.startContractingListener = () => {startTime = System.currentTimeMillis()}
    muscle.stopContractingListener = () => {endTime = System.currentTimeMillis() - startTime
      countDownLatch.countDown();}
    muscle.startContracting(requiredContractionLevel, contractionsPerSecond)
    countDownLatch.await(20000, TimeUnit.MILLISECONDS);
    endTime
  }
}
