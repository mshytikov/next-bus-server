(ns next-bus-server.server
  (:use [next-bus-server.routes :only (app-server)])
  (:use [org.httpkit.server])
  (:gen-class))

; https://github.com/RyanMcG/Cadence/blob/0.4.2/src/cadence/config.clj
(defn getenv
  "A nice wrapper around System/getenv that allows a second argument to be
  passed in as the default."
  ([variable default] (or (System/getenv variable) default))
  ([variable] (getenv variable nil)))

(defn -main
  [& args]
  (let [port (Integer/parseInt (getenv "PORT" "8080"))]
    (run-server app-server {:port port})))
