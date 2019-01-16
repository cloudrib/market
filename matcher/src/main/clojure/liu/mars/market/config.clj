(ns liu.mars.market.config
  (:require [clojure.java.io :refer [resource]])
  (:require [clojure.edn :as edn]
            [clj-postgresql.core :as pg]))

(def default-config
  {:db-spec {:dbtype "postgresql"
             :dbname "match"}
   :sequences "akka://sequences/user/sequences"
   :depth "akka://quotations/user/depth"
   :peek "akka://counter/user/peek"})

(def conf (delay
            (if-let [url (resource "config.edn")]
              (-> url
                  slurp
                  (edn/read-string)
                  (#(merge default-config %)))
              default-config)))

(def sequences
  (:sequences @conf))

(def db
  (delay
    (->> @conf
         :db-spec
         (apply concat)
         (apply pg/pool))))