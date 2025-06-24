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

(defn get-articles-demo
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
      ["SELECT id, 
        ts_headline('english', title, plainto_tsquery('english', ?), 'StartSel=<b>, StopSel=</b>') AS title,
        ts_headline('english', subtitle, plainto_tsquery('english', ?), 'StartSel=<b>, StopSel=</b>') AS subtitle,
        content, 
        ts_rank(search_vector, plainto_tsquery('english', ?)) AS rank
        FROM articles
        WHERE search_vector @@ plainto_tsquery('english', ?)
        ORDER BY rank DESC;" 
       search-query search-query search-query search-query]
      jdbc/unqualified-snake-kebab-opts)))

(defn create-update-demo
  []
  (with-open [database-container (postgres-container)
              conn (jdbc/get-connection
                     {:jdbcUrl (.getJdbcUrl database-container)
                      :user (.getUsername database-container)
                      :password (.getPassword database-container)})]
    (let [new-article (jdbc/execute-one!
                        conn
                        ["INSERT INTO articles (title, subtitle, content) VALUES (?, ?, ?) RETURNING *;"
                         "Clojure and PostgreSQL"
                         "A powerful combination for data-driven applications"
                         "Clojure's immutable data structures and functional programming paradigm make it an excellent choice for building robust data-driven applications. When combined with PostgreSQL's advanced features like full-text search, the result is a highly capable and maintainable system."]
                        jdbc/unqualified-snake-kebab-opts)
          updated-article (jdbc/execute-one!
                            conn
                            ["UPDATE articles SET title = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ? RETURNING *;"
                             "Clojure and PostgreSQL: A Match Made in Heaven"
                             (:id new-article)]
                            jdbc/unqualified-snake-kebab-opts)]
      (println "New article created:")
      (clojure.pprint/pprint new-article)
      (println "Updated article:")
      (clojure.pprint/pprint updated-article))))

(defn search-articles-dynamic
  [search-query]
  (with-open [database-container (postgres-container)
              conn (jdbc/get-connection
                     {:jdbcUrl (.getJdbcUrl database-container)
                      :user (.getUsername database-container)
                      :password (.getPassword database-container)})]
    (jdbc/execute!
      conn
      ["SELECT id,
        title,
        subtitle
        content,
        ts_rank(
          setweight(to_tsvector('english', coalesce(title, '')), 'A') ||
          setweight(to_tsvector('english', coalesce(subtitle, '')), 'B') ||
          setweight(to_tsvector('english', coalesce(content, '')), 'C'),
          plainto_tsquery('english', ?)
        ) AS rank
        FROM articles
        WHERE 
          setweight(to_tsvector('english', coalesce(title, '')), 'A') ||
          setweight(to_tsvector('english', coalesce(subtitle, '')), 'B') ||
          setweight(to_tsvector('english', coalesce(content, '')), 'C') @@ plainto_tsquery('english', ?)
        ORDER BY rank DESC;" 
       search-query search-query]
      jdbc/unqualified-snake-kebab-opts)))

(defn search-demo
  []
  (let [results (search-articles "Future AI")]
    (println "Search results:")
    (clojure.pprint/pprint results)))

(defn search-demo-dynamic
  []
  (let [results (search-articles-dynamic "Future AI")]
    (println "Search results (dynamic):")
    (clojure.pprint/pprint results)))

(comment
  (get-articles-demo)
  (search-demo)
  (search-demo-dynamic)
  (create-update-demo))
