package net.consumerjunk.privy;

import net.consumerjunk.privy.events.ClientEventHandler;
import net.consumerjunk.privy.networking.PrivyController;
import net.consumerjunk.privy.proxy.CommonProxy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = Privy.MODID, version = Privy.VERSION, name = Privy.NAME, clientSideOnly = true)
public class Privy {

    public static final String MODID = "privy";
    public static final String VERSION = "1.0";
    public static final String NAME = "Privy";

    @SidedProxy(clientSide = "net.consumerjunk.privy.proxy.ClientProxy", serverSide = "net.consumerjunk.privy.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final KeyBinding SWITCH_CHAT = new KeyBinding("Switch networking", Keyboard.KEY_P, "Privy - Privatized chat");
    public static final KeyBinding PRIVY_SETTINGS = new KeyBinding("Privy settings", Keyboard.KEY_O, "Privy - Privatized chat");

    @Mod.Instance
    public static Privy instance;

    public static PrivyController privyController;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        privyController = new PrivyController();
        proxy = new CommonProxy();
        proxy.preInit(event);
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        ClientRegistry.registerKeyBinding(SWITCH_CHAT);
        ClientRegistry.registerKeyBinding(PRIVY_SETTINGS);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
