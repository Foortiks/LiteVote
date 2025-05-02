package ea.foort.litevote.utils;

import ea.foort.litevote.LiteVote;
import ea.foort.litevote.managers.VoteManager;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {
    private final LiteVote plugin;
    private final VoteManager voteManager;
    private int timeLeft;
    private boolean enabled;

    public Timer(LiteVote plugin, VoteManager voteManager) {
        this.plugin = plugin;
        this.voteManager = voteManager;
        enabled = plugin.getConfig().getBoolean("autostart.enable", true);
        timeLeft = plugin.getConfig().getInt("autostart.timer", 3600);
        if (enabled) startTimer();
    }

    private void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (voteManager.isActive()) return;
                if (timeLeft <= 0) {
                    voteManager.startVote();
                    timeLeft = plugin.getConfig().getInt("autostart.timer", 3600);
                    return;
                }
                timeLeft--;
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
}
