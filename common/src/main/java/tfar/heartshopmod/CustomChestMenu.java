package tfar.heartshopmod;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

public class CustomChestMenu extends ChestMenu {
    public CustomChestMenu(MenuType<?> menuType, int i, Inventory inventory, Container container, int j) {
        super(menuType, i, inventory, container, j);

        NonNullList<Slot> slotNonNullList = this.slots;
        for (int k = 0; k < slotNonNullList.size(); k++) {
            Slot slot = slotNonNullList.get(k);
            if (!(slot.container instanceof Inventory)) {
                slotNonNullList.set(k, new LockedSlot(slot.container, slot.x, slot.y, slot.index));
            }
        }
    }

    public static class LockedSlot extends Slot {

        public LockedSlot(Container container, int i, int j, int k) {
            super(container, i, j, k);
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }
}
