package squeek.quakemovement.mariostates;

import net.minecraft.entity.MovementType;
import net.minecraft.registry.tag.FluidTags;
import org.slf4j.Logger;
import squeek.quakemovement.MarioClient;
import squeek.quakemovement.MarioInputs;
import squeek.quakemovement.ModQuakeMovement;

import java.util.ArrayList;

public abstract class MarioState {
	@FunctionalInterface
	protected interface MarioStateTransition {
		MarioState evaluate();
	}

	protected static final Logger LOGGER = ModQuakeMovement.LOGGER;

	public String name;
	public ArrayList<MarioStateTransition> preTickTransitions;
	public ArrayList<MarioStateTransition> postTickTransitions;
	public ArrayList<MarioStateTransition> postMoveTransitions;

	public MarioState evaluateTransitions(ArrayList<MarioStateTransition> transitions) {
		if(transitions != null) for (MarioStateTransition transition : transitions) {
			MarioState nextState = transition.evaluate();
			if (nextState != null) {
				MarioClient.stateTimer = 0;
				return nextState;
			}
		}
		return this; // Stay in the same state if no transition occurs
	}

	public abstract void tick();

	protected void applyGravity(double accel, double terminalVelocity) {
		if(MarioClient.yVel > terminalVelocity) {
			MarioClient.yVel += accel;
			if(MarioClient.yVel < terminalVelocity) MarioClient.yVel = terminalVelocity;
		}
	}
	protected void applyGravity() {
		applyGravity(-0.115, -3.25);
	}

	protected static class CommonTransitions {
		protected static final MarioStateTransition FALL = () -> {
			if(!MarioClient.player.isOnGround()) {
				MarioClient.yVel = Math.max(0.0, MarioClient.yVel);
				return MarioAerial.INSTANCE;
			}
			return null;
		};

		protected static final MarioStateTransition JUMP = () -> {
			if(MarioInputs.isPressed(MarioInputs.Key.JUMP)) {
				MarioInputs.unbuffer(MarioInputs.Key.JUMP);
				MarioClient.yVel = 0.95;
				return MarioJump.INSTANCE;
			}
			return null;
		};

		protected static final MarioStateTransition LANDING = () -> {
			if(MarioClient.player.isOnGround()) {
				return MarioGrounded.INSTANCE;
			}
			return null;
		};

		protected static final MarioStateTransition ENTER_WATER = () -> {
			if(MarioClient.player.getFluidHeight(FluidTags.WATER) > 0.5) {
				MarioClient.yVel = 1;
			}
			return null;
		};

		protected static final MarioStateTransition LAVA_BOOST = () -> {
			if(MarioClient.player.isInLava()) {
				MarioClient.player.move(MovementType.SELF, MarioClient.player.getVelocity().multiply(-1));
			}
			return null;
		};
	}
}

