package media.arc.item;

import media.arc.index.ArcSenalItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;

public class FollySwordItem extends ExtendedSwordItem {
    public FollySwordItem(ToolMaterial mat) {
        super(mat, 5, -3f, new Settings().group(ArcSenalItems.GROUP).fireproof(), 1.0, 1.0);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, LivingEntity entity, int slot, boolean selected) {

    }

    @Override
    public boolean hasGlint() {
        return false;
    }

    @Override
    public boolean canDisableShields() {
        return false;
    }
}
