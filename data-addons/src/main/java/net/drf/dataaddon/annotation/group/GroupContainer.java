/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.drf.dataaddon.annotation.group;


import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.drf.dataaddon.DataAddonBootstrap;
import net.drf.dataaddon.RegistryException;
import net.drf.dataaddon.annotation.DataAddon;
import net.drf.dataaddon.holder.TypeHolder;
import net.drf.dataaddon.storeload.StoreLoadController;
import net.drf.dataaddon.typeregistry.TypeRegistry;

/**
 * Container containing group tags and classes with their instances, it is needed only for {@link DataAddonBootstrap}
 */
public class GroupContainer {
	private static final String DOESNT_HAVE_ANNOTATION = "Class doesn't have annotation";
	public final Map<String, Class<?>> dataAddons = new HashMap<>();
	public final Map<String, Class<? extends TypeRegistry>> typeRegistries = new HashMap<>();
	public final Map<String, Class<? extends TypeHolder>> holders = new HashMap<>();
	public final Map<String, Class<? extends StoreLoadController>> controllers = new HashMap<>();
	public final BiMap<Class<?>, Object> data = HashBiMap.create();

	public <T> void hold(T instance) {
		data.put(instance.getClass(), instance);
	}
	
	public <T> T getInstance(Class<T> clazz) {
		return (T) data.get(clazz);
	}

	public void registerTypeRegistry(Class<? extends TypeRegistry> clazz) {
		if (!clazz.isAnnotationPresent(Registry.class)) {
			
			throw new RegistryException(DOESNT_HAVE_ANNOTATION);
		}
		typeRegistries.put(clazz.getAnnotation(Registry.class).value(), clazz);
	}

	public void registerHolder(Class<? extends TypeHolder> clazz) {
		if (!clazz.isAnnotationPresent(Holder.class)) throw new RegistryException(DOESNT_HAVE_ANNOTATION);
		holders.put(clazz.getAnnotation(Holder.class).value(), clazz);
	}

	public void registerController(Class<? extends StoreLoadController> clazz) {
		if (!clazz.isAnnotationPresent(Controller.class)) throw new RegistryException(DOESNT_HAVE_ANNOTATION);
		controllers.put(clazz.getAnnotation(Controller.class).value(), clazz);
	}

	public void registerImplementation(Class<?> clazz) {
		dataAddons.put(clazz.getAnnotation(DataAddon.class).groupTag(), clazz);
	}
	
	public void clearAll() {
		dataAddons.clear();
		typeRegistries.clear();
		holders.clear();
		controllers.clear();
		data.clear();
	}
}
