package com.noomtech.respiratorysystem;

import com.noomtech.thebody.buildingblocks.Particle;
import com.noomtech.thebody.buildingblocks.ParticleProcessor;
import com.noomtech.thebody.buildingblocks.SingleConnectorPipe;
import com.noomtech.thebody.cells.RedBloodCell;

/**
 * Created by ga2newh on 05/03/15.
 */
public class BasicLungsTest {


    public static void main(String[] args) throws Exception {
        new BasicLungsTest();
    }


    private BasicLungsTest() throws Exception {
        SingleConnectorPipe in = new SingleConnectorPipe("LungsTest_IN", null);
        SingleConnectorPipe out = new SingleConnectorPipe("LungsTest_OUT", null);
        out.addParticleProcessor(new TestParticleProcessor());
        Lungs lungs = new Lungs(in, out, 500);

        lungs.startBreathing();

        for(int i = 0 ; i < 100; i++) {
            RedBloodCell redBloodCell = new RedBloodCell(Integer.toString(i));
            redBloodCell.setMomentum(10);
            in.submit(redBloodCell);
            Thread.sleep((int)(Math.random() * 1500));
        }
    }

    private class TestParticleProcessor implements ParticleProcessor {

        @Override
        public Particle processParticle(Particle p) {
            System.out.println(((RedBloodCell) p).getO2Level());
            return null;
        }
    }
}
