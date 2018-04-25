/*
 * MIT License
 *
 * Copyright (c) 2018 Isaac Ellingson (Falkreon) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.elytradev.glassjaw;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elytradev.glassjaw.countermeasures.AttackFixer;
import com.elytradev.glassjaw.countermeasures.Draconic;
import com.elytradev.glassjaw.countermeasures.ICountermeasure;
import com.elytradev.glassjaw.item.ItemGlassDagger;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid=GlassJaw.MODID, version=GlassJaw.VERSION, name="Glass Jaw")
public class GlassJaw {
	public static final String MODID = "glassjaw";
	public static final String VERSION = "@VERSION@";
	
	public static Logger LOG;
	@SidedProxy(clientSide="com.elytradev.glassjaw.client.ClientProxy", serverSide="com.elytradev.glassjaw.Proxy")
	public static Proxy PROXY;
	
	public static ItemGlassDagger GLASS_DAGGER;
	
	private static Map<String, ICountermeasure> availableMeasures = new HashMap<>();
	private static Map<String, ICountermeasure> active = new HashMap<>();
	static {
		register(new Draconic());
		register(new AttackFixer());
	}
	
	/**
	 * If you really want, third parties can register any time by first using a Class.forName() check, probably even in
	 * FMLConstructionEvent. However, it's better to just PR a countermeasure to this mod instead if you can.
	 */
	public static void register(ICountermeasure measure) {
		if (availableMeasures.containsKey(measure.getConfigName())) throw new IllegalArgumentException("Can't re-register the countermeasure '"+measure.getConfigName()+"'.");
		availableMeasures.put(measure.getConfigName(), measure);
	}
	
	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		LOG = LogManager.getLogger("GlassJaw");
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		String[] countermeasures = config.getStringList("countermeasures", "general", new String[]{"attackfixer"}, "The list of countermeasures to enable. draconic may be less invasive than attackfixer, but attackfixer can fix unknown mods.", availableMeasures.keySet().toArray(new String[availableMeasures.size()]));
		config.save();
		
		for(String key : countermeasures) {
			ICountermeasure measure = availableMeasures.get(key);
			if (measure==null) {
				LOG.warn("Can't acquire a countermeasure for key \"{}\"! ");
			} else {
				measure.enable();
				MinecraftForge.EVENT_BUS.register(measure);
				active.put(measure.getConfigName(), measure);
			}
		}
		
		if (!active.isEmpty()) {
			LOG.info("Active countermeasures: {}", active.keySet());
		} else {
			LOG.info("No countermeasures activated! Going dormant.");
		}
		
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(PROXY);
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		GLASS_DAGGER = new ItemGlassDagger();
		event.getRegistry().register(GLASS_DAGGER);
	}
	
	/*//Disabled for now, but be warned: Tampering with event lists is mutually assured destruction.
	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		try {
			Field field = EventBus.class.getDeclaredField("listeners");
			field.setAccessible(true);
			@SuppressWarnings("unchecked")
			ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = (ConcurrentHashMap<Object, ArrayList<IEventListener>>) field.get(MinecraftForge.EVENT_BUS);
			System.out.println(listeners.toString());
		} catch (Throwable t) {
			System.out.println("Unable to tamper with event listener priorities. ("+t.toString()+")");
		}
	}*/
}
