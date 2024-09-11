# Client-Server-Library
This project implements a client-server library system using Java sockets. The system allows users to search for, check out, and return books via a graphical user interface (GUI) on the client side, while the server manages book inventory, user authentication, and transaction logging. The project utilizes multithreading to handle multiple concurrent clients and ensures robust error handling for reliability.

Client-Server Architecture: Uses Java sockets to establish communication between the client and server for real-time book searches and checkouts.

Graphical User Interface (GUI): A user-friendly client interface for performing library operations such as login, book search, and checkout.

Server-Side Management: Manages a catalog of books, handles user authentication, and logs transactions to ensure data integrity.

Multithreading: Supports multiple concurrent clients, ensuring that the system can scale and handle several requests simultaneously.

Persistent Storage: User accounts and book inventory data are stored persistently, allowing for continuity across sessions

How It Works
Client-Side:

The client connects to the server using Java sockets.
Users can log in, search for books, and check out or return items using the GUI.
Server-Side:

The server maintains a list of available books, user data, and transaction logs.
The server can handle requests from multiple clients simultaneously by using multithreading.
All data is stored persistently to ensure that changes made by one client are reflected for others.
Multithreading:

Each client request is handled in a separate thread, allowing for simultaneous interactions with the server.
The server ensures data consistency by using synchronized methods to manage shared resources like the book inventory.
