package ea.foort.litevote;

import ea.foort.litevote.commands.StartVoteCommand;
import ea.foort.litevote.commands.StopVoteCommand;
import ea.foort.litevote.commands.VoteCommand;
import ea.foort.litevote.config.ConfigManager;
import ea.foort.litevote.managers.VoteManager;
import ea.foort.litevote.menus.VoteMenu;
import ea.foort.litevote.utils.Timer;
import org.bukkit.plugin.java.JavaPlugin;

public class LiteVote extends JavaPlugin {
    private static LiteVote instance;
    private ConfigManager configManager;
    private VoteManager voteManager;
    private Timer timer;

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.voteManager = new VoteManager(this);
        this.timer = new Timer(this, voteManager);
        getServer().getPluginManager().registerEvents(new VoteMenu(voteManager), this);
        getCommand("vote").setExecutor(new VoteCommand(voteManager));
        getCommand("startvote").setExecutor(new StartVoteCommand(voteManager));
        getCommand("stopvote").setExecutor(new StopVoteCommand(voteManager));
    }

    public static LiteVote getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public VoteManager getVoteManager() {
        return voteManager;
    }
}
