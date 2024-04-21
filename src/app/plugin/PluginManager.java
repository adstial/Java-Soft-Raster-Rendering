package app.plugin;

import app.startup.MyGLApp;

import java.util.ArrayList;

public class PluginManager {
    private ArrayList<Plugin> pluginList;


    public void load(final ArrayList<Class<? extends Plugin>> plugins) {

        pluginList = new ArrayList<>(plugins.size());

        for (final var pluginClass: plugins) {
            try {
                final var plugin = pluginClass.getDeclaredConstructor().newInstance();
                pluginList.add(plugin);

                expand(plugin);

            } catch (Exception ignored) {
                System.out.println("Plugin load failed: " + pluginClass.getName());
            }
        }

        for (final var p: pluginList) {
            p.init(MyGLApp.getContext());
        }
    }

    // 展开插件
    private void expand(final Plugin plugin) {
        if (!plugin.hasExpand()) {
            return;
        }

        for (final var p: plugin.getExpand()) {
            if (pluginList.contains(p)) {
                continue;
            }
            pluginList.add(p);
            expand(p);
        }
    }


}
