(ns postgres-full-text-search-example.core
  (:gen-class)
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs])
  (:import (org.testcontainers.containers PostgreSQLContainer)
           (org.testcontainers.utility DockerImageName MountableFile)))

(def postgres-container
  (delay
    (let [container (PostgreSQLContainer.
                      (DockerImageName/parse "postgres:17-alpine"))]
      (-> container
          (.withCopyFileToContainer
            (MountableFile/forClasspathResource "database/init.sql")
            "/docker-entrypoint-initdb.d/"))
      (.start container)
      container)))

(defn do-things!
  []
  (with-open [database-container (PostgreSQLContainer. "postgres:17")]
    (.start database-container)
    (with-open [database-container @postgres-container
                conn (jdbc/get-connection
                       {:jdbcUrl (.getJdbcUrl database-container)
                        :user (.getUsername database-container)
                        :password (.getPassword database-container)})]
      (jdbc/execute-one!
        conn
        ["select * from articles;"]
        {:return-keys true
         :builder-fn rs/as-unqualified-maps}))))

(defn -main
  []
  (do-things!))
