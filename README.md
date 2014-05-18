Simple Webserver
================
_Yaay!_

This is a simple webserver written in java. This is example code 
and while it does compile, it is not intended for production. 

It is intended to show by example how a webserver can function, when written in Java.

The project features the very foundation of a proper webserver, and thus it
should be easy to get started, and get going. 
Out of the box, it only responds to GET-requests, and it only serves a picture of a banana while doing so.


What's missing?
---------------
 * Proper request method acting. Currently, it finds out what request is being made, and a RequestHandler is chosen.
 * Nice headers. Currently, these are implemented in the RequestHandlers, but I suspect this is not the way to go.

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

