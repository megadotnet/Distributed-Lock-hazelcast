# Springboot hazelcast distributed lock
A SpringBoot project about using hazelcast distributed lock ti sequence http requests

## Dependency
1. Mysql Database version 5.7 or later
2. Hazelcast binary version 3.10 or later

## Scenario introduction
### Scenario 1 Concurrent without lock
1. Run web application with one instance
2. Run Load Test 'LoadTestNoLock.loadtest' in VisualStudio. It will request /api/insertWithoutLock with 20 of concurrent users.
3. Get error Duplicate entry exception logs like this:


     com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry '1416' for key 'UK_q3gdsul68avvqolqifk2go6dw'


### Scenario 2 Concurrent on distributed application with lock (synchronized method)
1. Run two instance web application with Nginx. We intent to construct simple distributed application. The better solution using Docker container.
   Nginx config file like this:
   	
        upstream tomcatserver1 
   	   {  
          server 127.0.0.1:8091;  
   		  server 127.0.0.1:8092;  
        }  
        server {
            listen       8090;
            server_name  localhost;
  
            location / {
   	 	    proxy_pass  http://tomcatserver1;
                root   html;
                index  index.html index.htm;
            }
    
            error_page   500 502 503 504  /50x.html;
            location = /50x.html {
                root   html;
            }
    
        }
2. Run  Load Test 'LoadTestLSyncLock.loadtest' in VisualStudio. It will request /api/insertWithSyncLock with 20 of concurrent users.
3. Get error Duplicate entry exception logs like this:


     com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry '7327' for key 'UK_q3gdsul68avvqolqifk2go6dw'

     
### Scenario 3  Concurrent on distributed application  with distributed lock 
1. Run two instance web application with Nginx. 
2. Run  Load Test 'LoadTestLock.loadtest' in VisualStudio. It will request /api/insertWithLock with 20 of concurrent users.
3. We do not get any exception with concurrent.

## Testing tools
  Visual Studio Web performance and Load Test 2017 
     
  Refer 
      
      https://docs.microsoft.com/en-us/visualstudio/test/quickstart-create-a-load-test-project?view=vs-2017
  
##More 
   
  Refer
  
      https://docs.hazelcast.org/docs/latest-development/manual/html/Distributed_Data_Structures/Lock.html