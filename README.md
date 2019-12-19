## Kubernetes client application

# Installation requirements
    1. Maven
    2. Java 11

# Installation and Run 
Use target directory to run built sources
```#!bash
$ ./run.sh 
```

# Usage 
You need an installed and properly configured ***kubectl*** tool to get access to your K8S cluster.
Check access with provided command:
```#!bash
$ kubectl get svc
NAME         TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)   AGE
kubernetes   ClusterIP   10.100.0.1   <none>        443/TCP   145d
```

You can import POSTMAN collection of API requests from ```K8sClient.postman_collection.json``` file

Frontend is available under 
[frontend](http://localhost:8080)
