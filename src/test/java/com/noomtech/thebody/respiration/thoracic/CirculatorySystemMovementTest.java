package com.noomtech.thebody.respiration.thoracic;

import com.noomtech.thebody.circulatorysystem.CirculatorySystemFacade;
import com.noomtech.thebody.circulatorysystem.Heart;

/**
 */
public class CirculatorySystemMovementTest {


    public static void main(String[] args) {
        CirculatorySystemMovementTest p = new CirculatorySystemMovementTest();
                p.doHeartWithOnceParticleAndMomentumOfOne();
    }

    private void doHeartWithOnceParticleAndMomentumOfOne() {
        Heart heart = CirculatorySystemFacade.buildXParticlesInEachCellTestCirculatorySystemJustHeart(0, 1, 1);
        new HeartThread(heart,1500).start();
    }

    private void doHeartWithOnceParticleAndMomentumOfThree() {
        Heart heart = CirculatorySystemFacade.buildXParticlesInEachCellTestCirculatorySystemJustHeart(0, 1, 3);
        new HeartThread(heart,1500).start();
    }

    private void doHeartWithThreeParticleAndMomentumOfThree() {
        Heart heart = CirculatorySystemFacade.buildXParticlesInEachCellTestCirculatorySystemJustHeart(0, 3, 3);
        new HeartThread(heart,1500).start();
    }

    private void doHeartWithTwoParticlesInEveryPipeAndAMomentumOfTwo() {
        Heart heart = CirculatorySystemFacade.buildXParticlesInEachCellTestCirculatorySystemJustHeart(2, 2, 2);
        new HeartThread(heart,1500).start();
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
