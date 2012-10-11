(ns reval.core
  (:gen-class)
  (:require [reval.server :as server]))

(defn -main
  "Start server"
  [& args]
  (apply server/spawn-repl-server (map read-string args)))
