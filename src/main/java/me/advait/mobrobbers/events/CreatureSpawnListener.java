package me.advait.mobrobbers.events;

import me.advait.mobrobbers.entities.Robber;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftNamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;

public class CreatureSpawnListener implements Listener {

    //@SuppressWarnings("unchecked")
    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        /*int random = new Random(4).nextInt();
        if (random != 4) return;*/

        EntityTypes<? extends EntityCreature> type = (EntityTypes<? extends EntityCreature>)
                ((CraftEntity) event.getEntity()).getHandle().getEntityType();

        if (event.getEntity() instanceof Robber || event.getEntity().getCustomName() != null) {
            return;
        }

        if (getNearest(event.getEntity()) == null) return;

        Robber robber = new Robber(type, event.getLocation());

        WorldServer world = ((CraftWorld) event.getEntity().getWorld()).getHandle();
        world.addEntity(robber, CreatureSpawnEvent.SpawnReason.CUSTOM);

        robber.setOwner(getNearest(event.getEntity()));
        return;

    }

    private Player getNearest(LivingEntity creature) {
        Player nearest = null;
        double distance = Double.MAX_VALUE;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            double distanceSquared = onlinePlayer.getLocation().distanceSquared(new Location(creature.getWorld(),
                    creature.getLocation().getX(),
                    creature.getLocation().getY(), creature.getLocation().getZ()));
            if (distanceSquared < distance) {
                distance = distanceSquared;
                nearest = onlinePlayer;
            }
            if (!nearest.isOnline()) {
                return null;
            }
        }
        return nearest;
    }

}
