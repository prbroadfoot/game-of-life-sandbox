(ns game-of-life-sandbox.db
  (:require [game-of-life-sandbox.game-of-life :refer [piece-types]]))

(def default-db
  {:board {:width 1400
           :height 700
           :cell-size 20
           :cells #{}
           :piece-coords #{{:x 0 :y 0}}}
   :canvas-origin {:x 0 :y 0}
   :tick false
   :tick-interval 30})

(defn should-shift-board? [db]
  (let [mouse-window-coords (get-in db [:board :mouse-window-coords])
        mouse-down-window-coords (get db :mouse-down-window-coords)]
    (when (and (:mouse-down db)
               (not= mouse-window-coords mouse-down-window-coords))
      (let [delta (merge-with - mouse-window-coords mouse-down-window-coords)
            new-origin (merge-with + (:canvas-origin db) delta)]
        new-origin))))

(defn mouse-piece-coords [db]
  (let [mouse-coords (get-in db [:board :mouse-coords])
        piece-coords (get-in db [:board :piece-coords])]
    (into #{} (map #(merge-with + mouse-coords %) piece-coords))))
