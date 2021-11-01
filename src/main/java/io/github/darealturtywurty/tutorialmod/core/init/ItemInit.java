package io.github.darealturtywurty.tutorialmod.core.init;

import io.github.darealturtywurty.tutorialmod.TutorialMod;
import io.github.darealturtywurty.tutorialmod.common.item.ClickerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			TutorialMod.MODID);

	public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
			() -> new Item(new Item.Properties().tab(TutorialMod.TUTORIAL_TAB).fireResistant()));

	public static final RegistryObject<ClickerItem> CLICKER = ITEMS.register("clicker",
			() -> new ClickerItem(new Item.Properties().tab(TutorialMod.TUTORIAL_TAB)));

	public static final RegistryObject<ForgeSpawnEggItem> EXAMPLE_ENTITY_SPAWN_EGG = ITEMS
			.register("example_entity_spawn_egg", () -> new ForgeSpawnEggItem(EntityInit.EXAMPLE_ENTITY, 0x1E51ED,
					0x34BD27, new Item.Properties().tab(TutorialMod.TUTORIAL_TAB)));

	// Block Items
	public static final RegistryObject<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block",
			() -> new BlockItem(BlockInit.EXAMPLE_BLOCK.get(), new Item.Properties().tab(TutorialMod.TUTORIAL_TAB)));

	public static final RegistryObject<BlockItem> LIGHTNING_JUMPER_BLOCK_ITEM = ITEMS.register("lightning_jumper",
			() -> new BlockItem(BlockInit.LIGHTNING_JUMPER.get(), new Item.Properties().tab(TutorialMod.TUTORIAL_TAB)));

	private ItemInit() {
	}
}
