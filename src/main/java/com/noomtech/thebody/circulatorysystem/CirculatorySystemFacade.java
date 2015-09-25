package com.noomtech.thebody.circulatorysystem;

import com.noomtech.thebody.respiratorysystem.Lungs;
import com.noomtech.thebody.buildingblocks.Particle;
import com.noomtech.thebody.buildingblocks.SingleConnectorPipe;
import com.noomtech.thebody.cells.RedBloodCell;
import com.noomtech.thebody.utils.ConcurrentHashSet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


//----------------------------------------TEST PLAN--------------------------------------------
//1/ One particle in Atrium DONE
//2/ Two particles in Atrium DONE
//3/ Two particles in atrium.  Two particles in next one along.  Two particles in one behind atrium DONE
//4/ One particle in each cell DONE
//5/ Three particles in 7 adjacent cells DONE
//6/ No particles DONE
//7/ 10 particles in atrium DONE
//8/ 200 particles in atrium DONE
//9/ 100 particles in each cell, check there are no exceptions DONE
//10/ 2 particles in 6 adjacents cells.  Heartbeats of force 2. DONE
//11/ 2 particles in 6 adjacents cells.  Heartbeats of force 3. DONE
//---------------------------------------------------------------------------------------------
/**
 * Conveniently wraps handy operations associated with the circulatory system in one class with easy to use methods (as the 
 * Facade pattern dictates).
 * This used to use hibernate a lot to load the configurations of cells and organs but have decided against that
 * now as it's overkill.
 * @author Nooom, Noomtech ltd
 */
public class CirculatorySystemFacade 
{
	private static SessionFactory factory;

	private static Session getSession()
	{
		if(factory == null)
		{
			factory = new Configuration().configure().buildSessionFactory();
		}

		return factory.getCurrentSession();
	}
	
//	public static void buildEmptyCirculatorySystemUsingHibernate()
//	{
//		Particle p1Heart = new Particle("HeartParticle11");
//		Particle p2Heart = new Particle("HeartParticle12");
//		HashSet<Particle> heartParticles = new HashSet<Particle>();
//		heartParticles.add(p1Heart);
//		heartParticles.add(p2Heart);
//		Atrium atrium = new Atrium("Atrium", heartParticles);
//
//		Particle p1 = new Particle("Particle11");
//		Particle p2 = new Particle("Particle12");
//		HashSet<Particle> particles1 = new HashSet<Particle>();
//		particles1.add(p1);
//		particles1.add(p2);
//		SingleConnectorPipe singleConnectorPipe1 = new SingleConnectorPipe("Pipe1", particles1);
//
//		Particle p3 = new Particle("Particle21");
//		Particle p4 = new Particle("Particle22");
//		HashSet<Particle> particles2 = new HashSet<Particle>();
//		particles2.add(p3);
//		particles2.add(p4);
//		SingleConnectorPipe singleConnectorPipe2 = new SingleConnectorPipe("Pipe2", particles2);
//
//		Particle p5 = new Particle("Particle31");
//		Particle p6 = new Particle("Particle31");
//		HashSet<Particle> particles3 = new HashSet<Particle>();
//		particles3.add(p5);
//		particles3.add(p6);
//		SingleConnectorPipe singleConnectorPipe3 = new SingleConnectorPipe("Pipe3", particles3);
//
//		Particle p7 = new Particle("Particle41");
//		Particle p8 = new Particle("Particle42");
//		HashSet<Particle> particles4 = new HashSet<Particle>();
//		particles4.add(p7);
//		particles4.add(p8);
//		SingleConnectorPipe singleConnectorPipe4 = new SingleConnectorPipe("Pipe4", particles4);
//
//		atrium.setForwardConnection1(singleConnectorPipe1);
//		singleConnectorPipe1.setForwardConnection1(singleConnectorPipe2);
//		singleConnectorPipe2.setForwardConnection1(singleConnectorPipe3);
//		singleConnectorPipe3.setForwardConnection1(singleConnectorPipe4);
//		singleConnectorPipe4.setForwardConnection1(atrium);
//
//		Session session = getSession();
//		session = getSession();
//		session.beginTransaction();
//		int atriumContainerId = (Integer)session.save(atrium);
//		session.getTransaction().commit();
//
//		session = getSession();
//		session.beginTransaction();
//		String heartPropertiesString = "1000,1," + Integer.toString(atriumContainerId);
//		OrganProperties heartProperties = new OrganProperties("Heart", heartPropertiesString);
//		session.save(heartProperties);
//		session.getTransaction().commit();
//	}
//
//	public static Heart getEmptyCirculatorySystemUsingHibernate()
//	{
//		Session session = getSession();
//		session.beginTransaction();
//		//Get the properties for the heart
//		OrganProperties organProperties = (OrganProperties)session.createQuery("from OrganProperties where NAME = 'Heart'").uniqueResult();
//		String[] heartProperties = organProperties.getProperties().split(",");
//		int heartBeatInterval = Integer.parseInt(heartProperties[0]);
//		int heartBeatForce = Integer.parseInt(heartProperties[1]);
//		int atriumId = Integer.parseInt(heartProperties[2]);
//		//Get the atrium, which will in turn instantiate all the forward links and
//		//therefore the entire circle of pipes
//		Atrium atrium = (Atrium)session.createQuery("from Atrium where CONTAINER_ID = " + atriumId).uniqueResult();
//		//Atrium atrium = (Atrium)session.load(Atrium.class, atriumId);
//		session.close();
//
//		Heart heart = new Heart(atrium, heartBeatInterval, heartBeatForce);
//
//		return heart;
//	}
	
//	
//	
	//�pass in some config context objects
	//�Use Spring here?
	public static Heart buildXParticlesInEachCellTestCirculatorySystemJustHeart(
            int numParticlesInEachPipe,
            int numParticlesInHeart,
            int heartBeatForce)
	{
        ConcurrentHashSet<Particle> particles1 = buildParticleSet(numParticlesInEachPipe, "Particle1.");
        ConcurrentHashSet<Particle> particles2 = buildParticleSet(numParticlesInEachPipe, "Particle2.");
        ConcurrentHashSet<Particle> particles3 = buildParticleSet(numParticlesInEachPipe, "Particle3.");
        ConcurrentHashSet<Particle> particles4 = buildParticleSet(numParticlesInEachPipe, "Particle4.");
        ConcurrentHashSet<Particle> particles5 = buildParticleSet(numParticlesInEachPipe, "Particle5.");
        ConcurrentHashSet<Particle> particles6 = buildParticleSet(numParticlesInEachPipe, "Particle6.");
        ConcurrentHashSet<Particle> particles7 = buildParticleSet(numParticlesInEachPipe, "Particle7.");
        ConcurrentHashSet<Particle> particles8 = buildParticleSet(numParticlesInEachPipe, "Particle8.");
		ConcurrentHashSet<Particle> particles9 = buildParticleSet(numParticlesInEachPipe, "Particle9.");
        ConcurrentHashSet<Particle> particles10 = buildParticleSet(numParticlesInEachPipe, "Particle10.");

		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,5, new ConcurrentHashSet[]{particles1, particles2, particles3,
				particles4, particles5});
		joinUp(outOfHeart);
		SingleConnectorPipe[] toHeart = buildPipeArray(6,5, new ConcurrentHashSet[]{particles6, particles7, particles8,
                particles9, particles10});
		joinUp(toHeart);

