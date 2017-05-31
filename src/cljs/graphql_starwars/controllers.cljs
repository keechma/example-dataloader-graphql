(ns graphql-starwars.controllers
  (:require [graphql-starwars.datasources :refer [datasources]]
            [graphql-starwars.edb :refer [edb-schema]]
            [keechma.toolbox.dataloader.controller :as dataloader-controller]))


(def controllers
  (-> {}
      (dataloader-controller/register datasources edb-schema)))
