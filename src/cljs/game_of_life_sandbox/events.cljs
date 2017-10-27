(ns game-of-life-sandbox.events
  (:require [re-frame.core :as re-frame]
            [game-of-life-sandbox.db :as db]
            [game-of-life-sandbox.game-of-life :refer [step]]))

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
 :add-cell
 (fn [{:keys [db]} [_ coords]]
   {:db (update-in db [:board :cells] conj coords)
    :draw-cell coords}))

(re-frame/reg-event-fx
 :remove-cell
 (fn [{:keys [db]} [_ coords]]
   {:db (update-in db [:board :cells] disj coords)
    :undraw-cell coords}))

(re-frame/reg-event-fx
 :mouse-clicked
 (fn  [{:keys [db]} _]
   (let [mouse-coords (get-in db [:board :mouse-coords])
         occupied? (get-in db [:board :cells mouse-coords])]
     (if occupied?
       {:dispatch [:remove-cell mouse-coords]}
       {:dispatch [:add-cell mouse-coords]}))))

(defn in-bounds? [board cell]
  (let [max-x (quot (:width board) (:cell-size board))
        max-y (quot (:height board) (:cell-size board))]
    (and (< (:x cell) max-x)
         (< (:y cell) max-y))))

(re-frame/reg-event-fx
 :iterate-cells
 (fn [{:keys [db]} _]
   (let [board (:board db)
         next-cells (->> (step (get-in db [:board :cells]))
                         (filter #(in-bounds? board %))
                         (into #{}))]
     {:db (assoc-in db [:board :cells] next-cells)
      :draw-cells next-cells})))
