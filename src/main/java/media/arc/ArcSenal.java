package media.arc;

import media.arc.index.ArcSenalEffects;
import media.arc.index.ArcSenalItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArcSenal implements ModInitializer {
	public static final String MOD_ID = "arcsenal";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ToolMaterial ARCSENAL = ToolMaterials.NETHERITE;

	@Override
	public void onInitialize() {
		ArcSenalItems.init();
		ArcSenalEffects.init();

		LOGGER.info("Arcsenal Loaded!");
	}
}