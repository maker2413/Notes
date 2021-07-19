#!/bin/bash
format_manifests () {
    for f in ${BASE_DIR}/templates/*; do        
        sed -i \
            -e "s|NAMESPACE|${NAMESPACE}|g" \
            -e "s|MINIO_PASSWORD|${MINIO_PASSWORD}|g" \
            -e "s|BASE_DIR|${BASE_DIR}|g" \
            ${f}
    done
}

set -e

BASE_DIR=/etc/spinnaker
NAMESPACE=spinnaker
PRIVATE_IP=$(ip route get 1.2.3.4 | awk '{print $7}' | head -n 1)
PUBLIC_IP=$(curl -s ifconfig.me)

echo "Installing Kubernetes"
sudo curl -sfL https://get.k3s.io | INSTALL_K3S_EXEC="--write-kubeconfig-mode=644 --tls-san ${PUBLIC_IP}" sh -
sudo kubectl config set-context --current --namespace=${NAMESPACE}

echo "Installing Java"
sudo add-apt-repository ppa:openjdk-r/ppa -y
sudo apt update -y
sudo apt install -y openjdk-11-jdk

echo "Creating Spinnaker Directory"
sudo mkdir -p ${BASE_DIR}/manifests
sudo mkdir -p ${BASE_DIR}/.kube
sudo mkdir -p ${BASE_DIR}/.hal/.secret
sudo mkdir -p ${BASE_DIR}/.hal/default/{profiles,service-settings}
sudo chown -R 1000 ${BASE_DIR}

echo "Generate MinIO Password"
openssl rand -base64 36 | tee ${BASE_DIR}/.hal/.secret/minio_password
MINIO_PASSWORD=`cat ${BASE_DIR}/.hal/.secret/minio_password`

echo "Copy Configs to Spinnaker Directory"
cp -rpv manifests ${BASE_DIR}/templates/
cp -rpv profiles ${BASE_DIR}/.hal/default/
# cp -rpv service-settings ${BASE_DIR}/templates/
# cp config ${BASE_DIR}/templates/
format_manifests

echo "Deploying Halyard in Kubernetes"
kubectl apply -f ${BASE_DIR}/templates/namespace.yml
kubectl apply -f ${BASE_DIR}/templates/halyard.yml

while [[ $(kubectl get statefulset halyard -o jsonpath='{.status.readyReplicas}') -ne 1 ]];
do
  echo "Waiting for Halyard pod to start"
  sleep 5;
done

echo "Creating hal Command"
sudo tee /usr/local/bin/hal <<-'EOF'
#!/bin/bash
POD_NAME=$(kubectl get pod -l app=halyard -oname | cut -d'/' -f 2)
# echo $POD_NAME
set -x
kubectl -n spinnaker exec -i ${POD_NAME} -- sh -c "hal $*"
EOF
sudo chmod 755 /usr/local/bin/hal

echo "Set up Kube Config"
sudo sed -i -e "s|127.0.0.1|${PRIVATE_IP}|g" /etc/rancher/k3s/k3s.yaml
cp /etc/rancher/k3s/k3s.yaml ${BASE_DIR}/.kube/config
sudo chown -R 1000 ${BASE_DIR}

echo "Creating Endpoint file"
if [[ ! -s ${BASE_DIR}/.hal/public_endpoint ]]; then
  echo "Using provided public IP ${PUBLIC_IP}"
  echo "${PUBLIC_IP}" > ${BASE_DIR}/.hal/public_endpoint
else
  echo "Using existing Public IP from ${BASE_DIR}/.hal/public_endpoint"
  cat ${BASE_DIR}/.hal/public_endpoint
fi

echo "Deploy MinIO"
kubectl apply -f ${BASE_DIR}/templates/minio.yml

echo "Configuring Halyard"
hal config provider kubernetes enable
hal config provider kubernetes account add default
hal config storage s3 edit --endpoint "http://minio.$NAMESPACE:9000" --bucket $NAMESPACE --access-key-id minio --secret-access-key $MINIO_PASSWORD --path-style-access true
hal config storage edit --type s3
hal config version edit --version 1.26.4
hal config deploy edit --type distributed --account-name default

echo "Configure URL"
hal config security ui edit --override-base-url http://${PRIVATE_IP}:9000
hal config security api edit --override-base-url http://${PRIVATE_IP}:8084

echo "Deploy Spinnaker"
hal deploy apply
KUBE_EDITOR='sed -i "s/ClusterIP/LoadBalancer/g"' kubectl edit svc spin-deck
KUBE_EDITOR='sed -i "s/ClusterIP/LoadBalancer/g"' kubectl edit svc spin-gate

echo "Here is the kube config you will use to access your k3s cluster"
cat ${BASE_DIR}/.kube/config

echo "Spinnaker is now starting up. Please wait for all pods to be ready:"
watch -tc 'echo "Spinnaker is now starting up. Please wait for all pods to be ready:" && echo "" && kubectl get pods'
