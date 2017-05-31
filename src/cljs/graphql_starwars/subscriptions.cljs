(ns graphql-starwars.subscriptions
  (:require [keechma.toolbox.dataloader.subscriptions :refer [make-subscriptions]]
            [graphql-starwars.datasources :refer [datasources]]
            [graphql-starwars.edb :refer [edb-schema]]))

(def subscriptions (make-subscriptions datasources edb-schema))