		ConcurrentHashSet<Particle> heartParticles = buildParticleSet(numParticlesInHeart, "From heart ");
        Atrium atrium = new Atrium("Atrium", heartParticles);
        atrium.setForwardConnection1(outOfHeart[0]);
		Heart heart = new Heart(atrium, 1500, heartBeatForce);
		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());

		return heart;
	}

    public static Object[] buildXParticlesInEachCellTestCirculatorySystemWithLungsAndHeart(
            int numParticlesInEachPipe,
            int numParticlesInHeart,
            int heartBeatForce)
    {
        ConcurrentHashSet<Particle> particles1 = buildRedBloodCellSet(numParticlesInEachPipe, "Particle1.");
        ConcurrentHashSet<Particle> particles2 = buildRedBloodCellSet(numParticlesInEachPipe, "Particle2.");
        ConcurrentHashSet<Particle> particles3 = buildRedBloodCellSet(numParticlesInEachPipe, "Particle3.");
        ConcurrentHashSet<Particle> particles4 = buildRedBloodCellSet(numParticlesInEachPipe, "Particle4.");

        SingleConnectorPipe[] outOfHeart = buildPipeArray(1,2, new ConcurrentHashSet[]{particles1, particles2});
        joinUp(outOfHeart);
        SingleConnectorPipe[] toHeart = buildPipeArray(3,2, new ConcurrentHashSet[]{particles3, particles4});
        joinUp(toHeart);

        ConcurrentHashSet<Particle> heartParticles = buildRedBloodCellSet(numParticlesInHeart, "From heart ");
        Atrium atrium = new Atrium("Atrium", heartParticles);
        atrium.setForwardConnection1(outOfHeart[0]);
        Lungs lungs = new Lungs(outOfHeart[outOfHeart.length - 1], toHeart[0], 1000);
        Heart heart = new Heart(atrium, 300, heartBeatForce);
        toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());

        return new Object[]{heart,lungs};
    }

    public static Object[] buildXParticlesInFirst2AdjacentCellsTestCirculatorySystemWithLungsAndHeart(
            int numParticlesInTheAdjacentPipes,
            int heartBeatForce)
    {
        ConcurrentHashSet<Particle> particles1 = buildRedBloodCellSet(numParticlesInTheAdjacentPipes, "Particle1.");
        ConcurrentHashSet<Particle> particles2 = buildRedBloodCellSet(numParticlesInTheAdjacentPipes, "Particle2.");
        ConcurrentHashSet<Particle> particles3 = new ConcurrentHashSet<Particle>();
        ConcurrentHashSet<Particle> particles4 = new ConcurrentHashSet<Particle>();

        SingleConnectorPipe[] outOfHeart = buildPipeArray(1,2, new ConcurrentHashSet[]{particles1, particles2});
        joinUp(outOfHeart);
        SingleConnectorPipe[] toHeart = buildPipeArray(3,2, new ConcurrentHashSet[]{particles3, particles4});
        joinUp(toHeart);

        ConcurrentHashSet<Particle> heartParticles = buildRedBloodCellSet(0, "From heart ");
        Atrium atrium = new Atrium("Atrium", heartParticles);
        atrium.setForwardConnection1(outOfHeart[0]);
        Lungs lungs = new Lungs(outOfHeart[outOfHeart.length - 1], toHeart[0], 1000);
        Heart heart = new Heart(atrium, 300, heartBeatForce);
        toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());

        return new Object[]{heart,lungs};
    }

    private static ConcurrentHashSet<Particle> buildParticleSet(int size, String namePrefix) {
        ConcurrentHashSet<Particle> particles = new ConcurrentHashSet<Particle>();
        for(int i = 0; i < size; i++) {
            Particle particle = new Particle(namePrefix + (i + 1));
            particle.sayItCanMove();
            particles.add(particle);
        }
        return particles;
    }

    private static ConcurrentHashSet<Particle> buildRedBloodCellSet(int size, String namePrefix) {
        ConcurrentHashSet<Particle> particles = new ConcurrentHashSet<Particle>();
        for(int i = 0; i < size; i++) {
            RedBloodCell particle = new RedBloodCell(namePrefix + (i + 1));
            particle.sayItCanMove();
            particles.add(particle);
        }
        return particles;
    }
