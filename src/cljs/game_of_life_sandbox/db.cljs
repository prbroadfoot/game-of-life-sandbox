(ns game-of-life-sandbox.db)

(def default-db
  {:board {:width 1400
           :height 700
           :cell-size 20
           :cells #{}}
   :canvas-origin {:x 0 :y 0}
   :tick false
   :tick-interval 30})
