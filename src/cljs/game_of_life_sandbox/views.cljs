(ns game-of-life-sandbox.views
  (:require [re-frame.core :as re-frame]
            [game-of-life-sandbox.subs :as subs]
            [game-of-life-sandbox.canvas :as canvas]
            ))

(defn MouseBoardCoords []
  [:p "Mouse board coordinates: " (str @canvas/mouse-board-coords)])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [MouseBoardCoords]
     [:div "Hello from " @name]]))
