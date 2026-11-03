
pipeline {
    agent {
        label 'agent1'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/nischalveenarudresh-pixel/sonarqube-java-demo.git'
            }
        }

        
stage('Sonar Analysis') {
    steps {
        withSonarQubeEnv('SonarQube-Server') {
            sh '''
                mvn sonar:sonar \
                  -Dsonar.projectKey=devops-java-app \
                  -Dsonar.projectName=DevOps-Java-App \
                  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
            '''
        }
    }
}


        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}

