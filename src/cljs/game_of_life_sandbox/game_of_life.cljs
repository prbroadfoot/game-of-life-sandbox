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
