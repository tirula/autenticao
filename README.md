

eval $(minikube docker-env)

minikube start --vm-driver=hyperkit

kubectl apply -f deploy/secret.yaml
kubectl apply -f deploy/mongo-deployment.yaml
kubectl apply -f deploy/creativedrive-api-deployment.yaml




kubectl get pods --show-labels

kubectl logs -f ID_POD

minikube service list
