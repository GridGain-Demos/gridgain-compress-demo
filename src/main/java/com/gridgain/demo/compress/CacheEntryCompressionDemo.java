package com.gridgain.demo.compress;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.gridgain.grid.cache.compress.ZstdDictionaryCompressionConfiguration;

public class CacheEntryCompressionDemo {
    private static final String[] lorems = {
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
        "Ut enim ad minim veniam, quis nostrud exercitation",
        "Excepteur sint occaecat cupidatat non proident, sunt in",
        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum"
    };

    public static void main(String[] args) {
        try (Ignite n1 = Ignition.start(cfg("n1"));
            Ignite n2 = Ignition.start(cfg("n2"))) {
            n1.cluster().state(ClusterState.ACTIVE);

            IgniteCache cacheWithCompression = n1.createCache(
                new CacheConfiguration<>("cacheWithCompression")
                    .setDataRegionName("DataRegionWithCompressedCache")
                    .setEntryCompressionConfiguration(
                        new ZstdDictionaryCompressionConfiguration()));
            IgniteCache cacheNoCompression = n1.createCache(
                new CacheConfiguration<>("cacheNoCompression")
                    .setDataRegionName("DataRegionWithOrdinaryCache"));

            for (int i = 0; i < 20201225; i++) {
                Person person = new Person(
                    i % 2 == 0 ? "Jane Doe" : "Marvin Acme",
                    (i >> 1) % 2 == 0 ? "A Company Made Everything"
                        : "Widgets and Gadgets Co LTD",
                    lorems[(i >> 2) % 4]);

                cacheWithCompression.put(i, person);
                cacheNoCompression.put(i, person);
            }
        }
    }

    private static IgniteConfiguration cfg(String name) {
        return new IgniteConfiguration().setDataStorageConfiguration(
            new DataStorageConfiguration().setMetricsEnabled(true).setDataRegionConfigurations(
                new DataRegionConfiguration().setName("DataRegionWithCompressedCache")
                    .setPersistenceEnabled(true).setMetricsEnabled(true),
                new DataRegionConfiguration().setName("DataRegionWithOrdinaryCache")
                    .setPersistenceEnabled(true).setMetricsEnabled(true)
            )
        ).setIgniteInstanceName(name).setConsistentId(name);
    }

    public static class Person {
        private String name;
        private String company;
        private String lorem;

        public Person(String name, String company, String lorem) {
            this.name = name;
            this.company = company;
            this.lorem = lorem;
        }
    }
}
