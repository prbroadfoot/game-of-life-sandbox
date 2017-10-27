(ns game-of-life-sandbox.events
  (:require [re-frame.core :as re-frame]
            [game-of-life-sandbox.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 :mouse-moved
 (fn  [db [_ {:keys [x y]}]]
   (let [cell-size (get-in db [:board :cell-size])
         board-x (quot x cell-size)
         board-y (quot y cell-size)]
     (assoc-in db [:board :mouse-coords] {:x board-x, :y board-y}))))

(re-frame/reg-event-fx
 :mouse-clicked
 (fn  [cofx _]
   (let [mouse-coords (get-in cofx [:db :board :mouse-coords])]
     {:draw-cell mouse-coords})))
