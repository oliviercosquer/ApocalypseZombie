/*
Copyright 2013 - Olivier Cosquer - http://www.olivier-cosquer.com

 This file is part of ApocalypseZombie.

    ApocalypseZombie is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ApocalypseZombie is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ApocalypseZombie.  If not, see <http://www.gnu.org/licenses/>.
*/
package fr.oliviercosquer.ApocalypseZombie.Configuration;

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
