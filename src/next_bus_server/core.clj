(ns next-bus-server.core
  (:require [clojure.tools.logging :as log])
  (:require [clj-time.core :as t])
  (:require [clj-time.local :as l])
  (:require [clj-time.coerce :as c])
  (:require [next-bus-server.client :as client])
  (:require [clojure.string :as str])
  (:use [next-bus-server.utils :onlys (assoc-distance)]))


(defn stopareacode-to-bus-stop
  [stopareacode]
  (let [{lat "Latitude"
         lon "Longitude"
         code "StopAreaCode"
         name "TimingPointName"
         } stopareacode
        bus-stop-name (last (str/split name #",\s*"))
        ]
    {:lat lat :lon lon :code code :name bus-stop-name}))

(defn passtime-to-timeline
  [passtime]
  (let [{number "LinePublicNumber"
         direction "LineDirection"
         destination "DestinationName50"
         arrival-time "ExpectedArrivalTime"
         } passtime
        tz (t/time-zone-for-id "Europe/Amsterdam")
        arrival-time (t/from-time-zone (c/from-string arrival-time) tz)
        sec-left (-
                  (c/to-long arrival-time)
                  (c/to-long (t/now)))]

    {:number number
     :direction direction
     :destination destination
     :min-left (int (/ sec-left 60000))}))

(defonce bus-stops
  (let [stopareacodes (vals (client/load-stopareacodes))]
    (map stopareacode-to-bus-stop stopareacodes)))

(defn timelines
  [bus-stop-code direction]
  (let [
        raw-data (client/get-stoparecode-raw-data bus-stop-code)
        timepoints (flatten (map vals (vals raw-data)))
        passtimes (flatten (map #(vals (get % "Passes")) timepoints))
        records (map passtime-to-timeline (remove nil? passtimes))
        direction-filter (if direction
                           #(= (:direction %) direction)
                           (constantly true))
        time-filter #(>= 99 (:min-left %) 0)
        ]
    (sort-by
      :min-left
      (filter (every-pred time-filter direction-filter) records))))

(defn nearby-bus-stops
  [lat lon]
  (let [stops-with-distance (map (partial assoc-distance lat lon) bus-stops)
        sorted-bus-stops (sort-by :distance stops-with-distance)]
    (take 5 (filter #(< (:distance %) 0.5) sorted-bus-stops))))
