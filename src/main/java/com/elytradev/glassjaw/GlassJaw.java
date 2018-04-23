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

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=GlassJaw.MODID, version=GlassJaw.VERSION, name="Glass Jaw")
public class GlassJaw {
	public static final String MODID = "glassjaw";
	public static final String VERSION = "@VERSION@";
	
	public static Logger LOG;
	
	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		LOG = LogManager.getLogger("GlassJaw");
		
		
		List<String> active = new ArrayList<String>();
		if (Loader.isModLoaded("draconicevolution")) {
			active.add("Draconic Evolution");
			MinecraftForge.EVENT_BUS.register(Draconic.class);
		}
		
		if (!active.isEmpty()) {
			LOG.info("Active countermeasures: {}", active);
		} else {
			LOG.info("No applicable countermeasures found! Going dormant. Please report an issue if absolute damage isn't getting through.");
		}
	}
}
