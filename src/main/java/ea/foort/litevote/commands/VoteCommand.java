package ea.foort.litevote.commands;

import ea.foort.litevote.menus.VoteMenu;
import ea.foort.litevote.utils.Color;
import ea.foort.litevote.managers.VoteManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {
    private final VoteManager voteManager;

    public VoteCommand(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("litevote.vote")) {
            sender.sendMessage(Color.color("&cУ вас нет прав для запуска голосования."));
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Color.color(voteManager.getConfigManager().getMainConfig().getString("messages.voteOnlyPlayer")));
            return true;
        }

        if (!voteManager.isActive()) {
            player.sendMessage(Color.color(voteManager.getConfigManager().getMainConfig().getString("messages.voteNotActive")));
            return true;
        }

        new VoteMenu(voteManager).open(player);
        return true;
    }
}
