(ns game-of-life-sandbox.events
  (:require [re-frame.core :as re-frame]
            [game-of-life-sandbox.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))
