(defproject me.grison/duct-elasticsearch "0.1.0"
  :description "Integrant methods for connecting to ElasticSearch via Spandex"
  :url "https://github.com/agrison/duct-elasticsearch"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :lein-release {:scm :git :deploy-via :clojars}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [integrant "0.6.3"]
                 [duct/logger "0.2.1"]
                 [cc.qbits/spandex "0.6.2"]]
  :profiles {:dev {:dependencies [[pl.allegro.tech/embedded-elasticsearch "2.4.2"]]}})
