# rockpick

A Clojure library designed to ... well, that part is up to you.

## Usage
```clojure
[rockpick "0.1.0-SNAPSHOT"]
```

```clojure
(def layers (rockpick.core/read-xp (clojure.java.io/input-stream "/path/to/file.xp"))

user=> layers
(({:ch \R, :fg {:r 217, :g 0, :b 0}, :bg {:r 51, :g 51, :b 51}} {:ch \R, :fg {:r 217, :g 0, :b 0}, :bg {:r 51, :g 51, :b 51}} {:ch \R, :fg {:r 217, :g 0, :b 0}, :bg {:r 51, :g 51, :b 51}} {:ch \R, :fg {:r 217, :g 0, :b 0}, :bg {:r 51, :g 51, :b 51}} {:ch \R, :fg {:r 217, :g 0, :b 0}, :bg {:r 51, :g 51, :b 51}} {:ch \G, :fg {:r 0, :g 217, :b 0}, :bg {:r 51, :g 51, :b 51}} {:ch \G, :fg {:r 0, :g 217, :b 0}, :bg {:r 51, :g 51, :b 51}} {:ch \G, :fg {:r 0, :g 217, :b 0}, :bg {:r 51, :g 51, :b 51}} {:ch \G, :fg {:r 0, :g 217, :b 0}, :bg {:r 51, :g 51, :b 51}} {:ch \G, :fg {:r 0, :g 217, :b 0}, :bg {:r 51, :g 51, :b 51}} {:ch \b, :fg {:r 0, :g 163, :b 217}, :bg {:r 51, :g 51, :b 51}} {:ch \b, :fg {:r 0, :g 163, :b 217}, :bg {:r 51, :g 51, :b 51}} {:ch \b, :fg {:r 0, :g 163, :b 217}, :bg {:r 51, :g 51, :b 51}} {:ch \b, :fg {:r 0, :g 163, :b 217}, :bg {:r 51, :g 51, :b 51}} {:ch \b, :fg {:r 0, :g 163, :b 217}, :bg {:r 51, :g 51, :b 51}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}} {:ch \space, :fg {:r 0, :g 0, :b 0}, :bg {:r 0, :g 0, :b 0}}))
```
## License

GNU Lesser GPL v3.0
