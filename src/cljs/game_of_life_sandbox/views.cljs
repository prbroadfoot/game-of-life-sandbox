(ns game-of-life-sandbox.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [game-of-life-sandbox.subs :as subs]
   [game-of-life-sandbox.canvas :refer [Canvas]]
   [game-of-life-sandbox.game-of-life :refer [piece-types]]))

(defn DBInfo []
  (let [mouse-coords (re-frame/subscribe [:mouse-coords])
        cells (re-frame/subscribe [:cells])]
    (fn []
      [:div
       [:p "Coords" (str @mouse-coords)]
       [:p "Cells" (str @cells)]])))

(defn iterate-btn []
  (let [tick (re-frame/subscribe [:tick])]
    (fn []
      [:button.button {:on-click #(re-frame/dispatch [:toggle-tick])
                       :style {:width "80px"}}
       (if @tick "Pause" "Start")])))

(defn piece-select []
  [:select {:on-change #(re-frame/dispatch [:change-piece-type
                                            (get piece-types (-> % .-target .-value))])}
   (for [[k v] piece-types]
     [:option {:value k} (str k)])])

(defn main-panel []
  [:div.main-panel
   [:div.button-group
    [iterate-btn]
    [:button.button {:on-click #(re-frame/dispatch [:clear-board])} "Clear"]
    [:button.button {:on-click #(re-frame/dispatch [:zoom-out])} "Zoom Out"]
    [:button.button {:on-click #(re-frame/dispatch [:zoom-in])} "Zoom In"]
    [piece-select]]
   [Canvas]
   #_[DBInfo]
   ])
