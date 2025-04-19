package ea.foort.litevote.commands;

import ea.foort.litevote.utils.Color;
import ea.foort.litevote.managers.VoteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartVoteCommand implements CommandExecutor {
    private final VoteManager voteManager;

    public StartVoteCommand(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (voteManager.isActive()) {
            sender.sendMessage(Color.color(voteManager.getConfigManager().getMainConfig().getString("messages.voteIsActive")));
            return true;
        }

        voteManager.startVote();
        return true;
    }
}