## OOP Exercise: Implement a call center using Java. The call center has 3 types of representatives: EMPLOYEE, SUPERVISOR, MANAGER

There can only be 1 SUPERVISOR and 1 MANAGER at any time but unlimited number of employees.
When the call is received it should be forwarded to EMPLOYEE. If EMPLOYEE is either busy or cannot handle the call it should be forwarded to SUPERVISOR. If SUPERVISOR is busy or cannot handle the call it should be forwarded to MANAGER.
Considered concurrency by using Runnable interface and creating new Threads.

Example of Supervisor picking up a call if Employee couldn't handle it

![logs](https://i.imgur.com/PMRccav.png)
