(ns defk.core-test
  (:require [clojure.test :refer :all]
            [defk.core :refer :all]))

(defk foo [bar quux]
  (- bar quux))

(deftest defk-tests
  (testing "underlying raw function"
    (is (= -1 (foo-raw {:bar 1, :quux 2})))
    (is (= 1 (foo-raw {:bar 2, :quux 1}))))

  (testing "that keyword-call is the same as calling directly and that parameters can be reoredered"
    (let [bar 1, quux 2]
      (is (= -1 (keyword-call foo bar quux)))
      (is (= -1 (foo bar quux)))
      (is (= -1 (keyword-call foo quux bar)))
      (is (= -1 (foo quux bar))))

    (let [bar 2, quux 1]
      (is (= 1 (keyword-call foo bar quux)))
      (is (= 1 (foo bar quux)))
      (is (= 1 (keyword-call foo quux bar)))
      (is (= 1 (foo quux bar)))))

  (testing "that calling with literals and explicit names works"
    (is (= -1 (foo bar=1 quux=2)))
    (is (= 1 (foo bar=2 quux=1))))

  (testing "mixed explicit and name binding"
    (let [bar 1] (is (= -1 (foo bar quux=2))))
    (let [quux 2] (is (= -1 (foo bar=1 quux)))))

  (testing "expressions as params"
    (is (= -1 (foo bar=(+ 0 1) quux=(+ 0 2)))))

  (testing "partial-keyword"
    (is (= -1 ((partial-keyword foo-raw {:bar 1}) {:quux 2})))
    (is (= -1 ((partial-keyword foo-raw {:bar 1, :quux 8}) {:quux 2})))
   )
)
