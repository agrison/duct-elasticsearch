(ns duct.database.elasticsearch.spandex-test
  (:require [clojure.test :refer :all]
            [duct.database.elasticsearch.spandex :refer :all]
            [integrant.core :as ig])
  (:import (pl.allegro.tech.embeddedelasticsearch EmbeddedElastic)
           (java.util.concurrent TimeUnit)
           (org.elasticsearch.client RestClient)
           (org.elasticsearch.client.sniff ElasticsearchHostsSniffer Sniffer)))

; by default a `test` database` exists in MongoDB.

(deftest key-derive-test
  (is (isa? :duct.database.elasticsearch/spandex :duct.database/elasticsearch))
  (is (isa? :duct.database/elasticsearch :duct/database)))

(def ^EmbeddedElastic elastic (-> (EmbeddedElastic/builder)
                                  (.withElasticVersion "5.0.0")
                                  (.withSetting "transport.tcp.port" 9350)
                                  (.withSetting "cluster.name" "my_cluster")
                                  (.withEsJavaOpts "-Xms128m -Xmx512m")
                                  (.withStartTimeout 1 TimeUnit/MINUTES)
                                  (.build)))

(let [es (atom 0)
      init (partial ig/init-key :duct.database.elasticsearch/spandex)
      halt (partial ig/halt-key! :duct.database.elasticsearch/spandex)]
  (try
    (reset! es (.start elastic))

    (deftest init-key-simple-test
      (let [{:keys [client sniffer]} (init {:client {:hosts ["127.0.0.1"]}})]
        (is (instance? RestClient client))
        (is (nil? sniffer))
        (halt client)))

    (deftest init-key-sniffer-test
      (let [{:keys [client sniffer]} (init {:client {:hosts ["127.0.0.1"]}
                                            :sniffer? true?
                                            :sniffer {}})]
        (is (instance? RestClient client))
        (is (instance? Sniffer sniffer))
        (halt client)))

    (finally
      (.stop @es))))