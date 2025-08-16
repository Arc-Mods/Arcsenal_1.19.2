package media.arc.item;

import media.arc.index.ArcSenalItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
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

public class BaseHammerItem extends ExtendedSwordItem{

    public BaseHammerItem(ToolMaterial mat){
        super(mat,4,-2.9f, new Item.Settings().group(ArcSenalItems.GROUP), 1.0, 1.0);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, LivingEntity entity, int slot, boolean selected) {
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.getItemCooldownManager().set(this, 40); // 2 sec cooldown

            Entity target = raycastEntity(user, 3.5D);

            if (target instanceof LivingEntity living && target != user) {
                living.damage(DamageSource.player(user), 4.0f);

                Vec3d look = user.getRotationVec(1.0F).normalize();
                living.takeKnockback(1.5, -look.x, -look.z);

                // particle
                ((ServerWorld) world).spawnParticles(
                        ParticleTypes.CRIT,
                        living.getX(),
                        living.getY() + living.getHeight() * 0.5,
                        living.getZ(),
                        8,
                        0.2, 0.2, 0.2,
                        0.1
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
            if (stack.isOf(ArcSenalItems.HAMMER_BASE)) {
                stack.decrement(1);
                user.giveItemStack(ArcSenalItems.HAMMER.getDefaultStack());
                user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 0.8F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }
        if (user != null && user.isSneaking() && state.isOf(Blocks.SMITHING_TABLE)) {
            ItemStack stack = user.getMainHandStack();
            if (stack.isOf(ArcSenalItems.HAMMER_BASE)) {
                stack.decrement(1);
                user.giveItemStack(ArcSenalItems.HAMMER.getDefaultStack());
                user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 0.8F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }


    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canDisableShield(ItemStack stack, LivingEntity entity, LivingEntity attacker) {
        return false;
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Skin: Industrial").formatted(Formatting.GRAY));
    }
}
