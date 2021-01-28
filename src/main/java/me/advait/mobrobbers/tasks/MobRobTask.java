package me.advait.mobrobbers.tasks;

import me.advait.mobrobbers.entities.Robber;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MobRobTask implements Runnable {

    @Override
    public void run() {
        for (World world : Bukkit.getServer().getWorlds()) {
            List<Robber> robbers = new ArrayList<>();
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Robber) {
                    System.out.println("Found a robber!");
                    robbers.add((Robber) entity);
                }
                else {
                    System.out.println("Cannot find robber!");
                    continue;
                }
            }
            //System.out.println(robbers);
            for (Robber robber : robbers) {
                Player player = getNearest((LivingEntity) robber);
                if (player == null) {
                    System.out.println("Could not find a player close to the robber!");
                    continue;
                }
                if (player.getLocation().distance(robber.getLocation()) < 3) {
                    player.sendMessage(ChatColor.GREEN + "You are close to a robber!");
                    ItemStack stolen = getRandomItem(player.getInventory().getContents());
                    if (stolen == null) {
                        continue;
                    }
                    player.getInventory().remove(stolen);
                    robber.addStolenItem(stolen);
                    player.sendMessage("A robber just mugged you!");
                }
            }
        }
    }

    public ItemStack getRandomItem(ItemStack[] content) {
        if (isInventoryEmpty(content)) return null;
        List<ItemStack> items = Arrays.asList(content);
        if (items.isEmpty())
        Collections.shuffle(items);
        while (items.get(0) == null) {
            Collections.shuffle(items);
        }
        return items.get(0);
    }

    public boolean isInventoryEmpty(ItemStack[] content) {
        for (ItemStack item : content) {
            if (item != null) return false;
        }
        return true;
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
