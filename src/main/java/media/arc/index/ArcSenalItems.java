package media.arc.index;

import media.arc.ArcSenal;
import media.arc.item.*;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ArcSenalItems {

    public static final Item SCYTHE = register("scythe", new ScytheItem(ArcSenal.ARCSENAL));
    public static final Item SCYTHE_CHROME = register("scythe_chrome", new ChromeScytheItem(ArcSenal.ARCSENAL));
    public static final Item SCYTHE_SCISSOR = register("scythe_scissor", new ScissorScytheItem(ArcSenal.ARCSENAL));
    public static final Item HAMMER = register("hammer", new HammerItem(ArcSenal.ARCSENAL));
    public static final Item HAMMER_BASE = register("hammer_base", new BaseHammerItem(ArcSenal.ARCSENAL));

    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier("arcsenal", name), item);
    }

    public static final ItemGroup GROUP = FabricItemGroupBuilder
            .create(new Identifier("arcsenal", "arcsenal"))
            .icon(() -> new ItemStack(SCYTHE))
            .appendItems(stacks -> {
                stacks.add(new ItemStack(SCYTHE));
                stacks.add(new ItemStack(SCYTHE_CHROME));
                stacks.add(new ItemStack(SCYTHE_SCISSOR));
                stacks.add(new ItemStack(HAMMER));
                stacks.add(new ItemStack(HAMMER_BASE));
            })
            .build();

    public static void init(){

    }
}
