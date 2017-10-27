(ns game-of-life-sandbox.canvas
  (:require [reagent.core :as r]))

(def canvas-properties
  {:width 400
   :height 400
   :grid-unit-size 20})

(defn get-mouse-pos [canvas event]
  (let [mouse-x (- (.-pageX event) (.-offsetLeft canvas))
        mouse-y (- (.-pageY event) (.-offsetTop canvas))]
    {:x mouse-x, :y mouse-y}))

(defn get-mouse-board-coords [canvas event]
  (let [{:keys [x y]} (get-mouse-pos canvas event)
        grid-unit-size (:grid-unit-size canvas-properties)]
    {:x (quot x grid-unit-size)
     :y (quot y grid-unit-size)}))

(def mouse-board-coords (r/atom nil))

(defn draw-cell [canvas context event]
  (let [{:keys [x y]} (get-mouse-board-coords canvas event)
        grid-unit-size (:grid-unit-size canvas-properties)]
    (set! (.-fillStyle context) "red")
    (.fillRect context (* x grid-unit-size) (* y grid-unit-size) grid-unit-size grid-unit-size)))

(defn add-event-handlers [canvas context]
  (set! (.-onclick canvas) #(draw-cell canvas context %)))

(defn get-canvas-context []
  (let [canvas (js/document.querySelector "canvas")
        context (.getContext canvas "2d")]
    (set! (.-height canvas) (:width canvas-properties))
    (set! (.-width canvas) (:height canvas-properties))
    (set! (.-onmousemove canvas) #(reset! mouse-board-coords (get-mouse-board-coords canvas %)))
    (add-event-handlers canvas context)
    (.getContext canvas "2d")))

(def canvas (get-canvas-context))


