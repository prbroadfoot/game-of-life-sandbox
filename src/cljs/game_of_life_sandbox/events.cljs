(ns game-of-life-sandbox.events
  (:require [re-frame.core :as re-frame]
            [game-of-life-sandbox.db :as db]
            [game-of-life-sandbox.game-of-life :refer [step]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-fx
 :mouse-moved
 (fn  [{:keys [db]} [_ {:keys [x y]}]]
   (let [cell-size (get-in db [:board :cell-size])
         mouse-window-coords {:x (quot x cell-size), :y (quot y cell-size)}
         mouse-coords (merge-with - mouse-window-coords (:canvas-origin db))
         new-db (-> db
                    (assoc-in [:board :mouse-coords] mouse-coords)
                    (assoc-in [:board :mouse-window-coords] mouse-window-coords))]
     (if-let [new-origin (db/should-shift-board? new-db)]
       {:db new-db
        :dispatch [:change-canvas-origin new-origin]}
       {:db new-db}))))

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

(re-frame/reg-event-db
 :mouse-down
 (fn [db]
   (let [mouse-window-coords (get-in db [:board :mouse-window-coords])]
     (assoc db :mouse-down true
            :mouse-down-window-coords mouse-window-coords
            :shift false))))

(re-frame/reg-event-db
 :mouse-up
 (fn [db _]
   (assoc db :mouse-down false)))

(re-frame/reg-event-fx
 :mouse-clicked
 (fn  [{:keys [db]} _]
   (let [mouse-coords (get-in db [:board :mouse-coords])
         occupied? (get-in db [:board :cells mouse-coords])]
     ;; don't create a cell when clicking+dragging was used to pan the board
     (when-not (:shift db)
       (if occupied?
         {:dispatch [:remove-cell mouse-coords]}
         {:dispatch [:add-cell mouse-coords]})))))

(defn in-bounds? [board cell]
  (let [max-x (quot (:width board) 4)
        max-y (quot (:height board) 5)]
    (and (< (Math/abs (:x cell)) max-x)
         (< (Math/abs (:y cell)) max-y))))

(re-frame/reg-event-fx
 :toggle-tick
 (fn [{:keys [db]} _]
   (let [fx {:db (update db :tick not)}]
     (if (:tick db)
       fx
       (assoc fx :dispatch [:iterate-cells])))))

(re-frame/reg-event-fx
 :iterate-cells
 (fn [{:keys [db]} _]
   (let [tick (:tick db)
         board (:board db)
         next-cells (->> (step (get-in db [:board :cells]))
                         (filter (partial in-bounds? board))
                         (into #{}))]
     (when tick
       {:db (assoc-in db [:board :cells] next-cells)
        :draw-cells next-cells
        :dispatch-later [{:ms (:tick-interval db)
                          :dispatch [:iterate-cells]}]}))))

(re-frame/reg-event-fx
 :zoom-out
 (fn [{:keys [db]} _]
   {:db (update-in db [:board :cell-size] #(max (quot % 1.2) 4))
    :draw-cells (get-in db [:board :cells])}))

(re-frame/reg-event-fx
 :zoom-in
 (fn [{:keys [db]} _]
   {:db (update-in db [:board :cell-size] #(Math/ceil (* % 1.2)))
    :draw-cells (get-in db [:board :cells])}))

(re-frame/reg-event-fx
 :change-canvas-origin
 (fn [{:keys [db]} [_ new-origin]]
   {:db (assoc db :canvas-origin new-origin
               :mouse-down-window-coords (get-in db [:board :mouse-window-coords])
               :shift true)
    :draw-cells (get-in db [:board :cells])}))
