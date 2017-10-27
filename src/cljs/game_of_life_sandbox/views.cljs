(ns game-of-life-sandbox.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [game-of-life-sandbox.subs :as subs]
   [game-of-life-sandbox.canvas :refer [Canvas context]]))

(defn MouseCoords []
  (let [mouse-coords (re-frame/subscribe [:mouse-coords])]
    (fn []
      [:p "Coords" (str @mouse-coords)])))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [Canvas]
     [MouseCoords]
     [:button {:on-click (fn []
                           (.fillRect @context 20 20 20 20))} "click"]
     [:div "Hello from " @name]]))
