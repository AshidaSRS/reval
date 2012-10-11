(ns reval.misc)

(defn spawn
  "Start a function in a new thread"
  [f & args]
  (.start (Thread. (partial apply f args))))
