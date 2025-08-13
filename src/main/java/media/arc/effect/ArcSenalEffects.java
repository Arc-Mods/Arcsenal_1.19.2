package media.arc.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ArcSenalEffects {
    public static final StatusEffect STUN = new StunEffect();

    public static void init(){
        Registry.register(Registry.STATUS_EFFECT, new Identifier("arcsenal", "stun"), STUN);
    }
}
