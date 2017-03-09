(ns rockpick.core)
;; http://www.gridsagegames.com/rexpaint/manual.txt

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

(defn write-byte [out b]
  (.write out (int b))
  out)

(defn write-int32 [out i]
  (let [b0 (bit-shift-right (bit-and 0xFF000000 i) 24)
        b1 (bit-shift-right (bit-and 0x00FF0000 i) 16)
        b2 (bit-shift-right (bit-and 0x0000FF00 i) 8)
        b3 (bit-and 0x000000FF i)]
    (-> out
      (write-byte b3)
      (write-byte b2)
      (write-byte b1)
      (write-byte b0))))

(defn write-character
  [out character]
  {:pre [(every? character [:ch :fg :bg])]}
  (-> out
    (write-int32 (int (get character :ch)))
    (write-byte (get-in character [:fg :r]))
    (write-byte (get-in character [:fg :g]))
    (write-byte (get-in character [:fg :b]))
    (write-byte (get-in character [:bg :r]))
    (write-byte (get-in character [:bg :g]))
    (write-byte (get-in character [:bg :b]))))

(defn split-into
  [n coll]
  (partition (/ (count coll) n) coll))

(defn transpose
  [m]
  (apply mapv vector m))

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
        first-val          (read-int32 data 0)
        layer-count-offset (if (neg? first-val)
                             4
                             0)
        layer-count        (read-int32 data layer-count-offset)
        layer-width-offset (/ (+ 32 (* 8 layer-count-offset)) 8)
        width              (read-int32 data layer-width-offset)
        tile-data          (drop (+ layer-count-offset 4) data)
        tile-size          10 ;bytes
        layers             (mapv (fn [layer]
                                  (let [width  (read-int32 layer 0)
                                        height (read-int32 layer 4)
                                        layer-data (drop 8 layer)]
                                    (transpose
                                      (mapv (fn [column]
                                             (mapv (fn [tile]
                                                    (let [[ch _ _ _ fg-r fg-g fg-b bg-r bg-g bg-b] tile]
                                                      {:ch (char (byte->int ch))
                                                       :fg {:r (byte->int fg-r) :g (byte->int fg-g) :b (byte->int fg-b)}
                                                       :bg {:r (byte->int bg-r) :g (byte->int bg-g) :b (byte->int bg-b)}}))
                                                  (split-into height column)))
                                           (split-into width layer-data)))))
                                (split-into layer-count tile-data))]
    layers))

;; Write the collection of layers to out-strem
;; Each layer is a list of rows and each row is a list of tiles.
;; Each tile is a map containing the keys :ch :fg :bg.
;; The value of :ch is a character, and :fg and :bg an rgb map each.
;; Each rgb map is a map with the keys :r :g :b each of which has an associate
;; byte value.
;; Call like (read-xp (clojure.java.io/input-stream "/path/to/file.xp"))
(defn write-xp [out-stream layers]
  {:pre [(instance? java.io.OutputStream out-stream)
         (sequential? layers)
         (every? sequential? layers)]}
  (with-open [out (java.util.zip.GZIPOutputStream. out-stream)]
    (-> out
      ;; write version
      (write-int32 -16843009)
      ;; write number of layers
      (write-int32 (count layers)))
    ;; foreach layer
    (doseq [layer layers]
      (-> out
        ;; write width
        (write-int32 (count (first layer)))
        ;; write height
        (write-int32 (count layer)))
      ;; transpose layer
      (doseq [line      (transpose layer)
              character line]
        ;; write each character in layer
        (write-character out character)))
    (.flush out)))

