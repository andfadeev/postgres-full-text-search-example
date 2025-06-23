(defproject postgres-full-text-search-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.1"]
                 [com.github.seancorfield/next.jdbc "1.3.1048"]
                 [com.github.seancorfield/honeysql "2.7.1310"]
                 [org.postgresql/postgresql "42.7.7"]
                 [org.clojure/tools.logging "1.3.0"]
                 [org.slf4j/slf4j-api "2.1.0-alpha1"]
                 [org.slf4j/slf4j-simple "2.1.0-alpha1"]]
  :main ^:skip-aot postgres-full-text-search-example.core
  :target-path "target/%s"
  :plugins [[com.github.liquidz/antq "2.11.1276"]]
  :antq {}
  :profiles {:dev {:dependencies [[com.stuartsierra/component.repl "1.0.0"]
                                  [nrepl/nrepl "1.3.1"]
                                  [org.testcontainers/testcontainers "1.21.2"]
                                  [org.testcontainers/postgresql "1.21.2"]]
                   :source-paths ["dev"]}
             :kaocha {:dependencies [[lambdaisland/kaocha "1.91.1392"]]}
             :uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
