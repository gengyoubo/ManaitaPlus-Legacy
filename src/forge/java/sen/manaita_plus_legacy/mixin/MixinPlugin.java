package sen.manaita_plus_legacy.mixin;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import sen.manaita_plus_legacy.transform.ManaitaPlusLegacyLaunchPluginService;
import sen.manaita_plus_legacy.util.Helper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MixinPlugin implements IMixinConfigPlugin {
    static {
        LaunchPluginHandler handler = Helper.getFieldValue(Launcher.INSTANCE, "launchPlugins");
        Map<String, ILaunchPluginService> plugins = Helper.getFieldValue(handler, "plugins");
        Map<String, ILaunchPluginService> newMap = new ConcurrentHashMap<>();
        newMap.put("ManaitaPlusLegacy", new ManaitaPlusLegacyLaunchPluginService());
        if (plugins != null)
            for (String name : plugins.keySet())
                newMap.put(name, plugins.get(name));
        Helper.setFieldValue(handler, "plugins", newMap);
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
