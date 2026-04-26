package sen.manaita_plus_legacy;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.jetbrains.annotations.NotNull;
import sen.manaita_plus_legacy.transform.ManaitaPlusLegacyLaunchPluginService;
import sen.manaita_plus_legacy.util.Helper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ManaitaManaitaPlusTransformationService implements ITransformationService {

    static {
        LaunchPluginHandler handler = Helper.getFieldValue(Launcher.INSTANCE, "launchPlugins");
        Map<String, ILaunchPluginService> plugins = Helper.getFieldValue(handler, "plugins");
        Map<String, ILaunchPluginService> newMap = new ConcurrentHashMap<>();
        newMap.put("MPG", new ManaitaPlusLegacyLaunchPluginService());
        if (plugins != null)
            for (String name : plugins.keySet())
                newMap.put(name, plugins.get(name));
        Helper.setFieldValue(handler, "plugins", newMap);
        Helper.coexistenceCoreAndMod();
    }

    @Override
    public @NotNull String name() {
        return "ManaitaPlusLegacyTransformationService";
    }

    @Override
    public void initialize(IEnvironment environment) {
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) {
    }

    @Override
    @SuppressWarnings("rawtypes")
    public @NotNull List<ITransformer> transformers() {
        return List.of();
    }
}
