# Autenticação

# Criar configurar e serviços
```
kubectl apply -f deploy/secret.yaml
kubectl apply -f deploy/mongo-deployment.yaml
kubectl apply -f deploy/autenticacao-api-deployment.yaml
```
## obter url da api - (autenticacao-api)

```
minikube service list
```

Com a url da api em mãos executar os curls abaixo. Foi criado um usuário default admin:admin heheh

```
{{URL_API}}/swagger-ui.html
```

kubectl get pods --show-labels

kubectl logs -f ID_POD

minikube service list
