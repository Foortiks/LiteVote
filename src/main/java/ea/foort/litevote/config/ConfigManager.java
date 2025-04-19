package ea.foort.litevote.config;

import ea.foort.litevote.LiteVote;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {
    private final File menuFile;
    private final FileConfiguration menuConfig;
    private final FileConfiguration config;

    public ConfigManager(LiteVote plugin) {
        plugin.saveResource("menu.yml", false);
        this.menuFile = new File(plugin.getDataFolder(), "menu.yml");
        this.menuConfig = YamlConfiguration.loadConfiguration(menuFile);
        this.config = plugin.getConfig();
    }

    public FileConfiguration getMainConfig() {
        return config;
    }

    public FileConfiguration getMenuConfig() {
        return menuConfig;
    }
}
