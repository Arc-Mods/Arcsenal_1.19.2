package media.arc.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import media.arc.index.ArcSenalEffects;
import media.arc.index.ArcSenalItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ChromeScytheItem extends ExtendedSwordItem {

    public ChromeScytheItem(ToolMaterial mat) {
        super(mat, 5, -3f, new Item.Settings().group(ArcSenalItems.GROUP), 1.0, 1.0);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.getItemCooldownManager().set(this, 20); // 1 sec cooldown

            Entity target = raycastEntity(user, 5.5D);

            if (target instanceof LivingEntity living && target != user) {
                Vec3d playerPos = user.getPos().add(0, user.getStandingEyeHeight() / 2, 0);
                Vec3d targetPos = living.getPos().add(0, living.getStandingEyeHeight() / 2, 0);

                Vec3d pullVec = playerPos.subtract(targetPos);
                double distance = pullVec.length();
                double strength = Math.min(distance * 3.35, 2.5);

                pullVec = pullVec.normalize().multiply(strength);

                living.addVelocity(pullVec.x, pullVec.y, pullVec.z);
                living.velocityModified = true;

                living.addStatusEffect(new StatusEffectInstance(ArcSenalEffects.STUN, 15, 0));
                living.damage(DamageSource.player(user), 2.0f);

                // Replace user.spawnSweepAttackParticles() with:
                ((ServerWorld) world).spawnParticles(
                        ParticleTypes.SWEEP_ATTACK,
                        living.getX(),
                        living.getY() + living.getHeight() * 0.5,
                        living.getZ(),
                        1,
                        0, 0, 0,
                        0.0
                );
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }


    private Entity raycastEntity(PlayerEntity player, double maxDistance) {
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d end = start.add(player.getRotationVec(1.0F).multiply(maxDistance));

        Box box = player.getBoundingBox().stretch(player.getRotationVec(1.0F).multiply(maxDistance)).expand(1.0D);

        EntityHitResult ehr = ProjectileUtil.getEntityCollision(
                player.world,
                player,
                start,
                end,
                box,
                entity -> !entity.isSpectator()
        );

        return ehr != null ? ehr.getEntity() : null;
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockState state = context.getWorld().getBlockState(context.getBlockPos());
        PlayerEntity user = context.getPlayer();
        if (user != null && user.isSneaking() && state.isOf(Blocks.ANVIL)) {
            ItemStack stack = user.getMainHandStack();
            if (stack.isOf(ArcSenalItems.SCYTHE_CHROME)) {
                stack.decrement(1);
                user.giveItemStack(ArcSenalItems.SCYTHE_SCISSOR.getDefaultStack());
                user.playSound(SoundEvents.BLOCK_ANVIL_USE, 0.8F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }
        if (user != null && user.isSneaking() && state.isOf(Blocks.SMITHING_TABLE)) {
            ItemStack stack = user.getMainHandStack();
            if (stack.isOf(ArcSenalItems.SCYTHE_CHROME)) {
                stack.decrement(1);
                user.giveItemStack(ArcSenalItems.SCYTHE_SCISSOR.getDefaultStack());
                user.playSound(SoundEvents.BLOCK_SMITHING_TABLE_USE, 0.8F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
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
                            "Scythe Additional range",
                            0.5,
                            EntityAttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }

        return super.getAttributeModifiers(slot);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Skin: Chrome").formatted(Formatting.GRAY));
    }
}