/*
 * This file is part of EmergencyLanding, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <http://techshoom.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.techshroom.emergencylanding.library.eventful;

import java.util.Map;

public final class EmergencyLandingEventFactoryUtils {
    // static final ClassGeneratorProvider factoryProvider = new
    // ClassGeneratorProvider("org.spongepowered.api.event.impl");
    //
    // public static List<? extends EventFactoryPlugin> plugins =
    // Lists.newArrayList(new AccessorModifierEventFactoryPlugin());
    //
    // private static final LoadingCache<Class<?>, EventFactory<?>> factories =
    // CacheBuilder.newBuilder().build(
    // new CacheLoader<Class<?>, EventFactory<?>>() {
    //
    // @Override
    // public EventFactory<?> load(Class<?> type) {
    // return factoryProvider.create(type,
    // ReflectionUtils.getBaseClass(type).getActualClass(), plugins);
    // }
    // });
    //
    //
    // static {
    // factoryProvider.setNullPolicy(NullPolicy.NON_NULL_BY_DEFAULT);
    // }

    /**
     * Creates an event class from an interface and a map of property names to
     * values.
     *
     * @param type
     *            The event interface to generate a class for
     * @param values
     *            The map of property names to values
     * @param <T>
     *            The type of event to be created
     * @return The generated event class.
     */
    public static <T> T createEventImpl(Class<T> type,
            Map<String, Object> values) {
        return null;
        // return (T) factories.getUnchecked(type).apply(values);
    }

}
