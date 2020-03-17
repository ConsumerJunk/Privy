package net.consumerjunk.privyserver.privy;

import net.consumerjunk.privyserver.privy.events.ClientEventHandler;
import net.consumerjunk.privyserver.privy.poxy.CommonProxy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = PrivyMain.MODID, version = PrivyMain.VERSION, name = PrivyMain.NAME, clientSideOnly = true)
public class PrivyMain {

    public static final String MODID = "privy";
    public static final String VERSION = "1.0";
    public static final String NAME = "Privy";

    //@SidedProxy(clientSide = "net.consumerjunk.privy.proxy.ClientProxy", serverSide = "net.consumerjunk.privy.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final KeyBinding SWITCH_CHAT = new KeyBinding("Switch chat", Keyboard.KEY_P, "Privy - Privatized chat");
    public static final KeyBinding PRIVY_SETTINGS = new KeyBinding("Switch chat", Keyboard.KEY_O, "Privy - Privatized chat");

    @Mod.Instance
    public static PrivyMain instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy = new CommonProxy();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        ClientRegistry.registerKeyBinding(SWITCH_CHAT);
        ClientRegistry.registerKeyBinding(PRIVY_SETTINGS);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        proxy.postInit(event);
    }

}
