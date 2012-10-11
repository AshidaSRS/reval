(ns reval.ns)

(defmacro eval-in-ns
  "Evaluate body in the context of the supplied namespace"
  [tmp-ns & body]
  `(binding [*ns* ~tmp-ns]
     ~@body))

(defn new-user-ns
  "Create and return a new unique namespace of the form user-<base>-<n>"
  [base]
  (loop [suffix 1]
    (let [ns-symbol (symbol (str "user-" base "-" suffix))]
      (if (find-ns ns-symbol)
        (recur (inc suffix))
        (create-ns ns-symbol)))))
