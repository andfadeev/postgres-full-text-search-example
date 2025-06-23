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

(defn search-articles
  [search-query]
  (with-open [database-container (postgres-container)
              conn (jdbc/get-connection
                     {:jdbcUrl (.getJdbcUrl database-container)
                      :user (.getUsername database-container)
                      :password (.getPassword database-container)})]
    (jdbc/execute!
      conn
      ["SELECT id, title, subtitle, content, 
        ts_rank(search_vector, plainto_tsquery('english', ?)) AS rank
        FROM articles
        WHERE search_vector @@ plainto_tsquery('english', ?)
        ORDER BY rank DESC;" 
       search-query search-query]
      jdbc/unqualified-snake-kebab-opts)))

(defn -main
  []
  (search-articles "Cybersecurity"))
