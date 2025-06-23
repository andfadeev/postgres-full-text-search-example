(ns postgres-full-text-search-example.core
  (:gen-class)
  (:require [next.jdbc :as jdbc])
  (:import (org.testcontainers.containers PostgreSQLContainer)
           (org.testcontainers.utility DockerImageName MountableFile)))

(defn postgres-container
  []
  (let [container (-> (DockerImageName/parse "postgres:17-alpine")
                      (PostgreSQLContainer.)
                      (.withCopyFileToContainer
                        (MountableFile/forClasspathResource "database/init.sql")
                        "/docker-entrypoint-initdb.d/"))]
    (.start container)
    container))

(defn do-things!
  []
  (with-open [database-container (postgres-container)
              conn (jdbc/get-connection
                     {:jdbcUrl (.getJdbcUrl database-container)
                      :user (.getUsername database-container)
                      :password (.getPassword database-container)})]
    (jdbc/execute!
      conn
      ["select * from articles;"]
      jdbc/unqualified-snake-kebab-opts)))

(defn -main
  []
  (do-things!))
