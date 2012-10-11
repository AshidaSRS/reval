# reval

This is a really simple implementation of remote evaluation in Clojure. The main
thing is the server, that listens to, evaluates, and returns the value of
incoming expressions, but a client is also provided.

The easiest way to use it is to build a standalone jar with leiningen*, so you
just need a JRE in order to run it anywhere you want.

* https://github.com/technomancy/leiningen

## Usage

From Clojure:

    Import ns'es and call functions individually :)

From anywhere else:

    $ lein uberjar
    $ java -jar target/reval-<version>-standalone.jar <port> [:local true]

which creates a new repl server and puts it to listen in the specified port.
If the :local flag is set to true, only connections from localhost will be
allowed. To stop the server you can politely send it a SIGTERM.

The protocol used by the server is good ol' TCP, so arguably the most simple way
to use it is with netcat:

Server:

    $ java -jar reval-<version>-standalone.jar 9999

Client:

    $ nc localhost 9999
    (range 10)              # client input
    (0 1 2 3 4 5 6 7 8 9)   # server response
    :quit                   # (better than CTRL-c)
    :quit                   # server is like -> :(
    $


* NOTE: Needless to say, there are serious security implications on letting the
world execute arbitrary code on your machine, so use it with :local true unless
you know what you're doing.

* NOTE 2: The server listens to and evaluates *Clojure s-expressions*, not lines.

* NOTE 3: Each client is assigned a shiny new namespace but, as they are on the
same VM, you can still use fully qualified symbols in order to access other
clients' data.

## License

Copyright © 2012 Guillermo Ramos Gutiérrez <0xwille-at-gmail-dot-com>

Distributed under the BSD-3 license