//	
//	public static Heart buildCirculatorySystemWith100ParticlesInEachCell()
//	{				
//		ConcurrentHashSet<Particle> p1 = buildParticleSet("Particle",1,100);
//		ConcurrentHashSet<Particle> p2 = buildParticleSet("Particle",2,100);
//		ConcurrentHashSet<Particle> p3 = buildParticleSet("Particle",3,100);
//		ConcurrentHashSet<Particle> p4 = buildParticleSet("Particle",4,100);
//		ConcurrentHashSet<Particle> p5 = buildParticleSet("Particle",5,100);
//		ConcurrentHashSet<Particle> p6 = buildParticleSet("Particle",6,100);
//		ConcurrentHashSet<Particle> p7 = buildParticleSet("Particle",7,100);
//		ConcurrentHashSet<Particle> p8 = buildParticleSet("Particle",8,100);
//		ConcurrentHashSet<Particle> p9 = buildParticleSet("Particle",9,100);
//		ConcurrentHashSet<Particle> p10 = buildParticleSet("Particle",10,100);
//		ConcurrentHashSet<Particle> p11 = buildParticleSet("Particle",11,100);
//		ConcurrentHashSet<Particle> p12 = buildParticleSet("Particle",12,100);
//		ConcurrentHashSet<Particle> p13 = buildParticleSet("Particle",13,100);
//		ConcurrentHashSet<Particle> p14 = buildParticleSet("Particle",14,100);
//		ConcurrentHashSet<Particle> heartParticles = buildParticleSet("Particle from heart",1,100);
//		
//		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,7, new ConcurrentHashSet[]{p1,p2,p3,p4,p5,p6,p7});
//		joinUp(outOfHeart);
//		SingleConnectorPipe[] toHeart = buildPipeArray(8,7, new ConcurrentHashSet[]{p8,p9,p10,p11,p12,p13,p14});
//		joinUp(toHeart);
//	
//		Heart heart = new Heart(buildParticleSet("From heart", 1, 1), 250);
//		heart.getAtrium1().setForwardConnection1(outOfHeart[0]);
//		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
//		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());
//		
//		return heart;
//	}	
//	
//	public static Heart buildCirculatorySystemWithOneParticleInHeartOnly()
//	{				
//		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,7, new ConcurrentHashSet[]{buildParticleSet("Particle",1,1)});
//		joinUp(outOfHeart);
//		SingleConnectorPipe[] toHeart = buildPipeArray(8,7, null);
//		joinUp(toHeart);
//	
//		Heart heart = new Heart(buildParticleSet("From heart", 1, 1), 250);
//		heart.getAtrium1().setForwardConnection1(outOfHeart[0]);
//		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
//		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());
//		
//		return heart;
//	}
//	
//	public static Heart buildCirculatorySystemWithTenParticlesInHeartOnly()
//	{				
//		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,7, null);
//		joinUp(outOfHeart);
//		SingleConnectorPipe[] toHeart = buildPipeArray(8,7, null);
//		joinUp(toHeart);
//	
//		Heart heart = new Heart(buildParticleSet("From heart", 1, 10), 250);
//		heart.getAtrium1().setForwardConnection1(outOfHeart[0]);
//		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
//		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());
//		
//		return heart;
//	}	
//
//	public static Heart buildCirculatorySystemWith200ParticlesInHeartOnly()
//	{				
//		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,7, null);
//		joinUp(outOfHeart);
//		SingleConnectorPipe[] toHeart = buildPipeArray(8,7, null);
//		joinUp(toHeart);
//	
//		Heart heart = new Heart(buildParticleSet("From heart", 1, 200), 250);
//		heart.getAtrium1().setForwardConnection1(outOfHeart[0]);
//		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
//		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());
//		
//		return heart;
//	}	
//	
//	public static Heart buildCirculatorySystemWithThreeParticlesInSixAdajacentCells()
//	{		
//		ConcurrentHashSet<Particle> p1 = buildParticleSet("Particle",1,3);
//		ConcurrentHashSet<Particle> p2 = buildParticleSet("Particle",2,3);
//		ConcurrentHashSet<Particle> p3 = buildParticleSet("Particle",3,3);
//		ConcurrentHashSet<Particle> p4 = buildParticleSet("Particle",4,3);
//		ConcurrentHashSet<Particle> p5 = buildParticleSet("Particle",5,3);
//		ConcurrentHashSet<Particle> p6 = buildParticleSet("Particle",6,3);
//		ConcurrentHashSet<Particle> heartParticles = buildParticleSet("Particle from heart",1,3);
//				
//		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,7, new ConcurrentHashSet[]{p1,p2,p3,p4,p5,p6});
//		joinUp(outOfHeart);
//		SingleConnectorPipe[] toHeart = buildPipeArray(8,7, null);
//		joinUp(toHeart);
//	
//		Heart heart = new Heart(heartParticles, 250);
//		heart.getAtrium1().setForwardConnection1(outOfHeart[0]);
//		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
//		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());
//		
//		return heart;
//	}
//	
//	public static Heart buildCirculatorySystemWithTwoParticlesInSixAdajacentCellsWithHeartbeatForce2()
//	{		
//		ConcurrentHashSet<Particle> p1 = buildParticleSet("Particle",1,2);
//		ConcurrentHashSet<Particle> p2 = buildParticleSet("Particle",2,2);
//		ConcurrentHashSet<Particle> p3 = buildParticleSet("Particle",3,2);
//		ConcurrentHashSet<Particle> p4 = buildParticleSet("Particle",4,2);
//		ConcurrentHashSet<Particle> p5 = buildParticleSet("Particle",5,2);
//		ConcurrentHashSet<Particle> p6 = buildParticleSet("Particle",6,2);
//		ConcurrentHashSet<Particle> heartParticles = buildParticleSet("Particle from heart",1,2);
//				
//		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,7, new ConcurrentHashSet[]{p1,p2,p3,p4,p5,p6});
//		joinUp(outOfHeart);
//		SingleConnectorPipe[] toHeart = buildPipeArray(8,7, null);
//		joinUp(toHeart);
//	
//		Heart heart = new Heart(heartParticles, 250);
//		heart.setHeartBeatForce(2);
//		heart.getAtrium1().setForwardConnection1(outOfHeart[0]);
//		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
//		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());
//		
//		return heart;
//	}		
//	
//	public static Heart buildCirculatorySystemWithTwoParticlesInThreeAdajacentCellsWithHeartbeatForce3()
//	{		
//		ConcurrentHashSet<Particle> p1 = buildParticleSet("Particle",1,2);
//		ConcurrentHashSet<Particle> p2 = buildParticleSet("Particle",2,2);
//		ConcurrentHashSet<Particle> p3 = buildParticleSet("Particle",3,2);
//		ConcurrentHashSet<Particle> heartParticles = buildParticleSet("Particle from heart",1,2);
//				
//		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,7, new ConcurrentHashSet[]{p1,p2,p3});
//		joinUp(outOfHeart);
//		SingleConnectorPipe[] toHeart = buildPipeArray(8,7, null);
//		joinUp(toHeart);
//	
//		Heart heart = new Heart(heartParticles, 250);
//		heart.setHeartBeatForce(3);
//		heart.getAtrium1().setForwardConnection1(outOfHeart[0]);
//		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
//		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());
//		
//		return heart;
//	}			
//	
//	public static Heart buildCirculatorySystemWithOneParticleInAtrium()
//	{		
//		ConcurrentHashSet<Particle> heartParticles = buildParticleSet("Particle from heart",1,1);
//				
//		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,7, null);
//		joinUp(outOfHeart);
//		SingleConnectorPipe[] toHeart = buildPipeArray(8,7, null);
//		joinUp(toHeart);
//	
//		Heart heart = new Heart(heartParticles, 250);
//		heart.getAtrium1().setForwardConnection1(outOfHeart[0]);
//		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
//		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());
//		
//		return heart;
//	}	
//
//	public static Heart buildCirculatorySystemWithTwoParticlesInAtrium()
//	{		
//		ConcurrentHashSet<Particle> heartParticles = buildParticleSet("Particle from heart",1,2);
//				
//		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,7, null);
//		joinUp(outOfHeart);
//		SingleConnectorPipe[] toHeart = buildPipeArray(8,7, null);
//		joinUp(toHeart);
//	
//		Heart heart = new Heart(heartParticles, 250);
//		heart.getAtrium1().setForwardConnection1(outOfHeart[0]);
//		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
//		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());
//		
//		return heart;
//	}
//	
//	public static Heart buildCirculatorySystemWithTwoParticlesInAtriumTwoInCellInFrontAndTwoInCellBehind()
//	{
//		ConcurrentHashSet<Particle> heartParticles = buildParticleSet("Particle from heart",1,2);
//		ConcurrentHashSet<Particle> p1 = buildParticleSet("Particle",1,2);
//		ConcurrentHashSet<Particle> p2 = buildParticleSet("Particle",2,2);
//				
//		SingleConnectorPipe[] outOfHeart = buildPipeArray(1,7, new ConcurrentHashSet[]{p1});
//		joinUp(outOfHeart);
//		SingleConnectorPipe[] toHeart = buildPipeArray(8,7, new ConcurrentHashSet[]{null, null, null, null, null, null, p2});
//		joinUp(toHeart);
//	
//		Heart heart = new Heart(heartParticles, 250);
//		heart.getAtrium1().setForwardConnection1(outOfHeart[0]);
//		outOfHeart[outOfHeart.length - 1].setForwardConnection1(toHeart[0]);
//		toHeart[toHeart.length - 1].setForwardConnection1(heart.getAtrium1());
//		
//		return heart;
//	}	
//	
//	private static ConcurrentHashSet<Particle> buildParticleSet(String prepend, int marker, int noOfParticles)
//	{
//		ConcurrentHashSet<Particle> particleSet = new ConcurrentHashSet<Particle>();
//		for(int i = 1; i <= noOfParticles; i++)
//		{
//			particleSet.add(new Particle(prepend + " " + marker + "." + i));
//		}
//		
//		return particleSet;
//	}
//	
	private static void joinUp(SingleConnectorPipe[] pipes)
	{
		int boundary = pipes.length - 1;
		for(int i = 0; i < pipes.length; i++)
		{
			if(i < boundary)
			{
				pipes[i].setForwardConnection1(pipes[i + 1]);
			}
		}
	}

	private static SingleConnectorPipe[] buildPipeArray(int start, int size, ConcurrentHashSet<Particle>[] particles)
	{
		SingleConnectorPipe[] pipes = new SingleConnectorPipe[size];
		int limit = start + size;
		for(int i = start; i < limit; i++)
		{
			int scaledI = i - start;
			if(particles != null && scaledI <= (particles.length - 1) && particles[scaledI] != null)
			{
				pipes[scaledI] = new SingleConnectorPipe("SingleConnectorPipe" + i, particles[scaledI]);
			}
			else
			{
				pipes[scaledI] = new SingleConnectorPipe("SingleConnectorPipe" + i, null);
			}

		}

		return pipes;
	}
}