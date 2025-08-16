package media.arc.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import media.arc.index.ArcDamageSources;
import media.arc.index.ArcSenalItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.UUID;
import java.util.WeakHashMap;

public class SolitudeItem extends ExtendedSwordItem {
    public static final WeakHashMap<UUID, Long> markedPlayers = new WeakHashMap<>();

    public SolitudeItem(ToolMaterial mat) {
        super(mat, 7 - 2, -2.8f, new Item.Settings().group(ArcSenalItems.GROUP).fireproof(), 1.0, 1.0);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(target instanceof ServerPlayerEntity player)) return super.postHit(stack, target, attacker);

        // target is a player
        if (player.getHealth() <= 1.0f) {
            player.setHealth(5.0f); // restore health
            player.damage(ArcDamageSources.LIBERATION, 9999f); // Lets mc handle the death

            player.getServer().execute(() -> {
                // Waits a few ticks to make sure death is processed
                if (player.isDead()) {
                    player.requestRespawn();
                }

                ServerWorld world = (ServerWorld) player.getWorld();
                world.spawnParticles(ParticleTypes.END_ROD,
                        player.getX(), player.getY() + 1, player.getZ(),
                        50, 0.3, 0.5, 0.3, 0.35
                );
                world.spawnParticles(ParticleTypes.FLASH,
                        target.getX(), target.getY() + 1, target.getZ(),
                        20, 0.3, 0.5, 0.3, 0.15);
            });
        }

        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false; // Prevents enchanting via table
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot));

            builder.put(ReachEntityAttributes.ATTACK_RANGE,
                    new EntityAttributeModifier(
                            UUID.fromString("a67e3cc0-45d5-4e8e-9d64-7421e1b5fe3e"),
                            "Solitude attack range",
                            0.5,
                            EntityAttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }

        return super.getAttributeModifiers(slot);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, LivingEntity entity, int slot, boolean selected) {

    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canDisableShield(ItemStack stack, LivingEntity entity, LivingEntity attacker) {
        return true;
    }
}
