(ns game-of-life-sandbox.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :mouse-coords
 (fn [db]
   (get-in db [:board :mouse-coords])))

(re-frame/reg-sub
 :board
 (fn [db]
   (:board db)))

(re-frame/reg-sub
 :board-dimensions
 (fn [db]
   (let [width (get-in db [:board :width])
         height (get-in db [:board :height])]
     {:width width, :height height})))

(re-frame/reg-sub
 :cells
 (fn [db]
   (get-in db [:board :cells])))

(re-frame/reg-sub
 :canvas-origin
 (fn [db]
   (get-in db [:canvas-origin])))

(re-frame/reg-sub
 :tick
 (fn [db]
   (get db :tick)))
