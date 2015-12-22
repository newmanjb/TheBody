package com.noomtech.thebody.circulatorysystem;

import com.noomtech.thebody.buildingblocks.Named;
import com.noomtech.thebody.buildingblocks.nerve.NerveImpulseReceiverForJava;
import com.noomtech.thebody.buildingblocks.nerve.NerveImpulseReceiverJavaAdapter;
import com.noomtech.thebody.buildingblocks.transport.SingleConnectorPipe;
import com.noomtech.thebody.communication.Communicator;
import com.noomtech.thebody.communication.CommunicatorFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the heart (obviously).
 * @author Nooom, Noomtech Ltd
 */
public class Heart implements Named, NerveImpulseReceiverForJava
{
	//@todo -- could have several chambers, each one a pipe and connected to the other one like proper heart,
	//but just have one for now with the heart only having one outbound connection
	private final Atrium atrium1;
	private static final Communicator COMMUNICATOR = CommunicatorFactory.getCommunicator();
	private static final String NAME = "Heart";
	private NerveImpulseReceiverJavaAdapter nerveImpulseReceiverJavaAdapter = new NerveImpulseReceiverJavaAdapter(this);
	private static final ExecutorService HEARTBEAT_SERVICE = Executors.newFixedThreadPool(1);

	public static String HEART_BEAT_EVENT = "Heart.Beat";


	public Heart(Atrium atrium, int heartBeatForce)
	{
		atrium1 = atrium;
		atrium1.setBeatForce(heartBeatForce);
	}
	
	public SingleConnectorPipe getAtrium1()
	{
		return atrium1;
	}
	
	public String getName()
	{
		return NAME;
	}


	public NerveImpulseReceiverJavaAdapter getNerveImpulseReceiverJavaAdapter() {
		return nerveImpulseReceiverJavaAdapter;
	}

	public void onNerveImpulse() {
		HEARTBEAT_SERVICE.execute(new Runnable() {public void run() {
			COMMUNICATOR.postEvent(this, "Heartbeat", HEART_BEAT_EVENT);
			atrium1.beat();
		}});
	}
}