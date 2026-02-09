package me.realized.duels.hook.hooks;

import dev.kitteh.factions.event.PowerLossEvent;
import me.realized.duels.DuelsPlugin;
import me.realized.duels.arena.ArenaManagerImpl;
import me.realized.duels.config.Config;
import me.realized.duels.util.Log;
import me.realized.duels.util.hook.PluginHook;
import me.realized.duels.util.reflect.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FactionsUUIDHook extends PluginHook<DuelsPlugin> {

    public static final String NAME = "Factions";

    private final Config config;
    private final ArenaManagerImpl arenaManager;

    public FactionsUUIDHook(final DuelsPlugin plugin) {
        super(plugin, NAME);
        this.config = plugin.getConfiguration();
        this.arenaManager = plugin.getArenaManager();

        Listener listener = null;

        if (ReflectionUtil.getClassUnsafe("dev.kitteh.factions.event.PowerLossEvent") != null) {
            listener = new FactionsUUIDListener();
        }

        if (listener == null) {
            Log.error("Could not detect this version of Factions. Please contact the developer if you believe this is an error.");
            return;
        }

        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public class FactionsUUIDListener implements Listener {

        @EventHandler
        public void on(final PowerLossEvent event) {
            if (!config.isFuNoPowerLoss()) {
                return;
            }

            final Player player = event.getFPlayer().asPlayer();

            if (player == null || !arenaManager.isInMatch(player)) {
                return;
            }

            event.setCancelled(true);
        }
    }
}