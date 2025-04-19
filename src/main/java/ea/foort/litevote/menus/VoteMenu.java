package ea.foort.litevote.menus;

import ea.foort.litevote.managers.VoteManager;
import ea.foort.litevote.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VoteMenu implements Listener {
    private final VoteManager voteManager;

    public VoteMenu(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    public void open(Player player) {
        Configuration config = voteManager.getConfigManager().getMenuConfig();
        int size = config.getInt("menu.size", 27);
        String title = Color.color(config.getString("menu.title"));
        Inventory inv = Bukkit.createInventory(null, size, title);

        ConfigurationSection section = config.getConfigurationSection("votes");
        for (String key : section.getKeys(false)) {
            String path = "votes." + key;
            Material mat = Material.valueOf(config.getString(path + ".material"));
            int slot = config.getInt(path + ".slot");
            String name = config.getString(path + ".name");
            List<String> lore = new ArrayList<>(config.getStringList(path + ".lore"));
            int votes = voteManager.getVotes(key);

            lore = lore.stream().map(Color::color).collect(Collectors.toList());
            lore.add(Color.color(voteManager.getConfigManager().getMainConfig().getString("messages.currentVotes")
                    .replace("{votes}", String.valueOf(votes))));

            if (voteManager.hasVoted(player, key)) {
                lore.add(Color.color(voteManager.getConfigManager().getMainConfig().getString("messages.voteSelected")));
            }

            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Color.color(name));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(slot, item);
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (!voteManager.isActive()) return;

        String title = Color.color(voteManager.getConfigManager().getMenuConfig().getString("menu.title"));
        if (!e.getView().getTitle().equals(title)) return;

        e.setCancelled(true);

        for (String key : voteManager.getConfigManager().getMenuConfig().getConfigurationSection("votes").getKeys(false)) {
            int slot = voteManager.getConfigManager().getMenuConfig().getInt("votes." + key + ".slot");
            if (e.getSlot() == slot) {
                if (voteManager.hasVoted(player, key)) {
                    player.sendMessage(Color.color(voteManager.getConfigManager().getMainConfig().getString("messages.voteAlreadyCast")));
                    return;
                }
                voteManager.registerVote(player, key);
                open(player);
                return;
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {}
}