(ns next-bus-server.client
  (:require [clojure.data.json :as json])
  (:require [clojure.tools.logging :as log])
  (:require [org.httpkit.client :as http])
  (:require [clojure.java.io :as io]))

(def resp-file (io/as-file "/tmp/next-bus.json"))
(def bus-stops-file (io/as-file "/tmp/next-bus-stops.json"))

(defn stopareacode-url
  [stopareacode]
  (format "http://v0.ovapi.nl/stopareacode/%s" stopareacode ))

(defn get-raw-data
  [url]
  (let [{:keys [error body] :as req} @(http/get url)]
    (if error
      (log/error error)
      (json/read-str body))))

(defn get-stoparecode-raw-data
  [sac]
  (get-raw-data (stopareacode-url sac)))

(defn get-stoparecodes-raw-data
  []
  (get-raw-data "http://v0.ovapi.nl/stopareacode"))


(defn load-stopareacodes
  []
  (if-not (.exists bus-stops-file)
    (spit bus-stops-file (get-stoparecodes-raw-data)))
  (json/read-str (slurp bus-stops-file)))
