package media.arc.index;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;

public class ArcDamageSources {

    public static final DamageSource LIBERATION = (new DamageSource("liberation") {
        public Text getDeathMessage(LivingEntity entity) {
            return Text.literal(entity.getName().getString() + " was liberated");
        }
    }).setBypassesArmor();
}
