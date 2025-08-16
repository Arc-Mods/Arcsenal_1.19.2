package media.arc.item;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;

import java.util.UUID;

import static net.minecraft.entity.attribute.EntityAttributeModifier.Operation.ADDITION;

public abstract class ExtendedSwordItem extends SwordItem {
    private final double reachBonus, attackRangeBonus;
    private static final UUID REACH_MODIFIER_UUID = UUID.fromString("fe31c179-268b-4562-9050-123456789abc");
    private static final UUID ATTACK_RANGE_MODIFIER_UUID = UUID.fromString("0e31a44e-8b1c-4524-935a-def987654321");

    public ExtendedSwordItem(ToolMaterial mat, int dmg, float speed,
                             Settings settings, double reachBonus, double attackRangeBonus) {
        super(mat, dmg, speed, settings);
        this.reachBonus = reachBonus;
        this.attackRangeBonus = attackRangeBonus;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof LivingEntity e) || world.isClient) return;

        var reachAttr = e.getAttributeInstance(ReachEntityAttributes.REACH);
        var rangeAttr = e.getAttributeInstance(ReachEntityAttributes.ATTACK_RANGE);

        if (selected) {
            if (reachAttr != null && reachAttr.getModifier(REACH_MODIFIER_UUID) == null) {
                reachAttr.addTemporaryModifier(new EntityAttributeModifier(
                        REACH_MODIFIER_UUID, "ExtendedSword reach", reachBonus, ADDITION));
            }
            if (rangeAttr != null && rangeAttr.getModifier(ATTACK_RANGE_MODIFIER_UUID) == null) {
                rangeAttr.addTemporaryModifier(new EntityAttributeModifier(
                        ATTACK_RANGE_MODIFIER_UUID, "ExtendedSword attack range", attackRangeBonus, ADDITION));
            }
        } else {
            // remove if no longer holding it
            if (reachAttr != null && reachAttr.getModifier(REACH_MODIFIER_UUID) != null) {
                reachAttr.removeModifier(REACH_MODIFIER_UUID);
            }
            if (rangeAttr != null && rangeAttr.getModifier(ATTACK_RANGE_MODIFIER_UUID) != null) {
                rangeAttr.removeModifier(ATTACK_RANGE_MODIFIER_UUID);
            }
        }
    }

    public abstract void inventoryTick(ItemStack stack, World world, LivingEntity entity, int slot, boolean selected);

    public abstract boolean hasGlint(ItemStack stack);

    public abstract boolean canDisableShield(ItemStack stack, LivingEntity entity, LivingEntity attacker);
}
