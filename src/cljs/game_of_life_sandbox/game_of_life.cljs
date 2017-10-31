(ns game-of-life-sandbox.game-of-life)

(defn neighbours
  [{:keys [x y]}]
  (for [dx [-1 0 1] dy [-1 0 1] :when (not= 0 dx dy)]
    {:x (+ dx x), :y (+ dy y)}))

(defn step
  "Yields the next state of the world"
  [cells]
  (set (for [[loc n] (frequencies (mapcat neighbours cells))
             :when (or (= n 3) (and (= n 2) (cells loc)))]
         loc)))

(defn vecs->maps [coords]
  (into #{}
        (map
         (fn [coords] {:x (first coords) :y (second coords)})
          coords)))

(def piece-types
  {"cell" #{{:x 0 :y 0}}
   "glider" #{{:x 0 :y 0} {:x 0 :y 1} {:x 0 :y 2} {:x -1 :y 2} {:x -2 :y 1}}

   "pulsar"
    (vecs->maps #{[1 2] [1 3] [1 4] [2 1] [3 1] [4 1]
                  [-1 2] [-1 3] [-1 4] [2 -1] [3 -1] [4 -1]
                  [1 -2] [1 -3] [1 -4] [-2 1] [-3 1] [-4 1]
                  [-1 -2] [-1 -3] [-1 -4] [-2 -1] [-3 -1] [-4 -1]
                  [-6 2] [-6 3] [-6 4] [6 2] [6 3] [6 4]
                  [-6 -2] [-6 -3] [-6 -4] [6 -2] [6 -3] [6 -4]
                  [2 -6] [3 -6] [4 -6] [-2 -6] [-3 -6] [-4 -6]
                  [2 6] [3 6] [4 6] [-2 6] [-3 6] [-4 6]})
   })
