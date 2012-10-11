(ns reval.client
  (:require [reval.net :as net])
  (:import  (java.net Socket)))

(defn remote-repl
  "Return a function that accepts one argument:
    - :close -> closes the socket
    - everything else is evaluated remotely in the specified server
  Note: this is a function, not a macro, so if you want to send code (a list)
  to be executed remotely it must be quoted to avoid local evaluation.

  Example:
  (let [repl (remote-repl \"localhost\" 9999)] (repl '(range 10)))"
  [host port]
  (let [server-socket    (Socket. host port)
        {:keys [in out]} (net/streams server-socket)]
    (fn [expr]
      (if (= :close expr)
        (.close server-socket)
        (binding [*out* out]
          (prn expr) (flush) (read in))))))
