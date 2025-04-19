package ea.foort.litevote.managers;

import ea.foort.litevote.LiteVote;
import ea.foort.litevote.config.ConfigManager;
import ea.foort.litevote.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class VoteManager {
    private final ConfigManager configManager;
    private boolean active = false;
    private final Map<UUID, String> playerVotes = new HashMap<>();
    private final Map<String, Integer> votes = new HashMap<>();

    public VoteManager(LiteVote plugin) {
        this.configManager = plugin.getConfigManager();
    }

    public boolean isActive() {
        return active;
    }

    public void startVote() {
        active = true;
        playerVotes.clear();
        votes.clear();

        ConfigurationSection section = configManager.getMenuConfig().getConfigurationSection("votes");
        for (String key : section.getKeys(false)) {
            votes.put(key, 0);
        }

        for (String msg : configManager.getMainConfig().getStringList("start.voteStarted")) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage(Color.color(msg));
            }
        }

        try {
            Sound sound = Sound.valueOf(configManager.getMainConfig().getString("start.sound.type"));
            float volume = (float) configManager.getMainConfig().getDouble("start.sound.volume");
            float pitch = (float) configManager.getMainConfig().getDouble("start.sound.pitch");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), sound, volume, pitch);
            }
        } catch (Exception ignored) {}

        long duration = configManager.getMainConfig().getLong("settings.vote-duration", 60) * 20L;
        new BukkitRunnable() {
            public void run() {
                if (active) stopVote();
            }
        }.runTaskLater(LiteVote.getInstance(), duration);
    }

    public void stopVote() {
        active = false;

        String menuTitle = Color.color(configManager.getMenuConfig().getString("menu.title"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory().getTitle().equals(menuTitle)) {
                player.closeInventory();
            }
        }

        String winner = null;
        int maxVotes = 0;
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                winner = entry.getKey();
            }
        }

        if (winner == null) {
            String noVotesMessage = Color.color(configManager.getMainConfig().getString("messages.noVotes"));
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage(noVotesMessage);
            }
            return;
        }

        ConfigurationSection winnerSection = configManager.getMenuConfig().getConfigurationSection("votes." + winner);
        List<String> winMessage = winnerSection.getStringList("win-message");
        String command = winnerSection.getString("command");
        String soundName = winnerSection.getString("sound");

        for (String msg : winMessage) {
            String formattedMessage = msg.replace("{votes}", String.valueOf(maxVotes));
            String coloredMessage = Color.color(formattedMessage);
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage(coloredMessage);
            }
        }

        try {
            Sound sound = Sound.valueOf(soundName);
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), sound, 1f, 1f);
            }
        } catch (Exception ignored) {}

        new BukkitRunnable() {
            public void run() {
                if (command != null && !command.isEmpty()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }
        }.runTaskLater(LiteVote.getInstance(), configManager.getMainConfig().getInt("settings.command-delay", 60) * 20L);
    }

    public void registerVote(Player player, String key) {
        String previous = playerVotes.put(player.getUniqueId(), key);
        if (previous != null && !previous.equals(key)) {
            votes.put(previous, votes.get(previous) - 1);
        }

        votes.put(key, votes.getOrDefault(key, 0) + 1);
        String message = configManager.getMainConfig().getString("messages.voteCast")
                .replace("{option}", configManager.getMenuConfig().getString("votes." + key + ".name"));
        player.sendMessage(Color.color(message));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public boolean hasVoted(Player player, String key) {
        return key.equals(playerVotes.get(player.getUniqueId()));
    }

    public int getVotes(String key) {
        return votes.getOrDefault(key, 0);
    }
}
