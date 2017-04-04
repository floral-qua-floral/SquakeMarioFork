package squeek.quakemovement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ModInfo.MODID, version = ModInfo.VERSION, name="Squake", acceptedMinecraftVersions="[1.11,1.12)", dependencies = "after:squeedometer")
public class ModQuakeMovement
{
	// The instance of your mod that Forge uses.
	@Instance(value = ModInfo.MODID)
	public static ModQuakeMovement instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModConfig.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}

	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event)
	{
		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		if (ModConfig.INCREASED_FALL_DISTANCE != 0.0D)
		{
			event.setDistance((float) (event.getDistance() - ModConfig.INCREASED_FALL_DISTANCE));
		}
	}
}
