package com.example.invisnoparticles;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class InvisNoParticles extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("InvisNoParticles enabled - invisibility particles will be hidden.");
    }

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        // Only care about Invisibility being added or changed
        if (event.getModifiedType() != PotionEffectType.INVISIBILITY) {
            return;
        }

        PotionEffect newEffect = event.getNewEffect();
        if (newEffect == null) {
            // Effect was removed, nothing to do
            return;
        }

        // Already hidden? Don't loop, just let it through.
        if (!newEffect.hasParticles()) {
            return;
        }

        // Cancel the "particled" version and re-apply an identical
        // effect with particles turned off. Icon stays visible so the
        // player can still see the effect in their inventory HUD.
        event.setCancelled(true);

        PotionEffect silent = new PotionEffect(
                PotionEffectType.INVISIBILITY,
                newEffect.getDuration(),
                newEffect.getAmplifier(),
                newEffect.isAmbient(),
                false,          // particles = off
                newEffect.hasIcon()
        );

        if (event.getEntity() instanceof LivingEntity entity) {
            entity.addPotionEffect(silent);
        }
    }
}
