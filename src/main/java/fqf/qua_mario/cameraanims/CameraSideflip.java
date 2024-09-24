package fqf.qua_mario.cameraanims;

import net.minecraft.util.math.MathHelper;

public class CameraSideflip extends CameraAnim {
	public static final CameraSideflip INSTANCE = new CameraSideflip();

	private CameraSideflip() {
		this.name = "Sideflip";
		this.duration = 13.5;
	}

	@Override
	public double[] getRotations(double progress) {
		return new double[]{cappedLerp(progress, 180, 0), 0.0, cappedLerp(progress, 0, 360)};
	}
}
