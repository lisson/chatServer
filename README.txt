Description and Summary:

Academic Project for a simple telnet chat server written in Java using Eclipse IDE.

Makes use of java packages, threads, semaphores and singletons. Each thread will create go to sleep and wakesup when a client connects to it. It will sleep again when the client disconnects. This is implemented using threadpools therefore no threads are created or destroyed.

Messages are not encrypted because the primary goal of this was to demonstrate my understanding of threads.

Compiling: Import chatServer folder as Eclipse project and compile.

Usage: java chatServer/serverMain [port] [number of threads] [error|info|debug]

Client: telnet [IP] [port]

Written in 2009.

Author: Yi Li


