#!/bin/bash
set -e

PRIVATE_IP=$(ip route get 1.2.3.4 | awk '{print $7}' | head -n 1)
PUBLIC_IP=$(curl -s ifconfig.me)

echo "Installing Kubernetes"
#sudo curl -sfL https://get.k3s.io | INSTALL_K3S_EXEC="--write-kubeconfig-mode=644 --tls-san ${PUBLIC_IP}" sh -

echo "Installing Java"
sudo add-apt-repository ppa:openjdk-r/ppa -y
sudo apt update -y
sudo apt install -y openjdk-11-jdk

echo "Deploying Halyard in Kubernetes"
kubectl apply -f manifests/namespace.yml
kubectl apply -f manifests/halyard.yml

while [[ $(kubectl get statefulset -n spinnaker halyard -o jsonpath='{.status.readyReplicas}') -ne 1 ]];
do
  echo "Waiting for Halyard pod to start"
  sleep 5;
done

echo "Creating hal command"
sudo tee /usr/local/bin/hal <<-'EOF'
#!/bin/bash
POD_NAME=$(kubectl -n spinnaker get pod -l app=halyard -oname | cut -d'/' -f 2)
# echo $POD_NAME
set -x
kubectl -n spinnaker exec -i ${POD_NAME} -- sh -c "hal $*"
EOF
sudo chmod 755 /usr/local/bin/hal
