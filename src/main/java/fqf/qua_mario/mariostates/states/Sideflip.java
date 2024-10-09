package fqf.qua_mario.mariostates.states;

import fqf.qua_mario.MarioClient;
import fqf.qua_mario.characters.CharaStat;
import fqf.qua_mario.mariostates.AirborneState;
import fqf.qua_mario.stomptypes.stomptypes.StompBasic;

import java.util.ArrayList;
import java.util.Arrays;

public class Sideflip extends AirborneState {
	public static final Sideflip INSTANCE = new Sideflip();

	private Sideflip() {
		this.name = "Sideflip";
		this.isJump = true;
		this.jumpCapStat = CharaStat.SIDEFLIP_CAP;
		this.stompType = StompBasic.INSTANCE;

		preTickTransitions = new ArrayList<>(Arrays.asList(
				AirborneTransitions.DOUBLE_JUMPABLE_LANDING,
				AirborneTransitions.GROUND_POUND
		));
	}

	@Override
	protected void airTick() {
		MarioClient.stateTimer++;
		if(MarioClient.stateTimer > 5)
			MarioClient.aerialAccel(MarioClient.forwardInput * 0.04, MarioClient.rightwardInput * 0.04, 0.25, -0.25, 0.195);
	}
}
