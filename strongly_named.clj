;; Function call:

;;     (foo bar baz)
;;     (foo :bar 1 :baz 2)

;; expands to:

;;     (foo {:bar bar, :baz baz})
;;     (foo {:bar 1, :baz 2})

;; Function definition:

;;     (defk foo [bar baz]
;;         ...)

;; expands to:

;;     (defn foo [{:keys [bar baz]}]
;;         ...)

(defmacro keyword-call [name & args]
  (list (symbol (str name "-raw")) (into {} (for [arg# args] [(keyword arg#) (symbol arg#)]))))


(defmacro defk [name args & body]
  (list 'do
    (list 'def (symbol (str name "-raw")) (list* 'fn [{:keys args}] body))
    `(defmacro ~name [& ~(symbol "args")]
       (list* (quote keyword-call) (quote ~name) ~(symbol "args")))))


(defk foo [bar quux]
  (- bar quux))

(assert (= -1 (foo-raw {:bar 1, :quux 2})))
(assert (= 1 (foo-raw {:bar 2, :quux 1})))

(let [bar 1, quux 2]
  (assert (= -1 (keyword-call foo bar quux)))
  (assert (= -1 (foo bar quux)))
  (assert (= -1 (keyword-call foo quux bar)))
  (assert (= -1 (foo quux bar))))

(let [bar 2, quux 1]
  (assert (= 1 (keyword-call foo bar quux)))
  (assert (= 1 (foo bar quux)))
  (assert (= 1 (keyword-call foo quux bar)))
  (assert (= 1 (foo quux bar))))
