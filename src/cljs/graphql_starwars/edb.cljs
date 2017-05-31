(ns graphql-starwars.edb
  (:require [keechma.toolbox.edb :refer-macros [defentitydb]]))

(def edb-schema
  {:film     {:id :id}
   :species  {:id :id}
   :starship {:id :id}
   :person   {:id :id}
   :planet   {:id :id}
   :vehicle  {:id :id}})

(defentitydb edb-schema)
