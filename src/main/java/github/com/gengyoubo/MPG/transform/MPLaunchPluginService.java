package github.com.gengyoubo.MPG.transform;

import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.EnumSet;

public class MPLaunchPluginService implements ILaunchPluginService {
    public static final Logger LOGGER = LogManager.getLogger("ManaitaPlusCore");

    private static final String EVENT_UTIL_OWNER = "sen/manaita_plus_general/util/EventUtil";
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
        return switch (classNode.name) {
            case "net/minecraft/client/Minecraft" -> processMinecraftClass(classNode);
            case "net/minecraft/world/item/ItemCooldowns" -> processItemCooldownClass(classNode);
            case "net/minecraft/world/entity/Entity" -> processEntityClass(classNode);
            case "net/minecraft/world/entity/LivingEntity" -> processLivingEntityClass(classNode);
            default -> false;
        };
    }

    private static boolean processMinecraftClass(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("m_91152_") && method.desc.equals("(Lnet/minecraft/client/gui/screens/Screen;)V")) {
                method.instructions.insert(buildMinecraftScreenGuard());
                return true;
            }
        }
        return false;
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
