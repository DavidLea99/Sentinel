/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.util;

import com.alibaba.csp.sentinel.annotation.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
public final class SpiLoader {

    private static final Map<String, ServiceLoader> SERVICE_LOADER_MAP = new ConcurrentHashMap<String, ServiceLoader>();

    /**
     * Load the first instance by SPI
     * If no instance, return null
     * @param clazz the interface or abstract class representing the service
     * @param <T> the class of the service type
     * @return the first instance in service loader
     */
    public static <T> T loadFirstInstance(Class<T> clazz) {
        String key = clazz.getName();
        // Not thread-safe, as it's expected to be resolved in a thread-safe context.
        ServiceLoader<T> serviceLoader = SERVICE_LOADER_MAP.get(key);
        if (serviceLoader == null) {
            serviceLoader = ServiceLoader.load(clazz);
            SERVICE_LOADER_MAP.put(key, serviceLoader);
        }

        Iterator<T> iterator = serviceLoader.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    /**
     * Load the ordered instance list by SPI
     * The order are sorted by {@link Order} annotation
     * If no @Order annotation in the class of instance, use lowest order {@link Order#LOWEST_PRECEDENCE} as default
     * If no instance, return null
     *
     * @param clazz the interface or abstract class representing the service
     * @param <T> <T> the class of the service type
     * @return the ordered instance list in service loader
     */
    public static <T> List<T> loadOrderedInstanceList(Class<T> clazz) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);

        if (!loader.iterator().hasNext()) {// if no instance in service loader, return null
            return null;
        }

        int instanceSize = 0;
        // create instanceOrderMap, key is T and value is the order of the T component
        Map<T, Integer> instanceOrderMap = new HashMap<T, Integer>(16); // for total 9 default slot
        for (T instance : loader) {
            instanceSize++;
            int order = Order.LOWEST_PRECEDENCE;// if no @Order annotation, use lowest order as default
            if (instance.getClass().isAnnotationPresent(Order.class)) {// get the order value from @Order annotation
                order = instance.getClass().getAnnotation(Order.class).value();
            }
            instanceOrderMap.put(instance, order);
        }

        // if only has 1 instance, no need to sort the order, just return a new ArrayList which contains the instance
        if (instanceSize == 1) {
            return new ArrayList<T>(instanceOrderMap.keySet());
        }

        // create a new ArrayList from instanceOrderMap's entrySet, and sort with the entry's value which is the order of instance
        List<Map.Entry<T, Integer>> tmpList = new ArrayList<Map.Entry<T, Integer>>(instanceOrderMap.entrySet());
        Collections.sort(tmpList, new Comparator<Map.Entry<T, Integer>>() {
            @Override
            public int compare(Map.Entry<T, Integer> o1, Map.Entry<T, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        // now the tmpList is ordered, iterate tmpList and add the entry's key to create the orderedInstanceList
        List<T> orderedInstanceList = new ArrayList<T>(instanceSize);
        for (Map.Entry<T, Integer> entry : tmpList) {
            orderedInstanceList.add(entry.getKey());
        }

        return orderedInstanceList;
    }

    private SpiLoader() {}
}
