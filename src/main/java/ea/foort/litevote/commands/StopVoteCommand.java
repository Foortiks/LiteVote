package ea.foort.litevote.commands;

import ea.foort.litevote.utils.Color;
import ea.foort.litevote.managers.VoteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopVoteCommand implements CommandExecutor {
    private final VoteManager voteManager;

    public StopVoteCommand(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!voteManager.isActive()) {
            sender.sendMessage(Color.color(voteManager.getConfigManager().getMainConfig().getString("messages.voteNoActive")));
            return true;
        }

        voteManager.stopVote();
        return true;
    }
}
