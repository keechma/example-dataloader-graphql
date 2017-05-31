(ns graphql-starwars.ui.main
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [route> sub> <cmd]]))

(def columns
  [[:films "Films"]
   [:species "Species"]
   [:starships "Starships"]
   [:people "People"]
   [:planets "Planets"]
   [:vehicles "Vehicles"]])

(defn toggle-column [ctx current-route key]
  (let [column (name key)
        checked-columns (set (:columns current-route))
        new-columns (if (contains? checked-columns column)
                      (filter #(not= % column) checked-columns)
                      (conj checked-columns column))]
    (ui/redirect ctx (assoc current-route :columns new-columns))))

(defn render-navbar [ctx]
  (let [current-route (route> ctx)
        checked-columns (set (:columns current-route))]
    [:nav.border.navbar.navbar-light.bg-faded
     [:div
      (doall (map (fn [[key title]]
                    (let [checked? (contains? checked-columns (name key))]
                      [:label.mr-4 {:key key}
                       [:input
                        {:type :checkbox
                         :checked checked?
                         :on-change #(toggle-column ctx current-route key)}]
                       [:span.ml-1.d-inline-block title]])) columns))]]))

(defn render-column [ctx content]
  [:ul.list-group.list-group-flush
   (doall
    (map (fn [item]
             [:li.list-group-item {:key (:id item)}
              (or (:name item) (:title item))])
         content))])

(defn render [ctx]
  (let [current-route (route> ctx)
        checked-columns (set (:columns current-route))]
    [:div
     [render-navbar ctx]
     [:div.container-fluid.mt-4
      [:div.row
       (doall
        (map (fn [[key title]]
               (when (contains? checked-columns (name key))
                 [:div.col {:key key}
                  [:div.card
                   [:div.card-block
                    [:h4.card-title title]]
                   [render-column ctx (sub> ctx key)]]])) columns))]]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:films
                                       :species
                                       :starships
                                       :people
                                       :planets
                                       :vehicles]}))
