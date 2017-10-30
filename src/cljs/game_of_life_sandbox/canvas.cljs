(ns game-of-life-sandbox.canvas
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(def context (r/atom nil))

(defn mouse-move-handler [event]
  (let [canvas (.-target event)
        mouse-x (- (.-pageX event) (.-offsetLeft canvas))
        mouse-y (- (.-pageY event) (.-offsetTop canvas))]
    (rf/dispatch [:mouse-moved {:x mouse-x, :y mouse-y}])))

(defn Canvas
  []
  (r/create-class
   {:component-did-mount
    (fn [component]
      (reset! context (.getContext (r/dom-node component) "2d")))

    :reagent-render
    (let [board-dimensions (rf/subscribe [:board-dimensions])]
      (fn [] [:canvas {:width (:width @board-dimensions)
                       :height (:height @board-dimensions)
                       :on-mouse-move mouse-move-handler
                       :on-click #(rf/dispatch [:mouse-clicked])
                       :on-mouse-down #(rf/dispatch [:mouse-down])
                       :on-mouse-up #(rf/dispatch [:mouse-up])}]))}))

(defn draw [color {:keys [x y] :as coords}]
  (let [board (rf/subscribe [:board])
        cell-size (:cell-size @board)
        canvas-origin (rf/subscribe [:canvas-origin])]
    (set! (.-fillStyle @context) color)
    (.fillRect @context
               (* (+ x (:x @canvas-origin)) cell-size)
               (* (+ y (:y @canvas-origin)) cell-size)
               cell-size
               cell-size)))

(defn clear-board []
  (let [board (rf/subscribe [:board])]
    (set! (.-fillStyle @context) "white")
    (.fillRect @context 0 0 (:width @board) (:height @board))))

(defn draw-cells [cells]
  (clear-board)
  (doseq [cell cells]
    (draw "red" cell)))

(rf/reg-fx
 :draw-cell
 (partial draw "red"))

(rf/reg-fx
 :undraw-cell
 (partial draw "white"))

(rf/reg-fx
 :draw-cells
 draw-cells)
