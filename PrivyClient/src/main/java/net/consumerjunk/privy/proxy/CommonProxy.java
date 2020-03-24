package net.consumerjunk.privy.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		if(event.getSide() == Side.SERVER) {
			System.out.println("PRIVY CLIENT IS NOT INTENDED FOR SERVER USE!");
		}
	}

	public void init(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {

	}

}
