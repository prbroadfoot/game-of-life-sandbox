(ns game-of-life-sandbox.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [game-of-life-sandbox.subs :as subs]
   [game-of-life-sandbox.canvas :refer [Canvas]]))

(defn DBInfo []
  (let [mouse-coords (re-frame/subscribe [:mouse-coords])
        cells (re-frame/subscribe [:cells])]
    (fn []
      [:div
       [:p "Coords" (str @mouse-coords)]
       [:p "Cells" (str @cells)]])))

(defn main-panel []
  [:div
   [Canvas]
   #_[DBInfo]
   [:button {:on-click #(re-frame/dispatch [:toggle-tick])} "Iterate"]
   [:button {:on-click #(re-frame/dispatch [:zoom-out])} "Zoom Out"]
   [:button {:on-click #(re-frame/dispatch [:zoom-in])} "Zoom In"]])
