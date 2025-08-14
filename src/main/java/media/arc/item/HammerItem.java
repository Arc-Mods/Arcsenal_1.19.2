package media.arc.item;

import media.arc.index.ArcSenalItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HammerItem extends ExtendedSwordItem{

    public HammerItem(ToolMaterial mat){
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

                living.damage(DamageSource.GENERIC,4.0f);

                Vec3d look = user.getRotationVec(1.0F).normalize();
                living.takeKnockback(1.5, -look.x, -look.z);

                user.addCritParticles(living);
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
    public boolean hasGlint() {
        return false;
    }

    @Override
    public boolean canDisableShields() {
        return true;
    }
}
