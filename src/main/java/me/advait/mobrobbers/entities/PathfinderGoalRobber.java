package me.advait.mobrobbers.entities;

import me.advait.mobrobbers.MobRobbers;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class PathfinderGoalRobber extends PathfinderGoal {

    private final Robber robber;
    private EntityLiving player;

    private final double speed;
    //private final float distance;

    private double x;
    private double y;
    private double z;


    public PathfinderGoalRobber(Robber robber, double speed, float distance) {
        this.robber = robber;
        this.speed = speed;
        //this.distance = distance;
        this.a(EnumSet.of(Type.MOVE));
    }

    @Override
    public boolean a() {
        this.player = this.robber.getGoalTarget();
        Location locPlayer = new Location(player.getWorld().getWorld(), player.locX(), player.locY(), player.locZ());
        Location locRobber = robber.getLocation();
        if (this.player == null) {
            return false;
        } else if (locPlayer.distance(locRobber) <= 3) {
            Player p = Bukkit.getPlayer(player.getUniqueID());
            ItemStack item = getRandomItem(p.getInventory().getContents());
            if (!robber.getStolenItems().isEmpty()) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MobRobbers.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        robber.goalSelector.a(0, new PathfinderGoalAvoidTarget<EntityPlayer>(
                                robber, EntityPlayer.class, 30, 2.5D, 2.5D));
                    }
                }, 20);
                return false;
            }
            robber.addStolenItem(item);
            p.getInventory().remove(item);
            return false;
        } else if (this.robber.getDisplayName() == null) {
            return false;
        } else if (!robber.getStolenItems().isEmpty()) {
            return false;
        }
        else {
            Vec3D vec = RandomPositionGenerator.a((EntityCreature) this.robber, 16, 7, this.player.getPositionVector());
            if (vec == null) return false;
            this.x = this.player.locX();
            this.y = this.player.locY();
            this.z = this.player.locZ();

            return true;
        }
    }

    public void c() {
        this.robber.getNavigation().a(this.x, this.y, this.z, this.speed);
    }

    public boolean b() {
        return !this.robber.getNavigation().m() && this.player.h(this.robber) < (double) (this.speed * this.speed);
    }

    public void d() {
        this.player = null;
    }

    public org.bukkit.inventory.ItemStack getRandomItem(org.bukkit.inventory.ItemStack[] content) {
        if (isInventoryEmpty(content)) return null;
        List<org.bukkit.inventory.ItemStack> items = Arrays.asList(content);
        if (items.isEmpty())
            Collections.shuffle(items);
        while (items.get(0) == null) {
            Collections.shuffle(items);
        }
        return items.get(0);
    }

    public boolean isInventoryEmpty(org.bukkit.inventory.ItemStack[] content) {
        for (ItemStack item : content) {
            if (item != null) return false;
        }
        return true;
    }

}
