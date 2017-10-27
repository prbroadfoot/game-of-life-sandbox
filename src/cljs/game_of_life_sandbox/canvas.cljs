(ns game-of-life-sandbox.canvas
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(def context (r/atom nil))

(defn mouse-move-handler [event]
  (let [canvas (.-target event)
        mouse-x (- (.-pageX event) (.-offsetLeft canvas))
        mouse-y (- (.-pageY event) (.-offsetTop canvas))]
    (rf/dispatch [:mouse-moved {:x mouse-x, :y mouse-y}])))

(def Canvas
  (r/create-class
   {:component-did-mount
    (fn [component]
      (reset! context (.getContext (r/dom-node component) "2d")))

    :reagent-render
    (fn [] [:canvas {:width 400
                     :height 400
                     :on-mouse-move mouse-move-handler
                     :on-click #(rf/dispatch [:mouse-clicked])}])}))

(defn draw [color {:keys [x y] :as coords}]
  (let [board (rf/subscribe [:board])
        cell-size (:cell-size @board)]
    (set! (.-fillStyle @context) color)
    (.fillRect @context (* x cell-size) (* y cell-size) cell-size cell-size)))

(rf/reg-fx
 :draw-cell
 (partial draw "red"))

(rf/reg-fx
 :undraw-cell
 (partial draw "white"))
