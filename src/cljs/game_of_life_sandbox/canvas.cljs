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

;; (def canvas (js/document.querySelector "canvas"))
;; (def context (.getContext canvas "2d"))

;; (defn get-mouse-pos [event]
;;   (let [mouse-x (- (.-pageX event) (.-offsetLeft canvas))
;;         mouse-y (- (.-pageY event) (.-offsetTop canvas))]
;;     {:x mouse-x, :y mouse-y}))


;; TODO fix this
;; (set! (.-height canvas) 400)
;; (set! (.-width canvas) 400)

;; (set! (.-onmousemove canvas) #(rf/dispatch [:mouse-moved (get-mouse-pos %)]))
;; (set! (.-onclick canvas) #(rf/dispatch [:mouse-clicked]))

(defn draw-cell [{:keys [x y] :as board-coords} cell-size]
  (set! (.-fillStyle @context) "red")
  (.fillRect @context (* x cell-size) (* y cell-size) cell-size cell-size))

(rf/reg-fx
 :draw-cell
 (fn [coords]
   (let [board (rf/subscribe [:board])
         cell-size (:cell-size @board)]
     (draw-cell coords cell-size)))) ;; TODO don't hardcode cell-size value

;; (def canvas-properties
;;   {:width 400
;;    :height 400
;;    :grid-unit-size 20})

;; (def mouse-board-coords (r/atom nil))

;; (defn get-mouse-board-coords [event]
;;   (let [{:keys [x y]} (get-mouse-pos event)
;;         grid-unit-size (:grid-unit-size canvas-properties)]
;;     {:x (quot x grid-unit-size)
;;      :y (quot y grid-unit-size)}))

;; (defn draw-cell [board-coords]
;;   (let [{:keys [x y]} board-coords
;;         grid-unit-size (:grid-unit-size canvas-properties)]
;;     (set! (.-fillStyle context) "red")
;;     (.fillRect context (* x grid-unit-size) (* y grid-unit-size) grid-unit-size grid-unit-size)))

;; (defn add-canvas-properties []
;;   (set! (.-height canvas) (:height canvas-properties))
;;   (set! (.-width canvas) (:width canvas-properties))
;;   (set! (.-onmousemove canvas) #(reset! mouse-board-coords (get-mouse-board-coords canvas %)))
;;   (set! (.-onclick canvas) #(draw-cell canvas context %)))

;; (add-canvas-properties)
