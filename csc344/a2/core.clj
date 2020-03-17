(ns prolang.core)

(defn andLookup
  [i m]
  (if (= i 'and)
    (get m i 'nor)
    (get m i (list 'nor i)))
  )

(defn not-expression [expr]
  (list 'nor (last expr))
  )

(defn or-expression [expr]
  (list 'nor
        (cons 'nor (rest expr)))
  )

(defn and-expression [expr]
  (map (fn [i] ( andLookup i expr)) expr)
  )

(defn convert [exp]
  (cond
    (= (first exp) 'not) (not-expression exp)
    (= (first exp) 'or) (or-expression exp)
    (= (first exp) 'and) (and-expression exp))

  )

(defn true-expr [exp]
  'false
  )

(defn var-expr [exp] (cond (seq? (last exp))
    (if (= (first (last exp)) (first exp))
      (cond
        (> (count (last (distinct exp))) 2) (distinct exp)
        (= (count (last (distinct exp))) 2) (last (last exp)))
      (last (distinct exp)))
    :else (distinct exp)
    ))

(defn false-expr [exp]
  (if (every? false? (rest exp))
   'true
   (remove false? (distinct exp))
  )
  )

(defn simplify-result [exp]
  (cond
    (some true? exp) (true-expr exp)
    (some false? exp) (false-expr exp)
    :else (var-expr exp)
    )
  )

(defn simplify [exp]
  (simplify-result (map (fn [i]
                   (if (seq? i)
                     (simplify i) i)) exp))
  )

(defn nor-converter
  [l m]
  (convert (map (fn [i]
                  (if (seq? i)
                    (nor-converter i m)
                    (m i i)))
                l)
           )
  )

(defn evalexp
  [exp bindings]
  (println "logical exp       : " exp)
  (println "nor-exp & binding : " (nor-converter exp bindings))
  (simplify(nor-converter exp bindings)))

