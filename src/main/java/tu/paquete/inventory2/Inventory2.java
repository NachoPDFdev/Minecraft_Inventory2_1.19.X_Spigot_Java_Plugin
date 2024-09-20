package tu.paquete.inventory2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Inventory2 extends JavaPlugin implements Listener {

    private Map<UUID, Inventory> playerInventories;

    @Override
    public void onEnable() {
        playerInventories = new HashMap<>();
        getServer().getPluginManager().registerEvents(this, this);

        // Register the command
        this.getCommand("inv2").setExecutor(this);

        getLogger().info(ChatColor.GREEN + "Inventory2 plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "Inventory2 plugin has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("inv2")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
                return true;
            }

            Player player = (Player) sender;
            openSecondInventory(player);
            return true;
        }
        return false;
    }

    private void openSecondInventory(Player player) {
        UUID playerUUID = player.getUniqueId();
        Inventory inventory = playerInventories.get(playerUUID);

        if (inventory == null) {
            inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + "Second Inventory");
            playerInventories.put(playerUUID, inventory);
        }

        player.openInventory(inventory);
        player.sendMessage(ChatColor.AQUA + "Opening your second inventory!");
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() == null && event.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Second Inventory")) {
            playerInventories.put(event.getPlayer().getUniqueId(), event.getInventory());
        }
    }
}
