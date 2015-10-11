(ns defk.core-test
  (:require [clojure.test :refer :all]
            [defk.core :refer :all]))

(deftest defk-tests
  (testing "Call underlying raw function"
    (is (= -1 (foo-raw {:bar 1, :quux 2})))
    (is (= 1 (foo-raw {:bar 2, :quux 1}))))

  (testing "Check that keyword-call is the same as calling directly and that parameters can be reoredered"
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

  (testing "Check that calling with literals and explicit names works"
    (is (= -1 (foo bar=1 quux=2)))
    (is (= 1 (foo bar=2 quux=1))))

  (testing "Check mixed explicit and name binding"
    (let [bar 1] (is (= -1 (foo bar quux=2))))
    (let [quux 2] (is (= -1 (foo bar=1 quux))))))
