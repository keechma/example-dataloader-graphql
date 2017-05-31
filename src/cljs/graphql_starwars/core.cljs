(ns graphql-starwars.core
  (:require-macros
   [reagent.ratom :refer [reaction]])
  (:require
   [reagent.core :as reagent]
   [keechma.app-state :as app-state]
   [graphql-starwars.controllers :refer [controllers]]
   [graphql-starwars.ui :refer [ui]]
   [graphql-starwars.subscriptions :refer [subscriptions]]))

(def app-definition
  {:components    ui 
   :controllers   controllers 
   :subscriptions subscriptions 
   :html-element  (.getElementById js/document "app")})

(defonce running-app (clojure.core/atom))

(defn start-app! []
  (reset! running-app (app-state/start! app-definition)))

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn reload []
  (let [current @running-app]
    (if current
      (app-state/stop! current start-app!)
      (start-app!))))

(defn ^:export main []
  (dev-setup)
  (start-app!))
