(ns rockpick.core)

;; Convert one byte to an integer
(defn byte->int [b]
  (bit-and b 0xFF))

;; Convert 4 bytes in little-endian order to a 32-bit integer
(defn bytes->int32 [b0 b1 b2 b3]
  (+ (bit-shift-left b3 24) (bit-shift-left  b2 16) (bit-shift-left b1 8) b0))

;; Read four bytes in data starting at offset as a 32-bit integer
(defn read-int32 [data offset]
  (let  [[b0 b1 b2 b3] (take 4 (drop offset data))
        i32   (bytes->int32 b0 b1 b2 b3)]
    i32))

(defn split-into
  [n coll]
  (partition (/ (count coll) n) coll))

;; Open the file at path and convert it to a collection of layers.
;; Each layer is a list of rows and each row is a list of tiles.
;; Each tile is a map containing the keys :ch :fg :bg.
;; The value of :ch is a character, and :fg and :bg an rgb map each.
;; Each rgb map is a map with the keys :r :g :b each of which has an associate
;; byte value.
;; Call like (read-xp (clojure.java.io/input-stream "/path/to/file.xp"))
(defn read-xp [in-stream]
  (let [input              (java.util.zip.GZIPInputStream. in-stream)
        data               (with-open [out (java.io.ByteArrayOutputStream.)]
                             (clojure.java.io/copy (clojure.java.io/input-stream input) out)
                             (.toByteArray out))
        #_ (println "read" (count data) "bytes")
        #_ (println "data" (map int data))
        first-val          (read-int32 data 0)
        #_ (println "first-val" first-val)
        layer-count-offset (if (neg? first-val)
                             4
                             0)
        #_ (println "layer-count-offset" layer-count-offset)
        layer-count        (read-int32 data layer-count-offset)
        #_ (println "layer-count" layer-count)
        layer-width-offset (/ (+ 32 (* 8 layer-count-offset)) 8)
        width              (read-int32 data layer-width-offset)
        #_ (println "width" width)
        layer-height-offset (/ (+ (* 8 layer-count-offset) 32 32) 8)
        height              (read-int32 data layer-height-offset)
        #_ (println "height" height)
        first-tile-offset  (/ (+ (* layer-count-offset 8) (* layer-count 64) 32) 8)
        #_ (println "first-tile-offset" first-tile-offset)
        tile-data          (drop first-tile-offset data)
        #_ (println "read" (count tile-data) "bytes of tile data")
        tile-size          10 ;bytes
        #_ (println "tile-size" tile-size "bytes")
        layers             (map (fn [layer]
                                  #_(println "got layer" (count layer) "bytes")
                                  (apply interleave
                                    (map (fn [column]
                                           #_(println "got column" (count column) "bytes")
                                           (map (fn [tile]
                                                  (let [[ch _ _ _ fg-r fg-g fg-b bg-r bg-g bg-b] tile]
                                                    #_(println "got tile" (count tile) "bytes")
                                                    {:ch (char ch)
                                                     :fg {:r (byte->int fg-r) :g (byte->int fg-g) :b (byte->int fg-b)}
                                                     :bg {:r (byte->int bg-r) :g (byte->int bg-g) :b (byte->int bg-b)}}))
                                                (split-into height column)))
                                         (split-into width layer))))
                                (split-into layer-count tile-data))]
    layers))

