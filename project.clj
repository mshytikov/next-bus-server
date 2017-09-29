(defproject next-bus-server "0.1.0-SNAPSHOT"
  :description "Next Bus NL (server)"
  :url "https://github.com/mshytikov/next-bus-server"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :min-lein-version "2.0.0"
  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.logging "0.3.1"]
                 [http-kit "2.1.18"]
                 [compojure "1.3.4"]
                 [ring/ring-core "1.3.2"]
                 [ring/ring-json "0.3.1"]
                 [ring/ring-defaults "0.1.5"]
                 [javax.servlet/servlet-api "2.5"]
                 [clj-time "0.9.0"]
                 ]
  :main ^:skip-aot next-bus-server.server
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
