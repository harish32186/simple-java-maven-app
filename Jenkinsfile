pipeline {
    agent { label 'ubuntu' }

    environment {
        IMAGE_NAME = "harishchandra86/simple-java-maven-app"
        IMAGE_TAG  = "latest"
    }

    stages {

        stage('Build & Push Docker Image') {
            steps {
                dir('.') {
                    withCredentials([
                        usernamePassword(
                            credentialsId: '19b9a84d-4412-427f-ab0d-435f340b5974', 
                            usernameVariable: 'DOCKER_USER', 
                            passwordVariable: 'DOCKER_PASS'
                        )
                    ]) {
                        sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker build -t $IMAGE_NAME:$IMAGE_TAG .
                        docker push $IMAGE_NAME:$IMAGE_TAG
                        '''
                    }
                }
            }
        }

        stage('Trigger Ansible Deployment') {
            steps {
                withCredentials([
                    sshUserPrivateKey(
                        credentialsId: 'ansible-ssh-key',
                        keyFileVariable: 'SSH_KEY'
                    )
                ]) {
                    sh '''
                    export ANSIBLE_HOST_KEY_CHECKING=False
                    ansible-playbook -i ansible/deploy.yml --private-key "$SSH_KEY"
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([
                    file(
                        credentialsId: 'kubeconfig',
                        variable: 'KCFG'
                    )
                ]) {
                    sh '''
                    # Export KUBECONFIG
                    export KUBECONFIG=/home/ubuntu/.kube/config

                    # Fix Minikube cert permissions if using local Minikube
                    sudo chown -R ubuntu:ubuntu /home/ubuntu/.minikube /home/ubuntu/.kube
                    chmod 600 /home/ubuntu/.minikube/profiles/minikube/client.key
                    chmod 644 /home/ubuntu/.minikube/profiles/minikube/client.crt
                    chmod 644 /home/ubuntu/.minikube/ca.crt

                    # Apply YAML and check rollout
                    kubectl apply -f /home/ubuntu/project/tf/k8s/simple-app.yml --validate=false
                    kubectl rollout status deployment/simple-app
                    kubectl get pods -A
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed. Check logs for details."
        }
    }
}
