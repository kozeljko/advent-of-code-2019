(ns day03.core
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.math.numeric-tower :as math])
  (:require [clojure.set]))

(defn extractInt [reading]
  (Integer. (re-find #"\d+" reading))
)

(defn extractAndOne [reading]
  (+ 1 (extractInt reading))
)

(defn calcCoords [reading [a b]]
  (cond
    (re-matches #"U.*" reading) (map #(vec [a (+ b %1) ]) (range 1 (extractAndOne reading)))
    (re-matches #"D.*" reading) (map #(vec [a (- b %1) ]) (range 1 (extractAndOne reading)))
    (re-matches #"L.*" reading) (map #(vec [(- a %1) b ]) (range 1 (extractAndOne reading)))
    (re-matches #"R.*" reading) (map #(vec [(+ a %1) b ]) (range 1 (extractAndOne reading)))
    :else [0 0])
)

(defn readStuff [fileName]
  (with-open [r  (io/reader fileName)]
    (doall (line-seq r)))
)

(defn parseToCoords [vec]
  (reduce #(concat %1 (calcCoords %2 (last %1))) '([0 0]) vec)
)

(defn findPairs [list]
  (clojure.set/intersection (set (first list)) (set (first (rest list))))
)

(defn findMinDistance [list]
  (reduce 
    (fn [min [a b]]
      (let 
        [manhattan (+ (Math/abs a) (Math/abs b))]
        (if (and (< manhattan min) (not= a 0) (not= b 0))
          manhattan
          min
        )
      )
    )
    1000000000000000
    list
  )
)

(defn parseStuffOne [fileName]
  (findMinDistance
    (findPairs
      (map #(parseToCoords %1)
        (map #(str/split % #",") (readStuff fileName))
      )
    )
  )
)

(defn countSteps [target path]
  (first (keep-indexed (fn [idx item] (if (= item target) idx)) path))
)

(defn countStepsAll [intersections paths]

  (reduce 
    (fn [agg item]
      (let 
        [oneCount (countSteps item (first paths))
         twoCount (countSteps item (first (rest paths)))]
        (if (and (< (+ oneCount twoCount) agg) (not= [0 0] item))
          (+ oneCount twoCount)   
          agg
        )
      )
    )
    1000000000000000000
    (vec intersections)
  )
)

(defn parseStuffTwo [fileName]
  (def paths 
    (map #(parseToCoords %1)
      (map #(str/split % #",") (readStuff fileName))
    )
  )

  (def intersections (findPairs paths))

  (countStepsAll intersections paths) 
)

(def fileName "resources/input.txt")

(defn -main [& args]
  (println "Part1 result:" (parseStuffOne fileName))
  (println "Part2 result:" (parseStuffTwo fileName))
)