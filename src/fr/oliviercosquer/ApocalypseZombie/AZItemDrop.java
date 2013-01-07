/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.oliviercosquer.ApocalypseZombie;

import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Olivier
 */
public class AZItemDrop {
    
    private ItemStack item;
    private int dropRate;

    AZItemDrop(ItemStack item, int dropRate) {
        this.item = item;
        this.dropRate = dropRate;
    }
    
    public int getDropRate() {
        return dropRate;
    }

    public ItemStack getItem() {
        return item;
    }
    
    
}
