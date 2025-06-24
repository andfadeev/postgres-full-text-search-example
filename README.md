# postgres-full-text-search-example

A simple example project demonstrating PostgreSQL full-text search capabilities with Clojure.

## Features

This project demonstrates two approaches to PostgreSQL full-text search:

1. Using pre-calculated search vectors stored in the database
2. Building search vectors dynamically in the query

## Usage

The project provides two main search functions:

### 1. search-articles

This function uses the pre-calculated `search_vector` column from the database:

```clojure
(search-articles "your search query")
```

### 2. search-articles-dynamic

This function builds the search vector dynamically in the query instead of using the pre-calculated column:

```clojure
(search-articles-dynamic "your search query")
```

Both functions return the same structure of results, but the dynamic version may be useful when:
- You need to search across columns not included in the pre-calculated vector
- You want to use different weights for different search contexts
- The search vector definition needs to change frequently

## Running the examples

You can run the example functions in a REPL:

```clojure
;; Get all articles
(get-articles-demo)

;; Search using pre-calculated search vector
(search-demo)

;; Search using dynamically built search vector
(search-demo-dynamic)

;; Demo of creating and updating articles
(create-update-demo)
```

## Implementation Details

The database schema creates a `search_vector` column that is automatically generated from the title, subtitle, and content columns with different weights:
- Title: Weight A (highest)
- Subtitle: Weight B (medium)
- Content: Weight C (lowest)

The dynamic search function builds the same vector structure directly in the query.

## License

Copyright © 2025 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
