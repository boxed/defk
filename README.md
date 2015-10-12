[![Build Status](https://travis-ci.org/boxed/defk.svg)](https://travis-ci.org/boxed/defk)

# defk

A Clojure library designed to make keyword arguments not just easier but so much easier in fact that you
might just never use anything else again.

## Usage

Instead of using `defn` to define your function, use `defk`. Now anyone who calls your function gets keyword
argument semantics automatically. An example is easier:

```clojure
(defk foo [a b]
  (- a b))

(let [a 1, b 2]
  (foo b a)) ; <- returns 1 - 2, NOT 2 - 1. How? Because the NAME a and
             ; b are used, not the positions of those variables at the call site
```

You can of course also pass in the value of variables with other names or literals:

```clojure
(let [c 2]
  (foo a=1 b=c))

(foo a=(+ 1 2 3) b=2)
```

The underlying function can be called directly like so:

```clojure
(foo-raw {:a 1, :b 2})
```

## License

Copyright © 2015 Anders Hovmöller

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
