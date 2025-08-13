package media.arc.item;

import media.arc.index.ArcSenalEffects;
import media.arc.index.ArcSenalItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ScytheItem extends ExtendedSwordItem {

    public ScytheItem(ToolMaterial mat) {
        super(mat, 5, -3f, new Item.Settings().group(ArcSenalItems.GROUP), 1.0, 1.0);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.getItemCooldownManager().set(this, 10); // 0.5 sec cooldown

            Entity target = raycastEntity(user, 5.5D);

            if (target instanceof LivingEntity living && target != user) {
                // Player's center position (accounts for height)
                Vec3d playerPos = user.getPos().add(0, user.getStandingEyeHeight() / 2, 0);
                // Target's center position
                Vec3d targetPos = living.getPos().add(0, living.getStandingEyeHeight() / 2, 0);

                // Direction vector from target to player
                Vec3d pullVec = playerPos.subtract(targetPos);

                // Optional: scale force based on distance (stronger if far)
                double distance = pullVec.length();
                double strength = Math.min(distance * 0.35, 1.5); // clamp max pull speed

                // Normalize and scale
                pullVec = pullVec.normalize().multiply(strength);

                // Apply velocity directly toward player's position
                living.addVelocity(pullVec.x, pullVec.y, pullVec.z);
                living.velocityModified = true;

                living.addStatusEffect(new StatusEffectInstance(ArcSenalEffects.STUN, 15, 0));
                living.damage(DamageSource.GENERIC,2.0f);

                user.spawnSweepAttackParticles();
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



    @Override
    public void inventoryTick(ItemStack stack, World world, LivingEntity entity, int slot, boolean selected) {

    }

    @Override
    public boolean hasGlint() {
        return false;
    }

    @Override
    public boolean canDisableShields() {
        return true;
    }
}
