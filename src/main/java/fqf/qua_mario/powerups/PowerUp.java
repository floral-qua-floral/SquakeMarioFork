package fqf.qua_mario.powerups;

import fqf.qua_mario.MarioRegistries;
import fqf.qua_mario.characters.CharaStat;
import fqf.qua_mario.characters.MarioCharacter;
import fqf.qua_mario.mariostates.MarioState;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.EnumMap;

public abstract class PowerUp {
	protected Identifier ID;
	protected String prefix;
	protected float widthFactor;
	protected float heightFactor;

	public abstract MarioState customTransition(MarioState state, MarioState.TransitionPhases phase);
	public abstract MarioState interceptTransition(MarioState from, MarioState to);

	public Identifier getID() {
		return(this.ID);
	}
	public String getPrefix() {
		return(this.prefix);
	}
	public float getWidthFactor() {
		return(this.widthFactor);
	}
	public float getHeightFactor() {
		return(this.heightFactor);
	}
	public String getFormName(MarioCharacter character) {
		return this.getPrefix() + character.getName();
	}
	public void register() {
		Registry.register(MarioRegistries.POWER_UPS, this.getID(), this);
	}
}
