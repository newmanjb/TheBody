package com.noomtech.respiratorysystem;

import com.noomtech.thebody.circulatorysystem.CirculatorySystemFacade;
import com.noomtech.thebody.circulatorysystem.Heart;

/**
 * Created by ga2newh on 06/03/15.
 */
public class CirculatorySystemMovementTest {


    public static void main(String[] args) {
        CirculatorySystemMovementTest p = new CirculatorySystemMovementTest();
                p.doHeartWithOnceParticleAndMomentumOfOne();
    }

    private void doHeartWithOnceParticleAndMomentumOfOne() {
        Heart heart = CirculatorySystemFacade.buildXParticlesInEachCellTestCirculatorySystemJustHeart(0, 1, 1);
        heart.start();
    }

    private void doHeartWithOnceParticleAndMomentumOfThree() {
        Heart heart = CirculatorySystemFacade.buildXParticlesInEachCellTestCirculatorySystemJustHeart(0, 1, 3);
        heart.start();
    }

    private void doHeartWithThreeParticleAndMomentumOfThree() {
        Heart heart = CirculatorySystemFacade.buildXParticlesInEachCellTestCirculatorySystemJustHeart(0, 3, 3);
        heart.start();
    }

    private void doHeartWithTwoParticlesInEveryPipeAndAMomentumOfTwo() {
        Heart heart = CirculatorySystemFacade.buildXParticlesInEachCellTestCirculatorySystemJustHeart(2, 2, 2);
        heart.start();
    }
}
