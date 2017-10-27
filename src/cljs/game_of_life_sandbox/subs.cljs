(ns game-of-life-sandbox.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 :mouse-coords
 (fn [db]
   (get-in db [:board :mouse-coords])))

(re-frame/reg-sub
 :board
 (fn [db]
   (:board db)))

(re-frame/reg-sub
 :cells
 (fn [db]
   (get-in db [:board :cells])))
