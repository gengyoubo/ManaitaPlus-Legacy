package github.com.gengyoubo.MPG.transform;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.EnumSet;

public class MPLaunchPluginService implements ILaunchPluginService {
    public static final Logger LOGGER = LogManager.getLogger("ManaitaPlusCore");

    private static final String JEI_STARTER_OWNER = "mezz/jei/library/startup/JeiStarter";
    private static final String JEI_VANILLA_PLUGIN_OWNER = "mezz/jei/library/plugins/vanilla/VanillaPlugin";
    private static final String JEI_VANILLA_RECIPES_OWNER = "mezz/jei/library/plugins/vanilla/crafting/VanillaRecipes";
    private static final String JEI_ITEM_STACK_LIST_FACTORY_OWNER = "mezz/jei/library/plugins/vanilla/ingredients/ItemStackListFactory";
    private static final String EVENT_UTIL_OWNER = "sen/manaita_plus_general/util/EventUtil";
    private static final String I18N_OWNER = "net/minecraft/client/resources/language/I18n";
    private static final String CHAT_FORMATTING_OWNER = "net/minecraft/ChatFormatting";
    private static final String STYLE_OWNER = "net/minecraft/network/chat/Style";
    private static final String COMMON_COMPONENTS_OWNER = "net/minecraft/network/chat/CommonComponents";
    private static final String CLIENT_LEVEL_OWNER = "net/minecraft/client/multiplayer/ClientLevel";
    private static final String STYLED_CONTENT_CONSUMER_OWNER = "net/minecraft/network/chat/FormattedText$StyledContentConsumer";
    private static final String RESOURCE_RELOAD_LISTENER_OWNER = "net/minecraft/server/packs/resources/ResourceManagerReloadListener";
    private static final String RESOURCE_LOCATION_OWNER = "net/minecraft/resources/ResourceLocation";
    private static final String RESOURCE_KEY_OWNER = "net/minecraft/resources/ResourceKey";
    private static final String HOLDER_OWNER = "net/minecraft/core/Holder";
    private static final String NON_NULL_LIST_OWNER = "net/minecraft/core/NonNullList";
    private static final String DEFAULTED_REGISTRY_OWNER = "net/minecraft/core/DefaultedRegistry";
    private static final String REGISTRY_OWNER = "net/minecraft/core/Registry";
    private static final String HOLDER_REFERENCE_OWNER = "net/minecraft/core/Holder$Reference";
    private static final String BLOCK_ITEM_OWNER = "net/minecraft/world/item/BlockItem";
    private static final String BANNER_ITEM_OWNER = "net/minecraft/world/item/BannerItem";
    private static final String ARMOR_MATERIALS_OWNER = "net/minecraft/world/item/ArmorMaterials";
    private static final String DYE_ITEM_OWNER = "net/minecraft/world/item/DyeItem";
    private static final String DYE_COLOR_OWNER = "net/minecraft/world/item/DyeColor";
    private static final String TIERS_OWNER = "net/minecraft/world/item/Tiers";
    private static final String ITEMS_OWNER = "net/minecraft/world/item/Items";
    private static final String BLOCKS_OWNER = "net/minecraft/world/level/block/Blocks";
    private static final String BLOCK_OWNER = "net/minecraft/world/level/block/Block";
    private static final String BLOCK_STATE_BASE_OWNER = "net/minecraft/world/level/block/state/BlockBehaviour$BlockStateBase";
    private static final String FLOWER_BLOCK_OWNER = "net/minecraft/world/level/block/FlowerBlock";
    private static final String SHULKER_BOX_BLOCK_OWNER = "net/minecraft/world/level/block/ShulkerBoxBlock";
    private static final String FLUIDS_OWNER = "net/minecraft/world/level/material/Fluids";
    private static final String FLUID_OWNER = "net/minecraft/world/level/material/Fluid";
    private static final String FLUID_STATE_OWNER = "net/minecraft/world/level/material/FluidState";
    private static final String ABSTRACT_CONTAINER_MENU_OWNER = "net/minecraft/world/inventory/AbstractContainerMenu";
    private static final String MENU_TYPE_OWNER = "net/minecraft/world/inventory/MenuType";
    private static final String REGISTRIES_OWNER = "net/minecraft/core/registries/Registries";
    private static final String BUILT_IN_REGISTRIES_OWNER = "net/minecraft/core/registries/BuiltInRegistries";
    private static final String LEVEL_STORAGE_ACCESS_OWNER = "net/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess";
    private static final String ITEM_TAGS_OWNER = "net/minecraft/tags/ItemTags";
    private static final String BLOCK_ENTITY_TYPE_OWNER = "net/minecraft/world/level/block/entity/BlockEntityType";
    private static final String RECIPE_MANAGER_OWNER = "net/minecraft/world/item/crafting/RecipeManager";
    private static final String RECIPE_INTERFACE_OWNER = "net/minecraft/world/item/crafting/Recipe";
    private static final String RECIPE_OWNER = "net/minecraft/world/item/crafting/CraftingRecipe";
    private static final String CRAFTING_RECIPE_PACKAGE_PREFIX = "net/minecraft/world/item/crafting/";
    private static final String RECIPE_TYPE_OWNER = "net/minecraft/world/item/crafting/RecipeType";
    private static final String INGREDIENT_OWNER = "net/minecraft/world/item/crafting/Ingredient";
    private static final String MINECRAFT_OWNER = "net/minecraft/client/Minecraft";
    private static final String CONNECTION_OWNER = "net/minecraft/network/Connection";
    private static final String COMPOUND_TAG_OWNER = "net/minecraft/nbt/CompoundTag";
    private static final String LIST_TAG_OWNER = "net/minecraft/nbt/ListTag";
    private static final String ENCHANTED_BOOK_ITEM_OWNER = "net/minecraft/world/item/EnchantedBookItem";
    private static final String ENCHANTMENT_OWNER = "net/minecraft/world/item/enchantment/Enchantment";
    private static final String ENCHANTMENT_HELPER_OWNER = "net/minecraft/world/item/enchantment/EnchantmentHelper";
    private static final String POTION_OWNER = "net/minecraft/world/item/alchemy/Potion";
    private static final String POTION_BREWING_OWNER = "net/minecraft/world/item/alchemy/PotionBrewing";
    private static final String POTIONS_OWNER = "net/minecraft/world/item/alchemy/Potions";
    private static final String POTION_UTILS_OWNER = "net/minecraft/world/item/alchemy/PotionUtils";
    private static final String TOOLTIP_FLAG_OWNER = "net/minecraft/world/item/TooltipFlag";
    private static final String TOOLTIP_FLAG_DEFAULT_OWNER = "net/minecraft/world/item/TooltipFlag$Default";
    private static final String SUSPICIOUS_STEW_ITEM_OWNER = "net/minecraft/world/item/SuspiciousStewItem";
    private static final String COMPONENT_OWNER = "net/minecraft/network/chat/Component";
    private static final String MUTABLE_COMPONENT_OWNER = "net/minecraft/network/chat/MutableComponent";
    private static final String FONT_OWNER = "net/minecraft/client/gui/Font";
    private static final String STRING_SPLITTER_OWNER = "net/minecraft/client/StringSplitter";
    private static final String EDIT_BOX_OWNER = "net/minecraft/client/gui/components/EditBox";
    private static final String TAG_KEY_OWNER = "net/minecraft/tags/TagKey";
    private static final String GUI_GRAPHICS_OWNER = "net/minecraft/client/gui/GuiGraphics";
    private static final String CRASH_REPORT_OWNER = "net/minecraft/CrashReport";
    private static final String CRASH_REPORT_CATEGORY_OWNER = "net/minecraft/CrashReportCategory";
    private static final String ENTITY_OWNER = "net/minecraft/world/entity/Entity";
    private static final String LIVING_ENTITY_OWNER = "net/minecraft/world/entity/LivingEntity";
    private static final String ENTITY_DESC = "(Lnet/minecraft/world/entity/Entity;)Z";
    private static final String LIVING_ENTITY_DESC = "(Lnet/minecraft/world/entity/LivingEntity;)Z";

    @Override
    public String name() {
        return "MPLaunchPluginService";
    }

