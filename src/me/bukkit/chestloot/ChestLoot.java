package me.bukkit.chestloot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestLoot extends JavaPlugin implements Listener{
	private int counter1;
	private int counter2;
	private int counter3;
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		loadConfig();
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("ChestLoot has been disabled");
	}
	
	public void loadConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			
			if(player.isOp()) {
				
				if (cmd.getName().equalsIgnoreCase("save")) {
					@SuppressWarnings("deprecation")
					Block block = player.getTargetBlock((HashSet<Byte>)null, 20);
					
					if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
						
						Location location = block.getLocation();
						
						switch(args[0]) {
						
						case("1"):
							this.getConfig().set("Tier1.Chest_" + counter1 + ".x", location.getX());
							this.getConfig().set("Tier1.Chest_" + counter1 + ".y", location.getY());
							this.getConfig().set("Tier1.Chest_" + counter1 + ".z", location.getZ());
							counter1++;
							player.sendMessage(ChatColor.GREEN + "Chest at " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " saved as item " + counter1 + " to tier1.");
							this.saveConfig();
							break;
						
						case("2"):
							this.getConfig().set("Tier2.Chest_" + counter2 + ".x", location.getX());
							this.getConfig().set("Tier2.Chest_" + counter2 + ".y", location.getY());
							this.getConfig().set("Tier2.Chest_" + counter2 + ".z", location.getZ());
							counter2++;
							player.sendMessage(ChatColor.GREEN + "Chest at " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " saved as item " + counter2 + " to tier2.");
							this.saveConfig();
							break;
							
						case("3"):
							this.getConfig().set("Tier3.Chest_" + counter3 + ".x", location.getX());
							this.getConfig().set("Tier3.Chest_" + counter3 + ".y", location.getY());
							this.getConfig().set("Tier3.Chest_" + counter3 + ".z", location.getZ());
							counter3++;
							player.sendMessage(ChatColor.GREEN + "Chest at " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " saved as item " + counter3 + " to tier3.");
							this.saveConfig();
							break;
						}
					}
					
					else {
						player.sendMessage(ChatColor.RED + "The block you are looking at isn't a chest.");
					}

				}
				
				else if (cmd.getName().equalsIgnoreCase("load")) {
					
					
					switch(args[0]) {
					
					case("1"):
						for(String key : this.getConfig().getConfigurationSection("Tier1").getKeys(false)) {
							HashMap<ItemStack, Integer> loottable1 = initLootTable1();
							
							double x = this.getConfig().getDouble("Tier1." + key + ".x");
							double y = this.getConfig().getDouble("Tier1." + key + ".y");
							double z = this.getConfig().getDouble("Tier1." + key + ".z");
							
							Location location = new Location(this.getServer().getWorld("world"), 0, 0, 0);
							location.setX(x);
							location.setY(y);
							location.setZ(z);
							
							refillChest(location, loottable1, 3);
							player.sendMessage(ChatColor.GREEN + "Chest at " + x + " " + y + " " + z + " has been refilled");
						}
					break;
					
					case("2"):
						for(String key : this.getConfig().getConfigurationSection("Tier2").getKeys(false)) {
							HashMap<ItemStack, Integer> loottable2 = initLootTable2();
							
						    double x = this.getConfig().getDouble("Tier2." + key + ".x");
							double y = this.getConfig().getDouble("Tier2." + key + ".y");
							double z = this.getConfig().getDouble("Tier2." + key + ".z");
							
							Location location = new Location(this.getServer().getWorld("world"), 0, 0, 0);
							location.setX(x);
							location.setY(y);
							location.setZ(z);
							
							refillChest(location, loottable2, 3);
							player.sendMessage(ChatColor.GREEN + "Chest at " + x + " " + y + " " + z + " has been refilled");
						    
							
						}
					break;
					
					case("3"):
						for(String key : this.getConfig().getConfigurationSection("Tier3").getKeys(false)) {
							HashMap<ItemStack, Integer> loottable3 = initLootTable3();
							Random random = new Random();
							
						    double x = this.getConfig().getDouble("Tier3." + key + ".x");
							double y = this.getConfig().getDouble("Tier3." + key + ".y");
							double z = this.getConfig().getDouble("Tier3." + key + ".z");
							
							Location location = new Location(this.getServer().getWorld("world"), 0, 0, 0);
							location.setX(x);
							location.setY(y);
							location.setZ(z);
							
							refillChest(location, loottable3, random.nextInt(3 + 1 - 2) + 2);
							player.sendMessage(ChatColor.GREEN + "Chest at " + x + " " + y + " " + z + " has been refilled");
							
						}
					break;
					
					}
					
				}
				
				return true;
			}
			
			else {
				player.sendMessage(ChatColor.RED + "You must be OP to run this command.");
			}
			
			return true;

		}
		
		else {
			sender.sendMessage(ChatColor.RED + "Only players can run this command.");
		}
		
		return false;
	}
	
	public static void refillChest(Location loc, HashMap<ItemStack, Integer> itemsWithChance, Integer maxItems){
		if(isChest(loc)){
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for(int i = 0; items.size() <= 1; i++){
				for(ItemStack item : itemsWithChance.keySet()){
					if(isSuccessfull(itemsWithChance.get(item))){
						items.add(item);
					}
				}
				if(i >= 100){
					Bukkit.getLogger().info("[ChestRefill] No items were selected so the refilled chest will be empty.");
					return;
				}
			}
			for(; items.size() > maxItems;){
				items.remove(new Random().nextInt(items.size()));
			}
			Chest chest = (Chest) loc.getBlock().getState();
			chest.getBlockInventory().clear();
			for(ItemStack item : items){
				for(int i = 0; i <= 100; i++){
					int slot = new Random().nextInt(chest.getBlockInventory().getSize());
					if(chest.getBlockInventory().getItem(slot) == null){
						chest.getBlockInventory().setItem(slot, item);
						break;
					}
				}
			}
		}
	}
	
	private static boolean isSuccessfull(int chance){
		if(chance >= 100){
			return true;
		}
		Random number = new Random();
		int c = 1 + number.nextInt(chance);
		if(c >= 1 && c <= chance){
			return true;
		}
		return false;
	}
	
	public static Boolean isChest(Location loc){
		if(loc.getBlock() != null){
			if(loc.getBlock().getType() == Material.CHEST){
				return true;
			}
		}
		return false;
	}
	
	public static HashMap<ItemStack, Integer> initLootTable1() {
		
		HashMap<ItemStack, Integer> loot = new HashMap<ItemStack, Integer>();
		
		loot.put(new ItemStack(Material.WOOD_AXE), 100);
		loot.put(new ItemStack(Material.WOOD_AXE), 100);
		loot.put(new ItemStack(Material.WOOD_AXE), 100);
		loot.put(new ItemStack(Material.WOOD_SWORD), 100);
		loot.put(new ItemStack(Material.WOOD_SWORD), 100);
		loot.put(new ItemStack(Material.WOOD_SWORD), 100);
		loot.put(new ItemStack(Material.LEATHER_HELMET), 100);
		loot.put(new ItemStack(Material.LEATHER_CHESTPLATE), 100);
		loot.put(new ItemStack(Material.LEATHER_LEGGINGS), 100);
		loot.put(new ItemStack(Material.LEATHER_BOOTS), 100);
		loot.put(new ItemStack(Material.STICK), 100);
		loot.put(new ItemStack(Material.GOLD_INGOT), 100);
		loot.put(new ItemStack(Material.GOLD_SWORD), 100);
		loot.put(new ItemStack(Material.GOLD_SWORD), 100);
		loot.put(new ItemStack(Material.GOLD_SWORD), 100);
		loot.put(new ItemStack(Material.STONE_AXE), 100);
		loot.put(new ItemStack(Material.STONE_AXE), 100);
		loot.put(new ItemStack(Material.STONE_AXE), 100);
		loot.put(new ItemStack(Material.APPLE), 100);
		loot.put(new ItemStack(Material.STRING), 100);
		loot.put(new ItemStack(Material.RED_MUSHROOM), 100);
		loot.put(new ItemStack(Material.BROWN_MUSHROOM), 100);
		loot.put(new ItemStack(Material.BOWL), 100);
		loot.put(new ItemStack(Material.ARROW), 100);
		
		loot.put(new ItemStack(Material.GOLD_HELMET), 5);
		loot.put(new ItemStack(Material.GOLD_CHESTPLATE), 5);
		loot.put(new ItemStack(Material.GOLD_LEGGINGS), 5);
		loot.put(new ItemStack(Material.GOLD_BOOTS), 5);
		loot.put(new ItemStack(Material.CHAINMAIL_HELMET), 5);
		loot.put(new ItemStack(Material.CHAINMAIL_CHESTPLATE), 5);
		loot.put(new ItemStack(Material.CHAINMAIL_LEGGINGS), 5);
		loot.put(new ItemStack(Material.CHAINMAIL_BOOTS), 5);
		
		return loot;
		
	}
	
	public static HashMap<ItemStack, Integer> initLootTable2() {
		
		HashMap<ItemStack, Integer> loot = new HashMap<ItemStack, Integer>();
		
		loot.put(new ItemStack(Material.WOOD_SWORD), 100);
		loot.put(new ItemStack(Material.WOOD_SWORD), 100);
		loot.put(new ItemStack(Material.WOOD_SWORD), 100);
		loot.put(new ItemStack(Material.STONE_AXE), 100);
		loot.put(new ItemStack(Material.STONE_AXE), 100);
		loot.put(new ItemStack(Material.STONE_AXE), 100);
		loot.put(new ItemStack(Material.STONE_SWORD), 100);
		loot.put(new ItemStack(Material.STONE_SWORD), 100);
		loot.put(new ItemStack(Material.STONE_SWORD), 100);
		loot.put(new ItemStack(Material.GOLD_HELMET), 100);
		loot.put(new ItemStack(Material.GOLD_CHESTPLATE), 100);
		loot.put(new ItemStack(Material.GOLD_LEGGINGS), 100);
		loot.put(new ItemStack(Material.GOLD_BOOTS), 100);
		loot.put(new ItemStack(Material.CHAINMAIL_HELMET), 100);
		loot.put(new ItemStack(Material.CHAINMAIL_CHESTPLATE), 100);
		loot.put(new ItemStack(Material.CHAINMAIL_LEGGINGS), 100);
		loot.put(new ItemStack(Material.CHAINMAIL_BOOTS), 100);
		loot.put(new ItemStack(Material.FISHING_ROD), 100);
		loot.put(new ItemStack(Material.STRING), 100);
		loot.put(new ItemStack(Material.STICK), 100);
		loot.put(new ItemStack(Material.GRILLED_PORK), 100);
		loot.put(new ItemStack(Material.COOKED_BEEF), 100);
		loot.put(new ItemStack(Material.IRON_INGOT), 100);
		loot.put(new ItemStack(Material.GOLD_INGOT), 100);
		loot.put(new ItemStack(Material.BOW), 100);
		loot.put(new ItemStack(Material.EGG), 100);
		loot.put(new ItemStack(Material.EXP_BOTTLE), 100);
		
		loot.put(new ItemStack(Material.IRON_HELMET), 5);
		loot.put(new ItemStack(Material.IRON_CHESTPLATE), 5);
		loot.put(new ItemStack(Material.IRON_LEGGINGS), 5);
		loot.put(new ItemStack(Material.IRON_BOOTS), 5);
		loot.put(new ItemStack(Material.DIAMOND), 5);
		loot.put(new ItemStack(Material.IRON_SWORD), 10);
		
		return loot;
		
	}
	
	public static HashMap<ItemStack, Integer> initLootTable3() {
		
		HashMap<ItemStack, Integer> loot = new HashMap<ItemStack, Integer>();
		
		loot.put(new ItemStack(Material.IRON_HELMET), 100);
		loot.put(new ItemStack(Material.IRON_CHESTPLATE), 100);
		loot.put(new ItemStack(Material.IRON_LEGGINGS), 100);
		loot.put(new ItemStack(Material.IRON_BOOTS), 100);
		loot.put(new ItemStack(Material.DIAMOND), 100);
		loot.put(new ItemStack(Material.STONE_SWORD), 100);
		loot.put(new ItemStack(Material.GOLDEN_APPLE), 100);
		loot.put(new ItemStack(Material.IRON_INGOT), 100);
		loot.put(new ItemStack(Material.IRON_SWORD), 100);
		loot.put(new ItemStack(Material.IRON_SWORD), 100);
		loot.put(new ItemStack(Material.DIAMOND_AXE), 100);
		
		return loot;
		
	}
	
	public static HashMap<ItemStack, Integer> initCLootTable3() {
		
		HashMap<ItemStack, Integer> loot = new HashMap<ItemStack, Integer>();
		Random random = new Random();
		
		loot.put(new ItemStack(Material.IRON_HELMET), 1);
		loot.put(new ItemStack(Material.IRON_CHESTPLATE), 1);
		loot.put(new ItemStack(Material.IRON_LEGGINGS), 1);
		loot.put(new ItemStack(Material.IRON_BOOTS), 1);
		loot.put(new ItemStack(Material.DIAMOND), 1);
		loot.put(new ItemStack(Material.STONE_SWORD), 1);
		loot.put(new ItemStack(Material.GOLDEN_APPLE), random.nextInt(2) + 1);
		loot.put(new ItemStack(Material.IRON_INGOT), random.nextInt(3) + 1);
		loot.put(new ItemStack(Material.DIAMOND_AXE), 1);
		
		loot.put(new ItemStack(Material.DIAMOND_HELMET), 25);
		loot.put(new ItemStack(Material.DIAMOND_CHESTPLATE), 25);
		loot.put(new ItemStack(Material.DIAMOND_LEGGINGS), 25);
		loot.put(new ItemStack(Material.DIAMOND_BOOTS), 25);
		loot.put(new ItemStack(Material.DIAMOND_SWORD), 25);
		
		return loot;
	}
	
}
