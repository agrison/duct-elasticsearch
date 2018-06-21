(ns duct.database.elasticsearch.spandex
  (:require [qbits.spandex :as s]
            [integrant.core :as ig]
            [duct.logger :as logger]))

(derive :duct.database.elasticsearch/spandex :duct.database/elasticsearch)
(derive :duct.database/elasticsearch :duct/database)

(defrecord Boundary [client sniffer logger])

; Init the connection to ElasticSearch and return a Boundary whose keys are:
;
;   :client   which is the spandex client
;   :sniffer  the spandex sniffer (if any)
;   :logger   the duct/Logger instance
(defmethod ig/init-key :duct.database.elasticsearch/spandex [_ {:keys [logger client sniffer? sniffer]}]
  (let [spandex-client (s/client client)
        sniffer (when sniffer? (s/sniffer spandex-client sniffer))]
    (logger/log logger :report ::connected)
    (->Boundary spandex-client sniffer (atom logger))))

; Closes the Spandex client
(defmethod ig/halt-key! :duct.database.elasticsearch/spandex [_ {:keys [logger client]}]
  (if-not (nil? client)
    (do
      (s/close! client)
      (logger/log @logger :report ::disconnected))))