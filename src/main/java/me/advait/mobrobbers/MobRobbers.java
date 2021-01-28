package me.advait.mobrobbers;

import me.advait.mobrobbers.events.CreatureSpawnListener;
import me.advait.mobrobbers.tasks.MobRobTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobRobbers extends JavaPlugin {

    private static MobRobbers instance;

    public static MobRobbers getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        //Bukkit.getScheduler().scheduleSyncRepeatingTask(MobRobbers.getInstance(), new MobRobTask(), 0, 20);
        getServer().getPluginManager().registerEvents(new CreatureSpawnListener(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

}
