package com.noomtech.thebody.respiration.thoracic;

import com.noomtech.thebody.buildingblocks.transport.Particle;
import com.noomtech.thebody.buildingblocks.transport.ParticleProcessor;
import com.noomtech.thebody.buildingblocks.transport.Pipe;
import com.noomtech.thebody.cells.RedBloodCell;
import com.noomtech.thebody.circulatorysystem.CirculatorySystemFacade;
import com.noomtech.thebody.circulatorysystem.Heart;
import com.noomtech.thebody.communication.Communicator;
import com.noomtech.thebody.communication.CommunicatorFactory;
import com.noomtech.thebody.respiration.Lungs;

/**
 */
public class BigTestWithLungsAndHeart {


    private final Communicator COMMUNICATOR = CommunicatorFactory.getCommunicator();


    public static void main(String[] args) {
        BigTestWithLungsAndHeart p = new BigTestWithLungsAndHeart();
        p.doHeartHeartAndLungsWithThreeParticlesInEverything();
    }

    private void doHeartHeartAndLungsOneParticle() {

        Object[] organs  = CirculatorySystemFacade.
                buildXParticlesInEachCellTestCirculatorySystemWithLungsAndHeart(0, 1, 1);
        Heart heart = (Heart)organs[0];
        Lungs lungs = (Lungs)organs[1];

        Pipe singleConnectorPipe = heart.getAtrium1().getForwardConnection1().getForwardConnection1().getForwardConnection1();
        singleConnectorPipe.addParticleProcessor(new TakeAllOxygenProcessor());

        lungs.startBreathing();
        new HeartThread(heart,300).start();
    }

    private void doHeartHeartAndLungsWithThreeParticlesInHeart() {

        Object[] organs  = CirculatorySystemFacade.
                buildXParticlesInEachCellTestCirculatorySystemWithLungsAndHeart(0, 3, 1);
        Heart heart = (Heart)organs[0];
        Lungs lungs = (Lungs)organs[1];

        Pipe singleConnectorPipe = heart.getAtrium1().getForwardConnection1().getForwardConnection1().getForwardConnection1();
        singleConnectorPipe.addParticleProcessor(new TakeAllOxygenProcessor());

        lungs.startBreathing();
        new HeartThread(heart,300).start();
    }

    private void doHeartHeartAndLungsWithThreeParticlesInAdjacentPipes() {

        Object[] organs  = CirculatorySystemFacade.
                buildXParticlesInFirst2AdjacentCellsTestCirculatorySystemWithLungsAndHeart(3, 1);
        Heart heart = (Heart)organs[0];
        Lungs lungs = (Lungs)organs[1];

        Pipe singleConnectorPipe = heart.getAtrium1().getForwardConnection1().getForwardConnection1();
        singleConnectorPipe.addParticleProcessor(new TakeAllOxygenProcessor());

        lungs.startBreathing();
        new HeartThread(heart,300).start();
    }

    private void doHeartHeartAndLungsWithThreeParticlesInEverything() {

        Object[] organs  = CirculatorySystemFacade.
                buildXParticlesInEachCellTestCirculatorySystemWithLungsAndHeart(3, 3, 1);
        Heart heart = (Heart)organs[0];
        Lungs lungs = (Lungs)organs[1];

        Pipe singleConnectorPipe = heart.getAtrium1().getForwardConnection1().getForwardConnection1();
        singleConnectorPipe.addParticleProcessor(new TakeAllOxygenProcessor());

        lungs.startBreathing();
        new HeartThread(heart,300).start();
    }

    private class TakeAllOxygenProcessor implements ParticleProcessor {

        public Particle processParticle(Particle p) {
            if(p instanceof RedBloodCell) {

                RedBloodCell r = (RedBloodCell)p;
                COMMUNICATOR.postEvent(this, "Particle (" + p.getName() + ") o2 level was: " + r.getO2Level() + "  WAITING", "RedBloodCell.takeo2");

                try {
                    Thread.sleep(1000);
                }
                catch(InterruptedException e)
                {
                    COMMUNICATOR.postExceptionEvent("Take all oxygen process wait thead interrupted", e);
                }


                r.takeO2(r.getO2Level());

                COMMUNICATOR.postEvent(this, "o2 level (" + p.getName() + ") is now " + r.getO2Level(), "RedBloodCell.takeo2");


            }

            return p;
        }
    }

    private class HeartThread extends Thread {
        private Heart heart;
        private long interval;
        private HeartThread(Heart heart, long interval) {
            this.heart = heart;
            this.interval = interval;
        }
        public void run() {
            while(true) {
                heart.onNerveImpulse();
                try {
                    Thread.sleep(interval);
                }
                catch(Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }

            }
        }
    }
}
