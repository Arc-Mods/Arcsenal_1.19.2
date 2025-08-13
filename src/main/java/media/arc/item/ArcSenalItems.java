package media.arc.item;

import media.arc.ArcSenal;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ArcSenalItems {

    public static final Item SCYTHE = register("scythe", new ScytheItem(ArcSenal.ARCSENAL));

    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier("arcsenal", name), item);
    }

    public static final ItemGroup GROUP = FabricItemGroupBuilder
            .create(new Identifier("arcsenal", "arcsenal"))
            .icon(() -> new ItemStack(SCYTHE))
            .appendItems(stacks -> {
                stacks.add(new ItemStack(SCYTHE));
            })
            .build();

    public static void init(){

    }
}
