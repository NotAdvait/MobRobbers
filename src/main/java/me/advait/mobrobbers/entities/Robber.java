package me.advait.mobrobbers.entities;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Robber extends EntityCreature {

    private List<org.bukkit.inventory.ItemStack> stolenItems;

    public List<org.bukkit.inventory.ItemStack> getStolenItems() {
        return stolenItems;
    }

    public void addStolenItem(ItemStack itemStack) {
        stolenItems.add(itemStack);
    }

    public Robber(EntityTypes<? extends EntityCreature> entitytypes, Location loc) {
        super(entitytypes, ((CraftWorld) loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
        this.setCustomName(new ChatComponentText(ChatColor.GOLD + "Robber"));
        this.setCustomNameVisible(false);
        this.goalSelector.a(0, new PathfinderGoalRobber(this, 4, 10));
        this.stolenItems = new ArrayList<>();
        this.goalSelector.a(0, new PathfinderGoalAvoidTarget<EntityPlayer>(
                this, EntityPlayer.class, 30, 2.5D, 2.5D));


        /*
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 3D));
        this.goalSelector.a(2, new PathfinderGoalRandomStrollLand(this, 1D));
        this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
         */
    }

    public void setOwner(Player player) {
        this.setGoalTarget((EntityLiving) ((CraftPlayer) player).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, false);
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalRobber(this, 1, 15));
    }

    public Location getLocation() {
        return new Location(world.getWorld(), this.locX(), this.locY(), this.locZ(), yaw, pitch);
    }
}
