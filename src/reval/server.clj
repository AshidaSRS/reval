(ns reval.server
  (:require [reval.misc :as msc]
            [reval.net  :as net]
            [reval.ns   :as nsp])
  (:import (java.net ServerSocket InetAddress)))

(defn repl
  "Read-Eval-Print-Loop in the supplied socket.
  :quit to exit"
  [client-socket]
  (let [user-ns (nsp/new-user-ns (.getPort client-socket))]
    (println "[server] Accepted connection from " (net/str-socket client-socket)
             "->" user-ns)
    (nsp/eval-in-ns user-ns (clojure.core/use 'clojure.core))
    (net/io-to-socket client-socket
      (loop []
        (when-let [input (read *in* nil nil)]
          (try
            (nsp/eval-in-ns user-ns (prn (eval input)))
            (catch Exception e (prn {:exception e})))
          (flush)
          (when (not (= input :quit))
            (recur)))))
    (println "[server]" (net/str-socket client-socket) "disconnected.")
    (remove-ns (ns-name user-ns)))
  (.close client-socket))

(defn repl-server
  "Listen to incoming connections in the specified port and spawn REPL's"
  [port & {:keys [local]}]
  {:pre [(and (pos? port) (< port 65536))]}
  (let [socket (if local
                 (ServerSocket. port 0 (InetAddress/getLocalHost))
                 (ServerSocket. port))]
    (println "Server listening in" (net/str-socket socket))
    (while 1
      (msc/spawn repl (.accept socket)))))

(defn spawn-repl-server
  "Start repl-server in a new thread"
  [& args]
  (apply msc/spawn repl-server args))
