Simple Webserver
================
_Yaay!_

This is a simple webserver written in java. This is example code 
and while it does compile, it is not intended for production. 

It is intended to show by example how a webserver can function, when written in Java.

The project features the very foundation of a proper webserver, and thus it
should be easy to get started, and get going. 

What's missing?
---------------
 * Proper request method acting.
 * Nice headers in responses.

About resource consumption
--------------------------
This server uses the ExecutorService "cachedThreadPool". This will create a pool of threads to handle clients asynchronously, 
while not fragmenting memory. 
If the pool contains Threads idling for more than 60 seconds, these Threads will be disposed of. 
If the pool contains no Threads but it is needed, a new Thread will be added to the pool.
This solution allows the server to scale, as traffic increases. However, it will hog as many resources as it can come near.

A different approach is using the ExecutorService "fixedThreadPool", while specifying the max amount of Threads wanted. 
As trafic increases, so will be servers respond-time. It will however still handle client asynchronously, but with this solution it will not 
create more Threads, resulting in a more controlled server. 

Alternatively, the JVM running the software can be restricted on its own.

Links to good resources for further development
-----------------------------------------------
 * [Hypertext Transfer Protocol -- HTTP/1.1 Specification](http://www.w3.org/Protocols/rfc2616/rfc2616.html)

