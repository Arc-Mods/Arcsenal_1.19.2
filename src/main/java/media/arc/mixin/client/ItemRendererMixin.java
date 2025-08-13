package media.arc.mixin.client;

import media.arc.ArcSenal;
import media.arc.item.ArcSenalItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel arc$useCustomModel(BakedModel original,
                                           ItemStack stack,
                                           ModelTransformation.Mode renderMode,
                                           boolean leftHanded,
                                           MatrixStack matrices,
                                           VertexConsumerProvider vertexConsumers,
                                           int light,
                                           int overlay) {

        Identifier id = null;

        // Skip default item like air
        if (stack.isEmpty()) return original;

        if (stack.isOf(ArcSenalItems.SCYTHE)) id = new Identifier(ArcSenal.MOD_ID, "scythe");

        if (id != null && renderMode != ModelTransformation.Mode.FIXED) {
            String path = renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND
                    ? id.getPath() + "_gui"
                    : id.getPath();

            return ((ItemRendererAccessor) this).arc$getModels().getModelManager()
                    .getModel(new ModelIdentifier(id.getNamespace(), path, "inventory"));
        }

        return original;
    }
}