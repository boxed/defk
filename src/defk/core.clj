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

(defn keyword-pairs [args]
  (loop [args args
         result []]
    (let [arg (first args)
          matched (re-matches  #"(.+)=(.*)" (str arg))]
      (if (= arg nil)
        result
        (if matched
          (let [[_ left right] matched]
            (if (not= right "")
              (recur (rest args) (conj result [(keyword left) (read-string right)]))
              (recur (drop 2 args) (conj result [(keyword left) (first (rest args))]))))
          (recur (rest args) (conj result [(keyword arg) (symbol arg)])))
        ))))

(defmacro keyword-call [name & args]
  (list (symbol (str name "-raw"))
        (into {} (keyword-pairs args))))

(defmacro defk [name args & body]
  (list 'do
    (list 'def (symbol (str name "-raw")) (list* 'fn [{:keys args}] body))
    `(defmacro ~name [& ~(symbol "args")]
       (list* (quote keyword-call) (quote ~name) ~(symbol "args")))))
