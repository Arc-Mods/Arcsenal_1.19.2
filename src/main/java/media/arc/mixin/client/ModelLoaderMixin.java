package media.arc.mixin.client;

import media.arc.ArcSenal;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {

    @Shadow
    protected abstract void addModel(ModelIdentifier modelId);

    @Inject(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/model/ModelLoader;addModel(Lnet/minecraft/client/util/ModelIdentifier;)V",
            ordinal = 3,
            shift = At.Shift.AFTER))
    public void arc$addCustomModels(ResourceManager resourceManager, BlockColors blockColors, Profiler profiler, int mipmapLevel, CallbackInfo ci) {
        String[] modelNames = {
                "scythe"

        };

        for (String name : modelNames) {
            this.addModel(new ModelIdentifier(ArcSenal.MOD_ID, name, "inventory"));       // 3D model (in-hand)
            this.addModel(new ModelIdentifier(ArcSenal.MOD_ID, name + "_gui", "inventory")); // GUI model
        }
    }
}

