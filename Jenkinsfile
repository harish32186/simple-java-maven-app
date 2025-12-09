pipeline {
    agent any

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
                    ansible-playbook -i inventory ansible/deploy.yml --private-key "$SSH_KEY"
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
                    export KUBECONFIG="$KCFG"
                    kubectl apply -f project/tf/k8s/simple-app.yml
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
