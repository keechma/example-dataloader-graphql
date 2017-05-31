(ns graphql-starwars.datasources
  (:require [graphql-builder.parser :refer-macros [defgraphql]]
            [graphql-builder.core :as gql-core]
            [promesa.core :as p]
            [keechma.toolbox.ajax :refer [POST]]
            [clojure.string :as str]))

(defgraphql graphql "resources/graphql/queries.graphql")

(def gql-endpoint "https://swapi.apis.guru/")

(defn gql-results-handler [unpack]
  (fn [{:keys [data errors]}]
    (if errors
      (throw (ex-info "GraphQLError" errors))
      (unpack data))))

(defn gql-req [params]
  (->> (POST gql-endpoint
             {:format :json
              :params (:graphql params) 
              :response-format :json
              :keywords? true})
       (p/map (gql-results-handler (:unpack params)))))

(defn graphql-loader [reqs]
  (let [params (map (fn [req] (when (:params req) (assoc (:params req) :id (keyword (gensym "req"))))) reqs)
        clean-params (remove nil? params)]
    (if (seq clean-params)
      (let [queries (reduce (fn [acc p] (assoc acc (:id p) (:query p))) {} clean-params)
            variables (reduce (fn [acc p] (assoc acc (:id p) (:variables p))) {} clean-params)
            composed-fn (gql-core/composed-query graphql queries)
            req-promise (gql-req (composed-fn variables))]
           (map (fn [param]
                  (when param
                    (p/map #(get % (:id param)) req-promise))) params))
      params)))

(defn result-extract [resource]
  (let [query-name (str "all" (str/capitalize resource))]
    (fn [res]
      {:meta {:count (get-in res [query-name :totalCount])}
       :data (get-in res [query-name (keyword resource)])})))

(defn make-params [resource]
  (fn [_ {:keys [columns]} _]
    (when (contains? (set columns) resource)
      {:query (str "Load" (str/capitalize resource))
       :variables {}})))

(def datasources
  {:films {:target    [:edb/collection :film/list]
           :params    (make-params "films") 
           :loader    graphql-loader
           :processor (result-extract "films")}

   :species {:target    [:edb/collection :species/list]
             :params    (make-params "species") 
             :loader    graphql-loader
             :processor (result-extract "species")}

   :starships {:target    [:edb/collection :starship/list]
               :params    (make-params "starships") 
               :loader    graphql-loader
               :processor (result-extract "starships")}

   :people {:target    [:edb/collection :person/list]
            :params    (make-params "people") 
            :loader    graphql-loader
            :processor (result-extract "people")}

   :planets {:target    [:edb/collection :planet/list]
             :params    (make-params "planets") 
             :loader    graphql-loader
             :processor (result-extract "planets")}

   :vehicles {:target    [:edb/collection :vehicle/list]
              :params    (make-params "vehicles") 
              :loader    graphql-loader
              :processor (result-extract "vehicles")}})
