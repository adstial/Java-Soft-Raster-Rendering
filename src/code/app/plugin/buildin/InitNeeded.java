package code.app.plugin.buildin;


import code.app.plugin.top.PluginAnnotation;
import code.app.plugin.top.PluginType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PluginAnnotation(PluginType.BuildIn)
public @interface InitNeeded {

}
