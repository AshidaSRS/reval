(ns reval.main-test
  (:use clojure.test
        reval.server
        reval.client))

(deftest simple-remote-repl
  (testing "Simple remote evaluation"
    (let [port   9898
          server (spawn-repl-server port :local true)
          _      (Thread/sleep 500)
          reval  (remote-repl "localhost" port)
          exprs  ['(range 10)
                  '(do (def a (range 5)) a)
                  1234
                  '(let [b (cons 6 a)] b)
                  '(* 9 9)]]
      (is (= (map eval exprs) (map reval exprs))))))
