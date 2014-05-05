/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.grzegorz2047.autostonemachine;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Grzegorz
 */
public class AutoStoneMachine extends JavaPlugin implements Listener{

    static Material mat;
    static Material breakmaterial;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        try {
            mat = Material.getMaterial(this.getConfig().getString("material").toUpperCase());
        catch(IllegalArgumentException ex){
            System.err.println("Niepoprawny material! Wylaczam!");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        try {
            breakmaterial = Material.getMaterial(this.getConfig().getString("breakmaterial").toUpperCase());
        catch(IllegalArgumentException ex){
            System.err.println("Niepoprawny breakmaterial! Wylaczam!");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        
        this.getServer().getPluginManager().registerEvents(this, this);
        System.out.println(this.getName()+" Dziala!"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDisable() {
        System.out.println(this.getName()+" zostal wylaczony!"); //To change body of generated methods, choose Tools | Templates.
    }
    
    @EventHandler
    void onBreak(BlockBreakEvent e){
        if(e.isCancelled()){
            return;
        }
        if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            return;
        }
        Location loc = e.getBlock().getLocation();
        int radius = 2;
        boolean hassponge=false;
        for(int x =loc.getBlockX()-radius;x<loc.getBlockX()+radius;x++){
            for(int y =loc.getBlockY()-radius;y<loc.getBlockY()+radius;y++){
                for(int z =loc.getBlockZ()-radius;z<loc.getBlockZ()+radius;z++){
                    Block b = loc.getWorld().getBlockAt(x, y, z);
                    if(b.getType().equals(mat)){
                        hassponge=true;
                    }
                }
            }
        }
        if(hassponge){
            if(!e.getPlayer().getItemInHand().getType().equals(breakmaterial)){
                if(e.getBlock().getType().equals(mat)){
                    e.setCancelled(true);
                    return;
                }
                e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), new ItemStack(Material.STONE,1));
                e.setCancelled(true);
            }else{
                
            }
        }
    }
    
    @EventHandler( priority = EventPriority.LOWEST )
    void onPlaceMachine(BlockPlaceEvent e){
        if(e.getBlock().getType().equals(mat)){
            if(e.canBuild()){
                if(!e.isCancelled()){
                    Location loc = e.getBlock().getLocation();
                    int radius = 2-1;
                    for(int x =loc.getBlockX()-radius;x<loc.getBlockX()+radius;x++){
                        for(int y =loc.getBlockY()-radius;y<loc.getBlockY()+radius;y++){
                            for(int z =loc.getBlockZ()-radius;z<loc.getBlockZ()+radius;z++){
                                Block b = loc.getWorld().getBlockAt(x, y, z);
                                if(!b.getType().equals(mat)){
                                    b.setType(Material.STONE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    
}
