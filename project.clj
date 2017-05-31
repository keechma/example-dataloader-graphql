(defproject graphql-starwars "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0-alpha15"]
                 [org.clojure/clojurescript "1.9.229"]
                 [reagent "0.6.1"]
                 [keechma/toolbox "0.0.1-SNAPSHOT-4"]
                 [binaryage/devtools "0.8.2"]
                 [funcool/promesa "1.8.1"]
                 [cljs-ajax "0.5.8"]
                 [floatingpointio/graphql-builder "0.1.2"]
                 [medley "1.0.0"]
                 [keechma "0.2.0-SNAPSHOT-11" :exclusions [cljsjs/react-with-addons]]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.4"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles
  {:dev
   {:dependencies []

    :plugins      [[lein-figwheel "0.5.10"]]
    }}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "graphql-starwars.core/reload"}
     :compiler     {:main                 graphql-starwars.core
                    :optimizations        :none
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/dev"
                    :asset-path           "js/compiled/dev"
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            graphql-starwars.core
                    :optimizations   :advanced
                    :output-to       "resources/public/js/compiled/app.js"
                    :output-dir      "resources/public/js/compiled/min"
                    :elide-asserts   true
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    ]})
