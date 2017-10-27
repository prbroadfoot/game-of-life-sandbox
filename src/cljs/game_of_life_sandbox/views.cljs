(ns game-of-life-sandbox.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [game-of-life-sandbox.subs :as subs]
   [game-of-life-sandbox.canvas :refer [Canvas context]]))

(defn DBInfo []
  (let [mouse-coords (re-frame/subscribe [:mouse-coords])
        cells (re-frame/subscribe [:cells])]
    (fn []
      [:div
       [:p "Coords" (str @mouse-coords)]
       [:p "Cells" (str @cells)]])))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [Canvas]
     [DBInfo]
     [:button #_{:on-click #(re-frame/dispatch [:resize-board
                                              {:width 200 :height 200}])} "click"]
     [:div "Hello from " @name]]))
