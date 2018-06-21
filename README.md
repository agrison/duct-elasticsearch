# Duct database.elasticsearch.spandex

[![Build Status](https://travis-ci.org/agrison/duct-elasticsearch.svg?branch=master)](https://travis-ci.org/agrison/duct-elasticsearch)

Integrant multimethods for connecting to [ElasticSearch][] via [Spandex][].

[elasticsearch]: http://elastic.co
[spandex]: https://github.com/mpenet/spandex

## Installation

To install, add the following to your project `:dependencies`:

    [me.grison/duct-elasticsearch "0.1.0"]

## Usage

This library provides two things:

* a `Boundary` record that holds both the Spandex client (`:client`) 
  and a sniffer (`:sniffer`) if asked for.
* a multimethod for `:duct.database.elasticsearch/spandex` that initiates 
  the client based on those options into the Boundary.

When you write functions against the ElasticSearch database, 
consider using a protocol and extending the Boundary record. 
This will allow you to easily mock or stub out the database 
using a tool like [Shrubbery][].

A `:logger` key may also be specified, which will be used to log when
the module connects to or disconnects from ElasticSearch. 
The value of the key should be an implementation of the 
`duct.logger/Logger` protocol from the [duct.logger][] library

[shrubbery]: https://github.com/bguthrie/shrubbery
[duct.logger]: https://github.com/duct-framework/logger

## Connection settings

### Client

```edn
{:duct.database.elasticsearch/spandex
 {:client {:hosts ["127.0.0.1" "192.168.0.234"]}}}
```

### Sniffer

```edn
{:duct.database.elasticsearch/spandex
 {:client {:hosts ["127.0.0.1" "192.168.0.234"]}}
  :sniffer? true
  :sniffer { ... }}
```

For more information about Spandex client options you can see their
[client documentation](https://mpenet.github.io/spandex/qbits.spandex.html#var-client)
and [sniffer documentation](https://mpenet.github.io/spandex/qbits.spandex.html#var-sniffer)

## Example

The ES REST client can be extracted from this module Boundary by using the `:client` key.

```clojure
(ns my-project.boundary.search-db
  (:require [duct.database.elasticsearch.spandex]
            [qbits.spandex :as s]))
            
(defprotocol EntryDatabase
  (search [db]))
  
(extend-protocol EntryDatabase
  duct.database.elasticsearch.spandex.Boundary
  (search [{:keys [client]}]
    (s/request client {:url "/entries/entry/_search"
                       :method :get
                       :body {:query {:match_all {}}}})))
```

## License

Copyright © 2018 Alexandre Grison

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