    @Override
    public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
        return EnumSet.of(Phase.AFTER);
    }

    @Override
    public int processClassWithFlags(Phase phase, ClassNode classNode, Type classType, String reason) {
        return ILaunchPluginService.super.processClassWithFlags(phase, classNode, classType, reason);
    }

    @Override
    public boolean processClass(Phase phase, ClassNode classNode, Type classType) {
        if (classNode.name.startsWith("sen/")) {
            return false;
        }
        boolean patched = false;
        if (classNode.name.startsWith("mezz/jei/")) {
            patched |= processGenericJeiCompatibility(classNode);
        }
        patched |= switch (classNode.name) {
            case "net/minecraft/client/Minecraft" -> processMinecraftClass(classNode);
            case "net/minecraft/client/resources/language/LanguageManager" -> processLanguageManagerClass(classNode);
            case CLIENT_LEVEL_OWNER -> processClientLevelClass(classNode);
            case "net/minecraft/client/multiplayer/ClientPacketListener" -> processClientPacketListenerClass(classNode);
            case CONNECTION_OWNER -> processConnectionClass(classNode);
            case "net/minecraft/resources/ResourceKey" -> processResourceKeyClass(classNode);
            case RESOURCE_LOCATION_OWNER -> processResourceLocationClass(classNode);
            case HOLDER_OWNER -> processHolderClass(classNode);
            case NON_NULL_LIST_OWNER -> processNonNullListClass(classNode);
            case DEFAULTED_REGISTRY_OWNER -> processDefaultedRegistryClass(classNode);
            case REGISTRY_OWNER -> processRegistryClass(classNode);
            case LEVEL_STORAGE_ACCESS_OWNER -> processLevelStorageAccessClass(classNode);
            case HOLDER_REFERENCE_OWNER -> processHolderReferenceClass(classNode);
            case BLOCK_ITEM_OWNER -> processBlockItemClass(classNode);
            case ARMOR_MATERIALS_OWNER -> processArmorMaterialsClass(classNode);
            case INGREDIENT_OWNER -> processIngredientClass(classNode);
            case "net/minecraft/world/item/ItemStack" -> processItemStackClass(classNode);
            case ABSTRACT_CONTAINER_MENU_OWNER -> processAbstractContainerMenuClass(classNode);
            case BLOCK_OWNER -> processBlockClass(classNode);
            case BLOCK_STATE_BASE_OWNER -> processBlockStateBaseClass(classNode);
            case COMPOUND_TAG_OWNER -> processCompoundTagClass(classNode);
            case LIST_TAG_OWNER -> processListTagClass(classNode);
            case ENCHANTED_BOOK_ITEM_OWNER -> processEnchantedBookItemClass(classNode);
            case ENCHANTMENT_OWNER -> processEnchantmentClass(classNode);
            case ENCHANTMENT_HELPER_OWNER -> processEnchantmentHelperClass(classNode);
            case POTION_OWNER -> processPotionClass(classNode);
            case POTION_BREWING_OWNER -> processPotionBrewingClass(classNode);
            case POTION_UTILS_OWNER -> processPotionUtilsClass(classNode);
            case RECIPE_INTERFACE_OWNER -> processRecipeInterfaceClass(classNode);
            case RECIPE_MANAGER_OWNER -> processRecipeManagerClass(classNode);
            case TOOLTIP_FLAG_OWNER -> processTooltipFlagClass(classNode);
            case TOOLTIP_FLAG_DEFAULT_OWNER -> processTooltipFlagDefaultClass(classNode);
            case SUSPICIOUS_STEW_ITEM_OWNER -> processSuspiciousStewItemClass(classNode);
            case "net/minecraft/world/entity/player/Player" -> processPlayerClass(classNode);
            case FLOWER_BLOCK_OWNER -> processFlowerBlockClass(classNode);
            case FLUID_OWNER -> processFluidClass(classNode);
            case FLUID_STATE_OWNER -> processFluidStateClass(classNode);
            case I18N_OWNER -> processI18nClass(classNode);
            case CHAT_FORMATTING_OWNER -> processChatFormattingClass(classNode);
            case STYLE_OWNER -> processStyleClass(classNode);
            case COMPONENT_OWNER -> processComponentClass(classNode);
            case STYLED_CONTENT_CONSUMER_OWNER -> processStyledContentConsumerInterface(classNode);
            case MUTABLE_COMPONENT_OWNER -> processMutableComponentClass(classNode);
            case FONT_OWNER -> processFontClass(classNode);
            case STRING_SPLITTER_OWNER -> processStringSplitterClass(classNode);
            case EDIT_BOX_OWNER -> processEditBoxClass(classNode);
            case TAG_KEY_OWNER -> processTagKeyClass(classNode);
            case GUI_GRAPHICS_OWNER -> processGuiGraphicsClass(classNode);
            case CRASH_REPORT_OWNER -> processCrashReportClass(classNode);
            case CRASH_REPORT_CATEGORY_OWNER -> processCrashReportCategoryClass(classNode);
            case RESOURCE_RELOAD_LISTENER_OWNER -> processResourceReloadListenerInterface(classNode);
            case JEI_STARTER_OWNER -> processJeiStarterClass(classNode);
            case JEI_VANILLA_PLUGIN_OWNER -> processJeiVanillaPluginClass(classNode);
            case JEI_VANILLA_RECIPES_OWNER -> processJeiVanillaRecipesClass(classNode);
            case JEI_ITEM_STACK_LIST_FACTORY_OWNER -> processJeiItemStackListFactoryClass(classNode);
            case "net/minecraft/world/item/ItemCooldowns" -> processItemCooldownClass(classNode);
            case "net/minecraft/world/entity/Entity" -> processEntityClass(classNode);
            case "net/minecraft/world/entity/LivingEntity" -> processLivingEntityClass(classNode);
            default -> false;
        };
        return patched;
    }

    private static boolean processGenericJeiCompatibility(ClassNode classNode) {
        boolean patched = false;
        for (MethodNode method : classNode.methods) {
            for (org.objectweb.asm.tree.AbstractInsnNode node = method.instructions.getFirst(); node != null; node = node.getNext()) {
                if (node instanceof FieldInsnNode fieldInsn) {
                    patched |= rewriteJeiVanillaField(fieldInsn);
                    patched |= rewriteJeiItemStackListFactoryField(fieldInsn);
                    patched |= rewriteGenericJeiField(fieldInsn);
                } else if (node instanceof MethodInsnNode methodInsn) {
                    patched |= rewriteJeiVanillaMethod(methodInsn);
                    patched |= rewriteJeiItemStackListFactoryMethod(methodInsn);
                    patched |= rewriteGenericJeiMethod(methodInsn);
                } else if (node instanceof InvokeDynamicInsnNode invokeDynamicInsn) {
                    patched |= rewriteGenericJeiInvokeDynamic(invokeDynamicInsn);
                }
            }
        }
        return patched;
    }

    private static boolean processMinecraftClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureStaticBridge(
                classNode,
                "m_91087_",
                "()Lnet/minecraft/client/Minecraft;",
                new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/Minecraft", "getInstance", "()Lnet/minecraft/client/Minecraft;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_91097_",
                "()Lnet/minecraft/client/renderer/texture/TextureManager;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getTextureManager", "()Lnet/minecraft/client/renderer/texture/TextureManager;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_91102_",
                "()Lnet/minecraft/client/resources/language/LanguageManager;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "getLanguageManager", "()Lnet/minecraft/client/resources/language/LanguageManager;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_91403_",
                "()Lnet/minecraft/client/multiplayer/ClientPacketListener;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MINECRAFT_OWNER, "getConnection", "()Lnet/minecraft/client/multiplayer/ClientPacketListener;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_91296_",
                "()F",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MINECRAFT_OWNER, "getFrameTime", "()F", false),
                new InsnNode(Opcodes.FRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_91092_",
                "()Lnet/minecraft/client/server/IntegratedServer;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MINECRAFT_OWNER, "getSingleplayerServer", "()Lnet/minecraft/client/server/IntegratedServer;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("m_91152_") && method.desc.equals("(Lnet/minecraft/client/gui/screens/Screen;)V")) {
                method.instructions.insert(buildMinecraftScreenGuard());
                patched = true;
            }
        }
        return patched;
    }

    private static boolean processClientLevelClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_7465_",
                "()Lnet/minecraft/world/item/crafting/RecipeManager;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CLIENT_LEVEL_OWNER, "getRecipeManager", "()Lnet/minecraft/world/item/crafting/RecipeManager;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_9598_",
                "()Lnet/minecraft/core/RegistryAccess;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CLIENT_LEVEL_OWNER, "registryAccess", "()Lnet/minecraft/core/RegistryAccess;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processLanguageManagerClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_264236_",
                "()Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/resources/language/LanguageManager", "getSelected", "()Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processClientPacketListenerClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_247016_",
                "()Lnet/minecraft/world/flag/FeatureFlagSet;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/multiplayer/ClientPacketListener", "enabledFeatures", "()Lnet/minecraft/world/flag/FeatureFlagSet;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_104910_",
                "()Lnet/minecraft/network/Connection;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/multiplayer/ClientPacketListener", "getConnection", "()Lnet/minecraft/network/Connection;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processConnectionClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_129531_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CONNECTION_OWNER, "isMemoryConnection", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
    }

    private static boolean processPlayerClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_36337_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/entity/player/Player", "canUseGameMasterBlocks", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
    }

    private static boolean processResourceKeyClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_135782_",
                "()Lnet/minecraft/resources/ResourceLocation;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/resources/ResourceKey", "location", "()Lnet/minecraft/resources/ResourceLocation;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_135785_",
                "(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/resources/ResourceKey;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKESTATIC, RESOURCE_KEY_OWNER, "create", "(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/resources/ResourceKey;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processResourceLocationClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureStaticBridge(
                classNode,
                "m_135820_",
                "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, RESOURCE_LOCATION_OWNER, "tryParse", "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_135827_",
                "()Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RESOURCE_LOCATION_OWNER, "getNamespace", "()Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_135815_",
                "()Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RESOURCE_LOCATION_OWNER, "getPath", "()Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_135828_",
                "(C)Z",
                new VarInsnNode(Opcodes.ILOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, RESOURCE_LOCATION_OWNER, "validPathChar", "(C)Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        return patched;
    }

    private static boolean processStyleClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_131179_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, STYLE_OWNER, "isEmpty", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
    }

    private static boolean processHolderClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_203334_",
                "()Ljava/lang/Object;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEINTERFACE, HOLDER_OWNER, "value", "()Ljava/lang/Object;", true),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_203616_",
                "()Ljava/util/stream/Stream;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEINTERFACE, HOLDER_OWNER, "tags", "()Ljava/util/stream/Stream;", true),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processNonNullListClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureStaticBridge(
                classNode,
                "m_122779_",
                "()Lnet/minecraft/core/NonNullList;",
                new MethodInsnNode(Opcodes.INVOKESTATIC, NON_NULL_LIST_OWNER, "create", "()Lnet/minecraft/core/NonNullList;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_122783_",
                "(Ljava/lang/Object;[Ljava/lang/Object;)Lnet/minecraft/core/NonNullList;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKESTATIC, NON_NULL_LIST_OWNER, "of", "(Ljava/lang/Object;[Ljava/lang/Object;)Lnet/minecraft/core/NonNullList;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processRegistryClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_203636_",
                "(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEINTERFACE, REGISTRY_OWNER, "getHolder", "(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;", true),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processHolderReferenceClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_205785_",
                "()Lnet/minecraft/resources/ResourceKey;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, HOLDER_REFERENCE_OWNER, "key", "()Lnet/minecraft/resources/ResourceKey;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_203656_",
                "(Lnet/minecraft/tags/TagKey;)Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, HOLDER_REFERENCE_OWNER, "is", "(Lnet/minecraft/tags/TagKey;)Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        return patched;
    }

    private static boolean processItemStackClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_41619_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "isEmpty", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41720_",
                "()Lnet/minecraft/world/item/Item;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "getItem", "()Lnet/minecraft/world/item/Item;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41783_",
                "()Lnet/minecraft/nbt/CompoundTag;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "getTag", "()Lnet/minecraft/nbt/CompoundTag;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41698_",
                "(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "getOrCreateTagElement", "(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41737_",
                "(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "getTagElement", "(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41782_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "hasTag", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41613_",
                "()I",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "getCount", "()I", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41776_",
                "()I",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "getCount", "()I", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41764_",
                "(I)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ILOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "setCount", "(I)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41721_",
                "(I)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ILOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "setCount", "(I)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41777_",
                "()Lnet/minecraft/world/item/ItemStack;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "copy", "()Lnet/minecraft/world/item/ItemStack;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41792_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "isEnchanted", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_204131_",
                "()Ljava/util/stream/Stream;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "getTags", "()Ljava/util/stream/Stream;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_204117_",
                "(Lnet/minecraft/tags/TagKey;)Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "is", "(Lnet/minecraft/tags/TagKey;)Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41786_",
                "()Lnet/minecraft/network/chat/Component;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "getDisplayName", "()Lnet/minecraft/network/chat/Component;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_41651_",
                "(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ALOAD, 2),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", "getTooltipLines", "(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processBlockItemClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_40614_",
                "()Lnet/minecraft/world/level/block/Block;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BLOCK_ITEM_OWNER, "getBlock", "()Lnet/minecraft/world/level/block/Block;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processAbstractContainerMenuClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_6772_",
                "()Lnet/minecraft/world/inventory/MenuType;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ABSTRACT_CONTAINER_MENU_OWNER, "getType", "()Lnet/minecraft/world/inventory/MenuType;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processDefaultedRegistryClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_7854_",
                "(Ljava/lang/Object;)Ljava/util/Optional;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEINTERFACE, DEFAULTED_REGISTRY_OWNER, "getResourceKey", "(Ljava/lang/Object;)Ljava/util/Optional;", true),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processLevelStorageAccessClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_78277_",
                "()Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, LEVEL_STORAGE_ACCESS_OWNER, "getLevelId", "()Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processArmorMaterialsClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_6230_",
                "()Lnet/minecraft/world/item/crafting/Ingredient;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ARMOR_MATERIALS_OWNER, "getRepairIngredient", "()Lnet/minecraft/world/item/crafting/Ingredient;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processCompoundTagClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_128425_",
                "(Ljava/lang/String;I)Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, COMPOUND_TAG_OWNER, "contains", "(Ljava/lang/String;I)Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_128461_",
                "(Ljava/lang/String;)Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, COMPOUND_TAG_OWNER, "getString", "(Ljava/lang/String;)Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_128445_",
                "(Ljava/lang/String;)B",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, COMPOUND_TAG_OWNER, "getByte", "(Ljava/lang/String;)B", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_128437_",
                "(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, COMPOUND_TAG_OWNER, "getList", "(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_128448_",
                "(Ljava/lang/String;)S",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, COMPOUND_TAG_OWNER, "getShort", "(Ljava/lang/String;)S", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_128451_",
                "(Ljava/lang/String;)I",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, COMPOUND_TAG_OWNER, "getInt", "(Ljava/lang/String;)I", false),
                new InsnNode(Opcodes.IRETURN)
        );
        return patched;
    }

    private static boolean processListTagClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_128728_",
                "(I)Lnet/minecraft/nbt/CompoundTag;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ILOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, LIST_TAG_OWNER, "getCompound", "(I)Lnet/minecraft/nbt/CompoundTag;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processEnchantedBookItemClass(ClassNode classNode) {
        return ensureStaticBridge(
                classNode,
                "m_41163_",
                "(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/nbt/ListTag;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, ENCHANTED_BOOK_ITEM_OWNER, "getEnchantments", "(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/nbt/ListTag;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processEnchantmentClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_44704_",
                "()Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENCHANTMENT_OWNER, "getDescriptionId", "()Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_6586_",
                "()I",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENCHANTMENT_OWNER, "getMaxLevel", "()I", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_6081_",
                "(Lnet/minecraft/world/item/ItemStack;)Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ENCHANTMENT_OWNER, "canEnchant", "(Lnet/minecraft/world/item/ItemStack;)Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        return patched;
    }

    private static boolean processEnchantmentHelperClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureStaticBridge(
                classNode,
                "m_44831_",
                "(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Map;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, ENCHANTMENT_HELPER_OWNER, "getEnchantments", "(Lnet/minecraft/world/item/ItemStack;)Ljava/util/Map;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_44865_",
                "(Ljava/util/Map;Lnet/minecraft/world/item/ItemStack;)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKESTATIC, ENCHANTMENT_HELPER_OWNER, "setEnchantments", "(Ljava/util/Map;Lnet/minecraft/world/item/ItemStack;)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        return patched;
    }

    private static boolean processPotionClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_43492_",
                "(Ljava/lang/String;)Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, POTION_OWNER, "getName", "(Ljava/lang/String;)Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processPotionBrewingClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureStaticBridge(
                classNode,
                "m_43506_",
                "(Lnet/minecraft/world/item/ItemStack;)Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, POTION_BREWING_OWNER, "isIngredient", "(Lnet/minecraft/world/item/ItemStack;)Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_43529_",
                "(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKESTATIC, POTION_BREWING_OWNER, "mix", "(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processPotionUtilsClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureStaticBridge(
                classNode,
                "m_43579_",
                "(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/alchemy/Potion;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, POTION_UTILS_OWNER, "getPotion", "(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/alchemy/Potion;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_43547_",
                "(Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, POTION_UTILS_OWNER, "getMobEffects", "(Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_43549_",
                "(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/world/item/ItemStack;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKESTATIC, POTION_UTILS_OWNER, "setPotion", "(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/alchemy/Potion;)Lnet/minecraft/world/item/ItemStack;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processFlowerBlockClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_53521_",
                "()Lnet/minecraft/world/effect/MobEffect;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FLOWER_BLOCK_OWNER, "getSuspiciousEffect", "()Lnet/minecraft/world/effect/MobEffect;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_53522_",
                "()I",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FLOWER_BLOCK_OWNER, "getEffectDuration", "()I", false),
                new InsnNode(Opcodes.IRETURN)
        );
        return patched;
    }

    private static boolean processRecipeManagerClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_44013_",
                "(Lnet/minecraft/world/item/crafting/RecipeType;)Ljava/util/List;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RECIPE_MANAGER_OWNER, "getAllRecipesFor", "(Lnet/minecraft/world/item/crafting/RecipeType;)Ljava/util/List;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processIngredientClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_43908_",
                "()[Lnet/minecraft/world/item/ItemStack;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, INGREDIENT_OWNER, "getItems", "()[Lnet/minecraft/world/item/ItemStack;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_204132_",
                "(Lnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/crafting/Ingredient;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, INGREDIENT_OWNER, "of", "(Lnet/minecraft/tags/TagKey;)Lnet/minecraft/world/item/crafting/Ingredient;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processRecipeInterfaceClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_6423_",
                "()Lnet/minecraft/resources/ResourceLocation;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEINTERFACE, RECIPE_INTERFACE_OWNER, "getId", "()Lnet/minecraft/resources/ResourceLocation;", true),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processTooltipFlagClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_7050_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEINTERFACE, TOOLTIP_FLAG_OWNER, "isAdvanced", "()Z", true),
                new InsnNode(Opcodes.IRETURN)
        );
    }

    private static boolean processTooltipFlagDefaultClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_257777_",
                "()Lnet/minecraft/world/item/TooltipFlag$Default;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, TOOLTIP_FLAG_DEFAULT_OWNER, "asCreative", "()Lnet/minecraft/world/item/TooltipFlag$Default;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processSuspiciousStewItemClass(ClassNode classNode) {
        return ensureStaticBridge(
                classNode,
                "m_43258_",
                "(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/effect/MobEffect;I)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new MethodInsnNode(Opcodes.INVOKESTATIC, SUSPICIOUS_STEW_ITEM_OWNER, "saveMobEffect", "(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/effect/MobEffect;I)V", false),
                new InsnNode(Opcodes.RETURN)
        );
    }

    private static boolean processFluidClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_76145_",
                "()Lnet/minecraft/world/level/material/FluidState;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FLUID_OWNER, "defaultFluidState", "()Lnet/minecraft/world/level/material/FluidState;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_7444_",
                "(Lnet/minecraft/world/level/material/FluidState;)Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FLUID_OWNER, "isSource", "(Lnet/minecraft/world/level/material/FluidState;)Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_6212_",
                "(Lnet/minecraft/world/level/material/Fluid;)Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FLUID_OWNER, "isSame", "(Lnet/minecraft/world/level/material/Fluid;)Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_205067_",
                "(Lnet/minecraft/tags/TagKey;)Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FLUID_OWNER, "is", "(Lnet/minecraft/tags/TagKey;)Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        return patched;
    }

    private static boolean processFluidStateClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_76152_",
                "()Lnet/minecraft/world/level/material/Fluid;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FLUID_STATE_OWNER, "getType", "()Lnet/minecraft/world/level/material/Fluid;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processItemCooldownClass(ClassNode classNode) {
        boolean patched = false;
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("m_41519_") && method.desc.equals("(Lnet/minecraft/world/item/Item;)Z")) {
                method.instructions.insert(buildItemCooldownGuard());
                patched = true;
            }
        }
        return patched;
    }

    private static boolean processI18nClass(ClassNode classNode) {
        return ensureStaticBridge(
                classNode,
                "m_118938_",
                "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKESTATIC, I18N_OWNER, "get", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processChatFormattingClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureStaticBridge(
                classNode,
                "m_126657_",
                "(Ljava/lang/String;)Lnet/minecraft/ChatFormatting;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, CHAT_FORMATTING_OWNER, "getByName", "(Ljava/lang/String;)Lnet/minecraft/ChatFormatting;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_126649_",
                "(Ljava/lang/String;)Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, CHAT_FORMATTING_OWNER, "stripFormatting", "(Ljava/lang/String;)Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_126664_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CHAT_FORMATTING_OWNER, "isColor", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_126661_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CHAT_FORMATTING_OWNER, "isFormat", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_126666_",
                "()Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CHAT_FORMATTING_OWNER, "getName", "()Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processComponentClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureStaticBridge(
                classNode,
                "m_237113_",
                "(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, COMPONENT_OWNER, "literal", "(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", true),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_237115_",
                "(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKESTATIC, COMPONENT_OWNER, "translatable", "(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", true),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_237110_",
                "(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKESTATIC, COMPONENT_OWNER, "translatable", "(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;", true),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_7451_",
                "(Lnet/minecraft/network/chat/FormattedText$StyledContentConsumer;Lnet/minecraft/network/chat/Style;)Ljava/util/Optional;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ALOAD, 2),
                new MethodInsnNode(Opcodes.INVOKEINTERFACE, COMPONENT_OWNER, "visit", "(Lnet/minecraft/network/chat/FormattedText$StyledContentConsumer;Lnet/minecraft/network/chat/Style;)Ljava/util/Optional;", true),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processMutableComponentClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_130940_",
                "(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MUTABLE_COMPONENT_OWNER, "withStyle", "(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_6270_",
                "(Lnet/minecraft/network/chat/Style;)Lnet/minecraft/network/chat/MutableComponent;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MUTABLE_COMPONENT_OWNER, "withStyle", "(Lnet/minecraft/network/chat/Style;)Lnet/minecraft/network/chat/MutableComponent;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_7383_",
                "()Lnet/minecraft/network/chat/Style;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MUTABLE_COMPONENT_OWNER, "getStyle", "()Lnet/minecraft/network/chat/Style;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_7220_",
                "(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MUTABLE_COMPONENT_OWNER, "append", "(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processBlockClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_204297_",
                "()Lnet/minecraft/core/Holder$Reference;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BLOCK_OWNER, "builtInRegistryHolder", "()Lnet/minecraft/core/Holder$Reference;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_49966_",
                "()Lnet/minecraft/world/level/block/state/BlockState;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BLOCK_OWNER, "defaultBlockState", "()Lnet/minecraft/world/level/block/state/BlockState;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processBlockStateBaseClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_204343_",
                "()Ljava/util/stream/Stream;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, BLOCK_STATE_BASE_OWNER, "getTags", "()Ljava/util/stream/Stream;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processFontClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_92895_",
                "(Ljava/lang/String;)I",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FONT_OWNER, "width", "(Ljava/lang/String;)I", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_92865_",
                "()Lnet/minecraft/client/StringSplitter;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FONT_OWNER, "getSplitter", "()Lnet/minecraft/client/StringSplitter;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processStringSplitterClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "m_92414_",
                "(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/network/chat/Style;)Ljava/util/List;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new VarInsnNode(Opcodes.ALOAD, 3),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, STRING_SPLITTER_OWNER, "splitLines", "(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/network/chat/Style;)Ljava/util/List;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processEditBoxClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_94199_",
                "(I)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ILOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "setMaxLength", "(I)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_94182_",
                "(Z)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ILOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "setBordered", "(Z)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_94202_",
                "(I)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ILOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "setTextColor", "(I)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_94155_",
                "()Ljava/lang/String;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "getValue", "()Ljava/lang/String;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_94144_",
                "(Ljava/lang/String;)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "setValue", "(Ljava/lang/String;)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_93696_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "isFocused", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_94213_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "isVisible", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_93692_",
                "(Z)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ILOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "setFocused", "(Z)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_94151_",
                "(Ljava/util/function/Consumer;)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "setResponder", "(Ljava/util/function/Consumer;)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_94187_",
                "(Z)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ILOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "setEditable", "(Z)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_94191_",
                "(Z)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ILOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "setCanLoseFocus", "(Z)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_94205_",
                "(I)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ILOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "setTextColorUneditable", "(I)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_142518_",
                "()Z",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "canConsumeInput", "()Z", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_87963_",
                "(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new VarInsnNode(Opcodes.ILOAD, 3),
                new VarInsnNode(Opcodes.FLOAD, 4),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EDIT_BOX_OWNER, "renderWidget", "(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        return patched;
    }

    private static boolean processTagKeyClass(ClassNode classNode) {
        return ensureInstanceBridge(
                classNode,
                "f_203868_",
                "()Lnet/minecraft/resources/ResourceLocation;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, TAG_KEY_OWNER, "location", "()Lnet/minecraft/resources/ResourceLocation;", false),
                new InsnNode(Opcodes.ARETURN)
        );
    }

    private static boolean processGuiGraphicsClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_280203_",
                "(Lnet/minecraft/world/item/ItemStack;II)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new VarInsnNode(Opcodes.ILOAD, 3),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, GUI_GRAPHICS_OWNER, "renderItem", "(Lnet/minecraft/world/item/ItemStack;II)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_280370_",
                "(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ALOAD, 2),
                new VarInsnNode(Opcodes.ILOAD, 3),
                new VarInsnNode(Opcodes.ILOAD, 4),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, GUI_GRAPHICS_OWNER, "renderItemDecorations", "(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_280488_",
                "(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)I",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ALOAD, 2),
                new VarInsnNode(Opcodes.ILOAD, 3),
                new VarInsnNode(Opcodes.ILOAD, 4),
                new VarInsnNode(Opcodes.ILOAD, 5),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, GUI_GRAPHICS_OWNER, "drawString", "(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)I", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_285944_",
                "(Lnet/minecraft/client/renderer/RenderType;IIIII)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new VarInsnNode(Opcodes.ILOAD, 3),
                new VarInsnNode(Opcodes.ILOAD, 4),
                new VarInsnNode(Opcodes.ILOAD, 5),
                new VarInsnNode(Opcodes.ILOAD, 6),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, GUI_GRAPHICS_OWNER, "fill", "(Lnet/minecraft/client/renderer/RenderType;IIIII)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_285978_",
                "(Lnet/minecraft/client/renderer/RenderType;IIIIIII)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new VarInsnNode(Opcodes.ILOAD, 3),
                new VarInsnNode(Opcodes.ILOAD, 4),
                new VarInsnNode(Opcodes.ILOAD, 5),
                new VarInsnNode(Opcodes.ILOAD, 6),
                new VarInsnNode(Opcodes.ILOAD, 7),
                new VarInsnNode(Opcodes.ILOAD, 8),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, GUI_GRAPHICS_OWNER, "fillGradient", "(Lnet/minecraft/client/renderer/RenderType;IIIIIII)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_280614_",
                "(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ALOAD, 2),
                new VarInsnNode(Opcodes.ILOAD, 3),
                new VarInsnNode(Opcodes.ILOAD, 4),
                new VarInsnNode(Opcodes.ILOAD, 5),
                new VarInsnNode(Opcodes.ILOAD, 6),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, GUI_GRAPHICS_OWNER, "drawString", "(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I", false),
                new InsnNode(Opcodes.IRETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_280168_",
                "()Lcom/mojang/blaze3d/vertex/PoseStack;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, GUI_GRAPHICS_OWNER, "pose", "()Lcom/mojang/blaze3d/vertex/PoseStack;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processCrashReportClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_127514_",
                "(Ljava/lang/String;)Lnet/minecraft/CrashReportCategory;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CRASH_REPORT_OWNER, "addCategory", "(Ljava/lang/String;)Lnet/minecraft/CrashReportCategory;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_127516_",
                "(Ljava/lang/String;I)Lnet/minecraft/CrashReportCategory;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ILOAD, 2),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CRASH_REPORT_OWNER, "addCategory", "(Ljava/lang/String;I)Lnet/minecraft/CrashReportCategory;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureStaticBridge(
                classNode,
                "m_127521_",
                "(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKESTATIC, CRASH_REPORT_OWNER, "forThrowable", "(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        return patched;
    }

    private static boolean processCrashReportCategoryClass(ClassNode classNode) {
        boolean patched = false;
        patched |= ensureInstanceBridge(
                classNode,
                "m_128165_",
                "(Ljava/lang/String;Lnet/minecraft/CrashReportDetail;)Lnet/minecraft/CrashReportCategory;",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new VarInsnNode(Opcodes.ALOAD, 2),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CRASH_REPORT_CATEGORY_OWNER, "setDetail", "(Ljava/lang/String;Lnet/minecraft/CrashReportDetail;)Lnet/minecraft/CrashReportCategory;", false),
                new InsnNode(Opcodes.ARETURN)
        );
        patched |= ensureInstanceBridge(
                classNode,
                "m_128168_",
                "(Ljava/lang/StringBuilder;)V",
                new VarInsnNode(Opcodes.ALOAD, 0),
                new VarInsnNode(Opcodes.ALOAD, 1),
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL, CRASH_REPORT_CATEGORY_OWNER, "getDetails", "(Ljava/lang/StringBuilder;)V", false),
                new InsnNode(Opcodes.RETURN)
        );
        return patched;
    }

    private static boolean processResourceReloadListenerInterface(ClassNode classNode) {
        boolean patched = false;

        boolean hasLegacyMethod = false;
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("m_6213_") && method.desc.equals("(Lnet/minecraft/server/packs/resources/ResourceManager;)V")) {
                hasLegacyMethod = true;
                break;
            }
        }
        if (!hasLegacyMethod) {
            MethodNode legacy = new MethodNode(
                    Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT,
                    "m_6213_",
                    "(Lnet/minecraft/server/packs/resources/ResourceManager;)V",
                    null,
                    null
            );
            classNode.methods.add(legacy);
            patched = true;
        }

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("onResourceManagerReload") && method.desc.equals("(Lnet/minecraft/server/packs/resources/ResourceManager;)V")) {
                if ((method.access & Opcodes.ACC_ABSTRACT) != 0) {
                    method.access &= ~Opcodes.ACC_ABSTRACT;
                    method.instructions = new InsnList();
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    method.instructions.add(new MethodInsnNode(
                            Opcodes.INVOKEINTERFACE,
                            RESOURCE_RELOAD_LISTENER_OWNER,
                            "m_6213_",
                            "(Lnet/minecraft/server/packs/resources/ResourceManager;)V",
                            true
                    ));
                    method.instructions.add(new InsnNode(Opcodes.RETURN));
                    method.maxStack = 2;
                    method.maxLocals = 2;
                    patched = true;
                }
                break;
            }
        }

        return patched;
    }

    private static boolean processStyledContentConsumerInterface(ClassNode classNode) {
        boolean patched = false;

        boolean hasLegacyMethod = false;
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("m_7164_") && method.desc.equals("(Lnet/minecraft/network/chat/Style;Ljava/lang/String;)Ljava/util/Optional;")) {
                hasLegacyMethod = true;
                break;
            }
        }
        if (!hasLegacyMethod) {
            MethodNode legacy = new MethodNode(
                    Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT,
                    "m_7164_",
                    "(Lnet/minecraft/network/chat/Style;Ljava/lang/String;)Ljava/util/Optional;",
                    null,
                    null
            );
            classNode.methods.add(legacy);
            patched = true;
        }

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("accept") && method.desc.equals("(Lnet/minecraft/network/chat/Style;Ljava/lang/String;)Ljava/util/Optional;")) {
                if ((method.access & Opcodes.ACC_ABSTRACT) != 0) {
                    method.access &= ~Opcodes.ACC_ABSTRACT;
                    method.instructions = new InsnList();
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    method.instructions.add(new MethodInsnNode(
                            Opcodes.INVOKEINTERFACE,
                            STYLED_CONTENT_CONSUMER_OWNER,
                            "m_7164_",
                            "(Lnet/minecraft/network/chat/Style;Ljava/lang/String;)Ljava/util/Optional;",
                            true
                    ));
                    method.instructions.add(new InsnNode(Opcodes.ARETURN));
                    method.maxStack = 3;
                    method.maxLocals = 3;
                    patched = true;
                }
                break;
            }
        }

        return patched;
    }

    private static boolean processJeiStarterClass(ClassNode classNode) {
        boolean patched = false;
        for (MethodNode method : classNode.methods) {
            for (org.objectweb.asm.tree.AbstractInsnNode node = method.instructions.getFirst(); node != null; node = node.getNext()) {
                if (node instanceof FieldInsnNode fieldInsn
                        && fieldInsn.getOpcode() == Opcodes.GETFIELD
                        && fieldInsn.owner.equals("net/minecraft/client/Minecraft")
                        && fieldInsn.name.equals("f_91073_")
                        && fieldInsn.desc.equals("Lnet/minecraft/client/multiplayer/ClientLevel;")) {
                    fieldInsn.name = "level";
                    patched = true;
                }
            }
        }
        return patched;
    }

    private static boolean processJeiVanillaPluginClass(ClassNode classNode) {
        boolean patched = false;
        for (MethodNode method : classNode.methods) {
            for (org.objectweb.asm.tree.AbstractInsnNode node = method.instructions.getFirst(); node != null; node = node.getNext()) {
                if (node instanceof FieldInsnNode fieldInsn) {
                    patched |= rewriteJeiVanillaField(fieldInsn);
                } else if (node instanceof MethodInsnNode methodInsn) {
                    patched |= rewriteJeiVanillaMethod(methodInsn);
                }
            }
        }
        return patched;
    }

    private static boolean processJeiVanillaRecipesClass(ClassNode classNode) {
        boolean patched = false;
        for (MethodNode method : classNode.methods) {
            for (org.objectweb.asm.tree.AbstractInsnNode node = method.instructions.getFirst(); node != null; node = node.getNext()) {
                if (node instanceof FieldInsnNode fieldInsn) {
                    if (fieldInsn.owner.equals(MINECRAFT_OWNER)
                            && fieldInsn.name.equals("f_91073_")
                            && fieldInsn.desc.equals("Lnet/minecraft/client/multiplayer/ClientLevel;")) {
                        fieldInsn.name = "level";
                        patched = true;
                    }
                    if (fieldInsn.owner.equals(RECIPE_TYPE_OWNER)) {
                        patched |= rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                            case "f_44107_" -> "CRAFTING";
                            case "f_44108_" -> "SMELTING";
                            case "f_44109_" -> "BLASTING";
                            case "f_44110_" -> "SMOKING";
                            case "f_44111_" -> "CAMPFIRE_COOKING";
                            case "f_44112_" -> "STONECUTTING";
                            case "f_44113_" -> "SMITHING";
                            default -> null;
                        });
                    }
                } else if (node instanceof MethodInsnNode methodInsn) {
                    if (methodInsn.owner.equals(CLIENT_LEVEL_OWNER)
                            && methodInsn.name.equals("m_7465_")
                            && methodInsn.desc.equals("()Lnet/minecraft/world/item/crafting/RecipeManager;")) {
                        methodInsn.name = "getRecipeManager";
                        patched = true;
                    }
                    if (methodInsn.owner.equals(RECIPE_MANAGER_OWNER)
                            && methodInsn.name.equals("m_44013_")
                            && methodInsn.desc.equals("(Lnet/minecraft/world/item/crafting/RecipeType;)Ljava/util/List;")) {
                        methodInsn.name = "getAllRecipesFor";
                        patched = true;
                    }
                }
            }
        }
        return patched;
    }

    private static boolean processJeiItemStackListFactoryClass(ClassNode classNode) {
        boolean patched = false;
        for (MethodNode method : classNode.methods) {
            for (org.objectweb.asm.tree.AbstractInsnNode node = method.instructions.getFirst(); node != null; node = node.getNext()) {
                if (node instanceof FieldInsnNode fieldInsn) {
                    patched |= rewriteJeiItemStackListFactoryField(fieldInsn);
                } else if (node instanceof MethodInsnNode methodInsn) {
                    patched |= rewriteJeiItemStackListFactoryMethod(methodInsn);
                }
            }
        }
        return patched;
    }

    private static boolean rewriteJeiVanillaField(FieldInsnNode fieldInsn) {
        if (fieldInsn.owner.equals(ITEMS_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_42399_" -> "BOWL";
                case "f_42738_" -> "POTION";
                case "f_42589_" -> "SPLASH_POTION";
                case "f_42736_" -> "TIPPED_ARROW";
                case "f_42739_" -> "LINGERING_POTION";
                case "f_42690_" -> "ENCHANTED_BOOK";
                case "f_151033_" -> "LIGHT";
                case "f_42487_" -> "PAINTING";
                case "f_220219_" -> "GOAT_HORN";
                case "f_42688_" -> "FIREWORK_ROCKET";
                case "f_42718_" -> "SUSPICIOUS_STEW";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(BLOCKS_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_50072_" -> "BROWN_MUSHROOM";
                case "f_50073_" -> "RED_MUSHROOM";
                case "f_50091_" -> "CRAFTING_TABLE";
                case "f_50679_" -> "STONECUTTER";
                case "f_50094_" -> "FURNACE";
                case "f_50619_" -> "SMOKER";
                case "f_50620_" -> "BLAST_FURNACE";
                case "f_50683_" -> "CAMPFIRE";
                case "f_50684_" -> "SOUL_CAMPFIRE";
                case "f_50255_" -> "BREWING_STAND";
                case "f_50322_" -> "ANVIL";
                case "f_50625_" -> "SMITHING_TABLE";
                case "f_50715_" -> "COMPOSTER";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(MENU_TYPE_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_39968_" -> "CRAFTING";
                case "f_39970_" -> "FURNACE";
                case "f_39978_" -> "SMOKER";
                case "f_39966_" -> "BLAST_FURNACE";
                case "f_39967_" -> "BREWING_STAND";
                case "f_39964_" -> "ANVIL";
                case "f_39977_" -> "SMITHING";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(REGISTRIES_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_256808_" -> "FLUID";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(ITEM_TAGS_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_13145_" -> "SMALL_FLOWERS";
                default -> null;
            });
        }

        return false;
    }

    private static boolean rewriteJeiVanillaMethod(MethodInsnNode methodInsn) {
        if ((methodInsn.owner.equals(RECIPE_INTERFACE_OWNER) || methodInsn.owner.equals(RECIPE_OWNER))
                && methodInsn.name.equals("m_5598_")
                && methodInsn.desc.equals("()Z")) {
            methodInsn.name = "isSpecial";
            return true;
        }
        if ((methodInsn.owner.equals(RECIPE_INTERFACE_OWNER) || methodInsn.owner.equals(RECIPE_OWNER))
                && methodInsn.name.equals("m_8043_")
                && methodInsn.desc.equals("(Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;")) {
            methodInsn.name = "getResultItem";
            return true;
        }
        if ((methodInsn.owner.equals(RECIPE_INTERFACE_OWNER) || methodInsn.owner.equals(RECIPE_OWNER))
                && methodInsn.name.equals("m_7527_")
                && methodInsn.desc.equals("()Lnet/minecraft/core/NonNullList;")) {
            methodInsn.name = "getIngredients";
            return true;
        }
        if ((methodInsn.owner.equals(RECIPE_INTERFACE_OWNER) || methodInsn.owner.equals(RECIPE_OWNER))
                && methodInsn.name.equals("m_6423_")
                && methodInsn.desc.equals("()Lnet/minecraft/resources/ResourceLocation;")) {
            methodInsn.name = "getId";
            return true;
        }
        return false;
    }

    private static boolean rewriteJeiItemStackListFactoryField(FieldInsnNode fieldInsn) {
        if (fieldInsn.owner.equals(MINECRAFT_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_91074_" -> "player";
                case "f_91066_" -> "options";
                case "f_91073_" -> "level";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals("net/minecraft/client/player/LocalPlayer")) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_108617_" -> "connection";
                case "f_36096_" -> "containerMenu";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals("net/minecraft/client/gui/screens/Screen")) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_96541_" -> "minecraft";
                case "f_96543_" -> "width";
                case "f_96544_" -> "height";
                case "f_96547_" -> "font";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(REGISTRIES_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_256913_" -> "ITEM";
                case "f_256747_" -> "BLOCK";
                default -> null;
            });
        }

        return false;
    }

    private static boolean rewriteJeiItemStackListFactoryMethod(MethodInsnNode methodInsn) {
        if (methodInsn.owner.equals("net/minecraft/world/flag/FeatureFlagSet")
                && methodInsn.name.equals("m_246902_")
                && methodInsn.desc.equals("()Lnet/minecraft/world/flag/FeatureFlagSet;")) {
            methodInsn.name = "of";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/client/Options")
                && methodInsn.name.equals("m_257871_")
                && methodInsn.desc.equals("()Lnet/minecraft/client/OptionInstance;")) {
            methodInsn.name = "operatorItemsTab";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/client/OptionInstance")
                && methodInsn.name.equals("m_231551_")
                && methodInsn.desc.equals("()Ljava/lang/Object;")) {
            methodInsn.name = "get";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/client/multiplayer/ClientLevel")
                && methodInsn.name.equals("m_9598_")
                && methodInsn.desc.equals("()Lnet/minecraft/core/RegistryAccess;")) {
            methodInsn.name = "registryAccess";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/world/item/CreativeModeTabs")
                && methodInsn.name.equals("m_257478_")
                && methodInsn.desc.equals("()Ljava/util/List;")) {
            methodInsn.name = "allTabs";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/world/item/CreativeModeTab")) {
            String newName = switch (methodInsn.name) {
                case "m_257962_" -> "getType";
                case "m_40786_" -> "getDisplayName";
                case "m_269498_" -> "buildContents";
                case "m_261235_" -> "getSearchTabDisplayItems";
                case "m_260957_" -> "getDisplayItems";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals("net/minecraft/core/Registry")
                && methodInsn.name.equals("m_255303_")
                && methodInsn.desc.equals("()Lnet/minecraft/core/HolderLookup$RegistryLookup;")) {
            methodInsn.name = "asLookup";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/core/HolderLookup$RegistryLookup")
                && methodInsn.name.equals("m_245140_")
                && methodInsn.desc.equals("(Lnet/minecraft/world/flag/FeatureFlagSet;)Lnet/minecraft/core/HolderLookup;")) {
            methodInsn.name = "filterFeatures";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/core/HolderLookup")
                && methodInsn.name.equals("m_214062_")
                && methodInsn.desc.equals("()Ljava/util/stream/Stream;")) {
            methodInsn.name = "listElements";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/world/item/ItemStack")) {
            String newName = switch (methodInsn.name) {
                case "m_41619_" -> "isEmpty";
                case "m_41613_" -> "getCount";
                case "m_41720_" -> "getItem";
                case "m_41764_" -> "setCount";
                case "m_41777_" -> "copy";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        return false;
    }

    private static boolean rewriteGenericJeiField(FieldInsnNode fieldInsn) {
        if (fieldInsn.owner.equals("net/minecraft/world/level/block/ComposterBlock")) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_51914_" -> "COMPOSTABLES";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals("net/minecraft/world/item/alchemy/PotionBrewing")) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_43494_" -> "POTION_MIXES";
                case "f_43495_" -> "CONTAINER_MIXES";
                case "f_43496_" -> "ALLOWED_CONTAINERS";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals("net/minecraft/network/chat/Style")) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_131099_" -> "EMPTY";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(COMMON_COMPONENTS_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_237098_" -> "EMPTY";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals("net/minecraft/client/gui/components/Button")) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_252438_" -> "DEFAULT_NARRATION";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals("com/mojang/blaze3d/vertex/DefaultVertexFormat")) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_85817_" -> "POSITION_TEX";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(MINECRAFT_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_91062_" -> "font";
                case "f_91066_" -> "options";
                case "f_91067_" -> "mouseHandler";
                case "f_91073_" -> "level";
                case "f_91074_" -> "player";
                case "f_91080_" -> "screen";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals("net/minecraft/client/gui/screens/Screen")) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_96541_" -> "minecraft";
                case "f_96543_" -> "width";
                case "f_96544_" -> "height";
                case "f_96547_" -> "font";
                default -> null;
            });
        }

        if (fieldInsn.owner.startsWith("net/minecraft/client/gui/screens/")) {
            String newName = switch (fieldInsn.name) {
                case "f_96541_" -> "minecraft";
                case "f_96543_" -> "width";
                case "f_96544_" -> "height";
                case "f_96547_" -> "font";
                default -> null;
            };
            if (newName != null) {
                fieldInsn.name = newName;
                return true;
            }
        }

        if (fieldInsn.owner.startsWith("mezz/jei/")) {
            String newName = switch (fieldInsn.name) {
                case "f_96541_" -> "minecraft";
                case "f_96543_" -> "width";
                case "f_96544_" -> "height";
                case "f_96547_" -> "font";
                case "f_93618_" -> "width";
                case "f_93619_" -> "height";
                case "f_93623_" -> "active";
                case "f_93624_" -> "visible";
                default -> null;
            };
            if (newName != null) {
                fieldInsn.name = newName;
                return true;
            }
        }

        if (fieldInsn.owner.equals(TOOLTIP_FLAG_DEFAULT_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_256730_" -> "ADVANCED";
                case "f_256752_" -> "NORMAL";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals("net/minecraft/world/item/crafting/SmithingTransformRecipe")) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_265949_" -> "template";
                case "f_265888_" -> "base";
                case "f_265907_" -> "addition";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals("net/minecraft/world/item/crafting/SmithingTrimRecipe")) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_265958_" -> "template";
                case "f_266040_" -> "base";
                case "f_266053_" -> "addition";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(FLUIDS_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_76191_" -> "EMPTY";
                case "f_76193_" -> "WATER";
                case "f_76195_" -> "LAVA";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(FLUID_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_257020_" -> "builtInRegistryHolder";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(MINECRAFT_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_91062_" -> "font";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(POTIONS_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_43598_" -> "EMPTY";
                case "f_43599_" -> "WATER";
                case "f_43600_" -> "MUNDANE";
                case "f_43601_" -> "THICK";
                case "f_43602_" -> "AWKWARD";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(BUILT_IN_REGISTRIES_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_257020_" -> "FLUID";
                case "f_257033_" -> "ITEM";
                case "f_256896_" -> "INSTRUMENT";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(REGISTRIES_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_257010_" -> "INSTRUMENT";
                case "f_256762_" -> "ENCHANTMENT";
                case "f_256973_" -> "POTION";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(INGREDIENT_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_43901_" -> "EMPTY";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(ITEMS_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_42412_" -> "ARROW";
                case "f_42354_" -> "TURTLE_HELMET";
                case "f_42383_" -> "IRON_SWORD";
                case "f_42384_" -> "IRON_SHOVEL";
                case "f_42385_" -> "IRON_PICKAXE";
                case "f_42386_" -> "IRON_AXE";
                case "f_42387_" -> "IRON_HOE";
                case "f_42388_" -> "DIAMOND_SWORD";
                case "f_42389_" -> "DIAMOND_SHOVEL";
                case "f_42390_" -> "DIAMOND_PICKAXE";
                case "f_42391_" -> "DIAMOND_AXE";
                case "f_42392_" -> "DIAMOND_HOE";
                case "f_42393_" -> "NETHERITE_SWORD";
                case "f_42394_" -> "NETHERITE_SHOVEL";
                case "f_42395_" -> "NETHERITE_HOE";
                case "f_42396_" -> "NETHERITE_PICKAXE";
                case "f_42397_" -> "NETHERITE_AXE";
                case "f_42407_" -> "LEATHER_HELMET";
                case "f_42408_" -> "LEATHER_CHESTPLATE";
                case "f_42420_" -> "WOODEN_SWORD";
                case "f_42421_" -> "WOODEN_SHOVEL";
                case "f_42422_" -> "WOODEN_PICKAXE";
                case "f_42423_" -> "WOODEN_AXE";
                case "f_42424_" -> "WOODEN_HOE";
                case "f_42425_" -> "STONE_SWORD";
                case "f_42426_" -> "STONE_SHOVEL";
                case "f_42427_" -> "STONE_PICKAXE";
                case "f_42428_" -> "STONE_AXE";
                case "f_42429_" -> "STONE_HOE";
                case "f_42430_" -> "GOLDEN_SWORD";
                case "f_42431_" -> "GOLDEN_SHOVEL";
                case "f_42432_" -> "GOLDEN_PICKAXE";
                case "f_42433_" -> "GOLDEN_AXE";
                case "f_42434_" -> "GOLDEN_HOE";
                case "f_42462_" -> "LEATHER_LEGGINGS";
                case "f_42463_" -> "LEATHER_BOOTS";
                case "f_42464_" -> "CHAINMAIL_HELMET";
                case "f_42465_" -> "CHAINMAIL_CHESTPLATE";
                case "f_42466_" -> "CHAINMAIL_LEGGINGS";
                case "f_42467_" -> "CHAINMAIL_BOOTS";
                case "f_42468_" -> "IRON_HELMET";
                case "f_42469_" -> "IRON_CHESTPLATE";
                case "f_42470_" -> "IRON_LEGGINGS";
                case "f_42471_" -> "IRON_BOOTS";
                case "f_42472_" -> "DIAMOND_HELMET";
                case "f_42473_" -> "DIAMOND_CHESTPLATE";
                case "f_42474_" -> "DIAMOND_LEGGINGS";
                case "f_42475_" -> "DIAMOND_BOOTS";
                case "f_42476_" -> "GOLDEN_HELMET";
                case "f_42477_" -> "GOLDEN_CHESTPLATE";
                case "f_42478_" -> "GOLDEN_LEGGINGS";
                case "f_42479_" -> "GOLDEN_BOOTS";
                case "f_42480_" -> "NETHERITE_CHESTPLATE";
                case "f_42481_" -> "NETHERITE_BOOTS";
                case "f_42482_" -> "NETHERITE_LEGGINGS";
                case "f_42483_" -> "NETHERITE_HELMET";
                case "f_42738_" -> "TIPPED_ARROW";
                case "f_42739_" -> "LINGERING_POTION";
                case "f_42740_" -> "SHIELD";
                case "f_42714_" -> "PHANTOM_MEMBRANE";
                case "f_42741_" -> "ELYTRA";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(ITEM_TAGS_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_13168_" -> "PLANKS";
                case "f_13191_" -> "BANNERS";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(BLOCKS_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_50456_" -> "SHULKER_BOX";
                default -> null;
            });
        }

        if (fieldInsn.owner.equals(BLOCK_ENTITY_TYPE_OWNER)) {
            return rewriteFieldName(fieldInsn, switch (fieldInsn.name) {
                case "f_58935_" -> "BANNER";
                default -> null;
            });
        }

        return false;
    }

    private static boolean rewriteGenericJeiMethod(MethodInsnNode methodInsn) {
        if (methodInsn.owner.startsWith(CRAFTING_RECIPE_PACKAGE_PREFIX)) {
            if (methodInsn.name.equals("m_5598_")
                    && methodInsn.desc.equals("()Z")) {
                methodInsn.name = "isSpecial";
                return true;
            }
            if (methodInsn.name.equals("m_8043_")
                    && methodInsn.desc.equals("(Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;")) {
                methodInsn.name = "getResultItem";
                return true;
            }
            if (methodInsn.name.equals("m_7527_")
                    && methodInsn.desc.equals("()Lnet/minecraft/core/NonNullList;")) {
                methodInsn.name = "getIngredients";
                return true;
            }
            if (methodInsn.name.equals("m_6423_")
                    && methodInsn.desc.equals("()Lnet/minecraft/resources/ResourceLocation;")) {
                methodInsn.name = "getId";
                return true;
            }
        }

        if (methodInsn.owner.equals("net/minecraft/world/item/ItemStack")) {
            String newName = switch (methodInsn.name) {
                case "m_41613_" -> "getCount";
                case "m_41764_" -> "setCount";
                case "m_41777_" -> "copy";
                case "m_41778_" -> "getDescriptionId";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals("net/minecraft/world/level/block/Block")
                && methodInsn.name.equals("m_5456_")
                && methodInsn.desc.equals("()Lnet/minecraft/world/item/Item;")) {
            methodInsn.name = "asItem";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/world/level/block/Block")
                && methodInsn.name.equals("m_7705_")
                && methodInsn.desc.equals("()Ljava/lang/String;")) {
            methodInsn.name = "getDescriptionId";
            return true;
        }

        if (methodInsn.owner.equals(FLOWER_BLOCK_OWNER)) {
            if (methodInsn.name.equals("m_5456_")
                    && methodInsn.desc.equals("()Lnet/minecraft/world/item/Item;")) {
                methodInsn.name = "asItem";
                return true;
            }
            if (methodInsn.name.equals("m_7705_")
                    && methodInsn.desc.equals("()Ljava/lang/String;")) {
                methodInsn.name = "getDescriptionId";
                return true;
            }
        }

        if (methodInsn.owner.equals(BLOCK_ITEM_OWNER)
                && methodInsn.name.equals("m_40614_")
                && methodInsn.desc.equals("()Lnet/minecraft/world/level/block/Block;")) {
            methodInsn.name = "getBlock";
            return true;
        }

        if (methodInsn.owner.equals(BLOCK_ITEM_OWNER)
                && methodInsn.name.equals("m_186338_")
                && methodInsn.desc.equals("(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/nbt/CompoundTag;)V")) {
            methodInsn.name = "setBlockEntityData";
            return true;
        }

        if (methodInsn.owner.equals(BANNER_ITEM_OWNER)
                && methodInsn.name.equals("m_40545_")
                && methodInsn.desc.equals("()Lnet/minecraft/world/item/DyeColor;")) {
            methodInsn.name = "getColor";
            return true;
        }

        if (methodInsn.owner.equals(DYE_COLOR_OWNER)
                && methodInsn.name.equals("m_41060_")
                && methodInsn.desc.equals("()I")) {
            methodInsn.name = "getId";
            return true;
        }

        if (methodInsn.owner.equals(DYE_ITEM_OWNER)
                && methodInsn.name.equals("m_41082_")
                && methodInsn.desc.equals("(Lnet/minecraft/world/item/DyeColor;)Lnet/minecraft/world/item/DyeItem;")) {
            methodInsn.name = "byColor";
            return true;
        }

        if (methodInsn.owner.equals(TIERS_OWNER)
                && methodInsn.name.equals("m_6282_")
                && methodInsn.desc.equals("()Lnet/minecraft/world/item/crafting/Ingredient;")) {
            methodInsn.name = "getRepairIngredient";
            return true;
        }

        if (methodInsn.owner.equals(SUSPICIOUS_STEW_ITEM_OWNER)
                && methodInsn.name.equals("m_43258_")
                && methodInsn.desc.equals("(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/effect/MobEffect;I)V")) {
            methodInsn.name = "saveMobEffect";
            return true;
        }

        if (methodInsn.owner.equals(INGREDIENT_OWNER)
                && methodInsn.name.equals("m_43927_")
                && methodInsn.desc.equals("([Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/crafting/Ingredient;")) {
            methodInsn.name = "of";
            return true;
        }

        if (methodInsn.owner.equals(INGREDIENT_OWNER)
                && methodInsn.name.equals("m_43938_")
                && methodInsn.desc.equals("(Ljava/util/stream/Stream;)Lnet/minecraft/world/item/crafting/Ingredient;")) {
            methodInsn.name = "fromValues";
            return true;
        }

        if (methodInsn.owner.equals(INGREDIENT_OWNER)
                && methodInsn.name.equals("m_43929_")
                && methodInsn.desc.equals("([Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/crafting/Ingredient;")) {
            methodInsn.name = "of";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/core/DefaultedRegistry")
                && methodInsn.name.equals("m_206058_")
                && methodInsn.desc.equals("(Lnet/minecraft/tags/TagKey;)Ljava/lang/Iterable;")) {
            methodInsn.name = "getTagOrEmpty";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/core/DefaultedRegistry")
                && methodInsn.name.equals("m_203431_")
                && methodInsn.desc.equals("(Lnet/minecraft/tags/TagKey;)Ljava/util/Optional;")) {
            methodInsn.name = "getTag";
            return true;
        }

        if (methodInsn.owner.equals(RESOURCE_LOCATION_OWNER)) {
            String newName = switch (methodInsn.name) {
                case "m_135820_" -> "tryParse";
                case "m_135827_" -> "getNamespace";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals(RESOURCE_KEY_OWNER)
                && methodInsn.name.equals("m_135785_")
                && methodInsn.desc.equals("(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/resources/ResourceKey;")) {
            methodInsn.name = "create";
            return true;
        }

        if (methodInsn.owner.equals(CRASH_REPORT_OWNER)
                && methodInsn.name.equals("m_127514_")
                && methodInsn.desc.equals("(Ljava/lang/String;)Lnet/minecraft/CrashReportCategory;")) {
            methodInsn.name = "addCategory";
            return true;
        }

        if (methodInsn.owner.equals(CRASH_REPORT_OWNER)
                && methodInsn.name.equals("m_127516_")
                && methodInsn.desc.equals("(Ljava/lang/String;I)Lnet/minecraft/CrashReportCategory;")) {
            methodInsn.name = "addCategory";
            return true;
        }

        if (methodInsn.owner.equals(CRASH_REPORT_OWNER)
                && methodInsn.name.equals("m_127521_")
                && methodInsn.desc.equals("(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;")) {
            methodInsn.name = "forThrowable";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/PoseStack")
                && methodInsn.name.equals("m_85837_")
                && methodInsn.desc.equals("(DDD)V")) {
            methodInsn.name = "translate";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/client/renderer/RenderType")
                && methodInsn.name.equals("m_285907_")
                && methodInsn.desc.equals("()Lnet/minecraft/client/renderer/RenderType;")) {
            methodInsn.name = "gui";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/client/renderer/RenderType")
                && methodInsn.name.equals("m_286086_")
                && methodInsn.desc.equals("()Lnet/minecraft/client/renderer/RenderType;")) {
            methodInsn.name = "guiOverlay";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/client/gui/components/AbstractWidget")
                && methodInsn.name.equals("m_6375_")
                && methodInsn.desc.equals("(DDI)Z")) {
            methodInsn.name = "mouseClicked";
            return true;
        }

        if (methodInsn.owner.equals(CLIENT_LEVEL_OWNER)
                && methodInsn.name.equals("m_9598_")
                && methodInsn.desc.equals("()Lnet/minecraft/core/RegistryAccess;")) {
            methodInsn.name = "registryAccess";
            return true;
        }

        if (methodInsn.owner.equals(REGISTRY_OWNER)
                && methodInsn.name.equals("m_203636_")
                && methodInsn.desc.equals("(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;")) {
            methodInsn.name = "getHolder";
            return true;
        }

        if (methodInsn.owner.equals(HOLDER_REFERENCE_OWNER)
                && methodInsn.name.equals("m_205785_")
                && methodInsn.desc.equals("()Lnet/minecraft/resources/ResourceKey;")) {
            methodInsn.name = "key";
            return true;
        }

        if (methodInsn.owner.equals(HOLDER_OWNER)
                && methodInsn.name.equals("m_203334_")
                && methodInsn.desc.equals("()Ljava/lang/Object;")) {
            methodInsn.name = "value";
            return true;
        }

        if (methodInsn.owner.equals(HOLDER_OWNER)
                && methodInsn.name.equals("m_203633_")
                && methodInsn.desc.equals("()Z")) {
            methodInsn.name = "isBound";
            return true;
        }

        if (methodInsn.owner.equals(NON_NULL_LIST_OWNER)
                && methodInsn.name.equals("m_122783_")
                && methodInsn.desc.equals("(Ljava/lang/Object;[Ljava/lang/Object;)Lnet/minecraft/core/NonNullList;")) {
            methodInsn.name = "of";
            return true;
        }

        if (methodInsn.owner.equals(FLOWER_BLOCK_OWNER)) {
            String newName = switch (methodInsn.name) {
                case "m_53521_" -> "getSuspiciousEffect";
                case "m_53522_" -> "getEffectDuration";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals(SHULKER_BOX_BLOCK_OWNER)
                && methodInsn.name.equals("m_56190_")
                && methodInsn.desc.equals("(Lnet/minecraft/world/item/DyeColor;)Lnet/minecraft/world/level/block/Block;")) {
            methodInsn.name = "getBlockByColor";
            return true;
        }

        if (methodInsn.owner.equals(COMPOUND_TAG_OWNER)) {
            String newName = switch (methodInsn.name) {
                case "m_128405_" -> "putInt";
                case "m_128425_" -> "contains";
                case "m_128461_" -> "getString";
                case "m_128445_" -> "getByte";
                case "m_128437_" -> "getList";
                case "m_128448_" -> "getShort";
                case "m_128451_" -> "getInt";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals(LIST_TAG_OWNER)
                && methodInsn.name.equals("m_128728_")
                && methodInsn.desc.equals("(I)Lnet/minecraft/nbt/CompoundTag;")) {
            methodInsn.name = "getCompound";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/core/HolderSet$ListBacked")
                && methodInsn.name.equals("m_203614_")
                && methodInsn.desc.equals("()Ljava/util/stream/Stream;")) {
            methodInsn.name = "stream";
            return true;
        }

        if (methodInsn.owner.equals(INGREDIENT_OWNER)
                && methodInsn.name.equals("m_43908_")
                && methodInsn.desc.equals("()[Lnet/minecraft/world/item/ItemStack;")) {
            methodInsn.name = "getItems";
            return true;
        }

        if (methodInsn.owner.equals(ENCHANTED_BOOK_ITEM_OWNER)
                && methodInsn.name.equals("m_41163_")
                && methodInsn.desc.equals("(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/nbt/ListTag;")) {
            methodInsn.name = "getEnchantments";
            return true;
        }

        if (methodInsn.owner.equals(ENCHANTMENT_OWNER)
                && methodInsn.name.equals("m_44704_")
                && methodInsn.desc.equals("()Ljava/lang/String;")) {
            methodInsn.name = "getDescriptionId";
            return true;
        }

        if (methodInsn.owner.equals(POTION_OWNER)
                && methodInsn.name.equals("m_43492_")
                && methodInsn.desc.equals("(Ljava/lang/String;)Ljava/lang/String;")) {
            methodInsn.name = "getName";
            return true;
        }

        if (methodInsn.owner.equals(POTION_UTILS_OWNER)) {
            String newName = switch (methodInsn.name) {
                case "m_43549_" -> "setPotion";
                case "m_43579_" -> "getPotion";
                case "m_43547_" -> "getMobEffects";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals(TOOLTIP_FLAG_OWNER)
                && methodInsn.name.equals("m_7050_")
                && methodInsn.desc.equals("()Z")) {
            methodInsn.name = "isAdvanced";
            return true;
        }

        if (methodInsn.owner.equals(FLUID_OWNER)) {
            String newName = switch (methodInsn.name) {
                case "m_76145_" -> "defaultFluidState";
                case "m_7444_" -> "isSource";
                case "m_6212_" -> "isSame";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals(FLUID_STATE_OWNER)
                && methodInsn.name.equals("m_76152_")
                && methodInsn.desc.equals("()Lnet/minecraft/world/level/material/Fluid;")) {
            methodInsn.name = "getType";
            return true;
        }

        if (methodInsn.owner.equals(COMPONENT_OWNER)) {
            String newName = switch (methodInsn.name) {
                case "m_237113_" -> "literal";
                case "m_237115_" -> "translatable";
                case "m_237110_" -> "translatable";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals("net/minecraft/client/gui/screens/Screen")) {
            if (methodInsn.name.equals("m_7222_")
                    && methodInsn.desc.equals("()Lnet/minecraft/client/gui/components/events/GuiEventListener;")) {
                methodInsn.name = "getFocused";
                return true;
            }
            if (methodInsn.name.equals("m_7522_")
                    && methodInsn.desc.equals("(Lnet/minecraft/client/gui/components/events/GuiEventListener;)V")) {
                methodInsn.name = "setFocused";
                return true;
            }
        }

        if (methodInsn.owner.equals(MUTABLE_COMPONENT_OWNER)
                && methodInsn.name.equals("m_130940_")
                && methodInsn.desc.equals("(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;")) {
            methodInsn.name = "withStyle";
            return true;
        }

        if (methodInsn.owner.equals(FONT_OWNER)
                && methodInsn.name.equals("m_92895_")
                && methodInsn.desc.equals("(Ljava/lang/String;)I")) {
            methodInsn.name = "width";
            return true;
        }

        if (methodInsn.owner.equals(GUI_GRAPHICS_OWNER)
                && methodInsn.name.equals("m_280614_")
                && methodInsn.desc.equals("(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I")) {
            methodInsn.name = "drawString";
            return true;
        }

        if (methodInsn.owner.equals(GUI_GRAPHICS_OWNER)
                && methodInsn.name.equals("m_280168_")
                && methodInsn.desc.equals("()Lcom/mojang/blaze3d/vertex/PoseStack;")) {
            methodInsn.name = "pose";
            return true;
        }

        if (methodInsn.owner.equals(MINECRAFT_OWNER)
                && methodInsn.name.equals("m_91258_")
                && methodInsn.desc.equals("(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/function/Function;")) {
            methodInsn.name = "getTextureAtlas";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/platform/InputConstants")
                && methodInsn.name.equals("m_84827_")
                && methodInsn.desc.equals("(II)Lcom/mojang/blaze3d/platform/InputConstants$Key;")) {
            methodInsn.name = "getKey";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/platform/InputConstants$Key")
                && methodInsn.name.equals("m_84868_")
                && methodInsn.desc.equals("()Lcom/mojang/blaze3d/platform/InputConstants$Type;")) {
            methodInsn.name = "getType";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/platform/InputConstants$Type")
                && methodInsn.name.equals("m_84895_")
                && methodInsn.desc.equals("(I)Lcom/mojang/blaze3d/platform/InputConstants$Key;")) {
            methodInsn.name = "getOrCreate";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/client/renderer/GameRenderer")
                && methodInsn.name.equals("m_172817_")
                && methodInsn.desc.equals("()Lnet/minecraft/client/renderer/ShaderInstance;")) {
            methodInsn.name = "getPositionTexShader";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/Tesselator")) {
            String newName = switch (methodInsn.name) {
                case "m_85913_" -> "getInstance";
                case "m_85915_" -> "getBuilder";
                case "m_85914_" -> "end";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/BufferBuilder")
                && methodInsn.name.equals("m_166779_")
                && methodInsn.desc.equals("(Lcom/mojang/blaze3d/vertex/VertexFormat$Mode;Lcom/mojang/blaze3d/vertex/VertexFormat;)V")) {
            methodInsn.name = "begin";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/BufferBuilder")
                && methodInsn.name.equals("m_252986_")
                && methodInsn.desc.equals("(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;")) {
            methodInsn.name = "vertex";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/PoseStack")
                && methodInsn.name.equals("m_85850_")
                && methodInsn.desc.equals("()Lcom/mojang/blaze3d/vertex/PoseStack$Pose;")) {
            methodInsn.name = "last";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/PoseStack")
                && methodInsn.name.equals("m_85836_")
                && methodInsn.desc.equals("()V")) {
            methodInsn.name = "pushPose";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/PoseStack")
                && methodInsn.name.equals("m_85849_")
                && methodInsn.desc.equals("()V")) {
            methodInsn.name = "popPose";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/PoseStack")
                && methodInsn.name.equals("m_252880_")
                && methodInsn.desc.equals("(FFF)V")) {
            methodInsn.name = "translate";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/PoseStack")
                && methodInsn.name.equals("m_85841_")
                && methodInsn.desc.equals("(FFF)V")) {
            methodInsn.name = "scale";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/PoseStack")
                && methodInsn.name.equals("m_252781_")
                && methodInsn.desc.equals("(Lorg/joml/Quaternionf;)V")) {
            methodInsn.name = "mulPose";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/PoseStack$Pose")
                && methodInsn.name.equals("m_252922_")
                && methodInsn.desc.equals("()Lorg/joml/Matrix4f;")) {
            methodInsn.name = "pose";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/VertexConsumer")
                && methodInsn.name.equals("m_7421_")
                && methodInsn.desc.equals("(FF)Lcom/mojang/blaze3d/vertex/VertexConsumer;")) {
            methodInsn.name = "uv";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/vertex/VertexConsumer")
                && methodInsn.name.equals("m_5752_")
                && methodInsn.desc.equals("()V")) {
            methodInsn.name = "endVertex";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/client/renderer/texture/TextureAtlasSprite")) {
            String newName = switch (methodInsn.name) {
                case "m_118409_" -> "getU0";
                case "m_118410_" -> "getV0";
                case "m_118411_" -> "getU1";
                case "m_118412_" -> "getV1";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals("net/minecraft/client/resources/TextureAtlasHolder")
                && methodInsn.name.equals("m_118901_")
                && methodInsn.desc.equals("(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;")) {
            methodInsn.name = "getSprite";
            return true;
        }

        if (methodInsn.owner.equals(MINECRAFT_OWNER)
                && methodInsn.name.equals("m_91268_")
                && methodInsn.desc.equals("()Lcom/mojang/blaze3d/platform/Window;")) {
            methodInsn.name = "getWindow";
            return true;
        }

        if (methodInsn.owner.equals("com/mojang/blaze3d/platform/Window")) {
            String newName = switch (methodInsn.name) {
                case "m_85445_" -> "getGuiScaledWidth";
                case "m_85443_" -> "getScreenWidth";
                case "m_85446_" -> "getGuiScaledHeight";
                case "m_85444_" -> "getScreenHeight";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals("net/minecraft/client/MouseHandler")) {
            String newName = switch (methodInsn.name) {
                case "m_91589_" -> "xpos";
                case "m_91594_" -> "ypos";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        if (methodInsn.owner.equals("net/minecraft/client/player/LocalPlayer")
                && methodInsn.name.equals("m_21220_")
                && methodInsn.desc.equals("()Ljava/util/Collection;")) {
            methodInsn.name = "getActiveEffects";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/client/gui/screens/recipebook/RecipeUpdateListener")
                && methodInsn.name.equals("m_5564_")
                && methodInsn.desc.equals("()Lnet/minecraft/client/gui/screens/recipebook/RecipeBookComponent;")) {
            methodInsn.name = "getRecipeBookComponent";
            return true;
        }

        if (methodInsn.owner.equals("net/minecraft/client/gui/screens/recipebook/RecipeBookComponent")
                && methodInsn.name.equals("m_100385_")
                && methodInsn.desc.equals("()Z")) {
            methodInsn.name = "isVisible";
            return true;
        }

        if (methodInsn.owner.startsWith("mezz/jei/")) {
            if (methodInsn.name.equals("m_252865_")
                    && methodInsn.desc.equals("(I)V")) {
                methodInsn.name = "setX";
                return true;
            }
            if (methodInsn.name.equals("m_253211_")
                    && methodInsn.desc.equals("(I)V")) {
                methodInsn.name = "setY";
                return true;
            }
        }

        if (methodInsn.owner.startsWith("mezz/jei/")) {
            String newName = switch (methodInsn.name) {
                case "m_252754_" -> "getX";
                case "m_252865_" -> "setX";
                case "m_252907_" -> "getY";
                case "m_253211_" -> "setY";
                case "m_5711_" -> "getWidth";
                case "m_93694_" -> "getHeight";
                case "m_93696_" -> "isFocused";
                case "m_93692_" -> "setFocused";
                case "m_94120_" -> "tick";
                case "m_94144_" -> "setValue";
                case "m_94155_" -> "getValue";
                case "m_94213_" -> "isVisible";
                case "m_87963_" -> "renderWidget";
                case "m_88315_" -> "render";
                case "m_7972_" -> "isValidClickButton";
                case "m_93680_" -> "clicked";
                case "m_5953_" -> "isMouseOver";
                case "m_7435_" -> "playDownSound";
                case "m_5716_" -> "onClick";
                default -> null;
            };
            if (newName != null) {
                methodInsn.name = newName;
                return true;
            }
        }

        return false;
    }

    private static boolean rewriteGenericJeiInvokeDynamic(InvokeDynamicInsnNode invokeDynamicInsn) {
        boolean patched = false;
        Handle rewrittenBootstrap = rewriteGenericJeiHandle(invokeDynamicInsn.bsm);
        if (rewrittenBootstrap != null) {
            invokeDynamicInsn.bsm = rewrittenBootstrap;
            patched = true;
        }
        for (int i = 0; i < invokeDynamicInsn.bsmArgs.length; i++) {
            Object arg = invokeDynamicInsn.bsmArgs[i];
            if (arg instanceof Handle handle) {
                Handle rewrittenHandle = rewriteGenericJeiHandle(handle);
                if (rewrittenHandle != null) {
                    invokeDynamicInsn.bsmArgs[i] = rewrittenHandle;
                    patched = true;
                }
            }
        }
        return patched;
    }

    private static Handle rewriteGenericJeiHandle(Handle handle) {
        int opcode = handleTagToOpcode(handle.getTag());
        if (opcode == -1) {
            return null;
        }

        if (isFieldHandle(handle.getTag())) {
            FieldInsnNode fieldInsn = new FieldInsnNode(opcode, handle.getOwner(), handle.getName(), handle.getDesc());
            boolean patched = rewriteJeiVanillaField(fieldInsn)
                    | rewriteJeiItemStackListFactoryField(fieldInsn)
                    | rewriteGenericJeiField(fieldInsn);
            if (!patched) {
                return null;
            }
            return new Handle(handle.getTag(), fieldInsn.owner, fieldInsn.name, fieldInsn.desc, handle.isInterface());
        }

        if (isMethodHandle(handle.getTag())) {
            MethodInsnNode methodInsn = new MethodInsnNode(opcode, handle.getOwner(), handle.getName(), handle.getDesc(), handle.isInterface());
            boolean patched = rewriteJeiVanillaMethod(methodInsn)
                    | rewriteJeiItemStackListFactoryMethod(methodInsn)
                    | rewriteGenericJeiMethod(methodInsn);
            if (!patched) {
                return null;
            }
            return new Handle(handle.getTag(), methodInsn.owner, methodInsn.name, methodInsn.desc, methodInsn.itf);
        }

        return null;
    }

    private static boolean isFieldHandle(int tag) {
        return tag == Opcodes.H_GETFIELD
                || tag == Opcodes.H_GETSTATIC
                || tag == Opcodes.H_PUTFIELD
                || tag == Opcodes.H_PUTSTATIC;
    }

    private static boolean isMethodHandle(int tag) {
        return tag == Opcodes.H_INVOKEVIRTUAL
                || tag == Opcodes.H_INVOKESTATIC
                || tag == Opcodes.H_INVOKESPECIAL
                || tag == Opcodes.H_NEWINVOKESPECIAL
                || tag == Opcodes.H_INVOKEINTERFACE;
    }

    private static int handleTagToOpcode(int tag) {
        return switch (tag) {
            case Opcodes.H_GETFIELD -> Opcodes.GETFIELD;
            case Opcodes.H_GETSTATIC -> Opcodes.GETSTATIC;
            case Opcodes.H_PUTFIELD -> Opcodes.PUTFIELD;
            case Opcodes.H_PUTSTATIC -> Opcodes.PUTSTATIC;
            case Opcodes.H_INVOKEVIRTUAL -> Opcodes.INVOKEVIRTUAL;
            case Opcodes.H_INVOKESTATIC -> Opcodes.INVOKESTATIC;
            case Opcodes.H_INVOKESPECIAL, Opcodes.H_NEWINVOKESPECIAL -> Opcodes.INVOKESPECIAL;
            case Opcodes.H_INVOKEINTERFACE -> Opcodes.INVOKEINTERFACE;
            default -> -1;
        };
    }

    private static boolean rewriteFieldName(FieldInsnNode fieldInsn, String newName) {
        if (newName == null || fieldInsn.name.equals(newName)) {
            return false;
        }
        fieldInsn.name = newName;
        return true;
    }

    private static boolean ensureStaticBridge(ClassNode classNode, String name, String desc, org.objectweb.asm.tree.AbstractInsnNode... body) {
        for (MethodNode method : classNode.methods) {
            if (method.name.equals(name) && method.desc.equals(desc)) {
                return false;
            }
        }
        MethodNode bridge = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, name, desc, null, null);
        for (org.objectweb.asm.tree.AbstractInsnNode node : body) {
            bridge.instructions.add(node);
        }
        bridge.maxStack = Math.max(2, getArgumentSlotCount(desc) + 2);
        bridge.maxLocals = getArgumentSlotCount(desc);
        classNode.methods.add(bridge);
        return true;
    }

    private static boolean ensureInstanceBridge(ClassNode classNode, String name, String desc, org.objectweb.asm.tree.AbstractInsnNode... body) {
        for (MethodNode method : classNode.methods) {
            if (method.name.equals(name) && method.desc.equals(desc)) {
                return false;
            }
        }
        MethodNode bridge = new MethodNode(Opcodes.ACC_PUBLIC, name, desc, null, null);
        for (org.objectweb.asm.tree.AbstractInsnNode node : body) {
            bridge.instructions.add(node);
        }
        bridge.maxStack = Math.max(2, getArgumentSlotCount(desc) + 2);
        bridge.maxLocals = 1 + getArgumentSlotCount(desc);
        classNode.methods.add(bridge);
        return true;
    }

    private static int getArgumentSlotCount(String desc) {
        int count = 0;
        for (Type arg : Type.getArgumentTypes(desc)) {
            count += arg.getSize();
        }
        return count;
    }

    private static boolean processEntityClass(ClassNode classNode) {
        boolean patched = false;
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("<init>")) {
                method.instructions.add(buildEntityDataDefineInsn());
                LOGGER.error("add define Entity");
                patched = true;
            } else if (method.name.equals("m_213877_") && method.desc.equals("()Z")) {
                method.instructions.insert(buildEntityActiveGuard());
                patched = true;
            } else if (method.name.equals("m_6921_")) {
                method.instructions.insert(buildEntityRemoveObjectGuard(classNode.name, "f_19845_", "Lnet/minecraft/world/phys/AABB;"));
                patched = true;
            } else if (method.name.equals("m_20183_")) {
                method.instructions.insert(buildEntityRemoveObjectGuard("net/minecraft/core/BlockPos", "f_121853_", "Lnet/minecraft/core/BlockPos;"));
                patched = true;
            }
        }
        return patched;
    }

    private static boolean processLivingEntityClass(ClassNode classNode) {
        boolean patched = false;
        for (MethodNode method : classNode.methods) {
            InsnList patch = null;
            switch (method.name) {
                case "m_21133_" -> patch = buildLivingAttributeGuard();
                case "m_21223_" -> patch = buildLivingHealthGuard(classNode.name);
                case "m_21233_" -> patch = buildLivingMaxHealthGuard();
                case "m_6667_" -> patch = buildLivingResetGuard(null, null);
                case "m_6469_" -> patch = buildLivingResetGuard(Boolean.FALSE, null);
                case "m_21224_" -> patch = buildLivingResetGuard(Boolean.FALSE, Boolean.TRUE);
                case "m_6084_" -> patch = buildLivingResetGuard(Boolean.TRUE, Boolean.FALSE);
                default -> {
                }
            }
            if (patch != null) {
                method.instructions.insert(patch);
                patched = true;
            }
        }
        return patched;
    }

    private static InsnList buildMinecraftScreenGuard() {
        InsnList insnNodes = new InsnList();
        LabelNode label = new LabelNode();
        addPredicateCheck(insnNodes, 1, "isNotSafe", "(Lnet/minecraft/client/gui/screens/Screen;)Z", label);
        insnNodes.add(new InsnNode(Opcodes.RETURN));
        addLabelWithFrame(insnNodes, label);
        return insnNodes;
    }

    private static InsnList buildItemCooldownGuard() {
        InsnList insnNodes = new InsnList();
        LabelNode label = new LabelNode();

        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "sen/manaita_plus_general/item/data/IMPGKey"));
        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnNodes.add(new TypeInsnNode(Opcodes.INSTANCEOF, "sen/manaita_plus_general/item/armor/MPGArmor"));
        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, label));
        addReturnBoolean(insnNodes, false);

        addLabelWithFrame(insnNodes, label);
        return insnNodes;
    }

    private static InsnList buildEntityDataDefineInsn() {
        InsnList insnNodes = new InsnList();
        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnNodes.add(new FieldInsnNode(Opcodes.GETFIELD, ENTITY_OWNER, "f_19804_", "Lnet/minecraft/network/syncher/SynchedEntityData;"));
        insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC, "sen/manaita_plus_general/util/MPGEntityData", "Type", "Lnet/minecraft/network/syncher/EntityDataAccessor;"));
        insnNodes.add(new InsnNode(Opcodes.ICONST_0));
        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false));
        insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/network/syncher/SynchedEntityData", "define", "(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V", false));
        return insnNodes;
    }

    private static InsnList buildEntityActiveGuard() {
        InsnList insnNodes = new InsnList();
        LabelNode notManaita = new LabelNode();
        LabelNode notRemoved = new LabelNode();
        addPredicateCheck(insnNodes, 0, "isManaita", ENTITY_DESC, notManaita);
        addReturnBoolean(insnNodes, false);
        addLabelWithFrame(insnNodes, notManaita);
        addPredicateCheck(insnNodes, 0, "isRemove", ENTITY_DESC, notRemoved);
        addReturnBoolean(insnNodes, true);
        addLabelWithFrame(insnNodes, notRemoved);
        return insnNodes;
    }

    private static InsnList buildEntityRemoveObjectGuard(String fieldOwner, String fieldName, String fieldDesc) {
        InsnList insnNodes = new InsnList();
        LabelNode label = new LabelNode();
        addPredicateCheck(insnNodes, 0, "isRemove", ENTITY_DESC, label);
        insnNodes.add(new FieldInsnNode(Opcodes.GETSTATIC, fieldOwner, fieldName, fieldDesc));
        insnNodes.add(new InsnNode(Opcodes.ARETURN));
        addLabelWithFrame(insnNodes, label);
        return insnNodes;
    }

    private static InsnList buildLivingAttributeGuard() {
        InsnList insnNodes = new InsnList();
        LabelNode label = new LabelNode();
        addPredicateCheck(insnNodes, 0, "isManaita", LIVING_ENTITY_DESC, label);
        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_OWNER, "getAttributeValue", "(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/Attribute;)D", false));
        insnNodes.add(new InsnNode(Opcodes.DRETURN));
        addLabelWithFrame(insnNodes, label);
        return insnNodes;
    }

    private static InsnList buildLivingHealthGuard(String className) {
        InsnList insnNodes = new InsnList();
        LabelNode notManaita = new LabelNode();
        LabelNode notDead = new LabelNode();
        addPredicateCheck(insnNodes, 0, "isManaita", LIVING_ENTITY_DESC, notManaita);
        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnNodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, className, "m_21233_", "()F", false));
        insnNodes.add(new InsnNode(Opcodes.FRETURN));
        addLabelWithFrame(insnNodes, notManaita);
        addPredicateCheck(insnNodes, 0, "isDead", LIVING_ENTITY_DESC, notDead);
        insnNodes.add(new InsnNode(Opcodes.FCONST_0));
        insnNodes.add(new InsnNode(Opcodes.FRETURN));
        addLabelWithFrame(insnNodes, notDead);
        return insnNodes;
    }

    private static InsnList buildLivingMaxHealthGuard() {
        InsnList insnNodes = new InsnList();
        LabelNode label = new LabelNode();
        addPredicateCheck(insnNodes, 0, "isManaitaSafe", LIVING_ENTITY_DESC, label);
        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_OWNER, "getMaxHealth", "(Lnet/minecraft/world/entity/LivingEntity;)F", false));
        insnNodes.add(new InsnNode(Opcodes.FRETURN));
        addLabelWithFrame(insnNodes, label);
        return insnNodes;
    }

    private static InsnList buildLivingResetGuard(Boolean manaitaReturn, Boolean deadReturn) {
        InsnList insnNodes = new InsnList();
        LabelNode notManaita = new LabelNode();
        addPredicateCheck(insnNodes, 0, "isManaita", LIVING_ENTITY_DESC, notManaita);
        addResetLivingState(insnNodes);
        addReturnForStateMethod(insnNodes, manaitaReturn);
        addLabelWithFrame(insnNodes, notManaita);

        if (deadReturn != null) {
            LabelNode notDead = new LabelNode();
            addPredicateCheck(insnNodes, 0, "isDead", LIVING_ENTITY_DESC, notDead);
            addReturnBoolean(insnNodes, deadReturn);
            addLabelWithFrame(insnNodes, notDead);
        }
        return insnNodes;
    }

    private static void addPredicateCheck(InsnList insnNodes, int varIndex, String methodName, String methodDesc, LabelNode falseLabel) {
        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, varIndex));
        insnNodes.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EVENT_UTIL_OWNER, methodName, methodDesc, false));
        insnNodes.add(new JumpInsnNode(Opcodes.IFEQ, falseLabel));
    }

    private static void addLabelWithFrame(InsnList insnNodes, LabelNode labelNode) {
        insnNodes.add(labelNode);
        insnNodes.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
    }

    private static void addResetLivingState(InsnList insnNodes) {
        addSetLivingIntField(insnNodes, "f_20917_");
        addSetLivingIntField(insnNodes, "f_20919_");
        addSetLivingIntField(insnNodes, "f_20916_");
    }

    private static void addSetLivingIntField(InsnList insnNodes, String fieldName) {
        insnNodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnNodes.add(new InsnNode(Opcodes.ICONST_0));
        insnNodes.add(new FieldInsnNode(Opcodes.PUTFIELD, LIVING_ENTITY_OWNER, fieldName, "I"));
    }

    private static void addReturnForStateMethod(InsnList insnNodes, Boolean returnValue) {
        if (returnValue == null) {
            insnNodes.add(new InsnNode(Opcodes.RETURN));
            return;
        }
        addReturnBoolean(insnNodes, returnValue);
    }

    private static void addReturnBoolean(InsnList insnNodes, boolean value) {
        insnNodes.add(new InsnNode(value ? Opcodes.ICONST_1 : Opcodes.ICONST_0));
        insnNodes.add(new InsnNode(Opcodes.IRETURN));
    }
}
