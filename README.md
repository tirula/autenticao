# Autenticação

Esse projeto tem como objetivo disponibilzar um microserviço de autenticação de usuários.

- Operações de CRUD (create, retrieve, update, delete) para dados de um usuário de sistema (id, nome, e-mail, senha, endereço, telefone, perfil) 
- Os perfis de usuário aceitos são: ADMIN e USER 
- Operação de pesquisa de usuários, com filtros, paginação e ordenação dos dados. 
- Autenticação de usuários 
- Os recursos estão protegidos para que apenas usuários autenticados tenham acesso. 

## Tecnologias

- Java 11
- Springboot
- MongoDB
- Docker
- Kubernetes
- Swagger



# Build
```
docker build -t tirula/autenticao-api .
```

# Deploy
```
kubectl apply -f deploy/secret.yaml
kubectl apply -f deploy/mongo-deployment.yaml
kubectl apply -f deploy/autenticacao-api-deployment.yaml
```
## obter url da api - (autenticacao-api)

```
minikube service list
```


## Swagger

Com a url da api em mãos executar os curls abaixo. Foi criado um usuário default admin:admin heheh

```
{{URL_API}}/swagger-ui.html
```

No autorize tem que colocar o Bearer TOKEN

## curl - exempplo de login e inserir usuário

login 

```.env
curl --location --request POST 'http://localhost:8080/auth' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"admin",
    "password":"admin"
}'
```

inserir usuario

```.env
curl --location --request POST 'http://localhost:8080/usuarios' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYxNTgxMjU4Mn0.fhQLPKzTzQGAs9f4ehq5DBhvDLASmQqAnwTGwjRlFbKGcqgNF71ZHqImUbdYtTcMKs6mfnaxhoB_nyR07HNYkQ' \
--data-raw '{
    "username":"usuario_legal",
    "email":"legal@hotmail.com",
    "password":"legal123",
    "roles":["user"],
    "address":"Ilha da magia",
    "phoneNumber":"48999988979"
}'
```

# Aplicação em DEV

```.env
docker-compose up -d
```

Executar a classe floripa.autenticacao.backend.AutenticacaoApplication

## Mongo express

```.env
http://localhost:8081
```

## Ajuda dev para acompanhar logs

```.env
kubectl get pods --show-labels
kubectl logs -f ID_POD
minikube service list
```

```.env
kubectl delete -f deploy/secret.yaml
kubectl delete -f deploy/mongo-deployment.yaml
kubectl delete -f deploy/autenticacao-api-deployment.yaml

```