package io.github.darealturtywurty.tutorialmod.core.world;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import io.github.darealturtywurty.tutorialmod.TutorialMod;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class OreGeneration {

    public static final List<ConfiguredFeature<?, ?>> OVERWORLD_ORES = new ArrayList<>();
    public static final List<ConfiguredFeature<?, ?>> END_ORES = new ArrayList<>();
    public static final List<ConfiguredFeature<?, ?>> NETHER_ORES = new ArrayList<>();

    public static final RuleTest END_TEST = new BlockMatchTest(Blocks.END_STONE);

    public static void registerOres() {
        ConfiguredFeature<?, ?> glowstoneOre = Feature.ORE
                .configured(new OreConfiguration(List.of(
                        OreConfiguration.target(OreConfiguration.Predicates.STONE_ORE_REPLACEABLES,
                                Blocks.GLOWSTONE.defaultBlockState()),
                        OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES,
                                Blocks.ACACIA_WOOD.defaultBlockState())),
                        11))
                .rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(20)).squared().count(100);
        OVERWORLD_ORES.add(register("glowstone_ore", glowstoneOre));

        ConfiguredFeature<?, ?> beansOre = Feature.ORE
                .configured(new OreConfiguration(List.of(OreConfiguration.target(
                        OreConfiguration.Predicates.NETHER_ORE_REPLACEABLES, Blocks.BLUE_WOOL.defaultBlockState())), 3))
                .rangeTriangle(VerticalAnchor.absolute(50), VerticalAnchor.absolute(120)).squared().count(100);
        NETHER_ORES.add(register("beans", beansOre));

        ConfiguredFeature<?, ?> eggOre = Feature.ORE
                .configured(new OreConfiguration(
                        List.of(OreConfiguration.target(END_TEST, Blocks.GOLD_BLOCK.defaultBlockState())), 15))
                .rangeUniform(VerticalAnchor.absolute(20), VerticalAnchor.absolute(60)).squared().count(100);
        END_ORES.add(register("egg", eggOre));
    }

    private static <Config extends FeatureConfiguration> ConfiguredFeature<Config, ?> register(String name,
            ConfiguredFeature<Config, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(TutorialMod.MODID, name),
                configuredFeature);
    }

    @Mod.EventBusSubscriber(modid = TutorialMod.MODID, bus = Bus.FORGE)
    public static class ForgeBusSubscriber {
        @SubscribeEvent
        public static void biomeLoading(BiomeLoadingEvent event) {
            List<Supplier<ConfiguredFeature<?, ?>>> features = event.getGeneration()
                    .getFeatures(Decoration.UNDERGROUND_ORES);

            switch(event.getCategory()) {
                case NETHER -> OreGeneration.NETHER_ORES.forEach(ore -> features.add(() -> ore));
                case THEEND -> OreGeneration.END_ORES.forEach(ore -> features.add(() -> ore));
                default -> OreGeneration.OVERWORLD_ORES.forEach(ore -> features.add(() -> ore));
            }
        }
    }
}
