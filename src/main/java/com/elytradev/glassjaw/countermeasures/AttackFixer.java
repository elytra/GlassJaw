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

package com.elytradev.glassjaw.countermeasures;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AttackFixer implements ICountermeasure {
	
	@SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
	public void onEntityAttackHighest(LivingAttackEvent event) {
		if (event.getEntityLiving().getEntityWorld().isRemote) return;
		
		if (event.getSource().isDamageAbsolute() && event.getSource().isUnblockable()) {
			event.setCanceled(true); //Will be uncanceled later
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
	public void onEntityAttackLowest(LivingAttackEvent event) {
		if (event.getEntityLiving().getEntityWorld().isRemote) return;
		
		if (event.getSource().isDamageAbsolute() && event.getSource().isUnblockable()) {
			event.setCanceled(false);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
	public void onEntityDeathHighest(LivingDeathEvent event) {
		if (event.getEntityLiving().getEntityWorld().isRemote) return;
		
		if (event.getSource().isDamageAbsolute() && event.getSource().isUnblockable()) {
			event.setCanceled(true); //Will be uncanceled later
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
	public void onEntityDeathLowest(LivingDeathEvent event) {
		if (event.getEntityLiving().getEntityWorld().isRemote) return;
		
		if (event.getSource().isDamageAbsolute() && event.getSource().isUnblockable()) {
			event.setCanceled(false);
		}
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
	public void onGetHurtHighest(LivingHurtEvent event) {
		if (event.getEntityLiving().getEntityWorld().isRemote) return;
		
		if (event.getSource().isDamageAbsolute() && event.getSource().isUnblockable()) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=true)
	public void onGetHurtLowest(LivingHurtEvent event) {
		if (event.getEntityLiving().getEntityWorld().isRemote) return;
		
		if (event.getSource().isDamageAbsolute() && event.getSource().isUnblockable()) {
			event.setCanceled(false);
		}
	}
	
	@Override
	public String getConfigName() {
		return "attackfixer";
	}

	@Override
	public String getDisplayName() {
		return "Generic AttackFixer";
	}

	@Override
	public void enable() {}
}
