(ns next-bus-server.routes
  (:require [compojure.route :as route]
            [compojure.core :refer [GET defroutes]]
            [ring.util.response :refer [response]]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]])
  (:use [next-bus-server.core :only (nearby-bus-stops timelines)]))


(defn show-nearby-bus-stops [req]
  (let [lat (Float/parseFloat (-> req :params :lat))
        lon (Float/parseFloat (-> req :params :lon))]
    (response (nearby-bus-stops lat lon ))))

(defn show-timelines [req]
  (let [code (-> req :params :bus_stop_code)
        direction (-> req :params :direction {"1" 1 "2" 2})]
    (response (timelines code direction))))

(defroutes app-routes
  (GET "/timelines" [] show-timelines)
  (GET "/nearby-bus-stops" [] show-nearby-bus-stops))

(defonce app-server
  (-> app-routes
     ; (middleware/wrap-json-body)
      (middleware/wrap-json-response)
      (wrap-defaults api-defaults)))
