## Kubernetes client application

# Installation requirements
    1. Maven
    2. Java 11
    3. Kubectl

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

You can import POSTMAN collection of API requests from ```K8S_client.postman_collection.json``` file

Requests examples. No authentication is required.

Create deployment, by providing namespace name and deployment params:
```#!bash
$ curl -X POST \
    http://localhost:8080/api/k8sclient/v1/namespace/default/deployment \
    -d '{
  	"name": "hello-world",
  	"image": "gcr.io/hello-minikube-zero-install/hello-node",
  	"replicasCount": "1",
  	"port": "8080"
  }'
```

Update deployment replica count:
```#!bash
$ curl -X PUT \
    http://localhost:8080/api/k8sclient/v1/namespace/default/deployment/hello-world \
    -d '{
  	"name": "hello-world",
  	"image": "gcr.io/hello-minikube-zero-install/hello-node",
  	"replicasCount": "2",
  	"port": "8080"
  }'
```

Delete deployment:
```#!bash
$ curl -X DELETE \
    http://localhost:8080/api/k8sclient/v1/namespace/default/deployment/hello-world
```

Get deployment by name:
```#!bash
$ curl -X GET \
    http://localhost:8080/api/k8sclient/v1/namespace/default/deployment/hello-world
```

Get namespace deployments:
```#!bash
$ curl -X GET \
    http://localhost:8080/api/k8sclient/v1/namespace/default/deployment
```

Frontend is available under 
[frontend](http://localhost:8080)

# H2 Database
H2 Database console is available under 
[console](http://localhost:8080/h2-console)
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: <empty>
