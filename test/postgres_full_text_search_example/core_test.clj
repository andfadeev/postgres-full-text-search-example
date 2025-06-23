(ns postgres-full-text-search-example.core-test
  (:require [clojure.test :refer :all]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [honey.sql :as sql])
  (:import (org.testcontainers.containers PostgreSQLContainer)))

(defn create-database-container
  []
  (PostgreSQLContainer. "postgres:17"))

#_(deftest full-text-search-test
  (let [database-container (create-database-container)]
    (try
      (.start database-container)
      (let [db-spec {:jdbcUrl (.getJdbcUrl database-container)
                     :username (.getUsername database-container)
                     :password (.getPassword database-container)}])

      (with-system
        [sut (datasource-only-system
               {:db-spec })]
        (let [{:keys [datasource]} sut
              select-query (sql/format {:select :*
                                        :from :schema-version})
              [schema-version :as schema-versions]
              (jdbc/execute!
                (datasource)
                select-query
                {:builder-fn rs/as-unqualified-lower-maps})]
          (is (= ["SELECT * FROM schema_version"]
                 select-query))
          (is (= 1 (count schema-versions)))
          (is (= {:description "add todo tables"
                  :script "V1__add_todo_tables.sql"
                  :success true}
                 (select-keys schema-version [:description :script :success])))))
      (finally
        (.stop database-container)))))