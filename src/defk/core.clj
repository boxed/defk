(ns defk.core)

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
  (list (symbol (str name "-raw")) (into {} (for [arg# args]
                                              (let [matched (re-matches  #"(.+)=(.+)" (str arg#))]
                                                (if matched
                                                  (let [[_ left right] matched]
                                                    [(keyword left) (read-string right)])
                                                  [(keyword arg#) (symbol arg#)])
                                              )))))


(defmacro defk [name args & body]
  (list 'do
    (list 'def (symbol (str name "-raw")) (list* 'fn [{:keys args}] body))
    `(defmacro ~name [& ~(symbol "args")]
       (list* (quote keyword-call) (quote ~name) ~(symbol "args")))))


(defk foo [bar quux]
  (- bar quux))
