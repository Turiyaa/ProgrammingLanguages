(ns prolang.core)

(defn lookup
  [i m]
  (if (= i 'and)
    (get m 'nor 'nor)
    (get m (list 'nor i) (list 'nor i)))
  ;(get m (list 'nor i) (list 'nor i))
  )

; not- expression
(defn not-expression [expr]
  (list 'nor (last expr))
  )

; or-expression
(defn or-expression [expr]
  (list 'nor
        (cons 'nor (rest expr)))
  )
; and-expression
(defn and-expression [expr]
  (map (fn [i] (lookup i expr)) expr)
  )

(defn convert [expr]
  (cond
    (= (first expr) 'not) (not-expression expr)
    (= (first expr) 'or) (or-expression expr)
    (= (first expr) 'and) (and-expression expr))

  )